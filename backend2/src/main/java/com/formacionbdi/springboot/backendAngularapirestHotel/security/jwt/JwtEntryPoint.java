package com.formacionbdi.springboot.backendAngularapirestHotel.security.jwt;
import java.io.IOException;
import java.io.OutputStream;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.formacionbdi.springboot.backendAngularapirestHotel.handler.RestError;


//COMPRUEBA SI EXISTE EL TOKEN, devuelve el mensaje no autorizado en caso de erroe en token
@Component
public class JwtEntryPoint implements AuthenticationEntryPoint{
	private static final Logger logger = LoggerFactory.getLogger(JwtEntryPoint.class);

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
		logger.error("fail en el método commence: " + authException.getMessage());

		RestError re = new RestError(HttpStatus.UNAUTHORIZED.toString(), "Autenticación fallida");
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        OutputStream responseStream = response.getOutputStream();
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(responseStream, re);
        responseStream.flush();
        
		/*
		logger.error("fail en el método commence: " + authException.getMessage());

        response.resetBuffer();
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setHeader("Content-Type", "application/json");
        response.getOutputStream().print("{\"status\":401,\"error\":\"Unauthorized\",\"message\":\"No esta autorizado\"}");
        response.flushBuffer(); 
        */
}
}
