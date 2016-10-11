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
@DatabaseTable(tableName = "apm_campoValor")
public class CampoValor extends AbstractEntity {

	@DatabaseField(canBeNull = false, foreign = true)
	private Campo campo;
	
	@DatabaseField(canBeNull = false)
	private String etiqueta;

	@DatabaseField(canBeNull = false)
	private Integer obligatorio;
	
	@DatabaseField(canBeNull = false)
	private Integer tratamiento;
	
	@DatabaseField
	private String valorDefault;

	@DatabaseField
	private String tablaBusqueda;
	
	@DatabaseField
	private Integer soloLectura;
	
	@DatabaseField
	private String mascaraVisual;
	
	@ForeignCollectionField(eager = true)
	private Collection<CampoValorOpcion> campoValorOpcionList;

	public Campo getCampo() {
		return campo;
	}

	public Collection<CampoValorOpcion> getCampoValorOpcionList() {
		return campoValorOpcionList;
	}

	public String getEtiqueta() {
		return etiqueta;
	}

	public Integer getSoloLectura() {
		return soloLectura;
	}

	public String getTablaBusqueda() {
		return tablaBusqueda;
	}

	public Integer getTratamiento() {
		return tratamiento;
	}

	public String getValorDefault() {
		return valorDefault;
	}

	public void setCampo(Campo campo) {
		this.campo = campo;
	}

	public void setCampoValorOpcionList(Collection<CampoValorOpcion> campoValorOpcionList) {
		this.campoValorOpcionList = campoValorOpcionList;
	}

	public void setEtiqueta(String etiqueta) {
		this.etiqueta = etiqueta;
	}

	public void setObligatorio(Integer obligatorio) {
		this.obligatorio = obligatorio;
	}

	public void setSoloLectura(Integer soloLectura) {
		this.soloLectura = soloLectura;
	}

	public void setTablaBusqueda(String tablaBusqueda) {
		this.tablaBusqueda = tablaBusqueda;
	}

	public void setTratamiento(Integer tratamiento) {
		this.tratamiento = tratamiento;
	}

	public void setValorDefault(String valorDefault) {
		this.valorDefault = valorDefault;
	}

	public String getMascaraVisual() {
		return mascaraVisual;
	}

	public void setMascaraVisual(String mascaraVisual) {
		this.mascaraVisual = mascaraVisual;
	}

	public Integer getObligatorio() {
		return obligatorio;
	}
}