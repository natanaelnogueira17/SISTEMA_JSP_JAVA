package servlets;

import java.io.Serializable;
import java.sql.SQLException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import dao.DaoUsuariorepository;

public class ServletGenericUtil extends HttpServlet implements Serializable {

	private static final long serialVersionUID = 1L;
	
	
	private DaoUsuariorepository usuarioRepository = new DaoUsuariorepository();

	
	public Long getUserLogado(HttpServletRequest request) throws SQLException {
		HttpSession session =  request.getSession();
		String usuarioLogado = (String) session.getAttribute("usuario");
		
		return usuarioRepository.consultaUsuarioLogado(usuarioLogado).getId();
	}
	


}
