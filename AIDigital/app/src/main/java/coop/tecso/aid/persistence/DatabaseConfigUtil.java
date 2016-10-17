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

package coop.tecso.aid.persistence;

import java.io.IOException;
import java.sql.SQLException;

import com.j256.ormlite.android.apptools.OrmLiteConfigUtil;

import coop.tecso.aid.entities.Alcoholimetro;
import coop.tecso.aid.entities.Calle;
import coop.tecso.aid.entities.CalleAltura;
import coop.tecso.aid.entities.CalleInterseccion;
import coop.tecso.aid.entities.ClaseLicencia;
import coop.tecso.aid.entities.EstadoTipoFormulario;
import coop.tecso.aid.entities.Formulario;
import coop.tecso.aid.entities.FormularioDetalle;
import coop.tecso.aid.entities.Infraccion;
import coop.tecso.aid.entities.LineaTUP;
import coop.tecso.aid.entities.Localidad;
import coop.tecso.aid.entities.MarcaVehiculo;
import coop.tecso.aid.entities.Medico;
import coop.tecso.aid.entities.MotivoAnulacionTipoFormulario;
import coop.tecso.aid.entities.MotivoCierreTipoFormulario;
import coop.tecso.aid.entities.Pais;
import coop.tecso.aid.entities.Panico;
import coop.tecso.aid.entities.Reparticion;
import coop.tecso.aid.entities.Serie;
import coop.tecso.aid.entities.Talonario;
import coop.tecso.aid.entities.TelefonoPanico;
import coop.tecso.aid.entities.TipoDocumento;
import coop.tecso.aid.entities.TipoFormulario;
import coop.tecso.aid.entities.TipoVehiculo;
import coop.tecso.aid.entities.UsuarioApmReparticion;
import coop.tecso.daa.domain.aplicacion.AplicacionBinarioVersion;
import coop.tecso.daa.domain.aplicacion.AplicacionParametro;
import coop.tecso.daa.domain.base.TablaVersion;
import coop.tecso.daa.domain.seguridad.DispositivoMovil;
import coop.tecso.daa.domain.seguridad.UsuarioApm;

public class DatabaseConfigUtil extends OrmLiteConfigUtil {

	/**
	 * Classes to be persisted.
	 */
	public static final Class<?> [] PERSISTENT_CLASSES =  new Class<?> [] 
    {
		Formulario.class,
		FormularioDetalle.class,
		TipoFormulario.class,
		EstadoTipoFormulario.class,
		AplicacionParametro.class,
		MotivoCierreTipoFormulario.class,
		MotivoAnulacionTipoFormulario.class,
		UsuarioApmReparticion.class,
		Reparticion.class,
		Serie.class,
		Talonario.class,
		AplicacionBinarioVersion.class,
		Calle.class,
		CalleAltura.class,
		CalleInterseccion.class,
		Infraccion.class,
		TipoVehiculo.class,
		MarcaVehiculo.class,
		TipoDocumento.class,
		ClaseLicencia.class,
		Pais.class,
		LineaTUP.class,
		Localidad.class,
		UsuarioApm.class, 
		DispositivoMovil.class, 
		Panico.class,
		TelefonoPanico.class,
		TablaVersion.class,
		Medico.class,
		Alcoholimetro.class
	};
	
	/**
	 * Classes to be migrated.
	 */
	public static final Class<?> [] IMMUTABLE_CLASSES =  new Class<?> [] 
    {
		Formulario.class,
		FormularioDetalle.class
	};

	public static void main(String[] args) throws SQLException, IOException {
		writeConfigFile("db_config.txt", PERSISTENT_CLASSES);
	}
}