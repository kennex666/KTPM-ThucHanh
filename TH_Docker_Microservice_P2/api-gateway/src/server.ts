import express from "express";
import { createProxyMiddleware } from "http-proxy-middleware";
import cors from 'cors';
import { signToken, verifyToken } from "./services/jwt";

const app = express();
const PORT = 3000;

app.use(cors());

const jwtMiddleware = (req: any, res: any, next: any) => {
	const authHeader = req.headers.authorization;

	if (!authHeader || !authHeader.startsWith('Bearer ')) {
		return res.status(401).json({ message: 'Thiếu hoặc sai định dạng token' });
	}

	const token = authHeader.split(' ')[1];

	if (!token) {
		return res.status(401).json({ errorCode: 401, errorMessage: "Unauthorized" });
	}
	try {
		const decoded = verifyToken(token);
		if (!decoded) {
			return res.status(401).json({ errorCode: 401, errorMessage: "Unauthorized" });
		}
		req.user = decoded;
		next();
	} catch (error) {
		console.log("Error verifying token:", error);
		return res.status(401).json({ errorCode: 401, errorMessage: "Invalid token" });
	}
}

// Nếu muốn giới hạn origin:
app.use(cors({
	origin: '*',
	methods: ['GET', 'POST', 'PUT', 'DELETE'],
	credentials: true
}));

// Proxy config
app.use(
	"/api/product",
	jwtMiddleware,
	createProxyMiddleware({
		target: "http://product-service:3006",
		changeOrigin: true,
	})
);


app.use(
	"/api/order",
	jwtMiddleware,
	createProxyMiddleware({
		target: "http://order-service:3007",
		changeOrigin: true,
	})
);

// Health check
app.get("/", (_req, res) => {
	res.send("API Gateway is running...");
});

app.get("/login", (_req: any, res: any) => {
	// mockup login 
	const token = signToken({ username: "bao-test" });
	res.json({
		errorCode: 200,
		errorMessage: "Login success!",
		data: {
			access_token: token,
		},
	});
});

app.listen(PORT, () => {
	console.log(`API Gateway listening on port ${PORT}`);
});
