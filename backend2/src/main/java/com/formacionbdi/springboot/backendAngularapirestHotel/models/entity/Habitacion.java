package com.formacionbdi.springboot.backendAngularapirestHotel.models.entity;

import java.io.Serializable;

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
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name="habitaciones")
public class Habitacion implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(unique=true)
	@NotNull
    @Pattern(regexp="^[0-9]+$", message="debe contener solo números")
    @Size(min=3, max=3, message="debe tener exactamente 3 dígitos")
	private String numero;
	private String foto1;
	private String foto2;
	private String foto3;
	private boolean disponible;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="tipo_id")
	@JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
	@NotNull(message = "no puede estar vacío")
	private Tipo tipo;
	
	
}
