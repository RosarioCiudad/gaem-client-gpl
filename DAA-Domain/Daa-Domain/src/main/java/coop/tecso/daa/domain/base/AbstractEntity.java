package coop.tecso.daa.domain.base;

import java.util.Date;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;

/**
 * 
 * @author tecso.coop
 *
 */
public abstract class AbstractEntity {

	@DatabaseField(generatedId = true, id = false, allowGeneratedIdInsert = true)
	private int id;

	@DatabaseField(canBeNull = false)
	private String usuario;
	
	@DatabaseField(canBeNull = false, dataType = DataType.DATE_LONG)
	private Date fechaUltMdf;
	
	@DatabaseField(canBeNull = false)
	private int estado;
	
	@DatabaseField(canBeNull = false)
	private int version;
	
	public AbstractEntity() {}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public Date getFechaUltMdf() {
		return fechaUltMdf;
	}

	public void setFechaUltMdf(Date fechaUltMdf) {
		this.fechaUltMdf = fechaUltMdf;
	}

	public int getEstado() {
		return estado;
	}

	public void setEstado(int estado) {
		this.estado = estado;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}
}