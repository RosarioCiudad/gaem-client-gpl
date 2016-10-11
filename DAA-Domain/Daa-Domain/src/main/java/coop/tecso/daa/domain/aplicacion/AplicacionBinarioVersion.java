package coop.tecso.daa.domain.aplicacion;

import java.util.Date;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import coop.tecso.daa.domain.base.AbstractEntity;

/**
 * 
 * @author tecso.coop
 *
 */
@DatabaseTable(tableName = "apm_aplicacionBinarioVersion")
public class AplicacionBinarioVersion extends AbstractEntity {
	
	@DatabaseField(canBeNull = false, foreign = true)
	private Aplicacion aplicacion;

	@DatabaseField(canBeNull = false, foreign = true)
	private AplicacionTipoBinario aplicacionTipoBinario;
	
	@DatabaseField
	private String descripcion;
	
	@DatabaseField
	private Date fecha;
	
	@DatabaseField
	private String ubicacion;

	public Aplicacion getAplicacion() {
		return aplicacion;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public Date getFecha() {
		return fecha;
	}

	public String getUbicacion() {
		return ubicacion;
	}

	public void setAplicacion(Aplicacion aplicacion) {
		this.aplicacion = aplicacion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public void setUbicacion(String ubicacion) {
		this.ubicacion = ubicacion;
	}

	public AplicacionTipoBinario getAplicacionTipoBinario() {
		return aplicacionTipoBinario;
	}

	public void setAplicacionTipoBinario(AplicacionTipoBinario aplicacionTipoBinario) {
		this.aplicacionTipoBinario = aplicacionTipoBinario;
	}
}