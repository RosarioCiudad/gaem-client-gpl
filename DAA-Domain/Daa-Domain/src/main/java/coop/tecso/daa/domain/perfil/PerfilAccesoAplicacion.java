package coop.tecso.daa.domain.perfil;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import coop.tecso.daa.domain.aplicacion.Aplicacion;
import coop.tecso.daa.domain.base.AbstractEntity;

/**
 * 
 * @author tecso.coop
 *
 */
@DatabaseTable(tableName = "apm_perfilAccesoAplicacion")
public class PerfilAccesoAplicacion extends AbstractEntity {
	
	@DatabaseField(canBeNull = false, foreign = true)
	private PerfilAcceso perfilAcceso;

	@DatabaseField(canBeNull = false, foreign = true)
	private Aplicacion aplicacion;

	public Aplicacion getAplicacion() {
		return aplicacion;
	}

	public PerfilAcceso getPerfilAcceso() {
		return perfilAcceso;
	}

	public void setAplicacion(Aplicacion aplicacion) {
		this.aplicacion = aplicacion;
	}

	public void setPerfilAcceso(PerfilAcceso perfilAcceso) {
		this.perfilAcceso = perfilAcceso;
	}	
}