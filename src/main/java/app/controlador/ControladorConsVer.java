package app.controlador;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import app.models.Persona;

@RequestMapping({"/FondoRotatorio/"})
@Controller
public class ControladorConsVer {
	@RequestMapping({"consultar"})
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
		return "consulta/consver_perm";
	}

}
