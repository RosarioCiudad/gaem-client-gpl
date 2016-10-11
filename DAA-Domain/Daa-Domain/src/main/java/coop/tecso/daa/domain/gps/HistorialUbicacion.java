package coop.tecso.daa.domain.gps;

import java.util.Date;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import coop.tecso.daa.domain.base.AbstractEntity;
import coop.tecso.daa.domain.seguridad.DispositivoMovil;
import coop.tecso.daa.domain.seguridad.UsuarioApm;

/**
 * 
 * @author tecso.coop
 *
 */
@DatabaseTable(tableName = "gps_historialUbicacion")
public class HistorialUbicacion extends AbstractEntity {
	
	@DatabaseField(canBeNull = false, foreign = true)
	private UsuarioApm usuarioApm;

	@DatabaseField(canBeNull = false, foreign = true)
	private DispositivoMovil dispositivoMovil;
	
	@DatabaseField
	private Date fechaPosicion;
	
	@DatabaseField
	private Date fechaUbicacion;
	
	@DatabaseField
	private double latitud;
	
	@DatabaseField
	private double longitud;
	
	@DatabaseField
	private double precision;
	
	@DatabaseField
	private String origen;
	
	@DatabaseField
	private double velocidad;
	
	@DatabaseField
	private double radio;
	
	@DatabaseField
	private double altitud;
	
	@DatabaseField
	private double nivelBateria;

	@DatabaseField
	private double nivelSenial;

	public UsuarioApm getUsuarioApm() {
		return usuarioApm;
	}

	public void setUsuarioApm(UsuarioApm usuarioApm) {
		this.usuarioApm = usuarioApm;
	}

	public DispositivoMovil getDispositivoMovil() {
		return dispositivoMovil;
	}

	public void setDispositivoMovil(DispositivoMovil dispositivoMovil) {
		this.dispositivoMovil = dispositivoMovil;
	}

	public Date getFechaPosicion() {
		return fechaPosicion;
	}

	public void setFechaPosicion(Date fechaPosicion) {
		this.fechaPosicion = fechaPosicion;
	}

	public Date getFechaUbicacion() {
		return fechaUbicacion;
	}

	public void setFechaUbicacion(Date fechaUbicacion) {
		this.fechaUbicacion = fechaUbicacion;
	}

	public double getLatitud() {
		return latitud;
	}

	public void setLatitud(double latitud) {
		this.latitud = latitud;
	}

	public double getLongitud() {
		return longitud;
	}

	public void setLongitud(double longitud) {
		this.longitud = longitud;
	}

	public double getPrecision() {
		return precision;
	}

	public void setPrecision(double precision) {
		this.precision = precision;
	}

	public String getOrigen() {
		return origen;
	}

	public void setOrigen(String origen) {
		this.origen = origen;
	}

	public double getVelocidad() {
		return velocidad;
	}

	public void setVelocidad(double velocidad) {
		this.velocidad = velocidad;
	}

	public double getRadio() {
		return radio;
	}

	public void setRadio(double radio) {
		this.radio = radio;
	}

	public double getAltitud() {
		return altitud;
	}

	public void setAltitud(double altitud) {
		this.altitud = altitud;
	}

	public double getNivelBateria() {
		return nivelBateria;
	}

	public void setNivelBateria(double nivelBateria) {
		this.nivelBateria = nivelBateria;
	}

	public double getNivelSenial() {
		return nivelSenial;
	}

	public void setNivelSenial(double nivelSenial) {
		this.nivelSenial = nivelSenial;
	}
}