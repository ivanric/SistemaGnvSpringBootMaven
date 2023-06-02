package app.manager;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import app.models.MarcaReductor;
import app.utilidades.Db_Coneccion;
@Service
//public class ManejadorMarcaReductor extends Db_Coneccion {
	public class ManejadorMarcaReductor extends Db_Coneccion {
	
	private final  JdbcTemplate db;
	public ManejadorMarcaReductor(JdbcTemplate jdbcTemplate) {
		this.db = jdbcTemplate;
		
	}
	private String consulta;
	
	@Transactional
	public List<Map<String, Object>> listar(int start, int estado, String search, int length) throws SQLException {
		try {
			int tot = db.queryForObject("select count(*) from marcareductor where estado=?", Integer.class, estado);
			if (search == null)
				search = "";
//				consulta = "select marcareductor.*,row_number() OVER(ORDER BY marcareductor.idmarcred) as RN,? as Tot from marcareductor where (marcaReductor.estado=?) and (upper(nombre) like concat('%',upper(?),'%')) ORDER BY idmarcred ASC  OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
				consulta = "select marcareductor.*,row_number() OVER(ORDER BY marcareductor.idmarcred) as RN,? as Tot from marcareductor where (marcaReductor.estado=?) and (upper(nombre) like concat('%',upper(?),'%')) ORDER BY idmarcred ASC  LIMIT ? OFFSET ?";
			return db.queryForList(consulta, tot, estado, search,length,start);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
			// TODO: handle exception
			return null;
		}


	}
	@Transactional
	public boolean registrar(MarcaReductor obj) {
		System.out.println("MarcaReductor: "+obj.toString());
		try {
			consulta = "insert into marcaReductor(idmarcred,nombre,estado) values((select coalesce(max(idmarcred),0)+1 from marcaReductor),?,1);";
			db.update(consulta, obj.getNombre().toUpperCase());
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} 
	}
	@Transactional
	public Map<String, Object> obtener(MarcaReductor obj) throws SQLException {
		try {
			consulta = "select * from marcaReductor where idmarcred=?";
			return db.queryForMap(consulta, obj.getIdmarcred());
		} catch (Exception e) {
			return null;
		}
	}
	@Transactional
	public void modificar(MarcaReductor obj) {
		try {
			consulta = "update marcaReductor set nombre=? where idmarcred=?;";
			db.update(consulta, obj.getNombre().toUpperCase(), obj.getIdmarcred());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@Transactional
	public boolean eliminar(MarcaReductor obj) throws SQLException{
		try {
			db.update("update marcaReductor set estado=0 where idmarcred=?", obj.getIdmarcred());
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	@Transactional
	public boolean habilitar(MarcaReductor obj) throws SQLException{
		try {
			db.update("update marcaReductor set estado=1 where idmarcred=?", obj.getIdmarcred());
			return true;
		} catch (Exception e) {
			return false;
		}
	}

}
