import express from "express";
import orderService from "../services/orderService";
import { IOrder } from "../models/Order";
import productClient from "../clients/productClient";


const router = express.Router();

router.post("/", (req: any, res: any) => {
    const { customerId, products } = req.body;

    if (!customerId || !products) {
        return res.status(400).json({
            errorCode: 400,
            errorMessage: "Missing required fields",
            data: null,
        });
    }

    try {
        const newOrder = {
            customerId,
            products,
        } as IOrder;

        orderService.createOrder(newOrder).then((order: IOrder) => {
            return res.status(200).json({
                errorCode: 201,
                errorMessage: "Order created successfully",
                data: {
                    ...order.toObject(),
                    _id: order._id,
                },
            });
        }).catch((error: any) => {
            return res.status(200).json({
                errorCode: 500,
                errorMessage: "Internal server error",
                data: null,
            });
        });
    } catch (error) {
        return res.status(200).json({
            errorCode: 500,
            errorMessage: "Internal server error",
            data: null,
        });
    }
})

router.get("/", (req: any, res: any) => {
    orderService.getAllOrders().then((orders: IOrder[]) => {
        return res.status(200).json({
            errorCode: 200,
            errorMessage: "Get all orders successfully",
            data: orders,
        });
    }).catch((error: any) => {
        return res.status(200).json({
            errorCode: 500,
            errorMessage: "Internal server error",
            data: null,
        });
    });
})

router.get("/:id", (req: any, res: any) => {
    const { id } = req.params;

    orderService.getOrderById(id).then(async (order: IOrder | null) => {
        if (!order) {
            return res.status(200).json({
                errorCode: 404,
                errorMessage: "Order not found",
                data: null,
            });
        }

        order.products = await Promise.all(order.products.map( async (product: any) => {
            const metadata = await productClient.getProductById(product.productId);
            console.log(metadata);
            if (metadata) {
                product = {...metadata, ...product};
            }
            return product;
        }) as any);
        return res.status(200).json({
            errorCode: 200,
            errorMessage: "Get order successfully",
            data: order,
        });
    }).catch((error: any) => {
        return res.status(200).json({
            errorCode: 500,
            errorMessage: "Internal server error",
            data: null,
        });
    });
})

router.put("/:id", (req: any, res: any) => {
    const { id } = req.params;
    const { customerId, products } = req.body;

    if (!customerId || !products) {
        return res.status(200).json({
            errorCode: 400,
            errorMessage: "Missing required fields",
            data: null,
        });
    }

    try {
        const updatedOrder = {
            customerId,
            products,
        } as IOrder;

        orderService.updateOrder(id, updatedOrder).then((order: IOrder | null) => {
            if (!order) {
                return res.status(200).json({
                    errorCode: 404,
                    errorMessage: "Order not found",
                    data: null,
                });
            }
            return res.status(200).json({
                errorCode: 200,
                errorMessage: "Order updated successfully",
                data: order,
            });
        }).catch((error: any) => {
            return res.status(200).json({
                errorCode: 500,
                errorMessage: "Internal server error",
                data: null,
            });
        });
    } catch (error) {
        return res.status(200).json({
            errorCode: 500,
            errorMessage: "Internal server error",
            data: null,
        });
    }
})

router.delete("/:id", (req: any, res: any) => {
    const { id } = req.params;

    orderService.deleteOrder(id).then((order: IOrder | null) => {
        if (!order) {
            return res.status(200).json({
                errorCode: 404,
                errorMessage: "Order not found",
                data: null,
            });
        }
        return res.status(200).json({
            errorCode: 200,
            errorMessage: "Order deleted successfully",
            data: order,
        });
    }).catch((error: any) => {
        return res.status(200).json({
            errorCode: 500,
            errorMessage: "Internal server error",
            data: null,
        });
    });
})
export default router;
