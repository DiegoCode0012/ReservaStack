package com.formacionbdi.springboot.backendAngularapirestHotel.models.dao;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.formacionbdi.springboot.backendAngularapirestHotel.models.entity.Usuario;

public interface IUsuarioDao extends CrudRepository<Usuario, Long>{
	  	Optional<Usuario> findByUsername(String nombreUsuario);
	    Optional<Usuario> findByUsernameOrEmail(String nombreUsuario, String email);
	    boolean existsByUsername(String nombreUsuario);
	    boolean existsByEmail(String email);
	    @Query("SELECT u FROM Usuario u JOIN u.roles r WHERE u.id NOT IN " +
	            "(SELECT res.user.id FROM Reserva res WHERE res.estado = 'Activa')"+
	    		"AND r.id=3")
	    public List<Usuario> findClientesSinReservaActiva();
}
