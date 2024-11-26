package com.formacionbdi.springboot.backendAngularapirestHotel.controller;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.formacionbdi.springboot.backendAngularapirestHotel.models.entity.Habitacion;
import com.formacionbdi.springboot.backendAngularapirestHotel.services.IHabitacionService;
import com.formacionbdi.springboot.backendAngularapirestHotel.services.IUploadService;

import jakarta.validation.Valid;

@CrossOrigin(origins= {"http://localhost:4200"})
@RestController
@RequestMapping("/api")
public class HabitacionRestController {
	protected final Log logger=LogFactory.getLog(this.getClass());

	@Autowired
	private IHabitacionService habitacionService;
	
	@Autowired
	private IUploadService uploadService;
	
	@GetMapping("/habitaciones")
	public List<Habitacion> getAll(){
		return habitacionService.getAllHabitaciones();
	}
	
	@GetMapping("/habitacionesdisponibles")
	public List<Habitacion> HabitacionesDisponibles(){
		return habitacionService.getAllAvailableRooms();
	}
	
	@GetMapping("/habitaciones/{id}")
	public Habitacion ver(@PathVariable Long id){
		return habitacionService.findHabitacionById(id);
	}
	
	@PostMapping("/habitaciones")
	public ResponseEntity<?> create(@Valid  @RequestBody Habitacion habitacion,  BindingResult result) {
		Habitacion habitacionNuevo;
		Map<String,Object> response =new HashMap<>();
		if (result.hasFieldErrors()) {
	            return validation(result);
	        }
		
		try {
			habitacionNuevo =habitacionService.save(habitacion);
		} catch (DataIntegrityViolationException e) {//comprobamos los campos unicos
			List <String> errors= new ArrayList<String>();
			errors.add("el campo numero debe ser unico");
			response.put("errors", errors);
			return new ResponseEntity<Map<String,Object>>(response,HttpStatus.CONFLICT);
		}
		
		response.put("habitacion", habitacionNuevo);
		response.put("mensaje", "Habitación creado con exito");
		return new ResponseEntity<Map<String,Object>>(response,HttpStatus.CREATED);
	}
	
	@PutMapping("/habitaciones/{id}")
	public ResponseEntity<?> update(@Valid @RequestBody Habitacion habitacion, BindingResult result,@PathVariable Long id) {
		Map<String,Object> response =new HashMap<>();

		if (result.hasFieldErrors()) {
	            return validation(result);
	        }
		Habitacion habitacionUpdate=habitacionService.findHabitacionById(id);

		if(habitacionUpdate==null) {
			response.put("mensaje","tipo no encontrado en BBDD");
			return new ResponseEntity<Map<String,Object>>(response,HttpStatus.NOT_FOUND);
		}
		try {
			habitacionUpdate.setNumero(habitacion.getNumero());
			habitacionUpdate.setTipo(habitacion.getTipo());
			habitacionService.save(habitacionUpdate);		
			} catch (DataIntegrityViolationException e) {//comprobamos los campos unicos
			List <String> errors= new ArrayList<String>();
			errors.add("el campo numero debe ser unico");
			response.put("mensaje", "Error al realizar el insert en BBDD");
			response.put("errors", errors);
			return new ResponseEntity<Map<String,Object>>(response,HttpStatus.CONFLICT);
		}
		response.put("room", habitacionUpdate);
		response.put("mensaje", "La habitacion ha sido creado con exito");
		return new ResponseEntity<Map<String,Object>>(response,HttpStatus.CREATED);
	}
	
	@DeleteMapping("/habitaciones/{id}")
	public void deleteHabitacion(@PathVariable Long id) {
		habitacionService.deletebyId(id);
	}
	
	@GetMapping("/habitaciones/idTipo/{id}")
	public List<Habitacion> filtrarHabitacionesxTipo(@PathVariable Long id){
		return habitacionService.getAllAvailableRooms().stream().filter(x->x.getTipo().getId()==id).toList();
	}
	
	
	@PostMapping("/habitaciones/upload")
	public ResponseEntity<?> upload(@RequestParam("archivos") MultipartFile[] archivos, @RequestParam("id") Long id) {
	    Map<String, Object> response = new HashMap<>();
	    logger.info("inicio");
	    Habitacion room = habitacionService.findHabitacionById(id);
	    logger.info("inicio");
	    // Manejar la lógica para múltiples archivos
	    if (archivos.length > 0) {
	        for (int i = 0; i < archivos.length; i++) {
	            MultipartFile archivo = archivos[i];
	            if (!archivo.isEmpty()) {
	                String nombreArchivo = null;
	                try {
	                    nombreArchivo = uploadService.copiar(archivo);
	                } catch (IOException e) {
	                	logger.info("sss");
	                    response.put("mensaje", "Error al subir la imagen: " + e.getMessage());
	                    response.put("error", e.getMessage().concat(": ").concat(e.getCause().getMessage()));
	                    return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	                }

	                // Asignar el archivo a la propiedad correspondiente
	                switch (i) {
                    case 0:
                        String nombreFotoAnterior1 = room.getFoto1();
                            uploadService.eliminar(nombreFotoAnterior1);                   
                             room.setFoto1(nombreArchivo);
                        break;
                    case 1:
                        String nombreFotoAnterior2 = room.getFoto2();
                            uploadService.eliminar(nombreFotoAnterior2);                  
                        room.setFoto2(nombreArchivo);
                        break;
                    case 2:
                        String nombreFotoAnterior3 = room.getFoto3();
                            uploadService.eliminar(nombreFotoAnterior3);                       
                        room.setFoto3(nombreArchivo);
                        break;
                    default:
                        // Si hay más de 3 fotos, puedes decidir qué hacer
                        break;
                }
	            }
	        }

	        habitacionService.save(room);
	        response.put("room", room);
	        response.put("mensaje", "Has subido correctamente las imágenes.");
	    } else {
	        response.put("mensaje", "No se han subido imágenes.");
	    }

	    return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}
	
	
	/* 
	 * Map<String, Object> response = new HashMap<>();

		Habitacion room = habitacionService.findHabitacionById(id);

		if (!archivo.isEmpty()) {

			String nombreArchivo = null;
			try {
				nombreArchivo = uploadService.copiar(archivo);
			} catch (IOException e) {
				response.put("mensaje", "Error al subir la imagen del cliente");
				response.put("error", "ddsdsds");
				return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			}

			String nombreFotoAnterior = room.getFoto1();

			uploadService.eliminar(nombreFotoAnterior);

			room.setFoto1(nombreArchivo);

			habitacionService.save(room);

			response.put("room", room);
			response.put("mensaje", "Has subido correctamente la imagen: " + nombreArchivo);
		}

		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	 * 
	 * 
	 * */

	@GetMapping("/uploads/img/{nombreFoto:.+}")
	public ResponseEntity<Resource> verFoto(@PathVariable String nombreFoto) {
		
		Resource recurso = null;
		
		try {
			recurso = uploadService.cargar(nombreFoto);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		
		HttpHeaders cabecera = new HttpHeaders();
		cabecera.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + recurso.getFilename() + "\"");

		return new ResponseEntity<Resource>(recurso, cabecera, HttpStatus.OK);
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
