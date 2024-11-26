package com.formacionbdi.springboot.backendAngularapirestHotel.services;

import java.util.Optional;

import com.formacionbdi.springboot.backendAngularapirestHotel.models.entity.Role;

public interface IRoleService {
	 public Optional<Role> getByRolNombre(String rolNombre);

	    public void save(Role rol);

}
