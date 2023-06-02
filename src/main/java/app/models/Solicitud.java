package app.models;

import java.util.List;

import app.utilidades.OpcionesAdicionales;

public class Solicitud {
	protected Integer idsolt;
	protected Integer numSolt;
	protected String fecha;
	protected String fechasolicitud;
	protected String observaciones;
	protected Integer aprobadoSiNo;
	protected Integer idacc;
	
	protected String login;
	protected Integer estado;
	
	protected Vehiculo vehiculo;
	protected Persona persona;
	
	private List<Aprobacion> Aprobaciones;
	
	
	private int Tot;


	public Solicitud() {

	}


	public Solicitud(Integer idsolt, Integer numSolt, String fecha, String fechasolicitud, String observaciones,
			Integer aprobadoSiNo, Integer idacc, String login, Integer estado, Vehiculo vehiculo, Persona persona,
			List<Aprobacion> aprobaciones, int tot) {
		super();
		this.idsolt = idsolt;
		this.numSolt = numSolt;
		this.fecha = fecha;
		this.fechasolicitud = fechasolicitud;
		this.observaciones = observaciones;
		this.aprobadoSiNo = aprobadoSiNo;
		this.idacc = idacc;
		this.login = login;
		this.estado = estado;
		this.vehiculo = vehiculo;
		this.persona = persona;
		Aprobaciones = aprobaciones;
		Tot = tot;
	}


	public Integer getIdsolt() {
		return idsolt;
	}


	public void setIdsolt(Integer idsolt) {
		this.idsolt = idsolt;
	}


	public Integer getNumSolt() {
		return numSolt;
	}


	public void setNumSolt(Integer numSolt) {
		this.numSolt = numSolt;
	}


	public String getFecha() {
		return fecha;
	}
	
	public String getFechaConvertido(String fecha) {
		String fecha_d="";
		try {
			OpcionesAdicionales opcionesAdicionales=new OpcionesAdicionales();
			if(!fecha.equals(""))
			fecha_d=opcionesAdicionales.convertFecha(fecha);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			System.out.println(e.getMessage());
		} 
		return fecha_d;
		
	}

	public void setFecha(String fecha) {
		
		this.fecha = this.getFechaConvertido(fecha);
	}


	public String getFechasolicitud() {
		return fechasolicitud;
	}


	public void setFechasolicitud(String fechasolicitud) {
		this.fechasolicitud = fechasolicitud;
	}


	public String getObservaciones() {
		return observaciones;
	}


	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}


	public Integer getAprobadoSiNo() {
		return aprobadoSiNo;
	}


	public void setAprobadoSiNo(Integer aprobadoSiNo) {
		this.aprobadoSiNo = aprobadoSiNo;
	}


	public Integer getIdacc() {
		return idacc;
	}


	public void setIdacc(Integer idacc) {
		this.idacc = idacc;
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


	public List<Aprobacion> getAprobaciones() {
		return Aprobaciones;
	}


	public void setAprobaciones(List<Aprobacion> aprobaciones) {
		Aprobaciones = aprobaciones;
	}


	public int getTot() {
		return Tot;
	}


	public void setTot(int tot) {
		Tot = tot;
	}


	@Override
	public String toString() {
		return "Solicitud [idsolt=" + idsolt + ", numSolt=" + numSolt + ", fecha=" + fecha + ", fechasolicitud="
				+ fechasolicitud + ", observaciones=" + observaciones + ", aprobadoSiNo=" + aprobadoSiNo + ", idacc="
				+ idacc + ", login=" + login + ", estado=" + estado + ", vehiculo=" + vehiculo + ", persona=" + persona
				+ ", Aprobaciones=" + Aprobaciones + ", Tot=" + Tot + "]";
	}


	
}
