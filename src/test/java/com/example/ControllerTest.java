package com.example;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
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
import com.example.UserRepository.UserRepository;
import com.example.service.RetrofitService;

import io.reactivex.Single;

@RunWith(SpringRunner.class)
@WebMvcTest(value = RetrofitController.class)
@ComponentScan({ "com.example" })
@AutoConfigureMockMvc(addFilters = false)
class ControllerTest {

	@Autowired
	MockMvc mockmvc;

	@MockBean
	RetrofitService service;

	@MockBean
	UserRepository repo;

	Product p1 = new Product(1, "Mobile", 4, 18000);
	List<Product> products = new ArrayList<Product>();

	@Test
	// @AutoConfigureMockMvc(addFilters = false)
	public void getProductsTest() throws Exception {
		products.add(p1);
		GetAllProductsResponseModel rm = new GetAllProductsResponseModel(products, 200, "success");
		when(service.getProducts()).thenReturn(Single.just(rm));
		GetAllProductsResponseModel expectedResponse = service.getProducts().blockingGet();
		System.out.println(expectedResponse.toString());

		RequestBuilder requestbuilder = MockMvcRequestBuilders.get("/prod").accept(MediaType.APPLICATION_JSON);

		MvcResult result = mockmvc.perform(requestbuilder).andReturn();
		int status = result.getResponse().getStatus();
		System.out.println(result.getAsyncResult().toString());

		assertEquals(HttpStatus.OK.value(), expectedResponse.getCode());
		verify(service, times(2)).getProducts();
		assertEquals(rm, expectedResponse);
		assertEquals(HttpStatus.OK.value(), status);
		assertEquals(expectedResponse.toString(), result.getAsyncResult().toString());
	}

	@Test
	@AutoConfigureMockMvc(addFilters = false)
	public void getProdByIdTest() throws Exception {
		int id = 1;
		ResponseModel rm = new ResponseModel(p1, 200, "success");
		when(service.getProductsById(id)).thenReturn(Single.just(rm));
		ResponseModel expectedResponse = service.getProductsById(id).blockingGet();

		RequestBuilder requestbuilder = MockMvcRequestBuilders.get("/prod/1").accept(MediaType.APPLICATION_JSON);
		MvcResult result = mockmvc.perform(requestbuilder).andReturn();

		assertEquals(expectedResponse.toString(), result.getAsyncResult().toString());
		assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
	}

	@Test
	@AutoConfigureMockMvc(addFilters = false)
	public void createProdTest() throws Exception {
		ResponseModel rm = new ResponseModel(p1, 200, "success");
		when(service.createProducts(p1)).thenReturn(Single.just(rm));
		ResponseModel expectedResponse = service.createProducts(p1).blockingGet();
		String input = "{\"id\":1,\"name\":\"Mobile\",\"quantity\":4,\"price\":18000}";
		System.out.println(expectedResponse.toString());

		RequestBuilder requestbuilder = MockMvcRequestBuilders.post("/createProd").accept(MediaType.APPLICATION_JSON)
				.content(input).contentType(MediaType.APPLICATION_JSON);
		MvcResult result = this.mockmvc.perform(requestbuilder)
//				.andExpect(request().asyncStarted())
//				.andExpect(request().asyncResult(new Product(1,"Mobile",4,18000)))
				.andReturn();
//		this.mockmvc.perform(asyncDispatch(result))
//        .andExpect(status().isOk())
//        .andExpect(content().contentType(MediaType.APPLICATION_JSON));

//		System.out.println(result.getAsyncResult(5000L).toString());
//		System.out.println(result.getResponse().getContentAsString());
//		
//		assertEquals(expectedResponse.toString(), result.getResponse().getContentAsString());
		assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
		assertNotNull(expectedResponse);
		assertNotNull(result.getResponse());
	}

	@Test
	@AutoConfigureMockMvc(addFilters = false)
	public void deleteProdTest() throws Exception {
		int id = 1;
		DeleteResponseModel drm = new DeleteResponseModel(
				"{\"message\":\"Product removed successfully , ID : " + id + "\"}");
		when(service.deleteProducts(id)).thenReturn(Single.just(drm));
		DeleteResponseModel expectedResponse = service.deleteProducts(id).blockingGet();

		RequestBuilder requestbuilder = MockMvcRequestBuilders.delete("/deleteProd/1")
				.accept(MediaType.APPLICATION_JSON);
		MvcResult result = mockmvc.perform(requestbuilder).andReturn();

		assertEquals(expectedResponse.toString(), result.getAsyncResult().toString());
		assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
		assertEquals(null, service.deleteProducts(3));
	}

}
