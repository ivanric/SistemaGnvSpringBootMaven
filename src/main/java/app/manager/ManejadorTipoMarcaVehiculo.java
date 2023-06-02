package app.manager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import app.manager.ManejadorDocumentos.objDoc;
import app.models.DocumentoBeneficiario;
import app.models.MarcaVehiculo;
import app.models.Persona;
import app.models.TipoMarcaVehiculo;


@Service
public class ManejadorTipoMarcaVehiculo {
	private final  JdbcTemplate db;
	public ManejadorTipoMarcaVehiculo(JdbcTemplate jdbcTemplate) {
		this.db = jdbcTemplate;
		
	}
	
	public class objMarcaVehiculo implements RowMapper<MarcaVehiculo>{
		@Override
		public MarcaVehiculo mapRow(ResultSet rs, int arg1) throws SQLException {
			MarcaVehiculo mv= new MarcaVehiculo();
			mv.setIdmarcv(rs.getInt("idmarcv"));
			mv.setNombre(rs.getString("nombre"));
			mv.setEstado(rs.getInt("estado"));
			return mv;
	    }
	}
	
	public class objTipoMarcaVehiculo implements RowMapper<TipoMarcaVehiculo>{
		@Override
		public TipoMarcaVehiculo mapRow(ResultSet rs, int arg1) throws SQLException {
			TipoMarcaVehiculo mv= new TipoMarcaVehiculo();
			mv.setIdtipmarc(rs.getInt("idtipmarc"));
			mv.setNombre(rs.getString("nombre"));
			mv.setEstado(rs.getInt("estado"));
			try {
				mv.setMarcaVehiculo(metMarca(rs.getInt("idmarcv")));
			} catch (Exception e) {
				mv.setMarcaVehiculo(null);;
			}
			return mv;
	    }
	}
	@Transactional
	public MarcaVehiculo metMarca(int idmarcv){
		return this.db.queryForObject("select * from marcaVehiculo  where idmarcv=? and estado=1", new objMarcaVehiculo(),idmarcv);
	}
	@Transactional
	public List<TipoMarcaVehiculo> ListarTipoMarca(HttpServletRequest req){
		String filtro=req.getParameter("filtro");
		int estado=Integer.parseInt(req.getParameter("estado"));
		String sql="SELECT d.* FROM tipoMarcaVeh d where (d.nombre LIKE ?) and (d.estado=? or ?=-1) ORDER BY d.idtipmarc ASC";
		return this.db.query(sql, new objTipoMarcaVehiculo(),"%"+filtro+"%",estado,estado);
	}
	
	@Transactional
	public List<Map<String,Object>> ListarD(HttpServletRequest req){
		String filtro=req.getParameter("filtro");
		int estado=Integer.parseInt(req.getParameter("estado"));
		String sql="SELECT d.* FROM docBeneficiario d where (d.nombre LIKE ?) and (d.estado=? or ?=-1) ORDER BY d.iddocb ASC";
		return this.db.queryForList(sql, new Object[]{},"%"+filtro+"%",estado,estado);
	}
	@Transactional
	public List<MarcaVehiculo> ListarMarcaVeh(){
		String sql="SELECT d.* FROM marcaVehiculo d where estado=1 ORDER BY d.idmarcv ASC";
		return this.db.query(sql, new objMarcaVehiculo());
	}
	@Transactional
	public boolean registrar(HttpServletRequest req,Persona xuser) {
		String sql="";
		int id=generarIdTipoMarca();
		try {
			sql="INSERT INTO tipoMarcaVeh(idtipmarc,nombre,idmarcv) VALUES(?,?,?)";
			this.db.update(sql,id,req.getParameter("tipo").toUpperCase(),req.getParameter("marcaVeh"));
			return true;
		}catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
			return false;
		}
	}
	@Transactional
	public TipoMarcaVehiculo datosModificar(HttpServletRequest req){
		String sql="";
		TipoMarcaVehiculo tm=null;
		int idtipmarc=Integer.parseInt(req.getParameter("idtipmarc"));
		System.out.println("idtipmarc: "+idtipmarc);
		try {
			sql="SELECT tp.* FROM tipoMarcaVeh tp WHERE tp.idtipmarc=?";
			tm=this.db.queryForObject(sql,new objTipoMarcaVehiculo(),idtipmarc);
		} catch (Exception e) {
			tm=null;
		}
		return tm;
	}
	@Transactional
	public List<MarcaVehiculo> getListMarcas(){
		String sql="";
		try {
			sql="select * FROM marcaVehiculo WHERE estado=1 ORDER BY  idmarcv ASC";
			return this.db.query(sql,new objMarcaVehiculo());
		} catch (Exception e) {
			// TODO: handle exceptionr
			return null;
		}
		
	}
	@Transactional
	public boolean modificar(HttpServletRequest req,Persona xuser){
		String sql="";
		int iddocb=Integer.parseInt(req.getParameter("idtipmarc"));
		
		try {
			sql="UPDATE tipoMarcaVeh SET nombre=?,idmarcv=? WHERE idtipmarc=?";
			this.db.update(sql,req.getParameter("tipo").toUpperCase(),req.getParameter("marcaVeh"),iddocb);
			
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
			sql="UPDATE tipoMarcaVeh SET estado=0 WHERE idtipmarc=?";
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
			sql="UPDATE tipoMarcaVeh SET estado=1 WHERE idtipmarc=?";
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
	@Transactional
	public int generarIdTipoMarca(){
		String sql="select COALESCE(max(idtipmarc),0)+1 as idtipmarc from tipoMarcaVeh";
		return db.queryForObject(sql, Integer.class);
	}
}
