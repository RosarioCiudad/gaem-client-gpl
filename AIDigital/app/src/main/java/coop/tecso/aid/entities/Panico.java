/*
 * Copyright (c) 2016 Municipalidad de Rosario, Coop. de Trabajo Tecso Ltda.
 *
 * This file is part of GAEM.
 *
 * GAEM is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License.
 *
 * GAEM is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with GAEM.  If not, see <http://www.gnu.org/licenses/>.
 */

package coop.tecso.aid.entities;

import java.util.Date;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import coop.tecso.daa.domain.base.AbstractEntity;
import coop.tecso.daa.domain.seguridad.DispositivoMovil;
import coop.tecso.daa.domain.seguridad.UsuarioApm;

@DatabaseTable(tableName = "apm_panico")
public class Panico extends AbstractEntity {
	
	@DatabaseField(canBeNull = false, foreign = true)
	private DispositivoMovil dispositivoMovil;

	@DatabaseField(canBeNull = false, foreign = true)
	private UsuarioApm usuarioPanico;

	@DatabaseField
	private Date fechaPosicion;

	@DatabaseField
	private Date fechaPanico;

	@DatabaseField
	private double latitud;

	@DatabaseField
	private double longitud;

	@DatabaseField
	private String origen;

	@DatabaseField
	private double precision;

	// Getters y Setters
	public DispositivoMovil getDispositivoMovil() {
		return dispositivoMovil;
	}

	public void setDispositivoMovil(DispositivoMovil dispositivoMovil) {
		this.dispositivoMovil = dispositivoMovil;
	}

	public UsuarioApm getUsuarioPanico() {
		return usuarioPanico;
	}

	public void setUsuarioPanico(UsuarioApm usuarioPanico) {
		this.usuarioPanico = usuarioPanico;
	}

	public Date getFechaPosicion() {
		return fechaPosicion;
	}

	public void setFechaPosicion(Date fechaPosicion) {
		this.fechaPosicion = fechaPosicion;
	}

	public Date getFechaPanico() {
		return fechaPanico;
	}

	public void setFechaPanico(Date fechaPanico) {
		this.fechaPanico = fechaPanico;
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

	public String getOrigen() {
		return origen;
	}

	public void setOrigen(String origen) {
		this.origen = origen;
	}

	public double getPrecision() {
		return precision;
	}

	public void setPrecision(double precision) {
		this.precision = precision;
	}
}