package coop.tecso.aid.dao;

import android.content.Context;
import android.util.Log;

/**
 * A factory which produces Data-Access-Objects for Device
 * Administrator Application entities.
 * 
 * @author tecso.coop
 *
 */
public final class DAOFactory {

	private static final String LOG_TAG = DAOFactory.class.getSimpleName();

	/**
	 * Singleton instance.
	 */
	private static DAOFactory INSTANCE;

	private final FormularioDAO formularioDAO;
	private final FormularioDetalleDAO formularioDetalleDAO;
	private final EstadoTipoFormularioDAO estadoTipoFormularioDAO;
	private final TablaVersionDAO tablaVersionDAO;
	private final TipoFormularioDAO tipoFormularioDAO;
	private final MotivoCierreTipoFormularioDAO motivoCierreTipoFormularioDAO;
	private final MotivoAnulacionTipoFormularioDAO motivoAnulacionTipoFormularioDAO;
	private final TalonarioDAO talonarioDAO;
	private final SerieDAO serieDAO;
	private final UsuarioApmReparticionDAO usuarioApmReparticionDAO;
	private final AplicacionParametroDAO aplicacionParametroDAO;
	private final TelefonoPanicoDAO telefonoPanicoDAO;
	private final PanicoDAO panicoDAO;
	private final TipoDocumentoDAO tipoDocumentoDAO;
	private final ClaseLicenciaDAO claseLicenciaDAO;
	
	/**
	 * The DaoFactory constructor.
	 * 
	 * @param context
	 */
	private DAOFactory(Context context) {
		this.formularioDAO = new FormularioDAO(context);
		this.formularioDetalleDAO = new FormularioDetalleDAO(context);
		this.estadoTipoFormularioDAO = new EstadoTipoFormularioDAO(context);
		this.tablaVersionDAO = new TablaVersionDAO(context);
		this.tipoFormularioDAO = new TipoFormularioDAO(context);
		this.motivoCierreTipoFormularioDAO = new MotivoCierreTipoFormularioDAO(context);
		this.motivoAnulacionTipoFormularioDAO = new MotivoAnulacionTipoFormularioDAO(context);
		this.talonarioDAO = new TalonarioDAO(context);
		this.serieDAO = new SerieDAO(context);
		this.usuarioApmReparticionDAO = new UsuarioApmReparticionDAO(context);
		this.aplicacionParametroDAO = new AplicacionParametroDAO(context);
		this.telefonoPanicoDAO = new TelefonoPanicoDAO(context);
		this.panicoDAO = new PanicoDAO(context);
		this.tipoDocumentoDAO = new TipoDocumentoDAO(context);
		this.claseLicenciaDAO = new ClaseLicenciaDAO(context);
	}

	/**
	 * Initialize the DAOFactory.
	 * 
	 * @param context The Android context.
	 */
	public static void init(Context context) {
		Log.d(LOG_TAG, "Initializing...");
		INSTANCE = new DAOFactory(context);
	}

	/**
	 * Get the singleton instance of {@link DAOFactory}.
	 * 
	 * @return The singleton instance.
	 */
	public static DAOFactory getInstance() {
		if (INSTANCE == null) {
			Log.e(LOG_TAG, "DaoFactory not initialized!");
			throw new RuntimeException("DaoFactory not initialized!");
		}
		return INSTANCE;
	}

	/**
	 * 
	 * @return formularioDAO instance.
	 */
	public FormularioDAO getFormularioDAO() {
		return INSTANCE.formularioDAO;
	}

	/**
	 * 
	 * @return formularioValorDAO instance.
	 */
	public FormularioDetalleDAO getFormularioDetalleDAO() {
		return INSTANCE.formularioDetalleDAO;
	}

	/**
	 * 
	 * @return estadoFormularioDAO instance.
	 */
	public EstadoTipoFormularioDAO getEstadoTipoFormularioDAO() {
		return INSTANCE.estadoTipoFormularioDAO;
	}

	/**
	 * 
	 * @return tablaVersionDAO instance.
	 */
	public TablaVersionDAO getTablaVersionDAO() {
		return INSTANCE.tablaVersionDAO;
	}

	/**
	 * 
	 * @return tipoFormularioDAO instance.
	 */
	public TipoFormularioDAO getTipoFormularioDAO() {
		return INSTANCE.tipoFormularioDAO;
	}

	/**
	 * 
	 * @return motivoCierreTipoFormularioDAO instance.
	 */
	public MotivoCierreTipoFormularioDAO getMotivoCierreTipoFormularioDAO() {
		return INSTANCE.motivoCierreTipoFormularioDAO;
	}
	
	/**
	 * 
	 * @return MotivoAnulacionTipoFormularioDAO instance.
	 */
	public MotivoAnulacionTipoFormularioDAO getMotivoAnulacionTipoFormularioDAO() {
		return INSTANCE.motivoAnulacionTipoFormularioDAO;
	}
	
	/**
	 * 
	 * @return TalonarioDAO instance.
	 */
	public TalonarioDAO getTalonarioDAO() {
		return INSTANCE.talonarioDAO;
	}
	
	/**
	 * 
	 * @return SerieDAO instance.
	 */
	public SerieDAO getSerieDAO() {
		return INSTANCE.serieDAO;
	}
	
	/**
	 * 
	 * @return UsuarioApmReparticionDAO instance.
	 */
	public UsuarioApmReparticionDAO getUsuarioApmReparticionDAO() {
		return INSTANCE.usuarioApmReparticionDAO;
	}
	
	/**
	 * 
	 * @return AplicacionParametroDAO instance.
	 */
	public AplicacionParametroDAO getAplicacionParametroDAO() {
		return INSTANCE.aplicacionParametroDAO;
	}
	
	/**
	 * 
	 * @return TelefonoPanicoDAO instance.
	 */
	public TelefonoPanicoDAO getTelefonoPanicoDAO() {
		return INSTANCE.telefonoPanicoDAO;
	}
	
	/**
	 * 
	 * @return PanicoDAO instance.
	 */
	public PanicoDAO getPanicoDAO() {
		return INSTANCE.panicoDAO;
	}
	
	/**
	 * 
	 * @return TipoDocumentoDAO instance.
	 */
	public TipoDocumentoDAO getTipoDocumentoDAO() {
		return INSTANCE.tipoDocumentoDAO;
	}
	
	/**
	 * 
	 * @return ClaseLicenciaDAO instance.
	 */
	public ClaseLicenciaDAO getClaseLicenciaDAO() {
		return INSTANCE.claseLicenciaDAO;
	}
}