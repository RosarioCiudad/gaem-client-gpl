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

import java.util.Collection;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import coop.tecso.daa.domain.base.AbstractEntity;
import coop.tecso.daa.domain.perfil.PerfilElement;
import coop.tecso.daa.domain.perfil.PerfilVisitor;

/**
 * 
 * @author tecso.coop
 *
 */
@DatabaseTable(tableName = "apm_aplicacionPerfil")
public class AplicacionPerfil extends AbstractEntity implements PerfilElement {

	@DatabaseField(canBeNull = false, foreign = true)
	private Aplicacion aplicacion;

	@DatabaseField
	private String descripcion; 
	
	@DatabaseField
	private String printingTemplate;
	
	@ForeignCollectionField(eager = true, orderColumnName = "orden")
	private Collection<AplicacionPerfilSeccion> apPerfilSeccionList;

	@Override
	public void accept(PerfilVisitor visitor) {
		for (AplicacionPerfilSeccion apPerfilSeccion : apPerfilSeccionList) {
			apPerfilSeccion.accept(visitor);
		}	
		visitor.visit(this);
	}

	public Aplicacion getAplicacion() {
		return aplicacion;
	}

	public Collection<AplicacionPerfilSeccion> getApPerfilSeccionList() {
		return apPerfilSeccionList;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setAplicacion(Aplicacion aplicacion) {
		this.aplicacion = aplicacion;
	}

	public void setApPerfilSeccionList(
			Collection<AplicacionPerfilSeccion> apPerfilSeccionList) {
		this.apPerfilSeccionList = apPerfilSeccionList;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getPrintingTemplate() {
		return printingTemplate;
	}

	public void setPrintingTemplate(String printingTemplate) {
		this.printingTemplate = printingTemplate;
	}
}