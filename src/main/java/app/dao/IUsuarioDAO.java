package app.dao;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import app.rowmapper.Persona;
import app.rowmapper.Usuario;
public interface IUsuarioDAO {
	public List<Persona> findAll(HttpServletRequest req);
	public List<Persona> listar_d(int start,int estado,String search,int length);
	public Usuario getUsuarioById(int idper);
	public boolean save(HttpServletRequest req,String tel[],String nombreFoto);
	public boolean modify(HttpServletRequest req,String tel[],String nombreFoto);
	public Persona getPersonaById(int idper);
	public boolean changeStatus(HttpServletRequest req);
	public int generarIdPer();
	
}
