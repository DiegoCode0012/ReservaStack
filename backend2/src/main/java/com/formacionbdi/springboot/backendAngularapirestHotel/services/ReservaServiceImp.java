package com.formacionbdi.springboot.backendAngularapirestHotel.services;

import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.formacionbdi.springboot.backendAngularapirestHotel.dto.GananciaPorMesDTO;
import com.formacionbdi.springboot.backendAngularapirestHotel.dto.GananciaTipoDTO;
import com.formacionbdi.springboot.backendAngularapirestHotel.dto.HabitacionesMasSolicitadas;
import com.formacionbdi.springboot.backendAngularapirestHotel.models.dao.IReservaDao;
import com.formacionbdi.springboot.backendAngularapirestHotel.models.entity.Reserva;



@Component
public class ReservaServiceImp implements IReservaService {

	@Autowired
	private IReservaDao reservaDao;
	@Override
	public Reserva save(Reserva reserva) {
		return reservaDao.save(reserva);
	}

	@Override
	public Reserva findReservaById(Long id) {
		return reservaDao.findById(id).orElse(null);
	}

	@Override
	public List<Reserva> findAll() {
		return reservaDao.findAll();
	}

	@Override
	public void deleteReserva(Long id) {
		reservaDao.deleteById(id);
	}

	@Override
	public List<GananciaTipoDTO> listarGananciasDeLasReservasDeAcuerdoAlTipo(){
		return reservaDao.listarGananciasDeLasReservasDeAcuerdoAlTipo();
	}

	@Override
	public List<HabitacionesMasSolicitadas> listarHabitacionesMasSolicitadas() {
		return reservaDao.listarHabitacionesMasSolicitadas();
	}

	@Override
	public Double obtenerGananciasTotales() {
		return reservaDao.obtenerGananciasTotales();
	}

	@Override
	public Double totalClientes() {
		return reservaDao.totalClientes();
	}

	@Override
	public List<GananciaPorMesDTO> obtenerGananciasMensuales() {
		// TODO Auto-generated method stub
		return reservaDao.obtenerGananciasMensuales();
	}

	@Override
	public List<Reserva> reservasXUsuario(Long x) {
		return reservaDao.findAll().stream()
				.filter(reserva ->reserva.getUser().getId()==x)
				.sorted(Comparator.comparing(Reserva::getDiaStart).reversed())
				.toList();
	}

}
