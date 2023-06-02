package app.repositories;

import java.util.List;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


import app.entity.PaisEntity;
@Repository
public interface PaisRepository extends JpaRepository<PaisEntity, Integer>{
	@Query(value="SELECT max(idpais)+1  from pais",nativeQuery = true)
	public Integer getIdPrimaryKey();
	@Query(value = "SELECT  p.* FROM pais p WHERE upper(concat(p.idpais,p.nombre)) LIKE  concat('%',upper(?1),'%') and (p.estado=?2 or ?2=-1) ORDER BY p.idpais ASC LIMIT 10",nativeQuery = true)
	public List<PaisEntity> findAll(String clave,int estado); 
	@Modifying
	@Query(value="UPDATE pais SET estado= CASE ?1 WHEN 1 THEN 0 ELSE 1 END WHERE idpais=?2",nativeQuery = true)
	public void updateStatus(int status,int id);
} 