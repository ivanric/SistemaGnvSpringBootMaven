package app.controlador;



import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import app.models.Persona;
@RequestMapping({"/Recalificacion/"})
@Controller
public class ControladorRecalificacion{
	
	
	@RequestMapping({"Gestion"})
	public String gestion(HttpServletRequest request,Model m){
		HttpSession sesion=request.getSession(true);
		Persona xuser=(Persona) sesion.getAttribute("xusuario");
		try {
			if (xuser==null) {
				System.out.println("Entro");
				m.addAttribute("mensaje","Usuario no Autorizado..");
				return "principal/cerrarSession";
			} else {

			}
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("err");
			e.printStackTrace();
			m.addAttribute("mensaje","Usuario no Autorizado..");
			return "principal/cerrarSession";
		}
		return "recalificacion/gestion_d";
	}

	
	
	
}
