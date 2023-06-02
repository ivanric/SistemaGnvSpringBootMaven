package app.models;

public class Provincia {
	protected Integer idprov;
	protected String codigo;
	protected String nombre;
	protected Integer estado;
	protected Integer iddep;
	protected Departamento departamento;
	
	
	public Provincia() {
		super();
		// TODO Auto-generated constructor stub
	}


	public Provincia(Integer idprov, String codigo, String nombre, Integer estado, Integer iddep,
			Departamento departamento) {
//		super();
		this.idprov = idprov;
		this.codigo = codigo;
		this.nombre = nombre;
		this.estado = estado;
		this.iddep = iddep;
		this.departamento = departamento;
	}


	public Integer getIdprov() {
		return idprov;
	}


	public void setIdprov(Integer idprov) {
		this.idprov = idprov;
	}


	public String getCodigo() {
		return codigo;
	}


	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}


	public String getNombre() {
		return nombre;
	}


	public void setNombre(String nombre) {
		this.nombre = nombre;
	}


	public Integer getEstado() {
		return estado;
	}


	public void setEstado(Integer estado) {
		this.estado = estado;
	}


	public Integer getIddep() {
		return iddep;
	}


	public void setIddep(Integer iddep) {
		this.iddep = iddep;
	}


	public Departamento getDepartamento() {
		return departamento;
	}


	public void setDepartamento(Departamento departamento) {
		this.departamento = departamento;
	}


	@Override
	public String toString() {
		return "Provincia [idprov=" + idprov + ", codigo=" + codigo + ", nombre=" + nombre + ", estado=" + estado
				+ ", iddep=" + iddep + ", departamento=" + departamento + "]";
	}
	
	
	
	
	
	
}
