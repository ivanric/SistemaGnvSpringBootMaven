package app.restController;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import app.manager.ManejadorServicios;
import app.manager.ManejadorSolicitudes;
import app.models.Combustible;
import app.models.OrdenServicio;
import app.models.OrdenServicioRecalificacion;
import app.models.Persona;
import app.models.Servicio;
import app.models.Solicitud;
import app.models.SolicitudReposicionRecalificacion;
import app.models.TallerRecalificacion;
import app.models.Telefono;
import app.utilidades.GeneradorReportes;
import app.utilidades.URIS;

@RequestMapping("/RestOrdenRecalificacion/")
@RestController
public class RestOrdenRecalificacion {
	@Autowired   
	ManejadorSolicitudes manejadorSolicitudes;
	
	@Autowired
	ManejadorServicios manejadorServicios;
	
	
	@SuppressWarnings("unchecked") 
	@RequestMapping("listar_ordenes_servicio_recalificacion_d")
	public @ResponseBody Map<?, ?> lista1(HttpServletRequest request, Integer draw,int length, Integer start, int estado)throws IOException{
		int gestion =Integer.parseInt(request.getParameter("gestion"));
		if (gestion!=0) {
			System.out.println("gestion:"+gestion);
		}
		
//		int numsolt_s=Integer.parseInt(request.getParameter("numsolt"));
		System.out.println(request.getParameter("numsolt")=="");
		System.out.println(request.getParameter("numsolt").isEmpty());
		System.out.println(request.getParameter("numsolt")==null);
		
		int numsolt=-1;
		if(!request.getParameter("numsolt").isEmpty()) {
			numsolt=Integer.parseInt(request.getParameter("numsolt"));
		}
		
		
		int numord=-1;
		if(!request.getParameter("numord").isEmpty()) {
			numord=Integer.parseInt(request.getParameter("numord"));
		}
		
		System.out.println("numsolt"+numsolt);
		System.out.println("numord"+numord);
		
		
		String total;
		Map<String, Object> Data = new HashMap<String, Object>();
		String search = request.getParameter("search[value]");
		List<OrdenServicioRecalificacion> OrdenesServicios=this.manejadorServicios.listar_ordenes_servicio_recalificacion_d(start, estado, search,length,gestion,numord,numsolt);
		System.out.println("ListaOrdenesServicios:"+OrdenesServicios.toString());
		try {
			total=String.valueOf(OrdenesServicios.get(0).getTot());			
		} catch (Exception e) {
			total="0";
		}
		Data.put("draw", draw);
		Data.put("recordsTotal", total);
		Data.put("data", OrdenesServicios);
		if(!search.equals(""))
			Data.put("recordsFiltered", OrdenesServicios.size());
		else
			Data.put("recordsFiltered", total);
		return Data;

	}
	
	@Transactional
	@RequestMapping(value="adicionarOS_d")
	public Map<String, Object> adicionarOS_d(HttpServletRequest req,HttpServletResponse res){
		HttpSession sesion=req.getSession(true);
		Persona xuser=(Persona) sesion.getAttribute("xusuario");
		Map<String, Object> respuesta=new HashMap<String, Object>();
//		int numOrdServ=Integer.parseInt(req.getParameter("numOrdServ"));
		int idSolt=Integer.parseInt(req.getParameter("idsolt"));
		int idServ=Integer.parseInt(req.getParameter("idserv"));
		int idtall=Integer.parseInt(req.getParameter("idtall"));
		System.out.println(" idSolt: "+idSolt+" idServ: "+idServ+" idTall:"+idtall);
		try{
			Object[] consulta=this.manejadorServicios.registrarOS_d(req,xuser);
			System.out.println("resp: "+consulta);
			respuesta.put("estado", consulta[0]);
			respuesta.put("ordServ",Integer.parseInt(consulta[1].toString()));
			respuesta.put("numordserv",Integer.parseInt(consulta[2].toString()));

		}catch (Exception e){
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			respuesta.put("estado",false);
		}
		return respuesta;
	}
	/*ORDEN DE SERVICIO*/
	@RequestMapping(value="datosOrdenServicioRecalificacion")
	public ResponseEntity<List<List<?>>> dataOrdenServicio(HttpServletRequest req,HttpServletResponse res){
		int gestion=Integer.parseInt(req.getParameter("gestion"));
		List<List<?>> lista=new ArrayList<List<?>>();
		List<Map<String, Object>> Solicitud=new ArrayList<>();
		Map<String, Object> mapa=new HashMap<>();
		Date date = new Date();
		String fecha= new SimpleDateFormat("yyyy-MM-dd").format(date);
		mapa.put("numOrdServ", this.manejadorServicios.numeroOrdenServicioRecalificacionByGestion(gestion));
		mapa.put("fecha",fecha);
		Solicitud.add(0, mapa);
		List<?> ListaTalleresConversion=this.manejadorServicios.ListTalleresConversion(gestion);
		List<?> ListaTalleresRecalificacion=this.manejadorServicios.ListTalleresRecalificacion(gestion);

		List<Combustible> listaComb=this.manejadorServicios.ListComb();
		lista.add(Solicitud);
		lista.add(ListaTalleresConversion); 
		lista.add(ListaTalleresRecalificacion); 
		lista.add(listaComb);
		System.out.println("ListaOrdenServicios: "+lista.toString());
		return new ResponseEntity<List<List<?>>>(lista,HttpStatus.OK);
	}
	
	@RequestMapping(value="datosOrdenServicioRecalificacionCilindro")
	public ResponseEntity<List<List<?>>> datosOrdenServicioRecalificacionCilindro(HttpServletRequest req,HttpServletResponse res){
		int gestion=Integer.parseInt(req.getParameter("gestion"));
		List<List<?>> lista=new ArrayList<List<?>>();
		List<Map<String, Object>> Solicitud=new ArrayList<>();
		Map<String, Object> mapa=new HashMap<>();
		Date date = new Date();
		String fecha= new SimpleDateFormat("yyyy-MM-dd").format(date);
		mapa.put("numOrdServ", this.manejadorServicios.numeroOrdenServicioRecalificacionByGestion(gestion));
		mapa.put("fecha",fecha);
		Solicitud.add(0, mapa);
		List<?> ListaTalleresRecalificacion=this.manejadorServicios.ListTalleresRecalificacion(gestion);

		lista.add(Solicitud);
		lista.add(ListaTalleresRecalificacion); 
		
		System.out.println("ListaOrdenServiciosDatos: "+lista.toString());
		return new ResponseEntity<List<List<?>>>(lista,HttpStatus.OK);
	}
	
	
	@RequestMapping(value="datosOrdenServicioReposicionCilindro")
	public ResponseEntity<List<List<?>>> datosOrdenServicioReposicionCilindro(HttpServletRequest req,HttpServletResponse res){
		int gestion=Integer.parseInt(req.getParameter("gestion"));
		List<List<?>> lista=new ArrayList<List<?>>();
		List<Map<String, Object>> Solicitud=new ArrayList<>();
		Map<String, Object> mapa=new HashMap<>();
		Date date = new Date();
		String fecha= new SimpleDateFormat("yyyy-MM-dd").format(date);
		mapa.put("numOrdServ", this.manejadorServicios.numeroOrdenServicioRecalificacionByGestion(gestion));
		mapa.put("fecha",fecha);
		Solicitud.add(0, mapa);
//		List<?> ListaTalleresRecalificacion=this.manejadorServicios.ListTalleresRecalificacion(gestion);

		lista.add(Solicitud);
//		lista.add(ListaTalleresRecalificacion); 
		
		System.out.println("ListaOrdenServiciosDatos: "+lista.toString());
		return new ResponseEntity<List<List<?>>>(lista,HttpStatus.OK);
	}
	
	@RequestMapping(value="FiltroSolicitudOSRecalificacion")
	public ResponseEntity<List<SolicitudReposicionRecalificacion>> FiltroSolicitud(HttpServletRequest req,HttpServletResponse res){
		String texto=req.getParameter("texto");
		int gestion=Integer.parseInt(req.getParameter("gestion"));
		System.out.println("texto: "+texto);
		 
		List<SolicitudReposicionRecalificacion> solt=null;
		try {
			solt=this.manejadorServicios.FiltroSolicitudOSRecalificacion(req.getParameter("texto"),gestion);
			System.out.println("TAM: "+solt.size());
			System.out.println("KU: "+solt);
		} catch (Exception e) {
			solt=null;
		}
		return new ResponseEntity<List<SolicitudReposicionRecalificacion>>(solt,HttpStatus.OK);
	}
	
	@Transactional
	@RequestMapping(value="adicionarOSRecalificacion_d")
	public Map<String, Object> adicionarOSRecalificacion_d(HttpServletRequest req,HttpServletResponse res){
		HttpSession sesion=req.getSession(true);
		Persona xuser=(Persona) sesion.getAttribute("xusuario");
		Map<String, Object> respuesta=new HashMap<String, Object>();
//		int numOrdServ=Integer.parseInt(req.getParameter("numOrdServ"));
		int idrecal=Integer.parseInt(req.getParameter("idrecal"));
		int idtall=Integer.parseInt(req.getParameter("idtall"));
		System.out.println(" idrecal: "+idrecal+" idTall:"+idtall);
		try{
			Object[] consulta=this.manejadorServicios.registrarOSRecalificacion_d(req,xuser);
			System.out.println("resp: "+consulta);
			respuesta.put("estado", consulta[0]);
			respuesta.put("ordServ",Integer.parseInt(consulta[1].toString()));
			respuesta.put("numordserv",Integer.parseInt(consulta[2].toString()));

		}catch (Exception e){
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			respuesta.put("estado",false);
		}
		return respuesta;
	}
	
	
	@Transactional
	@RequestMapping(value="adicionarOSRecalificacion_Cilindro_d")
	public Map<String, Object> adicionarOSRecalificacion_Cilindro_d(HttpServletRequest req,HttpServletResponse res){
		HttpSession sesion=req.getSession(true);
		Persona xuser=(Persona) sesion.getAttribute("xusuario");
		Map<String, Object> respuesta=new HashMap<String, Object>();
		try{
			Object[] consulta=this.manejadorServicios.registrarOSRecalificacionCilindro_d(req,xuser);
			System.out.println("resp: "+consulta);
			respuesta.put("estado", consulta[0]);
			respuesta.put("idordservrecal",Integer.parseInt(consulta[1].toString()));

		}catch (Exception e){
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			respuesta.put("estado",false);
		}
		return respuesta;
	}
	
	@Transactional
	@RequestMapping(value="adicionarOSReposicion_Cilindro_d")
	public Map<String, Object> adicionarOSReposicion_Cilindro_d(HttpServletRequest req,HttpServletResponse res){
		HttpSession sesion=req.getSession(true);
		Persona xuser=(Persona) sesion.getAttribute("xusuario");
		Map<String, Object> respuesta=new HashMap<String, Object>();
		try{
			Object[] consulta=this.manejadorServicios.registrarOSReposicionCilindro_d(req,xuser);
			System.out.println("resp: "+consulta);
			respuesta.put("estado", consulta[0]);
			respuesta.put("idordservrecal",Integer.parseInt(consulta[1].toString()));

		}catch (Exception e){
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			respuesta.put("estado",false);
		}
		return respuesta;
	}
	
	
	
	@RequestMapping(value="VerOSRecalificacionCilindro")
	public ResponseEntity<List<?>> VerSolicitud(HttpServletRequest req,HttpServletResponse res){
		List<Object> lista=new ArrayList<>();
		String ListaTelefonosTallerConversion="",ListaTelefonosBeneficiario="";
		String idOrdServ=req.getParameter("idordservrecal");
		System.out.println("idOrdServ: "+idOrdServ);
		OrdenServicioRecalificacion OrdenServicio=this.manejadorServicios.verOrdenServicioRecalificacionReposicion(Integer.parseInt(idOrdServ));

		
		
		System.out.println("OrdenServicio:"+OrdenServicio.getTallerconversion()); 
		List<Telefono> ListaTelfTallerConvert=this.manejadorServicios.ListaTelf(OrdenServicio.getTallerconversion().getPersona().getIdper());
		System.out.println("ListaTelefonosTaller: "+ListaTelfTallerConvert.toString());	
		for (int i = 0; i < ListaTelfTallerConvert.size(); i++) {
			System.out.println("ListaTelS: "+ListaTelfTallerConvert.get(i));
			ListaTelefonosTallerConversion+=ListaTelfTallerConvert.get(i).getNumero()+" ";
		}
		SolicitudReposicionRecalificacion solt=this.manejadorSolicitudes.getSoltRecalRepoByOrdServ(OrdenServicio.getIdordservrecal());
		OrdenServicio.setSolicitudreposicionrecalificacion(solt);
		List<Telefono> ListaTelfBeneficiario=this.manejadorServicios.ListaTelf(OrdenServicio.getSolicitudreposicionrecalificacion().getPersona().getIdper());
		for (int i = 0; i < ListaTelfBeneficiario.size(); i++) {
			System.out.println("ListaTelS: "+ListaTelfBeneficiario.get(i));
			ListaTelefonosBeneficiario+=ListaTelfBeneficiario.get(i).getNumero()+" ";
		}
		ListaTelefonosTallerConversion=ListaTelefonosTallerConversion.trim().replaceAll(" ","-");
		ListaTelefonosBeneficiario=ListaTelefonosTallerConversion.trim().replaceAll(" ","-");
		System.out.println("ListaTelefonosTaller: "+ListaTelefonosTallerConversion);
		System.out.println("ListaTelefonosBeneficiario: "+ListaTelefonosTallerConversion);
		Map<String, Object> mapa1=new HashMap<>();
		mapa1.put("listaTelefonosTallerConversion", ListaTelefonosTallerConversion);
		mapa1.put("listaTelefonosBeneficiario", ListaTelefonosBeneficiario);
		lista.add(OrdenServicio);
		lista.add(mapa1);
		return new ResponseEntity<List<?>>(lista,HttpStatus.OK);
	}
	
	@RequestMapping(value="VerOSReposicionCilindro")
	public ResponseEntity<List<?>> VerOSReposicionCilindro(HttpServletRequest req,HttpServletResponse res){
		List<Object> lista=new ArrayList<>();
		String ListaTelefonosTallerConversion="",ListaTelefonosBeneficiario="";
		String idOrdServ=req.getParameter("idordservrecal");
		System.out.println("idOrdServ: "+idOrdServ);
		OrdenServicioRecalificacion OrdenServicio=this.manejadorServicios.verOrdenServicioRecalificacionReposicion(Integer.parseInt(idOrdServ));
		
		
		if(OrdenServicio.getIdtallrecal()!=0) {
			int idtallrecal=OrdenServicio.getIdtallrecal();
			System.out.println("idtallrecal_reposicion:"+idtallrecal);
			OrdenServicio.setTallerrecalificacion(manejadorServicios.getTallerRecalificacion(idtallrecal));
				
		}else {
			OrdenServicio.setTallerrecalificacion(null);	
		}

		
		List<Telefono> ListaTelfTallerConvert=this.manejadorServicios.ListaTelf(OrdenServicio.getTallerconversion().getPersona().getIdper());
		System.out.println("ListaTelefonosTaller: "+ListaTelfTallerConvert.toString());	
		for (int i = 0; i < ListaTelfTallerConvert.size(); i++) {
			System.out.println("ListaTelS: "+ListaTelfTallerConvert.get(i));
			ListaTelefonosTallerConversion+=ListaTelfTallerConvert.get(i).getNumero()+" ";
		}
		SolicitudReposicionRecalificacion solt=this.manejadorSolicitudes.getSoltRecalRepoByOrdServ(OrdenServicio.getIdordservrecal());
		OrdenServicio.setSolicitudreposicionrecalificacion(solt);
		List<Telefono> ListaTelfBeneficiario=this.manejadorServicios.ListaTelf(OrdenServicio.getSolicitudreposicionrecalificacion().getPersona().getIdper());
		for (int i = 0; i < ListaTelfBeneficiario.size(); i++) {
			System.out.println("ListaTelS: "+ListaTelfBeneficiario.get(i));
			ListaTelefonosBeneficiario+=ListaTelfBeneficiario.get(i).getNumero()+" ";
		}
		ListaTelefonosTallerConversion=ListaTelefonosTallerConversion.trim().replaceAll(" ","-");
		ListaTelefonosBeneficiario=ListaTelefonosTallerConversion.trim().replaceAll(" ","-");
		System.out.println("ListaTelefonosTaller: "+ListaTelefonosTallerConversion);
		System.out.println("ListaTelefonosBeneficiario: "+ListaTelefonosTallerConversion);
		Map<String, Object> mapa1=new HashMap<>();
		mapa1.put("listaTelefonosTallerConversion", ListaTelefonosTallerConversion);
		mapa1.put("listaTelefonosBeneficiario", ListaTelefonosBeneficiario);

		
		lista.add(OrdenServicio);
		lista.add(mapa1);
		return new ResponseEntity<List<?>>(lista,HttpStatus.OK);
	}
	
	
	@RequestMapping ({"getTallerRecalificacion"})
	public ResponseEntity<Map<String, Object>> existe(HttpServletRequest req){
		Map<String, Object> mapa=new HashMap<String, Object>();
		String idtallrecal_s =req.getParameter("idtallrecal");
		TallerRecalificacion taller=new TallerRecalificacion();
		int idtallrecal=0;
		if(idtallrecal_s!=null) {
			idtallrecal=Integer.parseInt(idtallrecal_s);
			taller=manejadorServicios.getTallerRecalificacion(idtallrecal);
		}
		
		
		System.out.println("taller: "+taller);
		mapa.put("tallerrecalificacion", taller);
		return new ResponseEntity<Map<String,Object>>(mapa,HttpStatus.OK);
	}
	
	
	@Autowired
	DataSource dataSource;
	@RequestMapping("ImprimirOSRecalificacionReposicion")
	public  void verOS(HttpServletResponse res,HttpServletRequest req){
		URIS uris=new URIS();
//		Persona us=(Persona)req.getSession(true).getAttribute("xusuario");
//		String Tramitador=us.getAp().toUpperCase()+" "+us.getAm().toUpperCase()+" "+us.getNombres().toUpperCase();
		String idOrdServ=req.getParameter("idordservrecal");
		String ListaTelefonosTaller="",ListaTelefonosBeneficiario="";
		
		List<Telefono> ListaTelfTall=this.manejadorServicios.ListaTelfTallOrdenRecalificacionReposicion(Integer.parseInt(idOrdServ));
//		System.out.println("ListaTelefonosTall: "+ListaTelfTall.toString());
		
		for (int i = 0; i < ListaTelfTall.size(); i++) {
//			System.out.println("ListaTelTall: "+ListaTelfTall.get(i));
			ListaTelefonosTaller+=ListaTelfTall.get(i).getNumero()+" ";
		}
		ListaTelefonosTaller=ListaTelefonosTaller.trim().replaceAll(" ","-");
//		System.out.println("ListaTelefonosTall: "+ListaTelefonosTaller);
		
		
		List<Telefono> ListaTelfBen=this.manejadorServicios.ListaTelfBenRecal(Integer.parseInt(idOrdServ));
//		System.out.println("ListaTelefonosTall: "+ListaTelfBen.toString());
		
		for (int i = 0; i < ListaTelfBen.size(); i++) {
//			System.out.println("ListaTelBen: "+ListaTelfBen.get(i));
			ListaTelefonosBeneficiario+=ListaTelfBen.get(i).getNumero()+" ";
		}
		ListaTelefonosBeneficiario=ListaTelefonosBeneficiario.trim().replaceAll(" ","-");
//		System.out.println("ListaTelefonosBen: "+ListaTelefonosBeneficiario);
		
		
//		System.out.println("idOrdServ: "+idOrdServ);
		
		Map<String, Object> nitSQL=this.manejadorServicios.nitEmpresa(1); 
//		System.out.println("nitSQL: "+nitSQL);
		String nit_patam=(String) nitSQL.get("nitInst");
		System.out.println("nit_param: "+nit_patam);
		String direccion_logo=uris.imgJasperReport+"LOGOTIPOGNVFINAL.png";
		String logoGob=uris.imgJasperReport+"escudo-tarija.png"; 
//		String subReportInst="/app/reportes/getEmpresa.jasper"; 
                             
		String nombreReporte="ORDEN DE SERVICIO",tipo="pdf", estado="inline";
		System.out.println("logo_param: "+this.getClass().getResourceAsStream(direccion_logo));
		    
		Map<String, Object> parametros=new HashMap<String, Object>();
		                       
		String url=uris.jasperReport+"getOrdenRecalificacionReposicion_m.jasper";	
//		String url=uris.jasperReport+"getOrdenServicio_m.jasper";	
	            
		parametros.put("idOrdServ_param",Integer.parseInt(idOrdServ));
		parametros.put("telefonosTall_param",ListaTelefonosTaller);
		parametros.put("telefonosBenf_param",ListaTelefonosBeneficiario);
//		parametros.put("tramitador_param",Tramitador);
		parametros.put("nit_param",nit_patam);
 
		  
		parametros.put("logo_param",this.getClass().getResourceAsStream(direccion_logo));
		parametros.put("logoGob_param",this.getClass().getResourceAsStream(logoGob));
//		parametros.put("subreport_inst_param",this.getClass().getResourceAsStream(subReportInst));

		GeneradorReportes g=new GeneradorReportes();
		try{
			g.generarReporte(res, getClass().getResource(url), tipo, parametros, dataSource.getConnection(), nombreReporte, estado);	
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
	
	
//	
//	@Autowired
//	DataSource dataSource;
	@RequestMapping("ImprimirOSRecalificacionCilindro")
	public  void ImprimirOSRecalificacionCilindro(HttpServletResponse res,HttpServletRequest req){
		URIS uris=new URIS();
//		Persona us=(Persona)req.getSession(true).getAttribute("xusuario");
//		String Tramitador=us.getAp().toUpperCase()+" "+us.getAm().toUpperCase()+" "+us.getNombres().toUpperCase();
		String idOrdServ=req.getParameter("idordservrecal");
		String ListaTelefonosTaller="",ListaTelefonosTallerRecal="",ListaTelefonosBeneficiario="";
		
		List<Telefono> ListaTelfTall=this.manejadorServicios.ListaTelfTallOrdenRecalificacionReposicion(Integer.parseInt(idOrdServ));
		List<Telefono> ListaTelfTallRecal=this.manejadorServicios.ListaTelfTallerRecalificacionOrdenRecalificacionCilindro(Integer.parseInt(idOrdServ));
//		System.out.println("ListaTelefonosTall: "+ListaTelfTall.toString());
		
		for (int i = 0; i < ListaTelfTall.size(); i++) {
//			System.out.println("ListaTelTall: "+ListaTelfTall.get(i));
			ListaTelefonosTaller+=ListaTelfTall.get(i).getNumero()+" ";
		}
		for (int i = 0; i < ListaTelfTallRecal.size(); i++) {
//			System.out.println("ListaTelTall: "+ListaTelfTall.get(i));
			ListaTelefonosTallerRecal+=ListaTelfTallRecal.get(i).getNumero()+" ";
		}
		ListaTelefonosTaller=ListaTelefonosTaller.trim().replaceAll(" ","-");
		ListaTelefonosTallerRecal=ListaTelefonosTallerRecal.trim().replaceAll(" ","-");
//		System.out.println("ListaTelefonosTall: "+ListaTelefonosTaller);
		System.out.println("ListaTelefonosTallerRecal: "+ListaTelefonosTallerRecal);
		
		
		List<Telefono> ListaTelfBen=this.manejadorServicios.ListaTelfBenRecal(Integer.parseInt(idOrdServ));
//		System.out.println("ListaTelefonosTall: "+ListaTelfBen.toString());
		
		for (int i = 0; i < ListaTelfBen.size(); i++) {
//			System.out.println("ListaTelBen: "+ListaTelfBen.get(i));
			ListaTelefonosBeneficiario+=ListaTelfBen.get(i).getNumero()+" ";
		}
		ListaTelefonosBeneficiario=ListaTelefonosBeneficiario.trim().replaceAll(" ","-");
//		System.out.println("ListaTelefonosBen: "+ListaTelefonosBeneficiario);
		
		
//		System.out.println("idOrdServ: "+idOrdServ);
		
		Map<String, Object> nitSQL=this.manejadorServicios.nitEmpresa(1); 
//		System.out.println("nitSQL: "+nitSQL);
		String nit_patam=(String) nitSQL.get("nitInst");
		System.out.println("nit_param: "+nit_patam);
		String direccion_logo=uris.imgJasperReport+"LOGOTIPOGNVFINAL.png";
		String logoGob=uris.imgJasperReport+"escudo-tarija.png"; 
//		String subReportInst="/app/reportes/getEmpresa.jasper"; 
                             
		String nombreReporte="ORDEN DE SERVICIO",tipo="pdf", estado="inline";
		System.out.println("logo_param: "+this.getClass().getResourceAsStream(direccion_logo));
		    
		Map<String, Object> parametros=new HashMap<String, Object>();
		                       
		String url=uris.jasperReport+"getOrdenRecalificacionCilindro.jasper";	
//		String url=uris.jasperReport+"getOrdenServicio_m.jasper";	
	            
		parametros.put("idOrdServ_param",Integer.parseInt(idOrdServ));
		parametros.put("telefonosTall_param",ListaTelefonosTaller);
		parametros.put("telefonosTallRecal_param",ListaTelefonosTallerRecal);
		parametros.put("telefonosBenf_param",ListaTelefonosBeneficiario);
//		parametros.put("tramitador_param",Tramitador);
		parametros.put("nit_param",nit_patam);
 
		  
		parametros.put("logo_param",this.getClass().getResourceAsStream(direccion_logo));
		parametros.put("logoGob_param",this.getClass().getResourceAsStream(logoGob));
//		parametros.put("subreport_inst_param",this.getClass().getResourceAsStream(subReportInst));

		GeneradorReportes g=new GeneradorReportes();
		try{
			g.generarReporte(res, getClass().getResource(url), tipo, parametros, dataSource.getConnection(), nombreReporte, estado);	
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
	
	
	@RequestMapping("ImprimirOSReposicionCilindro")
	public  void ImprimirOSReposicionCilindro(HttpServletResponse res,HttpServletRequest req){
		URIS uris=new URIS();
//		Persona us=(Persona)req.getSession(true).getAttribute("xusuario");
//		String Tramitador=us.getAp().toUpperCase()+" "+us.getAm().toUpperCase()+" "+us.getNombres().toUpperCase();
		String idOrdServ=req.getParameter("idordservrecal");
		String ListaTelefonosTaller="",ListaTelefonosTallerRecal="",ListaTelefonosBeneficiario="";
		
		List<Telefono> ListaTelfTall=this.manejadorServicios.ListaTelfTallOrdenRecalificacionReposicion(Integer.parseInt(idOrdServ));
		List<Telefono> ListaTelfTallRecal=this.manejadorServicios.ListaTelfTallerRecalificacionOrdenRecalificacionCilindro(Integer.parseInt(idOrdServ));
//		System.out.println("ListaTelefonosTall: "+ListaTelfTall.toString());
		
		for (int i = 0; i < ListaTelfTall.size(); i++) {
//			System.out.println("ListaTelTall: "+ListaTelfTall.get(i));
			ListaTelefonosTaller+=ListaTelfTall.get(i).getNumero()+" ";
		}
		for (int i = 0; i < ListaTelfTallRecal.size(); i++) {
//			System.out.println("ListaTelTall: "+ListaTelfTall.get(i));
			ListaTelefonosTallerRecal+=ListaTelfTallRecal.get(i).getNumero()+" ";
		}
		ListaTelefonosTaller=ListaTelefonosTaller.trim().replaceAll(" ","-");
		ListaTelefonosTallerRecal=ListaTelefonosTallerRecal.trim().replaceAll(" ","-");
//		System.out.println("ListaTelefonosTall: "+ListaTelefonosTaller);
		System.out.println("ListaTelefonosTallerRecal: "+ListaTelefonosTallerRecal);
		
		
		List<Telefono> ListaTelfBen=this.manejadorServicios.ListaTelfBenRecal(Integer.parseInt(idOrdServ));
//		System.out.println("ListaTelefonosTall: "+ListaTelfBen.toString());
		
		for (int i = 0; i < ListaTelfBen.size(); i++) {
//			System.out.println("ListaTelBen: "+ListaTelfBen.get(i));
			ListaTelefonosBeneficiario+=ListaTelfBen.get(i).getNumero()+" ";
		}
		ListaTelefonosBeneficiario=ListaTelefonosBeneficiario.trim().replaceAll(" ","-");
//		System.out.println("ListaTelefonosBen: "+ListaTelefonosBeneficiario);
		
		
//		System.out.println("idOrdServ: "+idOrdServ);
		
		Map<String, Object> nitSQL=this.manejadorServicios.nitEmpresa(1); 
//		System.out.println("nitSQL: "+nitSQL);
		String nit_patam=(String) nitSQL.get("nitInst");
		System.out.println("nit_param: "+nit_patam);
		String direccion_logo=uris.imgJasperReport+"LOGOTIPOGNVFINAL.png";
		String logoGob=uris.imgJasperReport+"escudo-tarija.png"; 
//		String subReportInst="/app/reportes/getEmpresa.jasper"; 
                             
		String nombreReporte="ORDEN DE SERVICIO",tipo="pdf", estado="inline";
		System.out.println("logo_param: "+this.getClass().getResourceAsStream(direccion_logo));
		    
		Map<String, Object> parametros=new HashMap<String, Object>();
		                       
		String url=uris.jasperReport+"getOrdenReposicionCilindro.jasper";	
//		String url=uris.jasperReport+"getOrdenServicio_m.jasper";	
	            
		parametros.put("idOrdServ_param",Integer.parseInt(idOrdServ));
		parametros.put("telefonosTall_param",ListaTelefonosTaller);
		parametros.put("telefonosTallRecal_param",ListaTelefonosTallerRecal);
		parametros.put("telefonosBenf_param",ListaTelefonosBeneficiario);
//		parametros.put("tramitador_param",Tramitador);
		parametros.put("nit_param",nit_patam);
 
		  
		parametros.put("logo_param",this.getClass().getResourceAsStream(direccion_logo));
		parametros.put("logoGob_param",this.getClass().getResourceAsStream(logoGob));
//		parametros.put("subreport_inst_param",this.getClass().getResourceAsStream(subReportInst));

		GeneradorReportes g=new GeneradorReportes();
		try{
			g.generarReporte(res, getClass().getResource(url), tipo, parametros, dataSource.getConnection(), nombreReporte, estado);	
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}

}
