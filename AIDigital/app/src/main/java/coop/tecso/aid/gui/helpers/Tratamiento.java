package coop.tecso.aid.gui.helpers;

public enum Tratamiento {
	
	TA(1, "Alfanúmerico"),
	TAM(2, "Alfanúmerico Multilinea"),
	TND(3, "Numérico Decimal"), 
	TNE(4, "Numérico Entero"), 
	TF(5, "Fecha"),
	TH(6, "Hora"),
	CHK(7, "CheckBox"),
	DOMI(70, "Domicilio Infracción"),
	DOMC(71, "Domicilio Conductor"),
	SO(72, ">>Sección Opcional>>"),
	SOC(73, ">>Sección Opcional Combo>>"),
	LD(74, ">>Lista Dinámica>>"),
	NA(76, "Opción Simple"),
	OP(75, "- Combo Opciones"),
	OP2(77,"- Combo Opciones vía DB"),
	OP3(78,"- Combo Opciones vía DB con opción 'Otra'"),	
//	LO(80, ">>Check Opciones>>"),
	PIC(79, "Adjunto Imagen"),
	DOM(80, "Dominio"),
	SBX(81,"SearchBox"),
	DOC(82, "Documento"),
	DESCONOCIDO(-1, "--");
	
//	TA("TA"), 	// Teclado Alfanumerico
//	TAM("TAM"), // Teclado Alfanumerico Multilinea
//	TNE("TNE"), // Numerico Entero
//	TND("TND"), // Numerico Decimal
//	TN2("TN2"), // Numerico Extendido (Teclado de telefono. Permite cargar punto decimal y barra)
//	TF("TF"),  	// Fecha
//	TT("TT"), 	// Tiempo
//	LO("LO"),  	// Lista de opciones (CheckList)
//	LB("LB"),  	// Lista de Busqueda (Lista de entidades tomadas de tabla busqueda)
//	LC("LC"),  	// Lista de Campos Estatica
//	LD("LD"),  	// Lista de Campos Dinamica 
//	OP("OP"),  	// Opciones simple seleccion (Combo)
//	BU("BU"),  	// Busqueda en Tabla (EntidadBusqueda)
//	NA("NA"),  	// Opcion Simple (Checked)
//	LNK("LNK"), // Link
//	NAV("NAV"), // Google Navigation
//	SO("SO"),  	// Secciones Opcionales (SectionsCheckList)
//	PIC("PIC"),  	// Adjunto Imagen 
//	SOC("SOC"),  	// Secciones Opcionales Combo (SectionsCombo)
//	PAD("PAD"),		// Consulta en padron de inhibidos
//	FIR("FIR"),		// Firma
//	CBU("CBU"),		// CBU
//	OP2("OP2"),		// Opcion simple extendida (Combo) - Llena el combo con valores de una tabla particular
//	DOM("DOM"),		// Domicilio
//	DESCONOCIDO("--");  // Para casos en que se informe un tratamiento desconocido por la aplicacion
	
	private Integer id;
	private String value;

	private Tratamiento(final Integer id,  final String value) {
		this.id = id;
		this.value = value;
	}

	public Integer getId() {
		return id;
	}

	public String getValue() {
		return value;
	}

	public static Tratamiento getById(Integer id){
		//
		for (Tratamiento tratamiento : Tratamiento.values()) {
			if(tratamiento.id.intValue() == id.intValue())
				return tratamiento;
		}

		return DESCONOCIDO;
	}
}