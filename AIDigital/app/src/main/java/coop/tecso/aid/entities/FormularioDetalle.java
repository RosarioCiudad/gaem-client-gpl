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

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import coop.tecso.daa.domain.base.AbstractEntity;
import coop.tecso.daa.domain.perfil.AplPerfilSeccionCampo;
import coop.tecso.daa.domain.perfil.AplPerfilSeccionCampoValor;
import coop.tecso.daa.domain.perfil.AplPerfilSeccionCampoValorOpcion;


@DatabaseTable(tableName = "for_formularioDetalle")
public class FormularioDetalle extends AbstractEntity {

	@DatabaseField(canBeNull = false, foreign = true)
	private Formulario formulario;
	
	@DatabaseField(canBeNull = false, foreign = true, foreignAutoRefresh = false)
	private AplPerfilSeccionCampo tipoFormularioDefSeccionCampo;
	
	@DatabaseField(foreign = true, foreignAutoRefresh = false)
	private AplPerfilSeccionCampoValor tipoFormularioDefSeccionCampoValor;
	
	@DatabaseField(foreign = true, foreignAutoRefresh = false)
	private AplPerfilSeccionCampoValorOpcion tipoFormularioDefSeccionCampoValorOpcion;
	
	@DatabaseField
	private String valor;
	
	@DatabaseField(dataType = DataType.BYTE_ARRAY)
	private byte[] imagen;

	// Getters & Setters
	public Formulario getFormulario() {
		return formulario;
	}

	public void setFormulario(Formulario formulario) {
		this.formulario = formulario;
	}

	public AplPerfilSeccionCampo getTipoFormularioDefSeccionCampo() {
		return tipoFormularioDefSeccionCampo;
	}

	public void setTipoFormularioDefSeccionCampo(
			AplPerfilSeccionCampo tipoFormularioDefSeccionCampo) {
		this.tipoFormularioDefSeccionCampo = tipoFormularioDefSeccionCampo;
	}

	public AplPerfilSeccionCampoValor getTipoFormularioDefSeccionCampoValor() {
		return tipoFormularioDefSeccionCampoValor;
	}

	public void setTipoFormularioDefSeccionCampoValor(
			AplPerfilSeccionCampoValor tipoFormularioDefSeccionCampoValor) {
		this.tipoFormularioDefSeccionCampoValor = tipoFormularioDefSeccionCampoValor;
	}

	public AplPerfilSeccionCampoValorOpcion getTipoFormularioDefSeccionCampoValorOpcion() {
		return tipoFormularioDefSeccionCampoValorOpcion;
	}

	public void setTipoFormularioDefSeccionCampoValorOpcion(
			AplPerfilSeccionCampoValorOpcion tipoFormularioDefSeccionCampoValorOpcion) {
		this.tipoFormularioDefSeccionCampoValorOpcion = tipoFormularioDefSeccionCampoValorOpcion;
	}

	public String getValor() {
		return valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}

	public byte[] getImagen() {
		return imagen;
	}

	public void setImagen(byte[] imagen) {
		this.imagen = imagen;
	}
}