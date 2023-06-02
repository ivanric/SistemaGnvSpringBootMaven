package app.manager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.itextpdf.text.Utilities;

import app.manager.ManejadorBeneficiarios.objPersona;
import app.manager.ManejadorSolicitudes.objBeneficiario;
import app.manager.ManejadorSolicitudes.objSolicitud;
import app.manager.ManejadorSolicitudes.objSolicitud_d;
import app.manager.ManejadorSolicitudes.objTelefono;
import app.manager.ManejadorSolicitudes.objVehiculo;
import app.manager.ManejadorTalleres.objTaller;
import app.manager.ManejadorUsuarios.objUsuario;
import app.models.Beneficiario;
import app.models.ChipRom;
import app.models.Cilindro;
import app.models.Combustible;
import app.models.CombustibleVehiculo;
import app.models.FactorCobro;
import app.models.InstalacionCilindro;
import app.models.MarcaCilindro;
import app.models.MarcaReductor;
import app.models.MarcaValvulaCilindro;
import app.models.OrdenServicio;
import app.models.OrdenServicioRecalificacion;
import app.models.Persona;
import app.models.Reductor;
import app.models.Servicio;
import app.models.Solicitud;
import app.models.SolicitudReposicionRecalificacion;
import app.models.Taller;
import app.models.TallerRecalificacion;
import app.models.Telefono;
import app.models.TipoMotorVehiculo;
import app.models.Usuario;
import app.utilidades.Constantes;
import app.utilidades.DatabaseConfig;
import app.utilidades.Db_Coneccion;

@Service
public class ManejadorServicios {
	
	@Autowired   
	ManejadorSolicitudes manejadorSolicitudes;
//	@Autowired
//	DataSource dataSource;
//	private JdbcTemplate db=new JdbcTemplate(dataSource);
	private final  JdbcTemplate db;
	public ManejadorServicios(JdbcTemplate jdbcTemplate) {
		this.db = jdbcTemplate;
		
	}
	
	public class objTaller implements RowMapper<Taller>{
		@Override
		public Taller mapRow(ResultSet rs, int arg1) throws SQLException {
			Taller t= new Taller();
			t.setIdtall(rs.getInt("idtall"));
			t.setNombretall(rs.getString("nombretall"));
			t.setDireccion(rs.getString("direccion"));
			t.setFecha_registro(rs.getString("fecha_registro"));
			t.setEstado(rs.getInt("estado"));
			try {
				t.setPersona(metPersona(rs.getInt("idper")));
			}catch (Exception e){
				System.out.println(e.getMessage());
				e.printStackTrace();
				t.setPersona(null);
			}
			return t; 
	    }
	}
	public class objTallerRecalificacion implements RowMapper<TallerRecalificacion>{
		@Override
		public TallerRecalificacion mapRow(ResultSet rs, int arg1) throws SQLException {
			TallerRecalificacion t= new TallerRecalificacion();
			t.setIdtall(rs.getInt("idtall"));
			t.setNombretall(rs.getString("nombretall"));
			t.setDireccion(rs.getString("direccion"));
			t.setFecha_registro(rs.getString("fecha_registro"));
			t.setEstado(rs.getInt("estado"));
			try {
				t.setPersona(metPersona(rs.getInt("idper")));
			}catch (Exception e){
				t.setPersona(null);
			}
			return t;
	    }
	}
	public class objChipRom implements RowMapper<ChipRom>{
		@Override
		public ChipRom mapRow(ResultSet rs, int arg1) throws SQLException {
			ChipRom c= new ChipRom();
			c.setIdRom(rs.getInt("idRom"));
			c.setNombreChip(rs.getString("nombreChip"));
			c.setFecha(rs.getString("fecha"));
			c.setBloqueado(rs.getInt("bloqueado"));
			return c;
	    }
	}

	public class objOrdenServicio implements RowMapper<OrdenServicio>{
		@Override
		public OrdenServicio mapRow(ResultSet rs, int arg1) throws SQLException {
			OrdenServicio os= new OrdenServicio();
			os.setIdordserv(rs.getInt("idordserv"));
			os.setNumords(rs.getInt("numords"));
			os.setIdsolt(rs.getInt("idsolt"));
			os.setFechaords(rs.getString("fechaords"));
			try {
				os.setTaller(metTaller(rs.getInt("idtall")));
			}catch (Exception e){
				e.printStackTrace();
				System.out.println(e.getMessage());
				os.setTaller(null);
			}
			try {
				os.setChipRom(metChip(rs.getInt("idRom")));
			}catch (Exception e){
				os.setChipRom(null);
			}
			try {
				os.setServicio(metServicio(rs.getInt("idserv")));
			}catch (Exception e){
				os.setServicio(null);
			}
			os.setInstaladoSiNo(rs.getInt("instaladoSiNo"));
			try {
				os.setPersona(metUsuario(rs.getString("login")));
			} catch (Exception e) {
				// TODO: handle exception
				os.setPersona(null);
			}
			try {
				os.setTot(rs.getInt("tot"));
			} catch (Exception e) {
				// TODO: handle exception
				os.setTot(0);;
			}
			
			
			
			//solicitud
			try {
				os.setSolicitud(metSolicitud(rs.getInt("idsolt")));
			}catch (Exception e){
				e.printStackTrace();
				System.out.println(e.getMessage());
				os.setSolicitud(null);
			}
			return os;
	    }
	}

	
	public class objOrdenServicioRecalificacion implements RowMapper<OrdenServicioRecalificacion>{
		@Override
		public OrdenServicioRecalificacion mapRow(ResultSet rs, int arg1) throws SQLException {
			OrdenServicioRecalificacion os= new OrdenServicioRecalificacion();
			os.setIdordservrecal(rs.getInt("idordservrecal"));
			os.setNumords(rs.getInt("numords"));
			os.setIdrecal(rs.getInt("idrecal"));
			os.setFechaords(rs.getString("fechaords"));
			os.setInstaladosino(rs.getInt("instaladosino"));
			os.setIdinst(rs.getInt("idinst"));
			os.setIdfirm(rs.getInt("idfirm"));
			os.setAnio(rs.getInt("anio"));
			os.setIdtallconver(rs.getInt("idtallconver"));
			os.setIdtallrecal(rs.getInt("idtallrecal"));
			os.setRecal_cil(rs.getInt("recal_cil"));
			os.setReponer_cil(rs.getInt("reponer_cil"));
			try {
				os.setReponer_kit_completo(rs.getInt("reponer_kit_completo"));
			} catch (Exception e) {
				os.setReponer_kit_completo(null);
			}
			try {
				os.setReponer_kit_partes(rs.getInt("reponer_kit_partes"));
			} catch (Exception e) {
				os.setReponer_kit_partes(null);
			}
			

			os.setEstado(rs.getInt("estado"));
			
			try {
				os.setTallerconversion(metTallerConversion(rs.getInt("idtallconver")));
			}catch (Exception e){
				os.setTallerconversion(null);
			}
			
			try {
				int idrecal=rs.getInt("idtallrecal");
				System.out.println("idrecal_reposicion:"+idrecal);
				
				if(idrecal!=0) {
					os.setTallerrecalificacion(metTallerRecalificacion(idrecal));
				}else {
					os.setTallerrecalificacion(null);
				}
				
			}catch (Exception e){
				System.out.println(e.getMessage());
				e.printStackTrace();
				os.setTallerrecalificacion(null);
			}
//			try {
//				os.setTallerrecalificacion(metTallerRecalificacion(rs.getInt("idtallrecal")));
//			}catch (Exception e){
//				e.printStackTrace();;
//				System.out.println(e.getMessage());
//				os.setTallerrecalificacion(null);
//			}
			

			os.setInstaladosino(rs.getInt("instaladosino"));
			try {
				os.setPersona(metUsuario(rs.getString("login")));
			} catch (Exception e) {
				// TODO: handle exception
				os.setPersona(null);
			}
			try {
				os.setTot(rs.getInt("tot"));
			} catch (Exception e) {
				// TODO: handle exception
				os.setTot(0);;
			}
			
			
			
			//solicitud
			try {
				os.setSolicitudreposicionrecalificacion(metSolicitudRecalificacion(rs.getInt("idrecal")));
			}catch (Exception e){
				os.setSolicitudreposicionrecalificacion(null);
			}
			return os;
	    }
	}

	public class objServicios implements RowMapper<Servicio>{
		@Override
		public Servicio mapRow(ResultSet rs, int arg1) throws SQLException {
			Servicio s= new Servicio();
			s.setIdserv(rs.getInt("idserv"));
			try {
				s.setCilindro(metCilindro(rs.getInt("idcil")));
			} catch (Exception e) {
				s.setCilindro(null);
			}
			try {
				s.setReductor(metReductor(rs.getInt("idreduc")));
			} catch (Exception e) {
				s.setReductor(null);
			}
			s.setFactorCobro(rs.getString("factorcobro"));

			try {
				s.setTipoMotorVehiculo(metTipoMotorVehiculo(rs.getInt("idtipomotorveh")));
			} catch (Exception e) {
				s.setTipoMotorVehiculo(null);
			}
			try {
				s.setCombustible(metCombustible(rs.getInt("idcomb")));
			} catch (Exception e) {
				// TODO: handle exception
				s.setCombustible(null);
			}
			try {
				s.setPersona(metUsuario(rs.getString("login")));
			} catch (Exception e) {	
				s.setPersona(null);
			}
			
			s.setNumPistones(rs.getInt("numpistones"));
			s.setPrecioDolares(rs.getString("preciodolares"));
			s.setPrecioBolivianos(rs.getString("preciobolivianos"));
			s.setPrecioTotal(rs.getString("preciototal"));
			s.setFecha(rs.getString("fecha"));
			s.setEstado(rs.getInt("estado"));
			return s;
	    }
	}
	
	public class objServicios_d implements RowMapper<Servicio>{
		@Override
		public Servicio mapRow(ResultSet rs, int arg1) throws SQLException {
			Servicio s= new Servicio();
			s.setIdserv(rs.getInt("idserv"));
			try {
				s.setCilindro(metCilindro(rs.getInt("idcil")));
			} catch (Exception e) {
				s.setCilindro(null);
			}
			try {
				s.setReductor(metReductor(rs.getInt("idreduc")));
			} catch (Exception e) {
				s.setReductor(null);
			}
			s.setFactorCobro(rs.getString("factorcobro"));

			try {
				s.setTipoMotorVehiculo(metTipoMotorVehiculo(rs.getInt("idtipomotorveh")));
			} catch (Exception e) {
				s.setTipoMotorVehiculo(null);
			}
			try {
				s.setCombustible(metCombustible(rs.getInt("idcomb")));
			} catch (Exception e) {
				// TODO: handle exception
				s.setCombustible(null);
			}
			try {
				s.setPersona(metUsuario(rs.getString("login")));
			} catch (Exception e) {	
				s.setPersona(null);
			}
			
			s.setNumPistones(rs.getInt("numpistones"));
			s.setPrecioDolares(rs.getString("preciodolares"));
			s.setPrecioBolivianos(rs.getString("preciobolivianos"));
			s.setPrecioTotal(rs.getString("preciototal"));
			s.setFecha(rs.getString("fecha"));
			s.setEstado(rs.getInt("estado"));
			
			s.setTot(rs.getInt("tot"));
			return s;
	    }
	}
	
	public class objMarcaReductor implements RowMapper<MarcaReductor>{
		@Override
		public MarcaReductor mapRow(ResultSet rs, int arg1) throws SQLException {
			MarcaReductor mr= new MarcaReductor();
			mr.setIdmarcred(rs.getInt("idmarcred"));
			mr.setNombre(rs.getString("nombre"));
			mr.setEstado(rs.getInt("estado"));
			return mr;
	    }
	}
	public class objMarcaCilindro implements RowMapper<MarcaCilindro>{
		@Override
		public MarcaCilindro mapRow(ResultSet rs, int arg1) throws SQLException {
			MarcaCilindro mc= new MarcaCilindro();
			mc.setIdmarccil(rs.getInt("idmarccil"));
			mc.setNombre(rs.getString("nombre"));
			mc.setEstado(rs.getInt("estado"));
			return mc;
	    }
	}
	
	public class objMarcaValvulaCilindro implements RowMapper<MarcaValvulaCilindro>{
		@Override
		public MarcaValvulaCilindro mapRow(ResultSet rs, int arg1) throws SQLException {
			MarcaValvulaCilindro mc= new MarcaValvulaCilindro();
			mc.setIdmarcvalcil(rs.getInt("idmarcvalcil"));
			mc.setNombre(rs.getString("nombre"));
			mc.setEstado(rs.getInt("estado"));
			return mc;
	    }
	}
	public class objCilindro implements RowMapper<Cilindro>{
		@Override
		public Cilindro mapRow(ResultSet rs, int arg1) throws SQLException {
			Cilindro c= new Cilindro();
			c.setIdcil(rs.getInt("idcil"));
			c.setCapacidad(rs.getString("capacidad"));
			c.setSerie(rs.getString("serie"));
			c.setIdmarccil(rs.getInt("idmarccil"));
			c.setEstado(rs.getInt("estado"));
			try {
				c.setMarcaCilindro(metMarcaCilindro(rs.getInt("idmarccil")));
			} catch (Exception e) {
				c.setMarcaCilindro(null);
			}
			return c;
	    }
	}
	public class objReductor implements RowMapper<Reductor>{
		@Override
		public Reductor mapRow(ResultSet rs, int arg1) throws SQLException {
			Reductor r= new Reductor();
			r.setIdreduc(rs.getInt("idreduc"));
			r.setSerie(rs.getString("serie"));
			r.setTipoTecnologia(rs.getString("tipotecnologia"));
			r.setIdmarcred(rs.getInt("idmarcred"));
			r.setEstado(rs.getInt("estado"));
			try {
				r.setMarcaReductor(metMarcaReductor(rs.getInt("idmarcred")));
			} catch (Exception e) {
				r.setMarcaReductor(null);
			}
			return r;
	    }
	}
	public class objTelefono implements RowMapper<Telefono>{
		@Override
		public Telefono mapRow(ResultSet rs, int arg1) throws SQLException {
			Telefono t= new Telefono();
//			t.setIdper(rs.getInt("idper"));
			t.setNumero(rs.getString("numero"));	
			return t;
	    }
	}

	public class objTipoMotorVehiculo implements RowMapper<TipoMotorVehiculo>{
		@Override
		public TipoMotorVehiculo mapRow(ResultSet rs, int arg1) throws SQLException {
			TipoMotorVehiculo t= new TipoMotorVehiculo();
			t.setIdtipoMotorVeh(rs.getInt("idtipomotorveh"));
			t.setNombre(rs.getString("nombre"));;
			t.setEstado(rs.getInt("estado"));
			return t;
	    }
	}
	public class objCombustible implements RowMapper<Combustible>{
		@Override
		public Combustible mapRow(ResultSet rs, int arg1) throws SQLException {
			Combustible c= new Combustible();
			c.setIdcomb(rs.getInt("idcomb"));
			c.setNombre(rs.getString("nombre"));;
			c.setEstado(rs.getInt("estado"));
			return c;
	    }
	}
	public class objUsuario implements RowMapper<Usuario>{
		@Override
		public Usuario mapRow(ResultSet rs, int arg1) throws SQLException {
			Usuario u= new Usuario();
			u.setLogin(rs.getString("login"));
			u.setPassword(rs.getString("password"));
			u.setEstado(rs.getInt("estado"));
			u.setIdper(rs.getInt("idper"));
			return u;
	    }
	}
	public class objPersona implements RowMapper<Persona>{
		@Override
		public Persona mapRow(ResultSet rs, int arg1) throws SQLException {
			Persona p= new Persona();
			p.setIdper(rs.getInt("idper"));
			p.setCi(rs.getString("ci"));
			p.setCiCod(rs.getString("ciCod"));
			p.setNombres(rs.getString("nombres"));
			p.setAp(rs.getString("ap"));
			p.setAm(rs.getString("am"));
			p.setGenero(rs.getString("genero"));
			p.setDireccion(rs.getString("direccion"));
			p.setEmail(rs.getString("email"));
			p.setFoto(rs.getString("foto"));
			p.setEstado(rs.getInt("estado"));
			try {
				p.setUsuario(obtenerUsuario(rs.getInt("idper")));
			} catch (Exception e) {
				p.setUsuario(null);
			}
			try {
				p.setListaTelf(metListaTelf(rs.getInt("idper")));
			} catch (Exception e) {
				// TODO: handle exception
				p.setListaTelf(null);
			}
			return p;
	    }
	}

	@Transactional
	public Persona metPersona(int idper){
		try {
			String sql="SELECT DISTINCT p.* FROM persona p JOIN taller t ON t.idper=p.idper and p.idper=?";
			return this.db.queryForObject(sql,new objPersona(),idper);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
			// TODO: handle exception
			return null;
		}
		
	}
	@Transactional
	public Taller metTaller(int idtall){
		try {
			return this.db.queryForObject("select * from taller where idtall=?", new objTaller(),idtall);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
			return null;
		}
		
	}
	@Transactional
	public Taller metTallerConversion(int idtall){
		try {
			return this.db.queryForObject("select * from taller where idtall=?", new objTaller(),idtall);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
			return null;
		}
		
	}
	@Transactional
	public TallerRecalificacion metTallerRecalificacion(int idtallrecal){
		try {
//			System.out.println("idtallrecal-met:"+idtallrecal);
			return this.db.queryForObject("select * from taller where idtall=?", new objTallerRecalificacion(),idtallrecal);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			System.out.println(e.getMessage());
			return null;
		}

	}
	@Transactional
	public TallerRecalificacion getTallerRecalificacion(int idtallrecal){
		try {
			System.out.println("idtallrecal-met:"+idtallrecal);
			return this.db.queryForObject("select * from taller where idtall=?", new objTallerRecalificacion(),idtallrecal);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			System.out.println(e.getMessage());
			return null;
		}

	}
	@Transactional
	public int getGestionActual(){
		try {
			String sql="SELECT gt.gestion FROM gestion gt WHERE gt.actual='1'";
			return this.db.queryForObject(sql,Integer.class);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			System.out.println(e.getMessage());
			return 0;
		}
		
	}
	@Transactional
	public Servicio metServicio(int idserv){
		return this.db.queryForObject("select * from servicio where idserv=?", new objServicios(),idserv);
	}
	@Transactional
	public Solicitud metSolicitud(int idsolt){
		return manejadorSolicitudes.metSolicitud(idsolt);
	}
	@Transactional
	public SolicitudReposicionRecalificacion metSolicitudRecalificacion(int idrecal){
		return manejadorSolicitudes.metSolicitudRecalificacion(idrecal);
	}
	
	@Transactional
	public ChipRom metChip(int idRom){
		return this.db.queryForObject("select * from chipRom where idRom=?", new objChipRom(),idRom);
	}
	
	@Transactional
	public Usuario obtenerUsuario(int codper){
		return this.db.queryForObject("select * from usuario where idper=?", new objUsuario(),codper);
	}
	@Transactional
	public List<Telefono> metListaTelf(int idper){
		return this.db.query("select * from telefono where idper=?", new objTelefono(),idper);
	}
	@Transactional
	public MarcaReductor metMarcaReductor(int idmarcred){
		return this.db.queryForObject("select * from marcaReductor where idmarcred=?", new objMarcaReductor(),idmarcred);
	}
	@Transactional
	public MarcaCilindro metMarcaCilindro(int idmarccil){
		return this.db.queryForObject("select * from marcaCilindro where idmarccil=?", new objMarcaCilindro(),idmarccil);
	}
	@Transactional
	public Cilindro metCilindro(int idcil){
		return this.db.queryForObject("select * from cilindro where idcil=?", new objCilindro(),idcil);
	}
	@Transactional
	public Reductor metReductor(int idreduc){
		return this.db.queryForObject("select * from reductor where idreduc=?", new objReductor(),idreduc);
	}
	@Transactional
	public TipoMotorVehiculo metTipoMotorVehiculo(int idTipoM){
		return this.db.queryForObject("select * from tipoMotorVehiculo where idtipomotorveh=?", new objTipoMotorVehiculo(),idTipoM);
	}
	@Transactional
	public Combustible metCombustible(int idTipoM){
		return this.db.queryForObject("select * from combustible where idcomb=?", new objCombustible(),idTipoM);
	}
	@Transactional
	public Persona metUsuario(String login){
		return this.db.queryForObject("select p.* from persona p,usuario u where u.idper=p.idper AND u.login=?", new objPersona(),login);
	}
	@Transactional
	public List<Servicio> ListarServicios(HttpServletRequest req){
		String filtro=req.getParameter("filtro");
		int estado=Integer.parseInt(req.getParameter("estado"));
		String sql="SELECT s.* FROM servicio s JOIN cilindro c ON c.idcil=s.idcil JOIN reductor r ON  r.idreduc=s.idreduc JOIN tipoMotorVehiculo tm ON tm.idtipomotorveh=s.idtipomotorveh JOIN usuario u ON u.login=s.login where  (r.tipotecnologia LIKE ? OR c.capacidad LIKE ?) and (s.estado=? or ?=-1) ORDER BY s.idserv ASC";
		return this.db.query(sql, new objServicios(),"%"+filtro+"%","%"+filtro+"%",estado,estado);
	}
	@Transactional
	public List<Servicio> listar_d1_servicio(int start,int estado,String search,int length,int anio){
		String sql="";
		try {

			sql="SELECT count(*) \r\n"
					+ "FROM servicio s \r\n"
					+ "JOIN cilindro c ON c.idcil=s.idcil \r\n"
					+ "JOIN reductor r ON  r.idreduc=s.idreduc \r\n"
					+ "JOIN tipoMotorVehiculo tm ON tm.idtipomotorveh=s.idtipomotorveh \r\n"
					+ "JOIN usuario u ON u.login=s.login \r\n"
					+ "WHERE  (r.tipotecnologia LIKE concat('%',?,'%') OR c.capacidad LIKE concat('%',?,'%')) and (s.estado=? or ?=-1) and anio=? ";
			Integer tot=db.queryForObject(sql, Integer.class,search,search,estado,estado,anio);
			
			sql="SELECT s.*,row_number() OVER(ORDER BY s.idserv) as RN,? as tot \r\n"
					+ "FROM servicio s \r\n"
					+ "JOIN cilindro c ON c.idcil=s.idcil \r\n"
					+ "JOIN reductor r ON  r.idreduc=s.idreduc \r\n"
					+ "JOIN tipoMotorVehiculo tm ON tm.idtipomotorveh=s.idtipomotorveh \r\n"
					+ "JOIN usuario u ON u.login=s.login \r\n"
					+ "WHERE  (r.tipotecnologia LIKE concat('%',?,'%') OR c.capacidad LIKE concat('%',?,'%')) and (s.estado=? or ?=-1) and anio=? \r\n"
					+ "ORDER BY s.idserv ASC \r\n"
					+ "LIMIT ? OFFSET ?";
			return db.query(sql,new objServicios_d(),tot,search,search,estado,estado,anio,length,start);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			System.out.println(e.getMessage());
			return null;	
		}  

	}
	@Transactional
	public int getGestionAcutal(){
		String sql="SELECT DISTINCT gestion FROM gestion WHERE actual='1'";
		return this.db.queryForObject(sql,Integer.class);
	}
	@Transactional
	public List<Map<String,Object>> listarContratosPorUsuario(String usuario){
		
		int gestionActual=this.getGestionAcutal();
		
		try {
			String sql="SELECT x.* FROM (\r\n"
					+ "((SELECT \r\n"
					+ "ors.numords as Nº,\r\n"
					+ "'' as prov,\r\n"
					+ "\r\n"
					+ "--************************************************************************************\r\n"
					+ "per.razonsocial as BENEFICIARIO,\r\n"
					+ "per.ci as NUM_CEDULA,\r\n"
					+ "per.cicod as EMITIDO_EN,\r\n"
					+ "per.estadocivil as ESTADO_CIVIL,\r\n"
					+ "per.profesion as PROFESION,\r\n"
					+ "--(SELECT telf.numero FROM telefono telf WHERE telf.idper=per.idper AND telf.numero LIKE '4%') as TELEFONO_FIJO,\r\n"
					+ "--(SELECT telf.numero FROM telefono telf WHERE telf.idper=per.idper AND (telf.numero LIKE '6%' OR telf.numero LIKE '7%') ) as TELEFONO_CEL,\r\n"
					+ "split_part(string_agg((SELECT telf.numero FROM telefono telf WHERE telf.idper=per.idper AND (telf.numero LIKE '4%')),' '),' ',1) as TELEFONO_FIJO,\r\n"
					+ "CONCAT (split_part(string_agg((SELECT telf.numero FROM telefono telf WHERE telf.idper=per.idper AND (telf.numero LIKE '6%')),' '),' ',1) ,\r\n"
					+ "split_part(string_agg((SELECT telf.numero FROM telefono telf WHERE telf.idper=per.idper AND (telf.numero LIKE '7%')),' '),' ',1)) AS TELEFONO_CELULAR,\r\n"
					+ "--string_agg(tf.numero,' ') as NUMERO_TELF,\r\n"
					+ "--(SELECT telf.numero FROM telefono telf WHERE telf.idper=per.idper) as NUMERO_T,\r\n"
					+ "--tf.numero as NUMERO_TELF,\r\n"
					+ "per.naturalde as NATURAL_DE,\r\n"
					+ "'' as DPTO,\r\n"
					+ "'' as PROVINCIA,\r\n"
					+ "'' as LOCALIDAD,\r\n"
					+ "per.direccion  as BARRIO_ZONA,\r\n"
					+ "'' as DETALLE,\r\n"
					+ "'' as Nº_CASA,\r\n"
					+ "\r\n"
					+ "--************************************************************************************\r\n"
					+ "\r\n"
					+ "veh.placa,\r\n"
					+ "veh.color,\r\n"
					+ "marc.nombre as MARCA,\r\n"
					+ "veh.tipovehiculo as SUBMARCA,\r\n"
					+ "mv.nombre as MODELO,\r\n"
					+ "tmv.nombre as SISTEMA_MOTOR,\r\n"
					+ "veh.cilindrada,\r\n"
					+ "veh.num_motor AS Nº_MOTOR,\r\n"
					+ "veh.num_chasis as Nº_CHASIS,\r\n"
					+ "'TARIJA' as RADICATORIA,\r\n"
					+ "veh.tjeta_prioridad as CARNET_PROPIEDAD,\r\n"
					+ "tpv.nombre as CLASE_VEHICULO,\r\n"
					+ "cb.nombre as TIPO_COMBUSTIBLE,\r\n"
					+ "serv.numpistones as Nº_PISTONES,\r\n"
					+ "--veh.kitglp\r\n"
					+ "CASE WHEN veh.kitglp=0 THEN 'NO' ELSE 'SI' END AS KITGLP,\r\n"
					+ "tpsv.nombre as TIPO_SERVICIO,\r\n"
					+ "bf.institucion AS INSTITUCION_PERTENECE,\r\n"
					+ "\r\n"
					+ "\r\n"
					+ "--************************************************************************************\r\n"
					+ "'' AS ITEM,\r\n"
					+ "red.tipotecnologia AS TIPO_CONVERSION,\r\n"
					+ "cil.capacidad AS CAPACIDAD_M3,\r\n"
					+ "tmvs.nombre AS SISTEMA_MOTOR,\r\n"
					+ "serv.preciodolares,\r\n"
					+ "f_convnl(serv.preciodolares::NUMERIC) as COSTO_DOLARES_LITERAL,\r\n"
					+ "serv.preciobolivianos,\r\n"
					+ "'' as CONSUMO_MENSUAL,\r\n"
					+ "tall.nombretall AS TALLER_DESIGNADO,\r\n"
					+ "tall.direccion as DIRECCION_TALLER,\r\n"
					+ "--telft.numero AS TELEFONO_TALLER,\r\n"
					+ "(SELECT telf.numero FROM telefono telf WHERE telf.idper=pert.idper) as TELEFONO_TALLER,\r\n"
					+ "pert.razonsocial AS PROPIETARIO_TALLER,\r\n"
					+ "'' as Nº_ORDEN_SERVICIO,\r\n"
					+ "'' as MARCA_KIT,\r\n"
					+ "'' as SERIE_KIT,\r\n"
					+ "'' as MARCA_CILINDRO,\r\n"
					+ "'' as SERIE_CILINDRO,\r\n"
					+ "'' as FECHA_FABRICACION_Nº_CILINDRO,\r\n"
					+ "'' as Nº_INMETRO,\r\n"
					+ "'' as CODIGO_BSISA,\r\n"
					+ "'' as FECHA_INSTALACION_PAQUETE_GNV,\r\n"
					+ "'' as Nº_ACTA_RECEPCION,\r\n"
					+ "'' as FECHA_ACTA_RECEPCION\r\n"
					+ "\r\n"
					+ "FROM persona per \r\n"
					+ "JOIN beneficiario bf ON bf.idper=per.idper\r\n"
					+ "JOIN benvehsolt bvs ON bvs.idben= bf.idben AND bvs.pertenecesino=1\r\n"
					+ "JOIN solicitud solt ON solt.idsolt=bvs.idsolt AND solt.anio=? AND solt.aprobadosino=1 AND solt.estado=1\r\n"
					+ "JOIN vehiculo veh ON bvs.placa=veh.placa\r\n"
					+ "JOIN marcavehiculo marc ON marc.idmarcv=veh.idmarcv\r\n"
					+ "JOIN modelovehiculo mv ON mv.idmodv=veh.idmodv\r\n"
					+ "JOIN tipomotorvehiculo tmv ON tmv.idtipomotorveh=veh.idtipomotorveh\r\n"
					+ "JOIN tipovehiculo tpv ON tpv.idtipv=veh.idtipv\r\n"
					+ "JOIN combveh cbh ON cbh.placa=veh.placa\r\n"
					+ "JOIN combustible cb ON cbh.idcomb =cb.idcomb\r\n"
					+ "\r\n"
					+ "JOIN ordenservicio ors ON ors.idsolt=solt.idsolt\r\n"
					+ "JOIN servicio serv ON serv.idserv=ors.idserv\r\n"
					+ "JOIN tiposerviciovehiculo tpsv ON tpsv.idtipsv=veh.idtipsv \r\n"
					+ "\r\n"
					+ "JOIN reductor red ON red.idreduc=serv.idreduc\r\n"
					+ "JOIN cilindro cil ON  cil.idcil=serv.idcil\r\n"
					+ "JOIN tipomotorvehiculo tmvs ON tmvs.idtipomotorveh=serv.idtipomotorveh\r\n"
					+ "JOIN taller tall ON tall.idtall=ors.idtall\r\n"
					+ "JOIN persona pert ON pert.idper=tall.idper\r\n"
					+ "JOIN usuariotaller ut ON ut.idtall=tall.idtall\r\n"
					+ "JOIN usuario us ON us.login=ut.login and us.estado=1\r\n"
					+ "\r\n"
					+ "WHERE  us.login=? AND per.idper  IN (SELECT tl.idper FROM telefono tl WHERE tl.idper =per.idper )\r\n"
					+ "		--AND pert.idper IN (SELECT tlt.idper FROM telefono tlt WHERE tlt.idper =pert.idper )\r\n"
					+ "GROUP BY \r\n"
					+ "per.idper,\r\n"
					+ "per.razonsocial,\r\n"
					+ "per.ci,\r\n"
					+ "per.cicod,\r\n"
					+ "per.direccion,\r\n"
					+ "veh.placa,\r\n"
					+ "veh.color,\r\n"
					+ "marc.nombre,\r\n"
					+ "veh.tipovehiculo,\r\n"
					+ "mv.nombre,\r\n"
					+ "tmv.nombre,\r\n"
					+ "veh.cilindrada,\r\n"
					+ "veh.num_motor,\r\n"
					+ "veh.num_chasis,\r\n"
					+ "veh.tjeta_prioridad,\r\n"
					+ "tpv.nombre,\r\n"
					+ "cb.nombre,\r\n"
					+ "serv.numpistones,\r\n"
					+ "solt.numsolt,\r\n"
					+ "tpsv.nombre,\r\n"
					+ "bf.institucion,\r\n"
					+ "red.tipotecnologia,\r\n"
					+ "cil.capacidad,\r\n"
					+ "tmvs.nombre,\r\n"
					+ "serv.preciodolares,\r\n"
					+ "serv.preciobolivianos,\r\n"
					+ "tall.nombretall,\r\n"
					+ "tall.direccion,\r\n"
					+ "pert.idper,\r\n"
					+ "pert.razonsocial, \r\n"
					+ "\r\n"
					+ "solt.numsolt, \r\n"
					+ "ors.numords \r\n"
					+ ")\r\n"
					+ "\r\n"
					+ "UNION ALL\r\n"
					+ "\r\n"
					+ "(SELECT \r\n"
					+ "ors.numords as Nº,\r\n"
					+ "'' as prov,\r\n"
					+ "\r\n"
					+ "--************************************************************************************\r\n"
					+ "per.razonsocial as BENEFICIARIO,\r\n"
					+ "per.ci as NUM_CEDULA,\r\n"
					+ "per.cicod as EMITIDO_EN,\r\n"
					+ "per.estadocivil as ESTADO_CIVIL,\r\n"
					+ "per.profesion as PROFESION,\r\n"
					+ "--(SELECT telf.numero FROM telefono telf WHERE telf.idper=per.idper AND telf.numero LIKE '4%') as TELEFONO_FIJO,\r\n"
					+ "--(SELECT telf.numero FROM telefono telf WHERE telf.idper=per.idper AND (telf.numero LIKE '6%' OR telf.numero LIKE '7%') ) as TELEFONO_CEL,\r\n"
					+ "split_part(string_agg((SELECT telf.numero FROM telefono telf WHERE telf.idper=per.idper AND (telf.numero LIKE '4%')),' '),' ',1) as TELEFONO_FIJO,\r\n"
					+ "CONCAT (split_part(string_agg((SELECT telf.numero FROM telefono telf WHERE telf.idper=per.idper AND (telf.numero LIKE '6%')),' '),' ',1) ,\r\n"
					+ "split_part(string_agg((SELECT telf.numero FROM telefono telf WHERE telf.idper=per.idper AND (telf.numero LIKE '7%')),' '),' ',1)) AS TELEFONO_CELULAR,\r\n"
					+ "--string_agg(tf.numero,' ') as NUMERO_TELF,\r\n"
					+ "--(SELECT telf.numero FROM telefono telf WHERE telf.idper=per.idper) as NUMERO_T,\r\n"
					+ "--tf.numero as NUMERO_TELF,\r\n"
					+ "per.naturalde as NATURAL_DE,\r\n"
					+ "'' as DPTO,\r\n"
					+ "'' as PROVINCIA,\r\n"
					+ "'' as LOCALIDAD,\r\n"
					+ "per.direccion  as BARRIO_ZONA,\r\n"
					+ "'' as DETALLE,\r\n"
					+ "'' as Nº_CASA,\r\n"
					+ "\r\n"
					+ "--************************************************************************************\r\n"
					+ "\r\n"
					+ "veh.placa,\r\n"
					+ "veh.color,\r\n"
					+ "marc.nombre as MARCA,\r\n"
					+ "veh.tipovehiculo as SUBMARCA,\r\n"
					+ "mv.nombre as MODELO,\r\n"
					+ "tmv.nombre as SISTEMA_MOTOR,\r\n"
					+ "veh.cilindrada,\r\n"
					+ "veh.num_motor AS Nº_MOTOR,\r\n"
					+ "veh.num_chasis as Nº_CHASIS,\r\n"
					+ "'TARIJA' as RADICATORIA,\r\n"
					+ "veh.tjeta_prioridad as CARNET_PROPIEDAD,\r\n"
					+ "tpv.nombre as CLASE_VEHICULO,\r\n"
					+ "cb.nombre as TIPO_COMBUSTIBLE,\r\n"
					+ "serv.numpistones as Nº_PISTONES,\r\n"
					+ "--veh.kitglp\r\n"
					+ "CASE WHEN veh.kitglp=0 THEN 'NO' ELSE 'SI' END AS KITGLP,\r\n"
					+ "tpsv.nombre as TIPO_SERVICIO,\r\n"
					+ "bf.institucion AS INSTITUCION_PERTENECE,\r\n"
					+ "\r\n"
					+ "\r\n"
					+ "--************************************************************************************\r\n"
					+ "'' AS ITEM,\r\n"
					+ "red.tipotecnologia AS TIPO_CONVERSION,\r\n"
					+ "cil.capacidad AS CAPACIDAD_M3,\r\n"
					+ "tmvs.nombre AS SISTEMA_MOTOR,\r\n"
					+ "serv.preciodolares,\r\n"
					+ "f_convnl(serv.preciodolares::NUMERIC) as COSTO_DOLARES_LITERAL,\r\n"
					+ "serv.preciobolivianos,\r\n"
					+ "'' as CONSUMO_MENSUAL,\r\n"
					+ "tall.nombretall AS TALLER_DESIGNADO,\r\n"
					+ "tall.direccion as DIRECCION_TALLER,\r\n"
					+ "--telft.numero AS TELEFONO_TALLER,\r\n"
					+ "(SELECT telf.numero FROM telefono telf WHERE telf.idper=pert.idper) as TELEFONO_TALLER,\r\n"
					+ "pert.razonsocial AS PROPIETARIO_TALLER,\r\n"
					+ "\r\n"
					+ "'' as Nº_ORDEN_SERVICIO,\r\n"
					+ "'' as MARCA_KIT,\r\n"
					+ "'' as SERIE_KIT,\r\n"
					+ "'' as MARCA_CILINDRO,\r\n"
					+ "'' as SERIE_CILINDRO,\r\n"
					+ "'' as FECHA_FABRICACION_Nº_CILINDRO,\r\n"
					+ "'' as Nº_INMETRO,\r\n"
					+ "'' as CODIGO_BSISA,\r\n"
					+ "'' as FECHA_INSTALACION_PAQUETE_GNV,\r\n"
					+ "'' as Nº_ACTA_RECEPCION,\r\n"
					+ "'' as FECHA_ACTA_RECEPCION\r\n"
					+ "\r\n"
					+ "FROM persona per \r\n"
					+ "JOIN beneficiario bf ON bf.idper=per.idper\r\n"
					+ "JOIN benvehsolt bvs ON bvs.idben= bf.idben AND bvs.pertenecesino=1\r\n"
					+ "JOIN solicitud solt ON solt.idsolt=bvs.idsolt AND solt.anio=? AND solt.aprobadosino=1 AND solt.estado=1\r\n"
					+ "JOIN vehiculo veh ON bvs.placa=veh.placa\r\n"
					+ "JOIN marcavehiculo marc ON marc.idmarcv=veh.idmarcv\r\n"
					+ "JOIN modelovehiculo mv ON mv.idmodv=veh.idmodv\r\n"
					+ "JOIN tipomotorvehiculo tmv ON tmv.idtipomotorveh=veh.idtipomotorveh\r\n"
					+ "JOIN tipovehiculo tpv ON tpv.idtipv=veh.idtipv\r\n"
					+ "JOIN combveh cbh ON cbh.placa=veh.placa\r\n"
					+ "JOIN combustible cb ON cbh.idcomb =cb.idcomb\r\n"
					+ "\r\n"
					+ "JOIN ordenservicio ors ON ors.idsolt=solt.idsolt\r\n"
					+ "JOIN servicio serv ON serv.idserv=ors.idserv\r\n"
					+ "JOIN tiposerviciovehiculo tpsv ON tpsv.idtipsv=veh.idtipsv \r\n"
					+ "\r\n"
					+ "JOIN reductor red ON red.idreduc=serv.idreduc\r\n"
					+ "JOIN cilindro cil ON  cil.idcil=serv.idcil\r\n"
					+ "JOIN tipomotorvehiculo tmvs ON tmvs.idtipomotorveh=serv.idtipomotorveh\r\n"
					+ "JOIN taller tall ON tall.idtall=ors.idtall\r\n"
					+ "JOIN persona pert ON pert.idper=tall.idper\r\n"
					+ "JOIN usuariotaller ut ON ut.idtall=tall.idtall\r\n"
					+ "JOIN usuario us ON us.login=ut.login and us.estado=1\r\n"
					+ "\r\n"
					+ "WHERE us.login=? AND per.idper NOT IN (SELECT tl.idper FROM telefono tl WHERE tl.idper =per.idper )\r\n"
					+ "			--AND pert.idper NOT IN (SELECT tlt.idper FROM telefono tlt WHERE tlt.idper =pert.idper )\r\n"
					+ "GROUP BY \r\n"
					+ "per.idper,\r\n"
					+ "per.razonsocial,\r\n"
					+ "per.ci,\r\n"
					+ "per.cicod,\r\n"
					+ "per.direccion,\r\n"
					+ "veh.placa,\r\n"
					+ "veh.color,\r\n"
					+ "marc.nombre,\r\n"
					+ "veh.tipovehiculo,\r\n"
					+ "mv.nombre,\r\n"
					+ "tmv.nombre,\r\n"
					+ "veh.cilindrada,\r\n"
					+ "veh.num_motor,\r\n"
					+ "veh.num_chasis,\r\n"
					+ "veh.tjeta_prioridad,\r\n"
					+ "tpv.nombre,\r\n"
					+ "cb.nombre,\r\n"
					+ "serv.numpistones,\r\n"
					+ "solt.numsolt,\r\n"
					+ "tpsv.nombre,\r\n"
					+ "bf.institucion,\r\n"
					+ "red.tipotecnologia,\r\n"
					+ "cil.capacidad,\r\n"
					+ "tmvs.nombre,\r\n"
					+ "serv.preciodolares,\r\n"
					+ "serv.preciobolivianos,\r\n"
					+ "tall.nombretall,\r\n"
					+ "tall.direccion,\r\n"
					+ "pert.idper,\r\n"
					+ "pert.razonsocial, \r\n"
					+ "\r\n"
					+ "solt.numsolt, \r\n"
					+ "ors.numords \r\n"
					+ ")\r\n"
					+ "\r\n"
					+ ")\r\n"
					+ "\r\n"
					+ "\r\n"
					+ ") AS x \r\n"
					+ "ORDER BY 1 ASC";
			return this.db.queryForList(sql,new Object[] {gestionActual,usuario,gestionActual,usuario});
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
			return null;	
		}
		
	}
	@Transactional
	public List<Map<String,Object>> listarContratosPorUsuarioRecal(String usuario){
		
		int gestionActual=this.getGestionAcutal();
		
		try {
			String sql="SELECT x.* FROM (\r\n"
					+ "((SELECT \r\n"
					+ "ors.numords as Nº,\r\n"
					+ "'' as prov,\r\n"
					+ "\r\n"
					+ "--************************************************************************************\r\n"
					+ "per.razonsocial as BENEFICIARIO,\r\n"
					+ "per.ci as NUM_CEDULA,\r\n"
					+ "per.cicod as EMITIDO_EN,\r\n"
					+ "per.estadocivil as ESTADO_CIVIL,\r\n"
					+ "per.profesion as PROFESION,\r\n"
					+ "--(SELECT telf.numero FROM telefono telf WHERE telf.idper=per.idper AND telf.numero LIKE '4%') as TELEFONO_FIJO,\r\n"
					+ "--(SELECT telf.numero FROM telefono telf WHERE telf.idper=per.idper AND (telf.numero LIKE '6%' OR telf.numero LIKE '7%') ) as TELEFONO_CEL,\r\n"
					+ "split_part(string_agg((SELECT telf.numero FROM telefono telf WHERE telf.idper=per.idper AND (telf.numero LIKE '4%')),' '),' ',1) as TELEFONO_FIJO,\r\n"
					+ "CONCAT (split_part(string_agg((SELECT telf.numero FROM telefono telf WHERE telf.idper=per.idper AND (telf.numero LIKE '6%')),' '),' ',1) ,\r\n"
					+ "split_part(string_agg((SELECT telf.numero FROM telefono telf WHERE telf.idper=per.idper AND (telf.numero LIKE '7%')),' '),' ',1)) AS TELEFONO_CELULAR,\r\n"
					+ "--string_agg(tf.numero,' ') as NUMERO_TELF,\r\n"
					+ "--(SELECT telf.numero FROM telefono telf WHERE telf.idper=per.idper) as NUMERO_T,\r\n"
					+ "--tf.numero as NUMERO_TELF,\r\n"
					+ "per.naturalde as NATURAL_DE,\r\n"
					+ "'' as DPTO,\r\n"
					+ "'' as PROVINCIA,\r\n"
					+ "'' as LOCALIDAD,\r\n"
					+ "per.direccion  as BARRIO_ZONA,\r\n"
					+ "'' as DETALLE,\r\n"
					+ "'' as Nº_CASA,\r\n"
					+ "\r\n"
					+ "--************************************************************************************\r\n"
					+ "\r\n"
					+ "veh.placa,\r\n"
					+ "veh.color,\r\n"
					+ "marc.nombre as MARCA,\r\n"
					+ "veh.tipovehiculo as SUBMARCA,\r\n"
					+ "mv.nombre as MODELO,\r\n"
					+ "tmv.nombre as SISTEMA_MOTOR,\r\n"
					+ "veh.cilindrada,\r\n"
					+ "veh.num_motor AS Nº_MOTOR,\r\n"
					+ "veh.num_chasis as Nº_CHASIS,\r\n"
					+ "'TARIJA' as RADICATORIA,\r\n"
					+ "veh.tjeta_prioridad as CARNET_PROPIEDAD,\r\n"
					+ "tpv.nombre as CLASE_VEHICULO,\r\n"
					+ "cb.nombre as TIPO_COMBUSTIBLE,\r\n"
					+ "'' as Nº_PISTONES,\r\n"
					+ "--veh.kitglp\r\n"
					+ "CASE WHEN veh.kitglp=0 THEN 'NO' ELSE 'SI' END AS KITGLP,\r\n"
					+ "tpsv.nombre as TIPO_SERVICIO,\r\n"
					+ "bf.institucion AS INSTITUCION_PERTENECE,\r\n"
					+ "\r\n"
					+ "\r\n"
					+ "--************************************************************************************\r\n"
					+ "'' AS ITEM,\r\n"
					+ "'' AS TIPO_CONVERSION,\r\n"
					+ "'' AS CAPACIDAD_M3,\r\n"
					+ "'' AS SISTEMA_MOTOR,\r\n"
					+ "'' AS preciodolares,\r\n"
					+ "'' AS COSTO_DOLARES_LITERAL,\r\n"
					+ "'' AS preciobolivianos,\r\n"
					+ "'' as CONSUMO_MENSUAL,\r\n"
					+ "tall.nombretall AS TALLER_DESIGNADO,\r\n"
					+ "tall.direccion as DIRECCION_TALLER,\r\n"
					+ "--telft.numero AS TELEFONO_TALLER,\r\n"
					+ "(SELECT telf.numero FROM telefono telf WHERE telf.idper=pert.idper) as TELEFONO_TALLER,\r\n"
					+ "pert.razonsocial AS PROPIETARIO_TALLER,\r\n"
					+ "'' as Nº_ORDEN_SERVICIO,\r\n"
					+ "'' as MARCA_KIT,\r\n"
					+ "'' as SERIE_KIT,\r\n"
					+ "'' as MARCA_CILINDRO,\r\n"
					+ "'' as SERIE_CILINDRO,\r\n"
					+ "'' as FECHA_FABRICACION_Nº_CILINDRO,\r\n"
					+ "'' as Nº_INMETRO,\r\n"
					+ "'' as CODIGO_BSISA,\r\n"
					+ "'' as FECHA_INSTALACION_PAQUETE_GNV,\r\n"
					+ "'' as Nº_ACTA_RECEPCION,\r\n"
					+ "'' as FECHA_ACTA_RECEPCION\r\n"
					+ "\r\n"
					+ "FROM persona per \r\n"
					+ "JOIN beneficiario bf ON bf.idper=per.idper\r\n"
					+ "JOIN benvehsoltrec bvs ON bvs.idben= bf.idben AND bvs.pertenecesino=1\r\n"
					+ "JOIN solicitudreposicionrecalificacion solt ON solt.idrecal=bvs.idrecal AND solt.anio=? AND solt.estado=1\r\n"
					+ "JOIN vehiculo veh ON bvs.placa=veh.placa\r\n"
					+ "JOIN marcavehiculo marc ON marc.idmarcv=veh.idmarcv\r\n"
					+ "JOIN modelovehiculo mv ON mv.idmodv=veh.idmodv\r\n"
					+ "JOIN tipomotorvehiculo tmv ON tmv.idtipomotorveh=veh.idtipomotorveh\r\n"
					+ "JOIN tipovehiculo tpv ON tpv.idtipv=veh.idtipv\r\n"
					+ "JOIN combveh cbh ON cbh.placa=veh.placa\r\n"
					+ "JOIN combustible cb ON cbh.idcomb =cb.idcomb\r\n"
					+ "\r\n"
					+ "JOIN ordenserviciorecalificacion ors ON ors.idrecal=solt.idrecal\r\n"
					+ "JOIN tiposerviciovehiculo tpsv ON tpsv.idtipsv=veh.idtipsv \r\n"
					+ "JOIN taller tall ON tall.idtall=ors.idtallconver\r\n"
					+ "JOIN persona pert ON pert.idper=tall.idper\r\n"
					+ "JOIN usuariotaller ut ON ut.idtall=tall.idtall\r\n"
					+ "JOIN usuario us ON us.login=ut.login and us.estado=1\r\n"
					+ "\r\n"
					+ "WHERE  us.login=? AND per.idper  IN (SELECT tl.idper FROM telefono tl WHERE tl.idper =per.idper )\r\n"
					+ "		--AND pert.idper IN (SELECT tlt.idper FROM telefono tlt WHERE tlt.idper =pert.idper )\r\n"
					+ "GROUP BY \r\n"
					+ "per.idper,\r\n"
					+ "per.razonsocial,\r\n"
					+ "per.ci,\r\n"
					+ "per.cicod,\r\n"
					+ "per.direccion,\r\n"
					+ "veh.placa,\r\n"
					+ "veh.color,\r\n"
					+ "marc.nombre,\r\n"
					+ "veh.tipovehiculo,\r\n"
					+ "mv.nombre,\r\n"
					+ "tmv.nombre,\r\n"
					+ "veh.cilindrada,\r\n"
					+ "veh.num_motor,\r\n"
					+ "veh.num_chasis,\r\n"
					+ "veh.tjeta_prioridad,\r\n"
					+ "tpv.nombre,\r\n"
					+ "cb.nombre,\r\n"
					+ "solt.numsoltrec,\r\n"
					+ "tpsv.nombre,\r\n"
					+ "bf.institucion,\r\n"
					+ "tall.nombretall,\r\n"
					+ "tall.direccion,\r\n"
					+ "pert.idper,\r\n"
					+ "pert.razonsocial, \r\n"
					+ "\r\n"
					+ "solt.numsoltrec, \r\n"
					+ "ors.numords \r\n"
					+ ")\r\n"
					+ "\r\n"
					+ "UNION ALL\r\n"
					+ "\r\n"
					+ "(SELECT \r\n"
					+ "ors.numords as Nº,\r\n"
					+ "'' as prov,\r\n"
					+ "\r\n"
					+ "--************************************************************************************\r\n"
					+ "per.razonsocial as BENEFICIARIO,\r\n"
					+ "per.ci as NUM_CEDULA,\r\n"
					+ "per.cicod as EMITIDO_EN,\r\n"
					+ "per.estadocivil as ESTADO_CIVIL,\r\n"
					+ "per.profesion as PROFESION,\r\n"
					+ "--(SELECT telf.numero FROM telefono telf WHERE telf.idper=per.idper AND telf.numero LIKE '4%') as TELEFONO_FIJO,\r\n"
					+ "--(SELECT telf.numero FROM telefono telf WHERE telf.idper=per.idper AND (telf.numero LIKE '6%' OR telf.numero LIKE '7%') ) as TELEFONO_CEL,\r\n"
					+ "split_part(string_agg((SELECT telf.numero FROM telefono telf WHERE telf.idper=per.idper AND (telf.numero LIKE '4%')),' '),' ',1) as TELEFONO_FIJO,\r\n"
					+ "CONCAT (split_part(string_agg((SELECT telf.numero FROM telefono telf WHERE telf.idper=per.idper AND (telf.numero LIKE '6%')),' '),' ',1) ,\r\n"
					+ "split_part(string_agg((SELECT telf.numero FROM telefono telf WHERE telf.idper=per.idper AND (telf.numero LIKE '7%')),' '),' ',1)) AS TELEFONO_CELULAR,\r\n"
					+ "--string_agg(tf.numero,' ') as NUMERO_TELF,\r\n"
					+ "--(SELECT telf.numero FROM telefono telf WHERE telf.idper=per.idper) as NUMERO_T,\r\n"
					+ "--tf.numero as NUMERO_TELF,\r\n"
					+ "per.naturalde as NATURAL_DE,\r\n"
					+ "'' as DPTO,\r\n"
					+ "'' as PROVINCIA,\r\n"
					+ "'' as LOCALIDAD,\r\n"
					+ "per.direccion  as BARRIO_ZONA,\r\n"
					+ "'' as DETALLE,\r\n"
					+ "'' as Nº_CASA,\r\n"
					+ "\r\n"
					+ "--************************************************************************************\r\n"
					+ "\r\n"
					+ "veh.placa,\r\n"
					+ "veh.color,\r\n"
					+ "marc.nombre as MARCA,\r\n"
					+ "veh.tipovehiculo as SUBMARCA,\r\n"
					+ "mv.nombre as MODELO,\r\n"
					+ "tmv.nombre as SISTEMA_MOTOR,\r\n"
					+ "veh.cilindrada,\r\n"
					+ "veh.num_motor AS Nº_MOTOR,\r\n"
					+ "veh.num_chasis as Nº_CHASIS,\r\n"
					+ "'TARIJA' as RADICATORIA,\r\n"
					+ "veh.tjeta_prioridad as CARNET_PROPIEDAD,\r\n"
					+ "tpv.nombre as CLASE_VEHICULO,\r\n"
					+ "cb.nombre as TIPO_COMBUSTIBLE,\r\n"
					+ "'' AS Nº_PISTONES,\r\n"
					+ "--veh.kitglp\r\n"
					+ "CASE WHEN veh.kitglp=0 THEN 'NO' ELSE 'SI' END AS KITGLP,\r\n"
					+ "tpsv.nombre as TIPO_SERVICIO,\r\n"
					+ "bf.institucion AS INSTITUCION_PERTENECE,\r\n"
					+ "\r\n"
					+ "\r\n"
					+ "--************************************************************************************\r\n"
					+ "'' AS ITEM,\r\n"
					+ "'' AS TIPO_CONVERSION,\r\n"
					+ "'' AS CAPACIDAD_M3,\r\n"
					+ "'' AS SISTEMA_MOTOR,\r\n"
					+ "'' AS preciodolares,\r\n"
					+ "'' AS COSTO_DOLARES_LITERAL,\r\n"
					+ "'' AS preciobolivianos,\r\n"
					+ "'' as CONSUMO_MENSUAL,\r\n"
					+ "tall.nombretall AS TALLER_DESIGNADO,\r\n"
					+ "tall.direccion as DIRECCION_TALLER,\r\n"
					+ "--telft.numero AS TELEFONO_TALLER,\r\n"
					+ "(SELECT telf.numero FROM telefono telf WHERE telf.idper=pert.idper) as TELEFONO_TALLER,\r\n"
					+ "pert.razonsocial AS PROPIETARIO_TALLER,\r\n"
					+ "\r\n"
					+ "'' as Nº_ORDEN_SERVICIO,\r\n"
					+ "'' as MARCA_KIT,\r\n"
					+ "'' as SERIE_KIT,\r\n"
					+ "'' as MARCA_CILINDRO,\r\n"
					+ "'' as SERIE_CILINDRO,\r\n"
					+ "'' as FECHA_FABRICACION_Nº_CILINDRO,\r\n"
					+ "'' as Nº_INMETRO,\r\n"
					+ "'' as CODIGO_BSISA,\r\n"
					+ "'' as FECHA_INSTALACION_PAQUETE_GNV,\r\n"
					+ "'' as Nº_ACTA_RECEPCION,\r\n"
					+ "'' as FECHA_ACTA_RECEPCION\r\n"
					+ "\r\n"
					+ "FROM persona per \r\n"
					+ "JOIN beneficiario bf ON bf.idper=per.idper\r\n"
					+ "JOIN benvehsoltrec bvs ON bvs.idben= bf.idben AND bvs.pertenecesino=1\r\n"
					+ "JOIN solicitudreposicionrecalificacion solt ON solt.idrecal=bvs.idrecal AND solt.anio=?  AND solt.estado=1\r\n"
					+ "JOIN vehiculo veh ON bvs.placa=veh.placa\r\n"
					+ "JOIN marcavehiculo marc ON marc.idmarcv=veh.idmarcv\r\n"
					+ "JOIN modelovehiculo mv ON mv.idmodv=veh.idmodv\r\n"
					+ "JOIN tipomotorvehiculo tmv ON tmv.idtipomotorveh=veh.idtipomotorveh\r\n"
					+ "JOIN tipovehiculo tpv ON tpv.idtipv=veh.idtipv\r\n"
					+ "JOIN combveh cbh ON cbh.placa=veh.placa\r\n"
					+ "JOIN combustible cb ON cbh.idcomb =cb.idcomb\r\n"
					+ "\r\n"
					+ "JOIN ordenserviciorecalificacion ors ON ors.idrecal=solt.idrecal\r\n"
					+ "JOIN tiposerviciovehiculo tpsv ON tpsv.idtipsv=veh.idtipsv \r\n"
					+ "\r\n"
					+ "JOIN taller tall ON tall.idtall=ors.idtallconver\r\n"
					+ "JOIN persona pert ON pert.idper=tall.idper\r\n"
					+ "JOIN usuariotaller ut ON ut.idtall=tall.idtall\r\n"
					+ "JOIN usuario us ON us.login=ut.login and us.estado=1\r\n"
					+ "\r\n"
					+ "WHERE us.login=? AND per.idper NOT IN (SELECT tl.idper FROM telefono tl WHERE tl.idper =per.idper )\r\n"
					+ "			--AND pert.idper NOT IN (SELECT tlt.idper FROM telefono tlt WHERE tlt.idper =pert.idper )\r\n"
					+ "GROUP BY \r\n"
					+ "per.idper,\r\n"
					+ "per.razonsocial,\r\n"
					+ "per.ci,\r\n"
					+ "per.cicod,\r\n"
					+ "per.direccion,\r\n"
					+ "veh.placa,\r\n"
					+ "veh.color,\r\n"
					+ "marc.nombre,\r\n"
					+ "veh.tipovehiculo,\r\n"
					+ "mv.nombre,\r\n"
					+ "tmv.nombre,\r\n"
					+ "veh.cilindrada,\r\n"
					+ "veh.num_motor,\r\n"
					+ "veh.num_chasis,\r\n"
					+ "veh.tjeta_prioridad,\r\n"
					+ "tpv.nombre,\r\n"
					+ "cb.nombre,\r\n"
					+ "solt.numsoltrec,\r\n"
					+ "tpsv.nombre,\r\n"
					+ "bf.institucion,\r\n"
					+ "tall.nombretall,\r\n"
					+ "tall.direccion,\r\n"
					+ "pert.idper,\r\n"
					+ "pert.razonsocial, \r\n"
					+ "\r\n"
					+ "solt.numsoltrec, \r\n"
					+ "ors.numords \r\n"
					+ ")\r\n"
					+ "\r\n"
					+ ")\r\n"
					+ "\r\n"
					+ "\r\n"
					+ ") AS x \r\n"
					+ "ORDER BY 1 ASC";
			return this.db.queryForList(sql,new Object[] {gestionActual,usuario,gestionActual,usuario});
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
			return null;	
		}
		
	}
	
	@Transactional
	public List<OrdenServicio> ListarOrdenServicio(HttpServletRequest req){
		int gestion =Integer.parseInt(req.getParameter("gestion"));
		if (gestion!=0) {
			System.out.println("gestion:"+gestion);
		}
		String filtro=req.getParameter("filtro");
		int estado=Integer.parseInt(req.getParameter("estado"));
		String sql="SELECT os.* \r\n"
				+ "FROM ordenServicio os \r\n"
				+ "JOIN servicio serv ON serv.idserv=os.idserv \r\n"
				+ "JOIN taller tll ON  os.idtall=tll.idtall \r\n"
				+ "JOIN solicitud s ON s.idsolt=os.idsolt\r\n"
				+ "\r\n"
				+ "JOIN benVehSolt bvs ON bvs.idsolt=s.idsolt\r\n"
				+ "join vehiculo v on bvs.placa=v.placa\r\n"
				+ "JOIN beneficiario b ON b.idben=bvs.idben AND bvs.perteneceSiNo=1\r\n"
				+ "JOIN persona per ON per.idper=b.idper\r\n"
				+ "\r\n"
				+ "JOIN usuario u ON u.login=os.login \r\n"
				+ "where ((concat('',os.numords) LIKE concat('%',upper(?),'%')) \r\n"
				+ "	or (concat('',s.numSolt) LIKE concat('%',upper(?),'%')) \r\n"
				+ "	or upper(v.placa) like concat('%',upper(?),'%') \r\n"
				+ "	or upper(per.ci) like concat('%',upper(?),'%')\r\n"
				+ "	)  \r\n"
				+ "and (os.instaladoSiNo=? or ?=-1) and os.anio=? ORDER BY os.idordserv ASC";
		return this.db.query(sql, new objOrdenServicio(),filtro,filtro,filtro,filtro,estado,estado,gestion);
	}
	
	@Transactional
	public List<OrdenServicio> listar_orden_servicio_d(int start,int estado,String search,int length,int anio,int numord,int numsolt){
		String sql="";
		try {
			sql="select count(os.*)\r\n"
					+ "FROM ordenServicio os \r\n"
					+ "JOIN servicio serv ON serv.idserv=os.idserv \r\n"
					+ "JOIN taller tll ON  os.idtall=tll.idtall \r\n"
					+ "JOIN solicitud s ON s.idsolt=os.idsolt\r\n"
					+ "JOIN benVehSolt bvs ON bvs.idsolt=s.idsolt\r\n"
					+ "join vehiculo v on bvs.placa=v.placa\r\n"
					+ "JOIN beneficiario b ON b.idben=bvs.idben AND bvs.perteneceSiNo=1\r\n"
					+ "JOIN persona per ON per.idper=b.idper\r\n"
					+ "JOIN usuario u ON u.login=os.login\r\n"
					+ "where (os.numords=? or ?=-1) \r\n"
					+ "and (s.numsolt=? or ?=-1)\r\n"
					+ "and (upper(v.placa) like concat('%',upper(?),'%') \r\n"
					+ "or upper(per.ci) like concat('%',upper(?),'%'))\r\n"
					+ "and (os.instaladoSiNo=? or ?=-1) and os.anio=? ";
			Integer tot=db.queryForObject(sql, Integer.class,numord,numord,numsolt,numsolt,search,search,estado,estado,anio);
			
			
			sql="SELECT os.*,row_number() OVER(ORDER BY os.idordserv) as RN,? as tot\r\n"
					+ "FROM ordenServicio os \r\n"
					+ "JOIN servicio serv ON serv.idserv=os.idserv \r\n"
					+ "JOIN taller tll ON  os.idtall=tll.idtall\r\n"
					+ "JOIN solicitud s ON s.idsolt=os.idsolt\r\n"
					+ "JOIN benVehSolt bvs ON bvs.idsolt=s.idsolt\r\n"
					+ "join vehiculo v on bvs.placa=v.placa\r\n"
					+ "JOIN beneficiario b ON b.idben=bvs.idben AND bvs.perteneceSiNo=1\r\n"
					+ "JOIN persona per ON per.idper=b.idper\r\n"
					+ "JOIN usuario u ON u.login=os.login\r\n"
					+ "where (os.numords=? or ?=-1) \r\n"
					+ "and (s.numsolt=? or ?=-1)\r\n"
					+ "and (upper(v.placa) like concat('%',upper(?),'%') \r\n"
					+ "or upper(per.ci) like concat('%',upper(?),'%'))\r\n"
					+ "and (os.instaladoSiNo=? or ?=-1) and os.anio=?\r\n"
					+ "ORDER BY os.numords ASC \r\n"
					+ "LIMIT ? OFFSET ?";

			return db.query(sql,new objOrdenServicio(),tot,numord,numord,numsolt,numsolt,search,search,estado,estado,anio,length,start);
		} catch (Exception e) {
			e.printStackTrace();    
			System.out.println(e.getMessage());
			return null;
		}
	}
	
	@Transactional
	public List<OrdenServicio> listar_orden_servicio_d1(int start,int estado,String search,int length,int anio,int numord,int numsolt){
		String sql="";
		try {
			Integer tot=Constantes.NUM_MAX_DATATABLE;
			
			sql="SELECT os.*,row_number() OVER(ORDER BY os.idordserv) as RN,? as tot\r\n"
					+ "FROM ordenServicio os \r\n"
					+ "JOIN servicio serv ON serv.idserv=os.idserv \r\n"
					+ "JOIN taller tll ON  os.idtall=tll.idtall\r\n"
					+ "JOIN solicitud s ON s.idsolt=os.idsolt\r\n"
					+ "JOIN benVehSolt bvs ON bvs.idsolt=s.idsolt\r\n"
					+ "join vehiculo v on bvs.placa=v.placa\r\n"
					+ "JOIN beneficiario b ON b.idben=bvs.idben AND bvs.perteneceSiNo=1\r\n"
					+ "JOIN persona per ON per.idper=b.idper\r\n"
					+ "JOIN usuario u ON u.login=os.login\r\n"
					+ "where (os.numords=? or ?=-1) \r\n"
					+ "and (s.numsolt=? or ?=-1)\r\n"
					+ "and (upper(v.placa) like concat('%',upper(?),'%') \r\n"
					+ "or upper(per.ci) like concat('%',upper(?),'%'))\r\n"
					+ "and (os.instaladoSiNo=? or ?=-1) and os.anio=?\r\n"
					+ "ORDER BY os.numords ASC \r\n"
					+ "limit " +tot;
			;
			List<OrdenServicio> lista =db.query(sql,new objOrdenServicio(),tot,numord,numord,numsolt,numsolt,search,search,estado,estado,anio);
//			new DatabaseConfig(db.getDataSource()).destroy();
			return lista;
		} catch (Exception e) {
			e.printStackTrace();    
			System.out.println(e.getMessage());
			return null;
		}
	}
	@Transactional
	public List<OrdenServicioRecalificacion> listar_ordenes_servicio_recalificacion_d(int start,int estado,String search,int length,int anio,int numord,int numsolt){
		String sql="";
		try {
			Integer tot=Constantes.NUM_MAX_DATATABLE;
			
			sql="SELECT os.*,row_number() OVER(ORDER BY os.idordservrecal) as RN,? as tot\r\n"
					+ "FROM ordenserviciorecalificacion os \r\n"
					+ "JOIN taller tll_c ON  os.idtallconver=tll_c.idtall\r\n"
//					+ "JOIN taller tll_r ON  os.idtallrecal=tll_r.idtall\r\n"
					+ "JOIN solicitudreposicionrecalificacion s ON s.idrecal=os.idrecal\r\n"
					+ "JOIN benvehsoltrec bvs ON bvs.idrecal=s.idrecal\r\n"
					+ "join vehiculo v on bvs.placa=v.placa\r\n"
					+ "JOIN beneficiario b ON b.idben=bvs.idben AND bvs.perteneceSiNo=1\r\n"
					+ "JOIN persona per ON per.idper=b.idper\r\n"
					+ "JOIN usuario u ON u.login=os.login\r\n"
					+ "where (os.numords=? or ?=-1) \r\n"
					+ "and (s.numsoltrec=? or ?=-1)\r\n"
					+ "and (upper(v.placa) like concat('%',upper(?),'%') or upper(per.ci) like concat('%',upper(?),'%'))\r\n"
					+ "AND (os.estado=? OR ?=-1)\r\n"
					+ "and os.anio=?\r\n"
					+ "ORDER BY os.numords ASC \r\n"
					+ "limit " +tot;

			return db.query(sql,new objOrdenServicioRecalificacion(),tot,numord,numord,numsolt,numsolt,search,search,estado,estado,anio);
		} catch (Exception e) {
			e.printStackTrace();    
			System.out.println(e.getMessage());
			return null;
		}
	}
	@Transactional
	public List<Map<String,Object>> ListReductores(){
		String sql="select r.* FROM reductor r";
		return this.db.queryForList(sql,new Object[] {});
	}
	@Transactional
	public List<Map<String,Object>> ListMarcaReductores(){
		String sql="select mr.* FROM marcaReductor mr";
		return this.db.queryForList(sql,new Object[] {});
	}
	@Transactional
	public List<Map<String,Object>> ListCilindros(){
		String sql="select c.* FROM cilindro c";
		return this.db.queryForList(sql,new Object[] {});
	}
	@Transactional
	public List<Map<String,Object>> ListSistemaMotor(){
		String sql="SELECT tp.* FROM tipoMotorVehiculo tp";
		return this.db.queryForList(sql,new Object[] {});
	}
	@Transactional
	public List<Map<String,Object>> ListCombustibles(){
		String sql="SELECT * FROM combustible where estado=1";
		return this.db.queryForList(sql,new Object[] {});
	}
	@Transactional
	public boolean registrar(HttpServletRequest req,Persona xuser) {
//		System.out.println("param1: "+req.getParameter("tipotecnologia"));
//		System.out.println("param2: "+req.getParameter("capacidad"));
		String sql="";
		int idserv=generarIdServ();
		int idcil=Integer.parseInt(req.getParameter("capacidad"));
		int idreduc=Integer.parseInt(req.getParameter("tipotecnologia"));
		int idtipomotorveh=Integer.parseInt(req.getParameter("sistemamotor"));
		int idcomb=Integer.parseInt(req.getParameter("combustible"));
		String factCobro=req.getParameter("factorcobro");
		String preciodolares=req.getParameter("preciodolares");
		String preciobolivianos=req.getParameter("preciobolivianos");
		String preciototal=req.getParameter("preciototal");
		int numpistones=Integer.parseInt(req.getParameter("numpistones"));
		String login=xuser.getUsuario().getLogin();
		System.out.println("idserv: "+idserv+" idcil: "+idcil+" idreduc: "+idreduc+" idtipomotorveh: "+idtipomotorveh+" idfactCobro: "+factCobro+" precioD: "+preciodolares+" precioB:"+preciobolivianos);
		try {
			sql="INSERT INTO servicio(idserv,preciodolares,preciobolivianos,idcil,idreduc,factorcobro,idtipomotorveh,login,numpistones,idcomb,preciototal) VALUES(?,?,?,?,?,?,?,?,?,?,?)";
			this.db.update(sql,idserv,preciodolares,preciobolivianos,idcil,idreduc,factCobro,idtipomotorveh,login,numpistones,idcomb,preciototal);
			return true;
		}catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
			return false;
		}
	}
	@Transactional
	public boolean registrar_d(HttpServletRequest req,Persona xuser) {
//		System.out.println("param1: "+req.getParameter("tipotecnologia"));
//		System.out.println("param2: "+req.getParameter("capacidad"));
		
		String gestion_s =req.getParameter("gestion");
		int gestion=Integer.parseInt(gestion_s);
		System.out.println("Gestion:"+gestion);
		
		String sql="";
		int idserv=generarIdServ();
		int idcil=Integer.parseInt(req.getParameter("capacidad"));
		int idreduc=Integer.parseInt(req.getParameter("tipotecnologia"));
		int idtipomotorveh=Integer.parseInt(req.getParameter("sistemamotor"));
		int idcomb=Integer.parseInt(req.getParameter("combustible"));
		String factCobro=req.getParameter("factorcobro");
		String preciodolares=req.getParameter("preciodolares");
		String preciobolivianos=req.getParameter("preciobolivianos");
		String preciototal=req.getParameter("preciototal");
		int numpistones=Integer.parseInt(req.getParameter("numpistones"));
		String login=xuser.getUsuario().getLogin();
		System.out.println("idserv: "+idserv+" idcil: "+idcil+" idreduc: "+idreduc+" idtipomotorveh: "+idtipomotorveh+" idfactCobro: "+factCobro+" precioD: "+preciodolares+" precioB:"+preciobolivianos);
		try {
			sql="INSERT INTO servicio(idserv,preciodolares,preciobolivianos,idcil,idreduc,factorcobro,idtipomotorveh,login,numpistones,idcomb,preciototal,estado,anio) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?)";
			this.db.update(sql,idserv,preciodolares,preciobolivianos,idcil,idreduc,factCobro,idtipomotorveh,login,numpistones,idcomb,preciototal,1,gestion);
			return true;
		}catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
			return false;
		}
	}
	@Transactional
	public int getAccionOS(){
		String sql="SELECT idacc FROM accion WHERE tipo='os'";
		return this.db.queryForObject(sql,Integer.class);
	}
	@Transactional
	public int getRegistrarFirmasOS(){
		String sql="SELECT DISTINCT idfirm FROM firmasOS WHERE estado=1";
		return this.db.queryForObject(sql,Integer.class);
	}
	@Transactional
	public Object[] registrarOS(HttpServletRequest req,Persona xuser) {
//		System.out.println("param1: "+req.getParameter("tipotecnologia"));
//		System.out.println("param2: "+req.getParameter("capacidad"));
		String sql="";
		Object []Respuesta=new Object[2];
		int idOrdServ=generarIdOrdServ();
		int idfirm=getRegistrarFirmasOS();
		int numOrdServ=Integer.parseInt(req.getParameter("numOrdServ"));  
		int idSolt=Integer.parseInt(req.getParameter("idsolt"));
		int idServ=Integer.parseInt(req.getParameter("idserv"));
		int idtall=Integer.parseInt(req.getParameter("idtall"));
//		int idRom=Integer.parseInt(req.getParameter("idRom"));
		String login=xuser.getUsuario().getLogin();
		
		try {
//			sql="INSERT INTO ordenServicio(idordserv,numords,idsolt,idtall,idserv,login,idRom,idfirm) VALUES(?,?,?,?,?,?,?,?)";
			sql="INSERT INTO ordenServicio(idordserv,numords,idsolt,idtall,idserv,login,idfirm,instaladosino,idinst,anio) VALUES(?,?,?,?,?,?,?,?,?,?)";
//			this.db.update(sql,idOrdServ,numOrdServ,idSolt,idtall,idServ,login,idRom,idfirm);
			this.db.update(sql,idOrdServ,numOrdServ,idSolt,idtall,idServ,login,idfirm,0,1,2021);
			sql="UPDATE solicitud SET idacc=? WHERE idsolt=?";
			this.db.update(sql,getAccionOS(),idSolt);
			
			Respuesta[0]=true;
			Respuesta[1]=idOrdServ;
			return Respuesta;
		}catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
			Respuesta[0]=false;
			return Respuesta;
		}
	}
	
	@Transactional
	public Object[] registrarOS_d(HttpServletRequest req,Persona xuser) {
//		System.out.println("param1: "+req.getParameter("tipotecnologia"));
//		System.out.println("param2: "+req.getParameter("capacidad"));
		
		String gestion_s =req.getParameter("gestion");
		int gestion=Integer.parseInt(gestion_s);
		System.out.println("Gestion:"+gestion);
		
		
		String sql="";
		Object []Respuesta=new Object[3];
		int idOrdServ=generarIdOrdServ();
		int idfirm=getRegistrarFirmasOS();
//		int numOrdServ=Integer.parseInt(req.getParameter("numOrdServ"));  
		int numOrdServ=numeroOrdenServicioByGestion(gestion);
		int idSolt=Integer.parseInt(req.getParameter("idsolt"));
		int idServ=Integer.parseInt(req.getParameter("idserv"));
		int idtall=Integer.parseInt(req.getParameter("idtall"));
//		int idRom=Integer.parseInt(req.getParameter("idRom"));
		String login=xuser.getUsuario().getLogin();
		
		try {
//			sql="INSERT INTO ordenServicio(idordserv,numords,idsolt,idtall,idserv,login,idRom,idfirm) VALUES(?,?,?,?,?,?,?,?)";
			sql="INSERT INTO ordenServicio(idordserv,numords,idsolt,idtall,idserv,login,idfirm,instaladosino,idinst,anio) VALUES(?,?,?,?,?,?,?,?,?,?)";
//			this.db.update(sql,idOrdServ,numOrdServ,idSolt,idtall,idServ,login,idRom,idfirm);
			this.db.update(sql,idOrdServ,numOrdServ,idSolt,idtall,idServ,login,idfirm,0,1,gestion);
			sql="UPDATE solicitud SET idacc=? WHERE idsolt=?";
			this.db.update(sql,getAccionOS(),idSolt);
			
			Servicio servicio=this.datosModificar_servicio(idServ);
			Solicitud solicitud=this.manejadorSolicitudes.verSolicitud(idSolt);
			
			sql="UPDATE vehiculo SET anio=?,costo=? WHERE placa=?";
			this.db.update(sql,gestion,Double.parseDouble(servicio.getPrecioDolares()),solicitud.getVehiculo().getPlaca());
			
			
			Respuesta[0]=true;
			Respuesta[1]=idOrdServ;
			Respuesta[2]=numOrdServ;
			return Respuesta;
		}catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
			Respuesta[0]=false;
			return Respuesta;
		}
	}
	@Transactional
	public Object[] registrarOSRecalificacion_d(HttpServletRequest req,Persona xuser) {

		
		int recal_cil=0;		
		int reponer_cil=0;
		int reponer_kit_completo=0;
		int reponer_kit_partes=0;
		
		String recalificacionString=req.getParameter("recal_cil");
		String repocisionCilindroString=req.getParameter("reponer_cil");
		String repocisionKitString=req.getParameter("reposicion_kit");
		
		
		if (recalificacionString!=null && repocisionCilindroString!=null ) {
			recal_cil=1;
			reponer_cil=1;
		}else if(recalificacionString!=null && repocisionCilindroString==null) {
			
			if(Integer.parseInt(recalificacionString)==1) {
				recal_cil=1;	
			}else {
				recal_cil=0;
			}
			reponer_cil=0;
		}
		
		if(repocisionKitString!=null){
			int repocisionKitStringInt=Integer.parseInt(req.getParameter("reposicion_kit"));
			if(repocisionKitStringInt==1) {
				 reponer_kit_completo=1;
				 reponer_kit_partes=0;
			}else if(repocisionKitStringInt==2) {
				 reponer_kit_completo=0;
				 reponer_kit_partes=1;
			}
		}else {
			 reponer_kit_completo=0;
			 reponer_kit_partes=0;
		}
		System.out.println("recal_cil:"+recal_cil);
		System.out.println("reponer_cil:"+reponer_cil);
		System.out.println("reponer_kit_completo:"+reponer_kit_completo);
		System.out.println("reponer_kit_partes:"+reponer_kit_partes);
		
		String gestion_s =req.getParameter("gestion");
		int gestion=Integer.parseInt(gestion_s);
		System.out.println("Gestion:"+gestion);
		
		
		String sql="";
		Object []Respuesta=new Object[3];
		int idOrdServ=generarIdOrdServRecal();
		int idfirm=getRegistrarFirmasOS();
		int numOrdServ=numeroOrdenServicioRecalificacionByGestion(gestion);
		int idrecal=Integer.parseInt(req.getParameter("idrecal"));
		int idtall=Integer.parseInt(req.getParameter("idtall"));
		String login=xuser.getUsuario().getLogin();
		
		try {
			sql="INSERT INTO ordenServiciorecalificacion(idordservrecal,numords,idrecal,idtallconver,login,idfirm,idinst,anio,recal_cil,reponer_cil,reponer_kit_completo,reponer_kit_partes) VALUES(?,?,?,?,?,?,?,?,?,?,?,?)";
			this.db.update(sql,idOrdServ,numOrdServ,idrecal,idtall,login,idfirm,1,gestion,recal_cil,reponer_cil,reponer_kit_completo,reponer_kit_partes);
			
			Respuesta[0]=true;
			Respuesta[1]=idOrdServ;
			Respuesta[2]=numOrdServ;
			return Respuesta;
		}catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
			Respuesta[0]=false;
			return Respuesta;
		}
	}
	@Transactional
	public Object[] registrarOSRecalificacionCilindro_d(HttpServletRequest req,Persona xuser) {

		int recal_cil=0;				
		String recalificacionString=req.getParameter("recal_cil");
		if (recalificacionString!=null) {
			recal_cil=1;
		}
		
		System.out.println("recal_cil:"+recal_cil);
		String gestion_s =req.getParameter("gestion");
		int gestion=Integer.parseInt(gestion_s);
		System.out.println("Gestion:"+gestion);
		
		
		String sql="";
		Object []Respuesta=new Object[3];

		int idordservrecal=Integer.parseInt(req.getParameter("idordservrecal"));
		int idtall=Integer.parseInt(req.getParameter("idtall"));
		String login=xuser.getUsuario().getLogin();
		System.out.println("idordservrecal:"+idordservrecal);
		System.out.println("idtall:"+idtall);
		try {

			sql="UPDATE ordenServiciorecalificacion SET recal_cil=?,idtallrecal=? WHERE idordservrecal=? and anio=?";
			this.db.update(sql,recal_cil,idtall,idordservrecal,gestion);
			
			
			Respuesta[0]=true;
			Respuesta[1]=idordservrecal;
			return Respuesta;
		}catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
			Respuesta[0]=false;
			return Respuesta;
		}
	}
	
	@Transactional
	public Object[] registrarOSReposicionCilindro_d(HttpServletRequest req,Persona xuser) {

		int reponer_cil=0;				
		String recalificacionString=req.getParameter("reponer_cil");
		if (recalificacionString!=null) {
			reponer_cil=1;
		}
		
		System.out.println("reponer_cil:"+reponer_cil);
		String gestion_s =req.getParameter("gestion");
		int gestion=Integer.parseInt(gestion_s);
		System.out.println("Gestion:"+gestion);
		
		
		String sql="";
		Object []Respuesta=new Object[3];

		int idordservrecal=Integer.parseInt(req.getParameter("idordservrecal"));
//		int idtall=Integer.parseInt(req.getParameter("idtall"));
		String login=xuser.getUsuario().getLogin();
		System.out.println("idordservrecal:"+idordservrecal);
//		System.out.println("idtall:"+idtall);
		try {

			sql="UPDATE ordenServiciorecalificacion SET reponer_cil=? WHERE idordservrecal=? and anio=?";
			this.db.update(sql,reponer_cil,idordservrecal,gestion);
			
			
			Respuesta[0]=true;
			Respuesta[1]=idordservrecal;
			return Respuesta;
		}catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
			Respuesta[0]=false;
			return Respuesta;
		}
	}
	@Transactional
	public Servicio datosModificar_servicio(int idserv_param){
		String sql="";
		Servicio s=null;
		int idserv=idserv_param;
		System.out.println("idserv: "+idserv);
		try {
			sql="SELECT s.* FROM servicio s WHERE s.idserv=?";
			s=this.db.queryForObject(sql,new objServicios(),idserv);
		} catch (Exception e) {
			s=null;
		}
		return s;
	}
	@Transactional
	public boolean modificar(HttpServletRequest req,Persona xuser){
		
		String gestion_s =req.getParameter("gestion");
		int gestion=Integer.parseInt(gestion_s);
		System.out.println("Gestion:"+gestion);
		
		String sql="";
		int idserv=Integer.parseInt(req.getParameter("idserv"));
		int idcil=Integer.parseInt(req.getParameter("capacidad"));
		int idreduc=Integer.parseInt(req.getParameter("tipotecnologia"));
		int idtipomotorveh=Integer.parseInt(req.getParameter("sistemamotor"));
		int idcomb=Integer.parseInt(req.getParameter("combustible"));
		int numpistones=Integer.parseInt(req.getParameter("numPistones"));
		
		String preciodolares=req.getParameter("precioDolares");
		String preciobolivianos=req.getParameter("precioBolivianos");
		String factCobro=req.getParameter("factorCobro");
		
	
		String preciototal=req.getParameter("precioTotal");
		
		String login=xuser.getUsuario().getLogin();
		System.out.println(" idcil: "+idcil+" idreduc: "+idreduc+" idtipomotorveh: "+idtipomotorveh+" factCobro: "+factCobro+" precioD: "+preciodolares+" precioB:"+preciobolivianos);
		System.out.println("idcomb: "+idcomb);
		System.out.println("PrecioModTotal: "+preciototal);
		try {
			sql="UPDATE servicio SET preciodolares=?,preciobolivianos=?,idcil=?,idreduc=?,factorcobro=?,idtipomotorveh=?,login=?,numpistones=?,idcomb=?,preciototal=? WHERE idserv=? and anio=?";
			this.db.update(sql,preciodolares,preciobolivianos,idcil,idreduc,factCobro,idtipomotorveh,login,numpistones,idcomb,preciototal,idserv,gestion);
			
			return true;
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e.getMessage());
			e.printStackTrace();
			return false;
		}
	}
	@Transactional
	public boolean eliminar(Integer id){
		String sql="";
		try {
			sql="UPDATE servicio SET estado=0 WHERE idserv=?";
			int a=this.db.update(sql,id);
			this.db.update(sql,id);
			System.out.println("sql_elimino: "+a);
			if (a==1) {
				return true;	
			} else {
				return false;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	@Transactional
	public boolean habilitar(Integer id){
		String sql="";
		try {
			sql="UPDATE servicio SET estado=1 WHERE idserv=?";
			int a=this.db.update(sql,id);
			this.db.update(sql,id);
			System.out.println("sql_habilito: "+a);
			if (a==1) {
				return true;	
			} else {
				return false;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/*ORDEN SERVICIO*/
	@Transactional
	public Map<String,Object>	nitEmpresa(int idinst){
		String sql="select * from institucion where idinst=?";
		return db.queryForMap(sql, new Object[]{idinst});
	}
	@Transactional
	public List<Telefono> ListaTelfTall(int idOrdServ){
		String sql="SELECT telf.numero FROM ordenServicio ords JOIN taller tall ON ords.idtall=tall.idtall JOIN persona per ON tall.idper=per.idper JOIN telefono telf ON per.idper=telf.idper WHERE idordserv=?";
		return this.db.query(sql,new objTelefono(),idOrdServ);
	}
	@Transactional
	public List<Telefono> ListaTelfTallOrdenRecalificacionReposicion(int idOrdServ){
		String sql="SELECT telf.numero FROM ordenserviciorecalificacion ords JOIN taller tall ON ords.idtallconver=tall.idtall JOIN persona per ON tall.idper=per.idper JOIN telefono telf ON per.idper=telf.idper WHERE idordservrecal=?";
		return this.db.query(sql,new objTelefono(),idOrdServ);
	}
	@Transactional
	public List<Telefono> ListaTelfTallerRecalificacionOrdenRecalificacionCilindro(int idOrdServ){
		String sql="SELECT telf.numero FROM ordenserviciorecalificacion ords JOIN taller tall ON ords.idtallrecal=tall.idtall JOIN persona per ON tall.idper=per.idper JOIN telefono telf ON per.idper=telf.idper WHERE idordservrecal=?";
		return this.db.query(sql,new objTelefono(),idOrdServ);
	}
	@Transactional
	public List<Telefono> ListaTelfBen(int idOrdServ){
		String sql="SELECT DISTINCT(tel.numero) FROM ordenServicio ords JOIN solicitud solt ON solt.idsolt=ords.idsolt JOIN benVehSolt bvs ON solt.idsolt=bvs.idsolt JOIN beneficiario ben ON ben.idben=bvs.idben  AND bvs.perteneceSiNo=1 JOIN persona per ON per.idper=ben.idper JOIN telefono tel ON tel.idper=per.idper WHERE idordserv=?";
		return this.db.query(sql,new objTelefono(),idOrdServ);
	}
	@Transactional
	public List<Telefono> ListaTelfBenRecal(int idOrdServ){
		String sql="SELECT DISTINCT(tel.numero) \r\n"
				+ "FROM ordenserviciorecalificacion ords \r\n"
				+ "JOIN solicitudreposicionrecalificacion solt ON solt.idrecal=ords.idrecal\r\n"
				+ "JOIN benvehsoltrec bvs ON solt.idrecal=bvs.idrecal \r\n"
				+ "JOIN beneficiario ben ON ben.idben=bvs.idben  AND bvs.perteneceSiNo=1 \r\n"
				+ "JOIN persona per ON per.idper=ben.idper \r\n"
				+ "JOIN telefono tel ON tel.idper=per.idper WHERE idordservrecal=?";
		return this.db.query(sql,new objTelefono(),idOrdServ);
	}
	@Transactional
	public List<Telefono> ListaTelfIC(int idincl){
		String sql="SELECT DISTINCT(tel.numero) FROM incumplimientoContrato ic JOIN solicitud solt ON solt.idsolt=ic.idsolt JOIN benVehSolt bvs ON solt.idsolt=bvs.idsolt JOIN beneficiario ben ON ben.idben=bvs.idben  AND bvs.perteneceSiNo=1 JOIN persona per ON per.idper=ben.idper JOIN telefono tel ON tel.idper=per.idper WHERE idincl=?";
		return this.db.query(sql,new objTelefono(),idincl);
	}
	@Transactional
	/*ORDEN SERVICIO*/
	public List<Map<String,Object>> ListTalleres(){
		String sql="select * FROM taller where estado=1";
		return this.db.queryForList(sql,new Object[] {});
	}
	@Transactional
	public List<Map<String,Object>> ListTalleres(int anio){
		String sql="select t.* FROM taller t,tallgest tx,gestion gt where t.estado=1 and tx.estado=1 and tx.idtall=t.idtall and gt.idgest=tx.idgest and gt.gestion=?";
		return this.db.queryForList(sql,new Object[] {anio});
	}
	@Transactional
	public List<Map<String,Object>> ListTalleresConversion(int anio){
		String sql="select t.* FROM taller t,tallgest tx,gestion gt where t.tipo='c' and t.estado=1 and tx.estado=1 and tx.idtall=t.idtall and gt.idgest=tx.idgest and gt.gestion=?";
		return this.db.queryForList(sql,new Object[] {anio});
	}
	@Transactional
	public List<Map<String,Object>> ListTalleresPorUsuarioTaller(int anio,String login){
		String sql="select t.* \r\n"
				+ "FROM taller t,tallgest tx,gestion gt,usuariotaller ut\r\n"
				+ "where t.tipo='c' \r\n"
				+ "and t.estado=1 \r\n"
				+ "and tx.estado=1 \r\n"
				+ "and tx.idtall=t.idtall \r\n"
				+ "and gt.idgest=tx.idgest \r\n"
				+ "and gt.gestion=?\r\n"
				+ "and ut.idtall=t.idtall\r\n"
				+ "and ut.login=?";
		return this.db.queryForList(sql,new Object[] {anio,login});
	}
	@Transactional
	public int getidRolTaller(String login){

		try {
			String sql="SELECT rl.idrol FROM rol rl\r\n"
					+ "WHERE  rl.tipo='t' AND rl.idrol  IN (SELECT ru.idrol FROM rolusu ru \r\n"
					+ "WHERE ru.idrol=rl.idrol\r\n"
					+ "and ru.\"login\" IN (SELECT ut.\"login\" FROM usuariotaller ut WHERE ut.\"login\"=ru.\"login\" AND ru.\"login\"=?)\r\n"
					+ ")" ;
			return this.db.queryForObject(sql,Integer.class,login);
		} catch (Exception e) {
			e.printStackTrace();
//			System.out.println(e.getMessage());
			return 0;
		}
		
	}
	@Transactional
	public List<Map<String,Object>> ListTalleresRecalificacion(int anio){
		String sql="select t.* FROM taller t,tallgest tx,gestion gt where t.tipo='rc' and t.estado=1 and tx.estado=1 and tx.idtall=t.idtall and gt.idgest=tx.idgest and gt.gestion=?";
		return this.db.queryForList(sql,new Object[] {anio});
	}
	@Transactional
	public List<Map<String,Object>> ListChipRom(){
		String sql="select * FROM chipRom where bloqueado=0";
		return this.db.queryForList(sql,new Object[] {});
	}
	@Transactional
	public List<Servicio> ListServicios(){
		String sql="select * FROM servicio where estado=1";
		return this.db.query(sql,new objServicios());
	}
	@Transactional
	public List<Servicio> ListServicios(int anio){
		String sql="select * FROM servicio where estado=1 and anio=?";
		return this.db.query(sql,new objServicios(),anio);
	}
	@Transactional
	public List<Combustible> ListComb(){
		String sql="select * FROM combustible where estado=1";
		return this.db.query(sql,new objCombustible());
	}
	@Transactional
	/*ORDEN DE SERVICIO*/
	public List<Solicitud> FiltroSolicitudOS(String cadena,int gestion){
		String sql="";
		try {
			sql="SELECT s.* \r\n"
					+ "FROM solicitud s,beneficiario b,persona p,benVehSolt bvs \r\n"
					+ "WHERE bvs.idben=b.idben \r\n"
					+ "AND bvs.perteneceSiNo=1 \r\n"
					+ "AND bvs.idsolt=s.idsolt \r\n"
					+ "AND s.estado=1 \r\n"
					+ "AND s.aprobadoSiNo=1 \r\n"
					+ "AND b.idper=p.idper \r\n"
					+ "AND ((concat('',s.numSolt) LIKE ?) or p.ci LIKE ?)\r\n"
					+ "AND s.idsolt NOT IN (SELECT od.idsolt FROM ordenServicio od WHERE od.idsolt=s.idsolt)\r\n"
					+ "AND s.anio=?\r\n"
					+ "";
				
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
		return this.db.query(sql, manejadorSolicitudes.new objSolicitud(),'%'+cadena+'%','%'+cadena+'%',gestion);
	}
	@Transactional
	public List<SolicitudReposicionRecalificacion> FiltroSolicitudOSRecalificacion(String cadena,int gestion){
		String sql="";
		try {
//			sql="SELECT s.* \r\n"
//					+ "FROM solicitudreposicionrecalificacion s,beneficiario b,persona p,benvehsoltrec bvs \r\n"
//					+ "WHERE bvs.idben=b.idben \r\n"
//					+ "AND bvs.pertenecesino=1 \r\n"
//					+ "AND bvs.idrecal=s.idrecal\r\n"
//					+ "AND s.estado=1\r\n"
//					+ "AND b.idper=p.idper \r\n"
//					+ "AND ((concat('',s.numsoltrec) LIKE ?) or p.ci LIKE ?)\r\n"
//					+ "AND s.idrecal NOT IN (SELECT od.idrecal FROM ordenserviciorecalificacion od WHERE od.idrecal=s.idrecal)\r\n"
//					+ "AND s.anio=?";
			
			sql="SELECT DISTINCT s.* \r\n"
					+ "FROM solicitudreposicionrecalificacion s,beneficiario b,persona p,benvehsoltrec bvs \r\n"
					+ "WHERE bvs.idben=b.idben\r\n"
					+ "AND bvs.pertenecesino=1 \r\n"
					+ "AND bvs.idrecal=s.idrecal\r\n"
					+ "AND s.estado=1\r\n"
					+ "AND b.idper=p.idper\r\n"
					+ "AND ((concat('',s.numsoltrec,'') LIKE ?) or p.ci LIKE ?)\r\n"
					+ "AND s.idrecal NOT IN (SELECT od.idrecal FROM ordenserviciorecalificacion od WHERE od.idrecal=s.idrecal)\r\n"
					+ "AND s.anio=?";
				
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
		return this.db.query(sql, manejadorSolicitudes.new objRecalificacion_d_recalificacion(),'%'+cadena+'%','%'+cadena+'%',gestion);
	}
	
	@Transactional
	public int generarIdServ(){
		String sql="select COALESCE(max(idserv),0)+1 as idserv from servicio";
		return db.queryForObject(sql, Integer.class);
	}
	@Transactional
	public int generarIdOrdServ(){
		String sql="select COALESCE(max(idordserv),0)+1 as idserv from ordenServicio";
		return db.queryForObject(sql, Integer.class);
	}
	@Transactional
	public int generarIdOrdServRecal(){
		String sql="select COALESCE(max(idordservrecal),0)+1 as idordservrecal from ordenserviciorecalificacion";
		return db.queryForObject(sql, Integer.class);
	}
	@Transactional
	public int numeroOrdenServicio(){
		String sql="select COALESCE(max(numords),0)+1 as numords from ordenServicio";
		return db.queryForObject(sql, Integer.class);
	}
	@Transactional
	public int numeroOrdenServicioByGestion(int gestion){
		String sql="select COALESCE(max(numords),0)+1 as numords from ordenServicio where anio=?";
		return db.queryForObject(sql, Integer.class,gestion);
	}
	@Transactional
	public int numeroOrdenServicioRByGestion(int gestion){
		String sql="select COALESCE(max(numords),0)+1 as numords from ordenServicio where anio=?";
		return db.queryForObject(sql, Integer.class,gestion);
	}
	@Transactional
	public int numeroOrdenServicioRecalificacionByGestion(int gestion){
		String sql="select COALESCE(max(numords),0)+1 as numords from ordenserviciorecalificacion where anio=?";
		return db.queryForObject(sql, Integer.class,gestion);
	}
	@Transactional
	public OrdenServicio verOrdenServicio(int id) {
		String sql="select * from ordenServicio where idordserv=?";
		return this.db.queryForObject(sql,new objOrdenServicio(),id);
	}
	@Transactional
	public OrdenServicioRecalificacion verOrdenServicioRecalificacionReposicion(int id) {
		String sql="select * from ordenserviciorecalificacion where idordservrecal=?";
		return this.db.queryForObject(sql,new objOrdenServicioRecalificacion(),id);
	}
	@Transactional
	public List<Telefono> ListaTelf(int id){
		String sql="SELECT t.* FROM telefono t,persona p WHERE t.idper=p.idper AND p.idper=?";
		return this.db.query(sql,new objTelefono(),id);
	}
	@Transactional
	/*ACTA DE RECEPCION*/
	public OrdenServicio getOrdenServicioAR(int id) {
		String sql="select os.*  from ordenServicio os,actaRecepcion ac where ac.idordserv=os.idordserv AND os.idordserv=?";
		return this.db.queryForObject(sql,new objOrdenServicio(),id);
	}
	@Transactional
	public List<OrdenServicio> FiltroOrdenServicioAR(String cadena){
		String sql="SELECT os.* FROM ordenServicio os,solicitud s,vehiculo veh,beneficiario b,persona p, benVehSolt bvs WHERE os.idsolt=s.idsolt AND os.instaladoSiNo=1 AND bvs.idben=b.idben AND bvs.perteneceSiNo=1 AND bvs.placa=veh.placa AND bvs.idsolt=s.idsolt and b.idper=p.idper and (os.numords LIKE ? or s.numSolt LIKE ? or p.ci LIKE ?) and os.idordserv NOT IN (SELECT ar.idordserv  FROM actaRecepcion ar WHERE ar.idordserv=os.idordserv)";
		return this.db.query(sql, new objOrdenServicio(),'%'+cadena+'%','%'+cadena+'%','%'+cadena+'%');
	}
	@Transactional
	/*Instalacion KIT*/
	public OrdenServicio getOrdenServicioIK(int id) {
		String sql="select os.*  from ordenServicio os,registroKit ik where ik.idordserv=os.idordserv AND ik.idregistroKit=?";
		return this.db.queryForObject(sql,new objOrdenServicio(),id);
	}
	@Transactional
	public OrdenServicio metOrdenServicio(int id) {
		String sql="select os.*  from ordenServicio os os.idordserv=?";
		return this.db.queryForObject(sql,new objOrdenServicio(),id);
	}
	@Transactional
	public int getIdtallByLogin(String login){
		try {
			String sql="select ut.idtall from usuariotaller ut where ut.login=?";
			return db.queryForObject(sql, Integer.class,login);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			System.out.println(e.getMessage());
			return -1;
		}
		
	
	}
	@Transactional
	public List<OrdenServicio> FiltroOrdenServicioIK(String cadena,String login,int gestion,int idtall){
		
//		int idtall=getIdtallByLogin(login);
		System.out.println("idtall:"+idtall);
		
		String sql="";
		try {
		
			sql="SELECT os.* \r\n" + 
					"FROM ordenServicio os,solicitud s,vehiculo veh,beneficiario b,persona p,benVehSolt bvs,usuario us,taller tall\r\n" + 
					"WHERE os.idsolt=s.idsolt AND os.anio=? \r\n" + 
					"AND os.instaladoSiNo=0\r\n" + 
					"AND bvs.idben=b.idben \r\n" + 
					"AND bvs.perteneceSiNo=1 \r\n" + 
					"AND bvs.placa=veh.placa \r\n" + 
					"AND bvs.idsolt=s.idsolt \r\n" + 
					"and b.idper=p.idper \r\n" + 
					"and (concat('',os.numords,'') LIKE ? or concat('',s.numsolt,'') LIKE ? or p.ci LIKE ?)\r\n" + 
					"and os.idtall=tall.idtall\r\n" + 
					"AND us.login=? and (tall.idtall=?)\r\n" + 
					"and os.idordserv NOT IN (SELECT r.idordserv  FROM registroKit r WHERE r.idordserv=os.idordserv)\r\n" + 
					"LIMIT ?";
			return this.db.query(sql, new objOrdenServicio(),gestion,'%'+cadena+'%','%'+cadena+'%','%'+cadena+'%',login,idtall,Constantes.NUM_MAX_DATATABLE);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
			return null;
		}
		
	}
	@Transactional
	public List<Cilindro> ListaCilindros(){
		String sql="SELECT * FROM cilindro WHERE estado=1";
		return this.db.query(sql, new objCilindro());
	}
	@Transactional
	public List<MarcaCilindro> ListaMarcaCilindros(){
		String sql="SELECT * FROM marcacilindro WHERE estado=1";
		return this.db.query(sql, new objMarcaCilindro());
	}
	@Transactional
	public List<Reductor> ListaReductores(){
		String sql="SELECT * FROM reductor WHERE estado=1";
		return this.db.query(sql, new objReductor());
	}
	@Transactional
	public List<MarcaReductor> ListaMarcaReductores(){
		String sql="SELECT * FROM marcareductor WHERE estado=1";
		return this.db.query(sql, new objMarcaReductor());
	}
	@Transactional
	public List<Map<String, Object>> ListaCapacidadCilindros(){
		String sql="SELECT * FROM capacidadcilindro WHERE estado=1";
		return this.db.queryForList(sql, new Object[] {});
	}
	@Transactional
	public List<MarcaValvulaCilindro> ListaMarcaValvulaCilindro(){
		String sql="SELECT * FROM marcavalvulacilindro WHERE estado=1";
		return this.db.query(sql, new objMarcaValvulaCilindro() {});
	}


}
