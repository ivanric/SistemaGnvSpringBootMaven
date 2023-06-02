package app.models;

public class MarcaValvulaCilindro {
	protected int idmarcvalcil;
	protected String nombre;
	protected int estado;
	public MarcaValvulaCilindro() {
//		super();
		// TODO Auto-generated constructor stub
	}
	public MarcaValvulaCilindro(int idmarcvalcil, String nombre, int estado) {
//		super();
		this.idmarcvalcil = idmarcvalcil;
		this.nombre = nombre;
		this.estado = estado;
	}
	public int getIdmarcvalcil() {
		return idmarcvalcil;
	}
	public void setIdmarcvalcil(int idmarcvalcil) {
		this.idmarcvalcil = idmarcvalcil;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public int getEstado() {
		return estado;
	}
	public void setEstado(int estado) {
		this.estado = estado;
	}
	@Override
	public String toString() {
		return "MarcaValvulaCilindro [idmarcvalcil=" + idmarcvalcil + ", nombre=" + nombre + ", estado=" + estado + "]";
	}
	
}
