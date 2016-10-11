package coop.tecso.daa.domain.perfil;

/**
 * 
 * @author tecso.coop
 *
 */
public interface PerfilElement {
	
	/**
	 * 
	 * @param visitor
	 */
	public void accept(PerfilVisitor visitor);

}