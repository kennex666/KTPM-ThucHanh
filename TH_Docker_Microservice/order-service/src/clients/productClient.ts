import axios from "axios";


class ProductClient {
    async getProductById(productId: string) {
        const response = await axios.get(
			`http://api-gateway:3000/api/product/${productId}`,
			{
				headers: {
					Authorization: `Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VybmFtZSI6ImJhby10ZXN0IiwiaWF0IjoxNzQ1MDMzMjQ2LCJleHAiOjE3NDUwMzY4NDZ9.v8joc-dp7vfkpmakraBDSshiDImCX7BY-y0dzMLWp0Y`,
				},
			}
		);
        
        if (response.data.errorCode != 200){
            console.log("Error from product service:", response.data.errorMessage);
            throw new Error(response.data.errorMessage);
        }
        return response.data.data;
    }
}

export default new ProductClient();