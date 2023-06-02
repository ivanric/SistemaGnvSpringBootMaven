package app.utilidades;

import java.io.Closeable;
import java.io.IOException;

import javax.annotation.PreDestroy;
import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
public class DatabaseConfig {
	private final DataSource dataSource;
    
    public DatabaseConfig(DataSource dataSource) {
        this.dataSource = dataSource;
    }
    
    @Bean
    public JdbcTemplate jdbcTemplate() {
        return new JdbcTemplate(dataSource);
    }
    
    @PreDestroy
    public void destroy() {
        if (dataSource instanceof Closeable) {
            try {
                ((Closeable) dataSource).close();
            } catch (IOException e) {
                // Manejo de excepciones
            }
        }
    }
}
