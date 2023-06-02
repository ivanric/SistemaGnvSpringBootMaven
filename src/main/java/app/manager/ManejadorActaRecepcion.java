package app.manager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import app.manager.ManejadorBeneficiarios.objPersona;
import app.manager.ManejadorServicios.objOrdenServicio;
import app.models.ActaRecepcion;
import app.models.Beneficiario;
import app.models.OrdenPago;
import app.models.OrdenServicio;
import app.models.Persona;

@Service
public class ManejadorActaRecepcion {

	private final  JdbcTemplate db;
	public ManejadorActaRecepcion(JdbcTemplate jdbcTemplate) {
		this.db = jdbcTemplate;
		
	}
	public class objActaRecepcion implements RowMapper<ActaRecepcion>{
		@Override
		public ActaRecepcion mapRow(ResultSet rs, int arg1) throws SQLException {
			ActaRecepcion ar= new ActaRecepcion();
			ar.setIdrecep(rs.getInt("idrecep"));
			ar.setNumrecep(rs.getString("numRecep"));
			ar.setFechaActaRecepcion(rs.getString("fecha"));
			ar.setAlmacenesSiNo(rs.getInt("almacenesSiNo"));
			ar.setActivosFijosSiNo(rs.getInt("activosFijosSiNo"));
			ar.setServGeneralesSiNo(rs.getInt("servGeneralesSiNo"));
			ar.setIdordserv(rs.getInt("idordserv"));
			
			return ar;
	    }
	}
	public class objOrdenPago implements RowMapper<OrdenPago>{
		@Override
		public OrdenPago mapRow(ResultSet rs, int arg1) throws SQLException {
			OrdenPago op=new OrdenPago();
			op.setIdOrdPago(rs.getInt("idOrdPago"));
			op.setNumOrdPago(rs.getString("numOrdPago"));
			op.setPrecio(rs.getString("precio"));
			op.setFechaOrdPago(rs.getString("fechaOrdPago"));
			op.setIdrecep(rs.getInt("idrecep"));
			op.setLogin(rs.getString("login"));
			try {
				op.setActaRecepcion(metActaRecep(rs.getInt("idrecep")));
			}catch (Exception e){
				op.setActaRecepcion(null);
			}
			return op;
		}
	}
	@Transactional
	public ActaRecepcion metActaRecep(int id){
		String sql="SELECT DISTINCT a.* FROM actaRecepcion a JOIN ordenPago op ON op.idrecep=a.idrecep and a.idrecep=?";
		return this.db.queryForObject(sql,new objActaRecepcion(),id);
	}
	@Transactional
	public List<ActaRecepcion> Lista(HttpServletRequest req){
		String filtro=req.getParameter("filtro");
		//int estado=Integer.parseInt(req.getParameter("estado"));
		String sql="select ar.* from actaRecepcion ar join ordenServicio os on ar.idordserv=os.idordserv JOIN solicitud s ON s.idsolt=os.idsolt  JOIN benVehSolt bvs ON bvs.idsolt=s.idsolt JOIN beneficiario b ON b.idben=bvs.idben AND bvs.perteneceSiNo=1 JOIN persona per ON per.idper=b.idper where (concat(per.ap,' ',per.am,' ',per.nombres) LIKE ?) ORDER BY ar.idrecep ASC";
		return this.db.query(sql, new objActaRecepcion(),"%"+filtro+"%");
	}
	@Transactional
	public int getAccionAR(){
		String sql="SELECT idacc FROM accion WHERE tipo='ar'";
		return this.db.queryForObject(sql,Integer.class);
	}
	@Transactional
	public int getIdSoltByAR(int id){
		String sql="SELECT s.idsolt FROM ordenServicio os,solicitud s WHERE s.idsolt=os.idsolt AND os.idordserv=?";
		return this.db.queryForObject(sql,Integer.class,id);
	}
	

	@Transactional
	public Object[] registrar(HttpServletRequest req,Persona xuser) {
		String sql="";
		Object [] resp=new Object[2];
		String login=xuser.getUsuario().getLogin();
		int idrecep=idRecep();
		int numrecep=Integer.parseInt(req.getParameter("numRecep"));
		int idordserv=Integer.parseInt(req.getParameter("idordserv"));
		int almacenes=Integer.parseInt(req.getParameter("almacenes"));
		int activosFijos=Integer.parseInt(req.getParameter("activosFijos"));
		int serviciosGenerales=Integer.parseInt(req.getParameter("serviciosGenerales"));
		System.out.println("numrecep: "+numrecep+" :idordserv:"+idordserv+" almacenes:"+almacenes+" activosFijos:"+activosFijos+" serviciosGenerales:"+serviciosGenerales);
 
		try {
			sql="INSERT INTO actaRecepcion(idrecep,numrecep,almacenesSiNo,activosFijosSiNo,servGeneralesSiNo,idordserv,login) VALUES(?,?,?,?,?,?,?)";
			this.db.update(sql,idrecep,numrecep,almacenes,activosFijos,serviciosGenerales,idordserv,login);
			
			sql="UPDATE solicitud SET idacc=? WHERE idsolt=?";
			this.db.update(sql,getAccionAR(),getIdSoltByAR(idordserv));
			
			resp[0]=true;
			resp[1]=idrecep;
			return resp;
		}catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
			resp[0]=false;
			return resp;
		}
	}
	

	@Transactional
	public int getAccionOP(){
		String sql="SELECT idacc FROM accion WHERE tipo='op'";
		return this.db.queryForObject(sql,Integer.class);
	}
	@Transactional
	public int getIdSoltByOP(int idrecep){
		String sql="SELECT s.idsolt FROM actaRecepcion ar,ordenServicio os,solicitud s WHERE  ar.idordserv=os.idordserv and s.idsolt=os.idsolt AND ar.idrecep=?";
		return this.db.queryForObject(sql,Integer.class,idrecep);
	}
	@Transactional
	public Object[] registrarOP(HttpServletRequest req,Persona xuser) {
		String sql="";
		Object [] resp=new Object[2];
		String login=xuser.getUsuario().getLogin();
		int idOrdPago=idOrdPago();
		String numOrdPago=req.getParameter("numOrdPago");
		int idrecep=Integer.parseInt(req.getParameter("idrecep"));
//		String precio=req.getParameter("precio");

		System.out.println("numOrdPago: "+numOrdPago+" :idrecep:"+idrecep);
 
		try {
//			sql="INSERT INTO ordenPago(idOrdPago,numOrdPago,precio,idrecep,login) VALUES(?,?,?,?,?)";
			sql="INSERT INTO ordenPago(idOrdPago,numOrdPago,idrecep,login) VALUES(?,?,?,?)";
			this.db.update(sql,idOrdPago,numOrdPago,idrecep,login);
			
			sql="UPDATE solicitud SET idacc=? WHERE idsolt=?";
			this.db.update(sql,getAccionOP(),getIdSoltByOP(idrecep));
			
			resp[0]=true;
			resp[1]=idOrdPago;
			return resp;
		}catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
			resp[0]=false;
			return resp;
		}
	}
	@Transactional
	public ActaRecepcion verActaRecepcion(int id) {
		String sql="select * from actaRecepcion where idrecep=?";
		return this.db.queryForObject(sql,new objActaRecepcion(),id);
	}
	@Transactional
	public int numeroRecep(){
		String sql="select COALESCE(max(numrecep),0)+1 as numrecep from actaRecepcion";
		return db.queryForObject(sql, Integer.class);
	}
	@Transactional
	public int idRecep(){
		String sql="select COALESCE(max(idrecep),0)+1 as idrecep from actaRecepcion";
		return db.queryForObject(sql, Integer.class);
	}
	@Transactional
	/*ORDEN DE PAGO*/
	public int idOrdPago(){
		String sql="select COALESCE(max(idOrdPago),0)+1 as idOrdPago from ordenPago";
		return db.queryForObject(sql, Integer.class);
	}
	@Transactional
	public int numeroOrdPago(){
		String sql="select COALESCE(max(numOrdPago),0)+1 as numOrdPago from ordenPago";
		return db.queryForObject(sql, Integer.class);
	}
	@Transactional
	public List<OrdenPago> ListaOP(HttpServletRequest req){
		String filtro=req.getParameter("filtro");
		String sql="select op.* from ordenPago op JOIN actaRecepcion ar ON ar.idrecep=op.idrecep join ordenServicio os on ar.idordserv=os.idordserv JOIN solicitud s ON s.idsolt=os.idsolt  where (os.numords LIKE ? OR s.numSolt LIKE ? OR op.numOrdPago LIKE ? )  ORDER BY op.idOrdPago ASC";
		return this.db.query(sql, new objOrdenPago(),"%"+filtro+"%","%"+filtro+"%","%"+filtro+"%");
	}
	@Transactional
	public List<ActaRecepcion> FiltroActaRecepcionOP(String cadena){
		String sql="SELECT ar.* FROM actaRecepcion ar,ordenServicio os,solicitud s,vehiculo veh,beneficiario b,persona p,benVehSolt bvs WHERE ar.idordserv=os.idordserv AND os.idsolt=s.idsolt AND os.instaladoSiNo=1 AND bvs.idben=b.idben AND bvs.perteneceSiNo=1 AND bvs.placa=veh.placa AND bvs.idsolt=s.idsolt and b.idper=p.idper and (os.numords LIKE ? or s.numSolt LIKE ? or p.ci LIKE ?) AND ar.idrecep NOT IN (SELECT op.idrecep FROM ordenPago op WHERE op.idrecep=ar.idrecep)";
		return this.db.query(sql, new objActaRecepcion(),'%'+cadena+'%','%'+cadena+'%','%'+cadena+'%');
	}
	@Transactional
	public int getIdRegistroKit(int id){
		String sql="SELECT DISTINCT rk.idregistroKit FROM registroKit rk,ordenServicio os,ordenPago op,actaRecepcion ac WHERE rk.idordserv=os.idordserv AND ac.idordserv=rk.idordserv AND ac.idrecep=op.idrecep  AND op.idOrdPago=?";
		return db.queryForObject(sql, Integer.class,id);
	}
	@Transactional
	public OrdenPago verOrdenPago(int id) {
		String sql="select * from ordenPago where idOrdPago=?";
		return this.db.queryForObject(sql,new objOrdenPago(),id);
	}
	
}
