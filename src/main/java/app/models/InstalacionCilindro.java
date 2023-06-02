package app.models;

public class InstalacionCilindro {
	protected int idmarccil,idregistroKit,idcapcil,idmarcvalcil;
	protected String serie,fechaFab,inmetro,serievalvula;
	protected MarcaCilindro marcaCilindro;
	protected CapacidadCilindro capacidadCilindro;
	protected MarcaValvulaCilindro marcaValvulaCilindro;
	public InstalacionCilindro() {
//		super();
		// TODO Auto-generated constructor stub
	}
	public InstalacionCilindro(int idmarccil, int idregistroKit, int idcapcil, int idmarcvalcil, String serie,
			String fechaFab, String inmetro, String serievalvula, MarcaCilindro marcaCilindro,
			CapacidadCilindro capacidadCilindro, MarcaValvulaCilindro marcaValvulaCilindro) {
//		super();
		this.idmarccil = idmarccil;
		this.idregistroKit = idregistroKit;
		this.idcapcil = idcapcil;
		this.idmarcvalcil = idmarcvalcil;
		this.serie = serie;
		this.fechaFab = fechaFab;
		this.inmetro = inmetro;
		this.serievalvula = serievalvula;
		this.marcaCilindro = marcaCilindro;
		this.capacidadCilindro = capacidadCilindro;
		this.marcaValvulaCilindro = marcaValvulaCilindro;
	}
	public int getIdmarccil() {
		return idmarccil;
	}
	public void setIdmarccil(int idmarccil) {
		this.idmarccil = idmarccil;
	}
	public int getIdregistroKit() {
		return idregistroKit;
	}
	public void setIdregistroKit(int idregistroKit) {
		this.idregistroKit = idregistroKit;
	}
	public int getIdcapcil() {
		return idcapcil;
	}
	public void setIdcapcil(int idcapcil) {
		this.idcapcil = idcapcil;
	}
	public int getIdmarcvalcil() {
		return idmarcvalcil;
	}
	public void setIdmarcvalcil(int idmarcvalcil) {
		this.idmarcvalcil = idmarcvalcil;
	}
	public String getSerie() {
		return serie;
	}
	public void setSerie(String serie) {
		this.serie = serie;
	}
	public String getFechaFab() {
		return fechaFab;
	}
	public void setFechaFab(String fechaFab) {
		this.fechaFab = fechaFab;
	}
	public String getInmetro() {
		return inmetro;
	}
	public void setInmetro(String inmetro) {
		this.inmetro = inmetro;
	}
	public String getSerievalvula() {
		return serievalvula;
	}
	public void setSerievalvula(String serievalvula) {
		this.serievalvula = serievalvula;
	}
	public MarcaCilindro getMarcaCilindro() {
		return marcaCilindro;
	}
	public void setMarcaCilindro(MarcaCilindro marcaCilindro) {
		this.marcaCilindro = marcaCilindro;
	}
	public CapacidadCilindro getCapacidadCilindro() {
		return capacidadCilindro;
	}
	public void setCapacidadCilindro(CapacidadCilindro capacidadCilindro) {
		this.capacidadCilindro = capacidadCilindro;
	}
	public MarcaValvulaCilindro getMarcaValvulaCilindro() {
		return marcaValvulaCilindro;
	}
	public void setMarcaValvulaCilindro(MarcaValvulaCilindro marcaValvulaCilindro) {
		this.marcaValvulaCilindro = marcaValvulaCilindro;
	}
	@Override
	public String toString() {
		return "InstalacionCilindro [idmarccil=" + idmarccil + ", idregistroKit=" + idregistroKit + ", idcapcil="
				+ idcapcil + ", idmarcvalcil=" + idmarcvalcil + ", serie=" + serie + ", fechaFab=" + fechaFab
				+ ", inmetro=" + inmetro + ", serievalvula=" + serievalvula + ", marcaCilindro=" + marcaCilindro
				+ ", capacidadCilindro=" + capacidadCilindro + ", marcaValvulaCilindro=" + marcaValvulaCilindro + "]";
	}
	
	
}
