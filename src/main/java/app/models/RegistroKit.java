package app.models;

import java.util.ArrayList;
import java.util.List;

public class RegistroKit{
	protected Integer idregistroKit;
	protected String fechaconversion;
	protected Integer desinstaladokitglpsino;
	protected Integer certificadodesintalacionsino;
	protected String login;
	protected Integer idordserv;
	protected Integer idmarcred;
	protected String numserieregulador;
	protected String certificadokit;
	protected String numseriemotor;
	protected String codigobsisa;
	protected String fechacertificado;
	protected Integer anio;
	protected Integer idtall;
	protected String observacioneskit;
	protected String primeramedicion;
	protected String segundamedicion;

	protected Integer conformeestandasino;
	protected OrdenServicio ordenServicio;
	protected Reductor reductor;
	protected implementoskit implementokit;
	
	protected List<InstalacionCilindro> Cilindros = new ArrayList<InstalacionCilindro>() ;
	
	private int Tot;

	public RegistroKit() {
//		super();
		// TODO Auto-generated constructor stub
	}

	public RegistroKit(Integer idregistroKit, String fechaconversion, Integer desinstaladokitglpsino,
			Integer certificadodesintalacionsino, String login, Integer idordserv, Integer idmarcred,
			String numserieregulador, String certificadokit, String numseriemotor, String codigobsisa,
			String fechacertificado, Integer anio, Integer idtall, String observacioneskit, String primeramedicion,
			String segundamedicion, Integer conformeestandasino, OrdenServicio ordenServicio, Reductor reductor,
			implementoskit implementokit, List<InstalacionCilindro> cilindros, int tot) {
		super();
		this.idregistroKit = idregistroKit;
		this.fechaconversion = fechaconversion;
		this.desinstaladokitglpsino = desinstaladokitglpsino;
		this.certificadodesintalacionsino = certificadodesintalacionsino;
		this.login = login;
		this.idordserv = idordserv;
		this.idmarcred = idmarcred;
		this.numserieregulador = numserieregulador;
		this.certificadokit = certificadokit;
		this.numseriemotor = numseriemotor;
		this.codigobsisa = codigobsisa;
		this.fechacertificado = fechacertificado;
		this.anio = anio;
		this.idtall = idtall;
		this.observacioneskit = observacioneskit;
		this.primeramedicion = primeramedicion;
		this.segundamedicion = segundamedicion;
		this.conformeestandasino = conformeestandasino;
		this.ordenServicio = ordenServicio;
		this.reductor = reductor;
		this.implementokit = implementokit;
		Cilindros = cilindros;
		Tot = tot;
	}

	public Integer getIdregistroKit() {
		return idregistroKit;
	}

	public void setIdregistroKit(Integer idregistroKit) {
		this.idregistroKit = idregistroKit;
	}

	public String getFechaconversion() {
		return fechaconversion;
	}

	public void setFechaconversion(String fechaconversion) {
		this.fechaconversion = fechaconversion;
	}

	public Integer getDesinstaladokitglpsino() {
		return desinstaladokitglpsino;
	}

	public void setDesinstaladokitglpsino(Integer desinstaladokitglpsino) {
		this.desinstaladokitglpsino = desinstaladokitglpsino;
	}

	public Integer getCertificadodesintalacionsino() {
		return certificadodesintalacionsino;
	}

	public void setCertificadodesintalacionsino(Integer certificadodesintalacionsino) {
		this.certificadodesintalacionsino = certificadodesintalacionsino;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public Integer getIdordserv() {
		return idordserv;
	}

	public void setIdordserv(Integer idordserv) {
		this.idordserv = idordserv;
	}

	public Integer getIdmarcred() {
		return idmarcred;
	}

	public void setIdmarcred(Integer idmarcred) {
		this.idmarcred = idmarcred;
	}

	public String getNumserieregulador() {
		return numserieregulador;
	}

	public void setNumserieregulador(String numserieregulador) {
		this.numserieregulador = numserieregulador;
	}

	public String getCertificadokit() {
		return certificadokit;
	}

	public void setCertificadokit(String certificadokit) {
		this.certificadokit = certificadokit;
	}

	public String getNumseriemotor() {
		return numseriemotor;
	}

	public void setNumseriemotor(String numseriemotor) {
		this.numseriemotor = numseriemotor;
	}

	public String getCodigobsisa() {
		return codigobsisa;
	}

	public void setCodigobsisa(String codigobsisa) {
		this.codigobsisa = codigobsisa;
	}

	public String getFechacertificado() {
		return fechacertificado;
	}

	public void setFechacertificado(String fechacertificado) {
		this.fechacertificado = fechacertificado;
	}

	public Integer getAnio() {
		return anio;
	}

	public void setAnio(Integer anio) {
		this.anio = anio;
	}

	public Integer getIdtall() {
		return idtall;
	}

	public void setIdtall(Integer idtall) {
		this.idtall = idtall;
	}

	public String getObservacioneskit() {
		return observacioneskit;
	}

	public void setObservacioneskit(String observacioneskit) {
		this.observacioneskit = observacioneskit;
	}

	public String getPrimeramedicion() {
		return primeramedicion;
	}

	public void setPrimeramedicion(String primeramedicion) {
		this.primeramedicion = primeramedicion;
	}

	public String getSegundamedicion() {
		return segundamedicion;
	}

	public void setSegundamedicion(String segundamedicion) {
		this.segundamedicion = segundamedicion;
	}

	public Integer getConformeestandasino() {
		return conformeestandasino;
	}

	public void setConformeestandasino(Integer conformeestandasino) {
		this.conformeestandasino = conformeestandasino;
	}

	public OrdenServicio getOrdenServicio() {
		return ordenServicio;
	}

	public void setOrdenServicio(OrdenServicio ordenServicio) {
		this.ordenServicio = ordenServicio;
	}

	public Reductor getReductor() {
		return reductor;
	}

	public void setReductor(Reductor reductor) {
		this.reductor = reductor;
	}

	public implementoskit getImplementokit() {
		return implementokit;
	}

	public void setImplementokit(implementoskit implementokit) {
		this.implementokit = implementokit;
	}

	public List<InstalacionCilindro> getCilindros() {
		return Cilindros;
	}

	public void setCilindros(List<InstalacionCilindro> cilindros) {
		Cilindros = cilindros;
	}

	public int getTot() {
		return Tot;
	}

	public void setTot(int tot) {
		Tot = tot;
	}

	@Override
	public String toString() {
		return "RegistroKit [idregistroKit=" + idregistroKit + ", fechaconversion=" + fechaconversion
				+ ", desinstaladokitglpsino=" + desinstaladokitglpsino + ", certificadodesintalacionsino="
				+ certificadodesintalacionsino + ", login=" + login + ", idordserv=" + idordserv + ", idmarcred="
				+ idmarcred + ", numserieregulador=" + numserieregulador + ", certificadokit=" + certificadokit
				+ ", numseriemotor=" + numseriemotor + ", codigobsisa=" + codigobsisa + ", fechacertificado="
				+ fechacertificado + ", anio=" + anio + ", idtall=" + idtall + ", observacioneskit=" + observacioneskit
				+ ", primeramedicion=" + primeramedicion + ", segundamedicion=" + segundamedicion
				+ ", conformeestandasino=" + conformeestandasino + ", ordenServicio=" + ordenServicio + ", reductor="
				+ reductor + ", implementokit=" + implementokit + ", Cilindros=" + Cilindros + ", Tot=" + Tot + "]";
	}

	
	
	
}
