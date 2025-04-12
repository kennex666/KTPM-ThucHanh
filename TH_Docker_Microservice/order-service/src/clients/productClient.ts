import axios from "axios";


class ProductClient {
    async getProductById(productId: string) {
        const response = await axios.get(`http://localhost:3006/${productId}`);
        
        if (response.data.errorCode != 200){
            throw new Error(response.data.errorMessage);
        }
        return response.data.data;
    }
}

export default new ProductClient();