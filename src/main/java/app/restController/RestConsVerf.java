package app.restController;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;



import app.entity.CarguioEntity;

import app.manager.ManejadorCarguios;

import app.service.CarguioService;
import app.service.ConsVerfService;
import app.service.EstacionService;
import app.utilidades.CargarArchivos;
import app.utilidades.LectorCSV;

@RestController
@RequestMapping("/RestConsVerf")
public class RestConsVerf {
	@Autowired
	private ConsVerfService consVerfService;
	@Autowired
	private EstacionService estacionService;
	@Autowired ManejadorCarguios manejadorCarguios;
	
	@RequestMapping("get_anio_by_placa")
    public @ResponseBody Map<?, ?>  get_anio_by_placa(HttpServletRequest request)throws IOException { 
		Map<String, Object> Data = new HashMap<String, Object>();
		try {
        	String placa=request.getParameter("placa");
        	System.out.println("placa_V:"+placa);
//        	
        	Data.put("anio",this.consVerfService.get_anio_by_placa(placa).get("anio"));

    		System.out.println("listaA: "+Data.toString());

        } catch (Exception e) {
        	e.printStackTrace();
        	System.out.println(e.getMessage());
        }
    	return Data;
    }
	
	@RequestMapping("get_data_consumo_anio_by_placa")
    public @ResponseBody Map<?, ?>  get_data_consumo_anio_by_placa(HttpServletRequest request)throws IOException { 
		Map<String, Object> Data = new HashMap<String, Object>();
		try {
        	String placa=request.getParameter("placa");
        	System.out.println("placa_V:"+placa);
        	Map<String, Object> mapa1=this.consVerfService.get_data_costo_anio_by_placa(placa);
        	List<Map<String, Object>> list=this.consVerfService.get_data_consumo_anio_by_placa(placa,1);
        	Data.put("vehiculo",mapa1);
        	Data.put("vehiculo_consumo",list);

    		System.out.println("listaA: "+Data.toString());

        } catch (Exception e) {
        	e.printStackTrace();
        	System.out.println(e.getMessage());
        }
    	return Data;
    }
	

    
}
