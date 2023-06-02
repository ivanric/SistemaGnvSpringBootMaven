package app.service;




import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.entity.CarguioEntity;
import app.repositories.ConsVerfRepository;

@Service
public  class ConsVerfService implements GenericService<CarguioEntity>{
	@Autowired private ConsVerfRepository consverfrepository;
	
   public Map<String, Object> get_anio_by_placa(String placa) throws Exception {
	      try{
	    	  Map<String, Object> entities = consverfrepository.get_anio_by_placa(placa);
	            return entities;
	        } catch (Exception e){
	        	System.out.println(e.getMessage());
	            throw new Exception(e.getMessage());
	        }
   }
   
   public Map<String, Object> get_data_costo_anio_by_placa(String placa) throws Exception {
	      try{
	    	  Map<String, Object> entities = consverfrepository.get_data_costo_anio_by_placa(placa);
	            return entities;
	        } catch (Exception e){
	        	System.out.println(e.getMessage());
	            throw new Exception(e.getMessage());
	        }
   }
   
   public List<Map<String, Object>> get_data_consumo_anio_by_placa(String placa,int status) throws Exception {
	      try{
	    	  List<Map<String, Object>> entities = consverfrepository.get_data_consumo_anio_by_placa(placa,status);
	            return entities;
	        } catch (Exception e){
	        	System.out.println(e.getMessage());
	            throw new Exception(e.getMessage());
	        }
}

@Override
public List<CarguioEntity> findAll(String search, int status) throws Exception {
	// TODO Auto-generated method stub
	return null;
}

@Override
public CarguioEntity findById(Integer id) throws Exception {
	// TODO Auto-generated method stub
	return null;
}

@Override
public CarguioEntity save(CarguioEntity entity) throws Exception {
	// TODO Auto-generated method stub
	return null;
}

@Override
public CarguioEntity update(Integer id, CarguioEntity entity) throws Exception {
	// TODO Auto-generated method stub
	return null;
}

@Override
public boolean delete(Integer id) throws Exception {
	// TODO Auto-generated method stub
	return false;
}

@Override
public void updateStatus(int status, int id) throws Exception {
	// TODO Auto-generated method stub
	
}

}
