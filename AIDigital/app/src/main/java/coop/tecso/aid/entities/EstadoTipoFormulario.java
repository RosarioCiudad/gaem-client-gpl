package coop.tecso.aid.entities;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import coop.tecso.daa.domain.base.AbstractEntity;

@DatabaseTable(tableName = "for_estadoTipoFormulario")
public class EstadoTipoFormulario extends AbstractEntity {
	
	public final static int ID_EN_PREPARACION = 1;
	public final static int ID_CERRADA_PROVISORIA = 2;
	public final static int ID_CERRADA_DEFINITIVA = 3;
	public final static int ID_ANULADA = 4;

	@DatabaseField(canBeNull = false)
	private String descripcion;
	
	@DatabaseField(canBeNull = false, foreign = true)
	private TipoFormulario tipoFormulario;

	// Constructores
	public EstadoTipoFormulario() {
		super();
	}

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