package com.example.Model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GetAllProductsResponseModel {
	private List<Product> products;
	private int code;
	private String message;
}
