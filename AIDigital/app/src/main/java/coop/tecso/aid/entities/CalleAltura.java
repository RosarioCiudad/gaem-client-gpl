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

@DatabaseTable(tableName = "gis_calleAltura")
public class CalleAltura {

	@DatabaseField(canBeNull = false, foreign = true, columnName="codigo")
	private Calle calle;

	@DatabaseField(canBeNull = false)
	private Integer alturaDesde;

	@DatabaseField(canBeNull = false)
	private Integer alturaHasta;

	@DatabaseField
	private String letraCalle;

	@DatabaseField
	private Boolean bis;
	

	// Constructores
	public CalleAltura() {
		super();
	}

	// Getters y Setters
	public Calle getCalle() {
		return calle;
	}

	public void setCalle(Calle calle) {
		this.calle = calle;
	}

	public Integer getAlturaDesde() {
		return alturaDesde;
	}

	public void setAlturaDesde(Integer alturaDesde) {
		this.alturaDesde = alturaDesde;
	}

	public Integer getAlturaHasta() {
		return alturaHasta;
	}

	public void setAlturaHasta(Integer alturaHasta) {
		this.alturaHasta = alturaHasta;
	}

	public String getLetraCalle() {
		return letraCalle;
	}

	public void setLetraCalle(String letraCalle) {
		this.letraCalle = letraCalle;
	}

	public Boolean getBis() {
		return bis;
	}

	public void setBis(Boolean bis) {
		this.bis = bis;
	}
	
}