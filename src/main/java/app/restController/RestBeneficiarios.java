package app.restController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import app.manager.ManejadorBeneficiarios;
import app.models.DocumentoBeneficiario;
import app.models.Persona;
import app.models.Solicitud;

@RequestMapping("/RestBeneficiarios/")
@RestController
public class RestBeneficiarios {
	@Autowired
	ManejadorBeneficiarios manejadorBeneficiarios;
	
	@RequestMapping(value="listar")
	public ResponseEntity<List<Persona>> listarBneneficiarios(HttpServletRequest req,HttpServletResponse res){	
		List<Persona> beneficiarios=this.manejadorBeneficiarios.Lista(req);
		System.out.println("listaBen: "+beneficiarios.toString());
		return new ResponseEntity<List<Persona>>(beneficiarios,HttpStatus.OK);
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping("listar_d")
	public @ResponseBody Map<?, ?> lista(HttpServletRequest request, Integer draw,int length, Integer start, int estado)throws IOException{
		String total;
		Map<String, Object> Data = new HashMap<String, Object>();
		String search = request.getParameter("search[value]");
		List<Persona> lista=this.manejadorBeneficiarios.listar_d(start, estado, search,length);
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
	
	@RequestMapping(value="documentosBeneficiario")
	public ResponseEntity<List<DocumentoBeneficiario>> docuemtosBeneficiario(HttpServletRequest req,HttpServletResponse res){	
		List<DocumentoBeneficiario> listaDocumentos=this.manejadorBeneficiarios.listaDocumentos();
		return new ResponseEntity<List<DocumentoBeneficiario>>(listaDocumentos,HttpStatus.OK);
	}
	
	
	@RequestMapping(value="documentosBeneficiarioRecalificacion")
	public ResponseEntity<List<DocumentoBeneficiario>> documentosBeneficiarioRecalificacion(HttpServletRequest req,HttpServletResponse res){	
		List<DocumentoBeneficiario> listaDocumentos=this.manejadorBeneficiarios.documentosDocumentosRecalificacion();
		return new ResponseEntity<List<DocumentoBeneficiario>>(listaDocumentos,HttpStatus.OK);
	}
	
	@RequestMapping({"existeCi"})
	public ResponseEntity<Map<String, Object>> existeCi(HttpServletRequest req){
		Map<String, Object> mapa=new HashMap<String, Object>();
		String ci=req.getParameter("ci");
		boolean existe;
		System.out.println("tam_"+ci.length());
		
		if(this.manejadorBeneficiarios.verificarCi(ci)){
			existe=true;
		}else{
			existe=false;			
		}
		System.out.println("existe: "+existe);
		mapa.put("estado", existe);
		return new ResponseEntity<Map<String,Object>>(mapa,HttpStatus.OK);
	}
	
	@RequestMapping({"existeCiRecalificacion"})
	public ResponseEntity<Map<String, Object>> existeCiRecalificacion(HttpServletRequest req){
		Map<String, Object> mapa=new HashMap<String, Object>();
		String ci=req.getParameter("ci");
		boolean existe;
		System.out.println("tam_"+ci.length());
		
		if(this.manejadorBeneficiarios.verificarCiRecalificacion(ci)){
			existe=true;
		}else{
			existe=false;			
		}
		System.out.println("existe: "+existe);
		mapa.put("estado", existe);
		return new ResponseEntity<Map<String,Object>>(mapa,HttpStatus.OK);
	}
	
	@RequestMapping({"existeCiSistema"})
	public ResponseEntity<Map<String, Object>> existeCiSistema(HttpServletRequest req){
		Map<String, Object> mapa=new HashMap<String, Object>();
		String ci=req.getParameter("ci");
		boolean existe;
		System.out.println("tam_"+ci.length());
		
		if(this.manejadorBeneficiarios.verificarCiSistema(ci)){
			existe=true;
		}else{
			existe=false;			
		}
		System.out.println("existe: "+existe);
		mapa.put("estado", existe);
		return new ResponseEntity<Map<String,Object>>(mapa,HttpStatus.OK);
	}
	
	
	@RequestMapping(value="verificacionUsuario")
	public ResponseEntity<Map<String, Object>> VerificarTipoResitro(HttpServletRequest req){
		Map<String, Object> mapa=new HashMap<String, Object>();
		
		List<Object> lista=new ArrayList<Object>();
		String ci=req.getParameter("ci");
		System.out.println("ci: "+req.getParameter("ci"));
		
		//usuario normal, persona como tal
		Persona per=this.manejadorBeneficiarios.getPersonaByCi_d1(req.getParameter("ci"));
		mapa.put("persona",per);
		List<?> ListaTelefono=this.manejadorBeneficiarios.ListaTelefonos(per.getIdper());
		mapa.put("telefonos",ListaTelefono);
		
		mapa.put("telefonos",ListaTelefono);
		if(this.manejadorBeneficiarios.verificarUsuarioPersona(ci)){
			mapa.put("tipo","user");
			mapa.put("mensaje","Hemos encontrado esta persona registrada en el sistema");
		}else if(this.manejadorBeneficiarios.verificarUsuarioPersonaTaller(ci)){
			mapa.put("tipo","usertaller");
			mapa.put("mensaje","Se registrara a un usuario de taller");
			
		}else if(this.manejadorBeneficiarios.verificarUsuarioPersonaEstacion(ci)) {
			mapa.put("tipo","userestacion");
			mapa.put("mensaje","Se registrara a un usuario de estacion de servicio");
			
		}


		
		return new ResponseEntity<Map<String,Object>>(mapa,HttpStatus.OK);		
	}
	
//	@RequestMapping({"existeCiBenf"})
//	public ResponseEntity<Map<String, Object>> existeCiBenf(HttpServletRequest req){
//		Map<String, Object> mapa=new HashMap<String, Object>();
//		String ci=req.getParameter("ci");
//		boolean existe;
//		System.out.println("tam_"+ci.length());
//		
//		if(this.manejadorBeneficiarios.verificarCiBenf(ci)){
//			existe=true;
//		}else{
//			existe=false;			
//		}
//		System.out.println("existe: "+existe);
//		mapa.put("estado", existe);
//		return new ResponseEntity<Map<String,Object>>(mapa,HttpStatus.OK);
//	}
	
	@Transactional
	@RequestMapping(value="adicionar")
	public Map<String, Object> adicionar(HttpServletRequest req,HttpServletResponse res,Persona p,@RequestParam String ci){
		System.out.println("Pers: "+p);
//		System.out.println("ci: "+ci);
//		String[] documentos=req.getParameterValues("documentos[]");
		String[] telefonos=req.getParameterValues("telefono[]");
//		System.out.println("documentosArray: "+documentos.toString());
//		System.out.println("tamanioDocArray: "+documentos.length);
		System.out.println("TelefonosArray: "+telefonos.length);
		Map<String, Object> respuesta=new HashMap<String, Object>();
//		for (String i : documentos) {
//			System.out.println("coddocb: "+i);
//		}
		for (String i : telefonos) {
			System.out.println("telefonos: "+i);
//			System.out.println("vacio? : "+i.equals(null));
			System.out.println("vacio? : "+!i.equals(""));
			System.out.println("vacio? : "+i.equals(""));
		}
		try {
			boolean consulta=this.manejadorBeneficiarios.registrar_d(req, p,telefonos);
//			boolean consulta=this.manejadorBeneficiarios.registrar_d(req, p, documentos,telefonos);
			//boolean consulta=true;
			System.out.println(consulta);
			respuesta.put("estado", consulta);
		} catch (Exception e) {
			// TODO: handle exception
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			respuesta.put("estado",false);
		}
		respuesta.put("estado",true);
		return respuesta;
	}
	@RequestMapping(value="datos-mod")
	public ResponseEntity<List<?>> datosMod(HttpServletRequest req){
		List<Object> lista=new ArrayList<Object>();
		int idben=Integer.parseInt(req.getParameter("idben"));
		Persona BeneficiarioDatos=this.manejadorBeneficiarios.datosModificar(idben);
		List<?> ListaTelefono=this.manejadorBeneficiarios.ListaTelefonos(BeneficiarioDatos.getIdper());
		List<DocumentoBeneficiario> listaDocumentos=this.manejadorBeneficiarios.getDocumentos(Integer.parseInt(req.getParameter("idben")));
		lista.add(BeneficiarioDatos);
		lista.add(ListaTelefono);
		lista.add(listaDocumentos);
		System.out.println("Persona a Modificar:"+lista);
		return new ResponseEntity<List<?>>(lista,HttpStatus.OK);		
	}
	@RequestMapping(value="datos-mod_d")
	public ResponseEntity<List<?>> datosMod_d(HttpServletRequest req){
		List<Object> lista=new ArrayList<Object>();
		int idben=Integer.parseInt(req.getParameter("idben"));
		System.out.println("idben"+idben);
		Persona BeneficiarioDatos=this.manejadorBeneficiarios.datosModificar_d(idben);
		List<?> ListaTelefono=this.manejadorBeneficiarios.ListaTelefonos(BeneficiarioDatos.getIdper());
		List<DocumentoBeneficiario> listaDocumentos=this.manejadorBeneficiarios.getDocumentos(Integer.parseInt(req.getParameter("idben")));
		List<DocumentoBeneficiario> listaDocumentosTotal=this.manejadorBeneficiarios.listaDocumentos();
		
		lista.add(BeneficiarioDatos);
		lista.add(ListaTelefono);
		lista.add(listaDocumentos);
		lista.add(listaDocumentosTotal);
		System.out.println("Persona a Modificar:"+lista);
		return new ResponseEntity<List<?>>(lista,HttpStatus.OK);		
	}

	@RequestMapping(value="getIdBen")
	public ResponseEntity<List<?>> getIdBen(HttpServletRequest req){
		List<Object> lista=new ArrayList<Object>();
		System.out.println("ci: "+req.getParameter("ci"));
		int idben=this.manejadorBeneficiarios.getIdBenByCi(req.getParameter("ci"));
		System.out.println("idben: "+idben);
		Persona BeneficiarioDatos=this.manejadorBeneficiarios.datosModificar(idben);
		List<?> ListaTelefono=this.manejadorBeneficiarios.ListaTelefonos(BeneficiarioDatos.getIdper());
		List<DocumentoBeneficiario> listaDocumentos=this.manejadorBeneficiarios.getDocumentos(idben);
		lista.add(BeneficiarioDatos);
		lista.add(ListaTelefono);
		lista.add(listaDocumentos);
		System.out.println("Persona:"+lista);
		return new ResponseEntity<List<?>>(lista,HttpStatus.OK);		
	}
	
	@RequestMapping(value="datos-modByCi")
	public ResponseEntity<List<?>> datosModByCi(HttpServletRequest req){
		List<Object> lista=new ArrayList<Object>();
		System.out.println("ci: "+req.getParameter("ci"));
		int idben=this.manejadorBeneficiarios.getIdBenByCi(req.getParameter("ci"));
		System.out.println("idben: "+idben);
		if (idben==0) {
			Persona per=this.manejadorBeneficiarios.getPersonaByCi(req.getParameter("ci"));; 
			List<?> ListaTelefono=this.manejadorBeneficiarios.ListaTelefonos(per.getIdper());
			lista.add(per);
			lista.add(ListaTelefono);
		} else {
			Persona BeneficiarioDatos=this.manejadorBeneficiarios.datosModificar(idben);
			List<?> ListaTelefono=this.manejadorBeneficiarios.ListaTelefonos(BeneficiarioDatos.getIdper());
			List<DocumentoBeneficiario> listaDocumentos=this.manejadorBeneficiarios.getDocumentos(idben);
			lista.add(BeneficiarioDatos);
			lista.add(ListaTelefono);
			lista.add(listaDocumentos);
		}

		System.out.println("Persona a Modificar:"+lista);
		return new ResponseEntity<List<?>>(lista,HttpStatus.OK);		
	}
	
	@RequestMapping(value="datos-modByCi_d1")
	public ResponseEntity<List<?>> datosModByCi_d1(HttpServletRequest req){
		List<Object> lista=new ArrayList<Object>();
		System.out.println("ci: "+req.getParameter("ci"));
		int idben=this.manejadorBeneficiarios.getIdBenByCi(req.getParameter("ci"));
		System.out.println("idben: "+idben);
		if (idben==0) {

			System.out.println("***********************************beneficiario nuevo");
			Persona per=this.manejadorBeneficiarios.getPersonaByCi_d1(req.getParameter("ci"));; 
			List<?> ListaTelefono=this.manejadorBeneficiarios.ListaTelefonos(per.getIdper());
			lista.add(per);
			lista.add(ListaTelefono);
		} else {//beneficiario existente
			System.out.println("*************************************beneficiario existe");
			Persona BeneficiarioDatos=this.manejadorBeneficiarios.datosModificar(idben);
			List<?> ListaTelefono=this.manejadorBeneficiarios.ListaTelefonos(BeneficiarioDatos.getIdper());
			List<DocumentoBeneficiario> listaDocumentos=this.manejadorBeneficiarios.getDocumentos(idben);
			lista.add(BeneficiarioDatos);
			lista.add(ListaTelefono);
			lista.add(listaDocumentos);
			
		}

		List<DocumentoBeneficiario> listaDocOrigin=this.manejadorBeneficiarios.listaDocumentos();
		lista.add(listaDocOrigin);
		
		System.out.println("Persona a Modificar:"+lista);
		return new ResponseEntity<List<?>>(lista,HttpStatus.OK);		
	}
	

	
	@RequestMapping(value="datos-modByCi_recalificacion")
	public ResponseEntity<List<?>> datos_recalificacion(HttpServletRequest req){
		List<Object> lista=new ArrayList<Object>();
		System.out.println("ci: "+req.getParameter("ci"));
		int idben=this.manejadorBeneficiarios.getIdBenByCi(req.getParameter("ci"));
		System.out.println("idben: "+idben);
		if (idben==0) {

			System.out.println("***********************************beneficiario nuevo");
			Persona per=this.manejadorBeneficiarios.getPersonaByCi_d1(req.getParameter("ci"));; 
			List<?> ListaTelefono=this.manejadorBeneficiarios.ListaTelefonos(per.getIdper());
			lista.add(per);
			lista.add(ListaTelefono);
		} else {//beneficiario existente
			System.out.println("*************************************beneficiario existe");
			Persona BeneficiarioDatos=this.manejadorBeneficiarios.datosModificar(idben);
			List<?> ListaTelefono=this.manejadorBeneficiarios.ListaTelefonos(BeneficiarioDatos.getIdper());
			List<DocumentoBeneficiario> listaDocumentos=this.manejadorBeneficiarios.getDocumentosRecalificacion(idben);
			lista.add(BeneficiarioDatos);
			lista.add(ListaTelefono);
			lista.add(listaDocumentos);
			
		}

		List<DocumentoBeneficiario> listaDocOrigin=this.manejadorBeneficiarios.documentosDocumentosRecalificacion();
		lista.add(listaDocOrigin);
		
		System.out.println("Persona a Modificar:"+lista);
		return new ResponseEntity<List<?>>(lista,HttpStatus.OK);		
	}
	
	
	
	
	@Transactional
	@RequestMapping(value="modificar")
	public Map<String, Object> modificar(HttpServletRequest req,HttpServletResponse res,Persona p,@RequestParam String ci){
		Map<String, Object> respuesta=new HashMap<String, Object>();
		String[] documentos=req.getParameterValues("documentos[]");
		String[] telefonos=req.getParameterValues("telefono[]");
		
		System.out.println("Persona"+p.toString());
		
		for (String i : documentos) {
			System.out.println("coddocb_modifocado: "+i);
		}
		for (String i : telefonos) {
			System.out.println("telefonos: "+i);
//			System.out.println("vacio? : "+i.equals(null));
			System.out.println("vacio? : "+!i.equals(""));
			System.out.println("vacio? : "+i.equals(""));
		}
		try {
			boolean consulta=this.manejadorBeneficiarios.modificar(req, p, documentos,telefonos);
			//boolean consulta=true;
			System.out.println(consulta);
			respuesta.put("estado", consulta);
		} catch (Exception e) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			respuesta.put("estado",false);
		}
		return respuesta;
	}
	@Transactional
	@RequestMapping(value="modificar_d")
	public Map<String, Object> modificar_d(HttpServletRequest req,HttpServletResponse res,Persona p,@RequestParam String ci){
		Map<String, Object> respuesta=new HashMap<String, Object>();
//		String[] documentos=req.getParameterValues("documentos[]");
		String[] telefonos=req.getParameterValues("telefono[]");
		
		System.out.println("Persona"+p.toString());
		
//		for (String i : documentos) {
//			System.out.println("coddocb_modifocado: "+i);
//		}
		for (String i : telefonos) {
			System.out.println("telefonos: "+i);
//			System.out.println("vacio? : "+i.equals(null));
			System.out.println("vacio? : "+!i.equals(""));
			System.out.println("vacio? : "+i.equals(""));
		}
		try {
			boolean consulta=this.manejadorBeneficiarios.modificar_d(req, p,telefonos);
//			boolean consulta=this.manejadorBeneficiarios.modificar_d(req, p, documentos,telefonos);
			//boolean consulta=true;
			System.out.println(consulta);
			respuesta.put("estado", consulta);
		} catch (Exception e) {
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
		String idben=req.getParameter("idben");
		System.out.println("idben_servidor: "+idben);
		Map<String, Object> respuesta=new HashMap<String, Object>();
		try {
			boolean resp=this.manejadorBeneficiarios.eliminar(Integer.parseInt(idben));
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
		String idben=req.getParameter("idben");
		System.out.println("idben_servidor: "+idben);
		Map<String, Object> respuesta=new HashMap<String, Object>();
		try {
			boolean resp=this.manejadorBeneficiarios.habilitar(Integer.parseInt(idben));
			respuesta.put("estado", resp);
		} catch (Exception e) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			respuesta.put("estado",false);
		}
		return new ResponseEntity<Map<String,Object>>(respuesta,HttpStatus.OK);	
	}
	

	
}
