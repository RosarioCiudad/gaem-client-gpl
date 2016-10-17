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

import java.util.Date;

/**
 * Notificacion para Informe Medico (notificaciones de tipo Generales, Alertas Medicas o Errores)
 * 
 * @author tecso
 *
 */
public class FormNotification {

	private int id;
	private String code;
	private FormNotificationType formNotificationType;
	private Integer atencionId;
	private Date date;
	private String briefDescription;
	private String fullDescription;
	private int icon;	// TODO ver bien tipo de dato
	private int sound;   // TODO ver bien tipo de dato
	private boolean readed;
	private boolean alwaysVisible;
	private boolean readForced;
	
	// Constructors
	public FormNotification() {
		super();
	}
	
	public FormNotification(int id, String code, FormNotificationType formNotificationType,
			Integer atencionId, Date date, String briefDescription,
			String fullDescription, Integer icono, Integer sound, boolean readed,
			boolean alwaysVisible, boolean readForced) {
		super();
		this.id = id;
		this.code = code;
		this.formNotificationType = formNotificationType;
		this.atencionId = atencionId;
		this.date = date;
		this.briefDescription = briefDescription;
		this.fullDescription = fullDescription;
		this.icon = icono;
		this.sound = sound;
		this.readed = readed;
		this.alwaysVisible = alwaysVisible;
		this.readForced = readForced;
	}

	// Getters And Setters
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public FormNotificationType getFormNotificationType() {
		return formNotificationType;
	}
	public void setFormNotificationType(FormNotificationType formNotificationType) {
		this.formNotificationType = formNotificationType;
	}
	public Integer getAtencionId() {
		return atencionId;
	}
	public void setAtencionId(Integer atencionId) {
		this.atencionId = atencionId;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getBriefDescription() {
		return briefDescription;
	}
	public void setBriefDescription(String briefDescription) {
		this.briefDescription = briefDescription;
	}
	public String getFullDescription() {
		return fullDescription;
	}
	public void setFullDescription(String fullDescription) {
		this.fullDescription = fullDescription;
	}
	public int getIcon() {
		return icon;
	}
	public void setIcon(Integer icon) {
		this.icon = icon;
	}
	public int getSound() {
		return sound;
	}
	public void setSound(Integer sound) {
		this.sound = sound;
	}
	public boolean isReaded() {
		return readed;
	}
	public void setReaded(boolean readed) {
		this.readed = readed;
	}
	public boolean isAlwaysVisible() {
		return alwaysVisible;
	}
	public void setAlwaysVisible(boolean alwaysVisible) {
		this.alwaysVisible = alwaysVisible;
	}
	public boolean isReadForced() {
		return readForced;
	}
	public void setReadForced(boolean readForced) {
		this.readForced = readForced;
	}
}