import express from "express";
import { createProxyMiddleware } from "http-proxy-middleware";
import cors from 'cors';

const app = express();
const PORT = 3000;

app.use(cors());

// Nếu muốn giới hạn origin:
app.use(cors({
	origin: '*',
	methods: ['GET', 'POST', 'PUT', 'DELETE'],
	credentials: true
}));

// Proxy config
app.use(
	"/api/user",
	createProxyMiddleware({
		target: "http://user-service:3006",
		changeOrigin: true,
	})
);

// Health check
app.get("/", (_req, res) => {
	res.send("API Gateway is running...");
});

app.listen(PORT, () => {
	console.log(`API Gateway listening on port ${PORT}`);
});
