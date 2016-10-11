package coop.tecso.daa.domain.perfil;

import java.util.Collection;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import coop.tecso.daa.domain.base.AbstractEntity;

/**
 * 
 * @author tecso.coop
 *
 */
@DatabaseTable(tableName = "apm_aplPerfilSeccionCampoValor")
public class AplPerfilSeccionCampoValor extends AbstractEntity implements PerfilElement {

	@DatabaseField(canBeNull = false, foreign = true)
	private AplPerfilSeccionCampo  aplPerfilSeccionCampo;

	@DatabaseField(canBeNull = false, foreign = true, foreignAutoRefresh = true)
	private CampoValor  campoValor;

	@DatabaseField(canBeNull = false)
	private Integer orden;
	
	@ForeignCollectionField(eager = true, orderColumnName = "orden")
	private Collection<AplPerfilSeccionCampoValorOpcion> aplPerfilSeccionCampoValorOpcionList;

	public void accept(PerfilVisitor visitor) {
		for (AplPerfilSeccionCampoValorOpcion 
				aplPerfilSeccionCampoValorOpcion : aplPerfilSeccionCampoValorOpcionList) {
			aplPerfilSeccionCampoValorOpcion.accept(visitor);
		}	
		visitor.visit(this);
	}

	public AplPerfilSeccionCampo getAplPerfilSeccionCampo() {
		return aplPerfilSeccionCampo;
	}

	public Collection<AplPerfilSeccionCampoValorOpcion> getAplPerfilSeccionCampoValorOpcionList() {
		return aplPerfilSeccionCampoValorOpcionList;
	}

	public CampoValor getCampoValor() {
		return campoValor;
	}

	public int getOrden() {
		return orden;
	}

	public void setAplPerfilSeccionCampo(AplPerfilSeccionCampo aplPerfilSeccionCampo) {
		this.aplPerfilSeccionCampo = aplPerfilSeccionCampo;
	}
	
	public void setAplPerfilSeccionCampoValorOpcionList(
			Collection<AplPerfilSeccionCampoValorOpcion> aplPerfilSeccionCampoValorOpcionList) {
		this.aplPerfilSeccionCampoValorOpcionList = aplPerfilSeccionCampoValorOpcionList;
	}

	public void setCampoValor(CampoValor campoValor) {
		this.campoValor = campoValor;
	}

	public void setOrden(Integer orden) {
		this.orden = orden;
	}
}