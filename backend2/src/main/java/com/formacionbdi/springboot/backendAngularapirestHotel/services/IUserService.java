package com.formacionbdi.springboot.backendAngularapirestHotel.services;

import java.util.List;

import com.formacionbdi.springboot.backendAngularapirestHotel.models.entity.Usuario;


public interface IUserService {
public List<Usuario> findAll();
public Usuario findById(Long id);
public Usuario save(Usuario user);
public void delete(Long id);	
boolean existsByNombreUsuario(String nombreUsuario);
public List<Usuario> getAllClientesSinReservaActiva();

}
