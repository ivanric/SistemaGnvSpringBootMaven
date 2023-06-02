package app.models;

import java.util.List;

public class SolicitudReposicionRecalificacion {
	protected Integer idrecal;
	protected Integer numsoltrec;
	protected String fechacreacion;
	protected String observaciones;
	
	protected String login;
	protected Integer estado;
	protected Integer anio;
	
	protected String reposicion;
	protected String recalificacion;
	
	protected Vehiculo vehiculo;
	protected Persona persona;
	
	private int Tot;

	
	
	public SolicitudReposicionRecalificacion() {
//		super();
		// TODO Auto-generated constructor stub
	}



	public SolicitudReposicionRecalificacion(Integer idrecal, Integer numsoltrec, String fechacreacion,
			String observaciones, String login, Integer estado, Integer anio, String reposicion, String recalificacion,
			Vehiculo vehiculo, Persona persona, int tot) {
//		super();
		this.idrecal = idrecal;
		this.numsoltrec = numsoltrec;
		this.fechacreacion = fechacreacion;
		this.observaciones = observaciones;
		this.login = login;
		this.estado = estado;
		this.anio = anio;
		this.reposicion = reposicion;
		this.recalificacion = recalificacion;
		this.vehiculo = vehiculo;
		this.persona = persona;
		Tot = tot;
	}



	public Integer getIdrecal() {
		return idrecal;
	}



	public void setIdrecal(Integer idrecal) {
		this.idrecal = idrecal;
	}



	public Integer getNumsoltrec() {
		return numsoltrec;
	}



	public void setNumsoltrec(Integer numsoltrec) {
		this.numsoltrec = numsoltrec;
	}



	public String getFechacreacion() {
		return fechacreacion;
	}



	public void setFechacreacion(String fechacreacion) {
		this.fechacreacion = fechacreacion;
	}



	public String getObservaciones() {
		return observaciones;
	}



	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}



	public String getLogin() {
		return login;
	}



	public void setLogin(String login) {
		this.login = login;
	}



	public Integer getEstado() {
		return estado;
	}



	public void setEstado(Integer estado) {
		this.estado = estado;
	}



	public Integer getAnio() {
		return anio;
	}



	public void setAnio(Integer anio) {
		this.anio = anio;
	}



	public String getReposicion() {
		return reposicion;
	}



	public void setReposicion(String reposicion) {
		this.reposicion = reposicion;
	}



	public String getRecalificacion() {
		return recalificacion;
	}



	public void setRecalificacion(String recalificacion) {
		this.recalificacion = recalificacion;
	}



	public Vehiculo getVehiculo() {
		return vehiculo;
	}



	public void setVehiculo(Vehiculo vehiculo) {
		this.vehiculo = vehiculo;
	}



	public Persona getPersona() {
		return persona;
	}



	public void setPersona(Persona persona) {
		this.persona = persona;
	}



	public int getTot() {
		return Tot;
	}



	public void setTot(int tot) {
		Tot = tot;
	}



	@Override
	public String toString() {
		return "SolicitudReposicionRecalificacion [idrecal=" + idrecal + ", numsoltrec=" + numsoltrec
				+ ", fechacreacion=" + fechacreacion + ", observaciones=" + observaciones + ", login=" + login
				+ ", estado=" + estado + ", anio=" + anio + ", reposicion=" + reposicion + ", recalificacion="
				+ recalificacion + ", vehiculo=" + vehiculo + ", persona=" + persona + ", Tot=" + Tot + "]";
	}
	
	
	
	
	
}
