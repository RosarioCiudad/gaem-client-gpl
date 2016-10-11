package coop.tecso.aid.entities;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import coop.tecso.daa.domain.base.AbstractEntity;

@DatabaseTable(tableName = "aid_infraccion")
public class Infraccion extends AbstractEntity {
	
	@DatabaseField(canBeNull = false)
	private String codigo;

	@DatabaseField(canBeNull = false)
	private String descripcion;
	
	@DatabaseField(canBeNull = false)
	private String descripcionLarga;
	
	@DatabaseField(canBeNull = false)
	private String tipo;

	// Getters y Setters
	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getDescripcionLarga() {
		return descripcionLarga;
	}

	public void setDescripcionLarga(String descripcionLarga) {
		this.descripcionLarga = descripcionLarga;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
}