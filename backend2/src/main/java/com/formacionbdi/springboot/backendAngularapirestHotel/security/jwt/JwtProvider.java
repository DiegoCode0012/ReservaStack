package com.formacionbdi.springboot.backendAngularapirestHotel.security.jwt;
import java.security.Key;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import com.formacionbdi.springboot.backendAngularapirestHotel.dto.JwtDto;
import com.formacionbdi.springboot.backendAngularapirestHotel.models.entity.UsuarioPrincipal;
import com.nimbusds.jwt.JWT;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.JWTParser;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

//CREA EL TOKEN Y TAMBIEN LO VALIDA
@Component
public class JwtProvider {
	private static final Logger logger = LoggerFactory.getLogger(JwtProvider.class);
	
	
	  @Value("${jwt.secret}")
	  private String secret; //jwtsecret

	  @Value("${jwt.expiration}")
	  private int expiration; //jwtexpiration
	 
	  public String generateJwtToken(Authentication authentication) {

	    UsuarioPrincipal usuarioPrincipal = (UsuarioPrincipal) authentication.getPrincipal();
	   // logger.info(usuarioPrincipal.getNombre());
	    List<String> roles = usuarioPrincipal.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
	    return Jwts.builder()
	        .setSubject((usuarioPrincipal.getUsername()))
	        .claim("roles", roles)
	        .claim("userId",usuarioPrincipal.getId())
	        .setIssuedAt(new Date())
	        .setExpiration(new Date(System.currentTimeMillis()+expiration*180)) //segundos
	        .signWith(getSecret(secret))
	        .compact();
	  }


	  public String getUserNameFromJwtToken(String token) {
	    return Jwts.parserBuilder().setSigningKey(getSecret(secret)).build()
	               .parseClaimsJws(token).getBody().getSubject();
	  }

	  public boolean validateJwtToken(String authToken) {
	    try {
	      Jwts.parserBuilder().setSigningKey(getSecret(secret)).build().parse(authToken);
	      return true;
	    } catch (MalformedJwtException e) {
	      logger.error("Invalid JWT token: {}", e.getMessage());
	    } catch (ExpiredJwtException e) {
	      logger.error("JWT token is expired: {}", e.getMessage());
	    } catch (UnsupportedJwtException e) {
	      logger.error("JWT token is unsupported: {}", e.getMessage());
	    } catch (IllegalArgumentException e) {
	      logger.error("JWT claims string is empty: {}", e.getMessage());
	    }

	    return false;
	  }
	  
	  @SuppressWarnings("unchecked")
	public String refreshToken(JwtDto jwtDto) throws ParseException {
	        try {
	            Jwts.parserBuilder().setSigningKey(getSecret(secret)).build().parseClaimsJws(jwtDto.getToken());
	        } catch (ExpiredJwtException e) {
	        	
	        	JWT jwt = JWTParser.parse(jwtDto.getToken());
	            JWTClaimsSet claims = jwt.getJWTClaimsSet();
	            String nombreUsuario = claims.getSubject();
				List<String> roles =  (List<String>) claims.getClaim("roles");

	            return Jwts.builder()
	                    .setSubject(nombreUsuario)
	                    .claim("roles", roles)
	                    .setIssuedAt(new Date())
	                    .setExpiration(new Date(new Date().getTime() + expiration))
	                    .signWith(getSecret(secret))
	                    .compact();
	        }
	        return null;
	    }
	  
	  private Key getSecret(String secret){
		  
	        byte[] secretBytes = Decoders.BASE64URL.decode(secret);
	        return Keys.hmacShaKeyFor(secretBytes);
	    }
}
