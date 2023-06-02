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

import app.manager.ManejadorInstalacionKit;
import app.manager.ManejadorServicios;
import app.manager.ManejadorSolicitudes;
import app.models.Cilindro;
import app.models.InstalacionCilindro;
import app.models.MarcaCilindro;
import app.models.MarcaReductor;
import app.models.MarcaValvulaCilindro;
import app.models.OrdenServicio;
import app.models.Persona;
import app.models.Reductor;
import app.models.RegistroKit;
import app.models.Solicitud;
import app.utilidades.GeneradorReportes;
import app.utilidades.URIS;

@RequestMapping("/RestInstalacionKit/")
@RestController
public class RestInstalacionKit {
	@Autowired
	ManejadorInstalacionKit manejadorInstalacionKit;
	@Autowired
	ManejadorServicios manejadorServicios;
	@Autowired 
	ManejadorSolicitudes manejadorSolicitudes;
	
	@RequestMapping(value="listar")
	public ResponseEntity<List<RegistroKit>> listarBneneficiarios(HttpServletRequest req,HttpServletResponse res){	
		List<RegistroKit> ListRegistro=this.manejadorInstalacionKit.Lista(req);
		System.out.println("listaPRE: "+ListRegistro.toString());
		for (int i = 0; i < ListRegistro.size(); i++){
			
//			ListRegistro.get(i).setOrdenServicio(this.manejadorServicios.getOrdenServicioIK(ListRegistro.get(i).getIdordserv()));
			//MODIFICADO
			ListRegistro.get(i).setOrdenServicio(this.manejadorServicios.getOrdenServicioIK(ListRegistro.get(i).getIdregistroKit()));
			
			ListRegistro.get(i).getOrdenServicio().setSolicitud(this.manejadorSolicitudes.getSoltByOrdServ(ListRegistro.get(i).getIdordserv()));			
		}
		System.out.println("listaPOST: "+ListRegistro.toString());
		return new ResponseEntity<List<RegistroKit>>(ListRegistro,HttpStatus.OK);
	}
	
	@SuppressWarnings("unchecked") 
	@RequestMapping("listar_d")
	public @ResponseBody Map<?, ?> lista1(HttpServletRequest request, Integer draw,int length, Integer start, int estado)throws IOException{
		int gestion =Integer.parseInt(request.getParameter("gestion"));
		String total;
		Map<String, Object> Data = new HashMap<String, Object>();
		String search = request.getParameter("search[value]");
		
		if (gestion!=0) {
//			System.out.println("gestion:"+gestion);
		}
		int idtall=0;
		if(request.getParameter("idtall")!=null) {
			
			idtall=Integer.parseInt(request.getParameter("idtall"));
//			System.out.println("idtall:"+idtall);
		}
		
		HttpSession sesion=request.getSession(true);
		Persona xuser=(Persona) sesion.getAttribute("xusuario");
		
//		int numci_s=Integer.parseInt(request.getParameter("numci"));
//		System.out.println(request.getParameter("numci")=="");
//		System.out.println(request.getParameter("numci").isEmpty());
//		System.out.println(request.getParameter("numci")==null);
		
		
		try {
			if (xuser!=null) {

				String numci="";
				if(!request.getParameter("numci").isEmpty()) {
					numci=request.getParameter("numci");
				}
				
				
				int numord=-1;
				if(!request.getParameter("numord").isEmpty()) {
					numord=Integer.parseInt(request.getParameter("numord"));
				}
				
//				System.out.println("numci"+numci);
//				System.out.println("numord"+numord);
				
				String login=xuser.getUsuario().getLogin();

				
				List<RegistroKit> lista=this.manejadorInstalacionKit.listar_fichas_d(start,estado,search,length,idtall,gestion,numord,numci,login);
				//agregado a la lista de registro kit
				for (int i = 0; i < lista.size(); i++){
					
//					ListRegistro.get(i).setOrdenServicio(this.manejadorServicios.getOrdenServicioIK(ListRegistro.get(i).getIdordserv()));
					//MODIFICADO
					lista.get(i).setOrdenServicio(this.manejadorServicios.getOrdenServicioIK(lista.get(i).getIdregistroKit()));
					
					lista.get(i).getOrdenServicio().setSolicitud(this.manejadorSolicitudes.getSoltByOrdServ(lista.get(i).getIdordserv()));			
				}
				System.out.println("ListaInstalacionesKitLista:"+lista.toString());
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
			}
		}catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
			
		}

		return Data;

	}
	
	
	@RequestMapping(value="datosModal")
	public ResponseEntity<List<Object>> dataOrdenServicio(){
		List<Object> lista=new ArrayList<Object>();
		Map<String, Object> mapa=new HashMap<>();
		Date date = new Date();
		String fecha= new SimpleDateFormat("yyyy-MM-dd").format(date);
		mapa.put("fecha",fecha);
		
		List<MarcaCilindro> listaMarCil=this.manejadorServicios.ListaMarcaCilindros();
		List<MarcaReductor> listaMarRed=this.manejadorServicios.ListaMarcaReductores();
		List<?> listaCapacidadCilindros=this.manejadorServicios.ListaCapacidadCilindros();
		List<?> listaMarcaValvulasCilindros=this.manejadorServicios.ListaMarcaValvulaCilindro();
		
		List<?> ListaMarcasConmutador=this.manejadorInstalacionKit.ListaMarcasConmutador();
		List<?> ListaMarcasValvulaCargaInterna=this.manejadorInstalacionKit.ListaMarcasValvulaCargaInterna();
		List<?> ListaMarcasValvulaCargaExterna=this.manejadorInstalacionKit.ListaMarcasValvulaCargaExterna();
		List<?> ListaMarcasEmulador=this.manejadorInstalacionKit.ListaMarcasEmulador();
		List<?> ListaMarcasManometro=this.manejadorInstalacionKit.ListaMarcasManometro();
		List<?> ListaMarcasMemoria=this.manejadorInstalacionKit.ListaMarcasMemoria();
		List<?> ListaMarcasSensorMap=this.manejadorInstalacionKit.ListaMarcasSensorMap();
		List<?> ListaMarcasFiltroGas=this.manejadorInstalacionKit.ListaMarcasFiltroGas();
		List<?> ListaMarcasRamplaInyectores=this.manejadorInstalacionKit.ListaMarcasRamplaInyectores();
		//adicionado segunda version
		List<?> ListaMarcasCañeriaAltaPresion=this.manejadorInstalacionKit.ListaMarcasCañeriaAltaPresion();
		List<?> ListaMarcasElectrovalvulaGasolina=this.manejadorInstalacionKit.ListaMarcasElectrovalvulaGasolina();
		List<?> ListaMarcasElectrovalvulaGas=this.manejadorInstalacionKit.ListaMarcasElectrovalvulaGas();
		List<?> ListaMarcasMangueraGas=this.manejadorInstalacionKit.ListaMarcasMagueraGas();
		List<?> ListaMarcasMangueraAgua=this.manejadorInstalacionKit.ListaMarcasMagueraAgua();
		List<?> ListaMarcasMangueraVacio=this.manejadorInstalacionKit.ListaMarcasMagueraVacio();
		
		lista.add(mapa);
		lista.add(listaMarRed);
		lista.add(listaMarCil);
		lista.add(listaCapacidadCilindros);
		lista.add(listaMarcaValvulasCilindros);
		
		lista.add(ListaMarcasConmutador);
		lista.add(ListaMarcasValvulaCargaInterna);
		lista.add(ListaMarcasValvulaCargaExterna);
		lista.add(ListaMarcasEmulador);
		lista.add(ListaMarcasManometro);
		lista.add(ListaMarcasMemoria);
		lista.add(ListaMarcasSensorMap);
		lista.add(ListaMarcasFiltroGas);
		lista.add(ListaMarcasRamplaInyectores);
		
		lista.add(ListaMarcasCañeriaAltaPresion);
		lista.add(ListaMarcasElectrovalvulaGasolina);
		lista.add(ListaMarcasElectrovalvulaGas);
		lista.add(ListaMarcasMangueraGas);
		lista.add(ListaMarcasMangueraAgua);
		lista.add(ListaMarcasMangueraVacio);
		
		
		
		
		System.out.println("Lista: "+lista.toString());
		return new ResponseEntity<List<Object>>(lista,HttpStatus.OK);
	}
	
	@RequestMapping(value="datosModal_d")
	public ResponseEntity<List<Object>> dataOrdenServicio_d(){
		List<Object> lista=new ArrayList<Object>();
		Map<String, Object> mapa=new HashMap<>();
		Date date = new Date();
		String fecha= new SimpleDateFormat("yyyy-MM-dd").format(date);
		mapa.put("fecha",fecha);
		
		List<MarcaCilindro> listaMarCil=this.manejadorServicios.ListaMarcaCilindros();
		List<MarcaReductor> listaMarRed=this.manejadorServicios.ListaMarcaReductores();
		List<?> listaCapacidadCilindros=this.manejadorServicios.ListaCapacidadCilindros();
		List<?> listaMarcaValvulasCilindros=this.manejadorServicios.ListaMarcaValvulaCilindro();

		
		lista.add(mapa);
		lista.add(listaMarRed);
		lista.add(listaMarCil);
		lista.add(listaCapacidadCilindros);
		lista.add(listaMarcaValvulasCilindros);

		System.out.println("Lista: "+lista.toString());
		return new ResponseEntity<List<Object>>(lista,HttpStatus.OK);
	}
	
	
	@RequestMapping(value="FiltroOrdenServicio")
	public ResponseEntity<List<OrdenServicio>> FiltroOrdenServicio(HttpServletRequest req,HttpServletResponse res){
//		HttpSession sesion=req.getSession(true);
//		Persona xuser=(Persona) sesion.getAttribute("xusuario");
		String texto=req.getParameter("texto");
		System.out.println("textoIK: "+texto);
		 
		List<OrdenServicio> ListOrdenServicio=null;
		
		HttpSession sesion=req.getSession(true);
		Persona xuser=(Persona) sesion.getAttribute("xusuario");
		
		int gestion =Integer.parseInt(req.getParameter("gestion"));
		int idtall=0;
		
		if(req.getParameter("idtall")!=null) {
			idtall=Integer.parseInt(req.getParameter("idtall"));
		}
		
		if (gestion!=0) {
			System.out.println("gestionIK:"+gestion);
		}
		try {
			String login=xuser.getUsuario().getLogin();

			ListOrdenServicio=this.manejadorServicios.FiltroOrdenServicioIK(req.getParameter("texto"),login,gestion,idtall);
			for (int i = 0; i < ListOrdenServicio.size(); i++) {
				ListOrdenServicio.get(i).setSolicitud(this.manejadorSolicitudes.getSoltByOrdServ(ListOrdenServicio.get(i).getIdordserv()));		
			}
			System.out.println("TAM: "+ListOrdenServicio.size());
			System.out.println("KU: "+ListOrdenServicio);
		} catch (Exception e) {
			ListOrdenServicio=null;
		}
		return new ResponseEntity<List<OrdenServicio>>(ListOrdenServicio,HttpStatus.OK);
	}
	@Transactional
	@RequestMapping(value="adicionar_bck")
	public Map<String, Object> adicionar(HttpServletRequest req,HttpServletResponse res){
		HttpSession sesion=req.getSession(true);
		Persona xuser=(Persona) sesion.getAttribute("xusuario");

		Map<String, Object> respuesta=new HashMap<String, Object>();
		try{
			Object[] consulta=this.manejadorInstalacionKit.registrar_bck(req,xuser);
			System.out.println("resp: "+consulta);
//			respuesta.put("estado", true);
			respuesta.put("estado", consulta[0]);
			respuesta.put("idRecep",Integer.parseInt(consulta[1].toString()));
		}catch (Exception e){
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			respuesta.put("estado",false);
		}
		return respuesta;
	}

	@Transactional
	@RequestMapping(value="adicionar")
	public Map<String, Object> adicionar_d1(HttpServletRequest req,HttpServletResponse res){
		HttpSession sesion=req.getSession(true);
		Persona xuser=(Persona) sesion.getAttribute("xusuario");

		Map<String, Object> respuesta=new HashMap<String, Object>();
		try{
			Object[] consulta=this.manejadorInstalacionKit.registrar(req,xuser);
			System.out.println("resp: "+consulta);
//			respuesta.put("estado", true);
			respuesta.put("estado", consulta[0]);
			respuesta.put("idregistrokit",Integer.parseInt(consulta[1].toString()));
		}catch (Exception e){
			System.out.println(e.getMessage());
			e.printStackTrace();
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			respuesta.put("estado",false);
		}
		return respuesta;
	}
	@RequestMapping(value="obtener")
	public ResponseEntity<List<Object>> datosmodificar(HttpServletRequest req){
		List<Object> lista=new ArrayList<Object>();
		Map<String, Object> mapa=new HashMap<>();
		Date date = new Date();
		String fecha= new SimpleDateFormat("yyyy-MM-dd").format(date);
		mapa.put("fecha",fecha);
		
		

		
		String idregistroKit=req.getParameter("idregistroKit");
		System.out.println("idregistroKit: "+idregistroKit);
//		Map<String, Object> respuesta=new HashMap<String, Object>();
		RegistroKit rg=null;
		try {
			System.out.println("idregistroKit: "+idregistroKit);
			rg=this.manejadorInstalacionKit.verInstacionKit(Integer.parseInt(idregistroKit));
			OrdenServicio ordServ=this.manejadorServicios.getOrdenServicioIK(rg.getIdregistroKit());
			Solicitud solt=this.manejadorSolicitudes.getSoltByOrdServ(ordServ.getIdordserv());
			ordServ.setSolicitud(solt);
			List<InstalacionCilindro> InsCil=this.manejadorInstalacionKit.ListaCilindro(rg.getIdregistroKit());
			rg.setOrdenServicio(ordServ);
			rg.setCilindros(InsCil);
			
			List<MarcaCilindro> listaMarCil=this.manejadorServicios.ListaMarcaCilindros();
			List<MarcaReductor> listaMarRed=this.manejadorServicios.ListaMarcaReductores();
			List<?> listaCapacidadCilindros=this.manejadorServicios.ListaCapacidadCilindros();
			List<MarcaValvulaCilindro> listaMarcaValvulasCilindros=this.manejadorServicios.ListaMarcaValvulaCilindro();
			
			
			List<?> ListaMarcasConmutador=this.manejadorInstalacionKit.ListaMarcasConmutador();
			List<?> ListaMarcasValvulaCargaInterna=this.manejadorInstalacionKit.ListaMarcasValvulaCargaInterna();
			List<?> ListaMarcasValvulaCargaExterna=this.manejadorInstalacionKit.ListaMarcasValvulaCargaExterna();
			List<?> ListaMarcasEmulador=this.manejadorInstalacionKit.ListaMarcasEmulador();
			List<?> ListaMarcasManometro=this.manejadorInstalacionKit.ListaMarcasManometro();
			List<?> ListaMarcasMemoria=this.manejadorInstalacionKit.ListaMarcasMemoria();
			List<?> ListaMarcasSensorMap=this.manejadorInstalacionKit.ListaMarcasSensorMap();
			List<?> ListaMarcasFiltroGas=this.manejadorInstalacionKit.ListaMarcasFiltroGas();
			List<?> ListaMarcasRamplaInyectores=this.manejadorInstalacionKit.ListaMarcasRamplaInyectores();
			
			//adicionado segunda version
			List<?> ListaMarcasCañeriaAltaPresion=this.manejadorInstalacionKit.ListaMarcasCañeriaAltaPresion();
			List<?> ListaMarcasElectrovalvulaGasolina=this.manejadorInstalacionKit.ListaMarcasElectrovalvulaGasolina();
			List<?> ListaMarcasElectrovalvulaGas=this.manejadorInstalacionKit.ListaMarcasElectrovalvulaGas();
			List<?> ListaMarcasMangueraGas=this.manejadorInstalacionKit.ListaMarcasMagueraGas();
			List<?> ListaMarcasMangueraAgua=this.manejadorInstalacionKit.ListaMarcasMagueraAgua();
			List<?> ListaMarcasMangueraVacio=this.manejadorInstalacionKit.ListaMarcasMagueraVacio();
			
			
			lista.add(mapa);
			lista.add(rg);
			lista.add(listaMarRed);
			lista.add(listaMarCil);
			lista.add(listaCapacidadCilindros);
			lista.add(listaMarcaValvulasCilindros);
			
			lista.add(ListaMarcasConmutador);
			lista.add(ListaMarcasValvulaCargaInterna);
			lista.add(ListaMarcasValvulaCargaExterna);
			lista.add(ListaMarcasEmulador);
			lista.add(ListaMarcasManometro);
			lista.add(ListaMarcasMemoria);
			lista.add(ListaMarcasSensorMap);
			lista.add(ListaMarcasFiltroGas);
			lista.add(ListaMarcasRamplaInyectores);
			
			lista.add(ListaMarcasCañeriaAltaPresion);
			lista.add(ListaMarcasElectrovalvulaGasolina);
			lista.add(ListaMarcasElectrovalvulaGas);
			lista.add(ListaMarcasMangueraGas);
			lista.add(ListaMarcasMangueraAgua);
			lista.add(ListaMarcasMangueraVacio);
			
			System.out.println("REGISTROKIT:"+rg.toString());
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
		return new ResponseEntity<List<Object>>(lista,HttpStatus.OK);	
	}
	
	@RequestMapping(value="obtener_d")
	public ResponseEntity<List<Object>> datosmodificar_d(HttpServletRequest req){
		List<Object> lista=new ArrayList<Object>();
		Map<String, Object> mapa=new HashMap<>();
		Date date = new Date();
		String fecha= new SimpleDateFormat("yyyy-MM-dd").format(date);
		mapa.put("fecha",fecha);
		
		

		
		String idregistroKit=req.getParameter("idregistroKit");
		System.out.println("idregistroKit: "+idregistroKit);
//		Map<String, Object> respuesta=new HashMap<String, Object>();
		RegistroKit rg=null;
		try {
			System.out.println("idregistroKit: "+idregistroKit);
			rg=this.manejadorInstalacionKit.verInstacionKit(Integer.parseInt(idregistroKit));
			OrdenServicio ordServ=this.manejadorServicios.getOrdenServicioIK(rg.getIdregistroKit());
			Solicitud solt=this.manejadorSolicitudes.getSoltByOrdServ(ordServ.getIdordserv());
			ordServ.setSolicitud(solt);
			List<InstalacionCilindro> InsCil=this.manejadorInstalacionKit.ListaCilindro(rg.getIdregistroKit());
			rg.setOrdenServicio(ordServ);
			rg.setCilindros(InsCil);
			
			List<MarcaCilindro> listaMarCil=this.manejadorServicios.ListaMarcaCilindros();
			List<MarcaReductor> listaMarRed=this.manejadorServicios.ListaMarcaReductores();
			List<?> listaCapacidadCilindros=this.manejadorServicios.ListaCapacidadCilindros();
			List<MarcaValvulaCilindro> listaMarcaValvulasCilindros=this.manejadorServicios.ListaMarcaValvulaCilindro();
			
			
			
			
			lista.add(mapa);
			lista.add(rg);
			lista.add(listaMarRed);
			lista.add(listaMarCil);
			lista.add(listaCapacidadCilindros);
			lista.add(listaMarcaValvulasCilindros);
			
			
			System.out.println("REGISTROKIT:"+rg.toString());
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
		return new ResponseEntity<List<Object>>(lista,HttpStatus.OK);	
	}
	
	@Transactional
	@RequestMapping(value="modificar_bck1")
	public Map<String, Object> modificarBCK(HttpServletRequest req,HttpServletResponse res){
		Map<String, Object> respuesta=new HashMap<String, Object>();
		HttpSession sesion=req.getSession(true);
		Persona xuser=(Persona) sesion.getAttribute("xusuario");
		try {
			boolean consulta=this.manejadorInstalacionKit.modificarconversionbck(req,xuser);
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
	
	@Transactional
	@RequestMapping(value="modificar")
	public Map<String, Object> modificar(HttpServletRequest req,HttpServletResponse res){
		Map<String, Object> respuesta=new HashMap<String, Object>();
		HttpSession sesion=req.getSession(true);
		Persona xuser=(Persona) sesion.getAttribute("xusuario");
		try {
			boolean consulta=this.manejadorInstalacionKit.modificarconversion(req,xuser);
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
	
	
	@RequestMapping(value="Ver")
	public ResponseEntity<List<?>> VerSolicitud(HttpServletRequest req,HttpServletResponse res){
//		Persona us=(Persona)req.getSession(true).getAttribute("xusuario");
		List<Object> lista=new ArrayList<>();
//		String Tramitador=us.getAp().toUpperCase()+" "+us.getAm().toUpperCase()+" "+us.getNombres().toUpperCase();
		String idregistroKit=req.getParameter("idregistroKit");
		System.out.println("idregistroKit: "+idregistroKit);

		RegistroKit rg=this.manejadorInstalacionKit.verInstacionKit(Integer.parseInt(idregistroKit));
		
//		OrdenServicio ordServ=this.manejadorServicios.getOrdenServicioIK(rg.getIdordserv()); 
//		MOdificado
		OrdenServicio ordServ=this.manejadorServicios.getOrdenServicioIK(rg.getIdregistroKit());
		Solicitud solt=this.manejadorSolicitudes.getSoltByOrdServ(ordServ.getIdordserv());
		ordServ.setSolicitud(solt);
		List<InstalacionCilindro> InsCil=this.manejadorInstalacionKit.ListaCilindro(rg.getIdregistroKit());
		rg.setOrdenServicio(ordServ);
		rg.setCilindros(InsCil);
		lista.add(rg);
		//lista.add(this.manejadorServicios.ListaCilindros());
		return new ResponseEntity<List<?>>(lista,HttpStatus.OK);
	}
	
	@Autowired
	DataSource dataSource;
	@RequestMapping("Imprimir")
	public  void Imprimir(HttpServletResponse res,HttpServletRequest req){
		URIS uris=new URIS();
		Persona us=(Persona)req.getSession(true).getAttribute("xusuario");
		String Tramitador=us.getRazonsocial().toUpperCase();
		String id=req.getParameter("idregistrokit");
		System.out.println("idregistroKit: "+id);
		String logo=uris.imgJasperReport+"LOGOTIPOGNVFINAL.png";
		String nombreReporte="INSTALACIoN KIT",tipo="pdf", estado="inline";
		      
		Map<String, Object> parametros=new HashMap<String, Object>();
		                        
		String url=uris.jasperReport+"getInstalacionConversion.jasper"; 	
	                                
		parametros.put("idregistrokit_param",Integer.parseInt(id));
		parametros.put("tramitador_param",Tramitador);
		parametros.put("logo_param",this.getClass().getResourceAsStream(logo));

		GeneradorReportes g=new GeneradorReportes();
		try{
			g.generarReporte(res, getClass().getResource(url), tipo, parametros, dataSource.getConnection(), nombreReporte, estado);	
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
}
