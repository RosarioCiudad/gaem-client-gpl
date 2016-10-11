package coop.tecso.aid.entities;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import coop.tecso.daa.domain.base.AbstractEntity;
import coop.tecso.daa.domain.seguridad.UsuarioApm;

/**
 * 
 * @author tecso.coop
 *
 */
@DatabaseTable(tableName = "aid_usuarioApmReparticion")
public class UsuarioApmReparticion extends AbstractEntity {
	
	@DatabaseField(canBeNull = false, foreign = true)
	private UsuarioApm usuarioApm;
	
	@DatabaseField(canBeNull = false, foreign = true)
	private Reparticion reparticion;
	
	@DatabaseField(canBeNull = false, foreign = true)
	private TipoFormulario tipoFormulario;
	
	@DatabaseField(canBeNull = false)
	private Integer numeroInspector;
	
	public UsuarioApm getUsuarioApm() {
		return usuarioApm;
	}
	public void setUsuarioApm(UsuarioApm usuarioApm) {
		this.usuarioApm = usuarioApm;
	}
	public Reparticion getReparticion() {
		return reparticion;
	}
	public void setReparticion(Reparticion reparticion) {
		this.reparticion = reparticion;
	}
	public TipoFormulario getTipoFormulario() {
		return tipoFormulario;
	}
	public void setTipoFormulario(TipoFormulario tipoFormulario) {
		this.tipoFormulario = tipoFormulario;
	}
	public Integer getNumeroInspector() {
		return numeroInspector;
	}
	public void setNumeroInspector(Integer numeroInspector) {
		this.numeroInspector = numeroInspector;
	}
}