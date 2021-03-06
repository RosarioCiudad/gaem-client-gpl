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

package coop.tecso.aid.utils;

/**
 * 
 * Constantes generales
 * 
 * @author tecso
 *
 */
public class Constants {
	
	public static final String COD_APPLICATION = "AIDigital";

	public static final String ACTION = "action";
	
	public static final String ACTION_CREATE = "create";
	public static final String ACTION_UPDATE = "update";
	
	public static final String ENTITY_ID = "ID";
	public static final String ENTITY_TYPE_ID = "TypeID";
	
	public static final String CHECK_PRINTER = "CheckPrinter";
	
	// Codigo de Preferencias
	public static final String PREF_DEFAULT_ORIENTATION = "orientation";
	
	// Block Message
//	public static final String ACTION_BLOCK_APPLICATION   = "coop.tecso.daa.custom.intent.action.BLOCK_APPLICATION";
	// UnBlock Message
//	public static final String ACTION_UNBLOCK_APPLICATION = "coop.tecso.daa.custom.intent.action.UNBLOCK_APPLICATION";
	// UnBlock Message
//	public static final String ACTION_UNBLOCK_UDAA_APPLICATION = "coop.tecso.daa.custom.intent.action.UNBLOCK_UDAA_APPLICATION";
	// Lose Session Message
	public static final String ACTION_LOSE_SESSION = "coop.tecso.daa.custom.intent.action.LOSE_SESSION";
	// New Notification Message
	public static final String ACTION_NEW_NOTIFICATION = "coop.tecso.daa.custom.intent.action.NEW_NOTIFICATION";
	
	
	// Print Form
	public static final String ACTION_PRINT_FORM = "coop.tecso.daa.custom.intent.action.PRINT_FORM";
	public static final String ACTION_PRINT_FORM_SUCCESS = "coop.tecso.daa.custom.intent.action.PRINT_FORM_SUCCESS";
	public static final String ACTION_PRINT_FORM_ERROR = "coop.tecso.daa.custom.intent.action.PRINT_FORM_ERROR";
	
	//Session
	public static final String COD_SESSION_TIMEOUT = "SessionTimeOut";
	//
	public static final String COD_TIPOBINARIO_DATABASE = "Database";
	
	// Close Form
	public static final String COD_MOTIVO_CIERRE = "MotivoCierreID";
	public static final String COD_CANT_IMPRESIONES = "CantidadImpresionesID";

	// Anular Form
	public static final String COD_MOTIVO_ANULACION = "MotivoAnulacionID";
}