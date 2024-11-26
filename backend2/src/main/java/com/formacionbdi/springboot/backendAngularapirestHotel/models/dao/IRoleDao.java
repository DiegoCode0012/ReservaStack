package com.formacionbdi.springboot.backendAngularapirestHotel.models.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.formacionbdi.springboot.backendAngularapirestHotel.models.entity.Role;

public interface IRoleDao extends JpaRepository<Role, Long>{
	 Optional<Role> findByName(String rolNombre);
}
