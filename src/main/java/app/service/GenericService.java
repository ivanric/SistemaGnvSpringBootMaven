package app.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public interface GenericService<E> {
    public List<E> findAll(String search,int status) throws Exception;
    
    public E findById(Integer id) throws Exception;
    public E save(E entity) throws Exception;
    public E update(Integer id, E entity) throws Exception;
    public boolean delete(Integer id) throws Exception;
    public void updateStatus(int status,int id) throws Exception;
}