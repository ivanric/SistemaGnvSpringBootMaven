package app.manager;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import app.models.Persona;
import app.models.Solicitud;
import app.models.Vehiculo;

@Service
public class ManejadorProgramaGnv {
	private final  JdbcTemplate db;
	public ManejadorProgramaGnv(JdbcTemplate jdbcTemplate) {
		this.db = jdbcTemplate;
		
	}
	@Transactional
	public Map<String,Object> getInstitucion(){
		String estado="1";
		return this.db.queryForMap("select * from institucion where estado=?",new Object[] {estado});
	}
	@Transactional
	public Map<String,Object> getFirmas(){
		try {
			return this.db.queryForMap("SELECT DISTINCT concat(p1.nombres,' ',p1.ap,' ',p1.am) as firma1,concat(p2.nombres,' ',p2.ap,' ',p2.am) as firma2,concat(p3.nombres,' ',p3.ap,' ',p3.am) as firma3 \r\n" + 
					"FROM firmasOS fr JOIN persona p1 ON p1.idper=fr.firma1 JOIN persona p2 ON p2.idper=fr.firma2 JOIN persona p3 ON p3.idper=fr.firma3 \r\n" + 
					"WHERE fr.estado=1",new Object[] {});
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
	
	}
	@Transactional
	public List<Map<String,Object>> getPersonasFirmas(){
		return this.db.queryForList("SELECT p.idper,concat(p.nombres,' ',p.ap,' ',p.am) as persona,r.nombre as rol \r\n" + 
				"FROM persona p JOIN usuario u ON p.idper=u.idper JOIN rolusu ru ON ru.login=u.login JOIN rol r ON r.idrol=ru.idrol",new Object[] {});
	}
	@Transactional
	public List<Map<String,Object>> getDeparta(){
		return this.db.queryForList("SELECT * from departamento where estado=1",new Object[] {});
	}
	@Transactional
	public Object[] modificarInst(HttpServletRequest req){
		Integer idnst=Integer.parseInt(req.getParameter("idinst"));
		String nombreCompania=req.getParameter("nombrecompania");
		String nombreInstitucion=req.getParameter("nombreinstitucion");
		String direccion=req.getParameter("direccion");
		String telefono=req.getParameter("telefono");
		Integer iddep=Integer.parseInt(req.getParameter("iddep"));
		String nitInst=req.getParameter("nitinst");
		String fax=req.getParameter("fax");
		Object []Respuesta=new Object[2];
		String sql="";
		try {

			sql="UPDATE institucion SET nombrecompania=?,nombreinstitucion=?,direccion=?,telefono=?,iddep=?,nitinst=?,fax=? WHERE idinst=?";
			this.db.update(sql,nombreCompania,nombreInstitucion,direccion,telefono,iddep,nitInst,fax,idnst);
		

			Respuesta[0]=true;
//			Respuesta[1]=;
			return Respuesta;
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("entro error");
			System.out.println("err:"+e.getMessage());
			e.printStackTrace();
			Respuesta[0]=false;
			return Respuesta;
		}
	}
	@Transactional
	public Object[] registrarFirmas(HttpServletRequest req){
		int idfirm=generarIdFirm();
		int firma1=Integer.parseInt(req.getParameter("firma1_m"));
		int firma2=Integer.parseInt(req.getParameter("firma2_m"));
		int firma3=Integer.parseInt(req.getParameter("firma3_m"));
		System.out.println(firma1);
		System.out.println(firma2);
		System.out.println(firma3);
		Object []Respuesta=new Object[2];
		String sql="";
		try {

			sql="INSERT INTO firmasOS(idfirm,firma1,firma2,firma3,estado) VALUES(?,?,?,?,?)"; 
			this.db.update(sql,idfirm,firma1,firma2,firma3,1);	

			sql="update firmasOS set estado=0 where idfirm!=?";
			this.db.update(sql,idfirm);
			 
			Respuesta[0]=true;

			return Respuesta;
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("entro error");
			System.out.println("err:"+e.getMessage());
			e.printStackTrace();
			Respuesta[0]=false;
			return Respuesta;
		}
	}
	@Transactional
	public int generarIdFirm(){
		String sql="select COALESCE(max(idfirm),0)+1 as idfirm from firmasOS";
		return db.queryForObject(sql, Integer.class);
	}
	
	
	// institucion 
	@Transactional
	public Map<String,Object> getInstitucionById(int id){
		return this.db.queryForMap("select * from institucion where idinst=?",new Object[] {id});
	}
	@Transactional
	public List<Map<String,Object>> getGestionByInst(int idinst){
		return this.db.queryForList("SELECT * from gestion where idinst=? ORDER BY gestion ASC",new Object[] {idinst});
	}
}
