package com.formacionbdi.springboot.backendAngularapirestHotel.services;

import java.util.List;

import com.formacionbdi.springboot.backendAngularapirestHotel.dto.GananciaPorMesDTO;
import com.formacionbdi.springboot.backendAngularapirestHotel.dto.GananciaTipoDTO;
import com.formacionbdi.springboot.backendAngularapirestHotel.dto.HabitacionesMasSolicitadas;
import com.formacionbdi.springboot.backendAngularapirestHotel.models.entity.Reserva;


public interface IReservaService {
	public Reserva save(Reserva reserva);
	public Reserva findReservaById(Long id);
	public List<Reserva> findAll();
	public void deleteReserva(Long id);
	public List<GananciaTipoDTO> listarGananciasDeLasReservasDeAcuerdoAlTipo();
	public List<HabitacionesMasSolicitadas> listarHabitacionesMasSolicitadas();
	public	Double obtenerGananciasTotales();
	 public	Double totalClientes();
	public	List<Reserva> reservasXUsuario(Long x);
	 List<GananciaPorMesDTO> obtenerGananciasMensuales();
}
