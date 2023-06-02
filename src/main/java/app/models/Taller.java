package app.models;



public class Taller {
	protected Integer idtall;
	protected String nombretall;
	protected String direccion;
	protected String fecha_registro;
	protected Integer estado;
	protected Persona persona;
	protected Integer idprov;
	private int Tot;

	protected Provincia provincia;
	
	public Taller() {
	}

	public Taller(Integer idtall, String nombretall, String direccion, String fecha_registro, Integer estado,
			Persona persona, Integer idprov, int tot, Provincia provincia) {
		super();
		this.idtall = idtall;
		this.nombretall = nombretall;
		this.direccion = direccion;
		this.fecha_registro = fecha_registro;
		this.estado = estado;
		this.persona = persona;
		this.idprov = idprov;
		Tot = tot;
		this.provincia = provincia;
	}

	public Integer getIdtall() {
		return idtall;
	}

	public void setIdtall(Integer idtall) {
		this.idtall = idtall;
	}

	public String getNombretall() {
		return nombretall;
	}

	public void setNombretall(String nombretall) {
		this.nombretall = nombretall;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public String getFecha_registro() {
		return fecha_registro;
	}

	public void setFecha_registro(String fecha_registro) {
		this.fecha_registro = fecha_registro;
	}

	public Integer getEstado() {
		return estado;
	}

	public void setEstado(Integer estado) {
		this.estado = estado;
	}

	public Persona getPersona() {
		return persona;
	}

	public void setPersona(Persona persona) {
		this.persona = persona;
	}

	public Integer getIdprov() {
		return idprov;
	}

	public void setIdprov(Integer idprov) {
		this.idprov = idprov;
	}

	public int getTot() {
		return Tot;
	}

	public void setTot(int tot) {
		Tot = tot;
	}

	public Provincia getProvincia() {
		return provincia;
	}

	public void setProvincia(Provincia provincia) {
		this.provincia = provincia;
	}

	@Override
	public String toString() {
		return "Taller [idtall=" + idtall + ", nombretall=" + nombretall + ", direccion=" + direccion
				+ ", fecha_registro=" + fecha_registro + ", estado=" + estado + ", persona=" + persona + ", idprov="
				+ idprov + ", Tot=" + Tot + ", provincia=" + provincia + "]";
	}

	
}
