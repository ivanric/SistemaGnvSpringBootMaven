package app.utilidades;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.manager.ManejadorServicios;
import app.rowmapper.Telefono;
import app.rowmapper.Usuario;
import app.service.ITelefonoService;
import app.service.IUsuarioService;


@Service
public class OpcionesAdicionales {
	@Autowired private IUsuarioService iUsuarioService;
	@Autowired private ITelefonoService iTelefonoService;
	@Autowired
	ManejadorServicios manejadorServicios;
	
	public List<Telefono> getTelefonosbyIdper(int idper) {
		return iTelefonoService.getAllTelefonosById(idper);
	}
	public Usuario getUserByIdper(int id) {
		return iUsuarioService.getUsuarioById(id);
	}
	
//	public String getNit() {
//		Map<String, Object> nitSQL=this.manejadorServicios.nitEmpresa(1); 
//		String nit_patam=(String) nitSQL.get("nitInst");
//		return nit_patam;
//	}
	
	public String convertFecha(String fecha) {
		String fechaF="";
		SimpleDateFormat  inSDF = new SimpleDateFormat("yyyy-mm-dd");
		SimpleDateFormat outSDF = new SimpleDateFormat("dd/mm/yyyy");
		Date date;
		try {
			date = inSDF.parse(fecha);
			fechaF=outSDF.format(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		return fechaF;
	}
	public java.sql.Date getDateSQL(String fecha){
		String fechaF="";
		SimpleDateFormat  inSDF = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat outSDF = new SimpleDateFormat("dd/MM/yyyy");
		Date date;
		Date date1=null;
		java.sql.Date salida=null; 
		
		try {
			date = inSDF.parse(fecha);
			System.out.println("date:"+date);
//			fechaF=outSDF.format(date);
//			System.out.println("fechaF:"+fechaF);
//			date1= outSDF.parse(fechaF);
//			System.out.println("date1:"+date1.toString());
			salida=new java.sql.Date(date.getTime());
			System.out.println("salida:"+salida);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}
		return salida;

	}

}
