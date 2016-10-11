package coop.tecso.daa.domain.aplicacion;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import coop.tecso.daa.domain.base.AbstractEntity;

/**
 * 
 * @author tecso.coop
 *
 */
@DatabaseTable(tableName = "apm_aplicacion")
public class Aplicacion extends AbstractEntity {

	@DatabaseField(canBeNull = false)
	private String codigo;
	
	@DatabaseField(canBeNull = false)
	private String descripcion;
	
	@DatabaseField(canBeNull = true)
	private String packageName;
	
	@DatabaseField(canBeNull = true)
	private String className;
	
	public String getCodigo() {
		return codigo;
	}
	
	public String getDescripcion() {
		return descripcion;
	}
	
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	@Override
	public String toString() {
		StringBuilder buffer = new StringBuilder();
		buffer.append(String.format("Entidad --> {%s} ", getClass().getSimpleName()));
		buffer.append(String.format("Codigo: {%s}, ", codigo));
		buffer.append(String.format("Descripcion: {%s}, ", descripcion));
		buffer.append(String.format("PackageName: {%s}, ", packageName));
		buffer.append(String.format("ClassName: {%s}, ", className));
		buffer.append("\n");
		return buffer.toString();
	}
}
