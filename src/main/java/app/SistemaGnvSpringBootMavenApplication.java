package app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

import app.utilidades.URIS;

@SpringBootApplication
public class SistemaGnvSpringBootMavenApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(SistemaGnvSpringBootMavenApplication.class, args);
		URIS uris=new URIS(); 
		System.out.println("INICIANDO APP");
		System.out.println("SISTEMA OPERATIVO:"+uris.checkOS());
		if ((uris.checkOS().contains("Linux"))) {
			try {
				System.out.println("DANDO PERMISOS A LA CARPETA DE ARCHIVOS");
				Process p = Runtime.getRuntime().exec("chmod -R 777 /opt/");
//				Process p1 = Runtime.getRuntime().exec("chown -R postgres:postgres /opt/");
				System.out.println("ruta_archivos linux :"+uris.obtenerRutaCarpetaRecursos(""));
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				System.out.println(e.getMessage());
			}

			
		}			
	}
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		// TODO Auto-generated method stub
		return builder.sources(SistemaGnvSpringBootMavenApplication.class);
	}

}
