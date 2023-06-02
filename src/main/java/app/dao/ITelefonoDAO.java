package app.dao;
import java.util.List;

import app.rowmapper.Telefono;
public interface ITelefonoDAO {
	List<Telefono> getAllTelefonosById(int idper);
}
