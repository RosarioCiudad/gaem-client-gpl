package coop.tecso.aid.entities;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import coop.tecso.daa.domain.base.AbstractEntity;
import coop.tecso.daa.domain.seguridad.Area;

@DatabaseTable(tableName = "apm_telefonoPanico")
public class TelefonoPanico extends AbstractEntity {
	
	@DatabaseField(canBeNull = false, foreign = true)
	private Area area;
	
	@DatabaseField(canBeNull = false)
	private String numero;

	@DatabaseField
	private String descripcion;
	
	// Getters y Setters
	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public Area getArea() {
		return area;
	}

	public void setArea(Area area) {
		this.area = area;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

}