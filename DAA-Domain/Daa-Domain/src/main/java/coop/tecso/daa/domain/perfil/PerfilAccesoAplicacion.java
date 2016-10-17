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

package coop.tecso.daa.domain.perfil;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import coop.tecso.daa.domain.aplicacion.Aplicacion;
import coop.tecso.daa.domain.base.AbstractEntity;

/**
 * 
 * @author tecso.coop
 *
 */
@DatabaseTable(tableName = "apm_perfilAccesoAplicacion")
public class PerfilAccesoAplicacion extends AbstractEntity {
	
	@DatabaseField(canBeNull = false, foreign = true)
	private PerfilAcceso perfilAcceso;

	@DatabaseField(canBeNull = false, foreign = true)
	private Aplicacion aplicacion;

	public Aplicacion getAplicacion() {
		return aplicacion;
	}

	public PerfilAcceso getPerfilAcceso() {
		return perfilAcceso;
	}

	public void setAplicacion(Aplicacion aplicacion) {
		this.aplicacion = aplicacion;
	}

	public void setPerfilAcceso(PerfilAcceso perfilAcceso) {
		this.perfilAcceso = perfilAcceso;
	}	
}