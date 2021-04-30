package com.example.RetrofitInterface;

import java.util.List;

import com.example.Model.DeleteResponseModel;
import com.example.Model.Product;

import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;


//Interface representing my API

public interface RetrofitInterface {
	
	@GET("/getproducts")
	public Single<List<Product>> getproducts(); 
	
	@GET("/getById/{id}")
	public Single<Product> getProductById(@Path("id") int id);
	
	@POST("/create")
	public Single<Product> createProduct(@Body Product product);
	
	
	@DELETE("/delete/{id}")
	public Single<DeleteResponseModel> deleteProduct(@Path("id") int id);
	
}
