package com.formacionbdi.springboot.backendAngularapirestHotel.services;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import com.formacionbdi.springboot.backendAngularapirestHotel.models.dao.IUsuarioDao;
import com.formacionbdi.springboot.backendAngularapirestHotel.models.entity.Usuario;


@Component
public class UserServiceImpl implements IUserService{


	@Autowired
	private	IUsuarioDao usuarioDao;
	
	@Override
	public List<Usuario> getAllClientesSinReservaActiva() {
		return usuarioDao.findClientesSinReservaActiva();
	}

	@Override
	@Transactional(readOnly=true)
	public List<Usuario> findAll() {
		return (List<Usuario>) usuarioDao.findAll();
	}

	@Override
	@Transactional(readOnly=true)
	public Usuario findById(Long id) {
		return usuarioDao.findById(id).orElse(null);
	}

	@Override
	public void delete(Long id) {
		usuarioDao.deleteById(id);
	}

	@Override
	public Usuario save(Usuario user) {
		return usuarioDao.save(user);
	}

	@Override
	public boolean existsByNombreUsuario(String nombreUsuario) {
		return usuarioDao.existsByUsername(nombreUsuario);
	}

}
