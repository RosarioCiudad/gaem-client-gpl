package coop.tecso.aid.entities;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import coop.tecso.daa.domain.base.AbstractEntity;

@DatabaseTable(tableName = "for_tipoFormulario")
public class TipoFormulario extends AbstractEntity {
	
	@DatabaseField(canBeNull = false)
	private String codigo;

	@DatabaseField(canBeNull = false)
	private String descripcion;

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
}