package coop.tecso.daa.domain.aplicacion;

import java.util.Collection;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import coop.tecso.daa.domain.base.AbstractEntity;
import coop.tecso.daa.domain.perfil.AplPerfilSeccionCampo;
import coop.tecso.daa.domain.perfil.PerfilElement;
import coop.tecso.daa.domain.perfil.PerfilVisitor;
import coop.tecso.daa.domain.perfil.Seccion;

/**
 * 
 * @author tecso.coop
 *
 */
@DatabaseTable(tableName = "apm_aplicacionPerfilSeccion")
public class AplicacionPerfilSeccion extends AbstractEntity implements PerfilElement {

	@DatabaseField(canBeNull = false, foreign = true)
	private AplicacionPerfil aplicacionPerfil;
	
	@DatabaseField(canBeNull = false, foreign = true, foreignAutoRefresh = true)
	private Seccion seccion;
	
	@DatabaseField(canBeNull = false)
	private Integer orden;

	@DatabaseField
	private Integer noDesplegar;
	
	@DatabaseField
	private Integer opcional;
	
	@ForeignCollectionField(eager = true, orderColumnName = "orden")
	private Collection<AplPerfilSeccionCampo> aplPerfilSeccionCampoList;

	@Override
	public void accept(PerfilVisitor visitor) {
		for (AplPerfilSeccionCampo aplPerfilSeccionCampo : aplPerfilSeccionCampoList) {
			aplPerfilSeccionCampo.accept(visitor);
		}	
		visitor.visit(this);
	}

	public AplicacionPerfil getAplicacionPerfil() {
		return aplicacionPerfil;
	}

	public Collection<AplPerfilSeccionCampo> getAplPerfilSeccionCampoList() {
		return aplPerfilSeccionCampoList;
	}

	public int getOrden() {
		return orden;
	}

	public Seccion getSeccion() {
		return seccion;
	}

	void setAplicacionPerfil(AplicacionPerfil aplicacionPerfil) {
		this.aplicacionPerfil = aplicacionPerfil;
	}

	public void setAplPerfilSeccionCampoList(
			Collection<AplPerfilSeccionCampo> aplPerfilSeccionCampoList) {
		this.aplPerfilSeccionCampoList = aplPerfilSeccionCampoList;
	}

	public void setOrden(int orden) {
		this.orden = orden;
	}
	
	public void setSeccion(Seccion seccion) {
		this.seccion = seccion;
	}

	public Integer getNoDesplegar() {
		return noDesplegar;
	}

	public void setNoDesplegar(Integer noDesplegar) {
		this.noDesplegar = noDesplegar;
	}

	public Integer getOpcional() {
		return opcional;
	}

	public void setOpcional(Integer opcional) {
		this.opcional = opcional;
	}
}