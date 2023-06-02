package app.restController;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;



import app.entity.CarguioEntity;

import app.manager.ManejadorCarguios;

import app.service.CarguioService;
import app.service.EstacionService;
import app.utilidades.CargarArchivos;
import app.utilidades.LectorCSV;
import app.utilidades.URIS;

@RestController
@RequestMapping("/RestCarguio")
public class RestCarguio {
	@Autowired
	private CarguioService carguioService;
	@Autowired
	private EstacionService estacionService;
	@Autowired ManejadorCarguios manejadorCarguios;

	
	/*
	@GetMapping("")
    public ResponseEntity<?> getAll(@Param("search")String search,@Param("status")int status) { 
        try {

    		List<Map<String, Object>> lista=this.carguioService.listar(search,status);
    		System.out.println("listaA: "+lista.toString());
    		for (int i = 0; i < lista.size(); i++) {

    			
    			int mesnum=(int)(double)lista.get(i).get("mesnum");
    			int anio=(int)(double)lista.get(i).get("anio");

    			Map<String, Object> mapa=this.carguioService.FONDO_ROTATORIO(mesnum,anio);
    			
    			
    			Double volumen=(double)mapa.get("volumen");
    			System.out.println("volumen:"+volumen);
    			lista.get(i).put("volumen",volumen);
    			lista.get(i).put("volumen",100);
    			Double fondorotatorio=(double)mapa.get("fondorotatorio");
    			System.out.println("fondorotatorio:"+fondorotatorio);
    			lista.get(i).put("fondorotatorio",fondorotatorio);
//    			lista.get(i).put("fondorotatorio",mapa.get("fondorotatorio"));
    		}
            return ResponseEntity.status(HttpStatus.OK).body(lista);
        } catch (Exception e) {
        	e.printStackTrace();
        	System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"error\":\"Error. Por favor intente más tarde.\"}");
        }
    }
	*/
	@GetMapping("")
    public ResponseEntity<?> getAll(@Param("anio")int anio) {
//		public ResponseEntity<?> getAll(@Param("search")String search,@Param("status")int status) { 
        try {
        	List<Object> listaAll=new ArrayList<>();
        	List<Map<String, Object>> lista1,lista2=null;
//        	lista1=this.carguioService.listarAnioEG(anio);
        	lista1=this.carguioService.get_eess_active_by_anio(anio);
        	lista2=this.carguioService.detalle_carguio_eess_general_by_anio(anio);
    		System.out.println("lista1: "+lista1.toString());
    		listaAll.add(lista1);
    		listaAll.add(lista2);
            return ResponseEntity.status(HttpStatus.OK).body(listaAll);
        } catch (Exception e) {
        	e.printStackTrace();
        	System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"error\":\"Error. Por favor intente más tarde.\"}");
        }
    }
	/*
	@GetMapping("")
    public ResponseEntity<?> getAllBCK(@Param("anio")int anio) { 
//		public ResponseEntity<?> getAll(@Param("search")String search,@Param("status")int status) { 
        try {
        	Map<String, Object> Inforeessbyanio=this.carguioService.Inforeessbyanio(anio);
        	int id_estacion;
        	System.out.println("Inforeessbyanio:"+Inforeessbyanio.get("idest"));
        	if(Inforeessbyanio.get("idest")==null)id_estacion=-1;
        	else  id_estacion=Integer.parseInt(String.valueOf(Inforeessbyanio.get("idest")) );
        	
        	System.out.println("idest:"+id_estacion);
        	List<Map<String, Object>> lista=null;
        	if(id_estacion==-1)lista=this.carguioService.listarAnioG(anio);
        	else lista=this.carguioService.listarAnioE(anio);
    		System.out.println("listaA: "+lista.toString());

            return ResponseEntity.status(HttpStatus.OK).body(lista);
        } catch (Exception e) {
        	e.printStackTrace();
        	System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"error\":\"Error. Por favor intente más tarde.\"}");
        }
    }
	
	@GetMapping("/carguioseess")
    public ResponseEntity<?> getAllEESS_bck2(@Param("search")String search,@Param("status")int status,@Param("anio")int anio,@Param("id")int id) {
        try {
    		List<Map<String, Object>> lista=this.carguioService.detalle_carguio_eess_by_id(search, status, anio, id);
    		System.out.println("listaA: "+lista.toString());

            return ResponseEntity.status(HttpStatus.OK).body(lista);
        } catch (Exception e) {
        	e.printStackTrace();
        	System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"error\":\"Error. Por favor intente más tarde.\"}");
        }
    }
*/
	@GetMapping("/carguioseess")
    public ResponseEntity<?> getAllEESS(@Param("search")String search,@Param("status")int status,@Param("anio")int anio,@Param("id")int id) {
        try {
        	List<Object> listaAll=new ArrayList<>();
        	List<Map<String, Object>> lista=null;
        	List<Map<String, Object>> listadet=null;
        	System.out.println("id__:"+id);
        	if(id==-1) {
        		lista=this.carguioService.detalle_carguio_eess_by_id_g(search, status, anio,id);
        	}
        	else {
        		lista=this.carguioService.detalle_carguio_eess_by_id_e(search, status, anio,id);
        	}
    		System.out.println("listaA: "+lista.toString());
    		listadet=this.carguioService.listarAnioEESS(anio,id);
    		listaAll.add(lista);
    		listaAll.add(listadet);
            return ResponseEntity.status(HttpStatus.OK).body(listaAll);
        } catch (Exception e) {
        	e.printStackTrace();
        	System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"error\":\"Error. Por favor intente más tarde.\"}");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOne(@PathVariable Integer id){
        try {
            return ResponseEntity.status(HttpStatus.OK).body(carguioService.findById(id));
        } catch (Exception e) {
        	
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"error\":\"Error. Por favor intente más tarde.\"}");
        }
    }

    @PostMapping("add")
    public ResponseEntity<?> save(@RequestBody CarguioEntity entity){
        try {
            return ResponseEntity.status(HttpStatus.OK).body(carguioService.save(entity));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\":\"Error. Por favor intente más tarde.\"}");
        }
    }
    
	@Transactional
	@RequestMapping(value="/adicionar", method = RequestMethod.POST,produces = "application/json")
//	@PostMapping("/adicionar")
//	public ResponseEntity<?> adicionar(@ModelAttribute CargarArchivo objetoArchivo,HttpServletRequest req)throws FileNotFoundException, IOException{
//	public Map<String, Object> adicionar (@RequestBody CargarArchivo objetoArchivo,HttpServletRequest req)throws FileNotFoundException, IOException{
		public  Map<String, Object> fileUpload(@RequestPart(value="archivo")    MultipartFile archivo, @RequestParam(value="descripcion") String descripcion,HttpServletRequest req){ 
		System.out.println("entro a cargar file");
		
		URIS uris=new URIS(); 
		CargarArchivos cargarArchivos=new CargarArchivos();	
		LectorCSV lectorCSV=new LectorCSV();
//		HttpSession sesion=req.getSession(true);
//		Persona xuser=(Persona) sesion.getAttribute("xusuario");
//		MultipartFile multipartFile=((MultipartRequest)req).getFile("archivo");
//		MultipartFile multipartFile=objetoArchivo.getArchivo();
		MultipartFile multipartFile=archivo;
		Map<String, Object> respuesta=new HashMap<String, Object>();
		String nombreArchivo;
		System.out.println("file:"+multipartFile);
		System.out.println("fileSize:"+multipartFile.getSize());
		File csv=null;
		try{
			if(multipartFile!=null && multipartFile.getSize()>0) {
				nombreArchivo=multipartFile.getOriginalFilename();
				System.out.println("nombre: "+nombreArchivo);
				  
//				File archivo=multipartToFile(multipartFile);				
//				leerExcel(archivo.getAbsolutePath()); 
//				ca.EliminarArchivo(multipartFile);
				System.out.println("ruta_archivos GENERAL:"+uris.obtenerRutaCarpetaRecursos(""));
//				if ((uris.checkOS().contains("Linux"))) {
//					try {
//						System.out.println("DANDO PERMISOS A LA CARPETA DE ARCHIVOS");
//						Process p = Runtime.getRuntime().exec("chmod -R 777 "+uris.obtenerRutaCarpetaRecursos(""));
//						System.out.println("ruta_archivos linux :"+uris.obtenerRutaCarpetaRecursos(""));
//					} catch (Exception e) {
//						// TODO: handle exception
//						e.printStackTrace();
//						System.out.println(e.getMessage());
//					}
//
//					
//				}
				
				csv=cargarArchivos.saveReturnCSV(multipartFile,req);
				List<String[]> ArrayCSV=lectorCSV.leerCSVSimple(csv.getAbsolutePath());
//				System.out.println(); 
				Map<String,Object> resp=lectorCSV.convertCSVtoTxt(ArrayCSV, multipartFile.getOriginalFilename(),this.estacionService,this.carguioService,req);
//				LeerArchivoExcel(multipartFile.getOriginalFilename());
				System.out.println("RESP"+resp.toString());
				System.out.println("RESP"+resp.get("estado")); 

				System.out.println("SISTEMA OPERATIVO:"+uris.checkOS());
				if ((uris.checkOS().contains("Linux"))) {
					try {
						System.out.println("DANDO PERMISOS A LA CARPETA DE ARCHIVOS");
						Process p = Runtime.getRuntime().exec("chmod -R 777 "+uris.obtenerRutaCarpetaRecursos(""));
//						Process p1 = Runtime.getRuntime().exec("chown -R postgres:postgres "+uris.obtenerRutaCarpetaRecursos(""));
						System.out.println("ruta_archivos linux :"+uris.obtenerRutaCarpetaRecursos(""));
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
						System.out.println(e.getMessage());
					}

					
				}			

				if ((boolean) resp.get("estado")) {
					System.out.println("ENTRO_ADD");
					System.out.println("PATH ADD:"+resp.get("path")+"");
//						this.carguioService.guardar(resp.get("path")+"");
						manejadorCarguios.adicionar_pg(resp.get("path")+"");
						respuesta.put("estado", true);
				}else {
					cargarArchivos.EliminarArchivoTXT((File) resp.get("archivotxt"));
					respuesta.put("estado", false);
				}  
//				cargarArchivos.EliminarArchivoTXT((File) resp.get("archivotxt"));
			}else { 
				respuesta.put("estado",false);
			}
						
		}catch (Exception e){
			System.out.println("ERROR:"+e.toString());
			e.printStackTrace();
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			respuesta.put("estado",false);
//			return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.BAD_REQUEST);
		}
//		return  ResponseEntity.status(HttpStatus.OK).body(respuesta);
		return respuesta;
	} 

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Integer id,@RequestBody CarguioEntity entity) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(carguioService.update(id, entity));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\":\"Error. Por favor intente más tarde.\"}");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        try {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(carguioService.delete(id));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\":\"Error. Por favor intente más tarde.\"}");
        }
    }
	
    @PostMapping("updateStatus")
    public ResponseEntity<?> updateStatus(@RequestBody CarguioEntity entity) {
        try {
        	System.out.println("Entidad:"+entity.toString());
        	carguioService.updateStatus(entity.getEstado(), entity.getId());
        	CarguioEntity entity2=carguioService.findById(entity.getId());
            return ResponseEntity.status(HttpStatus.OK).body(entity2);
        } catch (Exception e) {
        	System.out.println(e.getMessage());
        	e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\":\"Error. Por favor intente más tarde.\"}");
        }
    }
    
}
