import axios, { AxiosRequestConfig, AxiosResponse } from "axios";
import axiosRetry from "axios-retry";
import Bottleneck from "bottleneck";
import CircuitBreaker from "opossum";

// Limiter: giới hạn concurrent requests
const limiter = new Bottleneck({
	maxConcurrent: 5,
	minTime: 200,
	reservoir: 10, // chỉ cho tổng cộng 10 request trong 1 chu kì
	reservoirRefreshAmount: 10,// reset thì nạp lại 10 request cho reservoir
	reservoirRefreshInterval: 60000 // Thời gian refresh reservoir
});

// Khi có request bị queued
limiter.on("queued", (info) => {
	console.warn("🚨 Request queued! Rate limit reached or concurrency full!");
	console.log("Current queued jobs:", limiter.jobs("QUEUED").length);
});

// Khi tất cả request hoàn thành
limiter.on("idle", () => {
	console.info("✅ Limiter idle - no pending request.");
});

// Breaker config
const circuitBreakerOptions: CircuitBreaker.Options = {
	timeout: 10000,
	errorThresholdPercentage: 66,
	resetTimeout: 10000,
	rollingCountTimeout: 10000,
};

const axiosInstance = axios.create({
	baseURL: "http://product-service:3006",
	timeout: 3000,
});

// Retry: tự động gửi lại khi lỗi mạng, lỗi server
axiosRetry(axiosInstance, {
	retries: 3,
	retryDelay: axiosRetry.exponentialDelay,
	retryCondition: (error) => {
		return axiosRetry.isNetworkOrIdempotentRequestError(error);
	},
});

class ProductClient {
	private accessToken: string = "";
	private breaker: CircuitBreaker<any>;

	constructor() {
		this.breaker = new CircuitBreaker(
			(config: AxiosRequestConfig) => axiosInstance.request(config),
			circuitBreakerOptions
		);

		// Log trạng thái breaker
		this.breaker.on("open", () => console.warn("🚨 Breaker opened"));
		this.breaker.on("halfOpen", () => console.info("⏳ Breaker half-open"));
		this.breaker.on("close", () => console.info("✅ Breaker closed"));
	}

	public setAccessToken(token: string) {
		this.accessToken = token;
	}

	public getBreakerStatus(): string {
		if (this.breaker.opened) return "OPEN";
		if (this.breaker.halfOpen) return "HALF-OPEN";
		return "CLOSED";
	}

	private logBreakerStats() {
		const stats = this.breaker.stats;
		const status = this.getBreakerStatus();
		console.log(`
----------------------------
📊 Circuit Breaker Report:
- Total Requests: ${stats.fires}
- Successes: ${stats.successes}
- Failures: ${stats.failures}
- Current Status: ${status}
----------------------------
		`);
	}

	private async protectedRequest<T>(
		url: string,
		options?: AxiosRequestConfig
	): Promise<AxiosResponse<T>> {
		try {
			const fullOptions: AxiosRequestConfig = {
				url,
				headers: {
					Authorization: this.accessToken,
					...(options?.headers || {}),
				},
				...options,
			};

			const result = await limiter.schedule(() =>
				this.breaker.fire(fullOptions)
			);
			this.logBreakerStats();
			return result;
		} catch (error) {
			this.logBreakerStats();
			throw error;
		}
	}

	public async getProductById(productId: string): Promise<any> {
		if (!productId) {
			throw new Error("Product ID is required");
		}

		const response = await this.protectedRequest<any>(`/${productId}`, {
			method: "GET",
		});

		const { errorCode, errorMessage, data } = response.data;

		if (errorCode !== 200) {
			console.error("Error from product service:", errorMessage);
			throw new Error(errorMessage);
		}

		return data;
	}
}

export default new ProductClient();
