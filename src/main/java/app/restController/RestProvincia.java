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
import app.entity.ProvinciaEntity;
import app.service.DepartamentoService;
import app.service.ProvinciaService;

@RestController
@RequestMapping("/RestProvincia")
public class RestProvincia {
	@Autowired
	private ProvinciaService provinciaService;
	@GetMapping("")
    public ResponseEntity<?> getAll(@Param("search")String search,@Param("status")int status) { 
        try {
//        	System.out.println("list:"+paisService.findAll(valor_busqueda));
            return ResponseEntity.status(HttpStatus.OK).body(provinciaService.findAll(search,status));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"error\":\"Error. Por favor intente más tarde.\"}");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOne(@PathVariable Integer id){
        try {
            return ResponseEntity.status(HttpStatus.OK).body(provinciaService.findById(id));
        } catch (Exception e) {
        	
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"error\":\"Error. Por favor intente más tarde.\"}");
        }
    }

    @PostMapping("")
    public ResponseEntity<?> save(@RequestBody ProvinciaEntity entity){
        try {
            return ResponseEntity.status(HttpStatus.OK).body(provinciaService.save(entity));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\":\"Error. Por favor intente más tarde.\"}");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Integer id,@RequestBody ProvinciaEntity entity) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(provinciaService.update(id, entity));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\":\"Error. Por favor intente más tarde.\"}");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        try {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(provinciaService.delete(id));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\":\"Error. Por favor intente más tarde.\"}");
        }
    }
	
    @PostMapping("updateStatus")
    public ResponseEntity<?> updateStatus(@RequestBody ProvinciaEntity entity) {
        try {
        	System.out.println("Entidad:"+entity.toString());
        	provinciaService.updateStatus(entity.getEstado(), entity.getId());
        	ProvinciaEntity entity2=provinciaService.findById(entity.getId());
            return ResponseEntity.status(HttpStatus.OK).body(entity2);
        } catch (Exception e) {
        	System.out.println(e.getMessage());
        	e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\":\"Error. Por favor intente más tarde.\"}");
        }
    }
    
}
