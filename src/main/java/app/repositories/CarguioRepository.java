package app.repositories;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import app.entity.CarguioEntity;


@Repository
public interface CarguioRepository extends JpaRepository<CarguioEntity, Integer>{
//	@Query(value="SELECT max(idcarg)+1  from carguio",nativeQuery = true)
	@Query(value="select COALESCE(max(idcarg),0)+1 as idcarg from carguio",nativeQuery = true)
	public Integer getIdPrimaryKey();
	@Query(value = "SELECT  p.* FROM carguio p WHERE upper(concat(p.idcarg,p.nombre)) LIKE  concat('%',upper(?1),'%') and (p.estado=?2 or ?2=-1) ORDER BY p.idcarg ASC LIMIT 10",nativeQuery = true)
	public List<CarguioEntity> findAll(String clave,int estado);
	
//	@Query(value="SELECT \r\n"
//			+ "DISTINCT\r\n"
//			+ "TO_CHAR(c.fecha, 'MONTH') as Mes,\r\n"
//			+ "EXTRACT(MONTH FROM c.fecha) as MesNum, \r\n"
//			+ "EXTRACT(YEAR FROM c.fecha) as Anio,\r\n"
//			+ "to_char(c.fecha,'YYYY-MM-dd') as fecha,\r\n"
//			+ "e.nombreEstacion,\r\n"
//			+ "p.razonsocial as propietario \r\n"
//			+ "FROM carguio c,estacion e,persona p where e.idper=p.idper and e.idest=c.idest and upper(e.nombreEstacion) LIKE concat('%',upper(?1),'%')  and (p.estado=?2 or ?2=-1)\r\n"
//			+ "GROUP BY c.fecha,e.nombreEstacion,p.razonsocial",nativeQuery = true)
	
	
	
	@Query(value="select c.* from carguio c where ((EXTRACT(YEAR FROM c.fecha)=?1 or ?1=-1) or (c.anio=?1 or ?1=-1)) limit 1",nativeQuery = true)
	public Map<String, Object> Inforeessbyanio(int anio);
	@Query(value="SELECT DISTINCT e.* FROM estacion e,carguio c WHERE c.idest=e.idest and e.estado=1 and c.anio=?1",nativeQuery = true)
	public List<Map<String, Object>>  get_eess_active_by_anio(int anio);
	
	@Query(value="SELECT DISTINCT * FROM detalle_carguio_eess('',-1,?1,-1) as\r\n"
			+ "(\r\n"
			+ "	id INTEGER,\r\n"
			+ "	anio NUMERIC,\r\n"
			+ "	nombreestacion VARCHAR,\r\n"
			+ "	total NUMERIC,\r\n" 
			+ "	volumen NUMERIC,\r\n"
			+ "	fondorotatorio NUMERIC\r\n"
			+ ")",nativeQuery = true)
	public List<Map<String, Object>> listarAnioEG(int anio);
	@Query(value="SELECT DISTINCT * FROM detalle_carguio_eess_idest('',-1,?1,?2) as\r\n"
			+ "(\r\n"
			+ "	id INTEGER,\r\n"
			+ "	anio NUMERIC,\r\n"
			+ "	nombreestacion VARCHAR,\r\n"
			+ "	total NUMERIC,\r\n" 
			+ "	volumen NUMERIC,\r\n"
			+ "	fondorotatorio NUMERIC\r\n"
			+ ")",nativeQuery = true)
	public List<Map<String, Object>> listarAnioEESS(int anio,int id);
	
	@Query(value="SELECT DISTINCT * FROM detalle_carguio_eess_general_by_anio('',-1,?1,-1) as\r\n"
			+ "(\r\n"
			+ "	anio NUMERIC,\r\n"
			+ "	nombreestacion VARCHAR,\r\n"
			+ "	total NUMERIC,\r\n"
			+ "	volumen NUMERIC,\r\n"
			+ "	fondorotatorio NUMERIC\r\n"
			+ ")",nativeQuery = true)
	public List<Map<String, Object>> detalle_carguio_eess_general_by_anio(int anio);
	
	@Query(value="SELECT DISTINCT \r\n"
			+ "x.idest as id,\r\n"
			+ "x.Anio,\r\n"
			+ "--x.nombreEstacion,\r\n"
			+ "SUM(ROUND(CAST(x.total AS NUMERIC),2)) as total,\r\n"
			+ "SUM(ROUND(CAST(x.volumen AS NUMERIC),2)) as volumen,SUM(ROUND(CAST(x.fondorotatorio AS NUMERIC),2)) as fondorotatorio \r\n"
			+ "FROM (\r\n"
			+ "SELECT \r\n"
			+ "CASE WHEN EXTRACT(YEAR FROM c.fecha) <> NULL THEN (EXTRACT(YEAR FROM c.fecha)) ELSE c.anio END AS Anio,\r\n"
			+ "--CASE WHEN e.nombreestacion <> NULL THEN e.nombreestacion ELSE NULL END as nombreEstacion,\r\n"
			+ "c.total,\r\n"
			+ "c.volumen,\r\n"
			+ "c.fondorotatorio,\r\n"
			+ "CASE WHEN c.idest <> NULL THEN c.idest ELSE -1 END as idest\r\n"
			+ "\r\n"
			+ "FROM carguio c\r\n"
			+ "--,estacion e\r\n"
			+ "--,persona p \r\n"
			+ "WHERE \r\n"
			+ "--e.idper=p.idper and\r\n"
			+ "((EXTRACT(YEAR FROM c.fecha)=?1 or ?1=-1) or (c.anio=?1 or ?1=-1))\r\n"
			+ ") as x\r\n"
			+ "GROUP BY  x.Anio,\r\n"
			+ "x.idest\r\n"
			+ "--,x.nombreEstacion\r\n"
			+ "ORDER BY x.anio ASC",nativeQuery = true)
	public List<Map<String, Object>> listarAnioG(int anio);
	
	@Query(value="SELECT \r\n"
			+ "DISTINCT \r\n"
			+ "x.idest as id,\r\n"
			+ "x.Anio,\r\n"
			+ "x.nombreEstacion,\r\n"
			+ "SUM(ROUND(CAST(x.total AS NUMERIC),2)) as total,\r\n"
			+ "SUM(ROUND(CAST(x.volumen AS NUMERIC),2)) as volumen,SUM(ROUND(CAST(x.fondorotatorio AS NUMERIC),2)) as fondorotatorio\r\n"
			+ "\r\n"
			+ "FROM (\r\n"
			+ "SELECT \r\n"
			+ "--DISTINCT\r\n"
			+ "CASE WHEN EXTRACT(YEAR FROM c.fecha) <> NULL THEN (EXTRACT(YEAR FROM c.fecha)) ELSE c.anio END AS Anio,\r\n"
			+ "CASE WHEN e.nombreestacion <> NULL THEN NULL ELSE e.nombreestacion END as nombreEstacion,\r\n"
			+ "c.total,\r\n"
			+ "c.volumen,\r\n"
			+ "c.fondorotatorio,\r\n"
			+ "CASE WHEN c.idest <> NULL THEN -1 ELSE c.idest END as idest\r\n"
			+ "FROM carguio c,estacion e\r\n"
			+ "WHERE \r\n"
			+ "e.idest=c.idest AND \r\n"
			+ "((EXTRACT(YEAR FROM c.fecha)=?1 or ?1=-1) or (c.anio=?1 or ?1=-1))\r\n"
			+ ") as x\r\n"
			+ "GROUP BY  x.Anio,x.idest,x.nombreEstacion\r\n"
			+ "ORDER BY x.anio ASC",nativeQuery = true)
	public List<Map<String, Object>> listarAnioE(int anio);
	
	@Query(value="SELECT DISTINCT x.idest as id,x.mes,x.Anio,x.nombreEstacion,SUM(ROUND(CAST(x.volumen AS NUMERIC),2)) as volumen,SUM(ROUND(CAST(x.fondorotatorio AS NUMERIC),2)) as fondorotatorio FROM (\r\n"
			+ "SELECT \r\n"
			+ "TO_CHAR(c.fecha, 'TMMONTH') as Mes,\r\n"
			+ "EXTRACT(MONTH FROM c.fecha) as MesNum,\r\n"
			+ "EXTRACT(YEAR FROM c.fecha) as Anio,\r\n"
			+ "--to_char(c.fecha,'YYYY-MM-dd') as fecha,\r\n"
			+ "e.nombreEstacion,\r\n"
			+ "p.razonsocial as propietario,\r\n"
			+ "c.volumen,\r\n"
			+ "c.fondorotatorio,\r\n"
			+ "e.idest\r\n"
			+ "FROM carguio c,estacion e,persona p \r\n"
			+ "WHERE \r\n"
			+ "e.idper=p.idper \r\n"
			+ "AND e.idest=c.idest\r\n"
			+ "AND upper(e.nombreEstacion) LIKE concat('%',upper(?1),'%')\r\n"
			+ "AND (p.estado=?2 or ?2=-1)\r\n"
			+ "--AND EXTRACT(MONTH FROM c.fecha)=1\r\n"
			+ "AND EXTRACT(YEAR FROM c.fecha)=?3\r\n"
			+ "AND e.idest=?4\r\n"
			+ ") as x\r\n"
			+ "GROUP BY  x.idest,x.mes,x.Anio,x.nombreEstacion \r\n"
			+ "ORDER BY x.anio ASC",nativeQuery = true)
	public List<Map<String, Object>> listarMesbyId(String clave,int estado,int anio,int id);
	
	
	@Query(value="SELECT DISTINCT * FROM detalle_carguio_eess_by_id(?1,?2,?3,?4) as\r\n"
			+ "(\r\n"
			+ "	id INTEGER,\r\n"
			+ "	mes VARCHAR,\r\n"
			+ "	anio NUMERIC,\r\n"
			+ "	nombreestacion VARCHAR,\r\n"
			+ "	total NUMERIC,\r\n"
			+ "	volumen NUMERIC,\r\n"
			+ "	fondorotatorio NUMERIC\r\n"
			+ ")",nativeQuery = true)
	public List<Map<String, Object>> detalle_carguio_eess_by_id(String clave,int estado,int anio,int id);
	
	@Query(value="SELECT DISTINCT * FROM detalle_carguio_eess_by_id_g(?1,?2,?3,?4) as\r\n"
			+ "(\r\n"
			+ "	id INTEGER,\r\n"
			+ "	mes VARCHAR,\r\n"
			+ "	anio NUMERIC,\r\n"
			+ "	nombreestacion VARCHAR,\r\n"
			+ "	total NUMERIC,\r\n"
			+ "	volumen NUMERIC,\r\n"
			+ "	fondorotatorio NUMERIC\r\n"
			+ ")",nativeQuery = true)
	public List<Map<String, Object>> detalle_carguio_eess_by_id_g(String clave,int estado,int anio,int id);
	
	@Query(value="SELECT DISTINCT * FROM detalle_carguio_eess_by_id_e(?1,?2,?3,?4) as\r\n"
			+ "(\r\n"
			+ "	id INTEGER,\r\n"
			+ "	mes VARCHAR,\r\n"
			+ "	anio NUMERIC,\r\n"
			+ "	nombreestacion VARCHAR,\r\n"
			+ "	total NUMERIC,\r\n"
			+ "	volumen NUMERIC,\r\n"
			+ "	fondorotatorio NUMERIC\r\n"
			+ ")",nativeQuery = true)
	public List<Map<String, Object>> detalle_carguio_eess_by_id_e(String clave,int estado,int anio,int id);
	
//	@Query(value="COPY public.carguio FROM ?1 \r\n"
//			+ "WITH (FORMAT csv, HEADER true, DELIMITER '	')",nativeQuery = true)
	@Query(value="COPY carguio FROM ?1 WITH (FORMAT csv, HEADER true, DELIMITER '	')",nativeQuery = true)
	public boolean guardar(String link_text);
	
	
//	@Query(value="SELECT \r\n"
//			+ "SUM(c.volumen) as Volumen,\r\n"
//			+ "SUM(c.fondoRotatorio) as FondoRotatorio \r\n"
//			+ "FROM carguio c WHERE EXTRACT(MONTH FROM c.fecha)=?1 AND EXTRACT(YEAR FROM c.fecha)=?2",nativeQuery = true)
//	public Map<String, Object> FONDO_ROTATORIO(int Mes,int Anio);
	@Modifying
	@Query(value="UPDATE carguio SET estado= CASE ?1 WHEN 1 THEN 0 ELSE 1 END WHERE idcarg=?2",nativeQuery = true) 
	public void updateStatus(int status,int id);
} 