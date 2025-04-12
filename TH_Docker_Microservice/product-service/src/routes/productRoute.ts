import express from "express";
import productService from "../services/productService";
import { IProduct } from "../models/Product";


const router = express.Router();

router.post("/", (req: any, res: any) => {
    const { name, price, description, inStock } = req.body;
    if (!name || !price || !description || !inStock) {
        return res.status(400).json({
            errorCode: 400,
            errorMessage: "Missing required fields",
            data: null,
        });
    }
    
    try {
        
        const newProduct = {
            name,
            price,
            description,
            inStock,
        } as IProduct;
        productService.createProduct(newProduct).then((product: IProduct) => {
            return res.status(201).json({
				errorCode: 201,
				errorMessage: "Product created successfully",
				data: {
					...product.toObject(),
                    _id: product._id,
				},
			});
        }
        ).catch((error: any) => {
            return res.status(500).json({
                errorCode: 500,
                errorMessage: "Internal server error",
                data: null,
            });
        });
    } catch (error) {
        return res.status(500).json({
            errorCode: 500,
            errorMessage: "Internal server error",
            data: null,
        });
    }
});

router.get("/", (req: any, res: any) => {
    productService.getAllProducts().then((products: IProduct[]) => {
        return res.status(200).json({
            errorCode: 200,
            errorMessage: "Get all products successfully",
            data: products,
        });
    }).catch((error: any) => {
        return res.status(500).json({
            errorCode: 500,
            errorMessage: "Internal server error",
            data: null,
        });
    });
});

// :id
router.get("/:id", (req: any, res: any) => {
    productService.getProductById(req.params.id).then((product: IProduct | null) => {
        if (!product) {
            return res.status(200).json({
                errorCode: 404,
                errorMessage: "Product not found",
                data: null,
            });
        }
        return res.status(200).json({
            errorCode: 200,
            errorMessage: "Get product successfully",
            data: product,
        });
    }
    ).catch((error: any) => {
        return res.status(200).json({
            errorCode: 200,
            errorMessage: "Internal server error",
            data: null,
        });
    });
})

// update id
router.put("/:id", (req: any, res: any) => {
    const { name, price, description, inStock } = req.body;
    if (!name || !price || !description || !inStock) {
        return res.status(200).json({
            errorCode: 400,
            errorMessage: "Missing required fields",
            data: null,
        });
    }
    productService.updateProduct(req.params.id, req.body).then((product: IProduct | null) => {
        if (!product) {
            return res.status(200).json({
                errorCode: 404,
                errorMessage: "Product not found",
                data: null,
            });
        }
        return res.status(200).json({
            errorCode: 200,
            errorMessage: "Update product successfully",
            data: product,
        });
    }).catch((error: any) => {
        return res.status(200).json({
            errorCode: 500,
            errorMessage: "Internal server error",
            data: null,
        });
    });
});

// delete
router.delete("/:id", (req: any, res: any) => {
    productService.deleteProduct(req.params.id).then((product: IProduct | null) => {
        if (!product) {
            return res.status(404).json({
                errorCode: 404,
                errorMessage: "Product not found",
                data: null,
            });
        }
        return res.status(200).json({
            errorCode: 200,
            errorMessage: "Delete product successfully",
            data: product,
        });
    }).catch((error: any) => {
        return res.status(500).json({
            errorCode: 500,
            errorMessage: "Internal server error",
            data: null,
        });
    });
});

export default router;
