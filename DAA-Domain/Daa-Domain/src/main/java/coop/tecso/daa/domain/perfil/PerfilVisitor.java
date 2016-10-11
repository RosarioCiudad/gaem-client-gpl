package coop.tecso.daa.domain.perfil;

import coop.tecso.daa.domain.aplicacion.AplicacionPerfil;
import coop.tecso.daa.domain.aplicacion.AplicacionPerfilSeccion;

/**
 * 
 * @author tecso.coop
 *
 */
public interface PerfilVisitor {
	
	/**
	 * 
	 * @param perfil
	 */
	public void visit(AplicacionPerfil perfil);

	/**
	 * 
	 * @param seccion
	 */
	public void visit(AplicacionPerfilSeccion seccion);
	
	/**
	 * 
	 * @param campo
	 */
	public void visit(AplPerfilSeccionCampo campo);
	
	/**
	 * 
	 * @param campoValor
	 */
	public void visit(AplPerfilSeccionCampoValor campoValor);
	
	/**
	 * 
	 * @param campoValorOpcion
	 */
	public void visit(AplPerfilSeccionCampoValorOpcion campoValorOpcion);
}