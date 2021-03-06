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

import java.util.Collection;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import coop.tecso.daa.domain.aplicacion.AplicacionPerfilSeccion;
import coop.tecso.daa.domain.base.AbstractEntity;

/**
 * 
 * @author tecso.coop
 *
 */
@DatabaseTable(tableName = "apm_aplPerfilSeccionCampo")
public class AplPerfilSeccionCampo extends AbstractEntity implements PerfilElement {

	@DatabaseField(canBeNull = false, foreign = true)
	private AplicacionPerfilSeccion  aplicacionPerfilSeccion;

	@DatabaseField(canBeNull = false, foreign = true, foreignAutoRefresh = true)
	private Campo  campo;

	@DatabaseField(canBeNull = false)
	private Integer orden;

	@DatabaseField
	private Integer cantidadFilaGrid;
	
	@DatabaseField
	private Integer soloLectura;
	
	@ForeignCollectionField(eager = true, orderColumnName = "orden")
	private Collection<AplPerfilSeccionCampoValor> aplPerfilSeccionCampoValorList;


	public void accept(PerfilVisitor visitor) {
		for (AplPerfilSeccionCampoValor 
				aplPerfilSeccionCampoValor : aplPerfilSeccionCampoValorList) {
			aplPerfilSeccionCampoValor.accept(visitor);
		}	
		visitor.visit(this);
	}

	public AplicacionPerfilSeccion getAplicacionPerfilSeccion() {
		return aplicacionPerfilSeccion;
	}

	public Collection<AplPerfilSeccionCampoValor> getAplPerfilSeccionCampoValorList() {
		return aplPerfilSeccionCampoValorList;
	}

	public Campo getCampo() {
		return campo;
	}

	public Integer getCantidadFilaGrid() {
		return cantidadFilaGrid;
	}

	public Integer getOrden() {
		return orden;
	}

	public void setAplicacionPerfilSeccion(AplicacionPerfilSeccion aplicacionPerfilSeccion) {
		this.aplicacionPerfilSeccion = aplicacionPerfilSeccion;
	}

	public void setAplPerfilSeccionCampoValorList(
			Collection<AplPerfilSeccionCampoValor> aplPerfilSeccionCampoValorList) {
		this.aplPerfilSeccionCampoValorList = aplPerfilSeccionCampoValorList;
	}

	public void setCampo(Campo campo) {
		this.campo = campo;
	}

	public void setCantidadFilaGrid(int cantidadFilaGrid) {
		this.cantidadFilaGrid = cantidadFilaGrid;
	}

	public void setOrden(int orden) {
		this.orden = orden;
	}

	public Integer getSoloLectura() {
		return soloLectura;
	}

	public void setSoloLectura(Integer soloLectura) {
		this.soloLectura = soloLectura;
	}
}