import { Schema, model, Document } from "mongoose";

interface IProduct extends Document {
	name: string;
	price: number;
	description: string;
	inStock: number;
	createdAt: Date;
	updatedAt: Date;
}
// Định nghĩa Schema
const ProductSchema = new Schema<IProduct>(
	{
		name: {
			type: String,
			required: [true, "name is required!"],
			minlength: [1, "Name must be at least 1 characters"],
		},
		price: {
			type: Number,
			required: [true, "price is required!"],
			min: [0, "Price must be at least 0"],
		},
		description: {
			type: String,
			required: [true, "description is required!"],
			minlength: [1, "Description must be at least 1 characters"],
		},
		inStock: {
			type: Number,
			required: [true, "inStock is required!"],
			min: [0, "In stock must be at least 0"],
		},
	},
	{
		timestamps: true, // Tự động thêm createdAt & updatedAt
	}
);

// Tạo Model từ Schema
const ProductModel = model<IProduct>("Product", ProductSchema);

export default ProductModel;
export { IProduct };
