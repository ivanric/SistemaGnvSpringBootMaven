package app.repositories;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import app.entity.EstacionEntity;

@Repository
public interface EstacionRepository extends JpaRepository<EstacionEntity, Integer>{
	@Query(value="SELECT max(idest)+1  from estacion",nativeQuery = true)
	public Integer getIdPrimaryKey();
	@Query(value = "SELECT  p.* FROM estacion p WHERE upper(concat(p.idest,p.nombreestacion)) LIKE  concat('%',upper(?1),'%') and (p.estado=?2 or ?2=-1) ORDER BY p.idest ASC LIMIT 10",nativeQuery = true)
	public List<EstacionEntity> findAll(String clave,int estado); 
	@Modifying
	@Query(value="UPDATE estacion SET estado= CASE ?1 WHEN 1 THEN 0 ELSE 1 END WHERE idest=?2",nativeQuery = true)
	public void updateStatus(int status,int id);
	
	@Query(value = "select idest from estacion where RTRIM(LTRIM(nombreEstacion))= RTRIM(LTRIM(?1))",nativeQuery = true)
	public Map<String, Object> RETUNRIDEESS(String EESS);
	
	@Query(value = "select b.idben from beneficiario b,vehiculo v,benVehSolt  bvs where b.idben=bvs.idben AND v.placa=bvs.placa AND v.placa=?1",nativeQuery = true)
	public Integer RETUNR_IDBEN(String placa);
	@Query(value = "select p.razonsocial as Nombres from persona p,beneficiario b,vehiculo v,benVehSolt bvs \r\n"
			+ "where p.idper=b.idper AND bvs.idben=b.idben AND bvs.perteneceSiNo=1 AND v.placa=bvs.placa AND v.placa=?1",nativeQuery = true)
	public Map<String, Object> RETUNR_PLACA(String placa);
	@Query(value = "select v.placa from vehiculo v where v.placa=?1",nativeQuery = true)
	public Map<String, Object> RETUNR_PLACA_CARGUIO(String placa);
	
} 