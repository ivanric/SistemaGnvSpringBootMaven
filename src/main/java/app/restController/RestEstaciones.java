package app.restController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import app.manager.ManejadorEstaciones;
import app.models.Estacion;
import app.models.Persona;
import app.models.Taller;

 
@RequestMapping("/RestEstaciones/")
@RestController
public class RestEstaciones{ 
	@Autowired
	ManejadorEstaciones manejadorEstaciones;

	      
	@RequestMapping(value="listar")
	public ResponseEntity<List<Estacion>> listarServicio(HttpServletRequest req,HttpServletResponse res){	
		List<Estacion> lista=this.manejadorEstaciones.ListarEstaciones(req);
		System.out.println("listaRoles: "+lista.toString());
		return new ResponseEntity<List<Estacion>>(lista,HttpStatus.OK);
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping("listar_d")
	public @ResponseBody Map<?, ?> lista(HttpServletRequest request, Integer draw,int length, Integer start, int estado)throws IOException{
		String total;
		Map<String, Object> Data = new HashMap<String, Object>();
		String search = request.getParameter("search[value]");
		List<Estacion> lista=this.manejadorEstaciones.listar_d(start, estado, search,length);
		System.out.println("lista:"+lista.toString());
		try {
			total=String.valueOf(lista.get(0).getTot());			
		} catch (Exception e) {
			total="0";
		}
		Data.put("draw", draw);
		Data.put("recordsTotal", total);
		Data.put("data", lista);
		if(!search.equals(""))
			Data.put("recordsFiltered", lista.size());
		else
			Data.put("recordsFiltered", total);
		return Data;

	}
	
	@RequestMapping(value="getDatosEstaciones")
	public ResponseEntity<List<Object>> docuemtosBeneficiario(HttpServletRequest req,HttpServletResponse res){	
		List<Object> lista=new ArrayList<Object>();
		List<Map<String, Object>> listaDocumentos=this.manejadorEstaciones.listaDepartamentos();
		lista.add(listaDocumentos);
		return new ResponseEntity<List<Object>>(lista,HttpStatus.OK);
	}
	@RequestMapping(value="getProvinciasByIdDep")
	public ResponseEntity<List<Object>> getProvinciasByIdDep(HttpServletRequest req,HttpServletResponse res){	
		List<Object> lista=new ArrayList<Object>();
		
		int iddep=Integer.parseInt(req.getParameter("iddep"));
		List<Map<String, Object>> listaProvincias=this.manejadorEstaciones.listaProvincias(iddep);
		lista.add(listaProvincias);
		return new ResponseEntity<List<Object>>(lista,HttpStatus.OK);
	}
	@RequestMapping(value="getProvinciasByDepart")
	public ResponseEntity<List<Object>> getTipoMarcaVehByMarcaByMarca(HttpServletRequest req){
		List<Object> data=new ArrayList<Object>();
		
		List<Map<String, Object>> marca=this.manejadorEstaciones.getProvinciasByDepart(req);
		data.add(marca);
		System.out.println("data:"+data.toString());
		return new ResponseEntity<List<Object>>(data,HttpStatus.OK);		
	}
	
	@Transactional
	@RequestMapping(value="adicionar")
	public Map<String, Object> adicionar(HttpServletRequest req,Persona p,HttpServletResponse res){
//		HttpSession sesion=req.getSession(true);
//		Persona xuser=(Persona) sesion.getAttribute("xusuario");
		Map<String, Object> respuesta=new HashMap<String, Object>();
		try{
			boolean consulta=this.manejadorEstaciones.registrar(req,p);
			System.out.println("resp: "+consulta);
			respuesta.put("estado", consulta);
		}catch (Exception e){
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			respuesta.put("estado",false);
		}
		return respuesta;
	}
	
	
	
	@Transactional
	@RequestMapping(value="adicionar_d")
	public Map<String, Object> adicionar_d(HttpServletRequest req,Persona p,HttpServletResponse res){
//		HttpSession sesion=req.getSession(true);
//		Persona xuser=(Persona) sesion.getAttribute("xusuario");
		Map<String, Object> respuesta=new HashMap<String, Object>();
		try{
//			boolean consulta=true;
			boolean consulta=this.manejadorEstaciones.registrar_d(req,p);
			System.out.println("resp: "+consulta);
			respuesta.put("estado", consulta);
		}catch (Exception e){
			System.out.println(e.getMessage());
			e.printStackTrace();
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			respuesta.put("estado",false);
		}
		return respuesta;
	}
	

	
	@RequestMapping(value="datos-mod")
	public ResponseEntity<Estacion> datosMod(HttpServletRequest req){
		Estacion data=this.manejadorEstaciones.datosModificar(req);
		
		System.out.println("Rol a Modificar:"+data);
		return new ResponseEntity<Estacion>(data,HttpStatus.OK);		
	}
	
	@Transactional
	@RequestMapping(value="modificar")
	public Map<String, Object> modificar(HttpServletRequest req,HttpServletResponse res,Persona p){
		Map<String, Object> respuesta=new HashMap<String, Object>();
//		HttpSession sesion=req.getSession(true);
//		Persona xuser=(Persona) sesion.getAttribute("xusuario");
		String[] telefonos=req.getParameterValues("telefono[]");
		System.out.println("p: "+p.toString());
		try {
			boolean consulta=this.manejadorEstaciones.modificar(req, p,telefonos);
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
		String idest=req.getParameter("idest");
		System.out.println("idest: "+idest);
		Map<String, Object> respuesta=new HashMap<String, Object>();
		try {
			boolean resp=this.manejadorEstaciones.eliminar(Integer.parseInt(idest));
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
		String idest=req.getParameter("idest");
		System.out.println("idest: "+idest);
		Map<String, Object> respuesta=new HashMap<String, Object>();
		try {
			boolean resp=this.manejadorEstaciones.habilitar(Integer.parseInt(idest));
			respuesta.put("estado", resp);
		} catch (Exception e) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			respuesta.put("estado",false);
		}
		return new ResponseEntity<Map<String,Object>>(respuesta,HttpStatus.OK);	
	}
	
}
