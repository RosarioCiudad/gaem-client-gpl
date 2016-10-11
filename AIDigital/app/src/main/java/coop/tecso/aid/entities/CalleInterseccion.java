package coop.tecso.aid.entities;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "gis_calleInterseccion")
public class CalleInterseccion {

	@DatabaseField(canBeNull = false, foreign = true, columnName = "codigoCalle")
	private Calle calle;

	@DatabaseField(canBeNull = false, foreign = true, columnName = "codigoInterseccion")
	private Calle interseccion;

	// Getters y Setters
	public Calle getCalle() {
		return calle;
	}

	public void setCalle(Calle calle) {
		this.calle = calle;
	}

	public Calle getInterseccion() {
		return interseccion;
	}

	public void setInterseccion(Calle interseccion) {
		this.interseccion = interseccion;
	}
}