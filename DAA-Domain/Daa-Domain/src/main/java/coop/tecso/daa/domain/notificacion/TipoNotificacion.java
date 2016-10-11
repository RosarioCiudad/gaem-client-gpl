package coop.tecso.daa.domain.notificacion;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import coop.tecso.daa.domain.base.AbstractEntity;

/**
 * 
 * @author tecso.coop
 *
 */
@DatabaseTable(tableName = "not_tipoNotificacion")
public class TipoNotificacion extends AbstractEntity {
	
	@DatabaseField(canBeNull = false)
	private String descripcion;

	@DatabaseField(canBeNull = false)
	private String ubicacionSonido;

	@DatabaseField(canBeNull = false)
	private String ubicacionIcono;

	public String getDescripcion() {
		return descripcion;
	}

	public String getUbicacionIcono() {
		return ubicacionIcono;
	}

	public String getUbicacionSonido() {
		return ubicacionSonido;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public void setUbicacionIcono(String ubicacionIcono) {
		this.ubicacionIcono = ubicacionIcono;
	}

	public void setUbicacionSonido(String ubicacionSonido) {
		this.ubicacionSonido = ubicacionSonido;
	}
}