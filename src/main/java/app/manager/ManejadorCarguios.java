package app.manager;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;


@Service
public class ManejadorCarguios {
	private final  JdbcTemplate db;
	public ManejadorCarguios(JdbcTemplate jdbcTemplate) {
		this.db = jdbcTemplate;
		
	}
	
//	public List<Rol> ListarRoles(HttpServletRequest req){
//		String filtro=req.getParameter("filtro");
//		int estado=Integer.parseInt(req.getParameter("estado"));
//		String sql="SELECT r.* FROM rol r where (r.nombre LIKE ?) and (r.estado=? or ?=-1) ORDER BY r.idrol ASC";
//		return this.db.query(sql, new objRol(),"%"+filtro+"%",estado,estado);
//	}	
	//RESTROLES
	@Transactional
	public List<Map<String, Object>> Listar(HttpServletRequest req){
		String filtro=req.getParameter("filtro");
		System.out.println("filtro"+filtro);
		System.out.println("filtro"+filtro.equals(""));
//		int estado=Integer.parseInt(req.getParameter("estado"));
		
		String sql="";
		try {
			/*sql="SELECT DISTINCT DATENAME(MONTH,c.fecha) AS Mes,DATENAME(YEAR,c.fecha) AS Anio,FORMAT(c.fecha,'yyyy-MM') fechaRef,e.nombreEstacion,concat(p.ap,' ',p.am,' ',p.nombres) as propietario\r\n" + 
					"FROM carguio c,estacion e,persona p where e.idper=p.idper and e.idest=c.idest and (e.nombreEstacion LIKE ?) \r\n" + 
					"GROUP BY c.fecha,e.nombreEstacion,p.nombres,p.ap,p.am";*/
			sql="SELECT DISTINCT DATENAME(MONTH,c.fecha) AS Mes,MONTH(c.fecha) as MesNum,DATENAME(YEAR,c.fecha) AS Anio,FORMAT(c.fecha,'yyyy-MM') fechaRef,e.nombreEstacion,concat(p.ap,' ',p.am,' ',p.nombres) as propietario\r\n" + 
					"FROM carguio c,estacion e,persona p where e.idper=p.idper and e.idest=c.idest and (e.nombreEstacion LIKE ?)\r\n" + 
					"GROUP BY c.fecha,e.nombreEstacion,p.nombres,p.ap,p.am";
			return this.db.queryForList(sql, new Object[] {"%"+filtro+"%"});
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}

		
	}
	@Transactional
	public Map<String, Object> RETUNR_FondoRot(String Anio,String Mes) {
		String sql="";
		try {
//			sql="SELECT SUM(c.volumen) as Volumen,SUM(c.fondoRotatorio) as FondoRotatorio  FROM carguio c WHERE FORMAT(c.fecha,'yyyy-MM')=?"; 
			sql="SELECT SUM(c.volumen) as Volumen,SUM(c.fondoRotatorio) as FondoRotatorio  FROM carguio c WHERE MONTH(c.fecha)=? AND YEAR(c.fecha)=?"; 
			return db.queryForMap(sql, new Object[] {Integer.parseInt(Mes),Integer.parseInt(Anio)});
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
		
	}
	@Transactional
	public String RETUNR_Fecha(int idest) {
		String sql="";
		try {
			sql="SELECT DISTINCT DATENAME(MONTH,c.fecha) AS Fecha FROM carguio c,estacion e WHERE c.idest=e.idest AND e.idest=?"; 
			return db.queryForObject(sql, String.class,idest);
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
		
	}
	
	@Transactional
	public boolean registrar(String EESS,String FECHAC,String RAZONSOCIAL,String PLACA,Double TOTAL,Double VOLUMEN,Double EMTAGAS,Double UNIDAD) {
		int idest= RETUNRIDEESS(EESS);
		System.out.println("IDEESS:"+idest);
//		Map<String,Object> Ben=RETUNR_BEN(PLACA);
//		System.out.println("BEN:"+Ben.toString());
//		System.out.println("BEN1:"+Ben.get("Nombres"));//AQUI SALE NULL
		String sql="";
		int idcarg=generarIdCarg();
		try {
			sql="INSERT INTO carguio(idcarg,idest,fecha,nombre,placa,total,volumen,fondoRotatorio,unidad) VALUES(?,?,?,?,?,?,?,?,?)";
			this.db.update(sql,idcarg,idest,FECHAC,RAZONSOCIAL,PLACA,TOTAL,VOLUMEN,EMTAGAS,UNIDAD);
			return true;
		} catch (Exception e) {
			// TODO: handle exception
			return false;
		}
	}
	@Transactional
	public boolean registrarLIST(String lista) {
//		public boolean registrarLIST(List<Object> lista) {
//		int idest=RETUNRIEESS(EESS);
//		System.out.println("IDEESS:"+idest);
//		Map<String,Object> Ben=RETUNR_BEN(PLACA);
//		System.out.println("BEN:"+Ben.toString());
//		System.out.println("BEN1:"+Ben.get("Nombres"));//AQUI SALE NULL
		String sql="";
//		int idcarg=generarIdCarg();
		try {
//			sql="INSERT INTO carguio(idcarg,idest,fecha,nombre,placa,total,volumen,fondoRotatorio,unidad) VALUES\r\n" + 
//					"(2,3,'2018-10-01 00:00:00','FAVIAN ORTIZ','852GCA',11.4,6.867469879518072,1.3734939759036147,1.66),\r\n" + 
//					" (3,3,'2018-10-01 00:00:00','EDSON FLORES','963UZE',13.21,7.957831325301206,1.5915662650602413,1.66)";
			sql="INSERT INTO carguio(idcarg,idest,fecha,nombre,placa,total,volumen,fondoRotatorio,unidad) VALUES"+lista+"";
			this.db.update(sql);
			return true;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			System.out.println(e.getMessage());
			return false;
		}
	}
	@Transactional
	public boolean adicionar(String link_text) {
		String sql;
		try {
			sql="BULK INSERT carguio\r\n" + 
					"FROM '"+link_text+"'\r\n" + 
					"WITH \r\n" + 
					"(\r\n" + 
					"	 FIELDTERMINATOR =';',\r\n" + 
					"	 ROWTERMINATOR ='\\n'\r\n" + 
					")\r\n"; 
//					"GO";
			this.db.update(sql);
			return true;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			System.out.println(e.getMessage());
			return false;
		}
	}
	@Transactional
	public boolean adicionar_pg(String link_text) {
		String sql;
		try {
//			sql="COPY carguio FROM '"+link_text+"' WITH (FORMAT csv, HEADER true, DELIMITER ';')"; 
			sql="COPY carguio FROM '"+link_text+"' WITH (FORMAT csv, HEADER false, DELIMITER ';')"; 
//					
			this.db.update(sql);
			return true;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			System.out.println(e.getMessage());
			return false; 
		}
	}
	@Transactional
	public int RETUNRIDEESS(String EESS) { 
		String sql="";
		try {
			sql="select idest from estacion where RTRIM(LTRIM(nombreEstacion))= RTRIM(LTRIM(?))";
			return db.queryForObject(sql, Integer.class,EESS);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			System.out.println("error:"+e.getMessage());
			return 0;
		}
		
		
	}
	@Transactional
	public int RETUNR_IDBEN(String placa) {
		String sql="";
		try {
//			sql="select b.idben from beneficiario b,vehiculo v,benVehSolt  bvs where b.idben=bvs.idben AND v.placa=bvs.placa AND bvs.perteneceSiNo=1 AND v.placa=?";
			sql="select b.idben from beneficiario b,vehiculo v,benVehSolt  bvs where b.idben=bvs.idben AND v.placa=bvs.placa AND v.placa=?"; //se quito si pertenece xq puede q sea ya un benef inactivo con la misma placa
			return db.queryForObject(sql, Integer.class,placa);
		} catch (Exception e) {
			// TODO: handle exception
			return 0;
		}
		
		
	}
	@Transactional
	public Map<String, Object> RETUNR_PLACA(String placa) {
		String sql="";
		try {
			sql="SELECT DISTINCT placa FROM vehiculo WHERE placa=?"; 
			return db.queryForMap(sql, new Object[] {placa});
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
		
	}
	@Transactional
	public Map<String, Object> RETUNR_BEN(String PLACA) {
		String sql="";
		try {
			sql="select CONCAT(p.nombres,' ',p.ap,' ',p.am) as Nombres from persona p,beneficiario b,vehiculo v,benVehSolt bvs  \r\n" + 
					"where p.idper=b.idper AND bvs.idben=b.idben AND bvs.perteneceSiNo=1 AND v.placa=bvs.placa AND v.placa=?";
			return db.queryForMap(sql, new Object[] {PLACA});
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
		
	}
	@Transactional
	public int generarIdCarg(){
		String sql="select COALESCE(max(idcarg),0)+1 as idcarg from carguio";
		return db.queryForObject(sql, Integer.class);
	}
	
}
