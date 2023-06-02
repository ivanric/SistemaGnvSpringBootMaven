package app.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import app.manager.ManejadorUsuarios;
import app.manager.ManejadorTalleres.objTaller;
import app.rowmapper.Persona;
import app.rowmapper.PersonaRowMapper;
import app.rowmapper.Telefono;
import app.rowmapper.Usuario;
import app.utilidades.ALGORITMO3DES_Ecript_Desencript;

@Transactional
@Repository
public class UsuarioDAO implements IUsuarioDAO {
	private JdbcTemplate db;
	@Autowired
	public void setDataSource(DataSource dataSource){
		db = new JdbcTemplate(dataSource);
	}
	
	@Autowired private ITelefonoDAO telefonoDAO;
	@Autowired 
	ManejadorUsuarios manejadorUsuarios;
	
	
	@Override
	public List<Persona> findAll(HttpServletRequest req){
		String filtro=req.getParameter("filtro");
		int estado=Integer.parseInt(req.getParameter("estado"));
		String sql="select p.* from persona p join usuario u ON u.idper=p.idper where (concat(p.ap,' ',p.am,' ',p.nombres) LIKE ? or p.ci LIKE ?) and (p.estado=? or ?=-1) ORDER BY p.idper ASC";
		RowMapper<Persona> rowMapper=new PersonaRowMapper();

		
		return this.db.query(sql, rowMapper,"%"+filtro+"%","%"+filtro+"%",estado,estado);
	}
	@Override
	public List<Persona> listar_d(int start, int estado, String search, int length) {
		String sql="";
		System.out.println("estado:"+estado);
		try {
			sql="select COUNT (*) \r\n"
					+ "FROM persona p \r\n"
					+ "JOIN usuario u ON u.idper=p.idper \r\n"
					+ "WHERE (concat(p.razonsocial) LIKE concat('%',upper(?),'%') or p.ci LIKE concat('%',upper(?),'%')) and (p.estado=? or ?=-1) ";
			Integer tot=db.queryForObject(sql, Integer.class,search,search,estado,estado);
			System.out.println("Tot:"+tot);
			sql="select  p.*,row_number() OVER(ORDER BY p.idper) as RN, ? as tot\r\n"
					+ "FROM persona p \r\n"
					+ "JOIN usuario u ON u.idper=p.idper \r\n"
					+ "WHERE (concat(p.razonsocial) LIKE concat('%',upper(?),'%') or p.ci LIKE concat('%',upper(?),'%')) and (p.estado=? or ?=-1) \r\n"
					+ "ORDER BY p.idper ASC\r\n"
					+ "LIMIT ? OFFSET ?";

//			RowMapper<Persona> rowMapper=new PersonaRowMapper();
			RowMapper<Persona> rowMapper=new RowMapper<Persona>() {
				
				@Override
				public Persona mapRow(ResultSet rs, int arg1) throws SQLException {
					Persona p= new Persona();
					p.setIdper(rs.getInt("idper"));
					p.setCi(rs.getString("ci"));
					p.setCiCod(rs.getString("ciCod"));
					p.setDireccion(rs.getString("direccion"));
					p.setFoto(rs.getString("foto"));
					p.setEstado(rs.getInt("estado"));
					p.setRazonsocial(rs.getString("razonsocial"));
					try {
						p.setUsuario(metUsuario(rs.getInt("idper")));
					}catch (Exception e){
						e.printStackTrace();
						System.out.println(e.getMessage());
						p.setUsuario(null);
					}
					
					p.setTot(rs.getInt("tot"));
					
					return p;
			    }
				
			};
			
//			System.out.println("per");
			return db.query(sql,rowMapper,tot,search,search,estado,estado,length,start);
		} catch (Exception e) {
			e.printStackTrace();    
			System.out.println(e.getMessage());
			return null;
		}
	}

	
	@Override
	public Usuario getUsuarioById(int idper) {
		String sql="select * from usuario where idper=?";
		RowMapper<Usuario> rowMapper=new BeanPropertyRowMapper<Usuario>(Usuario.class);
		Usuario u=this.db.queryForObject(sql,rowMapper,idper);
		return u;
	}
	@Override
	public boolean save(HttpServletRequest req,String[] tel, String nombreFoto) {
		ALGORITMO3DES_Ecript_Desencript des=new ALGORITMO3DES_Ecript_Desencript();
		int idper= generarIdPer();
		String ci=req.getParameter("ci");
		String ciCod=req.getParameter("ciCod");
		String razonsocial=req.getParameter("razonsocial");
//		String ap=req.getParameter("ap");
//		String am=req.getParameter("am");
//		String genero=req.getParameter("genero");
		String direccion=req.getParameter("direccion");
//		String email=req.getParameter("email");
		String usuario=req.getParameter("usuario");
		String password=req.getParameter("password");
		String tipouser=req.getParameter("tipouser");
		String sql="";
		try {
			System.out.println(ci);
			System.out.println(ciCod);
			System.out.println(razonsocial);
			System.out.println(direccion);
			System.out.println(nombreFoto);
			System.out.println();
			if(tipouser.equals("x")) {
				System.out.println("usuario nuevo");
				sql="INSERT INTO persona(idper,ci,ciCod,razonsocial,direccion,foto,estado) VALUES(?,?,?,?,?,?,?)";
//				this.db.update(sql,idper,des.Encriptar(ci),ciCod,nombres.toUpperCase(),ap.toUpperCase(),am.toUpperCase(),genero,direccion.toUpperCase(),email,nombreFoto,1);
				this.db.update(sql,idper,ci,ciCod,razonsocial.toUpperCase(),direccion.toUpperCase(),nombreFoto,1);
				
				sql="insert into telefono(numero,idper) values(?,?)";
				for (int i = 0; i < tel.length; i++) {
					if(!tel[i].equals("")) {
						this.db.update(sql,tel[i],idper);
					}
				}	
				sql="insert into usuario(login,password,idper,estado) values(?,?,?,?)";
				this.db.update(sql,usuario,des.Encriptar(password),idper,1);
			}else {
				System.out.println("Usuario encontrado");

				int idper_b=this.manejadorUsuarios.generarIdPer_b(ci);
				sql="UPDATE persona SET foto=? WHERE idper=?";
				this.db.update(sql,nombreFoto,idper_b);
				
				sql="insert into usuario(login,password,idper,estado) values(?,?,?,?)";
				this.db.update(sql,usuario,des.Encriptar(password),idper_b,1);
			}
			return true;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			System.out.println(e.getMessage());
			return false;
		}
		

		
	
	}


	
	@Override
	public int generarIdPer() {
		String sql="select COALESCE(max(idper),0)+1 as idper from persona";
		return db.queryForObject(sql, Integer.class);
		
	}

	@Override
	public Persona getPersonaById(int idper) {
		String sql="";
		Persona p=null;
		try {
			sql="SELECT p.* FROM persona p,usuario u WHERE u.idper=p.idper and p.idper=?";
			RowMapper<Persona> mapper=new BeanPropertyRowMapper<Persona>(Persona.class);
			p=this.db.queryForObject(sql,mapper,idper);
		} catch (Exception e) {
			p=null;
		}
		return p;
	}

	@Override
	public boolean modify(HttpServletRequest req, String[] tel, String nombreFoto) {
		String sql="";
		String ci=req.getParameter("ci");
		String ciCod=req.getParameter("ciCod");
		String razonsocial=req.getParameter("razonsocial");
//		String ap=req.getParameter("ap");
//		String am=req.getParameter("am");
//		String genero=req.getParameter("genero");
		String direccion=req.getParameter("direccion");
//		String email=req.getParameter("email");
		
		int idper=Integer.parseInt(req.getParameter("idper"));
		try {
			sql="UPDATE persona SET ci=?,ciCod=?,razonsocial=?,direccion=?,foto=? WHERE idper=?";
			this.db.update(sql,ci,ciCod,razonsocial.toUpperCase(),direccion.toUpperCase(),nombreFoto,idper);
			
			sql="delete from telefono where idper=?";
			this.db.update(sql,new Object[] {idper});
			
			sql="insert into telefono(numero,idper) values(?,?)";
			for (int i = 0; i < tel.length; i++) {
				if(!tel[i].equals("")) {
					String telefono=String.valueOf(tel[i]);
					this.db.update(sql,telefono,idper);
				}
			}

			return true;
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e.getMessage());
			e.printStackTrace();
			return false;
		}
	}
	@Override
	public boolean changeStatus(HttpServletRequest req){
		int idper=Integer.parseInt(req.getParameter("idper"));
		int estado=Integer.parseInt(req.getParameter("estado"));
		String sql="";
		int resp=0;
		try{
			if (estado==1) {
				sql="UPDATE persona SET estado=0 WHERE idper=?";	
				resp=this.db.update(sql,idper);
			}else{
				sql="UPDATE persona SET estado=1 WHERE idper=?";	
				resp=this.db.update(sql,idper);
			}

			if (resp==1) {
				return true;
			}else{
				return false;
			}

		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}

	
	private List<Telefono> metListaTelf(int idper) {
		// TODO Auto-generated method stub

		
		return telefonoDAO.getAllTelefonosById(idper);
	}
	
	private Usuario metUsuario(int idper) {
		System.out.println("idper_entrante:"+idper);
		// TODO Auto-generated method stub
		try {
			return this.getUsuarioById(idper);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			System.out.println(e.getMessage());
			return null;
		}
		
	}
}
