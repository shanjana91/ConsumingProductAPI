package com.example.RetrofitController;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.Model.AuthenticationRequest;
import com.example.Model.AuthenticationResponse;
import com.example.Model.DeleteResponseModel;
import com.example.Model.GetAllProductsResponseModel;
import com.example.Model.Product;
import com.example.Model.ResponseModel;
import com.example.Util.JWTUtil;
import com.example.service.MyUserDetailsService;
import com.example.service.RetrofitService;

import io.reactivex.Single;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;

//exposing retrofit client as endpoints

@RestController
public class RetrofitController {

	@Autowired
	RetrofitService service;

	@Autowired
	AuthenticationManager authManager;
	// processes the auth request

	@Autowired
	MyUserDetailsService userdetailservice;

	@Autowired
	JWTUtil jwtutil;

	@ApiOperation(value = "Insert a Product Record")
	@PostMapping("/createProd")
	public Single<ResponseModel> createProd(@RequestBody Product prod) throws IOException {
		return service.createProducts(prod);
	}

	@ApiOperation(value = "Delete a Product Record")
	@DeleteMapping("/deleteProd/{id}")
	public Single<DeleteResponseModel> deleteProd(@PathVariable("id") int id) throws IOException {
		return service.deleteProducts(id);
	}

	@ApiOperation(value = "Get Products")
	@GetMapping("/prod")
	public Single<GetAllProductsResponseModel> getProducts() throws IOException {
		return service.getProducts();
	}

	@ApiOperation(value = "Get product By ID")
	@GetMapping("/prod/{id}")
	public Single<ResponseModel> getProdById(@PathVariable("id") int id) throws IOException {
		return service.getProductsById(id);
	}

	@ApiOperation(value = "Enter credentials for authorization")
	@PostMapping("/authenticate") // I/p : username and pwd , O/P : JWT Token
	// I/P : authentication request POJO
	// o/p : authentication response POJO
	public ResponseEntity createAuthToken(@RequestBody AuthenticationRequest request) throws Exception {

		try {
			authManager.authenticate(
					new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
		} catch (BadCredentialsException e) {
			throw new Exception("Incorrect Username or Password", e);
		}

		UserDetails userdetails = userdetailservice.loadUserByUsername(request.getUsername());

		String token = jwtutil.generatetoken(userdetails);

		return ResponseEntity.ok(new AuthenticationResponse(token));
	}

}
