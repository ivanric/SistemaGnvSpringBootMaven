package app.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import app.rowmapper.Persona;
import app.rowmapper.Usuario;
@Service
public interface IUsuarioService {
	public List<Persona> findAll(HttpServletRequest req);
	public List<Persona> listar_d(int start,int estado,String search,int length);
	public Usuario getUsuarioById(int idper);
	public boolean save(HttpServletRequest req,String tel[],String nombreFoto);
	public boolean modify(HttpServletRequest req,String tel[],String nombreFoto);
	public Persona getPersonaById(int idper);
	public boolean changeStatus(HttpServletRequest req);
	public int generarIdPer();
}
