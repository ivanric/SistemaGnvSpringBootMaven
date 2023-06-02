package app.utilidades;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;

import app.manager.ManejadorCarguios;
import app.service.CarguioService;
import app.service.EstacionService;
import au.com.bytecode.opencsv.CSVReader;

public class LectorCSV {

	// Propiedades
	private char separador;
	private char comillas;
	private String[] campos;
	private URIS uris=new URIS();
	
//	@Autowired
//	CarguioService carguioService;
//	@Autowired
//	EstacionService estacionService;
	
//	@Autowired
//	ManejadorCarguios manejadorCarguios=new ManejadorCarguios();
	
	// Constructor
//	private EstacionService estacionService=new EstacionService();
	
	
	/**
	 * Inicializa el constructor definiendo el separador de los campos y las comillas usadas
	 * @param separador
	 * @param comillas
	 */
	
	public LectorCSV() {
//		super();
		// TODO Auto-generated constructor stub
	}
	
	public LectorCSV(char separador, char comillas) {
		this.separador = separador;
		this.comillas = comillas;
	}

	// M�todos
	/**
	 * Lee un CSV que no contiene el mismo caracter que el separador en su texto
	 * y sin comillas que delimiten los campos
	 * @param path Ruta donde est� el archivo
	 * @throws IOException 
	 */
	public List<String[]> leerCSVSimple(String path) throws IOException {
		campos=null;
		this.separador=';';
		
		
		// Abro el .csv en buffer de lectura
		BufferedReader bufferLectura = new BufferedReader(new FileReader(path));
		
		// Leo una l�nea del archivo
		String linea = bufferLectura.readLine();
		
		//Crear una lista
		List<String[]> lista=new ArrayList<>();
		
		while (linea != null) {
			// Separa la l�nea le�da con el separador definido previamente
//			System.out.println("Linea: "+linea); 
			campos = linea.split(String.valueOf(this.separador));
//			System.out.println(Arrays.toString(campos));
//			System.out.println("INDEX:"+campos[0]);
//			System.out.println("campos:"+campos.toString());
			
//			//AGREGANDO METODO  
//			convertCSVtoTxt(campos, nombreArchivo);
			lista.add(campos);
			
			// Vuelvo a leer del fichero
			linea = bufferLectura.readLine();
		}
		
		// CIerro el buffer de lectura
		if (bufferLectura != null) {
			bufferLectura.close();
		}
		return lista;
	}
	
	/**
	 * Lee csv complejo usando la librer�a OpenCSV
	 * @param path Ruta donde est� el archivo
	 * @throws IOException
	 */
	public void leerCSVComplejo(String path) throws IOException {
		
		CSVReader lector = new CSVReader(new FileReader(path), separador, comillas);
		String[] linea = null;
		
		while ((linea = lector.readNext()) != null) {
			System.out.println(Arrays.toString(linea));
		}
		
		if (linea != null) {
			lector.close();
		}
	}
	
	public Map<String, Object> convertCSVtoTxt(List<String[]> lista,String nombreCSV,@Autowired EstacionService estacionService,@Autowired CarguioService carguioService,HttpServletRequest req) throws IOException {
	
		Map<String, Object> respuesta=new HashMap<String, Object>();
		
		String ruta = uris.GetDirecccionTextos(req);
//		String ruta = URIS.direccionTextos;
//		System.out.println("ruta: "+ruta);
		
		int ind=nombreCSV.lastIndexOf(".");
//		System.out.println("ind:"+ind);
		
		nombreCSV=nombreCSV.substring(0, ind);
//		System.out.println("nombreCSV:"+nombreCSV);
		nombreCSV=nombreCSV+".txt";
		
		Path path=Paths.get(ruta+"/"+nombreCSV);
		ruta=path.toString();
//		System.out.println("ruta: "+ruta);
		String YEAR=""; 
		String MONTH="";
		String DAY="";
		try {
			File archivo = new File(ruta);
			// Si el archivo no existe es creado
//			System.out.println("archivo.exists():"+archivo.exists());
            if (archivo.exists()) {
            	new CargarArchivos().EliminarArchivoTXT(archivo);
            	System.out.println("Eliminando archivo TExT");
            }
            archivo.createNewFile();
//            System.out.println("creando archivo TEXT");
             
            FileWriter fw = new FileWriter(archivo);
            BufferedWriter bw = new BufferedWriter(fw);
            Integer idest=0; 
            int idcarg=0;
            
            boolean bandid=true;
            Double UNIDAD=1.66; 
//            System.out.println("TAM_CSV:"+lista.size());
//            String [] vector1=lista.get(1);
//            System.out.println("vec:"+vector1.toString());
    		for (int i = 1; i < lista.size(); i++) {//empieza en 1 de la lista de arrays porq no se lee el titulo
//    			if(i==1)System.out.println("Array1"+(i)+":"+Arrays.toString(lista.get(i)));
//    			System.out.println("Array"+(i)+":"+Arrays.toString(lista.get(i)));
//    			String [] vector=lista.get(i);
    			String [] vector=lista.get(i);
//    			System.out.println("vector:"+vector[0]);
    			String EESS="",EESSN="",IDEST=""; 
    			if(i==1){
    				EESS=vector[1]; 
    				EESSN=EESS.replaceAll(" ","");
//    				System.out.println("EESS"+EESS);
//    				System.out.println("EESSN"+EESSN);
//    				idest=this.estacionService.RETUNRIDEESS(EESS);
//    				System.out.println("IDESTQUERY:"+estacionService.RETUNRIDEESS(EESS));
    				idest=estacionService.RETUNRIDEESS(EESS);
//    				System.out.println("idEST:"+idest);
    			} 
    			
    			Date date=null; 
    			String FECHAC="";
    			//CONVIRTIENDO FECHA
				String fecha=vector[2].trim();
//				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//				String FECHAC = sdf.format(FECHA);
				
//				SimpleDateFormat  inSDF= new SimpleDateFormat("dd/mm/yyyy");
				SimpleDateFormat  inSDF= new SimpleDateFormat("dd/MM/yyyy");
				SimpleDateFormat outSDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//				SimpleDateFormat outSDF = new SimpleDateFormat("yyyy-MM-dd");
				
				
				
				try {
					fecha=fecha.trim();
//					System.out.println("FECHA_VECTOR:"+fecha);	
//					System.out.println("tipo de variable:"+fecha.getClass().getSimpleName());
					if(!fecha.equals("")) { 
						System.out.println("ENTRO A CONVERTIR FECHA");
						date = inSDF.parse(fecha);
						FECHAC=outSDF.format(date);  
					}
//					else {  
//						FECHAC=""; 
//					} 

//					System.out.println("FECHAC:"+FECHAC);
					Calendar c = Calendar.getInstance(); 
//					System.out.println("CALENDAR:"+c.toString());
					
					if(date!=null) {
						c.setTime(date);
						YEAR=String.valueOf(c.get(Calendar.YEAR));
						MONTH=String.valueOf((c.get(Calendar.MONTH)+1));
						DAY=String.valueOf(c.get(Calendar.DAY_OF_MONTH));
						
					}
					
//					System.out.println("YEAR:"+c.get(Calendar.YEAR));
//					System.out.println("MONTH:"+c.get(Calendar.MONTH+1));
//					System.out.println("MONDAY:"+c.get((Calendar.MONDAY+1)));
//					System.out.println("DAY:"+c.get(Calendar.DAY_OF_MONTH));
//					if()
					
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
//				System.out.println("fechaF:"+fechaF);
				//FIN FECHA
				
				String RAZONSOCIAL=vector[3];
				String PLACA=vector[4];

				String TOTAL_s=vector[5].replaceAll(",",".");
//				System.out.println("total_S:"+TOTAL_s);
				Double TOTAL=Double.valueOf(TOTAL_s);
				Double VOLUMEN=TOTAL/UNIDAD;
				Double FONDO_ROTATORIO=VOLUMEN*(0.20);	
				 
				String GESTION="";
				String MES=""; 
//				if(i==1) {
//					System.out.println("anio_csv:"+vector[6]);
//					System.out.println("anio_csv_null:"+vector[6]==null);
//					System.out.println("anio_csv_str:"+vector[6]=="");
					
					if(date==null) {
					 
						if(vector[6]!=""){
							YEAR=vector[6];
	//						System.out.println("GESTION:"+GESTION);
	//						if(YEAR=="")YEAR=GESTION;
	//						System.out.println("YEAR:"+YEAR);
						}
//						System.out.println("mes_csv:"+vector[7]);
//						System.out.println("anio_csv_null:"+vector[7]==null);
//						System.out.println("anio_csv_str:"+vector[7]=="");
						if(vector[7]!="") {
							MONTH=vector[7]; 
	//						System.out.println("GESTION:"+GESTION);
	//						if(MONTH=="")MONTH=MES;
	//						System.out.println("YEAR:"+YEAR);
						}
					} 
//				}
				
//				int idbenR=estacionService.RETUNR_IDBEN(PLACA);
				int idbenR=0;
//				if (idbenR!=0) {
//					estacionService.RETUNR_IDBEN(PLACA);
//				}
//		
				Map<String,Object> mapa=estacionService.RETUNR_PLACA(PLACA);
//				System.out.println("mapa:"+mapa.toString()); 


//				System.out.println("mapa.isEmpty():"+mapa.isEmpty());
				if(mapa.size()>0) {//AUMENTADO, si la placa existe en la bd 
//				if(mapa.isEmpty() || mapa.size()!=0) {//AUMENTADO, si la placa existe en la bd 
					if(i==1){
//						System.out.println("PLACA ENCONTRADA "+PLACA+"..!!");	
					}

		            
					if(bandid) {//ya que para el primera linea ya se rescato el idcarg
//	    				idcarg=carguioService.getIdPrimaryKey();
						
						int IDCARGINIT=carguioService.getIdPrimaryKey();
//						System.out.println("carguioService.getIdPrimaryKey():"+IDCARGINIT);
						if(IDCARGINIT!=0) {
//		    				idcarg=IDCARGINIT-1; 
		    				idcarg=IDCARGINIT; 
						}
						bandid=false;
	    			}else {
	    				idcarg++;
	    			}
					
//					System.out.println("idcarg:"+idcarg);
//					System.err.println("idest??"+idest);
					if(idest!=0) {
						IDEST=String.valueOf(idest); 
					}
//	    			bw.write(idcarg+";"+IDEST+";"+FECHAC+";"+RAZONSOCIAL+";"+PLACA+";"+TOTAL+";"+VOLUMEN+";"+FONDO_ROTATORIO+";"+UNIDAD+";"+idbenR+";"+PLACA+";"+YEAR+";"+"1"+";"+MONTH+";"+DAY);
	    			bw.write(idcarg+";"+IDEST+";"+FECHAC+";"+RAZONSOCIAL+";"+PLACA+";"+TOTAL+";"+VOLUMEN+";"+FONDO_ROTATORIO+";"+UNIDAD+";"+idbenR+";"+PLACA+";"+YEAR+";"+"1"+";"+MONTH+";"+DAY);
	    			bw.newLine();	
				}else { 
//				System.out.println("NO SE GRABO LOS DATOS EN EL ARCHIVO CSV..! PLACA NO ENCONTRADA:"+PLACA);
				} 
    		}
            
            bw.close();
//            System.out.println("TAMAÑO FILE:"+archivo.length());
            if (archivo.length()>0) {
            	respuesta.put("estado", true);
			}else {
				respuesta.put("estado", false);
			}
    		
//    		System.out.println("realPath"+archivo.getPath());
//    		System.out.println("realPath1"+archivo.getAbsolutePath());
    		respuesta.put("path", archivo.getAbsolutePath());
    		System.out.println("path_archivosc_svs:"+archivo.getAbsolutePath());
//    		respuesta.put("path", archivo.getPath());
			respuesta.put("archivotxt", archivo);
			

			 //para linux persmisos
//			 Process process=null;
//			 process=Runtime.getRuntime().exec("chmod 777 "+archivo.getAbsolutePath());
			uris.setPermitionFolfer(archivo.getAbsolutePath());
				
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			System.out.println("e:"+e.getMessage());
			respuesta.put("estado", false);
		}

//		return true;
		return respuesta;

	}
}