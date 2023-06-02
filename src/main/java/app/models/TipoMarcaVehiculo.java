package app.models;

public class TipoMarcaVehiculo {
	protected Integer idtipmarc;
	protected String nombre;
	protected Integer estado;
	
	protected MarcaVehiculo MarcaVehiculo;

	
	
	public TipoMarcaVehiculo() {
//		super();
		// TODO Auto-generated constructor stub
	}



	public TipoMarcaVehiculo(Integer idtipmarc, String nombre, Integer estado, app.models.MarcaVehiculo marcaVehiculo) {
//		super();
		this.idtipmarc = idtipmarc;
		this.nombre = nombre;
		this.estado = estado;
		MarcaVehiculo = marcaVehiculo;
	}



	public Integer getIdtipmarc() {
		return idtipmarc;
	}



	public void setIdtipmarc(Integer idtipmarc) {
		this.idtipmarc = idtipmarc;
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



	public MarcaVehiculo getMarcaVehiculo() {
		return MarcaVehiculo;
	}



	public void setMarcaVehiculo(MarcaVehiculo marcaVehiculo) {
		MarcaVehiculo = marcaVehiculo;
	}



	@Override
	public String toString() {
		return "TipoMarcaVehiculo [idtipmarc=" + idtipmarc + ", nombre=" + nombre + ", estado=" + estado
				+ ", MarcaVehiculo=" + MarcaVehiculo + "]";
	}



	
	
}
