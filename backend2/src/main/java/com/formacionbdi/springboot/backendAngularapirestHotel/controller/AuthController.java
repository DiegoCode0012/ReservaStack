package com.formacionbdi.springboot.backendAngularapirestHotel.controller;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jakarta.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.formacionbdi.springboot.backendAngularapirestHotel.dto.JwtDto;
import com.formacionbdi.springboot.backendAngularapirestHotel.dto.LoginUsuario;
import com.formacionbdi.springboot.backendAngularapirestHotel.models.entity.Habitacion;
import com.formacionbdi.springboot.backendAngularapirestHotel.models.entity.Role;
import com.formacionbdi.springboot.backendAngularapirestHotel.models.entity.Usuario;
import com.formacionbdi.springboot.backendAngularapirestHotel.security.jwt.JwtProvider;
import com.formacionbdi.springboot.backendAngularapirestHotel.services.IRoleService;
import com.formacionbdi.springboot.backendAngularapirestHotel.services.IUserService;

import io.jsonwebtoken.io.IOException;


@RestController
@RequestMapping("/auth")
@CrossOrigin
public class AuthController {

	private final Logger log =LoggerFactory.getLogger(getClass());

	 	@Autowired
	    PasswordEncoder passwordEncoder;

	    @Autowired
	    AuthenticationManager authenticationManager;

	    @Autowired
	    IUserService usuarioService;

	    @Autowired
	    IRoleService rolService;

	    @Autowired
	    JwtProvider jwtProvider;

	    @PostMapping("/nuevo")
	    public ResponseEntity<?> nuevo(@Valid @RequestBody Usuario user, BindingResult bindingResult){
			Map<String,Object> response =new HashMap<>();

	    	if (bindingResult.hasFieldErrors()) {
		            return validation(bindingResult);
		        }
        	List <String> errors= new ArrayList<String>();

	        if(usuarioService.existsByNombreUsuario(user.getUsername())) {
				errors.add("el campo username debe ser unico");
				response.put("errors", errors);				
		        return new ResponseEntity<>(errors, HttpStatus.CONFLICT);
	        	}
	       
			user.setPassword(passwordEncoder.encode(user.getPassword()));

	        Set<Role> roles = new HashSet<>();
	        roles.add(rolService.getByRolNombre("ROLE_CLIENTE").get()); // objeto rol
	        user.setRoles(roles);
	        usuarioService.save(user);
			response.put("mensaje", "usuario creado con exito");
			return new ResponseEntity<Map<String,Object>>(response,HttpStatus.CREATED);

	    }

	    
	    
	    
	    @PostMapping("/login")
	    public ResponseEntity<JwtDto> login(@Valid @RequestBody LoginUsuario loginUsuario, BindingResult bindingResult){
	        log.info("ssss"+loginUsuario.getNombreUsuario() +"saa" +loginUsuario.getPassword());
	    	Authentication authentication =
	                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginUsuario.getNombreUsuario(), loginUsuario.getPassword()));
	        SecurityContextHolder.getContext().setAuthentication(authentication);
	        String jwt = jwtProvider.generateJwtToken(authentication);
	        UserDetails userDetails= (UserDetails) authentication.getPrincipal();
	        JwtDto jwtDto = new JwtDto(jwt,userDetails.getUsername(),userDetails.getAuthorities());
	        log.info("ssss"+loginUsuario.getNombreUsuario() +"saa" +loginUsuario.getPassword());
	        return new ResponseEntity<>(jwtDto, HttpStatus.OK);
	    }

	    @GetMapping("/users")
		public List<Usuario> getAll() {
		return usuarioService.findAll();
		}
	    
	    
	    @PostMapping("/upload-file")
	    public String uploadFile(@RequestParam("file") MultipartFile file) throws java.io.IOException {
	        try {
	            // Convertir el archivo a un arreglo de bytes
	            byte[] fileBytes = file.getBytes();

	            // Puedes imprimir o manipular el arreglo de bytes según sea necesario
	            System.out.println("Bytes del archivo: " + Arrays.toString(fileBytes));

	            // Retornar una respuesta simple (puede ser personalizada)
	            return "Archivo recibido y convertido a bytes con éxito.";

	        } catch (IOException e) {
	            e.printStackTrace();
	            return "Error al leer el archivo.";
	        }
	    }

		@GetMapping("/clientesSinReserva")
		public List<Usuario> getAllSinReserva() {
		return usuarioService.getAllClientesSinReservaActiva();
		}
		
		@DeleteMapping("/users/{id}")
		public void delete(@PathVariable Long id) {
			usuarioService.delete(id);
		}
		@GetMapping("/users/{id}")
		public Usuario ver(@PathVariable Long id){
			return usuarioService.findById(id);
		}
		
		private ResponseEntity<?> validation(BindingResult result) {
	        Map<String, Object> response = new HashMap<>();

	    List <String> errors= result.getFieldErrors().stream().map(err->
		 "El campo " + err.getField() + "  " + err.getDefaultMessage()
		).toList();
	        response.put("errors", errors);
	        return new ResponseEntity<Map<String,Object>>(response,HttpStatus.BAD_REQUEST);
	    }
}
