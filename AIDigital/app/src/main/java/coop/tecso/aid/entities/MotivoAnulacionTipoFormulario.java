package coop.tecso.aid.entities;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import coop.tecso.daa.domain.base.AbstractEntity;

@DatabaseTable(tableName = "for_motivoAnulacionTipoFormulario")
public class MotivoAnulacionTipoFormulario extends AbstractEntity {

	@DatabaseField(canBeNull = false)
	private String descripcion;
	
	@DatabaseField(canBeNull = false, foreign = true)
	private TipoFormulario tipoFormulario;

	// Getters y Setters
	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public TipoFormulario getTipoFormulario() {
		return tipoFormulario;
	}

	public void setTipoFormulario(TipoFormulario tipoFormulario) {
		this.tipoFormulario = tipoFormulario;
	}
}