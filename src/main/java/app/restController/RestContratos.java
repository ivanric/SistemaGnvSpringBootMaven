package app.restController;

import java.io.IOException;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.condition.ProducesRequestCondition;

import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.google.common.net.MediaType;

import app.manager.ManejadorServicios;
import app.manager.ManejadorSolicitudes;
import app.manager.ManejadorUsuarios;
import app.models.Persona;
import app.models.Servicio;
@RequestMapping("/RestContratos/")
@RestController
public class RestContratos {
	@Autowired
	ManejadorServicios manejadorServicios;
	@Autowired   
	ManejadorSolicitudes manejadorSolicitudes;

	@Autowired 
	ManejadorUsuarios manejadorUsuarios;
	
	@RequestMapping(value="planilla_contratos",method = RequestMethod.GET)
//	public @ResponseBody List<Map<String,Object>> lista(HttpServletRequest request,HttpSession sesion)throws IOException{
		public @ResponseBody List<Map<String,Object>> lista(HttpServletRequest request)throws IOException{
		System.out.println("contratos lista por usuario");
		Map<String, Object> Data = new HashMap<String, Object>();
		List<Map<String,Object>> lista=new ArrayList<Map<String,Object>>();
		
//		HttpSession sesion=request.getSession(false);
//		System.out.println(sesion);
//		Persona xuser=(Persona) sesion.getAttribute("xusuario");
		String usuario=request.getParameter("user");
		String contraseña=request.getParameter("password");
		
		Persona xuser=this.manejadorUsuarios.iniciarSession(usuario,contraseña);
		
		if (xuser!=null) {

			System.out.println("Xuser:"+xuser.getUsuario().getLogin());
		}
		System.out.println("User:"+usuario);
		if (xuser!=null && usuario.equals(xuser.getUsuario().getLogin())) {
			System.out.println("Ingresando..");
			try {
				lista=this.manejadorServicios.listarContratosPorUsuario(usuario);
				Data.put("data", lista);
			} catch (Exception e) {
				Data.put("data", lista);
				lista=null;
				System.out.println(e.getMessage());
				e.printStackTrace();
			}
		}else {
			lista=null;
			System.out.println("lista null");
		}


		
		return lista;

	}
	
	@RequestMapping(value="planilla_contratos_recalificacion",method = RequestMethod.GET)
//	public @ResponseBody List<Map<String,Object>> lista(HttpServletRequest request,HttpSession sesion)throws IOException{
		public @ResponseBody List<Map<String,Object>> listarecal(HttpServletRequest request)throws IOException{
		System.out.println("contratos lista por usuario");
		Map<String, Object> Data = new HashMap<String, Object>();
		List<Map<String,Object>> lista=new ArrayList<Map<String,Object>>();
		
//		HttpSession sesion=request.getSession(false);
//		System.out.println(sesion);
//		Persona xuser=(Persona) sesion.getAttribute("xusuario");
		String usuario=request.getParameter("user");
		String contraseña=request.getParameter("password");
		
		Persona xuser=this.manejadorUsuarios.iniciarSession(usuario,contraseña);
		
		if (xuser!=null) {

			System.out.println("Xuser:"+xuser.getUsuario().getLogin());
		}
		System.out.println("User:"+usuario);
		if (xuser!=null && usuario.equals(xuser.getUsuario().getLogin())) {
			System.out.println("Ingresando..");
			try {
				lista=this.manejadorServicios.listarContratosPorUsuarioRecal(usuario);
				Data.put("data", lista);
			} catch (Exception e) {
				Data.put("data", lista);
				lista=null;
				System.out.println(e.getMessage());
				e.printStackTrace();
			}
		}else {
			lista=null;
			System.out.println("lista null");
		}


		
		return lista;

	}
	
}
