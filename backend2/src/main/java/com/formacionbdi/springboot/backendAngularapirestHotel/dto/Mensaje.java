package com.formacionbdi.springboot.backendAngularapirestHotel.dto;

public class Mensaje {
private String mensaje;

public String getMensaje() {
	return mensaje;
}

public void setMensaje(String mensaje) {
	this.mensaje = mensaje;
}

public Mensaje(String mensaje) {
	super();
	this.mensaje = mensaje;
}

}
