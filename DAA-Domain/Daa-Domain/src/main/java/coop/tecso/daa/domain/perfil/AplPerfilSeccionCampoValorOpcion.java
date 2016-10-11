package coop.tecso.daa.domain.perfil;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import coop.tecso.daa.domain.base.AbstractEntity;

/**
 * 
 * @author tecso.coop
 *
 */
@DatabaseTable(tableName = "apm_aplPerfilSeccionCampoValorOpcion")
public class AplPerfilSeccionCampoValorOpcion extends AbstractEntity implements PerfilElement {

	@DatabaseField(canBeNull = false, foreign = true)
	private AplPerfilSeccionCampoValor aplPerfilSeccionCampoValor;

	@DatabaseField(canBeNull = false, foreign = true, foreignAutoRefresh= true)
	private CampoValorOpcion  campoValorOpcion;

	@DatabaseField(canBeNull = false)
	private Integer orden;

	public void accept(PerfilVisitor visitor) {
		visitor.visit(this);
	}

	public AplPerfilSeccionCampoValor getAplPerfilSeccionCampoValor() {
		return aplPerfilSeccionCampoValor;
	}

	public CampoValorOpcion getCampoValorOpcion() {
		return campoValorOpcion;
	}

	public Integer getOrden() {
		return orden;
	}

	public void setAplPerfilSeccionCampoValor(
			AplPerfilSeccionCampoValor aplPerfilSeccionCampoValor) {
		this.aplPerfilSeccionCampoValor = aplPerfilSeccionCampoValor;
	}

	public void setCampoValorOpcion(CampoValorOpcion campoValorOpcion) {
		this.campoValorOpcion = campoValorOpcion;
	}

	public void setOrden(Integer orden) {
		this.orden = orden;
	}
}