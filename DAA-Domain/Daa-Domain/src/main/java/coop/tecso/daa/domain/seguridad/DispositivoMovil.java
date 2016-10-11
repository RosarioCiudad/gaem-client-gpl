package coop.tecso.daa.domain.seguridad;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import coop.tecso.daa.domain.base.AbstractEntity;

/**
 * 
 * @author tecso.coop
 *
 */
@DatabaseTable(tableName = "apm_dispositivoMovil")
public class DispositivoMovil extends AbstractEntity {
	
	@DatabaseField(canBeNull = false, foreign = true)
	private Area area;

	@DatabaseField
	private String descripcion;
	
	@DatabaseField
	private String numeroSerie;
	
	@DatabaseField
	private String numeroLinea;
	
	@DatabaseField
	private String numeroIMEI;
	
	@DatabaseField
	private String emailAddress;
	
	@DatabaseField(canBeNull = false)
	private int forzarActualizacion;

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

	public String getNumeroLinea() {
		return numeroLinea;
	}

	public void setNumeroLinea(String numeroLinea) {
		this.numeroLinea = numeroLinea;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public int getForzarActualizacion() {
		return forzarActualizacion;
	}

	public void setForzarActualizacion(int forzarActualizacion) {
		this.forzarActualizacion = forzarActualizacion;
	}

	public String getNumeroIMEI() {
		return numeroIMEI;
	}

	public void setNumeroIMEI(String numeroIMEI) {
		this.numeroIMEI = numeroIMEI;
	}

	public Area getArea() {
		return area;
	}

	public void setArea(Area area) {
		this.area = area;
	}
}