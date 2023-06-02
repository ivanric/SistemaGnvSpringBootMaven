package app.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;

import app.manager.ManejadorServicios;
import app.service.ITelefonoService;
import app.service.IUsuarioService;
import app.utilidades.OpcionesAdicionales;

public class PersonaRowMapper implements RowMapper<Persona>{
//	@Autowired private IUsuarioService iUsuarioService;
//	@Autowired private ITelefonoService iTelefonoService;

	
	@Override
	public Persona mapRow(ResultSet rs, int arg1) throws SQLException {
		Persona p= new Persona();
		p.setIdper(rs.getInt("idper"));
		p.setCi(rs.getString("ci"));
		p.setCiCod(rs.getString("ciCod"));
		p.setDireccion(rs.getString("direccion"));
		p.setEmail(rs.getString("email"));
		p.setFoto(rs.getString("foto"));
		p.setEstado(rs.getInt("estado"));

		p.setRazonsocial(rs.getString("razonsocial"));
//		System.out.println("idper1:"+rs.getInt("idper"));
//		System.out.println("idper2:"+p.getIdper());
//		try {
//			p.setUsuario(metUsuario(rs.getInt("idper")));
//		}catch (Exception e){
//			e.printStackTrace();
//			System.out.println(e.getMessage());
//			p.setUsuario(null);
//		}
//		
//		try {
//			p.setListaTelf(metListaTelf(rs.getInt("idper")));
//		}catch (Exception e){
//
//			e.printStackTrace();
//			System.out.println(e.getMessage());
//			p.setListaTelf(null);
//		}
		p.setTot(rs.getInt("tot"));
		
		return p;
    }

//	private List<Telefono> metListaTelf(int idper) {
//		// TODO Auto-generated method stub
//
//		
//		return iTelefonoService.getAllTelefonosById(idper);
//	}
//
//	private Usuario metUsuario(int idper) {
//		// TODO Auto-generated method stub
//		return iUsuarioService.getUsuarioById(idper);
//	}
	
}
