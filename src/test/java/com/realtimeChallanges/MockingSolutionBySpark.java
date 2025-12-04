package com.realtimeChallanges;

import org.apache.commons.lang3.StringUtils;

import com.jayway.jsonpath.JsonPath;

import spark.Spark;

public class MockingSolutionBySpark {
	public static void main(String[] args) {
		Spark.port(8889);
		Spark.post("/credit-card", (req,res)->{
			String response = "";
			String cardNo = JsonPath.read(req.body().toString(), "$.creditcard");
			if (StringUtils.equalsAny(cardNo, "1234567890","1234567891","1234567892")) {
				response = "{\r\n"
						+ "\"status\" : \"payment is done\"\r\n"
						+ "}";
				res.status(200);
			}else {
				response="{\r\n"
						+ "\"status\" : \"payment is done\"\r\n"
						+ "}";
				res.status(404);
			}
			res.type("application/json");
			return response;
		});
		System.out.println("========Mock is Running...=======");
	}
}
