package com.formacionbdi.springboot.backendAngularapirestHotel.dto;

public class GananciaPorMesDTO {
	private String mes;
    private Double totalGanancias;
	public String getMes() {
		return mes;
	}
	public void setMes(String mes) {
		this.mes = mes;
	}
	public Double getTotalGanancias() {
		return totalGanancias;
	}
	public void setTotalGanancias(Double totalGanancias) {
		this.totalGanancias = totalGanancias;
	}
	public GananciaPorMesDTO(String mes, Double totalGanancias) {
		super();
		this.mes = mes;
		this.totalGanancias = totalGanancias;
	}
	public GananciaPorMesDTO() {

	}
    
}
