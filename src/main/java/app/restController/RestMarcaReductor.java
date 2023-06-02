package app.restController;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import app.manager.ManejadorMarcaReductor;
import app.models.MarcaReductor;
import app.models.Persona;


@RequestMapping("/RestMarcaReductor/")
@RestController
public class RestMarcaReductor {
	@Autowired
	ManejadorMarcaReductor MarcaReductorS;
	
	
	@SuppressWarnings("unchecked")
	@RequestMapping("lista")
	public Map<?, ?> lista(HttpServletRequest request, Integer draw,int length, Integer start, int estado)throws IOException, SQLException{
		System.out.println("ENTROO");
		System.out.println("estado:"+estado);
  
		String total;
		Map<String, Object> Data = new HashMap<String, Object>();
		
		String search = request.getParameter("search[value]");
		System.out.println(start+" "+estado+" "+search+" "+length);
		List<?> lista=MarcaReductorS.listar(start, estado, search,length);
		System.out.println("Lista: "+lista.toString());
		try {
			total=((Map<String, Object>) lista.get(0)).get("Tot").toString();			
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
	@RequestMapping("guardar")
	public Map<String, Object> guardar(HttpServletRequest req,MarcaReductor obj){
		HttpSession sesion=req.getSession(true);
		Persona xuser=(Persona) sesion.getAttribute("xusuario");
		Map<String, Object> respuesta=new HashMap<String, Object>();
		try{
			boolean consulta=this.MarcaReductorS.registrar(obj);
			System.out.println("resp: "+consulta);
			respuesta.put("status", consulta);
		}catch (Exception e){
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			respuesta.put("status",false);
		}
		return respuesta;
	}
	@RequestMapping("datos-mod")
	public Map<String, Object> obtener(MarcaReductor obj){
		Map<String, Object> Data = new HashMap<String, Object>();
		try {
			Data.put("data", MarcaReductorS.obtener(obj));
			Data.put("status", true);
		} catch (Exception e) {
			Data.put("status", false);
		}
		return Data;
	}
	@RequestMapping("modificar")
	public  Map<String, Object> actualizar(HttpServletRequest req,MarcaReductor obj)throws IOException{
		HttpSession sesion=req.getSession(true);
		Persona xuser=(Persona) sesion.getAttribute("xusuario");
		Map<String, Object> Data = new HashMap<String, Object>();
		if(xuser!=null){
			try {
				this.MarcaReductorS.modificar(obj);
				Data.put("status", true);
			}catch(Exception e){
				Data.put("status", false);
			}
		}
		return Data;
	}
	@RequestMapping("eliminar")
	public Map<String, Object> eliminar(HttpServletRequest req,MarcaReductor obj)throws IOException{
		HttpSession sesion=req.getSession(true);
		Persona xuser=(Persona) sesion.getAttribute("xusuario");
		Map<String, Object> Data = new HashMap<String, Object>();
		if(xuser!=null){
				try {
					Data.put("status", this.MarcaReductorS.eliminar(obj));
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		return Data;
	}
	@RequestMapping("habilitar")
	public Map<String, Object> habilitar(HttpServletRequest req,MarcaReductor obj)throws IOException{
		HttpSession sesion=req.getSession(true);
		Persona xuser=(Persona) sesion.getAttribute("xusuario");
		Map<String, Object> Data = new HashMap<String, Object>();
		if(xuser!=null){
				try {
					Data.put("status", this.MarcaReductorS.habilitar(obj));
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		return Data;
	}
}
