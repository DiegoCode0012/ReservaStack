package com.formacionbdi.springboot.backendAngularapirestHotel.security.jwt;

import java.io.IOException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.formacionbdi.springboot.backendAngularapirestHotel.services.UserDetailsServiceImp;

// VALIDA EL TOKEN POR CADA PETICION
@Component
public class JwtTokenFilter  extends OncePerRequestFilter {

			@Autowired
			JwtProvider jwtProvider;

		    @Autowired
		    UserDetailsServiceImp userDetailsService;
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		  try {
	            String token = getToken(request);
	            if(token != null && jwtProvider.validateJwtToken(token)){
	                String nombreUsuario = jwtProvider.getUserNameFromJwtToken(token);
	                UserDetails userDetails = userDetailsService.loadUserByUsername(nombreUsuario); 
	                System.out.print(userDetails);
// me devuelve el nombre , username,email y authorities
	                UsernamePasswordAuthenticationToken auth =
	                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
	                SecurityContextHolder.getContext().setAuthentication(auth);
	            }
	        } catch (Exception e){
	            logger.error("fail en el método doFilter " + e.getMessage());
	        }
	        filterChain.doFilter(request, response);
		
	}
	//obtenemos el token actual
	 private String getToken(HttpServletRequest request){
	        String header = request.getHeader("Authorization");
	        if(header != null && header.startsWith("Bearer"))
	            return header.replace("Bearer ", "");
	        return null;
	    }
}
