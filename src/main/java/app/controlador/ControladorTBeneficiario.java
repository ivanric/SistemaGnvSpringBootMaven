package app.controlador;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import app.models.Persona;

@RequestMapping({"/TBeneficiario/"})
@Controller
public class ControladorTBeneficiario {
	
	@RequestMapping({"Gestion"})
	public String gestion(HttpServletRequest request,Model m){
		HttpSession sesion=request.getSession(true);
	
		Persona xuser=(Persona) sesion.getAttribute("xusuario");
		try {
			if (xuser==null) {
				m.addAttribute("mensaje","Usuario no Autorizado..");
				return "principal/cerrarSession";
			} else {

			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			m.addAttribute("mensaje","Usuario no Autorizado..");
			return "principal/cerrarSession";
		}  
		System.out.println("entro");
		return "transferenciaBeneficiario/gestion";
	}
	@RequestMapping({"modal-add"})
	public String modal_add(HttpServletRequest request,Model m){
		HttpSession sesion=request.getSession(true);
		Persona xuser=(Persona) sesion.getAttribute("xusuario");
		try {
			if (xuser==null) {
				m.addAttribute("mensaje","Usuario no Autorizado..");
				return "principal/cerrarSession";
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			m.addAttribute("mensaje","Usuario no Autorizado..");
			return "principal/cerrarSession";
		}
		return "transferenciaBeneficiario/modal-adicionar";
	}
	@RequestMapping({"modal-aprobacion"})
	public String modal_aprobacion(HttpServletRequest request,Model m){
		HttpSession sesion=request.getSession(true);
		Persona xuser=(Persona) sesion.getAttribute("xusuario");
		try {
			if (xuser==null) {
				m.addAttribute("mensaje","Usuario no Autorizado..");
				return "principal/cerrarSession";
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			m.addAttribute("mensaje","Usuario no Autorizado..");
			return "principal/cerrarSession";
		}
		return "transferenciaBeneficiario/modal-aprobacion";
	}
	
	@RequestMapping({"modal-ver"})
	public String modal_ver(HttpServletRequest request,Model m){
		HttpSession sesion=request.getSession(true);
		Persona xuser=(Persona) sesion.getAttribute("xusuario");
		try {
			if (xuser==null) {
				m.addAttribute("mensaje","Usuario no Autorizado..");
				return "principal/cerrarSession";
			} else {
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			m.addAttribute("mensaje","Usuario no Autorizado..");
			return "principal/cerrarSession";
		}
		return "transferenciaBeneficiario/modal-ver";
	}

	
}
