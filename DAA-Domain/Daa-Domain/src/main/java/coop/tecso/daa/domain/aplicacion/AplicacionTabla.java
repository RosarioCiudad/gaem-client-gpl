package coop.tecso.daa.domain.aplicacion;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import coop.tecso.daa.domain.base.AbstractEntity;
import coop.tecso.daa.domain.base.TablaVersion;

/**
 * 
 * @author tecso.coop
 *
 */
@DatabaseTable(tableName = "apm_aplicacionTabla")
public class AplicacionTabla extends AbstractEntity  {
	
	@DatabaseField(canBeNull = false, foreign = true, foreignAutoRefresh = true)
	private Aplicacion aplicacion;
	
	@DatabaseField(canBeNull = false, foreign = true, foreignAutoRefresh = true)
	private TablaVersion tablaVersion;

	@DatabaseField(canBeNull = false)
	private Integer orden;
	
	public Aplicacion getAplicacion() {
		return aplicacion;
	}

	public int getOrden() {
		return orden;
	}

	public TablaVersion getTablaVersion() {
		return tablaVersion;
	}

	public void setAplicacion(Aplicacion aplicacion) {
		this.aplicacion = aplicacion;
	}

	public void setOrden(int orden) {
		this.orden = orden;
	}

	public void setTablaVersion(TablaVersion tablaVersion) {
		this.tablaVersion = tablaVersion;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder(); 
		builder.append(String.format("Entidad --> {%s} ", getClass().getSimpleName()));
		builder.append(String.format("Aplicacion: {%s}, ", aplicacion.getDescripcion()));
		builder.append(String.format("Version de Tabla: {%s}, ", tablaVersion.getVersion()));
		builder.append(String.format("Orden: {%s}, ", orden));
		return builder.toString();
	}
}
