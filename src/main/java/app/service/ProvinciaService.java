package app.service;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import app.entity.ProvinciaEntity;
import app.repositories.ProvinciaRepository;

@Service
public class ProvinciaService implements GenericService<ProvinciaEntity>{
	@Autowired private ProvinciaRepository provinciaRepository;
	
	   @Override
	    @Transactional
	    public List<ProvinciaEntity> findAll(String clave,int estado) throws Exception {
	        try{
	            List<ProvinciaEntity> entities = provinciaRepository.findAll(clave,estado);
	            return entities;
	        } catch (Exception e){
	        	System.out.println(e.getMessage());
	            throw new Exception(e.getMessage());
	        }
	    }

	    @Override
	    @Transactional
	    public ProvinciaEntity findById(Integer id) throws Exception {
	        try{
	            Optional<ProvinciaEntity> entityOptional = provinciaRepository.findById(id);
	            return entityOptional.get();
	        } catch (Exception e){
	            throw new Exception(e.getMessage());
	        }
	    }

	    @Override
	    @Transactional
	    public ProvinciaEntity save(ProvinciaEntity entity) throws Exception {
	        try{
	        	System.out.println("Entity:"+entity.toString());
//	        	System.out.println("id:"+paisRepository.getIdPrimaryKey());
	        	int id=provinciaRepository.getIdPrimaryKey();
	        	entity.setId(id);
	        	System.out.println("EntityMod:"+entity.toString());
	            entity = provinciaRepository.save(entity);
	            return entity;
	        } catch (Exception e){
	            throw new Exception(e.getMessage());
	        }
	    }

	    @Override
	    @Transactional
	    public ProvinciaEntity update(Integer id, ProvinciaEntity entity) throws Exception {
	        try{
	            Optional<ProvinciaEntity> entityOptional = provinciaRepository.findById(id);
	            ProvinciaEntity entity2 = entityOptional.get();
	            entity2 = provinciaRepository.save(entity);
	            return entity2;
	        } catch (Exception e){
	            throw new Exception(e.getMessage());
	        }
	    }

	    @Override
	    @Transactional
	    public boolean delete(Integer id) throws Exception {
	        try{
	            if (provinciaRepository.existsById(id)){
	            	provinciaRepository.deleteById(id);
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
	        	provinciaRepository.updateStatus(status,id);

	        } catch (Exception e){
	        	System.out.println(e.getMessage());
	        	e.printStackTrace();
	            throw new Exception(e.getMessage());
	        }

		}
}
