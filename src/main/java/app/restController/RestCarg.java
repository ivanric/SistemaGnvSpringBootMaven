//package app.restController;
//
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileNotFoundException;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import org.apache.log4j.Logger;
//import org.apache.poi.ss.usermodel.Cell;
//import org.apache.poi.ss.usermodel.Row;
//import org.apache.poi.xssf.usermodel.XSSFSheet;
//import org.apache.poi.xssf.usermodel.XSSFWorkbook;
//import org.apache.tomcat.util.http.fileupload.FileItem;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.transaction.interceptor.TransactionAspectSupport;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.multipart.MultipartFile;
//import org.springframework.web.multipart.MultipartRequest;
//import org.springframework.web.multipart.commons.CommonsMultipartFile;
//
//import app.manager.ManejadorCarguios;
//import app.models.Persona;
//import jxl.Sheet;
//import jxl.Workbook;
//
//
//
//@RequestMapping("/RestCarguios/")
//@RestController
//public class RestCarg{
//	@Autowired
//	ManejadorCarguios manejadorCarguios;
//	
//	@RequestMapping(value="listar")
//	public ResponseEntity<?> listarServicio(HttpServletRequest req,HttpServletResponse res){	
//		List<Map<String, Object>> lista=this.manejadorCarguios.Listar(req);
////		System.out.println("lista: "+lista.toString());
//		return new ResponseEntity<List<?>>(lista,HttpStatus.OK);
//	} 
//	@Transactional
//	@RequestMapping(value="adicionar")
////	public Map<String, Object> adicionar(HttpServletRequest req,Persona p,HttpServletResponse res,MultipartFile archivo){
//		public Map<String, Object> adicionar(HttpServletRequest req,Persona p,HttpServletResponse res){
////		HttpSession sesion=req.getSession(true);
////		Persona xuser=(Persona) sesion.getAttribute("xusuario");
//		MultipartFile multipartFile=((MultipartRequest)req).getFile("archivo");
//		Map<String, Object> respuesta=new HashMap<String, Object>();
//		String nombreArchivo;
//		try{
//			if(multipartFile!=null && multipartFile.getSize()>0) {
//				nombreArchivo=multipartFile.getOriginalFilename();
//				System.out.println("nombre: "+nombreArchivo);
////				File data=convert(multipartFile);
//				File data1=multipartToFile(multipartFile,multipartFile.getOriginalFilename());
////				System.out.println("ruta: "+data.getAbsolutePath());
//				System.out.println("ruta1: "+data1.getAbsolutePath());
//				System.out.println("ruta2: "+data1.getPath());
//				
////				File file=new File(nombreArchivo);
////				multipartFile.transferTo(file);
////				File path=file.getAbsoluteFile();
//////				File path1=file.getAbsoluteFile();
////				String ruta=path.toString();
////				String ruta1=file.getAbsolutePath();
//
////				System.out.println("ruta: "+ruta);
////				System.out.println("ruta1: "+ruta1);
////				LeerArchivoExcel(data1.getAbsolutePath()); 
//				leerExcel(data1.getAbsolutePath()); 
////				LeerArchivoExcel(multipartFile.getOriginalFilename()); 
//			}
//			
////			boolean consulta=this.manejadorTalleres.registrar(req,p);
////			System.out.println("resp: "+consulta);
////			respuesta.put("estado", consulta);
//			respuesta.put("estado", true);
//		}catch (Exception e){
//			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
//			respuesta.put("estado",false);
//		}
//		return respuesta;
//	}
//	/*
//	 public File convert(MultipartFile file) throws IOException
//	 {    
//	   File convFile = new File(file.getOriginalFilename());
//	   convFile.createNewFile(); 
//	   FileOutputStream fos = new FileOutputStream(convFile); 
//	   fos.write(file.getBytes());
//	   fos.close(); 
//	   return convFile;
//	 }*/
//	 public File multipartToFile(MultipartFile multipart, String fileName) throws IllegalStateException, IOException {
//		 String ruta=".//src//main//resources//static//archivos//excels//";
//		 File convFile = new File(ruta+fileName);
//		 FileOutputStream fos = new FileOutputStream(convFile); 
//		 fos.write(multipart.getBytes());
//		 fos.close(); 
//	    return convFile;
//	}
//	private void LeerArchivoExcel(String archivo) {
//		int contador=1;
//		System.out.println("archiv1o: "+archivo);
//		try {
////			 String ruta=".//src//main//resources//static//archivos//excels//";
////			Path path=Paths.get(ruta+archivo);
////			System.out.println("Path: "+path.toString());
////			Workbook archivoExcel=Workbook.getWorkbook(new File(path.toString()));
//			Workbook archivoExcel=Workbook.getWorkbook(new File(archivo));
//			//rrecorre cada fila
//			System.out.println("archivo: "+archivo);
//			for (int hojas = 0; hojas < archivoExcel.getNumberOfSheets(); hojas++) {
//				Sheet hoja=archivoExcel.getSheet(hojas);
//				int numColumnas=hoja.getColumns();
//				int numFilas=hoja.getRows();
//				String dato;
//				
//				for (int fila = 1; fila < numFilas; fila++) {
//					for (int columna = 0; columna <numColumnas; columna++) {
//						dato=hoja.getCell(columna, fila).getContents();
//						System.out.println(dato+" ");
////						switch (contador) {
////							case value:
////								break;
////
////						default:
////							break;
////						}
//					}
//				}
//			}
//			//..sigue
//		} catch (Exception e) {
//			// TODO: handle exception
//			e.printStackTrace();
//			System.out.println(e.getMessage());
//		}
//		 
//	}
//	public void leerExcel(String FilePath) throws IOException {
//		try {
//			FileInputStream file=new FileInputStream(new File(FilePath));//leemos el archivo
//
//			XSSFWorkbook wb=new XSSFWorkbook(file);//extraendo la informacion del archivo
//			System.out.println("numHojas: "+wb.getNumberOfSheets());
//			for (int hojas = 0; hojas < wb.getNumberOfSheets(); hojas++) {
//				XSSFSheet hoja= wb.getSheetAt(hojas);
//				int numColumnas=hoja.getDefaultColumnWidth();//num de columnas
//				int numFilas=hoja.getLastRowNum();//num de Filas
//				System.out.println("NumColumnas: "+numColumnas+" numFilas: "+numFilas);
//				
//				String dato;
//				for (int i = 1; i <=numFilas; i++) {
//					Row fila=hoja.getRow(i);
//					Integer NUM=(int)(fila.getCell(0).getNumericCellValue());  
//					String EESS=fila.getCell(1).getStringCellValue();
//					String EESSN=EESS.replaceAll(" ","");
//					Date FECHA=fila.getCell(2).getDateCellValue();
//				
//					
//					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//					String FECHAC = sdf.format(FECHA);
//					
//					String RAZONSOCIAL=fila.getCell(3).getStringCellValue();
//					String PLACA=fila.getCell(4).getStringCellValue();
//					Double TOTAL=fila.getCell(5).getNumericCellValue();
//					Double VOLUMEN=fila.getCell(6).getNumericCellValue();
//					Double EMTAGAS=fila.getCell(7).getNumericCellValue();
//					Double UNIDAD=fila.getCell(8).getNumericCellValue();
////					System.out.println("NUM: "+NUM+" EESSN: "+EESSN+" FECHA:"+FECHA+" RAZONSOCIAL: "+RAZONSOCIAL+" PLACA: "+PLACA+" TOTAL: "+TOTAL+" VOLUMEN: "+VOLUMEN+" EMTAGAS: "+EMTAGAS+" UNIDAD: "+UNIDAD);
////					Cell celda1=fila.getCell(0);
////					System.out.print("CELDA1: "+celda1.getStringCellValue()+" ");
////					System.out.print("CELDA1: "+celda1.getNumericCellValue()+" ");
//					System.out.println("NUM:"+NUM+"  EESSN:"+EESSN+"  FECHA:"+FECHA+"  FECHAC:"+FECHAC+"  RAZONSOCIAL:"+RAZONSOCIAL+"  PLACA:"+PLACA+"  TOTAL:"+TOTAL+"  VOLUMEN:"+VOLUMEN+"  EMTAGAS:"+EMTAGAS+"  UNIDAD:"+UNIDAD);
//				}
//
//			}
//			/*
//			System.out.println();
//			XSSFSheet sheet=wb.getSheetAt(0);//que hoja usaremos (0, es la primera hoja)
//			int numFilas=sheet.getLastRowNum();
//			for (int i = 0; i <= numFilas; i++) {//pusimo <=, por que nos devuelve cantidad exacta
//				Row fila=sheet.getRow(i);//sacamos cada fila de la hoja
//				int numCols=fila.getLastCellNum();//numero de columnas de la fila
//				for (int j = 0; j < numCols; j++) {
//					Cell celda=fila.getCell(j);
//					switch (celda.getCellTypeEnum().toString()) {//para leer el tipo de celda que contiene y lo convertimos en cadena para compararlo
//						case "NUMERIC":
//							System.out.print(celda.getNumericCellValue()+" ");
//						break;
//						case "STRING":
//							System.out.print(celda.getStringCellValue()+" ");
//						break;
//						case "FORMULA":
//							System.out.print(celda.getCellFormula()+" ");
//						break;
//
//					default:
//						break;
//					}
//				}
//				System.out.println("");
//			}*/
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			System.out.println("e: "+e.getMessage());
//		}
//	
//	}
//	
//}
