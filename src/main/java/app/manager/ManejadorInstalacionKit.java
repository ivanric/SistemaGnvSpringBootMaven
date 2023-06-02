package app.manager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;
import javax.transaction.Transactional;

import org.apache.catalina.valves.rewrite.InternalRewriteMap.UpperCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import org.springframework.stereotype.Service;

import app.manager.ManejadorServicios.objMarcaValvulaCilindro;
import app.manager.ManejadorServicios.objOrdenServicio;
import app.models.CapacidadCilindro;
import app.models.Cilindro;
import app.models.DesmontajeKit;
import app.models.InstalacionCilindro;
import app.models.MarcaCilindro;
import app.models.MarcaReductor;
import app.models.MarcaValvulaCilindro;
import app.models.OrdenServicio;
import app.models.Persona;
import app.models.Reductor;
import app.models.RegistroKit;
import app.models.Solicitud;
import app.models.Taller;
import app.models.TrasladoKitVehiculo;
import app.models.implementoskit;
import app.utilidades.Constantes;

@Service
public class ManejadorInstalacionKit {
	private final  JdbcTemplate db;
	public ManejadorInstalacionKit(JdbcTemplate jdbcTemplate) {
		this.db = jdbcTemplate;
		
	}
	@Autowired
	ManejadorServicios manejadorServicios;
	
	public class objDesmontajeKit implements RowMapper<DesmontajeKit>{
		@Override
		public DesmontajeKit mapRow(ResultSet rs, int arg1) throws SQLException {
			DesmontajeKit d= new DesmontajeKit();
			d.setIddes(rs.getInt("iddes"));
			d.setIdregistroKit(rs.getInt("idregistroKit"));
			d.setIdtall(rs.getInt("idtall"));
			d.setFecha(rs.getString("fecha"));
			d.setLogin(rs.getString("login"));
			try {
				d.setRegistroKit(metRegistroKit(rs.getInt("idregistroKit")));
			} catch (Exception e) {
				d.setRegistroKit(null);
			}
			try {
				d.setTaller(metTaller(rs.getInt("idtall")));
			} catch (Exception e) {
				d.setTaller(null);
			}
			return d;
	    }
	}
	public class objTrasladorKitVehiculo implements RowMapper<TrasladoKitVehiculo>{
		@Override
		public TrasladoKitVehiculo mapRow(ResultSet rs, int arg1) throws SQLException {
			TrasladoKitVehiculo t= new TrasladoKitVehiculo();
			t.setIdtraslv(rs.getInt("idtraslv"));
			t.setIddes(rs.getInt("iddes"));
			t.setIdsoltNueva(rs.getInt("idsoltNueva"));
			t.setFechaTraslado(rs.getString("fechaTraslado"));
			t.setLogin(rs.getString("login"));
			try {
				t.setDesmontajeKit(metDesmontajeKit(rs.getInt("iddes")));
			} catch (Exception e) {
				t.setDesmontajeKit(null);
			}
			return t;
	    }
	}
	
	
	//@aui empieza los objetos
	public class objTaller implements RowMapper<Taller>{
		@Override
		public Taller mapRow(ResultSet rs, int arg1) throws SQLException {
			Taller t= new Taller();
			t.setIdtall(rs.getInt("idtall"));
			t.setNombretall(rs.getString("nombretall"));
			t.setDireccion(rs.getString("direccion"));
			t.setFecha_registro(rs.getString("fecha_registro"));
			t.setEstado(rs.getInt("estado"));
			return t;
	    }
	}
	public class objRegistroKit implements RowMapper<RegistroKit>{
		@Override
		public RegistroKit mapRow(ResultSet rs, int arg1) throws SQLException {
			RegistroKit r= new RegistroKit();
			r.setIdregistroKit(rs.getInt("idregistroKit"));
			r.setFechaconversion(rs.getString("fechaconversion"));
//			r.setFechaInstalacion(rs.getString("fechaInstalacion"));
			r.setIdordserv(rs.getInt("idordserv"));
			r.setDesinstaladokitglpsino(rs.getInt("desinstaladokitglpsino"));
			r.setCertificadodesintalacionsino(rs.getInt("certificadoDesintalacionSiNo"));
			r.setLogin(rs.getString("login"));
			r.setIdordserv(rs.getInt("idordserv"));
			r.setIdmarcred(rs.getInt("idmarcred"));
			r.setNumserieregulador(rs.getString("numSerieRegulador"));
			r.setCertificadokit(rs.getString("certificadoKit"));
			r.setFechacertificado(rs.getString("fechaCertificado"));
			r.setNumseriemotor(rs.getString("numseriemotor"));
			r.setCodigobsisa(rs.getString("codigoBSISA"));
			r.setFechacertificado(rs.getString("fechacertificado"));
			r.setAnio(rs.getInt("anio"));
			r.setIdtall(rs.getInt("idtall"));
			r.setObservacioneskit(rs.getString("observacioneskit"));
			r.setPrimeramedicion(rs.getString("primeramedicion"));
			r.setSegundamedicion(rs.getString("segundamedicion"));
			r.setConformeestandasino(rs.getInt("conformeestandasino"));
			
			try {
				r.setReductor(metReductor(rs.getInt("idmarcred")));
			} catch (Exception e) {
				r.setReductor(null);
			}
			try {
				r.setImplementokit(metImplementosKit(rs.getInt("idregistroKit")));
			} catch (Exception e) {
				r.setImplementokit(null);
			}
			
			try {
				r.setCilindros(metListCilindros(rs.getInt("idregistroKit")));
			} catch (Exception e) {
				r.setCilindros(null);
			}
			
			
			//solicitud
			try {
				r.setOrdenServicio(metOrdenServicio(rs.getInt("idordserv")));
			}catch (Exception e){
				r.setOrdenServicio(null);
			}
			
			try {
				r.setTot(rs.getInt("tot"));
			} catch (Exception e) {
				// TODO: handle exception
				r.setTot(0);;
			}
			
			return r;
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
	public class objCapacidadCilindro implements RowMapper<CapacidadCilindro>{
		@Override
		public CapacidadCilindro mapRow(ResultSet rs, int arg1) throws SQLException {
			CapacidadCilindro mc= new CapacidadCilindro();
			mc.setIdcapcil(rs.getInt("idcapcil"));
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
			r.setTipoTecnologia(rs.getString("tipoTecnologia"));
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
	
	
	public class objImplementosKit implements RowMapper<implementoskit>{
		@Override
		public implementoskit mapRow(ResultSet rs, int arg1) throws SQLException {
			implementoskit i= new implementoskit();
			i.setIdimple(rs.getInt("idimple"));
			i.setIdregistrokit(rs.getInt("idregistrokit"));
			i.setCunacilindro(rs.getInt("cunacilindro"));
			i.setIdmarcelecgaso(rs.getInt("idmarcelecgaso"));
			i.setSerieelectrgasolina(rs.getString("serieelectrgasolina"));
			i.setIdmarcelecgas(rs.getInt("idmarcelecgas"));
			i.setSerieelectrgnv(rs.getString("serieelectrgnv"));
			i.setDosificadorsino(rs.getInt("dosificadorsino"));
			i.setSistventeosino(rs.getInt("sistventeosino"));
			i.setIdmarconm(rs.getInt("idmarconm"));
			i.setSeriellaveconmutadora(rs.getString("seriellaveconmutadora"));
			i.setIdmarcvalcargint(rs.getInt("idmarcvalcargint"));
			i.setSerievalvulacargainterna(rs.getString("serievalvulacargainterna"));
			i.setIdmarcvalcargext(rs.getInt("idmarcvalcargext"));
			i.setSerievalvulacargaexterna(rs.getString("serievalvulacargaexterna"));
			i.setIdmarcmangueragas(rs.getInt("idmarcmangueragas"));
			i.setSeriemangueragas(rs.getString("seriemangueragas"));
			i.setIdmarcmangueraagua(rs.getInt("idmarcmangueraagua"));
			i.setMangueraaguaserie(rs.getString("mangueraaguaserie"));
			
			i.setIdmarcemu(rs.getInt("idmarcemu"));
			i.setSerieemulador(rs.getString("serieemulador"));
			i.setIdmarcmano(rs.getInt("idmarcmano"));
			i.setSeriemanometro(rs.getString("seriemanometro"));
			i.setEstado(rs.getInt("estado"));
			

			i.setIdmarccanieria(rs.getInt("idmarccanieria"));
			i.setCanieriaaltapresionserie(rs.getString("canieriaaltapresionserie"));

			i.setIdmarcmem(rs.getInt("idmarcmem"));
			i.setMemoriaserie(rs.getString("memoriaserie"));
			i.setIdmarcsensormap(rs.getInt("idmarcsensormap"));
			i.setSensormapserie(rs.getString("sensormapserie"));
			i.setIdmarcmangueravacio(rs.getInt("idmarcmangueravacio"));
			i.setMangueravacioserie(rs.getString("mangueravacioserie"));
			i.setIdmarcfiltrogas(rs.getInt("idmarcfiltrogas"));
			i.setFiltrogasserie(rs.getString("filtrogasserie"));
			i.setIdmarcrampla(rs.getInt("idmarcrampla"));
			i.setRamplainyectoresserie(rs.getString("ramplainyectoresserie"));
			i.setRamplainyectoresnro(rs.getInt("ramplainyectoresnro"));

			
			return i;
	    }
	}

	public class objInstalacionCilindro implements RowMapper<InstalacionCilindro>{
		@Override
		public InstalacionCilindro mapRow(ResultSet rs, int arg1) throws SQLException {
			InstalacionCilindro ic=new InstalacionCilindro();
			ic.setIdmarccil(rs.getInt("idmarccil"));
			ic.setIdcapcil(rs.getInt("idcapcil"));
			ic.setSerie(rs.getString("serie"));
			ic.setFechaFab(rs.getString("fechaFab"));;
			ic.setIdregistroKit(rs.getInt("idregistroKit"));
			try {
				ic.setMarcaCilindro(metMarcaCilindro(rs.getInt("idmarccil")));
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println(e.getMessage());
				ic.setMarcaCilindro(null);
			}
			try {
				ic.setCapacidadCilindro(metCapacidadCilindro(rs.getInt("idcapcil")));
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println(e.getMessage());
				ic.setCapacidadCilindro(null);
			}
			
			
			ic.setInmetro(rs.getString("inmetro"));
			
			try {
				ic.setMarcaValvulaCilindro(metMarcaVavulaCilindro(rs.getInt("idmarcvalcil")));
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println(e.getMessage());
				ic.setMarcaValvulaCilindro(null);
			}
			ic.setSerievalvula(rs.getString("serievalvula"));
			return ic;
		}
	}
	/*DESMONTAJE*/
	@Transactional
	public Taller metTaller(int idtall){
		return this.db.queryForObject("select * from taller where idtall=?", new objTaller(),idtall);
	}
	@Transactional
	public RegistroKit metRegistroKit(int id){
		return this.db.queryForObject("select * from registroKit where idregistroKit=?", new objRegistroKit(),id);
	}
	@Transactional
	/* FIN DESMONTAJE*/
	public Reductor metReductor(int idreduc){
		return this.db.queryForObject("select DISTINCT r.* from reductor r,marcareductor mr,registrokit rk where r.idmarcred=mr.idmarcred and rk.idmarcred=mr.idmarcred and r.idmarcred=rk.idmarcred and  rk.idmarcred=?", new objReductor(),idreduc);
	}
	@Transactional
	public implementoskit metImplementosKit(int idregistrokit){
		return this.db.queryForObject("select DISTINCT i.* from implementoskit i,registrokit rk where  rk.idregistrokit=i.idregistrokit and  rk.idregistrokit=?", new objImplementosKit(),idregistrokit);
	}
	@Transactional
	public List<InstalacionCilindro>  metListCilindros(int idregistroKit){
		return this.db.query("select * from instalacioncilindro where idregistrokit=?", new objInstalacionCilindro(),idregistroKit);
	}
	@Transactional
	public OrdenServicio metOrdenServicio(int idord){
		return manejadorServicios.metOrdenServicio(idord);
	}
	
	@Transactional
	public Cilindro metCilindro(int idcil){
		return this.db.queryForObject("select * from cilindro where idcil=?", new objCilindro(),idcil);
	}
	@Transactional
	public Cilindro metCilindroByMarca(int idmarccil){
		return this.db.queryForObject("select * from cilindro where idcil=?", new objCilindro(),idmarccil);
	}
	@Transactional
	public MarcaCilindro metMarcaCilindro(int idmarccil){
		return this.db.queryForObject("select * from marcaCilindro where idmarccil=?", new objMarcaCilindro(),idmarccil);
	}
	@Transactional
	public CapacidadCilindro metCapacidadCilindro(int idcapcil){
		return this.db.queryForObject("select * from capacidadcilindro where idcapcil=?", new objCapacidadCilindro(),idcapcil);
	}
	@Transactional
	public MarcaValvulaCilindro metMarcaVavulaCilindro(int idmarcvalcil){
		return this.db.queryForObject("select * from marcavalvulacilindro where idmarcvalcil=?", new objMarcaValvulaCilindro(),idmarcvalcil);
	}
	@Transactional
	public MarcaReductor metMarcaReductor(int idmarcred){
		return this.db.queryForObject("select * from marcaReductor where idmarcred=?", new objMarcaReductor(),idmarcred);
	}
	@Transactional
	public List<RegistroKit> Lista(HttpServletRequest req){
		String filtro=req.getParameter("filtro");
		//int estado=Integer.parseInt(req.getParameter("estado"));
		String sql="select r.* from registroKit r join ordenServicio os on r.idordserv=os.idordserv JOIN solicitud s ON s.idsolt=os.idsolt  WHERE os.numords LIKE ? or s.numSolt LIKE ? ORDER BY r.idregistroKit ASC";
		return this.db.query(sql, new objRegistroKit(),"%"+filtro+"%","%"+filtro+"%");
	}
//	@Transactional
	public List<RegistroKit> listar_fichas_d(int start,int estado,String search,int length,int idtall,int anio,int numord,String numci,String login){
		String sql="";
		try {
			Integer tot=Constantes.NUM_MAX_DATATABLE;
			
			sql="select DISTINCT r.*,row_number() OVER(ORDER BY r.idregistrokit) as RN,? as tot\r\n"
					+ "from registroKit r\r\n"
					+ "JOIN marcareductor mr ON mr.idmarcred=r.idmarcred\r\n"
					+ "join ordenServicio os on r.idordserv=os.idordserv\r\n"
					+ "JOIN solicitud s ON s.idsolt=os.idsolt\r\n"
					+ "JOIN benVehSolt bvs ON bvs.idsolt=s.idsolt\r\n"
					+ "JOIN vehiculo v on bvs.placa=v.placa\r\n"
					+ "JOIN beneficiario b ON b.idben=bvs.idben AND bvs.perteneceSiNo=1\r\n"
					+ "JOIN persona per ON per.idper=b.idper\r\n"
					+ "WHERE (os.numords=? or ?=-1)\r\n"
					+ "AND upper(v.placa) like concat('%',upper(?),'%')\r\n"
					+ "AND upper(per.ci) like concat('%',upper(?),'%')\r\n"
					+ "AND r.anio=?\r\n"
					+ "AND r.idtall=?\r\n"
					+ "AND r.login=?\r\n"
					+ "AND (os.instaladosino=? OR ?=-1)\r\n"
					+ "ORDER BY r.idregistroKit ASC\r\n"
					+ "limit " +tot;

			return db.query(sql,new objRegistroKit(),tot,numord,numord,search,numci,anio,idtall,login,estado,estado);
		} catch (Exception e) {
			e.printStackTrace();    
			System.out.println(e.getMessage());
			return null;
		}
	}
	@Transactional
	public int getAccionKI(){
		String sql="SELECT idacc FROM accion WHERE tipo='ki'";
		return this.db.queryForObject(sql,Integer.class);
	}
	@Transactional
	public int getIdSoltByOS(int idOS){
		String sql="SELECT s.idsolt FROM ordenServicio os,solicitud s WHERE s.idsolt=os.idsolt AND os.idordserv=?";
		return this.db.queryForObject(sql,Integer.class,idOS);
	}
	@Transactional
	public Object[] registrar(HttpServletRequest req,Persona xuser) {

		String gestion_s =req.getParameter("gestion");
		int gestion=Integer.parseInt(gestion_s);
		System.out.println("Gestion:"+gestion);
		
		String idtall_s =req.getParameter("idtall_user");
		System.out.println("idtall_s:"+idtall_s);
		System.out.println("idtall_s:"+idtall_s==null);
		System.out.println("idtall_s:"+idtall_s.isEmpty());
//		System.out.println("idtall_s:"+idtall_s=="");
		int idtall=0;
		if(idtall_s!=null) {
			idtall=Integer.parseInt(idtall_s);	
		}
		
		
		
		String sql="";
		Object [] resp=new Object[2];
		int idRegistroKit=idRegistroKit();
		
		String login=xuser.getUsuario().getLogin();
		String idOrdServ=req.getParameter("idordserv");
		int idOS=Integer.parseInt(idOrdServ);
		String idReduc=req.getParameter("idmarcred");
		int idR=Integer.parseInt(idReduc);
		String FechaConversion=req.getParameter("fechaConversion");
//		String FechaInstalacion=req.getParameter("fechaInstalacion");
		String DesintalacionKitGlp=req.getParameter("desintalacionKitGlp");
		
		System.out.println("DesintalacionKitGlp: "+DesintalacionKitGlp);
		int DestKit;
		if(DesintalacionKitGlp==null) {
			DestKit=-1;
		}else {
			
			DestKit=Integer.parseInt(DesintalacionKitGlp);
		}
	
		String SerieRegulador=req.getParameter("serieRegulador");
		String CertificadoKit=req.getParameter("certificadoKit");
		String fechaCertificado=req.getParameter("fechaCertificado");
		String SerieMotor=req.getParameter("serieMotor");
		String codigoBSISA=req.getParameter("codigoBSISA");
		codigoBSISA=codigoBSISA.trim();
		String codigoBSISAC=req.getParameter("codigoBSISAC");

		System.out.println("idOrdServ: "+idOrdServ+" idReduc: "+idReduc+" FechaConversion: "+FechaConversion+" DesintalacionKitGlp: "+DesintalacionKitGlp+" SerieRegulador: "+SerieRegulador+" CertificadoKit: "+CertificadoKit+" SerieMotor: "+SerieMotor);
		String[] series=req.getParameterValues("serie[]");
		String[] cilindros=req.getParameterValues("cilindro[]");
		String[] capacidades=req.getParameterValues("capacidad[]");
		String[] fechaFab=req.getParameterValues("fechaFab[]");
		
		String[] inmetrosCilindros=req.getParameterValues("nroinmetrocil[]");
		String[] marcaRegulador=req.getParameterValues("marcavalvulacil[]");
		String[] serieValvula=req.getParameterValues("nroserievalvula[]");
		
		System.out.println("fechaCertificado:"+fechaCertificado);
		
		boolean serie_datos=true;
		boolean cilindros_datos=true;
		boolean capacidades_datos=true;
		boolean fechafab_datos=true;
		
		boolean inmetros_cilindros_datos=true;
		boolean marca_regulador_datos=true;
		boolean serie_regulador_datos=true;
		
		for (String i : series) {
			System.out.println("serie : "+i);
			if(i=="")serie_datos=false;
		}
		for (String i : cilindros) {
			System.out.println("codcil: "+i);
			if(i=="")cilindros_datos=false;
		}
		for (String i : capacidades) {
			System.out.println("codcap: "+i);
			if(i=="")capacidades_datos=false;
		}
		for (String i : fechaFab) {
			System.out.println("FechaFab: "+i);
			if(i=="")fechafab_datos=false;
		}
		for (String i : inmetrosCilindros) {
			System.out.println("marcaRegulador: "+i);
			if(i=="")inmetros_cilindros_datos=false;
		}
		for (String i : marcaRegulador) {
			System.out.println("marcaRegulador: "+i);
			if(i=="")marca_regulador_datos=false;
		}
		for (String i : serieValvula) {
			System.out.println("serieRegulador: "+i);
			if(i=="")serie_regulador_datos=false;
		}
		
		if(!codigoBSISAC.equals("")) {
			codigoBSISA+="-"+codigoBSISAC;
		}
		System.out.println("codigoBSISA_TOT:"+codigoBSISA);
		
		//*******************************************************************************************implementos kit
		int idimple=idImplementosKit(); 
		
		String observacioneskit=req.getParameter("observacioneskit");
		String primeramedicion=req.getParameter("primeramedicion");
		String segundamedicion=req.getParameter("segundamedicion");
		String conformeestandasino=req.getParameter("conformeestandasino");

		System.out.println("conformeestandasino: "+conformeestandasino);
		int conformeestandasinoentero;
		if(conformeestandasino==null) {
			conformeestandasinoentero=-1;
		}else {
			
			conformeestandasinoentero=Integer.parseInt(conformeestandasino);
		}
		System.out.println("conformeestandasinoentero: "+conformeestandasinoentero);
		
		String cunacilindro=req.getParameter("cunacilindro");
		
		System.out.println("cunacilindro: "+cunacilindro);
		int cunacilindroentero;
		if(cunacilindro==null) {
			cunacilindroentero=-1;
		}else {
			
			cunacilindroentero=Integer.parseInt(cunacilindro);
		}

		String dosificadorsino=req.getParameter("dosificadorsino");		
		System.out.println("dosificadorsino: "+dosificadorsino);
		int dosificadorsinoentero;
		if(dosificadorsino==null) {
			dosificadorsinoentero=-1;
		}else {
			
			dosificadorsinoentero=Integer.parseInt(dosificadorsino);
		}

		String sistventeosino=req.getParameter("sistventeosino");		
		System.out.println("dosificadorsino: "+dosificadorsino);
		int sistventeosinoentero;
		if(sistventeosino==null) {
			sistventeosinoentero=-1;
		}else {
			
			sistventeosinoentero=Integer.parseInt(sistventeosino);
		}
		
		String memoriaserie=req.getParameter("memoriaserie");

		String sensormapserie=req.getParameter("sensormapserie");
		
		String ramplainyectoresnro=req.getParameter("ramplainyectoresnro");
		System.out.println("ramplainyectoresnro: "+ramplainyectoresnro);
		Integer ramplainyectoresnroentero;
		if(ramplainyectoresnro==null || ramplainyectoresnro=="") {
			ramplainyectoresnroentero=null;
		}else {
			
			ramplainyectoresnroentero=Integer.parseInt(ramplainyectoresnro);
		}
		
		
		try {
			if(series.length>0 && cilindros.length>0 && capacidades.length>0 && fechaFab.length>0) {
				if(serie_datos=true && cilindros_datos==true && capacidades_datos==true && fechafab_datos==true) {
					sql="INSERT INTO registroKit(idregistroKit,idordserv,fechaconversion,desinstaladoKitGlpSiNo,idmarcred,numSerieRegulador,certificadoKit,numSerieMotor,codigoBSISA,fechaCertificado,login,anio,idtall,observacioneskit,primeramedicion,segundamedicion,conformeestandasino) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
					this.db.update(sql,idRegistroKit,idOS,FechaConversion,DestKit,idR,SerieRegulador,CertificadoKit,SerieMotor,codigoBSISA,fechaCertificado,login,gestion,idtall,observacioneskit,primeramedicion,segundamedicion,conformeestandasinoentero);
					
					String sql1="INSERT INTO instalacionCilindro(idmarccil,serie,idcapcil,fechaFab,inmetro,idmarcvalcil,serievalvula,idregistroKit,idinstcil) VALUES(?,?,?,?,?,?,?,?,?)";
					for (int i = 0; i < cilindros.length; i++) {
						if(cilindros[i]!="" && series[i]!="" && capacidades[i]!="" && fechaFab[i]!="") {
							int idinstcil=idInstalacionCilindro();
							this.db.update(sql1,Integer.parseInt(cilindros[i]),series[i],Integer.parseInt(capacidades[i]),fechaFab[i],inmetrosCilindros[i],Integer.parseInt(marcaRegulador[i]),serieValvula[i],idRegistroKit,idinstcil);
						}
						
					}
					
					sql="INSERT INTO implementoskit(idimple,idregistroKit,cunacilindro,dosificadorsino,sistventeosino,memoriaserie,sensormapserie,ramplainyectoresnro) VALUES(?,?,?,?,?,?,?,?)";
					this.db.update(sql,
							idimple,
							idRegistroKit,
							cunacilindroentero,
							dosificadorsinoentero,
							sistventeosinoentero,
							memoriaserie.toUpperCase(),
							sensormapserie.toUpperCase(),
							ramplainyectoresnroentero
							);
					
					
					
					this.db.update("UPDATE ordenServicio SET instaladoSiNo=1 WHERE idordserv=?",idOS);

					sql="UPDATE solicitud SET idacc=? WHERE idsolt=?";
					this.db.update(sql,getAccionKI(),getIdSoltByOS(idOS));
					
					resp[0]=true;
				}else {
					resp[0]=false;
				}
				
			}else {
				resp[0]=false;
			}
			resp[1]=idRegistroKit;
			return resp;
		}catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
			resp[0]=false;
			return resp;
		}
	}
	@Transactional
	public Object[] registrar_bck(HttpServletRequest req,Persona xuser) {

		String gestion_s =req.getParameter("gestion");
		int gestion=Integer.parseInt(gestion_s);
		System.out.println("Gestion:"+gestion);
		
		String idtall_s =req.getParameter("idtall_user");
		System.out.println("idtall_s:"+idtall_s);
		System.out.println("idtall_s:"+idtall_s==null);
		System.out.println("idtall_s:"+idtall_s.isEmpty());
//		System.out.println("idtall_s:"+idtall_s=="");
		int idtall=0;
		if(idtall_s!=null) {
			idtall=Integer.parseInt(idtall_s);	
		}
		
		
		
		String sql="";
		Object [] resp=new Object[2];
		int idRegistroKit=idRegistroKit();
		int idinstcil=idInstalacionCilindro();
		String login=xuser.getUsuario().getLogin();
		String idOrdServ=req.getParameter("idordserv");
		int idOS=Integer.parseInt(idOrdServ);
		String idReduc=req.getParameter("idmarcred");
		int idR=Integer.parseInt(idReduc);
		String FechaConversion=req.getParameter("fechaConversion");
//		String FechaInstalacion=req.getParameter("fechaInstalacion");
		String DesintalacionKitGlp=req.getParameter("desintalacionKitGlp");
		
		System.out.println("DesintalacionKitGlp: "+DesintalacionKitGlp);
		int DestKit;
		if(DesintalacionKitGlp==null) {
			DestKit=-1;
		}else {
			
			DestKit=Integer.parseInt(DesintalacionKitGlp);
		}
	
//		String CertificadoDesinstacion=req.getParameter("CertificadoDesinstacion");
//		int CerDes;
//		if (CertificadoDesinstacion==null) {
//			CerDes=-1;
//		} else {
//			CerDes=Integer.parseInt(CertificadoDesinstacion);
//		}
		String SerieRegulador=req.getParameter("serieRegulador");
		String CertificadoKit=req.getParameter("certificadoKit");
		String fechaCertificado=req.getParameter("fechaCertificado");
		String SerieMotor=req.getParameter("serieMotor");
		String codigoBSISA=req.getParameter("codigoBSISA");
		codigoBSISA=codigoBSISA.trim();
		String codigoBSISAC=req.getParameter("codigoBSISAC");

		System.out.println("idOrdServ: "+idOrdServ+" idReduc: "+idReduc+" FechaConversion: "+FechaConversion+" DesintalacionKitGlp: "+DesintalacionKitGlp+" SerieRegulador: "+SerieRegulador+" CertificadoKit: "+CertificadoKit+" SerieMotor: "+SerieMotor);
		String[] series=req.getParameterValues("serie[]");
		String[] cilindros=req.getParameterValues("cilindro[]");
		String[] capacidades=req.getParameterValues("capacidad[]");
		String[] fechaFab=req.getParameterValues("fechaFab[]");
		
		String[] inmetrosCilindros=req.getParameterValues("nroinmetrocil[]");
		String[] marcaRegulador=req.getParameterValues("marcavalvulacil[]");
		String[] serieValvula=req.getParameterValues("nroserievalvula[]");
		
		System.out.println("fechaCertificado:"+fechaCertificado);
		
		boolean serie_datos=true;
		boolean cilindros_datos=true;
		boolean capacidades_datos=true;
		boolean fechafab_datos=true;
		
		boolean inmetros_cilindros_datos=true;
		boolean marca_regulador_datos=true;
		boolean serie_regulador_datos=true;
		
		for (String i : series) {
			System.out.println("serie : "+i);
			if(i=="")serie_datos=false;
		}
		for (String i : cilindros) {
			System.out.println("codcil: "+i);
			if(i=="")cilindros_datos=false;
		}
		for (String i : capacidades) {
			System.out.println("codcap: "+i);
			if(i=="")capacidades_datos=false;
		}
		for (String i : fechaFab) {
			System.out.println("FechaFab: "+i);
			if(i=="")fechafab_datos=false;
		}
		for (String i : inmetrosCilindros) {
			System.out.println("marcaRegulador: "+i);
			if(i=="")inmetros_cilindros_datos=false;
		}
		for (String i : marcaRegulador) {
			System.out.println("marcaRegulador: "+i);
			if(i=="")marca_regulador_datos=false;
		}
		for (String i : serieValvula) {
			System.out.println("serieRegulador: "+i);
			if(i=="")serie_regulador_datos=false;
		}
		
		if(!codigoBSISAC.equals("")) {
			codigoBSISA+="-"+codigoBSISAC;
		}
		System.out.println("codigoBSISA_TOT:"+codigoBSISA);
		
		//*******************************************************************************************implementos kit
		int idimple=idImplementosKit(); 
		
		String observacioneskit=req.getParameter("observacioneskit");
		String primeramedicion=req.getParameter("primeramedicion");
		String segundamedicion=req.getParameter("segundamedicion");
		String conformeestandasino=req.getParameter("conformeestandasino");

		System.out.println("conformeestandasino: "+conformeestandasino);
		int conformeestandasinoentero;
		if(conformeestandasino==null) {
			conformeestandasinoentero=-1;
		}else {
			
			conformeestandasinoentero=Integer.parseInt(conformeestandasino);
		}
		System.out.println("conformeestandasinoentero: "+conformeestandasinoentero);
		
		String cunacilindro=req.getParameter("cunacilindro");
		
		System.out.println("cunacilindro: "+cunacilindro);
		int cunacilindroentero;
		if(cunacilindro==null) {
			cunacilindroentero=-1;
		}else {
			
			cunacilindroentero=Integer.parseInt(cunacilindro);
		}
		String idmarccanieria=req.getParameter("idmarccanieria");
		int idmarccanieria_int=Integer.parseInt(idmarccanieria);
//		String canieriaaltapresionserie=req.getParameter("canieriaaltapresionserie");
		
		String idmarcelecgaso=req.getParameter("idmarcelecgaso");
		int idmarcelecgaso_int=Integer.parseInt(idmarcelecgaso);
//		String serieelectrgasolina=req.getParameter("serieelectrgasolina");

		String idmarcelecgas=req.getParameter("idmarcelecgas");
		int idmarcelecgas_int=Integer.parseInt(idmarcelecgas);
//		String serieelectrgnv=req.getParameter("serieelectrgnv");
		
		String dosificadorsino=req.getParameter("dosificadorsino");		
		System.out.println("dosificadorsino: "+dosificadorsino);
		int dosificadorsinoentero;
		if(dosificadorsino==null) {
			dosificadorsinoentero=-1;
		}else {
			
			dosificadorsinoentero=Integer.parseInt(dosificadorsino);
		}
		

		String idmarconm=req.getParameter("idmarconm");
		int idmarconm_int=Integer.parseInt(idmarconm);
//		String seriellaveconmutadora=req.getParameter("seriellaveconmutadora");
		
		String sistventeosino=req.getParameter("sistventeosino");		
		System.out.println("dosificadorsino: "+dosificadorsino);
		int sistventeosinoentero;
		if(sistventeosino==null) {
			sistventeosinoentero=-1;
		}else {
			
			sistventeosinoentero=Integer.parseInt(sistventeosino);
		}
		
		String idmarcvalcargint=req.getParameter("idmarcvalcargint");
		int idmarcvalcargint_int=Integer.parseInt(idmarcvalcargint);
//		String serievalvulacargainterna=req.getParameter("serievalvulacargainterna");
		
		String idmarcvalcargext=req.getParameter("idmarcvalcargext");
		int idmarcvalcargext_int=Integer.parseInt(idmarcvalcargext);
//		String serievalvulacargaexterna=req.getParameter("serievalvulacargaexterna");
		
		String idmarcmangueragas=req.getParameter("idmarcmangueragas");
		int idmarcmangueragas_int=Integer.parseInt(idmarcmangueragas);
//		String seriemangueragas=req.getParameter("seriemangueragas");
		
		String idmarcmangueraagua=req.getParameter("idmarcmangueraagua");
		int idmarcmangueraagua_int=Integer.parseInt(idmarcmangueraagua);
//		String mangueraaguaserie=req.getParameter("mangueraaguaserie");
		
		String idmarcemu=req.getParameter("idmarcemu");
		int idmarcemu_int=Integer.parseInt(idmarcemu);
//		String serieemulador=req.getParameter("serieemulador");
		
		String idmarcmano=req.getParameter("idmarcmano");
		int idmarcmano_int=Integer.parseInt(idmarcmano);
//		String seriemanometro=req.getParameter("seriemanometro");
		
		String idmarcmem=req.getParameter("idmarcmem");
		int idmarcmem_int=Integer.parseInt(idmarcmem);
		String memoriaserie=req.getParameter("memoriaserie");
		
		String idmarcsensormap=req.getParameter("idmarcsensormap");
		int idmarcsensormap_int=Integer.parseInt(idmarcsensormap);
		String sensormapserie=req.getParameter("sensormapserie");
		
		String idmarcmangueravacio=req.getParameter("idmarcmangueravacio");
		int idmarcmangueravacio_int=Integer.parseInt(idmarcmangueravacio);
//		String mangueravacioserie=req.getParameter("mangueravacioserie");
		
		String idmarcfiltrogas=req.getParameter("idmarcfiltrogas");
		int idmarcfiltrogas_int=Integer.parseInt(idmarcfiltrogas);
//		String filtrogasserie=req.getParameter("filtrogasserie");
		
		String idmarcrampla=req.getParameter("idmarcrampla");
		int idmarcrampla_int=Integer.parseInt(idmarcrampla);
//		String ramplainyectoresserie=req.getParameter("ramplainyectoresserie");

		String ramplainyectoresnro=req.getParameter("ramplainyectoresnro");
		System.out.println("ramplainyectoresnro: "+ramplainyectoresnro);
		int ramplainyectoresnroentero;
		if(ramplainyectoresnro==null) {
			ramplainyectoresnroentero=-1;
		}else {
			
			ramplainyectoresnroentero=Integer.parseInt(ramplainyectoresnro);
		}
		
		
		try {
			if(series.length>0 && cilindros.length>0 && capacidades.length>0 && fechaFab.length>0) {
				if(serie_datos=true && cilindros_datos==true && capacidades_datos==true && fechafab_datos==true) {
					sql="INSERT INTO registroKit(idregistroKit,idordserv,fechaconversion,desinstaladoKitGlpSiNo,idmarcred,numSerieRegulador,certificadoKit,numSerieMotor,codigoBSISA,fechaCertificado,login,anio,idtall,observacioneskit,primeramedicion,segundamedicion,conformeestandasino) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
					this.db.update(sql,idRegistroKit,idOS,FechaConversion,DestKit,idR,SerieRegulador,CertificadoKit,SerieMotor,codigoBSISA,fechaCertificado,login,gestion,idtall,observacioneskit,primeramedicion,segundamedicion,conformeestandasinoentero);
					
					String sql1="INSERT INTO instalacionCilindro(idmarccil,serie,idcapcil,fechaFab,inmetro,idmarcvalcil,serievalvula,idregistroKit,idinstcil) VALUES(?,?,?,?,?,?,?,?,?)";
					for (int i = 0; i < cilindros.length; i++) {
						if(cilindros[i]!="" && series[i]!="" && capacidades[i]!="" && fechaFab[i]!="") {
							this.db.update(sql1,Integer.parseInt(cilindros[i]),series[i],Integer.parseInt(capacidades[i]),fechaFab[i],inmetrosCilindros[i],Integer.parseInt(marcaRegulador[i]),serieValvula[i],idRegistroKit,idinstcil);
						}
						
					}
					
					sql="INSERT INTO implementoskit(idimple,idregistroKit,cunacilindro,idmarcelecgaso,idmarcelecgas,dosificadorsino,sistventeosino,idmarconm,idmarcvalcargint,idmarcvalcargext,idmarcmangueragas,idmarcemu,idmarcmano,idmarccanieria,idmarcmangueraagua,idmarcmem,memoriaserie,idmarcsensormap,sensormapserie,idmarcmangueravacio,idmarcfiltrogas,idmarcrampla,ramplainyectoresnro) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
					this.db.update(sql,idimple,
							idRegistroKit,
							cunacilindroentero,
							idmarcelecgaso_int,
//							serieelectrgasolina.toUpperCase(),
							idmarcelecgas_int,
//							serieelectrgnv.toUpperCase(),
							dosificadorsinoentero,
							sistventeosinoentero,
							idmarconm_int,
//							seriellaveconmutadora.toUpperCase(),
							idmarcvalcargint_int,
//							serievalvulacargainterna.toUpperCase(),
							idmarcvalcargext_int,
//							serievalvulacargaexterna.toUpperCase(),
							idmarcmangueragas_int,
//							seriemangueragas.toUpperCase(),
							idmarcemu_int,
//							serieemulador.toUpperCase(),
							idmarcmano_int,
//							seriemanometro.toUpperCase(),
							idmarccanieria_int,
//							canieriaaltapresionserie.toUpperCase(),
							idmarcmangueraagua_int,
//							mangueraaguaserie.toUpperCase(),
							idmarcmem_int,
							memoriaserie.toUpperCase(),
							idmarcsensormap_int,
							sensormapserie.toUpperCase(),
							idmarcmangueravacio_int,
//							mangueravacioserie.toUpperCase(),
							idmarcfiltrogas_int,
//							filtrogasserie.toUpperCase(),
							idmarcrampla_int,
//							ramplainyectoresserie.toUpperCase(),
							ramplainyectoresnroentero
							);
					
					
					
					this.db.update("UPDATE ordenServicio SET instaladoSiNo=1 WHERE idordserv=?",idOS);

					sql="UPDATE solicitud SET idacc=? WHERE idsolt=?";
					this.db.update(sql,getAccionKI(),getIdSoltByOS(idOS));
					
					resp[0]=true;
				}else {
					resp[0]=false;
				}
				
			}else {
				resp[0]=false;
			}
			resp[1]=idRegistroKit;
			return resp;
		}catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
			resp[0]=false;
			return resp;
		}
	}
	@Transactional
	public boolean modificarconversionbck(HttpServletRequest req,Persona xuser){
		
		String gestion_s =req.getParameter("gestion");
		int gestion=Integer.parseInt(gestion_s);
		System.out.println("Gestion:"+gestion);
		
		String idtall_s =req.getParameter("idtall_user");
		System.out.println("idtall_s:"+idtall_s);
		System.out.println("idtall_s:"+idtall_s==null);
		System.out.println("idtall_s:"+idtall_s.isEmpty());
//		System.out.println("idtall_s:"+idtall_s=="");
		int idtall=0;
		if(idtall_s!=null) {
			idtall=Integer.parseInt(idtall_s);	
		}
		
		
		
		String sql="";
//		Object [] resp=new Object[2];
		boolean resp=false;
		int idRegistroKit=Integer.parseInt(req.getParameter("idregistroKit"));
		
		
		
		String login=xuser.getUsuario().getLogin();
		String idOrdServ=req.getParameter("idordserv");
		int idOS=Integer.parseInt(idOrdServ);
		String idReduc=req.getParameter("idmarcred");
		int idR=Integer.parseInt(idReduc);

		
		String SerieMotor=req.getParameter("numseriemotor");
		String DesintalacionKitGlp=req.getParameter("desinstaladokitglpsino");
		System.out.println("DesintalacionKitGlp: "+DesintalacionKitGlp);
		int DestKit;
		if(DesintalacionKitGlp==null) {
			DestKit=-1;
		}else {
			
			DestKit=Integer.parseInt(DesintalacionKitGlp);
		}
		
		String FechaConversion=req.getParameter("fechaconversion");
//		String FechaInstalacion=req.getParameter("fechaInstalacion");
		String codigoBSISA=req.getParameter("codigobsisa");
		codigoBSISA=codigoBSISA.trim();
		String codigoBSISAC=req.getParameter("codigoBSISAC");
		if(!codigoBSISAC.equals("")) {
			codigoBSISA+="-"+codigoBSISAC;
		}
		System.out.println("codigoBSISA_TOT:"+codigoBSISA);
		
		
		String SerieRegulador=req.getParameter("numserieregulador");
		String fechaCertificado=req.getParameter("fechacertificado");
		String CertificadoKit=req.getParameter("certificadokit");
		System.out.println("idOrdServ: "+idOrdServ+" idReduc: "+idReduc+" FechaConversion: "+FechaConversion+" DesintalacionKitGlp: "+DesintalacionKitGlp+" SerieRegulador: "+SerieRegulador+" CertificadoKit: "+CertificadoKit+" SerieMotor: "+SerieMotor);
		
		String[] series=req.getParameterValues("serie[]");
		String[] cilindros=req.getParameterValues("cilindro[]");
		String[] capacidades=req.getParameterValues("capacidad[]");
		String[] fechaFab=req.getParameterValues("fechaFab[]");
		
		String[] inmetrosCilindros=req.getParameterValues("nroinmetrocil[]");
		String[] marcaRegulador=req.getParameterValues("marcavalvulacil[]");
		String[] serieValvula=req.getParameterValues("nroserievalvula[]");
		
		
		System.out.println("fechaCertificado:"+fechaCertificado);
		
		boolean serie_datos=true;
		boolean cilindros_datos=true;
		boolean capacidades_datos=true;
		boolean fechafab_datos=true;
		
		
		boolean inmetros_cilindros_datos=true;
		boolean marca_regulador_datos=true;
		boolean serie_regulador_datos=true;
		
		for (String i : series) {
			System.out.println("serie : "+i);
			if(i=="")serie_datos=false;
		}
		for (String i : cilindros) {
			System.out.println("codcil: "+i);
			if(i=="")cilindros_datos=false;
		}
		for (String i : capacidades) {
			System.out.println("codcap: "+i);
			if(i=="")capacidades_datos=false;
		}
		for (String i : fechaFab) {
			System.out.println("FechaFab: "+i);
			if(i=="")fechafab_datos=false;
		}
		
		for (String i : inmetrosCilindros) {
			System.out.println("marcaRegulador: "+i);
			if(i=="")inmetros_cilindros_datos=false;
		}
		for (String i : marcaRegulador) {
			System.out.println("marcaRegulador: "+i);
			if(i=="")marca_regulador_datos=false;
		}
		for (String i : serieValvula) {
			System.out.println("serieRegulador: "+i);
			if(i=="")serie_regulador_datos=false;
		}
		
		
		//*******************************************************************************************implementos kit
//		int idimple=idImplementosKit(); 
		
		
		String observacioneskit=req.getParameter("observacioneskit");
		String primeramedicion=req.getParameter("primeramedicion");
		String segundamedicion=req.getParameter("segundamedicion");
		String conformeestandasino=req.getParameter("conformeestandasino");

		System.out.println("conformeestandasino: "+conformeestandasino);
		int conformeestandasinoentero;
		if(conformeestandasino==null) {
			conformeestandasinoentero=-1;
		}else {
			
			conformeestandasinoentero=Integer.parseInt(conformeestandasino);
		}
		System.out.println("conformeestandasinoentero: "+conformeestandasinoentero);
		
		String cunacilindro=req.getParameter("cunacilindro");
		
		System.out.println("cunacilindro: "+cunacilindro);
		int cunacilindroentero;
		if(cunacilindro==null) {
			cunacilindroentero=-1;
		}else {
			
			cunacilindroentero=Integer.parseInt(cunacilindro);
		}
		String idmarccanieria=req.getParameter("idmarccanieria");
		int idmarccanieria_int=Integer.parseInt(idmarccanieria);
//		String canieriaaltapresionserie=req.getParameter("canieriaaltapresionserie");
		
		String idmarcelecgaso=req.getParameter("idmarcelecgaso");
		int idmarcelecgaso_int=Integer.parseInt(idmarcelecgaso);
//		String serieelectrgasolina=req.getParameter("serieelectrgasolina");

		String idmarcelecgas=req.getParameter("idmarcelecgas");
		int idmarcelecgas_int=Integer.parseInt(idmarcelecgas);
//		String serieelectrgnv=req.getParameter("serieelectrgnv");
		
		String dosificadorsino=req.getParameter("dosificadorsino");		
		System.out.println("dosificadorsino: "+dosificadorsino);
		int dosificadorsinoentero;
		if(dosificadorsino==null) {
			dosificadorsinoentero=-1;
		}else {
			
			dosificadorsinoentero=Integer.parseInt(dosificadorsino);
		}
		

		String idmarconm=req.getParameter("idmarconm");
		int idmarconm_int=Integer.parseInt(idmarconm);
//		String seriellaveconmutadora=req.getParameter("seriellaveconmutadora");
		
		String sistventeosino=req.getParameter("sistventeosino");		
		System.out.println("dosificadorsino: "+dosificadorsino);
		int sistventeosinoentero;
		if(sistventeosino==null) {
			sistventeosinoentero=-1;
		}else {
			
			sistventeosinoentero=Integer.parseInt(sistventeosino);
		}
		
		String idmarcvalcargint=req.getParameter("idmarcvalcargint");
		int idmarcvalcargint_int=Integer.parseInt(idmarcvalcargint);
//		String serievalvulacargainterna=req.getParameter("serievalvulacargainterna");
		
		String idmarcvalcargext=req.getParameter("idmarcvalcargext");
		int idmarcvalcargext_int=Integer.parseInt(idmarcvalcargext);
//		String serievalvulacargaexterna=req.getParameter("serievalvulacargaexterna");
		
		String idmarcmangueragas=req.getParameter("idmarcmangueragas");
		int idmarcmangueragas_int=Integer.parseInt(idmarcmangueragas);
//		String seriemangueragas=req.getParameter("seriemangueragas");
		
		String idmarcmangueraagua=req.getParameter("idmarcmangueraagua");
		int idmarcmangueraagua_int=Integer.parseInt(idmarcmangueraagua);
//		String mangueraaguaserie=req.getParameter("mangueraaguaserie");
		
		String idmarcemu=req.getParameter("idmarcemu");
		int idmarcemu_int=Integer.parseInt(idmarcemu);
//		String serieemulador=req.getParameter("serieemulador");
		
		String idmarcmano=req.getParameter("idmarcmano");
		int idmarcmano_int=Integer.parseInt(idmarcmano);
//		String seriemanometro=req.getParameter("seriemanometro");
		
		String idmarcmem=req.getParameter("idmarcmem");
		int idmarcmem_int=Integer.parseInt(idmarcmem);
		String memoriaserie=req.getParameter("memoriaserie");
		
		String idmarcsensormap=req.getParameter("idmarcsensormap");
		int idmarcsensormap_int=Integer.parseInt(idmarcsensormap);
		String sensormapserie=req.getParameter("sensormapserie");
		
		String idmarcmangueravacio=req.getParameter("idmarcmangueravacio");
		int idmarcmangueravacio_int=Integer.parseInt(idmarcmangueravacio);
//		String mangueravacioserie=req.getParameter("mangueravacioserie");
		
		String idmarcfiltrogas=req.getParameter("idmarcfiltrogas");
		int idmarcfiltrogas_int=Integer.parseInt(idmarcfiltrogas);
//		String filtrogasserie=req.getParameter("filtrogasserie");
		
		String idmarcrampla=req.getParameter("idmarcrampla");
		int idmarcrampla_int=Integer.parseInt(idmarcrampla);
//		String ramplainyectoresserie=req.getParameter("ramplainyectoresserie");

		String ramplainyectoresnro=req.getParameter("ramplainyectoresnro");
		System.out.println("ramplainyectoresnro: "+ramplainyectoresnro);
		int ramplainyectoresnroentero;
		if(ramplainyectoresnro==null) {
			ramplainyectoresnroentero=-1;
		}else {
			
			ramplainyectoresnroentero=Integer.parseInt(ramplainyectoresnro);
		}
		


		
		try {
			if(series.length>0 && cilindros.length>0 && capacidades.length>0 && fechaFab.length>0) {
				if(serie_datos=true && cilindros_datos==true && capacidades_datos==true && fechafab_datos==true) {
					sql="UPDATE registrokit SET fechaconversion=?,desinstaladokitglpsino=?,login=?,idordserv=?,idmarcred=?,numserieregulador=?,certificadokit=?,numseriemotor=?,codigobsisa=?,fechaCertificado=?,anio=?,idtall=?,observacioneskit=?,primeramedicion=?,segundamedicion=?,conformeestandasino=? WHERE idregistrokit=?";
					this.db.update(sql,FechaConversion,DestKit,login,idOS,idR,SerieRegulador,CertificadoKit,SerieMotor,codigoBSISA,fechaCertificado,gestion,idtall,observacioneskit,primeramedicion,segundamedicion,conformeestandasinoentero,idRegistroKit);
					
					sql="delete from instalacionCilindro where idregistroKit=?";
					this.db.update(sql,new Object[] {idRegistroKit});
					
					String sql1="INSERT INTO instalacionCilindro(idmarccil,serie,idcapcil,fechaFab,inmetro,idmarcvalcil,serievalvula,idregistroKit,idinstcil) VALUES(?,?,?,?,?,?,?,?,?)";
					for (int i = 0; i < cilindros.length; i++) {
						if(cilindros[i]!="" && series[i]!="" && capacidades[i]!="" && fechaFab[i]!="") {
							int idinstcil=idInstalacionCilindro();
							this.db.update(sql1,Integer.parseInt(cilindros[i]),series[i],Integer.parseInt(capacidades[i]),fechaFab[i],inmetrosCilindros[i],Integer.parseInt(marcaRegulador[i]),serieValvula[i],idRegistroKit,idinstcil);
						}
						
					} 
					

					sql="UPDATE implementoskit SET cunacilindro=?,idmarcelecgaso=?,idmarcelecgas=?,dosificadorsino=?,sistventeosino=?,idmarconm=?,idmarcvalcargint=?,idmarcvalcargext=?,idmarcmangueragas=?,idmarcemu=?,idmarcmano=?,idmarccanieria=?,idmarcmangueraagua=?,idmarcmem=?,memoriaserie=?,idmarcsensormap=?,sensormapserie=?,idmarcmangueravacio=?,idmarcfiltrogas=?,idmarcrampla=?,ramplainyectoresnro=?  WHERE idregistrokit=?";
					this.db.update(sql,
							cunacilindroentero,
							idmarcelecgaso_int,
//							serieelectrgasolina.toUpperCase(),
							idmarcelecgas_int,
//							serieelectrgnv.toUpperCase(),
							dosificadorsinoentero,
							sistventeosinoentero,
							idmarconm_int,
//							seriellaveconmutadora.toUpperCase(),
							idmarcvalcargint_int,
//							serievalvulacargainterna.toUpperCase(),
							idmarcvalcargext_int,
//							serievalvulacargaexterna.toUpperCase(),
							idmarcmangueragas_int,
//							seriemangueragas.toUpperCase(),
							idmarcemu_int,
//							serieemulador.toUpperCase(),
							idmarcmano_int,
//							seriemanometro.toUpperCase(),
							idmarccanieria_int,
//							canieriaaltapresionserie.toUpperCase(),
							idmarcmangueraagua_int,
//							mangueraaguaserie.toUpperCase(),
							idmarcmem_int,
							memoriaserie.toUpperCase(),
							idmarcsensormap_int,
							sensormapserie.toUpperCase(),
							idmarcmangueravacio_int,
//							mangueravacioserie.toUpperCase(),
							idmarcfiltrogas_int,
//							filtrogasserie.toUpperCase(),
							idmarcrampla_int,
//							ramplainyectoresserie.toUpperCase(),
							ramplainyectoresnroentero,
							idRegistroKit
							);
					
					
					
					
					this.db.update("UPDATE ordenServicio SET instaladoSiNo=1 WHERE idordserv=?",idOS);

					sql="UPDATE solicitud SET idacc=? WHERE idsolt=?";
					this.db.update(sql,getAccionKI(),getIdSoltByOS(idOS));
					
					
					
					
					resp=true;
				}else {
					resp=false;
				}
				
			}else {
				resp=false;
			}
			//resp[1]=idrecep;
			return resp;
		}catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
			resp=false;
			return resp;
		}
	
	}
	@Transactional
	public boolean modificarconversion(HttpServletRequest req,Persona xuser){
		
		String gestion_s =req.getParameter("gestion");
		int gestion=Integer.parseInt(gestion_s);
		System.out.println("Gestion:"+gestion);
		
		String idtall_s =req.getParameter("idtall_user");
		System.out.println("idtall_s:"+idtall_s);
		System.out.println("idtall_s:"+idtall_s==null);
		System.out.println("idtall_s:"+idtall_s.isEmpty());
//		System.out.println("idtall_s:"+idtall_s=="");
		int idtall=0;
		if(idtall_s!=null) {
			idtall=Integer.parseInt(idtall_s);	
		}
		
		
		
		String sql="";
//		Object [] resp=new Object[2];
		boolean resp=false;
		int idRegistroKit=Integer.parseInt(req.getParameter("idregistroKit"));
		
		
		
		String login=xuser.getUsuario().getLogin();
		String idOrdServ=req.getParameter("idordserv");
		int idOS=Integer.parseInt(idOrdServ);
		String idReduc=req.getParameter("idmarcred");
		int idR=Integer.parseInt(idReduc);

		
		String SerieMotor=req.getParameter("numseriemotor");
		String DesintalacionKitGlp=req.getParameter("desinstaladokitglpsino");
		System.out.println("DesintalacionKitGlp: "+DesintalacionKitGlp);
		int DestKit;
		if(DesintalacionKitGlp==null) {
			DestKit=-1;
		}else {
			
			DestKit=Integer.parseInt(DesintalacionKitGlp);
		}
		
		String FechaConversion=req.getParameter("fechaconversion");
//		String FechaInstalacion=req.getParameter("fechaInstalacion");
		String codigoBSISA=req.getParameter("codigobsisa");
		codigoBSISA=codigoBSISA.trim();
		String codigoBSISAC=req.getParameter("codigoBSISAC");
		if(!codigoBSISAC.equals("")) {
			codigoBSISA+="-"+codigoBSISAC;
		}
		System.out.println("codigoBSISA_TOT:"+codigoBSISA);
		
		
		String SerieRegulador=req.getParameter("numserieregulador");
		String fechaCertificado=req.getParameter("fechacertificado");
		String CertificadoKit=req.getParameter("certificadokit");
		System.out.println("idOrdServ: "+idOrdServ+" idReduc: "+idReduc+" FechaConversion: "+FechaConversion+" DesintalacionKitGlp: "+DesintalacionKitGlp+" SerieRegulador: "+SerieRegulador+" CertificadoKit: "+CertificadoKit+" SerieMotor: "+SerieMotor);
		
		String[] series=req.getParameterValues("serie[]");
		String[] cilindros=req.getParameterValues("cilindro[]");
		String[] capacidades=req.getParameterValues("capacidad[]");
		String[] fechaFab=req.getParameterValues("fechaFab[]");
		
		String[] inmetrosCilindros=req.getParameterValues("nroinmetrocil[]");
		String[] marcaRegulador=req.getParameterValues("marcavalvulacil[]");
		String[] serieValvula=req.getParameterValues("nroserievalvula[]");
		
		
		System.out.println("fechaCertificado:"+fechaCertificado);
		
		boolean serie_datos=true;
		boolean cilindros_datos=true;
		boolean capacidades_datos=true;
		boolean fechafab_datos=true;
		
		
		boolean inmetros_cilindros_datos=true;
		boolean marca_regulador_datos=true;
		boolean serie_regulador_datos=true;
		
		for (String i : series) {
			System.out.println("serie : "+i);
			if(i=="")serie_datos=false;
		}
		for (String i : cilindros) {
			System.out.println("codcil: "+i);
			if(i=="")cilindros_datos=false;
		}
		for (String i : capacidades) {
			System.out.println("codcap: "+i);
			if(i=="")capacidades_datos=false;
		}
		for (String i : fechaFab) {
			System.out.println("FechaFab: "+i);
			if(i=="")fechafab_datos=false;
		}
		
		for (String i : inmetrosCilindros) {
			System.out.println("marcaRegulador: "+i);
			if(i=="")inmetros_cilindros_datos=false;
		}
		for (String i : marcaRegulador) {
			System.out.println("marcaRegulador: "+i);
			if(i=="")marca_regulador_datos=false;
		}
		for (String i : serieValvula) {
			System.out.println("serieRegulador: "+i);
			if(i=="")serie_regulador_datos=false;
		}
		
		
		//*******************************************************************************************implementos kit
//		int idimple=idImplementosKit(); 
		
		
		String observacioneskit=req.getParameter("observacioneskit");
		String primeramedicion=req.getParameter("primeramedicion");
		String segundamedicion=req.getParameter("segundamedicion");
		String conformeestandasino=req.getParameter("conformeestandasino");

		System.out.println("conformeestandasino: "+conformeestandasino);
		int conformeestandasinoentero;
		if(conformeestandasino==null) {
			conformeestandasinoentero=-1;
		}else {
			
			conformeestandasinoentero=Integer.parseInt(conformeestandasino);
		}
		System.out.println("conformeestandasinoentero: "+conformeestandasinoentero);
		
		String cunacilindro=req.getParameter("cunacilindro");
		
		System.out.println("cunacilindro: "+cunacilindro);
		int cunacilindroentero;
		if(cunacilindro==null) {
			cunacilindroentero=-1;
		}else {
			
			cunacilindroentero=Integer.parseInt(cunacilindro);
		}

		
		String dosificadorsino=req.getParameter("dosificadorsino");		
		System.out.println("dosificadorsino: "+dosificadorsino);
		int dosificadorsinoentero;
		if(dosificadorsino==null) {
			dosificadorsinoentero=-1;
		}else {
			
			dosificadorsinoentero=Integer.parseInt(dosificadorsino);
		}

		
		String sistventeosino=req.getParameter("sistventeosino");		
		System.out.println("dosificadorsino: "+dosificadorsino);
		int sistventeosinoentero;
		if(sistventeosino==null) {
			sistventeosinoentero=-1;
		}else {
			
			sistventeosinoentero=Integer.parseInt(sistventeosino);
		}

		String memoriaserie=req.getParameter("memoriaserie");

		String sensormapserie=req.getParameter("sensormapserie");

		String ramplainyectoresnro=req.getParameter("ramplainyectoresnro");
		System.out.println("ramplainyectoresnro: "+ramplainyectoresnro);
		Integer ramplainyectoresnroentero;
		if(ramplainyectoresnro==null || ramplainyectoresnro=="") {
			ramplainyectoresnroentero=null;
		}else {
			
			ramplainyectoresnroentero=Integer.parseInt(ramplainyectoresnro);
		}
		


		
		try {
			if(series.length>0 && cilindros.length>0 && capacidades.length>0 && fechaFab.length>0) {
				if(serie_datos=true && cilindros_datos==true && capacidades_datos==true && fechafab_datos==true) {
					sql="UPDATE registrokit SET fechaconversion=?,desinstaladokitglpsino=?,login=?,idordserv=?,idmarcred=?,numserieregulador=?,certificadokit=?,numseriemotor=?,codigobsisa=?,fechaCertificado=?,anio=?,idtall=?,observacioneskit=?,primeramedicion=?,segundamedicion=?,conformeestandasino=? WHERE idregistrokit=?";
					this.db.update(sql,FechaConversion,DestKit,login,idOS,idR,SerieRegulador,CertificadoKit,SerieMotor,codigoBSISA,fechaCertificado,gestion,idtall,observacioneskit,primeramedicion,segundamedicion,conformeestandasinoentero,idRegistroKit);
					
					sql="delete from instalacionCilindro where idregistroKit=?";
					this.db.update(sql,new Object[] {idRegistroKit});
					
					String sql1="INSERT INTO instalacionCilindro(idmarccil,serie,idcapcil,fechaFab,inmetro,idmarcvalcil,serievalvula,idregistroKit,idinstcil) VALUES(?,?,?,?,?,?,?,?,?)";
					for (int i = 0; i < cilindros.length; i++) {
						if(cilindros[i]!="" && series[i]!="" && capacidades[i]!="" && fechaFab[i]!="") {
							int idinstcil=idInstalacionCilindro();
							this.db.update(sql1,Integer.parseInt(cilindros[i]),series[i],Integer.parseInt(capacidades[i]),fechaFab[i],inmetrosCilindros[i],Integer.parseInt(marcaRegulador[i]),serieValvula[i],idRegistroKit,idinstcil);
						}
						
					} 

					sql="UPDATE implementoskit SET cunacilindro=?,dosificadorsino=?,sistventeosino=?,memoriaserie=?,sensormapserie=?,ramplainyectoresnro=?  WHERE idregistrokit=?";
					this.db.update(sql,
							cunacilindroentero,
							dosificadorsinoentero, 
							sistventeosinoentero, 
							memoriaserie.toUpperCase(),
							sensormapserie.toUpperCase(),
							ramplainyectoresnroentero,
							idRegistroKit
							);

					this.db.update("UPDATE ordenServicio SET instaladoSiNo=1 WHERE idordserv=?",idOS);

					sql="UPDATE solicitud SET idacc=? WHERE idsolt=?";
					this.db.update(sql,getAccionKI(),getIdSoltByOS(idOS));

					resp=true;
				}else {
					resp=false;
				}
				
			}else {
				resp=false;
			}
			//resp[1]=idrecep;
			return resp;
		}catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
			resp=false;
			return resp;
		}
	
	}
	@Transactional
	public List<InstalacionCilindro> ListaCilindro(int id) {
		String sql="select * from instalacionCilindro where idregistroKit=?";
		return this.db.query(sql,new objInstalacionCilindro(),id);
	}
//	@Transactional
	public RegistroKit verInstacionKit(int id) {
		String sql="select * from registroKit where idregistroKit=?";
		return this.db.queryForObject(sql,new objRegistroKit(),id);
	}
	@Transactional
	public int idRegistroKit(){
//		String sql="select COESCALE(max(idregistroKit),0)+1 as idregistroKit from registroKit";
		String sql="select COALESCE(max(idregistroKit),0)+1 as idregistroKit from registroKit";
		return db.queryForObject(sql, Integer.class);
	}
	@Transactional
	public int idInstalacionCilindro(){
		String sql="select COALESCE(max(idinstcil),0)+1 as idinstcil from instalacioncilindro";
		return db.queryForObject(sql, Integer.class);
	}
	@Transactional
	public int idImplementosKit(){
		String sql="select COALESCE(max(idimple),0)+1 as idimple from implementoskit";
		return db.queryForObject(sql, Integer.class);
	}
	@Transactional
	/*ORDEN PAGO*/
	public RegistroKit getRegistroKitOP(int id) {
		String sql="select rk.* from registroKit rk,actaRecepcion ar where rk.idordserv=ar.idordserv AND ar.idordserv=?";
		return this.db.queryForObject(sql,new objRegistroKit(),id);
	}
	@Transactional
	/*TRANSFERENCIA BENEFICIARIO*/
	public RegistroKit getRegistroKitTB(int id) {
		//String sql="SELECT rk.* FROM registroKit rk JOIN ordenServicio os ON os.idordserv=rk.idordserv JOIN solicitud s ON s.idsolt=os.idsolt JOIN trasladoBeneficiario tb ON tb.idsolt=s.idsolt WHERE tb.idsolt=?";
		String sql="SELECT rk.* FROM registroKit rk JOIN ordenServicio os ON os.idordserv=rk.idordserv JOIN solicitud s ON s.idsolt=os.idsolt  WHERE s.idsolt=?";
		return this.db.queryForObject(sql,new objRegistroKit(),id);
	}
	@Transactional
	public List<RegistroKit> FiltroRegistroKitTB(String cadena){
		String sql="SELECT rk.* FROM registroKit rk,ordenServicio os,solicitud s,vehiculo veh,beneficiario b,persona p, benVehSolt bvs \r\n" + 
				"WHERE rk.idordserv=os.idordserv AND os.idsolt=s.idsolt AND s.estado=1 AND os.instaladoSiNo=1 AND bvs.idben=b.idben AND bvs.perteneceSiNo=1 AND bvs.placa=veh.placa AND bvs.idsolt=s.idsolt and b.idper=p.idper and (os.numords LIKE ? or s.numSolt LIKE ? or p.ci LIKE ?) ";
		return this.db.query(sql, new objRegistroKit(),'%'+cadena+'%','%'+cadena+'%','%'+cadena+'%');
	}
	@Transactional
	public RegistroKit getRegistroKitTBbyIdTrasl(int id) {
		String sql="select rk.* from registroKit rk,trasladoBeneficiario tb,solicitud s,ordenServicio os\r\n" + 
				"WHERE tb.idsolt=s.idsolt AND s.idsolt=os.idsolt  AND rk.idordserv=os.idordserv AND tb.idtrasl=?";
		return this.db.queryForObject(sql,new objRegistroKit(),id);
	}
	
	@Transactional
	//DESMONTAJE
	public List<DesmontajeKit> ListarDesmontajes(HttpServletRequest req){
		String filtro=req.getParameter("filtro");
		String sql="select d.* from desmontajeKit d join registroKit rk on rk.idregistroKit=d.idregistroKit JOIN ordenServicio os ON os.idordserv=rk.idordserv join solicitud s on s.idsolt=os.idsolt JOIN benVehSolt bvs ON bvs.idsolt=s.idsolt JOIN beneficiario b ON b.idben=bvs.idben AND bvs.perteneceSiNo=1 JOIN vehiculo veh on veh.placa=bvs.placa JOIN persona per ON per.idper=b.idper  JOIN taller t ON t.idtall=d.idtall where (concat(per.ap,' ',per.am,' ',per.nombres) LIKE ? or veh.placa LIKE ? ) ORDER BY d.iddes ASC";
		return this.db.query(sql, new objDesmontajeKit(),"%"+filtro+"%","%"+filtro+"%");
	}	
	@Transactional
	public List<RegistroKit> FiltroInstalacionKit(String cadena){
		String sql="SELECT r.* FROM registroKit r,ordenServicio os,solicitud s,vehiculo veh,beneficiario b,persona p,benVehSolt bvs WHERE r.idordserv=os.idordserv AND os.idsolt=s.idsolt AND os.instaladoSiNo=1 AND s.estado=1 AND bvs.idben=b.idben AND bvs.perteneceSiNo=1 AND bvs.placa=veh.placa AND bvs.idsolt=s.idsolt and b.idper=p.idper and (os.numords LIKE ? or s.numSolt LIKE ? or p.ci LIKE ?)"
				+ " and r.idregistroKit not in (SELECT des.idregistroKit FROM desmontajeKit des WHERE r.idregistroKit=des.idregistroKit)";
		return this.db.query(sql, new objRegistroKit(),'%'+cadena+'%','%'+cadena+'%','%'+cadena+'%');
	}
	@Transactional
	public int idDesmontaje(){
		String sql="select COALESCE(max(iddes),0)+1 as iddes from desmontajeKit";
		return db.queryForObject(sql, Integer.class);
	}
	@Transactional
	public Object[] registrarDesmontaje(HttpServletRequest req,Persona xuser) {
		String sql="";
		Object [] resp=new Object[2];
		
		int iddes=idDesmontaje();
		int idtall=Integer.parseInt(req.getParameter("idtall"));
		int idregistroKit=Integer.parseInt(req.getParameter("idregistroKit"));
	
		String login=xuser.getUsuario().getLogin();
		System.out.println("iddes: "+iddes+" login: "+login+" idtall: "+idtall+" idregistroKit: "+idregistroKit);
		try {
			sql="INSERT INTO desmontajeKit(iddes,login,idtall,idregistroKit) VALUES(?,?,?,?)";
			this.db.update(sql,iddes,login,idtall,idregistroKit);				
			resp[0]=true;
			//resp[1]=idrecep;
			return resp;
		}catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
			resp[0]=false;
			return resp;
		}
	}
	@Transactional
	public int getIdSoltByIdRegKit(int idRegistroKit) {
		return this.db.queryForObject("SELECT s.idsolt FROM solicitud s,ordenServicio os,registroKit rk WHERE rk.idordserv=os.idordserv AND os.idsolt=s.idsolt AND rk.idregistroKit=?",Integer.class,idRegistroKit);
	}
	@Transactional
	/*TRASLADO KIT VEHICULO*/
	public List<TrasladoKitVehiculo> ListarTrasladoKitVehiculo(HttpServletRequest req){
		String filtro=req.getParameter("filtro");
		String sql="select tkv.* from trasladoKitVehiculo tkv JOIN  desmontajeKit d ON d.iddes=tkv.iddes join registroKit rk on rk.idregistroKit=d.idregistroKit JOIN ordenServicio os ON os.idordserv=rk.idordserv join solicitud s on s.idsolt=os.idsolt JOIN benVehSolt bvs ON bvs.idsolt=s.idsolt JOIN beneficiario b ON b.idben=bvs.idben AND bvs.perteneceSiNo=1 JOIN vehiculo veh on veh.placa=bvs.placa JOIN persona per ON per.idper=b.idper\r\n" + 
				"where (concat(per.ap,' ',per.am,' ',per.nombres) LIKE ? or veh.placa LIKE ? ) ORDER BY tkv.idtraslv ASC";
		return this.db.query(sql, new objTrasladorKitVehiculo(),"%"+filtro+"%","%"+filtro+"%");
	}
	@Transactional
	public DesmontajeKit metDesmontajeKit(int id){
		return this.db.queryForObject("select * from desmontajeKit where iddes=?", new objDesmontajeKit(),id);
	}
	@Transactional
	public List<DesmontajeKit> FiltroDesmontajeKit(String cadena){
		String sql="SELECT dk.* FROM desmontajeKit dk,registroKit r,ordenServicio os,solicitud s,vehiculo veh,beneficiario b,persona p,benVehSolt bvs \r\n" + 
				"WHERE dk.idregistroKit=r.idregistroKit AND r.idordserv=os.idordserv AND os.idsolt=s.idsolt AND os.instaladoSiNo=1 AND bvs.idben=b.idben AND bvs.perteneceSiNo=1 AND bvs.placa=veh.placa AND bvs.idsolt=s.idsolt and b.idper=p.idper and (os.numords LIKE ? or s.numSolt LIKE ? or p.ci LIKE ?) and dk.iddes not in (SELECT tk.iddes FROM trasladoKitVehiculo tk WHERE tk.iddes=dk.iddes)";
		return this.db.query(sql, new objDesmontajeKit(),'%'+cadena+'%','%'+cadena+'%','%'+cadena+'%');
	}
	@Transactional
	public Object[] registrarTKitVehiculo(HttpServletRequest req,Persona xuser,int idsolt) {
		String sql="";
		Object [] resp=new Object[2];
		
		int idtraslv=idTKitVehiculo();
		int iddes=Integer.parseInt(req.getParameter("iddes"));
	
		String login=xuser.getUsuario().getLogin();
		try {
			sql="INSERT INTO trasladoKitVehiculo(idtraslv,iddes,idsoltNueva,login) VALUES(?,?,?,?)";
			this.db.update(sql,idtraslv,iddes,idsolt,login);				
			resp[0]=true;
			//resp[1]=idrecep;
			return resp;
		}catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
			resp[0]=false;
			return resp;
		}
	}
	@Transactional
	public int idTKitVehiculo(){
		String sql="select COALESCE(max(idtraslv),0)+1 as idtraslv from trasladoKitVehiculo";
		return db.queryForObject(sql, Integer.class);
	}
	@Transactional
	public TrasladoKitVehiculo  verTrasladoKitVehiculo(int id) {
		String sql="select * from trasladoKitVehiculo where idtraslv=?";
		return this.db.queryForObject(sql,new objTrasladorKitVehiculo(),id);
	}
	@Transactional
	//implementosKit
	public List<Map<String, Object>> ListaMarcasConmutador(){
		String sql="SELECT * FROM marcaconmutador WHERE estado=1";
		return this.db.queryForList(sql, new Object[] {});
	}
	@Transactional
	public List<Map<String, Object>> ListaMarcasValvulaCargaInterna(){
		String sql="SELECT * FROM marcavalvulacargainterna WHERE estado=1";
		return this.db.queryForList(sql, new Object[] {});
	}
	@Transactional
	public List<Map<String, Object>> ListaMarcasValvulaCargaExterna(){
		String sql="SELECT * FROM marcavalvulacargaexterna WHERE estado=1";
		return this.db.queryForList(sql, new Object[] {});
	}
	@Transactional
	public List<Map<String, Object>> ListaMarcasEmulador(){
		String sql="SELECT * FROM marcaemulador WHERE estado=1";
		return this.db.queryForList(sql, new Object[] {});
	}
	@Transactional
	public List<Map<String, Object>> ListaMarcasManometro(){
		String sql="SELECT * FROM marcamanometro WHERE estado=1";
		return this.db.queryForList(sql, new Object[] {});
	}
	@Transactional
	public List<Map<String, Object>> ListaMarcasMemoria(){
		String sql="SELECT * FROM marcamemoria WHERE estado=1";
		return this.db.queryForList(sql, new Object[] {});
	}
	@Transactional
	public List<Map<String, Object>> ListaMarcasSensorMap(){
		String sql="SELECT * FROM marcasensormap WHERE estado=1";
		return this.db.queryForList(sql, new Object[] {});
	}
	@Transactional
	public List<Map<String, Object>> ListaMarcasFiltroGas(){
		String sql="SELECT * FROM marcafiltrogas WHERE estado=1";
		return this.db.queryForList(sql, new Object[] {});
	}
	@Transactional
	public List<Map<String, Object>> ListaMarcasRamplaInyectores(){
		String sql="SELECT * FROM marcarampla WHERE estado=1";
		return this.db.queryForList(sql, new Object[] {});
	}
	@Transactional
	//add segunda version
	public List<Map<String, Object>> ListaMarcasCañeriaAltaPresion(){
		String sql="SELECT * FROM marcacañeriaaltapresion  WHERE estado=1";
		return this.db.queryForList(sql, new Object[] {});
	}
	@Transactional
	public List<Map<String, Object>> ListaMarcasElectrovalvulaGasolina(){
		String sql="SELECT * FROM marcaelectrovalgaso  WHERE estado=1";
		return this.db.queryForList(sql, new Object[] {});
	}
	@Transactional
	public List<Map<String, Object>> ListaMarcasElectrovalvulaGas(){
		String sql="SELECT * FROM marcaelectrovalgas  WHERE estado=1";
		return this.db.queryForList(sql, new Object[] {});
	}
	@Transactional
	public List<Map<String, Object>> ListaMarcasMagueraGas(){
		String sql="SELECT * FROM marcamangueragas  WHERE estado=1";
		return this.db.queryForList(sql, new Object[] {});
	}
	@Transactional
	public List<Map<String, Object>> ListaMarcasMagueraAgua(){
		String sql="SELECT * FROM marcamangueraagua  WHERE estado=1";
		return this.db.queryForList(sql, new Object[] {});
	}
	@Transactional
	public List<Map<String, Object>> ListaMarcasMagueraVacio(){
		String sql="SELECT * FROM marcamangueravacio  WHERE estado=1";
		return this.db.queryForList(sql, new Object[] {});
	}
}
