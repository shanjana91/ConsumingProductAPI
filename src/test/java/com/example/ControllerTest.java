package com.example;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.example.Model.DeleteResponseModel;
import com.example.Model.GetAllProductsResponseModel;
import com.example.Model.Product;
import com.example.Model.ResponseModel;
import com.example.RetrofitController.RetrofitController;
import com.example.service.RetrofitService;

import io.reactivex.Single;

@EnableAsync
@WebMvcTest(RetrofitController.class)
@RunWith(SpringRunner.class)
class ControllerTest {

	@Autowired
	MockMvc mockmvc;
	
	@MockBean
	RetrofitService service;
	Product p1=new Product(1, "Mobile", 4, 18000);
	List<Product> products=new ArrayList<Product>();
	
	
	@Test
	public void getProductsTest() throws Exception {
		products.add(p1);
		GetAllProductsResponseModel rm=new GetAllProductsResponseModel(products, 200, "success");
		when(service.getProducts()).thenReturn(Single.just(rm));
		GetAllProductsResponseModel expectedResponse=service.getProducts().blockingGet();
		System.out.println(expectedResponse.toString());

		RequestBuilder requestbuilder=MockMvcRequestBuilders.get("/prod").accept(MediaType.APPLICATION_JSON);
		MvcResult result=mockmvc.perform(requestbuilder).andReturn();
		int status=result.getResponse().getStatus();
		System.out.println(result.getAsyncResult().toString());
		
		assertEquals(HttpStatus.OK.value(), expectedResponse.getCode());
		verify(service,times(2)).getProducts();
		assertEquals(rm,expectedResponse);
		assertEquals(HttpStatus.OK.value(), status);
		assertEquals(expectedResponse.toString(), result.getAsyncResult().toString());
	}
	
	@Test
	public void getProdByIdTest() throws Exception {
		int id=1;
		ResponseModel rm=new ResponseModel(p1, 200, "success");
		when(service.getProductsById(id)).thenReturn(Single.just(rm));
		ResponseModel expectedResponse=service.getProductsById(id).blockingGet();
		
		RequestBuilder requestbuilder=MockMvcRequestBuilders.get("/prod/1").accept(MediaType.APPLICATION_JSON);
		MvcResult result=mockmvc.perform(requestbuilder).andReturn();

		assertEquals(expectedResponse.toString(), result.getAsyncResult().toString());
		assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
	}
	
	@Test
	public void createProdTest() throws Exception {
		ResponseModel rm=new ResponseModel(p1, 200,"success");
		when(service.createProducts(p1)).thenReturn(Single.just(rm));
		ResponseModel expectedResponse=service.createProducts(p1).blockingGet();
		String input="{\"id\":1,\"name\":\"Mobile\",\"quantity\":4,\"price\":18000}";
		System.out.println(expectedResponse.toString());
		
		RequestBuilder requestbuilder=MockMvcRequestBuilders.post("/createProd").accept(MediaType.APPLICATION_JSON)
				.content(input).contentType(MediaType.APPLICATION_JSON);
		MvcResult result=this.mockmvc.perform(requestbuilder)
				.andExpect(request().asyncStarted())
				.andExpect(request().asyncResult(new Product(1,"Mobile",4,18000)))
				.andReturn();
		this.mockmvc.perform(asyncDispatch(result))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(content().string("{\"name\":\"Joe\",\"someDouble\":0.0,\"someBoolean\":false}"));
 
//		System.out.println(result.getAsyncResult(5000L).toString());
//		System.out.println(result.getResponse().getContentAsString());
//		
//		assertEquals(expectedResponse.toString(), result.getResponse().getContentAsString());
		assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
		assertNotNull(expectedResponse);
		assertNotNull(result.getResponse());
	}
	
	
	@Test
	public void deleteProdTest() throws Exception {
		int id=1;
		DeleteResponseModel drm=new DeleteResponseModel("{\"message\":\"Product removed successfully , ID : "+id+"\"}");
		when(service.deleteProducts(id)).thenReturn(Single.just(drm));
		DeleteResponseModel expectedResponse=service.deleteProducts(id).blockingGet();
		
		RequestBuilder requestbuilder=MockMvcRequestBuilders.delete("/deleteProd/1").accept(MediaType.APPLICATION_JSON);
		MvcResult result=mockmvc.perform(requestbuilder).andReturn();
		
		assertEquals(expectedResponse.toString(), result.getAsyncResult().toString());
		assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
		assertEquals(null, service.deleteProducts(3));
	}

}
