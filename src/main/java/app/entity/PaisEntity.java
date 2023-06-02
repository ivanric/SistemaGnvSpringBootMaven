package app.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;




@Entity
@Table(name = "pais")
public class PaisEntity implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	@Id
	@Column(name = "idpais")
//	@GeneratedValue(strategy = GenerationType.IDENTITY)
//	@GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "public.id_pais_seq")
//	@SequenceGenerator(
//	    name = "public.id_pais_seq"
//	)
	private Integer id;
	@Column(name = "codigo") 
	private String codigo;
	@Column(name = "nombre")
	private String nombre;
	@Column(name = "estado")
	private Integer estado;
	public PaisEntity() {
		super();
		// TODO Auto-generated constructor stub
	}
	public PaisEntity(Integer id, String codigo, String nombre, Integer estado) {
		super();
		this.id = id;
		this.codigo = codigo;
		this.nombre = nombre;
		this.estado = estado;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
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
		return "PaisEntity [id=" + id+ ", codigo=" + codigo + ", nombre=" + nombre + ", estado=" + estado
				+ "]";
	}
	
	
}
