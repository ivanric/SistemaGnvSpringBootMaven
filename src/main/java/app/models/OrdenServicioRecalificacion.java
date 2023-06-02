package app.models;

public class OrdenServicioRecalificacion {
	protected Integer idordservrecal;
	protected Integer idrecal;
	protected Integer numords;
	protected String fechaords;
	protected SolicitudReposicionRecalificacion solicitudreposicionrecalificacion;
//	protected Servicio servicio;
	protected Integer instaladosino;
	protected Persona persona;
	protected Taller tallerconversion;
	protected TallerRecalificacion tallerrecalificacion;
	protected Integer idinst;
	protected Integer idfirm;
	protected Integer anio;
	protected Integer idtallconver;
	protected Integer idtallrecal;
	protected Integer recal_cil;
	protected Integer reponer_cil;
	protected Integer reponer_kit_completo;
	protected Integer reponer_kit_partes;
	protected Integer estado;
	
	private int Tot;
	
	public OrdenServicioRecalificacion() {
//		super();
		// TODO Auto-generated constructor stub
	}

	public OrdenServicioRecalificacion(Integer idordservrecal, Integer idrecal, Integer numords, String fechaords,
			SolicitudReposicionRecalificacion solicitudreposicionrecalificacion, Integer instaladosino, Persona persona,
			Taller tallerconversion, TallerRecalificacion tallerrecalificacion, Integer idinst, Integer idfirm, Integer anio,
			Integer idtallconver, Integer idtallrecal, Integer recal_cil, Integer reponer_cil,
			Integer reponer_kit_completo, Integer reponer_kit_partes, Integer estado, int tot) {
		super();
		this.idordservrecal = idordservrecal;
		this.idrecal = idrecal;
		this.numords = numords;
		this.fechaords = fechaords;
		this.solicitudreposicionrecalificacion = solicitudreposicionrecalificacion;
		this.instaladosino = instaladosino;
		this.persona = persona;
		this.tallerconversion = tallerconversion;
		this.tallerrecalificacion = tallerrecalificacion;
		this.idinst = idinst;
		this.idfirm = idfirm;
		this.anio = anio;
		this.idtallconver = idtallconver;
		this.idtallrecal = idtallrecal;
		this.recal_cil = recal_cil;
		this.reponer_cil = reponer_cil;
		this.reponer_kit_completo = reponer_kit_completo;
		this.reponer_kit_partes = reponer_kit_partes;
		this.estado = estado;
		Tot = tot;
	}

	public Integer getIdordservrecal() {
		return idordservrecal;
	}

	public void setIdordservrecal(Integer idordservrecal) {
		this.idordservrecal = idordservrecal;
	}

	public Integer getIdrecal() {
		return idrecal;
	}

	public void setIdrecal(Integer idrecal) {
		this.idrecal = idrecal;
	}

	public Integer getNumords() {
		return numords;
	}

	public void setNumords(Integer numords) {
		this.numords = numords;
	}

	public String getFechaords() {
		return fechaords;
	}

	public void setFechaords(String fechaords) {
		this.fechaords = fechaords;
	}

	public SolicitudReposicionRecalificacion getSolicitudreposicionrecalificacion() {
		return solicitudreposicionrecalificacion;
	}

	public void setSolicitudreposicionrecalificacion(SolicitudReposicionRecalificacion solicitudreposicionrecalificacion) {
		this.solicitudreposicionrecalificacion = solicitudreposicionrecalificacion;
	}

	public Integer getInstaladosino() {
		return instaladosino;
	}

	public void setInstaladosino(Integer instaladosino) {
		this.instaladosino = instaladosino;
	}

	public Persona getPersona() {
		return persona;
	}

	public void setPersona(Persona persona) {
		this.persona = persona;
	}

	public Taller getTallerconversion() {
		return tallerconversion;
	}

	public void setTallerconversion(Taller tallerconversion) {
		this.tallerconversion = tallerconversion;
	}

	public TallerRecalificacion getTallerrecalificacion() {
		return tallerrecalificacion;
	}

	public void setTallerrecalificacion(TallerRecalificacion tallerrecalificacion) {
		this.tallerrecalificacion = tallerrecalificacion;
	}

	public Integer getIdinst() {
		return idinst;
	}

	public void setIdinst(Integer idinst) {
		this.idinst = idinst;
	}

	public Integer getIdfirm() {
		return idfirm;
	}

	public void setIdfirm(Integer idfirm) {
		this.idfirm = idfirm;
	}

	public Integer getAnio() {
		return anio;
	}

	public void setAnio(Integer anio) {
		this.anio = anio;
	}

	public Integer getIdtallconver() {
		return idtallconver;
	}

	public void setIdtallconver(Integer idtallconver) {
		this.idtallconver = idtallconver;
	}

	public Integer getIdtallrecal() {
		return idtallrecal;
	}

	public void setIdtallrecal(Integer idtallrecal) {
		this.idtallrecal = idtallrecal;
	}

	public Integer getRecal_cil() {
		return recal_cil;
	}

	public void setRecal_cil(Integer recal_cil) {
		this.recal_cil = recal_cil;
	}

	public Integer getReponer_cil() {
		return reponer_cil;
	}

	public void setReponer_cil(Integer reponer_cil) {
		this.reponer_cil = reponer_cil;
	}

	public Integer getReponer_kit_completo() {
		return reponer_kit_completo;
	}

	public void setReponer_kit_completo(Integer reponer_kit_completo) {
		this.reponer_kit_completo = reponer_kit_completo;
	}

	public Integer getReponer_kit_partes() {
		return reponer_kit_partes;
	}

	public void setReponer_kit_partes(Integer reponer_kit_partes) {
		this.reponer_kit_partes = reponer_kit_partes;
	}

	public Integer getEstado() {
		return estado;
	}

	public void setEstado(Integer estado) {
		this.estado = estado;
	}

	public int getTot() {
		return Tot;
	}

	public void setTot(int tot) {
		Tot = tot;
	}

	@Override
	public String toString() {
		return "OrdenServicioRecalificacion [idordservrecal=" + idordservrecal + ", idrecal=" + idrecal + ", numords="
				+ numords + ", fechaords=" + fechaords + ", solicitudreposicionrecalificacion="
				+ solicitudreposicionrecalificacion + ", instaladosino=" + instaladosino + ", persona=" + persona
				+ ", tallerconversion=" + tallerconversion + ", tallerrecalificacion=" + tallerrecalificacion
				+ ", idinst=" + idinst + ", idfirm=" + idfirm + ", anio=" + anio + ", idtallconver=" + idtallconver
				+ ", idtallrecal=" + idtallrecal + ", recal_cil=" + recal_cil + ", reponer_cil=" + reponer_cil
				+ ", reponer_kit_completo=" + reponer_kit_completo + ", reponer_kit_partes=" + reponer_kit_partes
				+ ", estado=" + estado + ", Tot=" + Tot + "]";
	}
	
	
	

	
}
