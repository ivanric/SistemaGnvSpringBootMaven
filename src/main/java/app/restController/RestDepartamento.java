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
import app.service.DepartamentoService;

@RestController
@RequestMapping("/RestDepartamento")
public class RestDepartamento {
	@Autowired
	private DepartamentoService departamentoService;
	@GetMapping("")
    public ResponseEntity<?> getAll(@Param("search")String search,@Param("status")int status) { 
        try {
//        	System.out.println("list:"+paisService.findAll(valor_busqueda));
            return ResponseEntity.status(HttpStatus.OK).body(departamentoService.findAll(search,status));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"error\":\"Error. Por favor intente más tarde.\"}");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOne(@PathVariable Integer id){
        try {
            return ResponseEntity.status(HttpStatus.OK).body(departamentoService.findById(id));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"error\":\"Error. Por favor intente más tarde.\"}");
        }
    }

    @PostMapping("")
    public ResponseEntity<?> save(@RequestBody DepartamentoEntity entity){
        try {
            return ResponseEntity.status(HttpStatus.OK).body(departamentoService.save(entity));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\":\"Error. Por favor intente más tarde.\"}");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Integer id,@RequestBody DepartamentoEntity entity) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(departamentoService.update(id, entity));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\":\"Error. Por favor intente más tarde.\"}");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        try {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(departamentoService.delete(id));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\":\"Error. Por favor intente más tarde.\"}");
        }
    }
	
    @PostMapping("updateStatus")
    public ResponseEntity<?> updateStatus(@RequestBody DepartamentoEntity entity) {
        try {
        	System.out.println("Entidad:"+entity.toString());
        	departamentoService.updateStatus(entity.getEstado(), entity.getId());
        	DepartamentoEntity entity2=departamentoService.findById(entity.getId());
            return ResponseEntity.status(HttpStatus.OK).body(entity2);
        } catch (Exception e) {
        	System.out.println(e.getMessage());
        	e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\":\"Error. Por favor intente más tarde.\"}");
        }
    }
    
}
