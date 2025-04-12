import { Types } from 'mongoose';
import ProductModel, { IProduct } from "../models/Product";

class ProductService {
    async createProduct(product: IProduct): Promise<IProduct> {
        const newProduct = new ProductModel(product);
        return await newProduct.save();
    }

    async getAllProducts(): Promise<IProduct[]> {
        return await ProductModel.find();
    }
    async getProductById(id: string): Promise<IProduct | null> {
        if (!Types.ObjectId.isValid(id)) {
            throw new Error('Invalid product ID');
        }
        return await ProductModel.findById(id);
    }

    async updateProduct(id: string, product: IProduct): Promise<IProduct | null> {
        if (!Types.ObjectId.isValid(id)) {
            throw new Error('Invalid product ID');
        }
        return await ProductModel.findByIdAndUpdate(id, product, { new: true });
    }

    async deleteProduct(id: string): Promise<IProduct | null> {
        if (!Types.ObjectId.isValid(id)) {
            throw new Error('Invalid product ID');
        }
        return await ProductModel.findByIdAndDelete(id);
    }
}

export default new ProductService();