package com.formacionbdi.springboot.backendAngularapirestHotel.models.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.formacionbdi.springboot.backendAngularapirestHotel.models.entity.Habitacion;


public interface IHabitacionDao extends JpaRepository<Habitacion, Long>{
	@Query("SELECT h FROM Habitacion h WHERE h.disponible=true")
	public List<Habitacion> listarHabitacionesDisponibles();

}
