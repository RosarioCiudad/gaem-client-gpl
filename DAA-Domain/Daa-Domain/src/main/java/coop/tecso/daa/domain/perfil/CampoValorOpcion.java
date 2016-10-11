package coop.tecso.daa.domain.perfil;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import coop.tecso.daa.domain.base.AbstractEntity;

/**
 * 
 * @author tecso.coop
 *
 */
@DatabaseTable(tableName = "apm_campoValorOpcion")
public class CampoValorOpcion extends AbstractEntity {

	@DatabaseField(canBeNull = false, foreign = true)
	private CampoValor campoValor;
	
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
	
	
	public CampoValor getCampoValor() {
		return campoValor;
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

	public void setCampoValor(CampoValor campoValor) {
		this.campoValor = campoValor;
	}

	public void setEtiqueta(String etiqueta) {
		this.etiqueta = etiqueta;
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

	public Integer getObligatorio() {
		return obligatorio;
	}

	public void setObligatorio(Integer obligatorio) {
		this.obligatorio = obligatorio;
	}

	public String getMascaraVisual() {
		return mascaraVisual;
	}

	public void setMascaraVisual(String mascaraVisual) {
		this.mascaraVisual = mascaraVisual;
	}
}