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

package coop.tecso.daa.domain.base;

import java.util.Date;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;

/**
 * 
 * @author tecso.coop
 *
 */
public abstract class AbstractEntity {

	@DatabaseField(generatedId = true, id = false, allowGeneratedIdInsert = true)
	private int id;

	@DatabaseField(canBeNull = false)
	private String usuario;
	
	@DatabaseField(canBeNull = false, dataType = DataType.DATE_LONG)
	private Date fechaUltMdf;
	
	@DatabaseField(canBeNull = false)
	private int estado;
	
	@DatabaseField(canBeNull = false)
	private int version;
	
	public AbstractEntity() {}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public Date getFechaUltMdf() {
		return fechaUltMdf;
	}

	public void setFechaUltMdf(Date fechaUltMdf) {
		this.fechaUltMdf = fechaUltMdf;
	}

	public int getEstado() {
		return estado;
	}

	public void setEstado(int estado) {
		this.estado = estado;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}
}