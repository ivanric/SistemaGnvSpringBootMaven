package app.controlador;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping({"/Pais/"})
@Controller
public class ControladorPaises {
	@RequestMapping({"Gestion"})
	public String gestion(){
		return "nacionalidad/gestion";
	}
}
