package com.formacionbdi.springboot.backendAngularapirestHotel.models.entity;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name="reservas")
public class Reserva implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	 @Column(name="hora_inicio")
	// @JsonFormat(pattern="yyyy-MM-dd")
	 @Temporal(TemporalType.TIMESTAMP)
	 @NotNull(message = "no puede estar vacío")
	 private Date diaStart;
	 
	 
	 @Column(name="hora_final")
//	 @JsonFormat(pattern="yyyy-MM-dd")
	 @Temporal(TemporalType.TIMESTAMP)
	 @NotNull(message = "no puede estar vacío")
	 private Date diaEnd;
	 
	 @ManyToOne(fetch = FetchType.LAZY)
	 @JoinColumn(name="habitacion_id")
	 @NotNull(message = "no puede estar vacio")
	 @JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
	 private Habitacion habitacion; 
	 
	 private String estado;
	 
	 @ManyToOne(fetch=FetchType.LAZY)
	 @JoinColumn(name = "user_id")
	 @JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
	 @NotNull(message = "no puede estar vacío")
	 private Usuario user;
	 
	 @JoinColumn(name = "pago_id")
	 private String idPago;

}
