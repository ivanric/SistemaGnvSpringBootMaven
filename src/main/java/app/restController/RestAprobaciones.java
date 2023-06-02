package app.restController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import app.manager.ManejadorAprobaciones;
import app.manager.ManejadorServicios;
import app.manager.ManejadorSolicitudes;
import app.models.Aprobacion;
import app.models.Aseguradora;
import app.models.Persona;
import app.models.Poliza;
import app.models.Solicitud;
import app.models.Taller;
import app.models.Vehiculo;
import app.models.Accion;
import app.utilidades.Constantes;
import app.utilidades.GeneradorReportes;
import app.utilidades.URIS;

@RequestMapping("/RestAprobaciones/")
@RestController 
public class RestAprobaciones { 
	@Autowired
	ManejadorServicios manejadorServicios;
	@Autowired
	ManejadorSolicitudes manejadorSolicitudes;
	@Autowired
	ManejadorAprobaciones manejadorAprobaciones;
	
	@RequestMapping(value="listar")
	public ResponseEntity<List<Solicitud>> listarAprobaciones(HttpServletRequest req,HttpServletResponse res){
//		String filtro=req.getParameter("filtro");
//		System.out.println("filtro: "+filtro);
		List<Solicitud> solicitudes=new ArrayList<Solicitud>();
		solicitudes=this.manejadorSolicitudes.ListarSolAp(req);
//		System.out.println("listaSoltAnt: "+solicitudes.toString());
		for (int i = 0; i < solicitudes.size(); i++) {
			List<Aprobacion>listaAprob=this.manejadorSolicitudes.cargarAprobaciones(solicitudes.get(i));
			solicitudes.get(i).setAprobaciones(listaAprob);
		}
		
//		System.out.println("listaSoltActual: "+solicitudes.toString());
		
		return new ResponseEntity<List<Solicitud>>(solicitudes,HttpStatus.OK);
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping("listar_d")
	public @ResponseBody Map<?, ?> lista(HttpServletRequest request, Integer draw,int length, Integer start)throws IOException{
		System.out.println("aprobaciones datatables");
		int gestion =Integer.parseInt(request.getParameter("gestion"));
		if (gestion!=0) {
			System.out.println("gestion:"+gestion);
		}
		String total;
		Map<String, Object> Data = new HashMap<String, Object>();
		String search = request.getParameter("search[value]");
//		List<Solicitud> lista=this.manejadorSolicitudes.listar_d_aprobaciones(start, search,length,gestion);
		System.out.println("start:"+start);
		System.out.println("search:"+search);
		System.out.println("length:"+length);
		System.out.println("gestion:"+gestion);
		List<Solicitud> lista=this.manejadorSolicitudes.listar_d1_aprobaciones(start, search,length,gestion);
		//System.out.println("lista:"+lista.toString());
		try {
//			total=String.valueOf(lista.get(0).getTot());
			
			total=String.valueOf((new Constantes().NUM_MAX_DATATABLE));		
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
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
	
	
	@RequestMapping(value="listard1")
	public ResponseEntity<List<Solicitud>> listarAprobacionesd1(HttpServletRequest req,HttpServletResponse res){
		
//		String filtro=req.getParameter("filtro");
//		System.out.println("filtro: "+filtro);
		List<Solicitud> solicitudes=new ArrayList<Solicitud>();
		solicitudes=this.manejadorSolicitudes.ListarSolApd1(req);
//		System.out.println("listaSoltAnt: "+solicitudes.toString());
		for (int i = 0; i < solicitudes.size(); i++) {
			List<Aprobacion>listaAprob=this.manejadorSolicitudes.cargarAprobaciones(solicitudes.get(i));
			solicitudes.get(i).setAprobaciones(listaAprob);
		}
		
//		System.out.println("listaSoltActual: "+solicitudes.toString());
		
		return new ResponseEntity<List<Solicitud>>(solicitudes,HttpStatus.OK);
	}
	
	@RequestMapping({"getDatosSolicitud"})
	public ResponseEntity<List<Object>> getDatosSol(HttpServletRequest req){
		String idsolt=req.getParameter("idsolt");
		System.out.println("idsolt: "+idsolt);
//		List<List<?>> Lista=new ArrayList<List<?>>();
		List<Object> lista=new ArrayList<Object>();
		
		Solicitud solicitud=new Solicitud();
		solicitud=this.manejadorSolicitudes.ListarSolById(req);
		List<Aprobacion>listaAprob=this.manejadorSolicitudes.cargarAprobaciones(solicitud);
		solicitud.setAprobaciones(listaAprob);
		System.out.println("sol: "+solicitud);

		List<Accion> listaTipAprob=this.manejadorSolicitudes.ListaTipoAprob();
		List<?> ListTelf=this.manejadorSolicitudes.ListaTelefonos(solicitud.getPersona().getIdper());
		System.out.println("ListaTelefonos: "+ListTelf.toString());
		List<?>	ListPausaAprob=this.manejadorSolicitudes.ListaPausaAprob(req);
		Map<String, Object> Mapa=new HashMap<String, Object>();
		Mapa.put("UsuarioCreador", this.manejadorSolicitudes.UsuarioCreadorSolt(solicitud.getLogin()));
		Mapa.put("ListaAprobaciones",listaTipAprob);
		Mapa.put("ListaTelefonos", ListTelf);
		Mapa.put("ListPausaAprob", ListPausaAprob);
		
		lista.add(solicitud);
		lista.add(Mapa);
		return new ResponseEntity<List<Object>>(lista,HttpStatus.OK);
	}
	@RequestMapping({"getAseguradoras"})
	public ResponseEntity<List<Aseguradora>> DatosVeh(HttpServletRequest req){
//		String placa=req.getParameter("placa");
//		System.out.println("la placa es : "+placa);
		List<Aseguradora> aseg=this.manejadorSolicitudes.getAseguradoras();
		return new ResponseEntity<List<Aseguradora>>(aseg,HttpStatus.OK);
	}
	

	
	@Transactional
	@RequestMapping({"adicionar"})
	public ResponseEntity<Map<String, Object>> add(HttpServletRequest req){
		Map<String, Object> resp=new HashMap<String, Object>();
		String idsolt=req.getParameter("idsolt");
//		System.out.println("idsol: "+idsolt);
		String idtipoPausa=req.getParameter("idtipoPausa");	
		String descripcionPausa=req.getParameter("descripcionPausa");	
		String dataFinal=req.getParameter("dataFinal");
//		System.out.println("dataFinal:"+dataFinal);
		String [] ListaAprobaciones=req.getParameterValues("aprobacion[]");
		Persona xuser=(Persona)req.getSession().getAttribute("xusuario");
//		System.out.println("ListaAprob: "+ListaAprobaciones);
		
		int idTipoFinal=this.manejadorAprobaciones.getTipoFinal();
		System.out.println("Aprob final: "+idTipoFinal);
		int aprobarSolt=0;

//		String numeroPol=req.getParameter("numeroPol");	
//		String idaseg=req.getParameter("idaseg");	
		String placa=req.getParameter("placa");	
		
//		System.out.println("numeroPol:"+numeroPol+" idaseg:"+idaseg+" Placa:"+placa);
		try {

			if(ListaAprobaciones!=null) {
				for (String string : ListaAprobaciones) {
					System.out.println("Aprob: "+string);
				}
				if(Integer.parseInt(dataFinal)==idTipoFinal) {
					aprobarSolt=Integer.parseInt(dataFinal);
				}else{
					aprobarSolt=0;
				}
				int statusApro=this.manejadorAprobaciones.insertarAprobacion(req,xuser.getUsuario().getLogin(), Integer.parseInt(idsolt),ListaAprobaciones,aprobarSolt);
				System.out.println("statusApro: "+statusApro);
				if(!descripcionPausa.equals("") && idtipoPausa!=null) {
					this.manejadorAprobaciones.insertarPausaAprobacion(Integer.parseInt(idsolt), Integer.parseInt(idtipoPausa), descripcionPausa,xuser.getUsuario().getLogin());
				} 
			}else {
				if((!descripcionPausa.equals("")) && (idtipoPausa!=null)) {
					System.out.println("entro sin aprobacions a pause");
					System.out.println("descripcionPausa: "+descripcionPausa);
					int resultPause=this.manejadorAprobaciones.insertarPausaAprobacion(Integer.parseInt(idsolt), Integer.parseInt(idtipoPausa), descripcionPausa,xuser.getUsuario().getLogin());
					System.out.println("resultPause: "+resultPause);
				}
			}
			resp.put("idsolt", Integer.parseInt(idsolt));
			resp.put("status", true);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			resp.put("status", false);
		}

		return new ResponseEntity<Map<String,Object>>(resp,HttpStatus.OK);
	}
	@Autowired
	DataSource dataSource;
	@RequestMapping("Imprimir")
	public  void Imprimir(HttpServletResponse res,HttpServletRequest req){
		URIS uris=new URIS();
		Persona us=(Persona)req.getSession(true).getAttribute("xusuario");
		String Tramitador=us.getRazonsocial().toUpperCase();
		String idsolt=req.getParameter("idsolt");
		System.out.println("idsoltImmprimir: "+idsolt);
		Map<String, Object> nitSQL=this.manejadorServicios.nitEmpresa(1); 
//		System.out.println("nitSQL: "+nitSQL);
		String nit_patam=(String) nitSQL.get("nitInst"); 
		String direccion_logo=uris.imgJasperReport+"LOGOTIPOGNVFINAL.png";
		String nombreReporte="Aprobacion",tipo="pdf", estado="inline";
		    
		String [] ListaAprobaciones=req.getParameterValues("aprobacion[]");
		String url="";
		Map<String, Object> parametros=new HashMap<String, Object>();
		GeneradorReportes g=new GeneradorReportes();
		int [] arrayID=null; 
//		int [] arrayID= {1,2,3}; 

		try{
			if(ListaAprobaciones!=null){
				url=uris.jasperReport+"getAprobacion.jasper";
				for (String string : ListaAprobaciones) {
					System.out.println("AprobImprimir: "+string);
				}
				System.out.println("ListaTam: "+ListaAprobaciones.length);
				arrayID=new int[ListaAprobaciones.length];
				for (int i = 0; i < ListaAprobaciones.length; i++) {
					arrayID[i]=Integer.parseInt(ListaAprobaciones[i]);
				}
				for (int i : arrayID) {
					System.out.println("array: "+i); 
				}		 
				System.out.println("escudo_param: "+this.getClass().getResourceAsStream(direccion_logo));          
				parametros.put("idsolt_param",Integer.parseInt(idsolt));
				parametros.put("tramitador_param",Tramitador);
				parametros.put("nit_param",nit_patam);
				parametros.put("escudo_param",this.getClass().getResourceAsStream(direccion_logo));
				parametros.put("miArray", arrayID);   
				g.generarReporte(res, getClass().getResource(url), tipo, parametros, dataSource.getConnection(), nombreReporte, estado);	
			}else{
				url=uris.jasperReport+"blanco.jasper";
				g.generarReporte(res, getClass().getResource(url), tipo, parametros, dataSource.getConnection(), nombreReporte, estado);	
				System.out.println("Sin aprobaciones" ); 
			}   
			              
		} catch (Exception e) {        
			e.printStackTrace(); 
		}		  
	}
	
}  
