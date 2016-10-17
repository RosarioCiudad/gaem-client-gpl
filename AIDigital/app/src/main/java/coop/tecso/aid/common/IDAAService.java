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

package coop.tecso.aid.common;

import java.util.List;

import coop.tecso.daa.domain.aplicacion.AplicacionParametro;
import coop.tecso.daa.domain.aplicacion.AplicacionPerfil;
import coop.tecso.daa.domain.seguridad.DispositivoMovil;
import coop.tecso.daa.domain.seguridad.Impresora;
import coop.tecso.daa.domain.seguridad.UsuarioApm;

public interface IDAAService {
	
	public UsuarioApm getCurrentUser() throws Exception;
	
	public String getServerURL() throws Exception;
	
	public AplicacionPerfil getAplicacionPerfilById(int aplicacionPerfilId) throws Exception;
	
	public UsuarioApm login(String username, String password) throws Exception;
	
	public DispositivoMovil getDispositivoMovil() throws Exception;
	
	public Impresora getImpresora() throws Exception;
	
	public void changeSession(String username, String password) throws Exception; 
	
	public boolean hasAccess(int usuarioID, String codAplicacion) throws Exception; 
	
	public AplicacionParametro getAplicacionParametroByCodigo(String codParametro, String codAplicacion) throws Exception;
	
	public Integer getIdAplicacionPerfilDefaultBy(String codAplicacion) throws Exception;
	
	public List<AplicacionPerfil> getListAplicacionPerfilByAplicacion(String codigoAplicacion) throws Exception;
	
	public String signData(String data) throws Exception;
}