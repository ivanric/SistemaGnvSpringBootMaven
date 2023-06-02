package app.restController;

import java.io.IOException;
import java.sql.SQLException;
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
import org.springframework.web.bind.annotation.RestController;

import app.manager.ManejadorBeneficiarios;
import app.manager.ManejadorServicios;
import app.manager.ManejadorSolicitudes;
import app.models.TipoMarcaVehiculo;
import app.models.Vehiculo;


@RequestMapping("/RestVehiculos/")
@RestController
public class RestVehiculos {
	@Autowired      
	ManejadorSolicitudes manejadorSolicitudes;
	@Autowired
	ManejadorBeneficiarios manejadorBeneficiarios;
	@Autowired
	ManejadorServicios manejadorServicios;
	
	public int [] GCombustible=null;
	
	@SuppressWarnings("unchecked")
	@RequestMapping("lista")
	public Map<?, ?> lista(HttpServletRequest request, Integer draw,int length, Integer start, int estado)throws IOException, SQLException{
//		System.out.println("ENTROO");
//		System.out.println("estado:"+estado);
  
		String total; 
		Map<String, Object> Data = new HashMap<String, Object>();
		
		String search = request.getParameter("search[value]");
		System.out.println(start+" "+estado+" "+search+" "+length);
		System.out.println("start:"+start+" estado:"+ estado+" search:" +search+" length:"+length);
		List<Vehiculo> lista=manejadorSolicitudes.listar_veh(start, estado, search,length);
		System.out.println("Lista: "+lista.toString());
		try {
			total=String.valueOf(lista.get(0).getTot());	
			
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
			total="0";
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
		System.out.println("total:"+total);
		Data.put("draw", draw); 
		Data.put("recordsTotal", total);
		Data.put("data", lista);
		if(!search.equals(""))
			Data.put("recordsFiltered", lista.size());
		else
			Data.put("recordsFiltered", total);
		return Data;

	}
	@RequestMapping({"getDatosVeh"})
	public ResponseEntity<List<?>> getDatosVeh(HttpServletRequest req,HttpServletResponse res){
		List<Object> lista=new ArrayList<>();
		lista.add(this.manejadorSolicitudes.tipoVehiculo());
		lista.add(this.manejadorSolicitudes.marcaVehiculo());
		lista.add(this.manejadorSolicitudes.modeloVehiculo());
		lista.add(this.manejadorSolicitudes.tipoMotorVehiculo());
		lista.add(this.manejadorSolicitudes.tipoServicioVehiculo());
		lista.add(this.manejadorSolicitudes.tipoCombustible());
//		lista.add(this.manejadorSolicitudes.numeroSolicitud());
		System.out.println("lista_listas: "+lista);
		return new ResponseEntity<List<?>>(lista,HttpStatus.OK);

	}
	@RequestMapping({"getDatosVehiculoByPlaca"})
	public ResponseEntity<Vehiculo> GetDatosVehiculo(HttpServletRequest req){
		String placa=req.getParameter("placa");
		System.out.println("la placa es : "+placa);
		Vehiculo veh=this.manejadorSolicitudes.getDatosVehiculoByPlaca(placa);
		return new ResponseEntity<Vehiculo>(veh,HttpStatus.OK);
	}
	@RequestMapping(value="getTipoMarcaVehByMarca")
	public ResponseEntity<List<Object>> getTipoMarcaVehByMarcaByMarca(HttpServletRequest req){
		List<Object> data=new ArrayList<Object>();
		
		List<TipoMarcaVehiculo> marca=this.manejadorSolicitudes.getTipoMarcaVehByMarca(req);
		data.add(marca);
		System.out.println("data:"+data.toString());
		return new ResponseEntity<List<Object>>(data,HttpStatus.OK);		
	}
	@Transactional
	@RequestMapping(value="modificarVeh")
	public Map<String, Object> modificarVeh(HttpServletRequest req,HttpServletResponse res,Vehiculo v,int [] combustible){
		Map<String, Object> respuesta=new HashMap<String, Object>();
		
		GCombustible=combustible;
		try {
			boolean consulta=this.manejadorSolicitudes.modificarVeh(req,v,combustible);	
			System.out.println(consulta);
			respuesta.put("estado", consulta);
		} catch (Exception e) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			respuesta.put("estado",false);
		}
		return respuesta;
	}
//	@RequestMapping("guardar")
//	public Map<String, Object> guardar(HttpServletRequest req,MarcaReductor obj){
//		HttpSession sesion=req.getSession(true);
//		Persona xuser=(Persona) sesion.getAttribute("xusuario");
//		Map<String, Object> respuesta=new HashMap<String, Object>();
//		try{
//			boolean consulta=this.MarcaReductorS.registrar(obj);
//			System.out.println("resp: "+consulta);
//			respuesta.put("status", consulta);
//		}catch (Exception e){
//			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
//			respuesta.put("status",false);
//		}
//		return respuesta;
//	}
//	@RequestMapping("datos-mod")
//	public Map<String, Object> obtener(MarcaReductor obj){
//		Map<String, Object> Data = new HashMap<String, Object>();
//		try {
//			Data.put("data", MarcaReductorS.obtener(obj));
//			Data.put("status", true);
//		} catch (Exception e) {
//			Data.put("status", false);
//		}
//		return Data;
//	}
//	@RequestMapping("modificar")
//	public  Map<String, Object> actualizar(HttpServletRequest req,MarcaReductor obj)throws IOException{
//		HttpSession sesion=req.getSession(true);
//		Persona xuser=(Persona) sesion.getAttribute("xusuario");
//		Map<String, Object> Data = new HashMap<String, Object>();
//		if(xuser!=null){
//			try {
//				this.MarcaReductorS.modificar(obj);
//				Data.put("status", true);
//			}catch(Exception e){
//				Data.put("status", false);
//			}
//		}
//		return Data;
//	}
//	@RequestMapping("eliminar")
//	public Map<String, Object> eliminar(HttpServletRequest req,MarcaReductor obj)throws IOException{
//		HttpSession sesion=req.getSession(true);
//		Persona xuser=(Persona) sesion.getAttribute("xusuario");
//		Map<String, Object> Data = new HashMap<String, Object>();
//		if(xuser!=null){
//				try {
//					Data.put("status", this.MarcaReductorS.eliminar(obj));
//				} catch (SQLException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//		}
//		return Data;
//	}
//	@RequestMapping("habilitar")
//	public Map<String, Object> habilitar(HttpServletRequest req,MarcaReductor obj)throws IOException{
//		HttpSession sesion=req.getSession(true);
//		Persona xuser=(Persona) sesion.getAttribute("xusuario");
//		Map<String, Object> Data = new HashMap<String, Object>();
//		if(xuser!=null){
//				try {
//					Data.put("status", this.MarcaReductorS.habilitar(obj));
//				} catch (SQLException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//		}
//		return Data;
//	}
}
