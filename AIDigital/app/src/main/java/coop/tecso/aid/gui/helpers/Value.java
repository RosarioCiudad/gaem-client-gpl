package coop.tecso.aid.gui.helpers;

import coop.tecso.daa.domain.perfil.AplPerfilSeccionCampo;
import coop.tecso.daa.domain.perfil.AplPerfilSeccionCampoValor;
import coop.tecso.daa.domain.perfil.AplPerfilSeccionCampoValorOpcion;

public class Value {

	private AplPerfilSeccionCampo campo = null;
	private AplPerfilSeccionCampoValor campoValor = null;
	private AplPerfilSeccionCampoValorOpcion campoValorOpcion = null;
	private String valor = null;
	private byte[] imagen;
	
	public Value() {
		super();
	}

	public Value(AplPerfilSeccionCampo campo, 
			AplPerfilSeccionCampoValor campoValor, 
			AplPerfilSeccionCampoValorOpcion campoValorOpcion, String valor) {
		super();
		this.campo = campo;
		this.campoValor = campoValor;
		this.campoValorOpcion = campoValorOpcion;
		this.valor = valor;
	}

	public Value(AplPerfilSeccionCampo campo, 
			AplPerfilSeccionCampoValor campoValor, 
			AplPerfilSeccionCampoValorOpcion campoValorOpcion, String valor, byte[] imagen) {
		super();
		this.campo = campo;
		this.campoValor = campoValor;
		this.campoValorOpcion = campoValorOpcion;
		this.valor = valor;
		this.imagen = imagen;
	}

	public AplPerfilSeccionCampo getCampo() {
		return campo;
	}
	public void setCampo(AplPerfilSeccionCampo campo) {
		this.campo = campo;
	}
	public AplPerfilSeccionCampoValor getCampoValor() {
		return campoValor;
	}
	public void setCampoValor(AplPerfilSeccionCampoValor campoValor) {
		this.campoValor = campoValor;
	}
	public AplPerfilSeccionCampoValorOpcion getCampoValorOpcion() {
		return campoValorOpcion;
	}
	public void setCampoValorOpcion(
			AplPerfilSeccionCampoValorOpcion campoValorOpcion) {
		this.campoValorOpcion = campoValorOpcion;
	}
	public String getValor() {
		return valor;
	}
	public void setValor(String valor) {
		this.valor = valor;
	}
	public byte[] getImagen() {
		return imagen;
	}
	public void setImagen(byte[] imagen) {
		this.imagen = imagen;
	}	
}