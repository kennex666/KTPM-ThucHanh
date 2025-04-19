const jwt = require("jsonwebtoken");

export const signToken = (payload: any) => {
	return jwt.sign(payload, "key_bao_mat", {
		expiresIn: "1h",
	});
};

export const verifyToken = (token: any) => {
	return jwt.verify(token, "key_bao_mat");
};

