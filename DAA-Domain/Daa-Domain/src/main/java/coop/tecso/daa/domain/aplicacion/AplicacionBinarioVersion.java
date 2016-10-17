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