package app.repositories;

import java.util.List;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import app.entity.DepartamentoEntity;
@Repository
public interface DepartamentoRepository extends JpaRepository<DepartamentoEntity, Integer>{
	@Query(value="SELECT max(iddep)+1  from departamento",nativeQuery = true)
	public Integer getIdPrimaryKey();
	@Query(value = "SELECT  p.* FROM departamento p WHERE upper(concat(p.iddep,p.nombre)) LIKE  concat('%',upper(?1),'%') and (p.estado=?2 or ?2=-1) ORDER BY p.idpais ASC LIMIT 10",nativeQuery = true)
	public List<DepartamentoEntity> findAll(String clave,int estado); 
	@Modifying
	@Query(value="UPDATE departamento SET estado= CASE ?1 WHEN 1 THEN 0 ELSE 1 END WHERE iddep=?2",nativeQuery = true)
	public void updateStatus(int status,int id);
} 