package app.utilidades;

import java.sql.CallableStatement;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

public class Db_Coneccion {
	
	protected JdbcTemplate db;
	
//	protected Integer listlength = 10;
//	@Resource
//	protected DataSource ds;
	
//	protected CallableStatement cstmt;
//@Autowired
//public void setJdbcTemplate(DataSource dataSource) {
//	this.db = new JdbcTemplate(dataSource);
//	
//}
	  @Autowired
	   public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
	       this.db = jdbcTemplate;
	   }
 	
}
