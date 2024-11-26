package com.formacionbdi.springboot.backendAngularapirestHotel.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.formacionbdi.springboot.backendAngularapirestHotel.models.dao.IHabitacionDao;
import com.formacionbdi.springboot.backendAngularapirestHotel.models.entity.Habitacion;


@Service
public class HabitacionServiceImp implements IHabitacionService{

	@Autowired
	private IHabitacionDao habitacionDao;
	@Override
	public List<Habitacion> getAllHabitaciones() {
		return habitacionDao.findAll();
	}
	@Override
	public List<Habitacion> getAllAvailableRooms() {
		return habitacionDao.listarHabitacionesDisponibles();
	}
	@Override
	public Habitacion save(Habitacion habitacion) {
		return habitacionDao.save(habitacion);
	}
	@Override
	public void deletebyId(Long id) {
		habitacionDao.deleteById(id);
	}
	@Override
	public Habitacion findHabitacionById(Long id) {
		return habitacionDao.findById(id).orElse(null);
	}
	
	
}
