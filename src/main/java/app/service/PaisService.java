package app.service;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import app.entity.PaisEntity;
import app.repositories.PaisRepository;

@Service
public class PaisService implements GenericService<PaisEntity>{
	@Autowired private PaisRepository paisRepository;
	
	   @Override
	    @Transactional
	    public List<PaisEntity> findAll(String clave,int estado) throws Exception {
	        try{
//	            List<PaisEntity> entities = paisRepository.findAll(Sort.by("idpais").ascending());
	            List<PaisEntity> entities = paisRepository.findAll(clave,estado);
	            return entities;
	        } catch (Exception e){
	        	System.out.println(e.getMessage());
	            throw new Exception(e.getMessage());
	        }
	    }

	    @Override
	    @Transactional
	    public PaisEntity findById(Integer id) throws Exception {
	        try{
	            Optional<PaisEntity> entityOptional = paisRepository.findById(id);
	            return entityOptional.get();
	        } catch (Exception e){
	            throw new Exception(e.getMessage());
	        }
	    }

	    @Override
	    @Transactional
	    public PaisEntity save(PaisEntity entity) throws Exception {
	        try{
	        	System.out.println("PaisEntity:"+entity.toString());
//	        	System.out.println("id:"+paisRepository.getIdPrimaryKey());
	        	int id=paisRepository.getIdPrimaryKey();
	        	entity.setId(id);
	        	System.out.println("PaisEntityMod:"+entity.toString());
	            entity = paisRepository.save(entity);
	            return entity;
	        } catch (Exception e){
	            throw new Exception(e.getMessage());
	        }
	    }

	    @Override
	    @Transactional
	    public PaisEntity update(Integer id, PaisEntity entity) throws Exception {
	        try{
	            Optional<PaisEntity> entityOptional = paisRepository.findById(id);
	            PaisEntity persona = entityOptional.get();
	            persona = paisRepository.save(entity);
	            return persona;
	        } catch (Exception e){
	            throw new Exception(e.getMessage());
	        }
	    }

	    @Override
	    @Transactional
	    public boolean delete(Integer id) throws Exception {
	        try{
	            if (paisRepository.existsById(id)){
	            	paisRepository.deleteById(id);
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
	        	paisRepository.updateStatus(status,id);

	        } catch (Exception e){
	        	System.out.println(e.getMessage());
	        	e.printStackTrace();
	            throw new Exception(e.getMessage());
	        }

		}
}
