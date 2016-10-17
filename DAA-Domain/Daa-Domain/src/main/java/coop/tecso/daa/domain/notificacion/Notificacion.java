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

package coop.tecso.daa.domain.notificacion;

import java.util.Date;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import coop.tecso.daa.domain.aplicacion.Aplicacion;
import coop.tecso.daa.domain.base.AbstractEntity;

/**
 * 
 * @author tecso.coop
 *
 */
@DatabaseTable(tableName = "not_notificacion")
public class Notificacion extends AbstractEntity {
	
	@DatabaseField(canBeNull = false, foreign = true, foreignAutoRefresh = true)
	private TipoNotificacion tipoNotificacion;

	@DatabaseField(canBeNull = false, foreign = true, foreignAutoRefresh = true)
	private EstadoNotificacion estadoNotificacion;
	
	@DatabaseField(canBeNull = true, foreign = true)
	private Aplicacion aplicacion;

	@DatabaseField
	private int numeroAplicacion;
	
	@DatabaseField
	private Date fecha;
	
	@DatabaseField
	private String descripcionReducida;

	@DatabaseField
	private String descripcionAmpliada;
	
	@DatabaseField
	private Date fechaDesde;
	
	@DatabaseField
	private Date fechaHasta;
	
	@DatabaseField(canBeNull = true)
	private Date fechaEnvio;
	
	@DatabaseField(canBeNull = true)
	private Date fechaRecepcion; 
	
	@DatabaseField(canBeNull = true)
	private boolean canalContingencia;
	
	public Aplicacion getAplicacion() {
		return aplicacion;
	}

	public String getDescripcionAmpliada() {
		return descripcionAmpliada;
	}

	public String getDescripcionReducida() {
		return descripcionReducida;
	}

	public EstadoNotificacion getEstadoNotificacion() {
		return estadoNotificacion;
	}

	public int getNumeroAplicacion() {
		return numeroAplicacion;
	}

	public void setNumeroAplicacion(int numeroAplicacion) {
		this.numeroAplicacion = numeroAplicacion;
	}

	public Date getFecha() {
		return fecha;
	}

	public Date getFechaDesde() {
		return fechaDesde;
	}

	public Date getFechaHasta() {
		return fechaHasta;
	}

	public TipoNotificacion getTipoNotificacion() {
		return tipoNotificacion;
	}

	public void setAplicacion(Aplicacion aplicacion) {
		this.aplicacion = aplicacion;
	}

	public void setDescripcionAmpliada(String descripcionAmpliada) {
		this.descripcionAmpliada = descripcionAmpliada;
	}

	public void setDescripcionReducida(String descripcionReducida) {
		this.descripcionReducida = descripcionReducida;
	}

	public void setEstadoNotificacion(EstadoNotificacion estadoNotificacion) {
		this.estadoNotificacion = estadoNotificacion;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public void setFechaDesde(Date fechaDesde) {
		this.fechaDesde = fechaDesde;
	}

	public void setFechaHasta(Date fechaHasta) {
		this.fechaHasta = fechaHasta;
	}

	public void setTipoNotificacion(TipoNotificacion tipoNotificacion) {
		this.tipoNotificacion = tipoNotificacion;
	}

	public Date getFechaEnvio() {
		return fechaEnvio;
	}

	public void setFechaEnvio(Date fechaEnvio) {
		this.fechaEnvio = fechaEnvio;
	}

	public Date getFechaRecepcion() {
		return fechaRecepcion;
	}

	public void setFechaRecepcion(Date fechaRecepcion) {
		this.fechaRecepcion = fechaRecepcion;
	}

	public boolean isCanalContingencia() {
		return canalContingencia;
	}

	public void setCanalContingencia(boolean canalContingencia) {
		this.canalContingencia = canalContingencia;
	}
	
	
	@Override
	public String toString() {
		StringBuilder output = new StringBuilder();
		output.append(String.format("Entidad --> {%s} ", getClass().getSimpleName()));
		output.append(String.format("TipoNotificacion: {%s}, ", getTipoNotificacion()));
		output.append(String.format("EstadoNotificacion: {%s}, ", getEstadoNotificacion()));
		output.append(String.format("Aplicacion: {%s}, ", getAplicacion()));
		output.append(String.format("Numero Aplicacion: {%s}, ", getNumeroAplicacion()));
		output.append(String.format("Fecha Generacion: {%s}, ", getFecha()));
		output.append(String.format("Descripcion Reducida: {%s}, ", getDescripcionReducida()));
		output.append(String.format("Descripcion Ampliada: {%s}, ", getDescripcionAmpliada()));
		output.append(String.format("Fecha Desde: {%s}, ", getFechaDesde()));
		output.append(String.format("Fecha Hasta: {%s}, ", getFechaHasta()));
		output.append(String.format("Fecha Envio: {%s}, ", getFechaEnvio()));
		output.append(String.format("Fecha Recepcion: {%s}, ", getFechaRecepcion()));
		output.append(String.format("Canal Contingencia: {%s}, ", Boolean.toString(isCanalContingencia())));
		output.append("\n");
		return output.toString();
	}
}