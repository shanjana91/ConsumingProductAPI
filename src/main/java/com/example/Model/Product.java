package com.example.Model;

import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {
	
	@SerializedName("id")
	private int id;
	
	@SerializedName("name")
	private String prod_name;
	
	@SerializedName("price")
	private int price;
	
	@SerializedName("quantity")
	private int quantity;
	
	
	
}
