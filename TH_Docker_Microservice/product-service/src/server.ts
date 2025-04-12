import express from "express";
import dotenv from "dotenv";
import connectDB from "./config/database";
import productRoutes from "./routes/productRoute";

dotenv.config();
const app = express();

app.use(express.json()); // Middleware xử lý JSON


app.get("/ping", (req, res) => {
	res.json({
		errorCode: 200,
		errorMessage: "Pong",
		data: null,
	});
});

app.use("/", productRoutes);
// Kết nối DB và chạy server
const PORT = process.env.PORT || 3006;
connectDB().then(() => {
	app.listen(PORT, () => console.log(`Server running on port ${PORT}`));
});
