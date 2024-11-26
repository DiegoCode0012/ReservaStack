package com.formacionbdi.springboot.backendAngularapirestHotel.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.formacionbdi.springboot.backendAngularapirestHotel.models.dao.IUsuarioDao;
import com.formacionbdi.springboot.backendAngularapirestHotel.models.entity.Usuario;
import com.formacionbdi.springboot.backendAngularapirestHotel.models.entity.UsuarioPrincipal;

import org.springframework.security.core.userdetails.UserDetailsService;


@Service
public class UserDetailsServiceImp implements UserDetailsService  {
	  
	  @Autowired
	  private IUsuarioDao usuarioDao;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Usuario user = usuarioDao.findByUsername(username)
		        .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));

		    return UsuarioPrincipal.build(user);
	}

	
}
