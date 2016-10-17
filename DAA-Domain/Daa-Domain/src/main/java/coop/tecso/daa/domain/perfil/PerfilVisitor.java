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

import coop.tecso.daa.domain.aplicacion.AplicacionPerfil;
import coop.tecso.daa.domain.aplicacion.AplicacionPerfilSeccion;

/**
 * 
 * @author tecso.coop
 *
 */
public interface PerfilVisitor {
	
	/**
	 * 
	 * @param perfil
	 */
	public void visit(AplicacionPerfil perfil);

	/**
	 * 
	 * @param seccion
	 */
	public void visit(AplicacionPerfilSeccion seccion);
	
	/**
	 * 
	 * @param campo
	 */
	public void visit(AplPerfilSeccionCampo campo);
	
	/**
	 * 
	 * @param campoValor
	 */
	public void visit(AplPerfilSeccionCampoValor campoValor);
	
	/**
	 * 
	 * @param campoValorOpcion
	 */
	public void visit(AplPerfilSeccionCampoValorOpcion campoValorOpcion);
}