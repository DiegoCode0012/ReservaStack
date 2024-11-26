package com.formacionbdi.springboot.backendAngularapirestHotel.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.formacionbdi.springboot.backendAngularapirestHotel.models.dao.IRoleDao;
import com.formacionbdi.springboot.backendAngularapirestHotel.models.entity.Role;

@Component
public class RoleServiceImp implements IRoleService{
	
	@Autowired
	private IRoleDao roleDao;
	@Override
	public Optional<Role> getByRolNombre(String rolNombre) {
		return roleDao.findByName(rolNombre);
	}

	@Override
	public void save(Role rol) {
		roleDao.save(rol);
	}

}
