package coop.tecso.aid.entities;

import java.util.Collection;
import java.util.Date;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import coop.tecso.daa.domain.aplicacion.AplicacionPerfil;
import coop.tecso.daa.domain.base.AbstractEntity;
import coop.tecso.daa.domain.seguridad.DispositivoMovil;
import coop.tecso.daa.domain.seguridad.UsuarioApm;


@DatabaseTable(tableName = "for_formulario")
public class Formulario extends AbstractEntity {
	
	@DatabaseField(canBeNull = false, foreign = true)
	private AplicacionPerfil tipoFormularioDef;

	@DatabaseField(canBeNull = false, foreign = true)
	private EstadoTipoFormulario estadoTipoFormulario;

	@DatabaseField(canBeNull = false, foreign = true)
	private TipoFormulario tipoFormulario;
	
	@DatabaseField(canBeNull = false, foreign = true)
	private DispositivoMovil dispositivoMovil;

	@DatabaseField(canBeNull = false, foreign = true)
	private UsuarioApm usuarioCierre;
	
	@DatabaseField(foreign = true)
	private MotivoAnulacionTipoFormulario motivoAnulacionTipoFormulario;
	
	@DatabaseField(foreign = true)
	private MotivoCierreTipoFormulario motivoCierreTipoFormulario;
	
	@DatabaseField(foreign = true)
	private Talonario talonario;

	@DatabaseField
	private String observacion;

	@DatabaseField
	private Date fechaInicio;

	@DatabaseField
	private String numero;

	@DatabaseField
	private String domicilio;
	
	@DatabaseField
	private String dominio;

	@DatabaseField
	private Date fechaCierre;

	@DatabaseField
	private Date fechaTrasmision;

	@DatabaseField
	private Integer cantidadImpresiones;

	@DatabaseField
	private Double latitud;
	
	@DatabaseField
	private Double longitud;
	
	@DatabaseField
	private Double precision;
	
	@DatabaseField
	private Date fechaMedicion;
	
	@DatabaseField
	private Integer origen;
	
	@DatabaseField
	private Integer numeroInspector;
	
	@DatabaseField
	private Integer reparticion;
	
	@DatabaseField(dataType = DataType.BYTE_ARRAY)
	private byte[] firmaDigital;
	
	@DatabaseField(dataType = DataType.BYTE_ARRAY)
	private byte[] formularioDigital;

	@ForeignCollectionField(eager = false)
	private Collection<FormularioDetalle> listFormularioDetalle;

	// Getters & Setters
	public AplicacionPerfil getTipoFormularioDef() {
		return tipoFormularioDef;
	}

	public void setTipoFormularioDef(AplicacionPerfil tipoFormularioDef) {
		this.tipoFormularioDef = tipoFormularioDef;
	}

	public EstadoTipoFormulario getEstadoTipoFormulario() {
		return estadoTipoFormulario;
	}

	public void setEstadoTipoFormulario(EstadoTipoFormulario estadoTipoFormulario) {
		this.estadoTipoFormulario = estadoTipoFormulario;
	}

	public TipoFormulario getTipoFormulario() {
		return tipoFormulario;
	}

	public void setTipoFormulario(TipoFormulario tipoFormulario) {
		this.tipoFormulario = tipoFormulario;
	}

	public DispositivoMovil getDispositivoMovil() {
		return dispositivoMovil;
	}

	public void setDispositivoMovil(DispositivoMovil dispositivoMovil) {
		this.dispositivoMovil = dispositivoMovil;
	}

	public UsuarioApm getUsuarioCierre() {
		return usuarioCierre;
	}

	public void setUsuarioCierre(UsuarioApm usuarioCierre) {
		this.usuarioCierre = usuarioCierre;
	}

	public MotivoAnulacionTipoFormulario getMotivoAnulacionTipoFormulario() {
		return motivoAnulacionTipoFormulario;
	}

	public void setMotivoAnulacionTipoFormulario(
			MotivoAnulacionTipoFormulario motivoAnulacionTipoFormulario) {
		this.motivoAnulacionTipoFormulario = motivoAnulacionTipoFormulario;
	}

	public MotivoCierreTipoFormulario getMotivoCierreTipoFormulario() {
		return motivoCierreTipoFormulario;
	}

	public void setMotivoCierreTipoFormulario(
			MotivoCierreTipoFormulario motivoCierreTipoFormulario) {
		this.motivoCierreTipoFormulario = motivoCierreTipoFormulario;
	}

	public String getObservacion() {
		return observacion;
	}

	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}

	public Date getFechaInicio() {
		return fechaInicio;
	}

	public void setFechaInicio(Date fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public String getDomicilio() {
		return domicilio;
	}

	public void setDomicilio(String domicilio) {
		this.domicilio = domicilio;
	}

	public Date getFechaCierre() {
		return fechaCierre;
	}

	public void setFechaCierre(Date fechaCierre) {
		this.fechaCierre = fechaCierre;
	}

	public Date getFechaTrasmision() {
		return fechaTrasmision;
	}

	public void setFechaTrasmision(Date fechaTrasmision) {
		this.fechaTrasmision = fechaTrasmision;
	}

	public Integer getCantidadImpresiones() {
		return cantidadImpresiones;
	}

	public void setCantidadImpresiones(Integer cantidadImpresiones) {
		this.cantidadImpresiones = cantidadImpresiones;
	}

	public Double getLatitud() {
		return latitud;
	}

	public void setLatitud(Double latitud) {
		this.latitud = latitud;
	}

	public Double getLongitud() {
		return longitud;
	}

	public void setLongitud(Double longitud) {
		this.longitud = longitud;
	}

	public Double getPrecision() {
		return precision;
	}

	public void setPrecision(Double precision) {
		this.precision = precision;
	}

	public Date getFechaMedicion() {
		return fechaMedicion;
	}

	public void setFechaMedicion(Date fechaMedicion) {
		this.fechaMedicion = fechaMedicion;
	}

	public Integer getOrigen() {
		return origen;
	}

	public void setOrigen(Integer origen) {
		this.origen = origen;
	}

	public byte[] getFirmaDigital() {
		return firmaDigital;
	}

	public void setFirmaDigital(byte[] firmaDigital) {
		this.firmaDigital = firmaDigital;
	}

	public Collection<FormularioDetalle> getListFormularioDetalle() {
		return listFormularioDetalle;
	}

	public void setListFormularioDetalle(
			Collection<FormularioDetalle> listFormularioDetalle) {
		this.listFormularioDetalle = listFormularioDetalle;
	}

	public byte[] getFormularioDigital() {
		return formularioDigital;
	}

	public void setFormularioDigital(byte[] formularioDigital) {
		this.formularioDigital = formularioDigital;
	}

	public Integer getNumeroInspector() {
		return numeroInspector;
	}

	public void setNumeroInspector(Integer numeroInspector) {
		this.numeroInspector = numeroInspector;
	}

	public Integer getReparticion() {
		return reparticion;
	}

	public void setReparticion(Integer reparticion) {
		this.reparticion = reparticion;
	}

	public Talonario getTalonario() {
		return talonario;
	}

	public void setTalonario(Talonario talonario) {
		this.talonario = talonario;
	}

	public String getDominio() {
		return dominio;
	}

	public void setDominio(String dominio) {
		this.dominio = dominio;
	}
}