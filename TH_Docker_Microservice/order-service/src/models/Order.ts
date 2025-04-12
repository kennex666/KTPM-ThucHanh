import { Schema, model, Document } from "mongoose";

interface IOrder extends Document {
	products: Object[]; // Mảng chứa các sản phẩm trong đơn hàng
	customerId: string;
	createdAt: Date;
	updatedAt: Date;
}
// Định nghĩa Schema
const OrderSchema = new Schema<IOrder>({
		products: {
			type: [Object],
			required: true,
		},
		customerId: {
			type: String,
			required: true,
		},
	},
	{
		timestamps: true, // Tự động thêm createdAt & updatedAt
	}
);

// Tạo Model từ Schema
const OrderModel = model<IOrder>("Order", OrderSchema);

export default OrderModel;
export { IOrder };
