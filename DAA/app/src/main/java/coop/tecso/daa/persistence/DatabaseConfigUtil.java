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

package coop.tecso.daa.persistence;

import java.io.IOException;
import java.sql.SQLException;

import com.j256.ormlite.android.apptools.OrmLiteConfigUtil;

import coop.tecso.daa.domain.aplicacion.Aplicacion;
import coop.tecso.daa.domain.aplicacion.AplicacionBinarioVersion;
import coop.tecso.daa.domain.aplicacion.AplicacionParametro;
import coop.tecso.daa.domain.aplicacion.AplicacionPerfil;
import coop.tecso.daa.domain.aplicacion.AplicacionPerfilSeccion;
import coop.tecso.daa.domain.aplicacion.AplicacionTabla;
import coop.tecso.daa.domain.aplicacion.AplicacionTipoBinario;
import coop.tecso.daa.domain.aplicacion.AreaAplicacionPerfil;
import coop.tecso.daa.domain.base.TablaVersion;
import coop.tecso.daa.domain.gps.HistorialUbicacion;
import coop.tecso.daa.domain.notificacion.EstadoNotificacion;
import coop.tecso.daa.domain.notificacion.Notificacion;
import coop.tecso.daa.domain.notificacion.TipoNotificacion;
import coop.tecso.daa.domain.perfil.AplPerfilSeccionCampo;
import coop.tecso.daa.domain.perfil.AplPerfilSeccionCampoValor;
import coop.tecso.daa.domain.perfil.AplPerfilSeccionCampoValorOpcion;
import coop.tecso.daa.domain.perfil.Campo;
import coop.tecso.daa.domain.perfil.CampoValor;
import coop.tecso.daa.domain.perfil.CampoValorOpcion;
import coop.tecso.daa.domain.perfil.PerfilAcceso;
import coop.tecso.daa.domain.perfil.PerfilAccesoAplicacion;
import coop.tecso.daa.domain.perfil.PerfilAccesoUsuario;
import coop.tecso.daa.domain.perfil.Seccion;
import coop.tecso.daa.domain.seguridad.Area;
import coop.tecso.daa.domain.seguridad.DispositivoMovil;
import coop.tecso.daa.domain.seguridad.Impresora;
import coop.tecso.daa.domain.seguridad.UsuarioApm;
import coop.tecso.daa.domain.seguridad.UsuarioApmDM;
import coop.tecso.daa.domain.seguridad.UsuarioApmImpresora;

public class DatabaseConfigUtil extends OrmLiteConfigUtil {

	/**
	 * Classes to be persisted.
	 */
	public static final Class<?> [] PERSISTENT_CLASSES =  new Class<?> [] 
    { 			
		Area.class,
		Aplicacion.class,
		AplicacionBinarioVersion.class,		
		AplicacionParametro.class,
		AplicacionPerfil.class,
		AplicacionPerfilSeccion.class,
		AplicacionTabla.class,
		AplicacionTipoBinario.class,
		AplPerfilSeccionCampo.class,
		AplPerfilSeccionCampoValor.class,
		AplPerfilSeccionCampoValorOpcion.class,
		Campo.class,
		CampoValor.class,
		CampoValorOpcion.class,
		DispositivoMovil.class,
		EstadoNotificacion.class,
		Notificacion.class,
		PerfilAcceso.class,
		PerfilAccesoAplicacion.class,
		PerfilAccesoUsuario.class,
		Seccion.class,
		TablaVersion.class,
		TipoNotificacion.class,
		UsuarioApm.class,
		HistorialUbicacion.class,
		UsuarioApmDM.class,
		UsuarioApmImpresora.class,
		Impresora.class,
		AreaAplicacionPerfil.class,
	};

	/**
	 * Classes to be migrated.
	 */
	public static final Class<?> [] IMMUTABLE_CLASSES =  new Class<?> [] 
    {
		Notificacion.class,
	};
	
	public static void main(String[] args) throws SQLException, IOException {
		writeConfigFile("db_config.txt", PERSISTENT_CLASSES);		
	}
	
}