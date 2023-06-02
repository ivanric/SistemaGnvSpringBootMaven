package app.models;

public class Departamento {
	protected Integer iddep;
	protected String codigo;
	protected String nombre;
	protected Integer estado;
	protected Integer idpais;
	protected String abreviacion;
	
	protected Pais pais;

	public Departamento() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Departamento(Integer iddep, String codigo, String nombre, Integer estado, Integer idpais, String abreviacion,
			Pais pais) {
		super();
		this.iddep = iddep;
		this.codigo = codigo;
		this.nombre = nombre;
		this.estado = estado;
		this.idpais = idpais;
		this.abreviacion = abreviacion;
		this.pais = pais;
	}

	public Integer getIddep() {
		return iddep;
	}

	public void setIddep(Integer iddep) {
		this.iddep = iddep;
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

	public Integer getIdpais() {
		return idpais;
	}

	public void setIdpais(Integer idpais) {
		this.idpais = idpais;
	}

	public String getAbreviacion() {
		return abreviacion;
	}

	public void setAbreviacion(String abreviacion) {
		this.abreviacion = abreviacion;
	}

	public Pais getPais() {
		return pais;
	}

	public void setPais(Pais pais) {
		this.pais = pais;
	}

	@Override
	public String toString() {
		return "Departamento [iddep=" + iddep + ", codigo=" + codigo + ", nombre=" + nombre + ", estado=" + estado
				+ ", idpais=" + idpais + ", abreviacion=" + abreviacion + ", pais=" + pais + "]";
	}
	
	
	
	
	

}
