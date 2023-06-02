package app.dao;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import app.rowmapper.Telefono;
import app.rowmapper.TelefonoRowMapper;

@Transactional
@Repository
public class TelefonoDAO  implements ITelefonoDAO{
	private JdbcTemplate db;
	@Autowired
	public void setDataSource(DataSource dataSource){
		db = new JdbcTemplate(dataSource);
	}

	@Override
	public List<Telefono> getAllTelefonosById(int idper) {
		System.out.println("idper_repositorio:"+idper);
		String sql="select * from telefono where idper=?";
		RowMapper<Telefono> rowMapper=new TelefonoRowMapper();
		return this.db.query(sql,rowMapper,idper);
	}
}
