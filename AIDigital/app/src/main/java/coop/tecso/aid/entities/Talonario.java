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
import coop.tecso.daa.domain.seguridad.DispositivoMovil;

@DatabaseTable(tableName = "for_talonario")
public class Talonario extends AbstractEntity {

	@DatabaseField(canBeNull = false, foreign = true)
	private DispositivoMovil dispositivoMovil;
	
	@DatabaseField(canBeNull = false, foreign = true)
	private TipoFormulario tipoFormulario;
	
	@DatabaseField(canBeNull = false, foreign = true)
	private Serie serie;
	
	@DatabaseField(canBeNull = false)
	private int valorDesde;
	
	@DatabaseField(canBeNull = false)
	private int valorHasta;
	
	@DatabaseField(canBeNull = false, unique = true)
	private int valor;

	public DispositivoMovil getDispositivoMovil() {
		return dispositivoMovil;
	}

	public void setDispositivoMovil(DispositivoMovil dispositivoMovil) {
		this.dispositivoMovil = dispositivoMovil;
	}

	public TipoFormulario getTipoFormulario() {
		return tipoFormulario;
	}

	public void setTipoFormulario(TipoFormulario tipoFormulario) {
		this.tipoFormulario = tipoFormulario;
	}

	public Serie getSerie() {
		return serie;
	}

	public void setSerie(Serie serie) {
		this.serie = serie;
	}

	public int getValorDesde() {
		return valorDesde;
	}

	public void setValorDesde(int valorDesde) {
		this.valorDesde = valorDesde;
	}

	public int getValorHasta() {
		return valorHasta;
	}

	public void setValorHasta(int valorHasta) {
		this.valorHasta = valorHasta;
	}

	public int getValor() {
		return valor;
	}

	public void setValor(int valor) {
		this.valor = valor;
	}
}