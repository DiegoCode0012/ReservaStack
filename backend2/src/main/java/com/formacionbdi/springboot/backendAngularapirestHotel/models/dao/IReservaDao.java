package com.formacionbdi.springboot.backendAngularapirestHotel.models.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.formacionbdi.springboot.backendAngularapirestHotel.dto.GananciaPorMesDTO;
import com.formacionbdi.springboot.backendAngularapirestHotel.dto.GananciaTipoDTO;
import com.formacionbdi.springboot.backendAngularapirestHotel.dto.HabitacionesMasSolicitadas;
import com.formacionbdi.springboot.backendAngularapirestHotel.models.entity.Reserva;


public interface IReservaDao extends JpaRepository<Reserva, Long>{

	   @Query("SELECT new com.formacionbdi.springboot.backendAngularapirestHotel.dto.GananciaTipoDTO(t.descripcion, SUM(t.precio)) " +
	           "FROM Reserva r " +
	           "JOIN r.habitacion h " +
	           "JOIN h.tipo t " +
	           "JOIN r.user u " +
	           "GROUP BY t.descripcion")
	public List<GananciaTipoDTO> listarGananciasDeLasReservasDeAcuerdoAlTipo();
	   

	   @Query("SELECT new com.formacionbdi.springboot.backendAngularapirestHotel.dto.HabitacionesMasSolicitadas(h.numero, COUNT(h.numero) AS Cantidad) " +
	           "FROM Reserva r " +
	           "JOIN r.habitacion h " +
	           "JOIN r.user u " +
	           "GROUP BY h.numero")
	public List<HabitacionesMasSolicitadas> listarHabitacionesMasSolicitadas();
	   
	   @Query("SELECT SUM(t.precio) FROM Reserva r " +
		       "JOIN r.habitacion h " +
		       "JOIN h.tipo t")
	public	Double obtenerGananciasTotales();
	   
	   
	   @Query("SELECT COUNT(u) FROM Usuario u JOIN u.roles r WHERE r.id = 3")
	   public	Double totalClientes();
	   
	   @Query("SELECT new com.formacionbdi.springboot.backendAngularapirestHotel.dto.GananciaPorMesDTO(MONTHNAME(r.diaStart) AS mes, SUM(t.precio) AS totalGanancias) " +
		       "FROM Reserva r " +
		       "JOIN r.habitacion h " +
		       "JOIN h.tipo t " +
		       "GROUP BY MONTH(r.diaStart), MONTHNAME(r.diaStart) " +
		       "ORDER BY MONTH(r.diaStart)")
		List<GananciaPorMesDTO> obtenerGananciasMensuales();
	   
}
