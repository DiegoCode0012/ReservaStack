package com.formacionbdi.springboot.backendAngularapirestHotel.dto;

public class GananciaTipoDTO {
	private String descripcion;
    private Double totalxTipo;
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public Double getTotalxTipo() {
		return totalxTipo;
	}
	public void setTotalxTipo(Double totalxTipo) {
		this.totalxTipo = totalxTipo;
	}
	public GananciaTipoDTO(String descripcion, Double totalxTipo) {
		super();
		this.descripcion = descripcion;
		this.totalxTipo = totalxTipo;
	}
	public GananciaTipoDTO() {
	}
   
}
