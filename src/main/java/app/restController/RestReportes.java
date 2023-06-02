package app.restController;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import app.manager.ManejadorServicios;
import app.manager.ManejadorSolicitudes;
import app.models.Solicitud;
import app.utilidades.GeneradorReportes;
import app.utilidades.OpcionesAdicionales;
//import app.utilidades.JasperRerportsSimpleConfig;
import app.utilidades.SimpleReportExporter;
import app.utilidades.SimpleReportFiller;
import app.utilidades.URIS;

@RequestMapping("/RestResports/")
@RestController
public class RestReportes {
	 
	@Autowired
	DataSource dataSource;
	@Autowired      
	ManejadorSolicitudes manejadorSolicitudes;
	@Autowired
	ManejadorServicios manejadorServicios;

	
	@RequestMapping(value="datos")
	public ResponseEntity<List<?>> listarBneneficiarios(HttpServletRequest req,HttpServletResponse res){	
		List<Object> datos=new ArrayList<Object>();
		List<Map<String, Object>> reportes=this.manejadorSolicitudes.ListarReportes();
		List<Map<String, Object>> estaciones=this.manejadorSolicitudes.ListarEstaciones();
		List<Map<String, Object>> placas=this.manejadorSolicitudes.ListarPlacas();
		datos.add(reportes);
		datos.add(estaciones);
		datos.add(placas);
		System.out.println("listaREPORT: "+reportes.toString());
		return new ResponseEntity<List<?>>(datos,HttpStatus.OK);
	} 
	@RequestMapping(value="listar")
	public void  reporte (HttpServletResponse res) {
		SimpleReportFiller simpleReportFiller=new SimpleReportFiller();

		URIS uris=new URIS();
		String url=uris.jasperReport+"getListBeneficiario.jasper"; 
		String escudo=uris.imgJasperReport+"escudobolivia.png";   
//		try {
//			simpleReportFiller.setReporte(getClass().getResource(url));
//		} catch (Exception e) {
//			// TODO: handle exception
//			e.getLocalizedMessage();
//		}
//	    
//	    
	    Map<String, Object> parameters = new HashMap<>();
	    
		
//	    parameters.put("nit_param",oa.getNit());
	    parameters.put("fInicial_param","20181015");
	    parameters.put("fFinal_param","20181022");
	    parameters.put("escudo_param",this.getClass().getResourceAsStream(escudo));
//	    simpleReportFiller.setParameters(parameters);
//	    
//	    simpleReportFiller.setDataSource(dataSource);
//	     
//	    simpleReportFiller.fillReport();
//	    
//	    SimpleReportExporter simpleExporter = new SimpleReportExporter();
//        simpleExporter.setJasperPrint(simpleReportFiller.getJasperPrint());
//        
//        simpleExporter.exportToXlsx("employeeReport.xlsx", "Employee Data");
//        simpleExporter.exportToPdf("employeeReport.pdf", "baeldung");
//        
		GeneradorReportes g=new GeneradorReportes();
		try{
			
//			g.generarReporte(res, getClass().getResource(url), "xls", parameters, dataSource.getConnection(), "MinameReport", "inline");	
			g.generarReporte(res, getClass().getResource(url), "pdf", parameters, dataSource.getConnection(), "MinameReport", "inline");	

		    
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@RequestMapping(value="LRSolicitudF")
	public void  LRSolicitudF (HttpServletRequest req,HttpServletResponse res) {
		SimpleReportFiller simpleReportFiller=new SimpleReportFiller();
		OpcionesAdicionales oa=new OpcionesAdicionales();
		URIS uris=new URIS();
		String url=uris.jasperReport+"getRListSolicitudesF.jasper"; 
		String escudo=uris.imgJasperReport+"escudobolivia.png";  
	    Map<String, Object> parameters = new HashMap<>();
	    
	    String tipo=req.getParameter("tipo");
	    String fecha_inicial=req.getParameter("fecha_inicial");
	    String fecha_final=req.getParameter("fecha_final");
		System.out.println("tipo: "+tipo+" fecha_inicial:"+fecha_inicial+" fecha_final:"+fecha_final);	
	    
//		String fecha_inicialC=oa.convertFecha(fecha_inicial);
		java.sql.Date fecha_inicialC=oa.getDateSQL(fecha_inicial);
//		String fecha_finalC=oa.convertFecha(fecha_final);
		java.sql.Date fecha_finalC=oa.getDateSQL(fecha_final);
		System.out.println("fecha_inicialC:"+fecha_inicialC);
		System.out.println("fecha_finalC:"+fecha_finalC);
		
		Map<String, Object> nitSQL=this.manejadorServicios.nitEmpresa(1); 
		String nit_patam=(String) nitSQL.get("nitInst");
		
	    parameters.put("nit_param",nit_patam);
//	    parameters.put("fInicial_param","15/10/2018"); 
	    parameters.put("fInicial_param",fecha_inicialC);
//	    parameters.put("fFinal_param","22/10/2018");
	    parameters.put("fFinal_param",fecha_finalC);
	    parameters.put("escudo_param",this.getClass().getResourceAsStream(escudo));
//        
		GeneradorReportes g=new GeneradorReportes(); 
		try{
			if(tipo.equals("pdf")) {
				System.out.println("entro");
				g.generarReporte(res, getClass().getResource(url), "pdf", parameters, dataSource.getConnection(), "MinameReport", "inline");	
			}else {
				g.generarReporte(res, getClass().getResource(url), "xls", parameters, dataSource.getConnection(), "MinameReport", "inline");
			}
					
 
		    
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@RequestMapping(value="LRSolicitudesAnuladas")
	public void  LRSolicitudesAnuladas (HttpServletRequest req,HttpServletResponse res) {
		OpcionesAdicionales oa=new OpcionesAdicionales();
		URIS uris=new URIS();
		String url=uris.jasperReport+"getRListSolicitudesAnuladas.jasper"; 
		String escudo=uris.imgJasperReport+"escudobolivia.png";  
	    Map<String, Object> parameters = new HashMap<>();
	    
	    String tipo=req.getParameter("tipo");

		
		Map<String, Object> nitSQL=this.manejadorServicios.nitEmpresa(1); 
		String nit_patam=(String) nitSQL.get("nitInst");
		
	    parameters.put("nit_param",nit_patam);
	    parameters.put("escudo_param",this.getClass().getResourceAsStream(escudo));

		GeneradorReportes g=new GeneradorReportes();
		try{
			if(tipo.equals("pdf")) {
				System.out.println("entro");
				g.generarReporte(res, getClass().getResource(url), "pdf", parameters, dataSource.getConnection(), "MinameReport", "inline");	
			}else {
				g.generarReporte(res, getClass().getResource(url), "xls", parameters, dataSource.getConnection(), "MinameReport", "inline");
			}
					

		    
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@RequestMapping(value="LROServicioF")
	public void  LROServicioF (HttpServletRequest req,HttpServletResponse res) {
		OpcionesAdicionales oa=new OpcionesAdicionales();
		URIS uris=new URIS();
		String url=uris.jasperReport+"getRListOrdenServicio.jasper"; 
		String escudo=uris.imgJasperReport+"escudobolivia.png";  
	    Map<String, Object> parameters = new HashMap<>();
	    
	    String tipo=req.getParameter("tipo");
	    String fecha_inicial=req.getParameter("fecha_inicial");
	    String fecha_final=req.getParameter("fecha_final");
		System.out.println("tipo: "+tipo+" fecha_inicial:"+fecha_inicial+" fecha_final:"+fecha_final);	
	    
		String fecha_inicialC=oa.convertFecha(fecha_inicial);
		String fecha_finalC=oa.convertFecha(fecha_final);
		System.out.println("fecha_inicialC:"+fecha_inicialC);
		
		Map<String, Object> nitSQL=this.manejadorServicios.nitEmpresa(1); 
		String nit_patam=(String) nitSQL.get("nitInst");
		
	    parameters.put("nit_param",nit_patam);
//	    parameters.put("fInicial_param","15/10/2018");
	    parameters.put("fInicial_param",fecha_inicialC);
//	    parameters.put("fFinal_param","22/10/2018");
	    parameters.put("fFinal_param",fecha_finalC);
	    parameters.put("escudo_param",this.getClass().getResourceAsStream(escudo));
//        
		GeneradorReportes g=new GeneradorReportes();
		try{
			if(tipo.equals("pdf")) {
				System.out.println("entro");
				g.generarReporte(res, getClass().getResource(url), "pdf", parameters, dataSource.getConnection(), "MinameReport", "inline");	
			}else {
				g.generarReporte(res, getClass().getResource(url), "xls", parameters, dataSource.getConnection(), "MinameReport", "inline");
			}
					

		    
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@RequestMapping(value="LRVehiculos")
	public void  LRVehiculos (HttpServletRequest req,HttpServletResponse res) {
		OpcionesAdicionales oa=new OpcionesAdicionales();
		URIS uris=new URIS();
		String url=uris.jasperReport+"getRListVehiculos.jasper"; 
		String escudo=uris.imgJasperReport+"escudobolivia.png";  
	    Map<String, Object> parameters = new HashMap<>();
	    
	    String tipo=req.getParameter("tipo");

		
		Map<String, Object> nitSQL=this.manejadorServicios.nitEmpresa(1); 
		String nit_patam=(String) nitSQL.get("nitInst");
		
	    parameters.put("nit_param",nit_patam);
	    parameters.put("escudo_param",this.getClass().getResourceAsStream(escudo));

		GeneradorReportes g=new GeneradorReportes();
		try{
			if(tipo.equals("pdf")) {
				System.out.println("entro");
				g.generarReporte(res, getClass().getResource(url), "pdf", parameters, dataSource.getConnection(), "MinameReport", "inline");	
			}else {
				g.generarReporte(res, getClass().getResource(url), "xls", parameters, dataSource.getConnection(), "MinameReport", "inline");
			}
					

		    
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@RequestMapping(value="LRBeneficiariosSinServicio")
	public void  LRBeneficiariosSinServicio (HttpServletRequest req,HttpServletResponse res) {
		OpcionesAdicionales oa=new OpcionesAdicionales();
		URIS uris=new URIS();
		String url=uris.jasperReport+"getRListBeneficiariosSinSolt.jasper"; 
		String escudo=uris.imgJasperReport+"escudobolivia.png";  
	    Map<String, Object> parameters = new HashMap<>();
	    
	    String tipo=req.getParameter("tipo");

		
		Map<String, Object> nitSQL=this.manejadorServicios.nitEmpresa(1); 
		String nit_patam=(String) nitSQL.get("nitInst");
		
	    parameters.put("nit_param",nit_patam);
	    parameters.put("escudo_param",this.getClass().getResourceAsStream(escudo));

		GeneradorReportes g=new GeneradorReportes();
		try{
			if(tipo.equals("pdf")) {
				System.out.println("entro");
				g.generarReporte(res, getClass().getResource(url), "pdf", parameters, dataSource.getConnection(), "MinameReport", "inline");	
			}else {
				g.generarReporte(res, getClass().getResource(url), "xls", parameters, dataSource.getConnection(), "MinameReport", "inline");
			}
					

		    
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@RequestMapping(value="LRBeneficiariosIEnSolt")
	public void  getRListBeneficiariosIEnSolt (HttpServletRequest req,HttpServletResponse res) {
		OpcionesAdicionales oa=new OpcionesAdicionales();
		URIS uris=new URIS();
		String url=uris.jasperReport+"getRListBeneficiariosIEnSolt.jasper"; 
		String escudo=uris.imgJasperReport+"escudobolivia.png";  
	    Map<String, Object> parameters = new HashMap<>();
	    
	    String tipo=req.getParameter("tipo");

		
		Map<String, Object> nitSQL=this.manejadorServicios.nitEmpresa(1); 
		String nit_patam=(String) nitSQL.get("nitInst");
		
	    parameters.put("nit_param",nit_patam);
	    parameters.put("escudo_param",this.getClass().getResourceAsStream(escudo));

		GeneradorReportes g=new GeneradorReportes();
		try{
			if(tipo.equals("pdf")) {
				System.out.println("entro");
				g.generarReporte(res, getClass().getResource(url), "pdf", parameters, dataSource.getConnection(), "MinameReport", "inline");	
			}else {
				g.generarReporte(res, getClass().getResource(url), "xls", parameters, dataSource.getConnection(), "MinameReport", "inline");
			}
					

		    
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@RequestMapping(value="LRBeneficiariosAEnSolt")
	public void  getRListBeneficiariosAEnSolt (HttpServletRequest req,HttpServletResponse res) {
		OpcionesAdicionales oa=new OpcionesAdicionales();
		URIS uris=new URIS();
		String url=uris.jasperReport+"getRListBeneficiariosAEnSolt.jasper"; 
		String escudo=uris.imgJasperReport+"escudobolivia.png";  
	    Map<String, Object> parameters = new HashMap<>();
	    
	    String tipo=req.getParameter("tipo");

		
		Map<String, Object> nitSQL=this.manejadorServicios.nitEmpresa(1); 
		String nit_patam=(String) nitSQL.get("nitInst");
		
	    parameters.put("nit_param",nit_patam);
	    parameters.put("escudo_param",this.getClass().getResourceAsStream(escudo));

		GeneradorReportes g=new GeneradorReportes();
		try{
			if(tipo.equals("pdf")) {
				System.out.println("entro");
				g.generarReporte(res, getClass().getResource(url), "pdf", parameters, dataSource.getConnection(), "MinameReport", "inline");	
			}else {
				g.generarReporte(res, getClass().getResource(url), "xls", parameters, dataSource.getConnection(), "MinameReport", "inline");
			}
					

		     
		} catch (Exception e) {
			e.printStackTrace();
		}   
	} 
	@RequestMapping(value="LRInstalacionKitF")
	public void  LRInstalacionKitF (HttpServletRequest req,HttpServletResponse res) {
		OpcionesAdicionales oa=new OpcionesAdicionales();
		URIS uris=new URIS();
		String url=uris.jasperReport+"getRListInstalacionKit.jasper"; 
		String escudo=uris.imgJasperReport+"escudobolivia.png";  
	    Map<String, Object> parameters = new HashMap<>();
	    
	    String tipo=req.getParameter("tipo");
	    String fecha_inicial=req.getParameter("fecha_inicial");
	    String fecha_final=req.getParameter("fecha_final");
		System.out.println("tipo: "+tipo+" fecha_inicial:"+fecha_inicial+" fecha_final:"+fecha_final);	
	    
		String fecha_inicialC=oa.convertFecha(fecha_inicial);
		String fecha_finalC=oa.convertFecha(fecha_final);
		System.out.println("fecha_inicialC:"+fecha_inicialC);
		
		Map<String, Object> nitSQL=this.manejadorServicios.nitEmpresa(1); 
		String nit_patam=(String) nitSQL.get("nitInst");
		
	    parameters.put("nit_param",nit_patam);
//	    parameters.put("fInicial_param","15/10/2018");
	    parameters.put("fInicial_param",fecha_inicialC);
//	    parameters.put("fFinal_param","22/10/2018");
	    parameters.put("fFinal_param",fecha_finalC);
	    parameters.put("escudo_param",this.getClass().getResourceAsStream(escudo));
//        
		GeneradorReportes g=new GeneradorReportes();
		try{
			if(tipo.equals("pdf")) {
				System.out.println("entro");
				g.generarReporte(res, getClass().getResource(url), "pdf", parameters, dataSource.getConnection(), "MinameReport", "inline");	
			}else {
				g.generarReporte(res, getClass().getResource(url), "xls", parameters, dataSource.getConnection(), "MinameReport", "inline");
			}
					

		    
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@RequestMapping(value="LRKitsPagadosF")
	public void  LRKitsPagadosF (HttpServletRequest req,HttpServletResponse res) {
		OpcionesAdicionales oa=new OpcionesAdicionales();
		URIS uris=new URIS();
		String url=uris.jasperReport+"getRListKitsPagados.jasper"; 
		String escudo=uris.imgJasperReport+"escudobolivia.png";  
	    Map<String, Object> parameters = new HashMap<>();
	    
	    String tipo=req.getParameter("tipo");
	    String fecha_inicial=req.getParameter("fecha_inicial");
	    String fecha_final=req.getParameter("fecha_final");
		System.out.println("tipo: "+tipo+" fecha_inicial:"+fecha_inicial+" fecha_final:"+fecha_final);	
	    
		String fecha_inicialC=oa.convertFecha(fecha_inicial);
		String fecha_finalC=oa.convertFecha(fecha_final);
		System.out.println("fecha_inicialC:"+fecha_inicialC);
		
		Map<String, Object> nitSQL=this.manejadorServicios.nitEmpresa(1); 
		String nit_patam=(String) nitSQL.get("nitInst");
		
	    parameters.put("nit_param",nit_patam);
//	    parameters.put("fInicial_param","15/10/2018");
	    parameters.put("fInicial_param",fecha_inicialC);
//	    parameters.put("fFinal_param","22/10/2018");
	    parameters.put("fFinal_param",fecha_finalC);
	    parameters.put("escudo_param",this.getClass().getResourceAsStream(escudo));
//        
		GeneradorReportes g=new GeneradorReportes();
		try{
			if(tipo.equals("pdf")) {
				System.out.println("entro");
				g.generarReporte(res, getClass().getResource(url), "pdf", parameters, dataSource.getConnection(), "MinameReport", "inline");	
			}else {
				g.generarReporte(res, getClass().getResource(url), "xls", parameters, dataSource.getConnection(), "MinameReport", "inline");
			}
					

		    
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	 
	//CARGUIOSSSS
	
	@RequestMapping(value="LRCarguiosGeneralesMensual")
	public void  LRCarguiosGeneralesMensual (HttpServletRequest req,HttpServletResponse res) {
		OpcionesAdicionales oa=new OpcionesAdicionales();
		URIS uris=new URIS();
		String url=uris.jasperReport+"getRListCarguios.jasper"; 
		String escudo=uris.imgJasperReport+"escudobolivia.png";  
	    Map<String, Object> parameters = new HashMap<>();
	    
	    String tipo=req.getParameter("tipo");

		
		Map<String, Object> nitSQL=this.manejadorServicios.nitEmpresa(1); 
		String nit_patam=(String) nitSQL.get("nitInst");
		
	    int idest=Integer.parseInt(req.getParameter("idest"));
	    String mes=req.getParameter("mes");
	    String anio=req.getParameter("anio");
		
	    parameters.put("nit_param",nit_patam);
	    parameters.put("escudo_param",this.getClass().getResourceAsStream(escudo));
	    parameters.put("idest_param",idest);
	    parameters.put("mes_param",mes);
	    parameters.put("anio_param",anio);


	    
		GeneradorReportes g=new GeneradorReportes();
		try{
			if(tipo.equals("pdf")) {
				System.out.println("entro");
				g.generarReporte(res, getClass().getResource(url), "pdf", parameters, dataSource.getConnection(), "MinameReport", "inline");	
			}else {
				g.generarReporte(res, getClass().getResource(url), "xls", parameters, dataSource.getConnection(), "MinameReport", "inline");
			}
					

		    
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@RequestMapping(value="LRCarguiosMensualesPlacaVehiculo")
	public void  LRCarguiosMensualesPlacaVehiculo (HttpServletRequest req,HttpServletResponse res) {
		OpcionesAdicionales oa=new OpcionesAdicionales();
		URIS uris=new URIS();
		String url=uris.jasperReport+"getRListCarguiosMensualesPlaca.jasper"; 
		String escudo=uris.imgJasperReport+"escudobolivia.png";  
	    Map<String, Object> parameters = new HashMap<>();
	    
	    String tipo=req.getParameter("tipo");

		
		Map<String, Object> nitSQL=this.manejadorServicios.nitEmpresa(1); 
		String nit_patam=(String) nitSQL.get("nitInst");
		
	    int idest=Integer.parseInt(req.getParameter("idest"));
	    String mes=req.getParameter("mes");
	    String anio=req.getParameter("anio");
	    String placa=req.getParameter("placa");
		
	    parameters.put("nit_param",nit_patam);
	    parameters.put("escudo_param",this.getClass().getResourceAsStream(escudo));
	    parameters.put("idest_param",idest);
	    parameters.put("mes_param",mes);
	    parameters.put("anio_param",anio);
	    parameters.put("placa_param",placa);


	    
		GeneradorReportes g=new GeneradorReportes();
		try{
			if(tipo.equals("pdf")) {
				System.out.println("entro");
				g.generarReporte(res, getClass().getResource(url), "pdf", parameters, dataSource.getConnection(), "MinameReport", "inline");	
			}else {
				g.generarReporte(res, getClass().getResource(url), "xls", parameters, dataSource.getConnection(), "MinameReport", "inline");
			}
					

		    
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
//	@RequestMapping(value="LRCarguiosEstacionF")//YA NO SE USARA POR QUE SE AUMENTO FILTRO POR PLACAS-- ver siguiente controlador
//	public void  LRCarguiosEstacionF(HttpServletRequest req,HttpServletResponse res) {
//		OpcionesAdicionales oa=new OpcionesAdicionales();
//		URIS uris=new URIS();
//		String url=uris.jasperReport+"getRListCarguiosEstacion.jasper"; 
//		String escudo=uris.imgJasperReport+"escudobolivia.png";  
//	    Map<String, Object> parameters = new HashMap<>();
//	    
//	    String tipo=req.getParameter("tipo");
//	    String fecha_inicial=req.getParameter("fecha_inicial");
//	    String fecha_final=req.getParameter("fecha_final");
//	    int idest=Integer.parseInt(req.getParameter("idest"));
//		System.out.println("tipo: "+tipo+" fecha_inicial:"+fecha_inicial+" fecha_final:"+fecha_final);	
//	      
//		String fecha_inicialC=oa.convertFecha(fecha_inicial);
//		String fecha_finalC=oa.convertFecha(fecha_final);
//		System.out.println("fecha_inicialC:"+fecha_inicialC);
//		System.out.println("fecha_inicialC:"+fecha_finalC);
////		Date fecha_inicialC=oa.convertirStringDate(fecha_inicial, "yyyy-mm-dd");
////		Date fecha_finalC=oa.convertirStringDate(fcecha_final, "yyyy-mm-dd");
//		Map<String, Object> nitSQL=this.manejadorServicios.nitEmpresa(1); 
//		String nit_patam=(String) nitSQL.get("nitInst");
//		
//		System.out.println("escudo:"+escudo);
//		
//	    parameters.put("nit_param",nit_patam);
////	    parameters.put("fInicial_param","15/10/2018");
//	    parameters.put("fInicial_param",fecha_inicialC);
////	    parameters.put("fFinal_param","22/10/2018");
//	    parameters.put("fFinal_param",fecha_finalC);
//	    parameters.put("escudo_param",this.getClass().getResourceAsStream(escudo));
//	    parameters.put("idest_param",idest);
////        
//		GeneradorReportes g=new GeneradorReportes();
//		try{
//			if(tipo.equals("pdf")) {
//				System.out.println("entro");
//				g.generarReporte(res, getClass().getResource(url), "pdf", parameters, dataSource.getConnection(), "MinameReport", "inline");	
//			}else {
//				g.generarReporte(res, getClass().getResource(url), "xls", parameters, dataSource.getConnection(), "MinameReport", "inline");
//			}
//					
//
//		    
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
	
	//CARGUIOS DIARIOS
	@RequestMapping(value="LRCarguiosEstacionPlacaF") 
	public void  LRCarguiosEstacionPlacaF(HttpServletRequest req,HttpServletResponse res) {
		OpcionesAdicionales oa=new OpcionesAdicionales();
		URIS uris=new URIS();
		String url=uris.jasperReport+"getRListCarguiosEstacionPlaca.jasper"; 
		String escudo=uris.imgJasperReport+"escudobolivia.png";  
	    Map<String, Object> parameters = new HashMap<>();
	    
	    String tipo=req.getParameter("tipo");
	    String fecha_inicial=req.getParameter("fecha_inicial");
	    String fecha_final=req.getParameter("fecha_final");
	    int idest=Integer.parseInt(req.getParameter("idest"));
	    String placa=req.getParameter("placa");
	    if(placa==null) {
	    	placa="";
	    }
		System.out.println("tipo: "+tipo+" fecha_inicial:"+fecha_inicial+" fecha_final:"+fecha_final);	
	      
		String fecha_inicialC=oa.convertFecha(fecha_inicial);
		String fecha_finalC=oa.convertFecha(fecha_final);
		System.out.println("fecha_inicialC:"+fecha_inicialC);
		System.out.println("fecha_inicialC:"+fecha_finalC);
//		Date fecha_inicialC=oa.convertirStringDate(fecha_inicial, "yyyy-mm-dd");
//		Date fecha_finalC=oa.convertirStringDate(fcecha_final, "yyyy-mm-dd");
		Map<String, Object> nitSQL=this.manejadorServicios.nitEmpresa(1); 
		String nit_patam=(String) nitSQL.get("nitInst");
		
		System.out.println("escudo:"+escudo);
		
	    parameters.put("nit_param",nit_patam);
//	    parameters.put("fInicial_param","15/10/2018");
	    parameters.put("fInicial_param",fecha_inicialC);
//	    parameters.put("fFinal_param","22/10/2018");
	    parameters.put("fFinal_param",fecha_finalC);
	    parameters.put("escudo_param",this.getClass().getResourceAsStream(escudo));
	    parameters.put("idest_param",idest);
	    parameters.put("placa_param",placa);
//          
		GeneradorReportes g=new GeneradorReportes();  
		try{
			if(tipo.equals("pdf")) {
				System.out.println("entro");
				g.generarReporte(res, getClass().getResource(url), "pdf", parameters, dataSource.getConnection(), "MinameReport", "inline");	
			}else {
				g.generarReporte(res, getClass().getResource(url), "xls", parameters, dataSource.getConnection(), "MinameReport", "inline");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
//	CARGUIOS ANUALES
	@RequestMapping(value="LRCarguiosAnualesEESS")
	public void  getRListCarguiosAnualesEESS (HttpServletRequest req,HttpServletResponse res) {
		OpcionesAdicionales oa=new OpcionesAdicionales();
		URIS uris=new URIS();
		String url=uris.jasperReport+"getRListCarguiosAnualesEESS.jasper"; 
		String escudo=uris.imgJasperReport+"escudobolivia.png";  
	    Map<String, Object> parameters = new HashMap<>();
	    
	    String tipo=req.getParameter("tipo");

		
		Map<String, Object> nitSQL=this.manejadorServicios.nitEmpresa(1); 
		String nit_patam=(String) nitSQL.get("nitInst");
		
	    int idest=Integer.parseInt(req.getParameter("idest"));
	    String anio=req.getParameter("anio");
		System.out.println("idest:"+idest);
		System.out.println("anio:"+anio);
	    parameters.put("nit_param",nit_patam);
	    parameters.put("escudo_param",this.getClass().getResourceAsStream(escudo));
	    parameters.put("idest_param",idest);
	    parameters.put("anio_param",anio);

  
	    
		GeneradorReportes g=new GeneradorReportes();
		try{
			if(tipo.equals("pdf")) {
				System.out.println("entro_an");
				g.generarReporte(res, getClass().getResource(url), "pdf", parameters, dataSource.getConnection(), "MinameReport", "inline");	
			}else {
				g.generarReporte(res, getClass().getResource(url), "xls", parameters, dataSource.getConnection(), "MinameReport", "inline");
			}
					

		    
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
	}
	@RequestMapping(value="LRCarguiosAnualesEESSPlaca")
	public void  getRListCarguiosAnualesEESSPlaca (HttpServletRequest req,HttpServletResponse res) {
		OpcionesAdicionales oa=new OpcionesAdicionales();
		URIS uris=new URIS();
		String url=uris.jasperReport+"getRListCarguiosAnualesEESSPlaca.jasper"; 
		String escudo=uris.imgJasperReport+"escudobolivia.png";  
	    Map<String, Object> parameters = new HashMap<>();
	    
	    String tipo=req.getParameter("tipo");

		
		Map<String, Object> nitSQL=this.manejadorServicios.nitEmpresa(1); 
		String nit_patam=(String) nitSQL.get("nitInst");
		
	    int idest=Integer.parseInt(req.getParameter("idest"));
	    String anio=req.getParameter("anio");
	    String placa=req.getParameter("placa");
		System.out.println("idest:"+idest);
		System.out.println("anio:"+anio);
	    parameters.put("nit_param",nit_patam);
	    parameters.put("escudo_param",this.getClass().getResourceAsStream(escudo));
	    parameters.put("idest_param",idest);
	    parameters.put("anio_param",anio);
	    parameters.put("placa_param",placa);

  
	    
		GeneradorReportes g=new GeneradorReportes();
		try{
			if(tipo.equals("pdf")) {
				System.out.println("entro_an");
				g.generarReporte(res, getClass().getResource(url), "pdf", parameters, dataSource.getConnection(), "MinameReport", "inline");	
			}else {
				g.generarReporte(res, getClass().getResource(url), "xls", parameters, dataSource.getConnection(), "MinameReport", "inline");
			}
					

		    
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
	}
	@RequestMapping(value="LRCarguiosAnualesPlacaG")
	public void  getRListCarguiosAnualesPlacaG (HttpServletRequest req,HttpServletResponse res) {
		OpcionesAdicionales oa=new OpcionesAdicionales();
		URIS uris=new URIS();
		String url=uris.jasperReport+"getRListCarguiosAnualesPlacaG.jasper"; 
		String escudo=uris.imgJasperReport+"escudobolivia.png";  
	    Map<String, Object> parameters = new HashMap<>();
	    
	    String tipo=req.getParameter("tipo");

		
		Map<String, Object> nitSQL=this.manejadorServicios.nitEmpresa(1); 
		String nit_patam=(String) nitSQL.get("nitInst");
		
	    String anio=req.getParameter("anio");
	    String placa=req.getParameter("placa");
		System.out.println("anio:"+anio);
	    parameters.put("nit_param",nit_patam);
	    parameters.put("escudo_param",this.getClass().getResourceAsStream(escudo));
	    parameters.put("anio_param",anio);
	    parameters.put("placa_param",placa);
	    
		GeneradorReportes g=new GeneradorReportes();
		try{
			if(tipo.equals("pdf")) {
				System.out.println("entro_an");
				g.generarReporte(res, getClass().getResource(url), "pdf", parameters, dataSource.getConnection(), "MinameReport", "inline");	
			}else {
				g.generarReporte(res, getClass().getResource(url), "xls", parameters, dataSource.getConnection(), "MinameReport", "inline");
			}
					

		    
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
	}
}
