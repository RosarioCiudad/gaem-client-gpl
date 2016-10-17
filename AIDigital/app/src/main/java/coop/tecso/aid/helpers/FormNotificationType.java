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

package coop.tecso.aid.helpers;

/**
 * Enumeracion de Tipo de Notificaciones para Informes Medicos
 *
 * <p> - General </p>
 * <p> - Medicas </p>
 * <p> - Error </p>
 * <p> - Error Grave </p>
 * 
 * @author tecso
 *
 */
public enum FormNotificationType {
	GENERAL(1,"Notificación General"), 		// Notificaciones Generales (recibidas de la UDAA)
	MEDICAS(2,"Alertas Médicas"), 		// Alertas Medicas evaluadas del IM
	ERROR(3,"Error"), 			// Errores
	ERRORGRAVE(4,"Error Grave"), 	// Errores Graves
	UNKOWN(0,"Desconocido");		// Tipo Desconocido
	
	private Integer id;
	private String value;
	

	private FormNotificationType(Integer id, String value) {
		this.id = id;
		this.value = value;
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}

	public static FormNotificationType getById(Integer id){
		FormNotificationType[] notificationTypes = FormNotificationType.values();
		for (int i = 0; i < notificationTypes.length; i++) {
			if(notificationTypes[i].getId().equals(id)){
				return notificationTypes[i];
			}
		}
		return UNKOWN;
	}
	
	@Override
	public String toString() {
		return this.value;
	}
}