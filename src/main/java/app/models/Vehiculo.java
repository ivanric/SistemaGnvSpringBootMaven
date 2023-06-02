package app.models;

import java.util.List;

public class Vehiculo {
	protected String placa;
	protected String tjeta_prioridad;
	protected String cilindrada;
	protected String color;
	protected String num_motor;
	protected String num_chasis;
	protected Integer kitGlpSiNo;	
	protected Integer estado;	
	
	protected Integer idtipv;
	protected Integer idmarcv;
	protected Integer idtipmarc;//AUMENTADO
	protected String tipoveh;
	protected Integer idTipSv;
	protected Integer idtipoMotorVeh;	
	protected Integer idmodv;	
	
	
	
	protected List<CombustibleVehiculo> combustibleVehiculo;
	protected TipoVehiculo tipoVehiculo;
	protected MarcaVehiculo marcaVehiculo;
	protected TipoMarcaVehiculo tipoMarcaVehiculo;
	protected TipoServicioVehiculo tipoServicio;
	protected TipoMotorVehiculo tipoMotor;
	protected ModeloVehiculo modeloVehiculo;

	protected Poliza poliza;
	
	private int Tot;
	
	public Vehiculo() {
//		super();
	}

	public Vehiculo(String placa, String tjeta_prioridad, String cilindrada, String color, String num_motor,
			String num_chasis, Integer kitGlpSiNo, Integer estado, Integer idtipv, Integer idmarcv, Integer idtipmarc,
			String tipoveh, Integer idTipSv, Integer idtipoMotorVeh, Integer idmodv,
			List<CombustibleVehiculo> combustibleVehiculo, TipoVehiculo tipoVehiculo, MarcaVehiculo marcaVehiculo,
			TipoMarcaVehiculo tipoMarcaVehiculo, TipoServicioVehiculo tipoServicio, TipoMotorVehiculo tipoMotor,
			ModeloVehiculo modeloVehiculo, Poliza poliza, int tot) {
		super();
		this.placa = placa;
		this.tjeta_prioridad = tjeta_prioridad;
		this.cilindrada = cilindrada;
		this.color = color;
		this.num_motor = num_motor;
		this.num_chasis = num_chasis;
		this.kitGlpSiNo = kitGlpSiNo;
		this.estado = estado;
		this.idtipv = idtipv;
		this.idmarcv = idmarcv;
		this.idtipmarc = idtipmarc;
		this.tipoveh = tipoveh;
		this.idTipSv = idTipSv;
		this.idtipoMotorVeh = idtipoMotorVeh;
		this.idmodv = idmodv;
		this.combustibleVehiculo = combustibleVehiculo;
		this.tipoVehiculo = tipoVehiculo;
		this.marcaVehiculo = marcaVehiculo;
		this.tipoMarcaVehiculo = tipoMarcaVehiculo;
		this.tipoServicio = tipoServicio;
		this.tipoMotor = tipoMotor;
		this.modeloVehiculo = modeloVehiculo;
		this.poliza = poliza;
		Tot = tot;
	}

	public String getPlaca() {
		return placa;
	}

	public void setPlaca(String placa) {
		this.placa = placa;
	}

	public String getTjeta_prioridad() {
		return tjeta_prioridad;
	}

	public void setTjeta_prioridad(String tjeta_prioridad) {
		this.tjeta_prioridad = tjeta_prioridad;
	}

	public String getCilindrada() {
		return cilindrada;
	}

	public void setCilindrada(String cilindrada) {
		this.cilindrada = cilindrada;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getNum_motor() {
		return num_motor;
	}

	public void setNum_motor(String num_motor) {
		this.num_motor = num_motor;
	}

	public String getNum_chasis() {
		return num_chasis;
	}

	public void setNum_chasis(String num_chasis) {
		this.num_chasis = num_chasis;
	}

	public Integer getKitGlpSiNo() {
		return kitGlpSiNo;
	}

	public void setKitGlpSiNo(Integer kitGlpSiNo) {
		this.kitGlpSiNo = kitGlpSiNo;
	}

	public Integer getEstado() {
		return estado;
	}

	public void setEstado(Integer estado) {
		this.estado = estado;
	}

	public Integer getIdtipv() {
		return idtipv;
	}

	public void setIdtipv(Integer idtipv) {
		this.idtipv = idtipv;
	}

	public Integer getIdmarcv() {
		return idmarcv;
	}

	public void setIdmarcv(Integer idmarcv) {
		this.idmarcv = idmarcv;
	}

	public Integer getIdtipmarc() {
		return idtipmarc;
	}

	public void setIdtipmarc(Integer idtipmarc) {
		this.idtipmarc = idtipmarc;
	}

	public String getTipoveh() {
		return tipoveh;
	}

	public void setTipoveh(String tipoveh) {
		this.tipoveh = tipoveh;
	}

	public Integer getIdTipSv() {
		return idTipSv;
	}

	public void setIdTipSv(Integer idTipSv) {
		this.idTipSv = idTipSv;
	}

	public Integer getIdtipoMotorVeh() {
		return idtipoMotorVeh;
	}

	public void setIdtipoMotorVeh(Integer idtipoMotorVeh) {
		this.idtipoMotorVeh = idtipoMotorVeh;
	}

	public Integer getIdmodv() {
		return idmodv;
	}

	public void setIdmodv(Integer idmodv) {
		this.idmodv = idmodv;
	}

	public List<CombustibleVehiculo> getCombustibleVehiculo() {
		return combustibleVehiculo;
	}

	public void setCombustibleVehiculo(List<CombustibleVehiculo> combustibleVehiculo) {
		this.combustibleVehiculo = combustibleVehiculo;
	}

	public TipoVehiculo getTipoVehiculo() {
		return tipoVehiculo;
	}

	public void setTipoVehiculo(TipoVehiculo tipoVehiculo) {
		this.tipoVehiculo = tipoVehiculo;
	}

	public MarcaVehiculo getMarcaVehiculo() {
		return marcaVehiculo;
	}

	public void setMarcaVehiculo(MarcaVehiculo marcaVehiculo) {
		this.marcaVehiculo = marcaVehiculo;
	}

	public TipoMarcaVehiculo getTipoMarcaVehiculo() {
		return tipoMarcaVehiculo;
	}

	public void setTipoMarcaVehiculo(TipoMarcaVehiculo tipoMarcaVehiculo) {
		this.tipoMarcaVehiculo = tipoMarcaVehiculo;
	}

	public TipoServicioVehiculo getTipoServicio() {
		return tipoServicio;
	}

	public void setTipoServicio(TipoServicioVehiculo tipoServicio) {
		this.tipoServicio = tipoServicio;
	}

	public TipoMotorVehiculo getTipoMotor() {
		return tipoMotor;
	}

	public void setTipoMotor(TipoMotorVehiculo tipoMotor) {
		this.tipoMotor = tipoMotor;
	}

	public ModeloVehiculo getModeloVehiculo() {
		return modeloVehiculo;
	}

	public void setModeloVehiculo(ModeloVehiculo modeloVehiculo) {
		this.modeloVehiculo = modeloVehiculo;
	}

	public Poliza getPoliza() {
		return poliza;
	}

	public void setPoliza(Poliza poliza) {
		this.poliza = poliza;
	}

	public int getTot() {
		return Tot;
	}

	public void setTot(int tot) {
		Tot = tot;
	}

	@Override
	public String toString() {
		return "Vehiculo [placa=" + placa + ", tjeta_prioridad=" + tjeta_prioridad + ", cilindrada=" + cilindrada
				+ ", color=" + color + ", num_motor=" + num_motor + ", num_chasis=" + num_chasis + ", kitGlpSiNo="
				+ kitGlpSiNo + ", estado=" + estado + ", idtipv=" + idtipv + ", idmarcv=" + idmarcv + ", idtipmarc="
				+ idtipmarc + ", tipoveh=" + tipoveh + ", idTipSv=" + idTipSv + ", idtipoMotorVeh=" + idtipoMotorVeh
				+ ", idmodv=" + idmodv + ", combustibleVehiculo=" + combustibleVehiculo + ", tipoVehiculo="
				+ tipoVehiculo + ", marcaVehiculo=" + marcaVehiculo + ", tipoMarcaVehiculo=" + tipoMarcaVehiculo
				+ ", tipoServicio=" + tipoServicio + ", tipoMotor=" + tipoMotor + ", modeloVehiculo=" + modeloVehiculo
				+ ", poliza=" + poliza + ", Tot=" + Tot + "]";
	}

	
}	
