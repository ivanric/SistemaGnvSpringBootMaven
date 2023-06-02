package app.entity;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;


@Entity
@Table(name = "estacion")
public class EstacionEntity implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "idest")
//	@GeneratedValue(strategy = GenerationType.IDENTITY)
//	@GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "public.id_pais_seq")
//	@SequenceGenerator(
//	    name = "public.id_pais_seq"
//	)
	private Integer id;
	
	@Column(name = "nombreestacion")
	private String nombreestacion;
	
	@Column(name = "direccion")
	private String direccion;

	@Column(name = "estado")
	private Integer estado;
	
	@Column(name = "fecha_registro") 
	private String fecha_registro;
	
	@ManyToOne
	@JoinColumn(name = "idprov")
	ProvinciaEntity provincia;
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name="idper")
	private PersonaEntity persona;

	public EstacionEntity() {
		super();
		// TODO Auto-generated constructor stub
	}

	public EstacionEntity(Integer id, String fecha_registro, String nombreestacion, Integer estado,
			ProvinciaEntity provincia, PersonaEntity persona) {
		super();
		this.id = id;
		this.fecha_registro = fecha_registro;
		this.nombreestacion = nombreestacion;
		this.estado = estado;
		this.provincia = provincia;
		this.persona = persona;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getFecha_registro() {
		return fecha_registro;
	}

	public void setFecha_registro(String fecha_registro) {
		this.fecha_registro = fecha_registro;
	}

	public String getNombreestacion() {
		return nombreestacion;
	}

	public void setNombreestacion(String nombreestacion) {
		this.nombreestacion = nombreestacion;
	}

	public Integer getEstado() {
		return estado;
	}

	public void setEstado(Integer estado) {
		this.estado = estado;
	}

	public ProvinciaEntity getProvincia() {
		return provincia;
	}

	public void setProvincia(ProvinciaEntity provincia) {
		this.provincia = provincia;
	}

	public PersonaEntity getPersona() {
		return persona;
	}

	public void setPersona(PersonaEntity persona) {
		this.persona = persona;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return "EstacionEntity [id=" + id + ", fecha_registro=" + fecha_registro + ", nombreestacion=" + nombreestacion
				+ ", estado=" + estado + ", provincia=" + provincia + ", persona=" + persona + "]";
	}
	
	
	
	
	
}
