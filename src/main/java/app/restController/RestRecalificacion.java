package app.restController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import app.manager.ManejadorServicios;
import app.manager.ManejadorSolicitudes;
import app.models.Combustible;
import app.models.OrdenServicio;
import app.models.Persona;
import app.models.Solicitud;
import app.models.SolicitudReposicionRecalificacion;
import app.models.Telefono;
import app.models.Vehiculo;
import app.utilidades.GeneradorReportes;
import app.utilidades.URIS;


@RequestMapping("/RestRecalificaciones/")
@RestController 
public class RestRecalificacion {
	@Autowired      
	ManejadorSolicitudes manejadorSolicitudes;
	
	@Autowired
	ManejadorServicios manejadorServicios;
	
	@SuppressWarnings("unchecked") 
	@RequestMapping("listar_d1")
	public @ResponseBody Map<?, ?> lista1(HttpServletRequest request, Integer draw,int length, Integer start, int estado)throws IOException{
		int gestion =Integer.parseInt(request.getParameter("gestion"));
		if (gestion!=0) {
			System.out.println("gestion:"+gestion);
		}
		
		int iestado =Integer.parseInt(request.getParameter("estado"));
		System.out.println("gestion:"+iestado);
		String total;
		Map<String, Object> Data = new HashMap<String, Object>();
		String search = request.getParameter("search[value]");
		List<SolicitudReposicionRecalificacion> lista=this.manejadorSolicitudes.listar_d_recalificacion(start, estado, search,length,gestion);
		System.out.println("listaRecalificaciones:"+lista.toString());
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
	
	
	@Transactional
	@RequestMapping(value="adicionar_d1")
	public Map<String, Object> adicionar_d1(HttpServletRequest req,HttpServletResponse res,Solicitud s,Vehiculo v,@RequestParam int [] combustible,Persona p,@RequestParam String ci){
		System.out.println("PersonaSolicitud:"+p.toString());
		System.out.println("SolicitudSolicitud: "+s.toString());
		System.out.println("VehiculoSolicitud: "+v.toString());
		
		String idben=req.getParameter("idben");
		String idper=req.getParameter("idper");
		System.out.println("idben: "+idben);
		System.out.println("idbenB: "+idben.equals(""));
		System.out.println("idper: "+idper);
		System.out.println("idperB: "+idper.equals(""));
		
		HttpSession sesion=req.getSession(true);
		Persona xuser=(Persona) sesion.getAttribute("xusuario");
		int idacc=this.manejadorSolicitudes.getAccion();
		System.out.println("idacc: "+idacc);
		

		Map<String, Object> respuesta=new HashMap<String, Object>();
		if(combustible!=null) {
			System.out.println("tamanio: "+combustible.length);
			for (int i : combustible) {
				System.out.println("cod_combustible: "+i);
			}	
		}
		
		/****************************DATOS BENEFICIARIO O USUARIO, O NUEVO BENEFICIARIO**************************/
		System.out.println("Pers: "+p);
//		System.out.println("ci: "+ci);
		String[] documentos=req.getParameterValues("documentos[]");
		String[] telefonos=req.getParameterValues("telefono[]");

//		Map<String, Object> respuesta=new HashMap<String, Object>();
		if (documentos!=null) {
//			System.out.println("documentosArray: "+documentos.toString());
			System.out.println("tamanioDocArray: "+documentos.length);
			for (String i : documentos) {
				System.out.println("coddocb: "+i);
			}
		}
		if(telefonos!=null) {
			System.out.println("TelefonosArray: "+telefonos.length);
			for (String i : telefonos) {
				System.out.println("telefonos: "+i);
//				System.out.println("vacio? : "+i.equals(null));
				System.out.println("vacio? : "+!i.equals(""));
				System.out.println("vacio? : "+i.equals(""));
			}
		}

		/****************************FIN DE DATOS BENEFICIARIO O USUARIO, O NUEVO BENEFICIARIO**************************/
		

		try {
			
			Object[] RespSolicitud=this.manejadorSolicitudes.registrar_recalificacion_d(req,xuser,v,s,combustible,idacc,p, documentos,telefonos);
			respuesta.put("estado", RespSolicitud[0]);
			respuesta.put("idrecal", Integer.parseInt(RespSolicitud[1].toString()));
			
			System.out.println("IDSOLT_PARA REPORTE:"+Integer.parseInt(RespSolicitud[1].toString()));
			
//			respuesta.put("estado", true);
//			respuesta.put("idsolt", 1);
			
		} catch (Exception e) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			respuesta.put("estado",false);
		}

		return respuesta;
	}
	
	
	@RequestMapping ({"existePlacaRecalificacion"})
	public ResponseEntity<Map<String, Object>> existe(HttpServletRequest req){
		Map<String, Object> mapa=new HashMap<String, Object>();
		String placa=req.getParameter("placa");
		int existe=0;
		System.out.println("tam_"+placa.length());
		//VERIFICA PRIMERO SI EXISTE LA PLACA QUE ESTE DISPONIBLE 
		if(this.manejadorSolicitudes.verificarPlacaRecalificacion(placa)){
			existe=1;
		}
		System.out.println("existe: "+existe);
		mapa.put("estado", existe);
		return new ResponseEntity<Map<String,Object>>(mapa,HttpStatus.OK);
	}
	@RequestMapping({"PlacaDatosRecalificacion"})
	public ResponseEntity<Vehiculo> DatosVeh(HttpServletRequest req){
		String placa=req.getParameter("placa");
		System.out.println("la placa es : "+placa);
		Vehiculo veh=this.manejadorSolicitudes.DatosVehiculo(placa);
		return new ResponseEntity<Vehiculo>(veh,HttpStatus.OK);
	}
	

	
	@Autowired //METODO SIN USO
	DataSource dataSource;
	@RequestMapping(value="Imprimir_solicitud_recalificacion_reposicion/{id}",method=RequestMethod.GET)
	public  void Imprimir_d(HttpServletResponse res,HttpServletRequest req,@PathVariable("id") Integer id){
		URIS uris=new URIS();
		Persona us=(Persona)req.getSession(true).getAttribute("xusuario");
		String ListaTelefonos="",Tramitador=us.getAp().toUpperCase()+" "+us.getAm().toUpperCase()+" "+us.getNombres().toUpperCase();
		int idsolt=id;
		System.out.println("idsoltIMPRIMIR: "+idsolt);
		String nombreReporte="Solicitud de Recalificacion de Cilindro y Reposicion de Kit",tipo="pdf", estado="inline";
		
		Map<String, Object> nitSQL=this.manejadorServicios.nitEmpresa(1); 
		String nit_patam=(String) nitSQL.get("nitInst");
//		System.out.println("nit_param: "+nit_patam);
		
		List<Telefono> ListaTelf=this.manejadorSolicitudes.ListaTelfRecal(idsolt);
//		System.out.println("ListaTelefonos: "+ListaTelf.toString());
		
		for (int i = 0; i < ListaTelf.size(); i++) {
			System.out.println("ListaTelS: "+ListaTelf.get(i));
			ListaTelefonos+=ListaTelf.get(i).getNumero()+" ";
		}
		ListaTelefonos=ListaTelefonos.trim().replaceAll(" ","-");
	
		String direccionLogo=uris.imgJasperReport+"LOGOTIPOGNVFINAL.png";
		String direccionEscudoTarijaFondo=uris.imgJasperReport+"escudo-tarija-agua.png";
		String direccionFondoReporte=uris.imgJasperReport+"fondoreporte.jpg";
		
		System.out.println("Dire: "+direccionLogo);             
		
		System.out.println("escudo: "+this.getClass().getResourceAsStream(direccionLogo));
		String subReportInst=uris.jasperReport+"getEmpresa.jasper";
		
		System.out.println("subReport: "+subReportInst);
		Map<String, Object> parametros=new HashMap<String, Object>();
		                      	
		String url=uris.jasperReport+"solicitud_recalificacion_d.jasper";  	
	                                
		parametros.put("nit_param",nit_patam);
		parametros.put("idsolt_param",idsolt);
		parametros.put("telefonos_param",ListaTelefonos);
		parametros.put("tramitador_param",Tramitador);

		parametros.put("logo_param",this.getClass().getResourceAsStream(direccionLogo));
		parametros.put("fondo_param",this.getClass().getResourceAsStream(direccionFondoReporte));
		parametros.put("gobfondo_param",this.getClass().getResourceAsStream(direccionEscudoTarijaFondo));
		parametros.put("subreport_inst_param",this.getClass().getResourceAsStream(subReportInst));

		GeneradorReportes g=new GeneradorReportes();
		try{
			
			g.generarReporte(res, getClass().getResource(url), tipo, parametros, dataSource.getConnection(), nombreReporte, estado);	

		    
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
	
	
}
