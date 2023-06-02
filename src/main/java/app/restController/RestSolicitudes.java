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


import app.manager.ManejadorBeneficiarios;
import app.manager.ManejadorServicios;
import app.manager.ManejadorSolicitudes;
import app.models.CombustibleVehiculo;
import app.models.DocumentoBeneficiario;
import app.models.MarcaVehiculo;
import app.models.OpcionesVehiculo;
import app.models.Persona;
import app.models.Solicitud;
import app.models.Telefono;
import app.models.TipoMarcaVehiculo;
import app.models.Vehiculo;
import app.utilidades.GeneradorReportes;
import app.utilidades.URIS;


@RequestMapping("/RestSolicitudes/")
@RestController 
public class RestSolicitudes {        
	@Autowired      
	ManejadorSolicitudes manejadorSolicitudes;
	@Autowired
	ManejadorBeneficiarios manejadorBeneficiarios;
	@Autowired
	ManejadorServicios manejadorServicios;
	
	public int [] GCombustible=null;
	
	@RequestMapping(value="listar")
	public ResponseEntity<List<Solicitud>> listarSolt(HttpServletRequest req,HttpServletResponse res){	
		List<Solicitud> solicitudes=this.manejadorSolicitudes.Listar(req);
		System.out.println("LINK "+req.getSession().getServletContext().getRealPath("/fotos"));
		System.out.println("listaSolt: "+solicitudes.toString());
		return new ResponseEntity<List<Solicitud>>(solicitudes,HttpStatus.OK);
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping("listar_d")
	public @ResponseBody Map<?, ?> lista(HttpServletRequest request, Integer draw,int length, Integer start, int estado)throws IOException{

		String total;
		Map<String, Object> Data = new HashMap<String, Object>();
		String search = request.getParameter("search[value]");
		List<Solicitud> lista=this.manejadorSolicitudes.listar_d(start, estado, search,length);
		//System.out.println("lista:"+lista.toString());
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
	@SuppressWarnings("unchecked") 
	@RequestMapping("listar_d1")
	public @ResponseBody Map<?, ?> lista1(HttpServletRequest request, Integer draw,int length, Integer start, int estado)throws IOException{
		int gestion =Integer.parseInt(request.getParameter("gestion"));
		if (gestion!=0) {
			System.out.println("gestion:"+gestion);
		}
		String total;
		Map<String, Object> Data = new HashMap<String, Object>();
		String search = request.getParameter("search[value]");
		List<Solicitud> lista=this.manejadorSolicitudes.listar_d1(start, estado, search,length,gestion);
		System.out.println("listaSolicitudes:"+lista.toString());
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
	
	
	@RequestMapping(value="listarVehiculos")
	public ResponseEntity<List<Vehiculo>> listarVehiculos(HttpServletRequest req,HttpServletResponse res){	
		List<Vehiculo> lista=this.manejadorSolicitudes.ListarVeh(req);
		System.out.println("lista: "+lista.toString());
		return new ResponseEntity<List<Vehiculo>>(lista,HttpStatus.OK);
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
	@RequestMapping({"busqueda_benficiario"})
	public ResponseEntity<List<?>> busqueda_benficiario(HttpServletRequest req,HttpServletResponse res){
		List<?> listaBen=this.manejadorBeneficiarios.Listabenficiario(req);
//		System.out.println("l: "+listaBen);
		return new ResponseEntity<List<?>>(listaBen,HttpStatus.OK);
	}
	@RequestMapping({"opcionesVehiculo"})
	public ResponseEntity<OpcionesVehiculo> opcionesVehiculo(HttpServletRequest req,HttpServletResponse res){
		OpcionesVehiculo lista= new OpcionesVehiculo();
		int gestion=Integer.parseInt(req.getParameter("gestion"));
//		lista.setIdacc(this.manejadorSolicitudes.getAccion());
		lista.setTipoVehiculo(this.manejadorSolicitudes.tipoVehiculo());
		lista.setMarcaVehiculo(this.manejadorSolicitudes.marcaVehiculo());
		lista.setModeloVehiculo(this.manejadorSolicitudes.modeloVehiculo());
		lista.setTipoMotor(this.manejadorSolicitudes.tipoMotorVehiculo());
		lista.setTipoServicio(this.manejadorSolicitudes.tipoServicioVehiculo());
		lista.setCombustibles(this.manejadorSolicitudes.tipoCombustible());
		lista.setNumSolt(this.manejadorSolicitudes.numeroSolicitudByGestion(gestion));
		System.out.println("lista_listas: "+lista);
		return new ResponseEntity<OpcionesVehiculo>(lista,HttpStatus.OK);
	}
	
	@RequestMapping({"getNumSoltByAnio"})
	public ResponseEntity<Map<String,Object>> getNumSoltByAnio(HttpServletRequest req,HttpServletResponse res){
		Map<String, Object> datos=new HashMap<String, Object>();
		int gestion=Integer.parseInt(req.getParameter("gestion"));

		datos.put("numSolt",this.manejadorSolicitudes.numeroSolicitudByGestion(gestion));
		System.out.println("datos: "+datos.toString());
		return new ResponseEntity<Map<String,Object>>(datos,HttpStatus.OK);
	}
	
	
	
	@RequestMapping(value="getTipoMarcaVeh")
	public ResponseEntity<List<Object>> getTipoMarcaVeh(HttpServletRequest req){
		List<Object> data=new ArrayList<Object>();
		
		List<TipoMarcaVehiculo> marca=this.manejadorSolicitudes.ListarTipoMarcaVeh();
		data.add(marca);
		System.out.println("data:"+data.toString());
		return new ResponseEntity<List<Object>>(data,HttpStatus.OK);		
	}
	
	@RequestMapping(value="getTipoMarcaVehByMarca")
	public ResponseEntity<List<Object>> getTipoMarcaVehByMarcaByMarca(HttpServletRequest req){
		List<Object> data=new ArrayList<Object>();
		
		List<TipoMarcaVehiculo> marca=this.manejadorSolicitudes.getTipoMarcaVehByMarca(req);
		data.add(marca);
		System.out.println("data:"+data.toString());
		return new ResponseEntity<List<Object>>(data,HttpStatus.OK);		
	}
	
	@RequestMapping ({"existePlaca"})
	public ResponseEntity<Map<String, Object>> existe(HttpServletRequest req){
		Map<String, Object> mapa=new HashMap<String, Object>();
		String placa=req.getParameter("placa");
		int existe;
		System.out.println("tam_"+placa.length());
		//VERIFICA PRIMERO SI EXISTE LA PLACA QUE ESTE DISPONIBLE 
		if(this.manejadorSolicitudes.verificarPlaca(placa)){
			existe=2;
		}else{
			//SI LA PLACA ESTA EN USO SE VERIFICA EN QUE  ESTADO ESTA PLACA
			existe=this.manejadorSolicitudes.EstadoPlaca(placa);			
		}
		System.out.println("existe: "+existe);
		mapa.put("estado", existe);
		return new ResponseEntity<Map<String,Object>>(mapa,HttpStatus.OK);
	}
	@RequestMapping({"PlacaDatosRecalificacion"})
	public ResponseEntity<Vehiculo> DatosVeh(HttpServletRequest req){
		String placa=req.getParameter("placa");
		System.out.println("la placa es : "+placa);
		Vehiculo veh=this.manejadorSolicitudes.DatosVehiculoRecalificacion(placa);
		return new ResponseEntity<Vehiculo>(veh,HttpStatus.OK);
	}
	
	//MODIFICAR VEH
	@RequestMapping ({"existePlacaMod"})
	public ResponseEntity<Map<String, Object>> existePlacaMod(HttpServletRequest req){
		Map<String, Object> mapa=new HashMap<String, Object>();
		String placa=req.getParameter("placa");
		int existe;
		System.out.println("tam_"+placa.length());
		//VERIFICA PRIMERO SI EXISTE LA PLACA QUE ESTE DISPONIBLE 
		if(this.manejadorSolicitudes.verificarPlacaMod(placa)){
			existe=1;
		}else{
			//SI LA PLACA ESTA EN USO SE VERIFICA EN QUE  ESTADO ESTA PLACA
			existe=0;
		}
		System.out.println("existe: "+existe);
		mapa.put("estado", existe);
		return new ResponseEntity<Map<String,Object>>(mapa,HttpStatus.OK);
	}

	@RequestMapping({"getDatosVehiculoByPlaca"})
	public ResponseEntity<Vehiculo> GetDatosVehiculo(HttpServletRequest req){
		String placa=req.getParameter("placa");
		System.out.println("la placa es : "+placa);
		Vehiculo veh=this.manejadorSolicitudes.getDatosVehiculoByPlaca(placa);
		return new ResponseEntity<Vehiculo>(veh,HttpStatus.OK);
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

	//FIN MODIFICAR VEH
	/*
	@Transactional
	@RequestMapping(value="adicionar")
	public Map<String, Object> adicionar(HttpServletRequest req,HttpServletResponse res,Solicitud s,Vehiculo v,@RequestParam int [] combustible){
		System.out.println("SolicitudSolicitud: "+s.toString());
		System.out.println("VehiculoSolicitud: "+v.toString());
		
		HttpSession sesion=req.getSession(true);
		Persona xuser=(Persona) sesion.getAttribute("xusuario");
		int idacc=this.manejadorSolicitudes.getAccion();
		System.out.println("idacc: "+idacc);
		
		System.out.println("tamanio: "+combustible.length);
		Map<String, Object> respuesta=new HashMap<String, Object>();
		
		for (int i : combustible) {
			System.out.println("cod_combustible: "+i);
		}

		try {
			Object[] RespSolicitud=this.manejadorSolicitudes.registrar(req,xuser,v,s,combustible,idacc);
			respuesta.put("estado", RespSolicitud[0]);
			respuesta.put("idsolt", Integer.parseInt(RespSolicitud[1].toString()));
		} catch (Exception e) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			respuesta.put("estado",false);
		}

		return respuesta;
	}
	 */
	
	
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
			
			Object[] RespSolicitud=this.manejadorSolicitudes.registrar_d1(req,xuser,v,s,combustible,idacc,p, documentos,telefonos);
			respuesta.put("estado", RespSolicitud[0]);
			respuesta.put("idsolt", Integer.parseInt(RespSolicitud[1].toString()));
			
			System.out.println("IDSOLT_PARA REPORTE:"+Integer.parseInt(RespSolicitud[1].toString()));
			
//			respuesta.put("estado", true);
//			respuesta.put("idsolt", 1);
			
		} catch (Exception e) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			respuesta.put("estado",false);
		}

		return respuesta;
	}
	
	
	@Transactional
	@RequestMapping(value="anular")
	public Map<String, Object> anular(HttpServletRequest req,HttpServletResponse res){
		String idsolt=req.getParameter("idsolt");
		System.out.println("idSolt_servidor"+idsolt);
		Map<String, Object> respuesta=new HashMap<String, Object>();
		try {
			boolean solicitud=this.manejadorSolicitudes.anular(Integer.parseInt(idsolt));
			
			respuesta.put("estado", solicitud);
		} catch (Exception e) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			// TODO: handle exception
			respuesta.put("estado",false);
		}
//		respuesta.put("estado", true);
		return respuesta;
	}
	
	

	@RequestMapping(value="FiltroSolicitudOS")
	public ResponseEntity<List<Solicitud>> FiltroSolicitud(HttpServletRequest req,HttpServletResponse res){
		String texto=req.getParameter("texto");
		System.out.println("texto: "+texto);
		 
		List<Solicitud> solt=null;
		try {
			solt=this.manejadorSolicitudes.FiltroSolicitudOS(req.getParameter("texto"));
			System.out.println("TAM: "+solt.size());
			System.out.println("KU: "+solt);
		} catch (Exception e) {
			solt=null;
		}
		return new ResponseEntity<List<Solicitud>>(solt,HttpStatus.OK);
	}
	@RequestMapping(value="Ver")
//	public ResponseEntity<List<List<?>>> VerSolicitud(HttpServletRequest req,HttpServletResponse res){
	public ResponseEntity<List<?>> VerSolicitud(HttpServletRequest req,HttpServletResponse res){
//		List<List<?>> lista=new ArrayList<List<?>>();
		List<Object> lista=new ArrayList<>();
		String ListaTelefonos="";
		String idsolt=req.getParameter("idsolt");
		System.out.println("idsolt: "+idsolt);
		  
		List<Telefono> ListaTelf=this.manejadorSolicitudes.ListaTelf(Integer.parseInt(idsolt));
		Solicitud solt=this.manejadorSolicitudes.verSolicitud(Integer.parseInt(idsolt));
		System.out.println("ListaTelefonos: "+ListaTelf.toString());
		
		for (int i = 0; i < ListaTelf.size(); i++) {
			System.out.println("ListaTelS: "+ListaTelf.get(i));
			ListaTelefonos+=ListaTelf.get(i).getNumero()+" ";
		}
		ListaTelefonos=ListaTelefonos.trim().replaceAll(" ","-");
		System.out.println("ListaTelefonos: "+ListaTelefonos);
		Map<String, Object> mapa1=new HashMap<>();
		mapa1.put("listaTelefonos", ListaTelefonos);
		List<DocumentoBeneficiario> listaDoc=this.manejadorBeneficiarios.listaDocumentos();
		List<CombustibleVehiculo> listaComb=this.manejadorSolicitudes.listaCombustible();
		lista.add(solt);
		lista.add(mapa1);
		lista.add(listaDoc);
		lista.add(listaComb);
		return new ResponseEntity<List<?>>(lista,HttpStatus.OK);
	}
	@Autowired //METODO SIN USO
	DataSource dataSource;
	@RequestMapping(value="Imprimir_d1/{id}",method=RequestMethod.GET)
	public  void Imprimir_d(HttpServletResponse res,HttpServletRequest req,@PathVariable("id") Integer id){
		URIS uris=new URIS();
		Persona us=(Persona)req.getSession(true).getAttribute("xusuario");
		String ListaTelefonos="",Tramitador=us.getAp().toUpperCase()+" "+us.getAm().toUpperCase()+" "+us.getNombres().toUpperCase();
		int idsolt=id;
		System.out.println("idsoltIMPRIMIR: "+idsolt);
		String nombreReporte="Solicitud",tipo="pdf", estado="inline";
		
		Map<String, Object> nitSQL=this.manejadorServicios.nitEmpresa(1); 
		String nit_patam=(String) nitSQL.get("nitInst");
//		System.out.println("nit_param: "+nit_patam);
		
		List<Telefono> ListaTelf=this.manejadorSolicitudes.ListaTelf(idsolt);
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
		                      	
		String url=uris.jasperReport+"solicitud_d.jasper";  	
	                                
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
		  
//			InputStream jasperStream = this.getClass().getResourceAsStream(url);
//
//		    JasperReport jasperReport = (JasperReport) JRLoader.loadObject(jasperStream);
//		    JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parametros, dataSource.getConnection());
//
//		    res.setContentType("application/pdf");
//		    res.setHeader("Content-disposition", "inline; filename=helloWorldReport.pdf");
//
//		    final ServletOutputStream outStream = res.getOutputStream();
//		    JasperExportManager.exportReportToPdfStream(jasperPrint, outStream);
		    
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
	
}
