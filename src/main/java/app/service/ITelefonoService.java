package app.service;

import java.util.List;

import org.springframework.stereotype.Service;

import app.rowmapper.Telefono;
@Service
public interface ITelefonoService {
	List<Telefono> getAllTelefonosById(int idper);
}
