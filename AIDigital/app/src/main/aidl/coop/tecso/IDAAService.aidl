package coop.tecso;

interface IDAAService {
	
	String getCurrentUser();
	
	String getServerURL();
	
	String getAplicacionPerfilById(int aplicacionPerfilId); 
	
	String login(String username, String password);
	
	String getDispositivoMovil();
	
	String getImpresora();
	
	String getNotificacionById(int notificacionID);
	
	void changeSession(String username, String password);
	
	boolean hasAccess(int usuarioID, String codAplicacion);	
	
	String getAplicacionParametroByCodigo(String codParametro, String codAplicacion);
	
	String getListBinarioPathBy(String codAplicacion, String tipoBinario);
	
	String getListAplicacionPerfilByAplicacion(String codigoAplicacion);
	
	String signData(String data);
}