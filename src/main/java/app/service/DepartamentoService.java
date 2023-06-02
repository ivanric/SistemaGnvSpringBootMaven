package app.service;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import app.entity.DepartamentoEntity;
import app.entity.PaisEntity;
import app.repositories.DepartamentoRepository;
import app.repositories.PaisRepository;

@Service
public class DepartamentoService implements GenericService<DepartamentoEntity>{
	@Autowired private DepartamentoRepository departamentoRepository;
	
	   @Override
	    @Transactional
	    public List<DepartamentoEntity> findAll(String clave,int estado) throws Exception {
	        try{
	            List<DepartamentoEntity> entities = departamentoRepository.findAll(clave,estado);
	            return entities;
	        } catch (Exception e){
	        	System.out.println(e.getMessage());
	            throw new Exception(e.getMessage());
	        }
	    }

	    @Override
	    @Transactional
	    public DepartamentoEntity findById(Integer id) throws Exception {
	        try{
	            Optional<DepartamentoEntity> entityOptional = departamentoRepository.findById(id);
	            return entityOptional.get();
	        } catch (Exception e){
	            throw new Exception(e.getMessage());
	        }
	    }

	    @Override
	    @Transactional
	    public DepartamentoEntity save(DepartamentoEntity entity) throws Exception {
	        try{
	        	System.out.println("Entity:"+entity.toString());
//	        	System.out.println("id:"+paisRepository.getIdPrimaryKey());
	        	int id=departamentoRepository.getIdPrimaryKey();
	        	entity.setId(id);
	        	System.out.println("EntityMod:"+entity.toString());
	            entity = departamentoRepository.save(entity);
	            return entity;
	        } catch (Exception e){
	            throw new Exception(e.getMessage());
	        }
	    }

	    @Override
	    @Transactional
	    public DepartamentoEntity update(Integer id, DepartamentoEntity entity) throws Exception {
	        try{
	            Optional<DepartamentoEntity> entityOptional = departamentoRepository.findById(id);
	            DepartamentoEntity entity2 = entityOptional.get();
	            entity2 = departamentoRepository.save(entity);
	            return entity2;
	        } catch (Exception e){
	            throw new Exception(e.getMessage());
	        }
	    }

	    @Override
	    @Transactional
	    public boolean delete(Integer id) throws Exception {
	        try{
	            if (departamentoRepository.existsById(id)){
	            	departamentoRepository.deleteById(id);
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
	        	departamentoRepository.updateStatus(status,id);

	        } catch (Exception e){
	        	System.out.println(e.getMessage());
	        	e.printStackTrace();
	            throw new Exception(e.getMessage());
	        }

		}
}
