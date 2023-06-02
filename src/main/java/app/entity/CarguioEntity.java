package app.entity;

import java.io.Serializable;
import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "carguio")
public class CarguioEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "idcarg")
	private Integer id;
	@Column(name = "fecha")
	private Date fecha;
	@Column(name = "nombre")
	private String nombre;
	
	@Column(name = "placa")
	private String placa;
	@Column(name = "volumen")
	private String volumen;
	@Column(name = "fondorotatorio")
	private String fondorotatorio;
	@Column(name = "total")
	private String total;
	@Column(name = "anio")
	private String anio;
	@Column(name = "estado")
	private Integer estado;
	@ManyToOne
	@JoinColumn(name = "idest")
	private EstacionEntity estacion;

	public CarguioEntity() {
		super();
		// TODO Auto-generated constructor stub
	}

	public CarguioEntity(Integer id, Date fecha, String nombre, String placa, String volumen, String fondorotatorio,
			String total, String anio, Integer estado, EstacionEntity estacion) {
		super();
		this.id = id;
		this.fecha = fecha;
		this.nombre = nombre;
		this.placa = placa;
		this.volumen = volumen;
		this.fondorotatorio = fondorotatorio;
		this.total = total;
		this.anio = anio;
		this.estado = estado;
		this.estacion = estacion;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getPlaca() {
		return placa;
	}

	public void setPlaca(String placa) {
		this.placa = placa;
	}

	public String getVolumen() {
		return volumen;
	}

	public void setVolumen(String volumen) {
		this.volumen = volumen;
	}

	public String getFondorotatorio() {
		return fondorotatorio;
	}

	public void setFondorotatorio(String fondorotatorio) {
		this.fondorotatorio = fondorotatorio;
	}

	public String getTotal() {
		return total;
	}

	public void setTotal(String total) {
		this.total = total;
	}

	public String getAnio() {
		return anio;
	}

	public void setAnio(String anio) {
		this.anio = anio;
	}

	public Integer getEstado() {
		return estado;
	}

	public void setEstado(Integer estado) {
		this.estado = estado;
	}

	public EstacionEntity getEstacion() {
		return estacion;
	}

	public void setEstacion(EstacionEntity estacion) {
		this.estacion = estacion;
	}

	@Override
	public String toString() {
		return "CarguioEntity [id=" + id + ", fecha=" + fecha + ", nombre=" + nombre + ", placa=" + placa + ", volumen="
				+ volumen + ", fondorotatorio=" + fondorotatorio + ", total=" + total + ", anio=" + anio + ", estado="
				+ estado + ", estacion=" + estacion + "]";
	}

	
	
}
