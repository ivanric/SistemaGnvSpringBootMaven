package app.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.entity.CarguioEntity;
import app.repositories.CarguioRepository;

@Service
public class CarguioService implements GenericService<CarguioEntity>{
	@Autowired private CarguioRepository carguioRepository;
	
	   @Override
	    @Transactional
	    public List<CarguioEntity> findAll(String clave,int estado) throws Exception {
	        try{
	            List<CarguioEntity> entities = carguioRepository.findAll(clave,estado);
	            return entities;
	        } catch (Exception e){
	        	System.out.println(e.getMessage());
	            throw new Exception(e.getMessage());
	        }
	    }

	   public Map<String, Object> Inforeessbyanio(int anio) throws Exception {
		      try{
		    	  Map<String, Object> entities = carguioRepository.Inforeessbyanio(anio);
		            return entities;
		        } catch (Exception e){
		        	System.out.println(e.getMessage());
		            throw new Exception(e.getMessage());
		        }
	   }
	   public List<Map<String, Object>>  get_eess_active_by_anio(int anio) throws Exception {
		      try{
		    	  String sql="";
		    	  List<Map<String, Object>> entities = carguioRepository.get_eess_active_by_anio(anio);
		            return entities;
		        } catch (Exception e){
		        	e.printStackTrace();
		        	System.out.println("mje2"+e.getMessage());
		            throw new Exception(e.getMessage());
		        }
	   } 
	   public List<Map<String, Object>>  detalle_carguio_eess_general_by_anio(int anio) throws Exception {
		      try{
		    	  List<Map<String, Object>> entities = carguioRepository.detalle_carguio_eess_general_by_anio(anio);
		            return entities;
		        } catch (Exception e){
		        	e.printStackTrace();
		        	System.out.println("mje2"+e.getMessage());
		            throw new Exception(e.getMessage());
		        }
	   } 
	   public List<Map<String, Object>> listarAnioEG(int anio) throws Exception {
		      try{
		    	  List<Map<String, Object>> entities = carguioRepository.listarAnioEG(anio);
		            return entities;
		        } catch (Exception e){
		        	e.printStackTrace();
		        	System.out.println("mje1"+e.getMessage());
		            throw new Exception(e.getMessage());
		        }
	   }
	   public List<Map<String, Object>> listarAnioEESS(int anio,int id) throws Exception {
		      try{
		    	  List<Map<String, Object>> entities = carguioRepository.listarAnioEESS(anio,id);
		            return entities;
		        } catch (Exception e){
		        	e.printStackTrace();
		        	System.out.println("mje1"+e.getMessage());
		            throw new Exception(e.getMessage());
		        }
	   }
	   
	   
	   public List<Map<String, Object>> listarAnioG(int anio) throws Exception {
	      try{
	    	  List<Map<String, Object>> entities = carguioRepository.listarAnioG(anio);
	            return entities;
	        } catch (Exception e){
	        	System.out.println(e.getMessage());
	            throw new Exception(e.getMessage());
	        }
	   }
	   public List<Map<String, Object>> listarAnioE(int anio) throws Exception {
		      try{
		    	  List<Map<String, Object>> entities = carguioRepository.listarAnioE(anio);
		            return entities;
		        } catch (Exception e){
		        	System.out.println(e.getMessage());
		            throw new Exception(e.getMessage());
		        }
		   }
	   /*
	   public List<Map<String, Object>> listarEESSBCK(String clave,int estado,int anio,int id) throws Exception {
		      try{
		    	  List<Map<String, Object>> entities = carguioRepository.listarMesbyId(clave,estado,anio,id);
		            return entities;
		        } catch (Exception e){
		        	System.out.println(e.getMessage());
		            throw new Exception(e.getMessage());
		        }
	   }*/
	   public List<Map<String, Object>> detalle_carguio_eess_by_id(String clave,int estado,int anio,int id) throws Exception {
		      try{
		    	  List<Map<String, Object>> entities = carguioRepository.detalle_carguio_eess_by_id(clave,estado,anio,id);
		            return entities;
		        } catch (Exception e){
		        	System.out.println(e.getMessage());
		            throw new Exception(e.getMessage());
		        }
	   }
	   public List<Map<String, Object>> detalle_carguio_eess_by_id_g(String clave,int estado,int anio,int id) throws Exception {
		      try{
		    	  List<Map<String, Object>> entities = carguioRepository.detalle_carguio_eess_by_id_g(clave,estado,anio,id);
		            return entities;
		        } catch (Exception e){
		        	System.out.println(e.getMessage());
		            throw new Exception(e.getMessage());
		        }
	   }
	   public List<Map<String, Object>> detalle_carguio_eess_by_id_e(String clave,int estado,int anio,int id) throws Exception {
		      try{
		    	  List<Map<String, Object>> entities = carguioRepository.detalle_carguio_eess_by_id_e(clave,estado,anio,id);
		            return entities;
		        } catch (Exception e){
		        	System.out.println(e.getMessage());
		            throw new Exception(e.getMessage());
		        }
	   }
//	   public Map<String, Object> FONDO_ROTATORIO(int Mes,int Anio) throws Exception {
//		      try{
//		    	  Map<String, Object>entities = carguioRepository.FONDO_ROTATORIO(Mes,Anio);
//		            return entities;
//		        } catch (Exception e){
//		        	System.out.println(e.getMessage());
//		            throw new Exception(e.getMessage());
//		        }
//	   }
	    @Override
	    @Transactional
	    public CarguioEntity findById(Integer id) throws Exception {
	        try{
	            Optional<CarguioEntity> entityOptional = carguioRepository.findById(id);
	            return entityOptional.get();
	        } catch (Exception e){
	            throw new Exception(e.getMessage());
	        }
	    }

	    @Override
	    @Transactional
	    public CarguioEntity save(CarguioEntity entity) throws Exception {
	        try{
	        	System.out.println("Entity:"+entity.toString());
//	        	System.out.println("id:"+paisRepository.getIdPrimaryKey());
	        	int id=carguioRepository.getIdPrimaryKey();
	        	entity.setId(id);
	        	System.out.println("EntityMod:"+entity.toString());
	            entity = carguioRepository.save(entity);
	            return entity;
	        } catch (Exception e){
	            throw new Exception(e.getMessage());
	        }
	    }

	    @Override
	    @Transactional
	    public CarguioEntity update(Integer id, CarguioEntity entity) throws Exception {
	        try{
	            Optional<CarguioEntity> entityOptional = carguioRepository.findById(id);
	            CarguioEntity entity2 = entityOptional.get();
	            entity2 = carguioRepository.save(entity);
	            return entity2;
	        } catch (Exception e){
	            throw new Exception(e.getMessage());
	        }
	    }

	    @Override
	    @Transactional
	    public boolean delete(Integer id) throws Exception {
	        try{
	            if (carguioRepository.existsById(id)){
	            	carguioRepository.deleteById(id);
	                return true;
	            } else {
	                throw new Exception();
	            }
	        } catch (Exception e){
	            throw new Exception(e.getMessage());
	        }
	    }

		@Override
		@Transactional
		public void updateStatus(int status,int id) throws Exception {
			// TODO Auto-generated method stub
	        try{
	        	System.out.println("estado:"+status+" id:"+id);
	        	carguioRepository.updateStatus(status,id);

	        } catch (Exception e){
	        	System.out.println(e.getMessage());
	        	e.printStackTrace();
	            throw new Exception(e.getMessage());
	        }

		}
		
		
	    @Transactional
	    public Integer getIdPrimaryKey() throws Exception {
	        try{
	        	
	        	int id=carguioRepository.getIdPrimaryKey();
	        	
	            return id;
	        } catch (Exception e){
	        	e.printStackTrace();
	        	System.out.println(e.getMessage());
	        	return 0;
//	            throw new Exception(e.getMessage());
	        }
	    }
	    
	    @Transactional
	    public boolean guardar(String link) throws Exception {
	        try{
	            if ( carguioRepository.guardar(link)){
	                return true;
	            } else {
	                throw new Exception();
	            }
	        } catch (Exception e){
	            throw new Exception(e.getMessage());
	        }
	    }
}
