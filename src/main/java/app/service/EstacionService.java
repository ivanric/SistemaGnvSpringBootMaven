package app.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.entity.EstacionEntity;
import app.repositories.EstacionRepository;

@Service
public class EstacionService implements GenericService<EstacionEntity>{
//	@Autowired private EstacionRepository estacionRepository;
	@Autowired
	private EstacionRepository estacionRepository;
	   @Override
	    @Transactional
	    public List<EstacionEntity> findAll(String clave,int estado) throws Exception {
	        try{
	            List<EstacionEntity> entities = estacionRepository.findAll(clave,estado);
	            return entities;
	        } catch (Exception e){
	        	System.out.println(e.getMessage());
	            throw new Exception(e.getMessage());
	        }
	    }

	    @Override
	    @Transactional
	    public EstacionEntity findById(Integer id) throws Exception {
	        try{
	            Optional<EstacionEntity> entityOptional = estacionRepository.findById(id);
	            return entityOptional.get();
	        } catch (Exception e){
	            throw new Exception(e.getMessage());
	        }
	    }

	    @Override
	    @Transactional
	    public EstacionEntity save(EstacionEntity entity) throws Exception {
	        try{
	        	System.out.println("Entity:"+entity.toString());
//	        	System.out.println("id:"+paisRepository.getIdPrimaryKey());
	        	int id=estacionRepository.getIdPrimaryKey();
	        	entity.setId(id);
	        	System.out.println("EntityMod:"+entity.toString());
	            entity = estacionRepository.save(entity);
	            return entity;
	        } catch (Exception e){
	            throw new Exception(e.getMessage());
	        }
	    }

	    @Override
	    @Transactional
	    public EstacionEntity update(Integer id, EstacionEntity entity) throws Exception {
	        try{
	            Optional<EstacionEntity> entityOptional = estacionRepository.findById(id);
	            EstacionEntity entity2 = entityOptional.get();
	            entity2 = estacionRepository.save(entity);
	            return entity2;
	        } catch (Exception e){
	            throw new Exception(e.getMessage());
	        }
	    }

	    @Override
	    @Transactional
	    public boolean delete(Integer id) throws Exception {
	        try{
	            if (estacionRepository.existsById(id)){
	            	estacionRepository.deleteById(id);
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
	        	estacionRepository.updateStatus(status,id);

	        } catch (Exception e){
	        	System.out.println(e.getMessage());
	        	e.printStackTrace();
	            throw new Exception(e.getMessage());
	        }

		}
		
	    @Transactional
	    public Integer RETUNRIDEESS(String EESS) throws Exception {
	    	System.out.println("EStacionParam Name:"+EESS);
	    	int id=0;
	        try{
	        	
	        	Map<String, Object> id_map=estacionRepository.RETUNRIDEESS(EESS);
	        	System.out.println("id_map:"+id_map);
	        	if (!id_map.isEmpty()) {
					id=(int) id_map.get("idest");
				}else {
					id=0;
				}
	        	System.out.println("idEESS:"+id);
	            return id;
	        } catch (Exception e){
	        	e.printStackTrace();
	            System.out.println("err_est:"+e.getMessage());
	            return 0;
	        }
	    }
	    
	    @Transactional
	    public Integer RETUNR_IDBEN(String placa) throws Exception {
	        try{
	        	
	        	int id=estacionRepository.RETUNR_IDBEN(placa);
	        	
	            return id;
	        } catch (Exception e){
	        	e.printStackTrace();
	            System.out.println(e.getMessage());
	            return 0;
	        }
	    }
	    @Transactional
	    public Map<String, Object> RETUNR_PLACA(String placa) throws Exception {
	        try{
	            return estacionRepository.RETUNR_PLACA_CARGUIO(placa);
	        } catch (Exception e){
//	            throw new Exception(e.getMessage());
	        	e.printStackTrace();
	            System.out.println("placa err:"+e.getMessage());
	            return null;
	        }
	    }
}
