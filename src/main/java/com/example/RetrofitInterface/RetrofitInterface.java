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
	
	// calling from localhost:8081/springbootwar/ --> PRODUCT API deployed as war in tomcat container
	@GET("springbootwar/getproducts")
	public Single<List<Product>> getproducts(); 
	
	@GET("springbootwar/getById/{id}")
	public Single<Product> getProductById(@Path("id") int id);
	
	@POST("springbootwar/create")
	public Single<Product> createProduct(@Body Product product);
	
	
	@DELETE("springbootwar/delete/{id}")
	public Single<DeleteResponseModel> deleteProduct(@Path("id") int id);
	
}
