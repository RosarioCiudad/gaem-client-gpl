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