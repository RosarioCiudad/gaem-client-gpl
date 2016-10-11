package coop.tecso.aid.entities;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "gis_localidad")
public class Localidad {
	
	@DatabaseField(canBeNull = false)
	private Integer codigoPostal;
    
	@DatabaseField(canBeNull = false)
	private Integer codigoSubPostal;

	@DatabaseField(canBeNull = false)
	private String descripcion;
	
	@DatabaseField
	private String provincia;

	// Getters y Setters
	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public Integer getCodigoPostal() {
		return codigoPostal;
	}

	public void setCodigoPostal(Integer codigoPostal) {
		this.codigoPostal = codigoPostal;
	}

	public Integer getCodigoSubPostal() {
		return codigoSubPostal;
	}

	public void setCodigoSubPostal(Integer codigoSubPostal) {
		this.codigoSubPostal = codigoSubPostal;
	}

	public String getProvincia() {
		return provincia;
	}

	public void setProvincia(String provincia) {
		this.provincia = provincia;
	}
}