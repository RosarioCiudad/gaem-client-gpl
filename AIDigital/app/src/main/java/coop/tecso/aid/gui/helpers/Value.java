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

package coop.tecso.aid.gui.helpers;

import coop.tecso.daa.domain.perfil.AplPerfilSeccionCampo;
import coop.tecso.daa.domain.perfil.AplPerfilSeccionCampoValor;
import coop.tecso.daa.domain.perfil.AplPerfilSeccionCampoValorOpcion;

public class Value {

	private AplPerfilSeccionCampo campo = null;
	private AplPerfilSeccionCampoValor campoValor = null;
	private AplPerfilSeccionCampoValorOpcion campoValorOpcion = null;
	private String valor = null;
	private byte[] imagen;
	
	public Value() {
		super();
	}

	public Value(AplPerfilSeccionCampo campo, 
			AplPerfilSeccionCampoValor campoValor, 
			AplPerfilSeccionCampoValorOpcion campoValorOpcion, String valor) {
		super();
		this.campo = campo;
		this.campoValor = campoValor;
		this.campoValorOpcion = campoValorOpcion;
		this.valor = valor;
	}

	public Value(AplPerfilSeccionCampo campo, 
			AplPerfilSeccionCampoValor campoValor, 
			AplPerfilSeccionCampoValorOpcion campoValorOpcion, String valor, byte[] imagen) {
		super();
		this.campo = campo;
		this.campoValor = campoValor;
		this.campoValorOpcion = campoValorOpcion;
		this.valor = valor;
		this.imagen = imagen;
	}

	public AplPerfilSeccionCampo getCampo() {
		return campo;
	}
	public void setCampo(AplPerfilSeccionCampo campo) {
		this.campo = campo;
	}
	public AplPerfilSeccionCampoValor getCampoValor() {
		return campoValor;
	}
	public void setCampoValor(AplPerfilSeccionCampoValor campoValor) {
		this.campoValor = campoValor;
	}
	public AplPerfilSeccionCampoValorOpcion getCampoValorOpcion() {
		return campoValorOpcion;
	}
	public void setCampoValorOpcion(
			AplPerfilSeccionCampoValorOpcion campoValorOpcion) {
		this.campoValorOpcion = campoValorOpcion;
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