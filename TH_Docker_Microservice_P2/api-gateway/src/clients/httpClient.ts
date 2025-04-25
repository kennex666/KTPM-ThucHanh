import axios, { AxiosRequestConfig, AxiosResponse } from "axios";
import axiosRetry from "axios-retry";
import Bottleneck from "bottleneck";
import CircuitBreaker from "opossum";

// Tạo một axios instance
const instance = axios.create({
	timeout: 3000, // Time limiter: 3s timeout cho mỗi request
});

// Retry config
axiosRetry(instance, {
	retries: 3,
	retryDelay: axiosRetry.exponentialDelay,
	retryCondition: (error) => {
		return (
			axiosRetry.isNetworkError(error) ||
			axiosRetry.isRetryableError(error)
		);
	},
});

// Rate limiter config
const limiter = new Bottleneck({
	maxConcurrent: 5,
	minTime: 200,
});

// Circuit breaker config
const breakerOptions: CircuitBreaker.Options = {
	timeout: 5000,
	errorThresholdPercentage: 50,
	resetTimeout: 10000,
};

// Hàm gọi với Circuit Breaker
const callWithBreaker = (
	url: string,
	options: AxiosRequestConfig = {}
): Promise<AxiosResponse<any>> => {
	const breaker = new CircuitBreaker<AxiosResponse<any>>(
		() => instance.request({ url, ...options }),
		breakerOptions
	);

	breaker.fallback(() => {
		console.log(`⚡ Fallback activated for ${url}`);
		// Trả về dạng giống AxiosResponse tối giản khi fallback
		return {
			data: null,
			status: 503,
			statusText: "Service Unavailable",
			headers: {},
			config: options,
		} as AxiosResponse;
	});

	return breaker.fire();
};

// Hàm chính để Gateway gọi service
export const callService = async (
	url: string,
	options: AxiosRequestConfig = {}
): Promise<AxiosResponse<any>> => {
	return limiter.schedule(() => callWithBreaker(url, options));
};
