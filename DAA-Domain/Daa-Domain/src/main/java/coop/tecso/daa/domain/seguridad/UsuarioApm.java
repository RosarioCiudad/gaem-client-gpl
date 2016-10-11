package coop.tecso.daa.domain.seguridad;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import coop.tecso.daa.domain.base.AbstractEntity;

/**
 * 
 * @author tecso.coop
 *
 */
@DatabaseTable(tableName = "apm_usuarioApm")
public class UsuarioApm extends AbstractEntity {
	
	@DatabaseField(canBeNull = false)
	private String nombre;
		
	@DatabaseField(canBeNull = false)
	private String username;
	
	@DatabaseField(canBeNull = true)
	private String password;

	@DatabaseField(canBeNull = true)
	private String usertoken;
	
	@DatabaseField(canBeNull = true)
	private String usercert;

	// Getters & Setters
	public String getNombre() {
		return nombre;
	}

	public String getPassword() {
		return password;
	}

	public String getUsername() {
		return username;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getUsertoken() {
		return usertoken;
	}

	public void setUsertoken(String usertoken) {
		this.usertoken = usertoken;
	}

	public String getUsercert() {
		return usercert;
	}

	public void setUsercert(String usercert) {
		this.usercert = usercert;
	}
}