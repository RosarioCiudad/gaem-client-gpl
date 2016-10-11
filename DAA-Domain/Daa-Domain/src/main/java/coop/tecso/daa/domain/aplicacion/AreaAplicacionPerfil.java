package coop.tecso.daa.domain.aplicacion;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import coop.tecso.daa.domain.base.AbstractEntity;
import coop.tecso.daa.domain.perfil.PerfilAcceso;
import coop.tecso.daa.domain.seguridad.Area;

/**
 * 
 * @author tecso.coop
 *
 */
@DatabaseTable(tableName = "def_areaAplicacionPerfil")
public class AreaAplicacionPerfil extends AbstractEntity {

	@DatabaseField(canBeNull = false, foreign = true)
	private Area area;

	@DatabaseField(canBeNull = false, foreign = true)
	private AplicacionPerfil aplicacionPerfil;

	@DatabaseField(canBeNull = false, foreign = true)
	private PerfilAcceso perfilAcceso;

	public Area getArea() {
		return area;
	}

	public void setArea(Area area) {
		this.area = area;
	}

	public AplicacionPerfil getAplicacionPerfil() {
		return aplicacionPerfil;
	}

	public void setAplicacionPerfil(AplicacionPerfil aplicacionPerfil) {
		this.aplicacionPerfil = aplicacionPerfil;
	}

	public PerfilAcceso getPerfilAcceso() {
		return perfilAcceso;
	}

	public void setPerfilAcceso(PerfilAcceso perfilAcceso) {
		this.perfilAcceso = perfilAcceso;
	}

}