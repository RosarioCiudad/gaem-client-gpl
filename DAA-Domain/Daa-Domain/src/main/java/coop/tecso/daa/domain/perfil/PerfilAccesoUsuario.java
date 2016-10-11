package coop.tecso.daa.domain.perfil;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import coop.tecso.daa.domain.base.AbstractEntity;
import coop.tecso.daa.domain.seguridad.UsuarioApm;

/**
 * 
 * @author tecso.coop
 *
 */
@DatabaseTable(tableName = "apm_perfilAccesoUsuario")
public class PerfilAccesoUsuario extends AbstractEntity {
	
	@DatabaseField(canBeNull = false, foreign = true)
	private PerfilAcceso perfilAcceso;

	@DatabaseField(canBeNull = false, foreign = true)
	private UsuarioApm usuarioAPM;

	public PerfilAcceso getPerfilAcceso() {
		return perfilAcceso;
	}

	public UsuarioApm getUsuarioAPM() {
		return usuarioAPM;
	}

	public void setPerfilAcceso(PerfilAcceso perfilAcceso) {
		this.perfilAcceso = perfilAcceso;
	}

	public void setUsuarioAPM(UsuarioApm usuarioAPM) {
		this.usuarioAPM = usuarioAPM;
	}
}