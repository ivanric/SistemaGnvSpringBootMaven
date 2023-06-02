package app.restController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import app.entity.DepartamentoEntity;
import app.entity.EstacionEntity;
import app.entity.ProvinciaEntity;
import app.service.DepartamentoService;
import app.service.EstacionService;
import app.service.ProvinciaService;

@RestController
@RequestMapping("/RestEstacion")
public class RestEstacion {
	@Autowired
	private EstacionService estacionService;
	@GetMapping("")
    public ResponseEntity<?> getAll(@Param("search")String search,@Param("status")int status) { 
        try {
//        	System.out.println("list:"+paisService.findAll(valor_busqueda));
            return ResponseEntity.status(HttpStatus.OK).body(estacionService.findAll(search,status));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"error\":\"Error. Por favor intente más tarde.\"}");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOne(@PathVariable Integer id){
        try {
            return ResponseEntity.status(HttpStatus.OK).body(estacionService.findById(id));
        } catch (Exception e) {
        	
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"error\":\"Error. Por favor intente más tarde.\"}");
        }
    }

    @PostMapping("")
    public ResponseEntity<?> save(@RequestBody EstacionEntity entity){
        try {
            return ResponseEntity.status(HttpStatus.OK).body(estacionService.save(entity));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\":\"Error. Por favor intente más tarde.\"}");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Integer id,@RequestBody EstacionEntity entity) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(estacionService.update(id, entity));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\":\"Error. Por favor intente más tarde.\"}");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        try {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(estacionService.delete(id));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\":\"Error. Por favor intente más tarde.\"}");
        }
    }
	
    @PostMapping("updateStatus")
    public ResponseEntity<?> updateStatus(@RequestBody EstacionEntity entity) {
        try {
        	System.out.println("Entidad:"+entity.toString());
        	estacionService.updateStatus(entity.getEstado(), entity.getId());
        	EstacionEntity entity2=estacionService.findById(entity.getId());
            return ResponseEntity.status(HttpStatus.OK).body(entity2);
        } catch (Exception e) {
        	System.out.println(e.getMessage());
        	e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\":\"Error. Por favor intente más tarde.\"}");
        }
    }
    @GetMapping("/getIdEstacion")
    public ResponseEntity<?> getIdEstacion(@Param("estacion")String estacion) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(estacionService.RETUNRIDEESS(estacion));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\":\"Error. Por favor intente más tarde.\"}");
        }
    }
    
    
    
}
