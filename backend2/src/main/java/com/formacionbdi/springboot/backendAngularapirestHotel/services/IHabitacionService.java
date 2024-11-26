package com.formacionbdi.springboot.backendAngularapirestHotel.services;

import java.util.List;

import com.formacionbdi.springboot.backendAngularapirestHotel.models.entity.Habitacion;


public interface IHabitacionService {

	public List<Habitacion> getAllHabitaciones();
	public List<Habitacion> getAllAvailableRooms();
	public Habitacion save(Habitacion habitacion);
	public void deletebyId (Long id);
	public Habitacion findHabitacionById(Long id);
}
