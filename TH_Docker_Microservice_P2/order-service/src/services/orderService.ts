import { Types } from 'mongoose';
import OrderModel, { IOrder } from "../models/Order";

class OrderService {
    async createOrder(order: IOrder): Promise<IOrder> {
        const newOrder = new OrderModel(order);
        return await newOrder.save();
    }

    async getAllOrders(): Promise<IOrder[]> {
        return await OrderModel.find();
    }
    async getOrderById(id: string): Promise<IOrder | null> {
        if (!Types.ObjectId.isValid(id)) {
            throw new Error('Invalid Order ID');
        }
        return await OrderModel.findById(id);
    }

    async updateOrder(id: string, order: IOrder): Promise<IOrder | null> {
        if (!Types.ObjectId.isValid(id)) {
            throw new Error('Invalid Order ID');
        }
        return await OrderModel.findByIdAndUpdate(id, order, { new: true });
    }

    async deleteOrder(id: string): Promise<IOrder | null> {
        if (!Types.ObjectId.isValid(id)) {
            throw new Error('Invalid Order ID');
        }
        return await OrderModel.findByIdAndDelete(id);
    }
}

export default new OrderService();