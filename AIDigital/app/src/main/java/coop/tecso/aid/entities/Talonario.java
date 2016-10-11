package coop.tecso.aid.entities;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import coop.tecso.daa.domain.base.AbstractEntity;
import coop.tecso.daa.domain.seguridad.DispositivoMovil;

@DatabaseTable(tableName = "for_talonario")
public class Talonario extends AbstractEntity {

	@DatabaseField(canBeNull = false, foreign = true)
	private DispositivoMovil dispositivoMovil;
	
	@DatabaseField(canBeNull = false, foreign = true)
	private TipoFormulario tipoFormulario;
	
	@DatabaseField(canBeNull = false, foreign = true)
	private Serie serie;
	
	@DatabaseField(canBeNull = false)
	private int valorDesde;
	
	@DatabaseField(canBeNull = false)
	private int valorHasta;
	
	@DatabaseField(canBeNull = false, unique = true)
	private int valor;

	public DispositivoMovil getDispositivoMovil() {
		return dispositivoMovil;
	}

	public void setDispositivoMovil(DispositivoMovil dispositivoMovil) {
		this.dispositivoMovil = dispositivoMovil;
	}

	public TipoFormulario getTipoFormulario() {
		return tipoFormulario;
	}

	public void setTipoFormulario(TipoFormulario tipoFormulario) {
		this.tipoFormulario = tipoFormulario;
	}

	public Serie getSerie() {
		return serie;
	}

	public void setSerie(Serie serie) {
		this.serie = serie;
	}

	public int getValorDesde() {
		return valorDesde;
	}

	public void setValorDesde(int valorDesde) {
		this.valorDesde = valorDesde;
	}

	public int getValorHasta() {
		return valorHasta;
	}

	public void setValorHasta(int valorHasta) {
		this.valorHasta = valorHasta;
	}

	public int getValor() {
		return valor;
	}

	public void setValor(int valor) {
		this.valor = valor;
	}
}