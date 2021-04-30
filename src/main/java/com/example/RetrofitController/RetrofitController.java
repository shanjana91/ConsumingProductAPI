package com.example.RetrofitController;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.Model.DeleteResponseModel;
import com.example.Model.GetAllProductsResponseModel;
import com.example.Model.Product;
import com.example.Model.ResponseModel;
import com.example.service.RetrofitService;

import io.reactivex.Single;
import io.swagger.annotations.ApiOperation;


//exposing retrofit client as endpoints

@RestController
public class RetrofitController {

	@Autowired
	RetrofitService service;
	
	@ApiOperation(value="Get Products")
	@GetMapping("/prod")
	public Single<GetAllProductsResponseModel> getProducts() throws IOException{
		return service.getProducts();
	}
	
	@ApiOperation(value="Get product By ID")
	@GetMapping("/prod/{id}")
	public Single<ResponseModel> getProdById(@PathVariable("id") int id) throws IOException {
		return service.getProductsById(id);
	}
	
	
	@ApiOperation(value="Insert a Product Record")
	@PostMapping("/createProd")
	public Single<ResponseModel> createProd(@RequestBody Product prod) throws IOException {
		return service.createProducts(prod);
	}
	
	@ApiOperation(value="Delete a Product Record")
	@DeleteMapping("/deleteProd/{id}")
	public Single<DeleteResponseModel> deleteProd(@PathVariable("id") int id) throws IOException {
		return service.deleteProducts(id);
	}
}
