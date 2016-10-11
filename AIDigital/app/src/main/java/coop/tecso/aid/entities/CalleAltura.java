package coop.tecso.aid.entities;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "gis_calleAltura")
public class CalleAltura {

	@DatabaseField(canBeNull = false, foreign = true, columnName="codigo")
	private Calle calle;

	@DatabaseField(canBeNull = false)
	private Integer alturaDesde;

	@DatabaseField(canBeNull = false)
	private Integer alturaHasta;

	@DatabaseField
	private String letraCalle;

	@DatabaseField
	private Boolean bis;
	

	// Constructores
	public CalleAltura() {
		super();
	}

	// Getters y Setters
	public Calle getCalle() {
		return calle;
	}

	public void setCalle(Calle calle) {
		this.calle = calle;
	}

	public Integer getAlturaDesde() {
		return alturaDesde;
	}

	public void setAlturaDesde(Integer alturaDesde) {
		this.alturaDesde = alturaDesde;
	}

	public Integer getAlturaHasta() {
		return alturaHasta;
	}

	public void setAlturaHasta(Integer alturaHasta) {
		this.alturaHasta = alturaHasta;
	}

	public String getLetraCalle() {
		return letraCalle;
	}

	public void setLetraCalle(String letraCalle) {
		this.letraCalle = letraCalle;
	}

	public Boolean getBis() {
		return bis;
	}

	public void setBis(Boolean bis) {
		this.bis = bis;
	}
	
}