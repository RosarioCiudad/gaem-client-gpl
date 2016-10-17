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

package coop.tecso.daa.domain.trazabilidad;

import java.util.Date;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import coop.tecso.daa.domain.base.AbstractEntity;

/**
 * 
 * @author tecso.coop
 *
 */
@DatabaseTable(tableName = "apm_historiaGPS")
public class UbicacionGPS extends AbstractEntity {
	
	@DatabaseField(canBeNull = false)
	private int dispositivoMovilID;

	@DatabaseField
	private int tipoRegistro;
	
	@DatabaseField
	private int tipoPosicionamiento;
	
	@DatabaseField
	private Date fechaLectura;
	
	@DatabaseField
	private Date fechaPosicion;
	
	@DatabaseField
	private double latitud;
	
	@DatabaseField
	private double longitud;
	
	
	public int getDispositivoMovilID() {
		return dispositivoMovilID;
	}

	public void setDispositivoMovilID(int dispositivoMovilID) {
		this.dispositivoMovilID = dispositivoMovilID;
	}

	public int getTipoRegistro() {
		return tipoRegistro;
	}

	public void setTipoRegistro(int tipoRegistro) {
		this.tipoRegistro = tipoRegistro;
	}

	public int getTipoPosicionamiento() {
		return tipoPosicionamiento;
	}

	public void setTipoPosicionamiento(int tipoPosicionamiento) {
		this.tipoPosicionamiento = tipoPosicionamiento;
	}

	public Date getFechaLectura() {
		return fechaLectura;
	}

	public void setFechaLectura(Date fechaLectura) {
		this.fechaLectura = fechaLectura;
	}

	public Date getFechaPosicion() {
		return fechaPosicion;
	}

	public void setFechaPosicion(Date fechaPosicion) {
		this.fechaPosicion = fechaPosicion;
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
}