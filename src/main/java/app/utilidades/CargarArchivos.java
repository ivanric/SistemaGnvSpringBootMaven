package app.utilidades;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class CargarArchivos {
//	@Autowired
//	ServletContext context;
//	@Autowired
//	HttpServletRequest request;
	public URIS uris=new URIS();
	
	private String direccion=".//src//main//resources//static//archivos//fotos//";
//	private String direccion="";
//	private String direccion=context.getRealPath("fotos");
//	private String direccion=request.getServletContext().getRealPath("/fotos");
	
	private String direccionArchivos=".//src//main//resources//static//archivos//excels//";
	private String direccionArchivosCSV=".//src//main//resources//static//archivos//csvs//";
	private String direccionArchivosTXT=".//src//main//resources//static//archivos//textos//";
	
	public File saveReturnCSV(MultipartFile multipart,HttpServletRequest req) throws IllegalStateException, IOException {
//		System.out.println("GUARDANDO ARCHIVO CSV"); 
//		System.out.println("NOMBRE DE CARPETA archivos:"+uris.obtenerRutaCarpetaRecursos(""));
//		System.out.println("NOMBRE DE CARPETA CSV:"+uris.obtenerRutaCarpetaRecursos("csvs"));
//		System.out.println("NOMBRE DE CARPETA excels:"+uris.obtenerRutaCarpetaRecursos("excels"));
//		System.out.println("NOMBRE DE CARPETA textos:"+uris.obtenerRutaCarpetaRecursos("textos"));
//		System.out.println("NOMBRE DE CARPETA fotos:"+uris.obtenerRutaCarpetaRecursos("fotos"));
		
//		String ruta=uris.direccionCsvs;
		 String ruta=uris.GetDirecccionCsvs(req); 
//		 String ruta=uris.GetDirecccionCsvs();
		 
		 File convFile = new File(ruta+"/"+multipart.getOriginalFilename());
		 FileOutputStream fos = new FileOutputStream(convFile); 
		 fos.write(multipart.getBytes()); 
		 fos.close(); 
		  
		 //para linux persmisos
		 Process process=null;
		 try {
//			 String string[]=new String[2];
//			 string[0]="notepad.exe";
//			 string[1]="";
			 
			 
//			 System.out.println("ruta_csv:"+convFile.getPath());
//			 System.out.println("ruta_csvabs:"+convFile.getAbsolutePath());
			 
			 String path_file=convFile.getAbsolutePath();
//			 System.out.println("path_file:"+path_file); 
			 
			 //QUITANDO PERMISO
//			 uris.setPermitionFolfer(path_file);
			 
			 
//			 process=Runtime.getRuntime().exec("chmod 777 "+path_file);
//			 process=Runtime.getRuntime().exec(string,null);
			 	
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			System.out.println(e.getMessage()); 
		}
	    return convFile;
	}
	
	public void saveImagen(MultipartFile archivo,String nombreFoto,HttpServletRequest req) throws IOException {
		if (!archivo.isEmpty()) {
			byte[] bytes=archivo.getBytes();
			//System.out.println("UBICACION: "+System.getProperty("user.dir"));
			//System.out.println("ENLACE: "+context.getRealPath("files"));
//			direccion=req.getSession().getServletContext().getRealPath("/resources/static/archivos/fotos/");
			Path path=Paths.get(direccion+nombreFoto);
			Files.write(path,bytes);
		}
		
	}
	public void modifyImagen(MultipartFile foto,String nombreFotoAnterior,String nombreFotoNuevo,HttpServletRequest req) throws IOException {
		//System.out.println("MODIFICANDO FOTO"+ nombreFotoAnterior +" PARA "+nombreFotoNuevo);
		if (!foto.isEmpty()) {
			byte[] bytes=foto.getBytes();
			Path path=null;
			
			
//			String home = System.getProperty("user.dir");
//			String home1 = System.getProperty("user.home");
//			String home2 = System.getProperty("catalina.base");
//			System.out.println("RUTA_HOME"+home);
//			System.out.println("RUTA_HOME1"+home1);
//			System.out.println("RUTA_HOME2"+home2);
//			File d=new File("/resources/");
//			System.out.println("RELATIVE:"+d.getAbsolutePath());
//			System.out.println("RELATIVE1:"+d.getPath());
			
//			direccion=req.getSession().getServletContext().getRealPath("/resources/static/archivos/fotos/");
//			direccion="../../../../WebContent/resources/archivos/fotos/";
			System.out.println("direccion: "+direccion);
			
			if(!nombreFotoAnterior.equals("user.png")) {
				System.out.println("Eliminando.."+nombreFotoAnterior);

				path=Paths.get(direccion+nombreFotoAnterior);
				Files.delete(path);
			}
			try {
				System.out.println("creando.."+nombreFotoNuevo);
				path=Paths.get(direccion+nombreFotoNuevo);
				Files.write(path,bytes);
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				System.out.println(e.toString());
				e.printStackTrace();
			}
			Files.write(path,bytes);
		}
		
	}
	public void EliminarArchivo(MultipartFile Archivo) throws IOException {
		//System.out.println("MODIFICANDO FOTO"+ nombreFotoAnterior +" PARA "+nombreFotoNuevo);
		if (!Archivo.isEmpty()) {
			Path path=null;
			try {
				System.out.println("Eliminando.."+Archivo.getOriginalFilename());
				path=Paths.get(direccionArchivos+Archivo.getOriginalFilename());
				Files.delete(path);
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				System.out.println(e.getMessage());
			}			
		}
		
	}
	
	public void EliminarArchivoCSV(File Archivo) throws IOException {
		//System.out.println("MODIFICANDO FOTO"+ nombreFotoAnterior +" PARA "+nombreFotoNuevo);
		if (Archivo!=null) {
			Path path=null;
			try {
				System.out.println("Eliminando arvhico csv.."+Archivo.getName());
				path=Paths.get(direccionArchivosCSV+Archivo.getName());
				Files.delete(path);
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				System.out.println(e.getMessage());
			}			
		}
		
	}
	
	public void EliminarArchivoTXT(File Archivo) throws IOException {
		//System.out.println("MODIFICANDO FOTO"+ nombreFotoAnterior +" PARA "+nombreFotoNuevo);
		URIS uris=new URIS();
		if(Archivo!=null) {
//			System.out.println("ARCHIVOTXT EMPLY:"+Archivo.exists());
//			System.out.println("ARCHIVOTXT vacio:"+Archivo.length());
//			if (Archivo.length()>0) {
				Path path=null;
//				Process process=null;
				try { 
					System.out.println("Eliminando archivo txt.."+Archivo.getName());
//					path=Paths.get(direccionArchivosTXT+Archivo.getName());
					//para linux persmisos

//					 process=Runtime.getRuntime().exec("chmod 777 "+uris.obtenerRutaCarpetaRecursos("textos")+"/"+Archivo.getName());
					 
					uris.setPermitionFolfer(Archivo.getAbsolutePath()); 
					path=Paths.get(uris.obtenerRutaCarpetaRecursos("textos")+"/"+Archivo.getName());
					Files.delete(path);
					
					
					
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
					System.out.println(e.getMessage());
				}			
//			}
		}


		
	}
	
	/*
	private String direccion=System.getProperty("user.dir")+"/files/fotos/";
	public void saveImagen(MultipartFile archivo,String nombreFoto) throws IOException {
		System.out.println("ENTRO");
		System.out.println("Direccion: "+direccion);
		if (!archivo.isEmpty()) {
			byte[] bytes=archivo.getBytes();	
			
			Path path=Paths.get(direccion+nombreFoto);
			Files.write(path,bytes);
		}
		
	}*/
}
