package app.repositories;

import java.util.List;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import app.entity.ProvinciaEntity;
@Repository
public interface ProvinciaRepository extends JpaRepository<ProvinciaEntity, Integer>{
	@Query(value="SELECT max(idprov)+1  from provincia",nativeQuery = true)
	public Integer getIdPrimaryKey();
	@Query(value = "SELECT  p.* FROM provincia p WHERE upper(concat(p.idprov,p.nombre)) LIKE  concat('%',upper(?1),'%') and (p.estado=?2 or ?2=-1) ORDER BY p.idprov ASC LIMIT 10",nativeQuery = true)
	public List<ProvinciaEntity> findAll(String clave,int estado); 
	@Modifying
	@Query(value="UPDATE provincia SET estado= CASE ?1 WHEN 1 THEN 0 ELSE 1 END WHERE idprov=?2",nativeQuery = true)
	public void updateStatus(int status,int id);
} 