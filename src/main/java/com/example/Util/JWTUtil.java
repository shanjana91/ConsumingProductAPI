package com.example.Util;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JWTUtil {
	
	@Value("${spring.secret.key}")
	private String SECRET_KEY;
	
	//I/P: Userdetails , O/P: JWT Token
	public String generatetoken(UserDetails userdetails) {
	Map<String, Object> claims=new HashMap<>(); 
	return createToken(claims,userdetails.getUsername()); //initially empty claim could add any specific payload claim
	}

	private String createToken(Map<String, Object> claims, String username) {
		return Jwts.builder().setClaims(claims).setSubject(username) 
		 		.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() +1000*60*60)) //in millisecs : (1 hour)
				.signWith(SignatureAlgorithm.HS256, SECRET_KEY).compact(); //builds JWT as a compact serialized string 
	}
	
	private Claims extractAllClaims(String token) {
		return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
	}
	
	public <T> T extractClaim(String token,Function<Claims, T> claimsResolver) {
		Claims claims=extractAllClaims(token); //claims are pieces of info asserted about a subject
		return claimsResolver.apply(claims); //apply the function to given argument
	}
	
	public String extractUserName(String token) {
		return extractClaim(token, Claims::getSubject);
	}
	
	public Date extractExpiration(String token) {
		return extractClaim(token, Claims::getExpiration);
	}
	
	private Boolean isExpired(String token) {
		return extractExpiration(token).before(new Date());
	}
	
	public boolean validateToken(String token,UserDetails userdetails) {
		String username=extractUserName(token);
		return(username.equals(userdetails.getUsername()) && !isExpired(token));
	}
	
}
