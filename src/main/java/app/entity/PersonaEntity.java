package app.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "persona")
public class PersonaEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "idper")
	private Integer id;
	@Column(name = "razonsocial")
	private String razonsocial;
	@Column(name = "ci")
	private String ci;
	@Column(name = "cicod")
	private String cicod;
	@Column(name = "email")
	private String email;
	@Column(name = "direccion")
	private String direccion;
	@Column(name = "estado")
	private Integer estado;
	public PersonaEntity() {
		super();
		// TODO Auto-generated constructor stub
	}
	public PersonaEntity(Integer id, String razonsocial, String ci, String cicod, String email, String direccion,
			Integer estado) {
		super();
		this.id = id;
		this.razonsocial = razonsocial;
		this.ci = ci;
		this.cicod = cicod;
		this.email = email;
		this.direccion = direccion;
		this.estado = estado;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getRazonsocial() {
		return razonsocial;
	}
	public void setRazonsocial(String razonsocial) {
		this.razonsocial = razonsocial;
	}
	public String getCi() {
		return ci;
	}
	public void setCi(String ci) {
		this.ci = ci;
	}
	public String getCicod() {
		return cicod;
	}
	public void setCicod(String cicod) {
		this.cicod = cicod;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getDireccion() {
		return direccion;
	}
	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}
	public Integer getEstado() {
		return estado;
	}
	public void setEstado(Integer estado) {
		this.estado = estado;
	}
	@Override
	public String toString() {
		return "PersonaEntity [id=" + id + ", razonsocial=" + razonsocial + ", ci=" + ci + ", cicod=" + cicod
				+ ", email=" + email + ", direccion=" + direccion + ", estado=" + estado + "]";
	}
	
	
	
	
	


}
