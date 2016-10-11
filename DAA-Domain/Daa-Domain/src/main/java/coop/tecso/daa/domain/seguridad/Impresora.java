package coop.tecso.daa.domain.seguridad;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import coop.tecso.daa.domain.base.AbstractEntity;

/**
 * 
 * @author tecso.coop
 *
 */
@DatabaseTable(tableName = "apm_impresora")
public class Impresora extends AbstractEntity {
	
	@DatabaseField
	private String descripcion;

	@DatabaseField
	private String numeroSerie;

	@DatabaseField
	private String numeroUUID;

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getNumeroSerie() {
		return numeroSerie;
	}

	public void setNumeroSerie(String numeroSerie) {
		this.numeroSerie = numeroSerie;
	}

	public String getNumeroUUID() {
		return numeroUUID;
	}

	public void setNumeroUUID(String numeroUUID) {
		this.numeroUUID = numeroUUID;
	}
}