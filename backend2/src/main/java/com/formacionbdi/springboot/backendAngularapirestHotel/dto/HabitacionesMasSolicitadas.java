package com.formacionbdi.springboot.backendAngularapirestHotel.dto;




public class HabitacionesMasSolicitadas {
	private String numero;
    private Long cantidad;
	public String getNumero() {
		return numero;
	}
	public void setNumero(String numero) {
		this.numero = numero;
	}
	public Long getCantidad() {
		return cantidad;
	}
	public void setCantidad(Long cantidad) {
		this.cantidad = cantidad;
	}
	public HabitacionesMasSolicitadas(String numero, Long cantidad) {
		super();
		this.numero = numero;
		this.cantidad = cantidad;
	}
	public HabitacionesMasSolicitadas( ) {
	}
}
