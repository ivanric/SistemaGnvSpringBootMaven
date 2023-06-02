package app.models;

import org.springframework.web.multipart.MultipartFile;

public class CargarArchivo {
	private String descripcion;
	private MultipartFile archivo;
	@Override
	public String toString() {
		return "CargarArchivo [descripcion=" + descripcion + ", archivo=" + archivo + "]";
	}
	public CargarArchivo() {
		super();
		// TODO Auto-generated constructor stub
	}
	public CargarArchivo(String descripcion, MultipartFile archivo) {
		super();
		this.descripcion = descripcion;
		this.archivo = archivo;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public MultipartFile getArchivo() {
		return archivo;
	}
	public void setArchivo(MultipartFile archivo) {
		this.archivo = archivo;
	}
	
	
}
