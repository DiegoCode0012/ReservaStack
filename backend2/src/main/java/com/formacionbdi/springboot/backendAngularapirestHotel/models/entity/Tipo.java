package com.formacionbdi.springboot.backendAngularapirestHotel.models.entity;

import java.io.Serializable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name="tipos")
public class Tipo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private Double precio;
	
	private Integer capacNiño;
	private Integer capacAdult;
	@NotNull
    @Pattern(regexp = "Estandar|Doble|Suite|Familiar", message = "debe ser ESTANDAR,DOBLE,SUITE O FAMILIAR")
    @Column(unique = true)
	private String descripcion;
	
	public double PrecioHabitacion(String x) {
		double precio = 0;
		if(x.equalsIgnoreCase("Estandar")) {
			capacNiño=0;
			capacAdult=1;
			precio=50.0;
		}else if (x.equalsIgnoreCase("Doble")) {
			capacNiño=0;
			capacAdult=2;
			precio=100.0;
		}else if (x.equalsIgnoreCase("Suite")) {
			capacNiño=3;
			capacAdult=4;
			precio=400.0;
		}else if(x.equalsIgnoreCase("Familiar")){
			capacNiño=2;
			capacAdult=4;
			precio=250.0;
		}
		return precio;
	}
}
