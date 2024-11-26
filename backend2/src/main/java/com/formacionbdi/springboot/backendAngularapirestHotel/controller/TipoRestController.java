package com.formacionbdi.springboot.backendAngularapirestHotel.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.formacionbdi.springboot.backendAngularapirestHotel.models.entity.Tipo;
import com.formacionbdi.springboot.backendAngularapirestHotel.services.ITipoService;

import jakarta.validation.Valid;

@CrossOrigin(origins= {"http://localhost:4200"})
@RestController
@RequestMapping("/api")
public class TipoRestController {
	@Autowired
	private ITipoService tipoService;
	
	@GetMapping("/tipos")
	public List<Tipo> getAll() {
	return tipoService.getAllTipo();
	}
	
	@GetMapping("/tipos/{id}")
	public Tipo ver(@PathVariable Long id) {
	return tipoService.findById(id);
	}
	
	@PostMapping("/tipos")
	public  ResponseEntity<?> create(@Valid @RequestBody Tipo tipo,BindingResult result) {
		Tipo  typeNuevo=null;
		Map<String,Object> response =new HashMap<>();
		  if (result.hasFieldErrors()) {
	            return validation(result);
	        }
		  try {
			  double x=tipo.PrecioHabitacion(tipo.getDescripcion());
				 tipo.setPrecio(x);
				 typeNuevo =tipoService.save(tipo);
			} catch (DataIntegrityViolationException  e) {//comprobamos los campos unicos
				List <String> errors= new ArrayList<String>();
				errors.add("el campo descripcion debe ser unico");
				response.put("errors", errors);				
				return new ResponseEntity<Map<String,Object>>(response,HttpStatus.CONFLICT);
			}		
			response.put("tipo", typeNuevo);
			response.put("mensaje", "Tipo de Habitación creado con exito");
		   return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}
	
	@DeleteMapping("/tipos/{id}")
	public void delete(@PathVariable Long id) {
		tipoService.deleteTipo(id);
	}
	
	@PutMapping("/tipos/{id}")
	public ResponseEntity<?> update(@Valid @RequestBody Tipo tipo, BindingResult result,@PathVariable Long id) {
		Map<String,Object> response =new HashMap<>();
		if (result.hasFieldErrors()) {
	            return validation(result);
	        }
		Tipo tiposUpdate=tipoService.findById(id);
		if(tiposUpdate==null) {
			response.put("mensaje","tipo no encontrado en BBDD");
			return new ResponseEntity<Map<String,Object>>(response,HttpStatus.NOT_FOUND);
		}
		  try {
			  double x=tipo.PrecioHabitacion(tipo.getDescripcion());
				tiposUpdate.setPrecio(x);
				tiposUpdate.setDescripcion(tipo.getDescripcion());
			} catch (Exception  e) {//comprobamos los campos unicos
				List <String> errors= new ArrayList<String>();
				errors.add("el campo descripcion debe ser unico");
				response.put("errors", errors);				
				return new ResponseEntity<Map<String,Object>>(response,HttpStatus.CONFLICT);
			}	
		response.put("tipo", tiposUpdate);
		response.put("mensaje", "El tipo ha sido creado con exito");
		tipoService.save(tiposUpdate);
		return new ResponseEntity<Map<String,Object>>(response,HttpStatus.CREATED);
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
