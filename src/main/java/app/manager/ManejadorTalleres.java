package app.manager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import app.manager.ManejadorBeneficiarios.objPersona_d;
import app.manager.ManejadorRoles.objRol;
import app.models.Departamento;
import app.models.Persona;
import app.models.Provincia;
import app.models.Rol;
import app.models.Taller;
import app.models.Telefono;
import app.models.Usuario;
@Service
public class ManejadorTalleres {
	private final  JdbcTemplate db;
	public ManejadorTalleres(JdbcTemplate jdbcTemplate) {
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
				t.setPersona(null);
			}
			t.setTot(rs.getInt("tot"));
			return t;
	    }
	}
	public class objTaller_mod implements RowMapper<Taller>{
		@Override
		public Taller mapRow(ResultSet rs, int arg1) throws SQLException {
			Taller t= new Taller();
			t.setIdtall(rs.getInt("idtall"));
			t.setNombretall(rs.getString("nombretall"));
			t.setDireccion(rs.getString("direccion"));
			t.setFecha_registro(rs.getString("fecha_registro"));
			t.setEstado(rs.getInt("estado"));
			t.setIdprov(rs.getInt("idprov"));
			try {
				t.setPersona(metPersona(rs.getInt("idper")));
			}catch (Exception e){
				t.setPersona(null);
			}
			try {
				t.setProvincia(metProvincia(rs.getInt("idprov")));
			}catch (Exception e){
				t.setPersona(null);
			}
			return t;
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
			p.setRazonsocial(rs.getString("razonsocial"));
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
	public class objTelefono implements RowMapper<Telefono>{
		@Override
		public Telefono mapRow(ResultSet rs, int arg1) throws SQLException {
			Telefono t= new Telefono();
			t.setNumero(rs.getString("numero"));	
			return t;
	    }
	}
	
	public class objProvincia implements RowMapper<Provincia>{
		@Override
		public Provincia mapRow(ResultSet rs, int arg1) throws SQLException {
			Provincia p= new Provincia();
			p.setIdprov(rs.getInt("idprov"));
			p.setCodigo(rs.getString("codigo"));
			p.setNombre(rs.getString("nombre"));
			p.setEstado(rs.getInt("estado"));
			p.setIddep(rs.getInt("iddep"));
			try {
				p.setDepartamento(metDepartamento(rs.getInt("iddep")));
			} catch (Exception e) {
				// TODO: handle exception
				p.setDepartamento(null);
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
			return null ;
			// TODO: handle exception
		}
	}
	@Transactional
	public Provincia metProvincia(int cod){
		try {
			String sql="select * from provincia where idprov=?";
			Provincia dato=db.queryForObject(sql, new objProvincia(),cod);
			return dato;
		} catch (Exception e) {
			System.out.println("error al obtener idprov"+e.toString());
			return null;
		}
	}
	@Transactional
	public Departamento metDepartamento(int cod){
		try {
			String sql="select * from departamento where iddep=?";
			Departamento dato=db.queryForObject(sql, new BeanPropertyRowMapper<Departamento>(Departamento.class),cod);
			return dato;
		} catch (Exception e) {
			System.out.println("error al obtener iddep"+e.toString());
			return null;
		}
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
	public int generarIdPer(){
		String sql="select COALESCE(max(idper),0)+1 as idper from persona";
		return db.queryForObject(sql, Integer.class);
	}
	@Transactional
	public int generarIdTall(){
		String sql="select COALESCE(max(idtall),0)+1 as idtall from taller";
		return db.queryForObject(sql, Integer.class);
	}
	@Transactional
	//REST_TALLERES
	public List<Taller> ListarTalleres(HttpServletRequest req){
		String filtro=req.getParameter("filtro");
		int estado=Integer.parseInt(req.getParameter("estado"));
		String sql="SELECT t.* FROM taller t where (t.nombretall LIKE ?) and (t.estado=? or ?=-1) ORDER BY t.idtall ASC";
		return this.db.query(sql, new objTaller(),"%"+filtro+"%",estado,estado);
	}
	@Transactional
	//listar datatable
	public List<Taller> listar_d(int start,int estado,String search,int length){
		String sql="";
		try {
			sql="select count(*) from taller t JOIN persona p on p.idper=t.idper where (t.estado=? or ?=-1) and  (upper(t.nombretall) like concat('%',upper(?),'%'))";
			//sql="select count(*) from solicitud s where estado=? and upper(s.nombre) like concat('%',upper(?),'%')";
			Integer tot=db.queryForObject(sql, Integer.class,estado,estado,search);
			sql="select t.*,row_number() OVER(ORDER BY t.idtall) as RN,? as tot from persona p join taller t on t.idper=p.idper where (t.estado=? or ?=-1) and  (upper(t.nombretall) like concat('%',upper(?),'%')) ORDER BY t.idtall ASC LIMIT ? OFFSET ?";

			return db.query(sql,new objTaller(),tot,estado,estado,search,length,start);
		} catch (Exception e) {
			e.printStackTrace();    
			System.out.println(e.getMessage());
			return null;
		}
	}
	
	@Transactional
	public List<Map<String, Object>> listaDepartamentos(){
		String sql="";
		try {
			sql="select * from departamento";
			return this.db.queryForList(sql,new Object[] {});
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			System.out.println(e.getMessage());
			return null;
		}
	}
	@Transactional
	public List<Map<String, Object>> listaProvincias(){
		String sql="";
		try {
			sql="select * from provincia";
			return this.db.queryForList(sql,new Object[] {});
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			System.out.println(e.getMessage());
			return null;
		}
	}
	@Transactional
	public List<Map<String, Object>> listaProvincias(int iddep){
		String sql="";
		try {
			sql="select * from provincia where iddep=?";
			return this.db.queryForList(sql,new Object[] {iddep});
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			System.out.println(e.getMessage());
			return null;
		}
	}
	@Transactional
	public List<Map<String, Object>> getProvinciasByDepart(HttpServletRequest req){
		int iddep=Integer.parseInt(req.getParameter("iddep"));
		String sql="";
		try {
			sql="select * from provincia where iddep=?";
			return this.db.queryForList(sql,new Object[] {iddep});
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			System.out.println(e.getMessage());
			return null;
		}
	}
	@Transactional
	public boolean registrar(HttpServletRequest req,Persona p){
		int idper= generarIdPer();
		int idtall= generarIdTall();
		String[] tel=req.getParameterValues("telefono[]");
		String sql="";
		
		System.out.println("PER:"+p.getAm().toString());
		System.out.println("iddep:"+req.getParameter("iddep"));
		System.out.println("idprov:"+req.getParameter("idprov"));
		try {
			sql="INSERT INTO persona(idper,ci,ciCod,nombres,ap,am,genero,direccion,email,estado) VALUES(?,?,?,?,?,?,?,?,?,?)";
			this.db.update(sql,idper,p.getCi(),p.getCiCod(),p.getNombres().toUpperCase(),p.getAp().toUpperCase(),p.getAm().toUpperCase(),p.getGenero(),p.getDireccion().toUpperCase(),p.getEmail(),1);
			
			
			sql="insert into telefono(numero,idper) values(?,?)";
			for (int i = 0; i < tel.length; i++) {
				if(!tel[i].equals("")) {
					this.db.update(sql,tel[i],idper);
				}
			}	
			sql="INSERT INTO taller(idtall,nombretall,direccion,estado,idper) VALUES(?,?,?,?,?)";
			this.db.update(sql,idtall,req.getParameter("nombretall").toUpperCase(),req.getParameter("direcciontall").toUpperCase(),1,idper);
			
			return true;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}
	}
	@Transactional
	public boolean registrar_d(HttpServletRequest req,Persona p){
		int idper= generarIdPer();
		int idtall= generarIdTall();
		String[] tel=req.getParameterValues("telefono[]");
		String sql="";
		int idprov=Integer.parseInt(req.getParameter("idprov"));
		System.out.println("PER:"+p.toString());
		System.out.println("iddep:"+req.getParameter("iddep"));
		System.out.println("idprov:"+req.getParameter("idprov"));
		
		if(tel!=null) {
			System.out.println("TelefonosArray: "+tel.length);
			for (String i : tel) {
				System.out.println("telefonos: "+i);
//				System.out.println("vacio? : "+i.equals(null));
				System.out.println("vacio? : "+!i.equals(""));
				System.out.println("vacio? : "+i.equals(""));
			}
		}
		try {
			sql="INSERT INTO persona(idper,ci,ciCod,razonsocial,email,estado) VALUES(?,?,?,?,?,?)";
			this.db.update(sql,idper,p.getCi(),p.getCiCod(),p.getRazonsocial().toUpperCase(),p.getEmail(),1);
			
			//actualizando telefonos
			if(tel!=null) {
				sql="DELETE FROM telefono WHERE idper=?";
				this.db.update(sql,new Object[] {idper});
				System.out.println("TelefonosArray: "+tel.length);
				sql="insert into telefono(numero,idper) values(?,?)";
				for (int i = 0; i < tel.length; i++) {
					if(!tel[i].equals("")) {
						this.db.update(sql,tel[i],idper);
					}
				}
			}
			sql="INSERT INTO taller(idtall,nombretall,direccion,estado,idprov,idper) VALUES(?,?,?,?,?,?)";
			this.db.update(sql,idtall,req.getParameter("nombretall").toUpperCase(),req.getParameter("direcciontall").toUpperCase(),1,idprov,idper);
		
			return true;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}
	}
	@Transactional
	public Taller datosModificar(HttpServletRequest req){
		String sql="";
		Taller t=null;
		int idtall=Integer.parseInt(req.getParameter("idtall"));
		System.out.println("idtall: "+idtall);
		try {
			sql="SELECT t.* FROM taller t WHERE t.idtall=?";
			t=this.db.queryForObject(sql,new objTaller_mod(),idtall);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
			t=null;
		}
		return t;
	}
	@Transactional
	public boolean modificar(HttpServletRequest req,Persona p,String [] tel){
		
		String nombreTaller=req.getParameter("nombretall");
		String direccionTaller=req.getParameter("direcciontall");
		int idtall=Integer.parseInt(req.getParameter("idtall"));	
		int idper=Integer.parseInt(req.getParameter("idper"));	
		System.out.println("nombreTaller:"+nombreTaller);
		System.out.println("direccionTaller:"+direccionTaller);
		System.out.println("idtall:"+idtall);
		String sql="";
		try {

			sql="UPDATE persona SET ci=?,ciCod=?,razonsocial=?,email=? WHERE idper=?";
			this.db.update(sql,p.getCi(),p.getCiCod(),p.getRazonsocial().toUpperCase(),p.getEmail(),idper);
//			
			if(tel!=null) {
				sql="delete from telefono where idper=?";
				this.db.update(sql,new Object[] {idper});
				
				sql="insert into telefono(numero,idper) values(?,?)";
				for (int i = 0; i < tel.length; i++) {
					if(!tel[i].equals("")) {
						this.db.update(sql,tel[i],idper);
					}
				}
			}
			sql="UPDATE taller SET nombretall=?,direccion=? WHERE idtall=?";
			this.db.update(sql,nombreTaller.toUpperCase(),direccionTaller.toUpperCase(),idtall);
			
			return true;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}
	}
	@Transactional
	public boolean eliminar(Integer id){
		String sql="";
		try {
			sql="UPDATE taller  SET estado=0 WHERE idtall=?";
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
			sql="UPDATE taller SET estado=1 WHERE idtall=?";
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
}
