package com.formacionbdi.springboot.backendAngularapirestHotel.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.formacionbdi.springboot.backendAngularapirestHotel.models.dao.ITipoDao;
import com.formacionbdi.springboot.backendAngularapirestHotel.models.entity.Tipo;


@Component
public class TipoServiceImp implements ITipoService{
@Autowired 
private ITipoDao tipoDao;

@Override
public List<Tipo> getAllTipo() {
	return tipoDao.findAll();
}

@Override
public void deleteTipo(Long id) {
	tipoDao.deleteById(id);
	
}

@Override
public Tipo save(Tipo tipo) {
	return tipoDao.save(tipo);
}

@Override
public Tipo findById(Long id) {
	return tipoDao.findById(id).orElse(null);
}

}
