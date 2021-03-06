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

import coop.tecso.daa.domain.base.AbstractEntity;

/**
 * 
 * @author tecso.coop
 *
 */
@DatabaseTable(tableName = "apm_campo")
public class Campo extends AbstractEntity {

	@DatabaseField(canBeNull = false)
	private String etiqueta;
	
	@DatabaseField(canBeNull = false)
	private int obligatorio;

	@DatabaseField(canBeNull = false)
	private Integer tratamiento;

	@DatabaseField
	private String valorDefault;

	@DatabaseField
	private int soloLectura;
	
	@DatabaseField
	private String tablaBusqueda;
	
	@DatabaseField
	private String mascaraVisual;
	
	@ForeignCollectionField(eager = true)
	private Collection<CampoValor> campoValorList;
	
	public Collection<CampoValor> getCampoValorList() {
		return campoValorList;
	}

	public String getEtiqueta() {
		return etiqueta;
	}

	public String getTablaBusqueda() {
		return tablaBusqueda;
	}

	public Integer getTratamiento() {
		return tratamiento;
	}

	public String getValorDefault() {
		return valorDefault;
	}

	public int getObligatorio() {
		return obligatorio;
	}

	public int getSoloLectura() {
		return soloLectura;
	}

	public void setCampoValorList(Collection<CampoValor> campoValorList) {
		this.campoValorList = campoValorList;
	}

	public void setEtiqueta(String etiqueta) {
		this.etiqueta = etiqueta;
	}

	public void setObligatorio(int obligatorio) {
		this.obligatorio = obligatorio;
	}

	public void setSoloLectura(int soloLectura) {
		this.soloLectura = soloLectura;
	}

	public void setTablaBusqueda(String tablaBusqueda) {
		this.tablaBusqueda = tablaBusqueda;
	}

	public void setTratamiento(Integer tratamiento) {
		this.tratamiento = tratamiento;
	}

	public void setValorDefault(String valorDefault) {
		this.valorDefault = valorDefault;
	}

	public String getMascaraVisual() {
		return mascaraVisual;
	}

	public void setMascaraVisual(String mascaraVisual) {
		this.mascaraVisual = mascaraVisual;
	}
}