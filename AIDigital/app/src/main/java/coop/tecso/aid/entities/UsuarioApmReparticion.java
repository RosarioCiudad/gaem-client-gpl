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

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import coop.tecso.daa.domain.base.AbstractEntity;
import coop.tecso.daa.domain.seguridad.UsuarioApm;

/**
 * 
 * @author tecso.coop
 *
 */
@DatabaseTable(tableName = "aid_usuarioApmReparticion")
public class UsuarioApmReparticion extends AbstractEntity {
	
	@DatabaseField(canBeNull = false, foreign = true)
	private UsuarioApm usuarioApm;
	
	@DatabaseField(canBeNull = false, foreign = true)
	private Reparticion reparticion;
	
	@DatabaseField(canBeNull = false, foreign = true)
	private TipoFormulario tipoFormulario;
	
	@DatabaseField(canBeNull = false)
	private Integer numeroInspector;
	
	public UsuarioApm getUsuarioApm() {
		return usuarioApm;
	}
	public void setUsuarioApm(UsuarioApm usuarioApm) {
		this.usuarioApm = usuarioApm;
	}
	public Reparticion getReparticion() {
		return reparticion;
	}
	public void setReparticion(Reparticion reparticion) {
		this.reparticion = reparticion;
	}
	public TipoFormulario getTipoFormulario() {
		return tipoFormulario;
	}
	public void setTipoFormulario(TipoFormulario tipoFormulario) {
		this.tipoFormulario = tipoFormulario;
	}
	public Integer getNumeroInspector() {
		return numeroInspector;
	}
	public void setNumeroInspector(Integer numeroInspector) {
		this.numeroInspector = numeroInspector;
	}
}