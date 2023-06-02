package app.models;

import java.util.List;

import app.utilidades.ALGORITMO3DES_Ecript_Desencript;


public class Persona {
	private Integer idper;
	private String ci;
	private String ciCod;
	private String nombres;
	private String ap;
	private String am;
	private String razonsocial;
	
	//adicionado para contrato
	private String estadocivil;
	private String profesion;
	private String naturalde;
	
	
	private String genero;
	private String direccion;
	private String email;
	private String foto;
	private Integer estado;
	private Usuario usuario;
	private Beneficiario beneficiario;
	private List<Telefono> ListaTelf;
	
	private int Tot;
	
	public Persona() {
	}

	public Persona(Integer idper, String ci, String ciCod, String nombres, String ap, String am, String razonsocial,
			String estadocivil, String profesion, String naturalde, String genero, String direccion, String email,
			String foto, Integer estado, Usuario usuario, Beneficiario beneficiario, List<Telefono> listaTelf,
			int tot) {
		super();
		this.idper = idper;
		this.ci = ci;
		this.ciCod = ciCod;
		this.nombres = nombres;
		this.ap = ap;
		this.am = am;
		this.razonsocial = razonsocial;
		this.estadocivil = estadocivil;
		this.profesion = profesion;
		this.naturalde = naturalde;
		this.genero = genero;
		this.direccion = direccion;
		this.email = email;
		this.foto = foto;
		this.estado = estado;
		this.usuario = usuario;
		this.beneficiario = beneficiario;
		ListaTelf = listaTelf;
		Tot = tot;
	}

	public Integer getIdper() {
		return idper;
	}

	public void setIdper(Integer idper) {
		this.idper = idper;
	}

	public String getCi() {
		return ci;
	}

	public void setCi(String ci) {
		this.ci = ci;
	}

	public String getCiCod() {
		return ciCod;
	}

	public void setCiCod(String ciCod) {
		this.ciCod = ciCod;
	}

	public String getNombres() {
		return nombres;
	}

	public void setNombres(String nombres) {
		this.nombres = nombres;
	}

	public String getAp() {
		return ap;
	}

	public void setAp(String ap) {
		this.ap = ap;
	}

	public String getAm() {
		return am;
	}

	public void setAm(String am) {
		this.am = am;
	}

	public String getRazonsocial() {
		return razonsocial;
	}

	public void setRazonsocial(String razonsocial) {
		this.razonsocial = razonsocial;
	}

	public String getEstadocivil() {
		return estadocivil;
	}

	public void setEstadocivil(String estadocivil) {
		this.estadocivil = estadocivil;
	}

	public String getProfesion() {
		return profesion;
	}

	public void setProfesion(String profesion) {
		this.profesion = profesion;
	}

	public String getNaturalde() {
		return naturalde;
	}

	public void setNaturalde(String naturalde) {
		this.naturalde = naturalde;
	}

	public String getGenero() {
		return genero;
	}

	public void setGenero(String genero) {
		this.genero = genero;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFoto() {
		return foto;
	}

	public void setFoto(String foto) {
		this.foto = foto;
	}

	public Integer getEstado() {
		return estado;
	}

	public void setEstado(Integer estado) {
		this.estado = estado;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Beneficiario getBeneficiario() {
		return beneficiario;
	}

	public void setBeneficiario(Beneficiario beneficiario) {
		this.beneficiario = beneficiario;
	}

	public List<Telefono> getListaTelf() {
		return ListaTelf;
	}

	public void setListaTelf(List<Telefono> listaTelf) {
		ListaTelf = listaTelf;
	}

	public int getTot() {
		return Tot;
	}

	public void setTot(int tot) {
		Tot = tot;
	}

	@Override
	public String toString() {
		return "Persona [idper=" + idper + ", ci=" + ci + ", ciCod=" + ciCod + ", nombres=" + nombres + ", ap=" + ap
				+ ", am=" + am + ", razonsocial=" + razonsocial + ", estadocivil=" + estadocivil + ", profesion="
				+ profesion + ", naturalde=" + naturalde + ", genero=" + genero + ", direccion=" + direccion
				+ ", email=" + email + ", foto=" + foto + ", estado=" + estado + ", usuario=" + usuario
				+ ", beneficiario=" + beneficiario + ", ListaTelf=" + ListaTelf + ", Tot=" + Tot + "]";
	}
	
}
