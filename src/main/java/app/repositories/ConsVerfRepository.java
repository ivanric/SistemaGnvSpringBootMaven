package app.repositories;



import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import app.entity.CarguioEntity;


@Repository
public interface ConsVerfRepository extends JpaRepository<CarguioEntity, Integer>{
	@Query(value="select DISTINCT c.anio from vehiculo c where c.placa=?1  limit 1",nativeQuery = true)
	public Map<String, Object> get_anio_by_placa(String placa);
	
	
	@Query(value="select DISTINCT c.anio,c.costo from vehiculo c where c.placa=?1  limit 1",nativeQuery = true)
	public Map<String, Object> get_data_costo_anio_by_placa(String placa);
	
	
	@Query(value="SELECT DISTINCT \r\n"
			+ "SUM(ROUND(CAST(x.total AS NUMERIC),2)) as total,\r\n"
			+ "SUM(ROUND(CAST(x.volumen AS NUMERIC),2)) as volumen,\r\n"
			+ "SUM(ROUND(CAST(x.fondorotatorio AS NUMERIC),2)) as fondorotatorio \r\n"
			+ "FROM (\r\n"
			+ "SELECT\r\n"
			+ "c.total,\r\n"
			+ "c.volumen,\r\n"
			+ "c.fondorotatorio\r\n"
			+ "FROM carguio c\r\n"
			+ "WHERE \r\n"
			+ "upper(c.placa) LIKE concat('%',upper(?1),'%')\r\n"
			+ "AND (c.estado=?2 or ?2=-1)\r\n"
			+ ") as x",nativeQuery = true)
	public List<Map<String, Object>> get_data_consumo_anio_by_placa(String placa,int status);
} 