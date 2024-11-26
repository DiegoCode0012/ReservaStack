package com.formacionbdi.springboot.backendAngularapirestHotel.services;

import java.util.List;

import com.formacionbdi.springboot.backendAngularapirestHotel.models.entity.Tipo;


public interface ITipoService {

	public List<Tipo> getAllTipo();
	public void deleteTipo(Long id);
	public Tipo save(Tipo tipo);
	public Tipo findById(Long id);

}
