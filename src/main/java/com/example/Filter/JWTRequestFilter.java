package com.example.Filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.Util.JWTUtil;
import com.example.service.MyUserDetailsService;


//intercept the requests : taking a bearer token and validate the token to know if it comes from trusted user
@Component
public class JWTRequestFilter extends OncePerRequestFilter {
	
	@Autowired
	JWTUtil jwtutil; 
	
	@Autowired
	MyUserDetailsService userdetailsservice;
	
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String header=request.getHeader("Authorization");
		String username=null;
		String jwt=null;
		
		if(header!=null && header.startsWith("Bearer ")) {
			jwt=header.substring(7);
			username=jwtutil.extractUserName(jwt);
		}
		
	
		if(username!=null && SecurityContextHolder.getContext().getAuthentication()==null) { //obtains currently authenticated principal
			UserDetails userdetails=this.userdetailsservice.loadUserByUsername(username);
			if(jwtutil.validateToken(jwt, userdetails)) {
				UsernamePasswordAuthenticationToken usernamepasswordAuthToken=new UsernamePasswordAuthenticationToken(
						userdetails,null,userdetails.getAuthorities());
				usernamepasswordAuthToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(usernamepasswordAuthToken);
			}
		}
		filterChain.doFilter(request, response); //gives control to next filter
	}

}
