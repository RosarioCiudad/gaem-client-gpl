package coop.tecso.daa.domain.aplicacion;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import coop.tecso.daa.domain.base.AbstractEntity;

/**
 * 
 * @author tecso.coop
 *
 */
@DatabaseTable(tableName = "apm_aplicacionParametro")
public class AplicacionParametro extends AbstractEntity {
	
	@DatabaseField(canBeNull = false, foreign = true)
	private Aplicacion aplicacion;
	
	@DatabaseField(canBeNull = false)
	private String codigo;
	
	@DatabaseField
	private String descripcion;
	
	@DatabaseField(canBeNull = false)
	private String valor;

	public Aplicacion getAplicacion() {
		return aplicacion;
	}

	public String getCodigo() {
		return codigo;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public String getValor() {
		return valor;
	}

	public void setAplicacion(Aplicacion aplicacion) {
		this.aplicacion = aplicacion;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}
	
	@Override
	public String toString() {
		StringBuilder buffer = new StringBuilder();
		buffer.append(String.format("Entidad --> {%s} ", getClass().getSimpleName()));
		buffer.append(String.format("Aplicacion: {%s}, ", aplicacion.getDescripcion()));
		buffer.append(String.format("Codigo: {%s}, ", codigo));
		buffer.append(String.format("Descripcion: {%s}, ", descripcion));
		buffer.append(String.format("Valor: {%s}, ", valor));
		buffer.append("\n");
		return buffer.toString();
	}

}