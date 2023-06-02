package app.restController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import app.manager.ManejadorTipoMarcaVehiculo;
import app.models.MarcaVehiculo;
import app.models.Persona;
import app.models.TipoMarcaVehiculo;

 
@RequestMapping("/RestTipoMarcaVehiculo/")
@RestController
public class RestTipoMarcaVehiculo{ 

	@Autowired
	ManejadorTipoMarcaVehiculo manejadorTipoMarcaVehiculo;


	      
	@RequestMapping(value="listar")
	public ResponseEntity<List<TipoMarcaVehiculo>> listar(HttpServletRequest req,HttpServletResponse res){	
		List<TipoMarcaVehiculo> lista=this.manejadorTipoMarcaVehiculo.ListarTipoMarca(req);
		System.out.println("listaDoc: "+lista.toString());
		return new ResponseEntity<List<TipoMarcaVehiculo>>(lista,HttpStatus.OK);
	}

	@RequestMapping(value="getMarcaVeh")
	public ResponseEntity<List<Object>> getMarcaVeh(HttpServletRequest req){
		List<Object> data=new ArrayList<Object>();
		List<MarcaVehiculo> marca=this.manejadorTipoMarcaVehiculo.ListarMarcaVeh();
		data.add(marca);
		System.out.println("data:"+data.toString());
		return new ResponseEntity<List<Object>>(data,HttpStatus.OK);		
	}
	
	@Transactional
	@RequestMapping(value="adicionar")
	public Map<String, Object> adicionar(HttpServletRequest req,HttpServletResponse res){
		HttpSession sesion=req.getSession(true);
		Persona xuser=(Persona) sesion.getAttribute("xusuario");
		Map<String, Object> respuesta=new HashMap<String, Object>();
		try{
			boolean consulta=this.manejadorTipoMarcaVehiculo.registrar(req,xuser);
			System.out.println("resp: "+consulta);
			respuesta.put("estado", consulta);
		}catch (Exception e){
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			respuesta.put("estado",false);
		}
		return respuesta;
	}
	@RequestMapping(value="datos-mod")
	public ResponseEntity<List<Object>> datosMod(HttpServletRequest req){
		List<Object>lista =new ArrayList<Object>();
		TipoMarcaVehiculo tipoM=this.manejadorTipoMarcaVehiculo.datosModificar(req);
		List<MarcaVehiculo> marcasVeh=this.manejadorTipoMarcaVehiculo.getListMarcas();
		lista.add(tipoM);
		lista.add(marcasVeh);
		System.out.println("datos mod:"+lista);
		return new ResponseEntity<List<Object>>(lista,HttpStatus.OK);		
	}
	@Transactional
	@RequestMapping(value="modificar")
	public Map<String, Object> modificar(HttpServletRequest req,HttpServletResponse res){
		Map<String, Object> respuesta=new HashMap<String, Object>();
		HttpSession sesion=req.getSession(true);
		Persona xuser=(Persona) sesion.getAttribute("xusuario");
		try {
			boolean consulta=this.manejadorTipoMarcaVehiculo.modificar(req,xuser);
			System.out.println(consulta);
			respuesta.put("estado", consulta);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			respuesta.put("estado",false);
		}
		return respuesta;
	}
	

	@RequestMapping(value="eliminar")
	public ResponseEntity<Map<String, Object>> elim(HttpServletRequest req){
		System.out.println("lego eliminar");
//		HttpSession sesion=req.getSession(true);
//		Persona xuser=(Persona) sesion.getAttribute("xusuario");
		String idtipmarc=req.getParameter("idtipmarc");
		System.out.println("idtipmarc: "+idtipmarc);
		Map<String, Object> respuesta=new HashMap<String, Object>();
		try {
			boolean resp=this.manejadorTipoMarcaVehiculo.eliminar(Integer.parseInt(idtipmarc));
			respuesta.put("estado", resp);
		} catch (Exception e) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			respuesta.put("estado",false);
		}
		return new ResponseEntity<Map<String,Object>>(respuesta,HttpStatus.OK);	
	}
	@RequestMapping(value="habilitar")
	public ResponseEntity<Map<String, Object>> habil(HttpServletRequest req){
		System.out.println("lego eliminar");
//		HttpSession sesion=req.getSession(true);
//		Persona xuser=(Persona) sesion.getAttribute("xusuario");
		String idtipmarc=req.getParameter("idtipmarc");
		System.out.println("idtipmarc: "+idtipmarc);
		Map<String, Object> respuesta=new HashMap<String, Object>();
		try {
			boolean resp=this.manejadorTipoMarcaVehiculo.habilitar(Integer.parseInt(idtipmarc));
			respuesta.put("estado", resp);
		} catch (Exception e) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			respuesta.put("estado",false);
		}
		return new ResponseEntity<Map<String,Object>>(respuesta,HttpStatus.OK);	
	}


	
}
