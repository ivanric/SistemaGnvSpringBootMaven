package app.restController;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import app.manager.ManejadorCarguios;
import app.service.CarguioService;
import app.service.EstacionService;
import app.utilidades.CargarArchivos;
import app.utilidades.LectorCSV;
import app.utilidades.URIS;





@RequestMapping("/RestCarguios/")
@RestController
public class RestCarguios {
	@Autowired
	 ManejadorCarguios manejadorCarguios;
	
	@Autowired  EstacionService estacionService;
	@Autowired CarguioService carguioService;
	
	public URIS uris=new URIS();
	
	@RequestMapping(value="listar")
	public ResponseEntity<?> listarServicio(HttpServletRequest req,HttpServletResponse res){	
		List<Map<String, Object>> lista=this.manejadorCarguios.Listar(req);
		System.out.println("listaA: "+lista.toString());
		for (int i = 0; i < lista.size(); i++) {
			String Anio=lista.get(i).get("Anio")+"";
			String Mes=lista.get(i).get("MesNum")+"";
			System.out.println("Anio:"+Anio+" MEs:"+Mes);
			Map<String, Object> mapa=this.manejadorCarguios.RETUNR_FondoRot(Anio,Mes);
//			System.out.println("mapa:"+mapa.toString());
			lista.get(i).put("Volumen",mapa.get("Volumen"));
			lista.get(i).put("FondoRotatorio",mapa.get("FondoRotatorio"));
		}
		System.out.println("lista: "+lista.toString());
		return new ResponseEntity<List<?>>(lista,HttpStatus.OK);
	} 
	@Transactional
	@RequestMapping(value="adicionar", method = RequestMethod.POST, consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
//	public Map<String, Object> adicionar(HttpServletRequest req,Persona p,HttpServletResponse res,MultipartFile archivo){
	public Map<String, Object> adicionar(@RequestParam("archivo")MultipartFile multipartFile, HttpServletRequest req,HttpServletResponse res){
		System.out.println("entro a cargar file");
		CargarArchivos ca=new CargarArchivos();	
		LectorCSV lectorCSV=new LectorCSV();
//		HttpSession sesion=req.getSession(true);
//		Persona xuser=(Persona) sesion.getAttribute("xusuario");
//		MultipartFile multipartFile=((MultipartRequest)req).getFile("archivo");
		Map<String, Object> respuesta=new HashMap<String, Object>();
		String nombreArchivo; 
		try{
			if(multipartFile!=null && multipartFile.getSize()>0) {
				nombreArchivo=multipartFile.getOriginalFilename();
				System.out.println("nombre: "+nombreArchivo);
				  
//				File archivo=multipartToFile(multipartFile);				
//				leerExcel(archivo.getAbsolutePath()); 
//				ca.EliminarArchivo(multipartFile);
				
				File csv=saveReturnCSV(multipartFile,req);
				List<String[]> ArrayCSV=lectorCSV.leerCSVSimple(csv.getAbsolutePath());
//				System.out.println();
				Map<String,Object> resp=lectorCSV.convertCSVtoTxt(ArrayCSV, multipartFile.getOriginalFilename(),this.estacionService,this.carguioService,req);
//				LeerArchivoExcel(multipartFile.getOriginalFilename());
				System.out.println("RESP"+resp.toString());
				System.out.println("RESP"+resp.get("estado"));
				if ((boolean) resp.get("estado")) {
					System.out.println("ENTRO_ADD");
						this.manejadorCarguios.adicionar(resp.get("path")+"");
				}
			}
			
			respuesta.put("estado", true);
		}catch (Exception e){
			System.out.println("ERROR:"+e.toString());
			e.printStackTrace();
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			respuesta.put("estado",false);
		}
		return respuesta;
	}

	public File saveReturnCSV(MultipartFile multipart,HttpServletRequest req) throws IllegalStateException, IOException {
//		 String ruta=uris.direccionCsvs;
		 String ruta=uris.GetDirecccionCsvs(req);
		 File convFile = new File(ruta+multipart.getOriginalFilename());
		 FileOutputStream fos = new FileOutputStream(convFile); 
		 fos.write(multipart.getBytes());
		 fos.close(); 
	    return convFile;
	}
	public File multipartToFile(MultipartFile multipart,HttpServletRequest req) throws IllegalStateException, IOException {
//		 String ruta=".//src//main//resources//static//archivos//excels//";
//		 String ruta=uris.direccionExcels;
		 String ruta=uris.GetDirecccionExcels(req);
		 File convFile = new File(ruta+multipart.getOriginalFilename());
		 FileOutputStream fos = new FileOutputStream(convFile); 
		 fos.write(multipart.getBytes());
		 fos.close(); 
	    return convFile;
	}

	public boolean leerExcel(String FilePath) throws IOException {
		try {
			FileInputStream file=new FileInputStream(new File(FilePath));//leemos el archivo

			XSSFWorkbook wb=new XSSFWorkbook(file);//extraendo la informacion del archivo
			System.out.println("numHojas: "+wb.getNumberOfSheets());
			for (int hojas = 0; hojas < wb.getNumberOfSheets(); hojas++) {
				XSSFSheet hoja= wb.getSheetAt(hojas);
				int numColumnas=hoja.getDefaultColumnWidth();//num de columnas
				int numFilas=hoja.getLastRowNum();//num de Filas
				System.out.println("NumColumnas: "+numColumnas+" numFilas: "+numFilas);
				
				String dato;
				List<Object> lista=new ArrayList<>();
				for (int i = 1; i <=numFilas; i++) {
					String ROW="";
					Row fila=hoja.getRow(i);
					
					
					Integer NUM=(int)(fila.getCell(0).getNumericCellValue());  
					String EESS=fila.getCell(1).getStringCellValue();
					String EESSN=EESS.replaceAll(" ","");

					
					Date FECHA=fila.getCell(2).getDateCellValue();
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					String FECHAC = sdf.format(FECHA);
					
					String RAZONSOCIAL=fila.getCell(3).getStringCellValue();
					String PLACA=fila.getCell(4).getStringCellValue();
					Double TOTAL=fila.getCell(5).getNumericCellValue();
					Double VOLUMEN=fila.getCell(6).getNumericCellValue();
					Double EMTAGAS=fila.getCell(7).getNumericCellValue();
					Double UNIDAD=fila.getCell(8).getNumericCellValue();
//					System.out.println("NUM: "+NUM+" EESSN: "+EESSN+" FECHA:"+FECHA+" RAZONSOCIAL: "+RAZONSOCIAL+" PLACA: "+PLACA+" TOTAL: "+TOTAL+" VOLUMEN: "+VOLUMEN+" EMTAGAS: "+EMTAGAS+" UNIDAD: "+UNIDAD);
//					Cell celda1=fila.getCell(0);
//					System.out.print("CELDA1: "+celda1.getStringCellValue()+" ");
//					System.out.print("CELDA1: "+celda1.getNumericCellValue()+" ");
					System.out.println("NUM:"+NUM+"  EESSN:"+EESSN+"  FECHA:"+FECHA+"  FECHAC:"+FECHAC+"  RAZONSOCIAL:"+RAZONSOCIAL+"  PLACA:"+PLACA+"  TOTAL:"+TOTAL+"  VOLUMEN:"+VOLUMEN+"  EMTAGAS:"+EMTAGAS+"  UNIDAD:"+UNIDAD);
					
//					this.manejadorCarguios.registrar(EESS, FECHAC, RAZONSOCIAL, PLACA, TOTAL, VOLUMEN, EMTAGAS, UNIDAD);
				}
				System.out.println("LISTA: "+lista.toString());

			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("e: "+e.getMessage());
			return false;
		}
		return true;
	}
	public boolean leerExcel1(String FilePath) throws IOException {
		try {
			FileInputStream file=new FileInputStream(new File(FilePath));//leemos el archivo

			XSSFWorkbook wb=new XSSFWorkbook(file);//extraendo la informacion del archivo
			System.out.println("numHojas: "+wb.getNumberOfSheets());
			
			
			String dato;
			List<Object> lista=new ArrayList<>();
			int idcarg=0;
			int idest=0;
			String LISTA;
			for (int hojas = 0; hojas < wb.getNumberOfSheets(); hojas++) {
				XSSFSheet hoja= wb.getSheetAt(hojas);
				int numColumnas=hoja.getDefaultColumnWidth();//num de columnas
				int numFilas=hoja.getLastRowNum();//num de Filas
				System.out.println("NumColumnas: "+numColumnas+" numFilas: "+numFilas);

				for (int i = 1; i <=numFilas; i++) {
					String ROW="";
					Row fila=hoja.getRow(i);
					
					
					Integer NUM=(int)(fila.getCell(0).getNumericCellValue());  
					String EESS=fila.getCell(1).getStringCellValue();
					String EESSN=EESS.replaceAll(" ","");

					if (i==1) {//SOLO UNA VEZ
						idcarg=this.manejadorCarguios.generarIdCarg();
						idest=this.manejadorCarguios.RETUNRIDEESS(EESS);
					} else {
						idcarg++;
					} 
					
					Date FECHA=fila.getCell(2).getDateCellValue();
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					String FECHAC = sdf.format(FECHA);
					
					String RAZONSOCIAL=fila.getCell(3).getStringCellValue();
					String PLACA=fila.getCell(4).getStringCellValue();
					Double TOTAL=fila.getCell(5).getNumericCellValue();
					Double VOLUMEN=fila.getCell(6).getNumericCellValue();
					Double EMTAGAS=fila.getCell(7).getNumericCellValue();
					Double UNIDAD=fila.getCell(8).getNumericCellValue();
//					System.out.println("NUM: "+NUM+" EESSN: "+EESSN+" FECHA:"+FECHA+" RAZONSOCIAL: "+RAZONSOCIAL+" PLACA: "+PLACA+" TOTAL: "+TOTAL+" VOLUMEN: "+VOLUMEN+" EMTAGAS: "+EMTAGAS+" UNIDAD: "+UNIDAD);
//					Cell celda1=fila.getCell(0);
//					System.out.print("CELDA1: "+celda1.getStringCellValue()+" ");
//					System.out.print("CELDA1: "+celda1.getNumericCellValue()+" ");
//					System.out.println("NUM:"+NUM+"  EESSN:"+EESSN+"  FECHA:"+FECHA+"  FECHAC:"+FECHAC+"  RAZONSOCIAL:"+RAZONSOCIAL+"  PLACA:"+PLACA+"  TOTAL:"+TOTAL+"  VOLUMEN:"+VOLUMEN+"  EMTAGAS:"+EMTAGAS+"  UNIDAD:"+UNIDAD);
					
					ROW="("+idcarg+","+idest+",'"+FECHAC+"','"+RAZONSOCIAL+"','"+PLACA+"',"+TOTAL+","+VOLUMEN+","+EMTAGAS+","+UNIDAD+")";
					lista.add(ROW);
					
//					Object [] Array={EESS, FECHAC, RAZONSOCIAL, PLACA, TOTAL, VOLUMEN, EMTAGAS, UNIDAD};
//					System.out.println("ARRAY: "+Array.toString());
//					lista.add(Array);

				}
			}
			LISTA=lista.toString();
//			System.out.println("LISTA_TOSTRING: "+lista.toString());
			LISTA=LISTA.replace("[", "");
			LISTA=LISTA.replace("]", "");
			System.out.println("LISTA: "+LISTA);
//			this.manejadorCarguios.registrarLIST(LISTA);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("e: "+e.getMessage());
			return false;
		}
		return true;
	}
	
}
