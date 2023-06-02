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

import org.hibernate.boot.model.relational.Database;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import app.manager.ManejadorServicios;
import app.manager.ManejadorSolicitudes;
import app.models.Combustible;
import app.models.OrdenServicio;
import app.models.Persona;
import app.models.Servicio;
import app.models.Solicitud;
import app.models.Telefono;
import app.utilidades.GeneradorReportes;
import app.utilidades.URIS;
 
@RequestMapping("/RestServicios/")
@RestController
public class RestServicios { 
	@Autowired
	ManejadorServicios manejadorServicios;
	@Autowired   
	ManejadorSolicitudes manejadorSolicitudes;
	
	
	@RequestMapping(value="planilla_d",method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> listarplanilla_d(HttpServletRequest req,HttpServletResponse res){
		
//		Map<String, Object> lista=new HashMap<String, Object>();
		Map<String, Object> mapa=new HashMap<String, Object>();
		mapa.put("d1",1);
		mapa.put("d2",2);
		mapa.put("d3",3);
		mapa.put("d4",3);
		System.out.println("lista: "+mapa.toString());
		return new ResponseEntity<Map<String, Object>>(mapa,HttpStatus.OK);
	}
	
	@RequestMapping(value="planilla")
	public ResponseEntity<List<?>> listarplanilla(HttpServletRequest req,HttpServletResponse res){
		
		List<?> lista=new ArrayList<>();
		Map<String, Object> mapa=new HashMap<String, Object>();
		mapa.put("d1",1);
		mapa.put("d2",2);
		lista.add(1, null);
		lista.add(2, null);
		lista.add(3, null);
		System.out.println("lista: "+lista.toString());
		return new ResponseEntity<List<?>>(lista,HttpStatus.OK);
	}
	
	
	
	
	@RequestMapping(value="listar")
	public ResponseEntity<List<Servicio>> listarServicio(HttpServletRequest req,HttpServletResponse res){	
		List<Servicio> servicios=this.manejadorServicios.ListarServicios(req);
		System.out.println("listaServicios: "+servicios.toString());
		return new ResponseEntity<List<Servicio>>(servicios,HttpStatus.OK);
	}
	@SuppressWarnings("unchecked")
	@RequestMapping("listar_servicios_d")
	public @ResponseBody Map<?, ?> lista(HttpServletRequest request, Integer draw,int length, Integer start,int estado)throws IOException{
		System.out.println("servicios datatables");
		int gestion =Integer.parseInt(request.getParameter("gestion"));
		if (gestion!=0) {
			System.out.println("gestion:"+gestion);
		}
		String total;
		Map<String, Object> Data = new HashMap<String, Object>();
		String search = request.getParameter("search[value]");
//		List<Solicitud> lista=this.manejadorSolicitudes.listar_d_aprobaciones(start, search,length,gestion);
		List<Servicio> lista=this.manejadorServicios.listar_d1_servicio(start, estado,search,length,gestion);
		//System.out.println("lista:"+lista.toString());
		try {
			total=String.valueOf(lista.get(0).getTot());			
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
	
	@RequestMapping(value="listarOrdenServicio")
	public ResponseEntity<List<OrdenServicio>> listarOrdenServicio(HttpServletRequest req,HttpServletResponse res){	
		List<OrdenServicio> OrdenesServicios=this.manejadorServicios.ListarOrdenServicio(req);
		for (int i = 0; i < OrdenesServicios.size(); i++) {
//			System.out.println("idBen:"+OrdenesServicios.get(i).getSolicitud().getIdben());
			OrdenesServicios.get(i).setSolicitud(manejadorSolicitudes.metSolicitud(OrdenesServicios.get(i).getIdsolt()));
		}

		System.out.println("listaServicios: "+OrdenesServicios.toString());
		return new ResponseEntity<List<OrdenServicio>>(OrdenesServicios,HttpStatus.OK);
	}
	
	@SuppressWarnings("unchecked") 
	@RequestMapping("listar_ordenes_servicio_d")
	public @ResponseBody Map<?, ?> lista1(HttpServletRequest request, Integer draw,int length, Integer start, int estado)throws IOException{
		int gestion =Integer.parseInt(request.getParameter("gestion"));
		if (gestion!=0) {
			System.out.println("gestion:"+gestion);
		}
		
//		int numsolt_s=Integer.parseInt(request.getParameter("numsolt"));
//		System.out.println(request.getParameter("numsolt")=="");
//		System.out.println(request.getParameter("numsolt").isEmpty());
//		System.out.println(request.getParameter("numsolt")==null);
		
		int numsolt=-1;
		if(!request.getParameter("numsolt").isEmpty()) {
			numsolt=Integer.parseInt(request.getParameter("numsolt"));
		}
		
		
		int numord=-1;
		if(!request.getParameter("numord").isEmpty()) {
			numord=Integer.parseInt(request.getParameter("numord"));
		}
		
//		System.out.println("numsolt"+numsolt);
//		System.out.println("numord"+numord);
		
		
		String total;
		Map<String, Object> Data = new HashMap<String, Object>();
		String search = request.getParameter("search[value]");
//		List<OrdenServicio> OrdenesServicios=this.manejadorServicios.listar_orden_servicio_d(start, estado, search,length,gestion,numord,numsolt);
		List<OrdenServicio> OrdenesServicios=this.manejadorServicios.listar_orden_servicio_d1(start, estado, search,length,gestion,numord,numsolt);
//		System.out.println("ListaOrdenesServicios:"+OrdenesServicios.toString());
	
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
	
	@RequestMapping(value="datosNuevoServicio")
	public ResponseEntity<List<List<?>>> datanewServ(){
		List<List<?>> lista=new ArrayList<List<?>>();
		List<?> ListaReductores=this.manejadorServicios.ListReductores();
//		List<?> ListaMarcaReductores=this.manejadorServicios.ListMarcaReductores();
		List<?> ListaCilindros=this.manejadorServicios.ListCilindros();
		List<?> ListaSistemaMotor=this.manejadorServicios.ListSistemaMotor();
		List<?> ListaCombustibles=this.manejadorServicios.ListCombustibles();
		lista.add(ListaReductores);
		lista.add(ListaCilindros);
		lista.add(ListaSistemaMotor);
//		lista.add(ListaMarcaReductores);
		lista.add(ListaCombustibles);
		System.out.println("datosNuevoServicio:"+lista.toString());
		return new ResponseEntity<List<List<?>>>(lista,HttpStatus.OK);
	}
	
	@Transactional
	@RequestMapping(value="adicionar")
	public Map<String, Object> adicionar(HttpServletRequest req,HttpServletResponse res){
		HttpSession sesion=req.getSession(true);
		Persona xuser=(Persona) sesion.getAttribute("xusuario");
		Map<String, Object> respuesta=new HashMap<String, Object>();
		try{
			boolean consulta=this.manejadorServicios.registrar(req,xuser);
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
	public Map<String, Object> adicionar_d(HttpServletRequest req,HttpServletResponse res){
		HttpSession sesion=req.getSession(true);
		Persona xuser=(Persona) sesion.getAttribute("xusuario");
		Map<String, Object> respuesta=new HashMap<String, Object>();
		
		
		try{
			boolean consulta=this.manejadorServicios.registrar_d(req,xuser);
			System.out.println("resp: "+consulta);
			respuesta.put("estado", consulta);
		}catch (Exception e){
			e.printStackTrace();
			System.out.println(e.getMessage());
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			respuesta.put("estado",false);
		}
		return respuesta;
	}
	@RequestMapping(value="datos-mod")
	public ResponseEntity<Servicio> datosMod(HttpServletRequest req){
		Servicio data=this.manejadorServicios.datosModificar_servicio(Integer.parseInt(req.getParameter("idserv")));
		
		System.out.println("Servicio a Modificar:"+data);
		return new ResponseEntity<Servicio>(data,HttpStatus.OK);		
	}
	@Transactional
	@RequestMapping(value="modificar")
	public Map<String, Object> modificar(HttpServletRequest req,HttpServletResponse res){
		Map<String, Object> respuesta=new HashMap<String, Object>();
		HttpSession sesion=req.getSession(true);
		Persona xuser=(Persona) sesion.getAttribute("xusuario");
		try {
			boolean consulta=this.manejadorServicios.modificar(req,xuser);
			System.out.println(consulta);
			respuesta.put("estado", consulta);
		} catch (Exception e) {
			// TODO: handle exception
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
		String idserv=req.getParameter("idserv");
		System.out.println("idserv_servidor: "+idserv);
		Map<String, Object> respuesta=new HashMap<String, Object>();
		try {
			boolean resp=this.manejadorServicios.eliminar(Integer.parseInt(idserv));
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
		String idserv=req.getParameter("idserv");
		System.out.println("idserv_servidor: "+idserv);
		Map<String, Object> respuesta=new HashMap<String, Object>();
		try {
			boolean resp=this.manejadorServicios.habilitar(Integer.parseInt(idserv));
			respuesta.put("estado", resp);
		} catch (Exception e) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			respuesta.put("estado",false);
		}
		return new ResponseEntity<Map<String,Object>>(respuesta,HttpStatus.OK);	
	}
	/*ORDEN DE SERVICIO*/
	@RequestMapping(value="datosOrdenServicio")
	public ResponseEntity<List<List<?>>> dataOrdenServicio(HttpServletRequest req,HttpServletResponse res){
		int gestion=Integer.parseInt(req.getParameter("gestion"));
		List<List<?>> lista=new ArrayList<List<?>>();
		List<Map<String, Object>> Solicitud=new ArrayList<>();
		Map<String, Object> mapa=new HashMap<>();
		Date date = new Date();
		String fecha= new SimpleDateFormat("yyyy-MM-dd").format(date);
		mapa.put("numOrdServ", this.manejadorServicios.numeroOrdenServicioByGestion(gestion));
		mapa.put("fecha",fecha);
		Solicitud.add(0, mapa);
		List<?> ListaTalleres=this.manejadorServicios.ListTalleres(gestion);
//		List<?> ListaRom=this.manejadorServicios.ListChipRom();
		List<Servicio> ListaServicios=this.manejadorServicios.ListServicios(gestion);
		List<Combustible> listaComb=this.manejadorServicios.ListComb();
		lista.add(Solicitud);
		lista.add(ListaTalleres); 
		lista.add(ListaServicios);
		lista.add(listaComb);
//		lista.add(ListaRom);
		System.out.println("ListaOrdenServicios: "+lista.toString());
		return new ResponseEntity<List<List<?>>>(lista,HttpStatus.OK);
	}
	
	@RequestMapping(value="FiltroSolicitudOS")
	public ResponseEntity<List<Solicitud>> FiltroSolicitud(HttpServletRequest req,HttpServletResponse res){
		String texto=req.getParameter("texto");
		int gestion=Integer.parseInt(req.getParameter("gestion"));
		System.out.println("texto: "+texto);
		 
		List<Solicitud> solt=null;
		try {
			solt=this.manejadorServicios.FiltroSolicitudOS(req.getParameter("texto"),gestion);
			System.out.println("TAM: "+solt.size());
			System.out.println("KU: "+solt);
		} catch (Exception e) {
			solt=null;
		}
		return new ResponseEntity<List<Solicitud>>(solt,HttpStatus.OK);
	}
	
//	@Transactional
//	@RequestMapping(value="adicionarOS")
//	public Map<String, Object> adicionarOS(HttpServletRequest req,HttpServletResponse res){
//		HttpSession sesion=req.getSession(true);
//		Persona xuser=(Persona) sesion.getAttribute("xusuario");
//		Map<String, Object> respuesta=new HashMap<String, Object>();
//		int numOrdServ=Integer.parseInt(req.getParameter("numOrdServ"));
//		int idSolt=Integer.parseInt(req.getParameter("idsolt"));
//		int idServ=Integer.parseInt(req.getParameter("idserv"));
//		int idtall=Integer.parseInt(req.getParameter("idtall"));
//		int idRom=Integer.parseInt(req.getParameter("idRom"));
//		System.out.println("numOrdServ: "+numOrdServ+" idSolt: "+idSolt+" idServ: "+idServ+" idTall:"+idtall+" idRom:"+idRom);
//		try{
//			Object[] consulta=this.manejadorServicios.registrarOS(req,xuser);
//			System.out.println("resp: "+consulta);
//			respuesta.put("estado", consulta[0]);
//			respuesta.put("ordServ",Integer.parseInt(consulta[1].toString()));
//
//		}catch (Exception e){
//			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
//			respuesta.put("estado",false);
//		}
//		return respuesta;
//	}
//	
	@Transactional
	@RequestMapping(value="adicionarOS")
	public Map<String, Object> adicionarOS(HttpServletRequest req,HttpServletResponse res){
		HttpSession sesion=req.getSession(true);
		Persona xuser=(Persona) sesion.getAttribute("xusuario");
		Map<String, Object> respuesta=new HashMap<String, Object>();
		int numOrdServ=Integer.parseInt(req.getParameter("numOrdServ"));
		int idSolt=Integer.parseInt(req.getParameter("idsolt"));
		int idServ=Integer.parseInt(req.getParameter("idserv"));
		int idtall=Integer.parseInt(req.getParameter("idtall"));
		System.out.println("numOrdServ: "+numOrdServ+" idSolt: "+idSolt+" idServ: "+idServ+" idTall:"+idtall);
		try{
			Object[] consulta=this.manejadorServicios.registrarOS(req,xuser);
			System.out.println("resp: "+consulta);
			respuesta.put("estado", consulta[0]);
			respuesta.put("ordServ",Integer.parseInt(consulta[1].toString()));

		}catch (Exception e){
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			respuesta.put("estado",false);
		}
		return respuesta;
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
	
	
	@RequestMapping(value="VerOS")
	public ResponseEntity<List<?>> VerSolicitud(HttpServletRequest req,HttpServletResponse res){		
		List<Object> lista=new ArrayList<>();
		String ListaTelefonosTaller="",ListaTelefonosBeneficiario="";
		String idOrdServ=req.getParameter("idOrdServ");
		System.out.println("idOrdServ: "+idOrdServ);
		List<Combustible> listaComb=this.manejadorServicios.ListComb();
		OrdenServicio OrdenServicio=this.manejadorServicios.verOrdenServicio(Integer.parseInt(idOrdServ));
		System.out.println("OrdenServicio:"+OrdenServicio.getTaller()); 
		List<Telefono> ListaTelfTaller=this.manejadorServicios.ListaTelf(OrdenServicio.getTaller().getPersona().getIdper());
		System.out.println("ListaTelefonosTaller: "+ListaTelfTaller.toString());	
		for (int i = 0; i < ListaTelfTaller.size(); i++) {
			System.out.println("ListaTelS: "+ListaTelfTaller.get(i));
			ListaTelefonosTaller+=ListaTelfTaller.get(i).getNumero()+" ";
		}
		Solicitud solt=this.manejadorSolicitudes.getSoltByOrdServ(OrdenServicio.getIdordserv());
		OrdenServicio.setSolicitud(solt);
		List<Telefono> ListaTelfBeneficiario=this.manejadorServicios.ListaTelf(OrdenServicio.getSolicitud().getPersona().getIdper());
		for (int i = 0; i < ListaTelfBeneficiario.size(); i++) {
			System.out.println("ListaTelS: "+ListaTelfBeneficiario.get(i));
			ListaTelefonosBeneficiario+=ListaTelfBeneficiario.get(i).getNumero()+" ";
		}
		ListaTelefonosTaller=ListaTelefonosTaller.trim().replaceAll(" ","-");
		ListaTelefonosBeneficiario=ListaTelefonosBeneficiario.trim().replaceAll(" ","-");
		System.out.println("ListaTelefonosTaller: "+ListaTelefonosTaller);
		System.out.println("ListaTelefonosBeneficiario: "+ListaTelefonosTaller);
		Map<String, Object> mapa1=new HashMap<>();
		mapa1.put("listaTelefonosTaller", ListaTelefonosTaller);
		mapa1.put("listaTelefonosBeneficiario", ListaTelefonosBeneficiario);
		lista.add(OrdenServicio);
		lista.add(mapa1);
		lista.add(listaComb);
		return new ResponseEntity<List<?>>(lista,HttpStatus.OK);
	}
	@Autowired
	DataSource dataSource;
	@RequestMapping("ImprimirOS")
	public  void verOS(HttpServletResponse res,HttpServletRequest req){
		URIS uris=new URIS();
//		Persona us=(Persona)req.getSession(true).getAttribute("xusuario");
//		String Tramitador=us.getAp().toUpperCase()+" "+us.getAm().toUpperCase()+" "+us.getNombres().toUpperCase();
		String idOrdServ=req.getParameter("idOrdServ");
		String ListaTelefonosTaller="",ListaTelefonosBeneficiario="";
		
		List<Telefono> ListaTelfTall=this.manejadorServicios.ListaTelfTall(Integer.parseInt(idOrdServ));
//		System.out.println("ListaTelefonosTall: "+ListaTelfTall.toString());
		
		for (int i = 0; i < ListaTelfTall.size(); i++) {
//			System.out.println("ListaTelTall: "+ListaTelfTall.get(i));
			ListaTelefonosTaller+=ListaTelfTall.get(i).getNumero()+" ";
		}
		ListaTelefonosTaller=ListaTelefonosTaller.trim().replaceAll(" ","-");
//		System.out.println("ListaTelefonosTall: "+ListaTelefonosTaller);
		
		
		List<Telefono> ListaTelfBen=this.manejadorServicios.ListaTelfBen(Integer.parseInt(idOrdServ));
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
		                       
		String url=uris.jasperReport+"getOrdenServicio_m.jasper";	
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
}
