package com.example.service;

import java.io.EOFException;
import java.io.IOException;

import org.springframework.stereotype.Service;

import com.example.Model.DeleteResponseModel;
import com.example.Model.GetAllProductsResponseModel;
import com.example.Model.Product;
import com.example.Model.ResponseModel;
import com.example.RetrofitInterface.RetrofitInterface;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import io.reactivex.Single;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


//provides a wrapper around the retrointerface to handle its interactions

@Service
public class RetrofitService {

	RetrofitInterface retrointerface;

	public RetrofitService() {
		
		HttpLoggingInterceptor logginginterceptor=new HttpLoggingInterceptor();
		logginginterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
		
		OkHttpClient okhttp=new OkHttpClient.Builder()
				.addInterceptor(logginginterceptor)
				.build();
		
		Gson gson = new GsonBuilder().create();

		Retrofit retrofit = new Retrofit.Builder().baseUrl("http://localhost:8083")
				.addConverterFactory(GsonConverterFactory.create(gson))
				.addCallAdapterFactory(RxJava2CallAdapterFactory.create())
				.client(okhttp)
				.build();

		retrointerface = retrofit.create(RetrofitInterface.class);
	}

	public Single<GetAllProductsResponseModel> getProducts() throws IOException {
		return retrointerface.getproducts()
				.flatMap(response->{
					GetAllProductsResponseModel resp=new GetAllProductsResponseModel(response, 200, "success");
					return Single.just(resp);
				});

//		Call<List<Product>> products=retrointerface.getproducts();
//		Response<List<Product>> response=products.execute();
//		if(!response.isSuccessful()) {
//			throw new IOException("unknown error");
//		}
//		return response.body();//deserializes response body of successful response

	}

	public Single<ResponseModel> getProductsById(int id) throws IOException {
//			Call<Product> product=retrointerface.getProductById(id);
//			Response<Product> resp=product.execute();
//			ResponseModel response=new ResponseModel(resp.body(),200,"success");
//			return Single.just(response);

		return retrointerface.getProductById(id)
				.flatMap(response -> {
			  ResponseModel rm = new ResponseModel(response, 200, "success");
			  return Single.just(rm);
		        })
			.onErrorResumeNext(error -> {
			if (error instanceof EOFException) {
				return Single.just(new ResponseModel(null, 404, "Product Not Found"));
			}
			return Single.error(error);
		});

//		Call<Product> product=retrointerface.getProductById(id);
//		Response<Product> response=product.execute();
//		if(!response.isSuccessful()) {
//			throw new IOException("unknown error");
//		}
//		return response.body();
	}

	public Single<DeleteResponseModel> deleteProducts(int id) throws IOException {
		return retrointerface.deleteProduct(id)
				.onErrorResumeNext(error -> {
					if (error instanceof retrofit2.adapter.rxjava2.HttpException) {
				    return Single.just(new DeleteResponseModel("Product Not Found"));
			}
			return Single.error(error);
		});
	}

	public Single<ResponseModel> createProducts(Product product) throws IOException {
		return retrointerface.createProduct(product)
				.flatMap(response -> {
					ResponseModel rm = new ResponseModel(response, 200, "success");
					return Single.just(rm);
		});

//		Call<Product> prod=retrointerface.createProduct(product);
//		Response<Product> response=prod.execute();
//		if(!response.isSuccessful()) {
//			throw new IOException("UNKNOWN ERROR");
//			}
//		return response.body();
	}

}
