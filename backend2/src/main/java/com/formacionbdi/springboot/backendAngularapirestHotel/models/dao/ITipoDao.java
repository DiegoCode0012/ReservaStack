package com.formacionbdi.springboot.backendAngularapirestHotel.models.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.formacionbdi.springboot.backendAngularapirestHotel.models.entity.Tipo;


public interface ITipoDao  extends JpaRepository<Tipo, Long>{

}
