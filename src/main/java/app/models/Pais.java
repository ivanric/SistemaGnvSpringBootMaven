package app.models;

public class Pais {
	protected Integer idpais;
	protected String codigo;
	protected String nombre;
	protected Integer estado;
	public Pais() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Pais(Integer idpais, String codigo, String nombre, Integer estado) {
		super();
		this.idpais = idpais;
		this.codigo = codigo;
		this.nombre = nombre;
		this.estado = estado;
	}
	public Integer getIdpais() {
		return idpais;
	}
	public void setIdpais(Integer idpais) {
		this.idpais = idpais;
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
	@Override
	public String toString() {
		return "Pais [idpais=" + idpais + ", codigo=" + codigo + ", nombre=" + nombre + ", estado=" + estado + "]";
	}

	
}
