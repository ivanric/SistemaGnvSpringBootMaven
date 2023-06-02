package app.models;

public class CapacidadCilindro {
	protected Integer idcapcil;
	protected String nombre;
	protected Integer estado;
	public CapacidadCilindro() {
//		super();
		// TODO Auto-generated constructor stub
	}
	public CapacidadCilindro(Integer idcapcil, String nombre, Integer estado) {
//		super();
		this.idcapcil = idcapcil;
		this.nombre = nombre;
		this.estado = estado;
	}
	public Integer getIdcapcil() {
		return idcapcil;
	}
	public void setIdcapcil(Integer idcapcil) {
		this.idcapcil = idcapcil;
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
		return "CapacidadCilindro [idcapcil=" + idcapcil + ", nombre=" + nombre + ", estado=" + estado + "]";
	}
	
	
}
