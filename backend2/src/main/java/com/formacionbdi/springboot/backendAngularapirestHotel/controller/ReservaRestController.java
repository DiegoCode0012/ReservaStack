package com.formacionbdi.springboot.backendAngularapirestHotel.controller;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.formacionbdi.springboot.backendAngularapirestHotel.dto.GananciaPorMesDTO;
import com.formacionbdi.springboot.backendAngularapirestHotel.dto.GananciaTipoDTO;
import com.formacionbdi.springboot.backendAngularapirestHotel.dto.HabitacionesMasSolicitadas;
import com.formacionbdi.springboot.backendAngularapirestHotel.models.entity.Reserva;
import com.formacionbdi.springboot.backendAngularapirestHotel.services.IReservaService;

import jakarta.validation.Valid;


@CrossOrigin(origins= {"http://localhost:4200"})
@RestController
@RequestMapping("/api")
public class ReservaRestController {
	protected final Log logger=LogFactory.getLog(this.getClass());

	@Autowired
	private IReservaService reservaService;
	
	@GetMapping("/reservas")
	public List<Reserva> getAll() {
	return reservaService.findAll();
	}
	
	@GetMapping("/reservasxUsuario/{id}")
	public List<Reserva> reservasXUsuario(@PathVariable Long id) {
	return reservaService.reservasXUsuario(id);
	}
	
	@GetMapping("/reservas/ganaciasxTipo")
	public List<GananciaTipoDTO> gananciasxTipo() {
	return reservaService.listarGananciasDeLasReservasDeAcuerdoAlTipo();
	}
	
	@GetMapping("/reservas/habitacionesMasSolicitadas")
	public List<HabitacionesMasSolicitadas> listarHabitacionesMasSolicitadas() {
	return reservaService.listarHabitacionesMasSolicitadas();
	}
	
	@GetMapping("/reservas/obtenerGananciasTotales")
	public Double obtenerGananciasTotales() {
	return reservaService.obtenerGananciasTotales();
	}
	
	@GetMapping("/reservas/totalClientes")
	public Double totalClientes() {
	return reservaService.totalClientes();
	}
	@GetMapping("/reservas/obtenerGananciasMensuales")
	public List<GananciaPorMesDTO> obtenerGananciasMensuales() {
	return reservaService.obtenerGananciasMensuales();
	}
	
	
	@PostMapping("/reservas")
	public ResponseEntity<?> create(@Valid @RequestBody Reserva reserva, BindingResult result) {
		Reserva reservaNuevo;
		Map<String,Object> response =new HashMap<>();
		if(result.hasErrors()) {
			List<String> errors=result.getFieldErrors()
					.stream()
					.map(err->
				 "El campo " + err.getField() + "  " + err.getDefaultMessage()
				)
					.collect(Collectors.toList());
			response.put("errors", errors);
			return new ResponseEntity<Map<String,Object>>(response,HttpStatus.BAD_REQUEST);

		}
		/* Validar que no halla cruzw de horarios*/
		List<Reserva> reservas = reservaService.reservasXUsuario(reserva.getUser().getId());
		List<String> errors = new ArrayList<>();
		boolean bandera=false;
		for(Reserva x:reservas) {
			logger.info(x.getEstado());
			if(x.getEstado().equalsIgnoreCase("Activa")) {
				logger.info("dddd");
				errors.add("Tiene la reserva con ID:"+ x.getId() + " y numero de habitación: " + reserva.getHabitacion().getNumero()+ "en curso , no es posible realizar otra reserva" );
				bandera=true;
			}
		}
		if(bandera) {
			logger.info("holaaa");
			response.put("errors", errors);
			return new ResponseEntity<Map<String,Object>>(response,HttpStatus.BAD_REQUEST);
		}

		logger.info("comidaaa");
		reservaNuevo =reservaService.save(reserva);
		response.put("reserva", reservaNuevo);
		response.put("mensaje", "Reserva creado con exito");
		return new ResponseEntity<Map<String,Object>>(response,HttpStatus.CREATED);
	}
	
	@DeleteMapping("/reservas/{id}")
	public void delete(@PathVariable Long id) {
		  reservaService.deleteReserva(id);
	}
	
	
}
