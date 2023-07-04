package servlets;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;

import dao.DaoUsuariorepository;
import model.ModelLogin;

/**
 * Servlet implementation class ServletUsuarioController
 */
@WebServlet(urlPatterns = { "/ServletUsuarioController" })
public class ServletUsuarioController extends ServletGenericUtil {
	private static final long serialVersionUID = 1L;

	DaoUsuariorepository daoUsuario = new DaoUsuariorepository();

	public ServletUsuarioController() {
		super();

	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			String acao = request.getParameter("acao");
			if (acao != null && !acao.isEmpty() && acao.equalsIgnoreCase("deletar")) {
				String idUser = request.getParameter("id");
				daoUsuario.deletarUser(idUser);
				List<ModelLogin> usuarios = daoUsuario.consultarTodosUsuario(super.getUserLogado(request));
				request.setAttribute("modelLogins", usuarios);
				request.setAttribute("msg", "Excluído com sucesso!");
				request.getRequestDispatcher("principal/usuario.jsp").forward(request, response);
			} else if (acao != null && !acao.isEmpty() && acao.equalsIgnoreCase("deletarajax")) {
				String idUser = request.getParameter("id");
				daoUsuario.deletarUser(idUser);
				response.getWriter().write("Excluído com sucesso!");
			} else if (acao != null && !acao.isEmpty() && acao.equalsIgnoreCase("buscarUserAjax")) {
				String idUser = request.getParameter("nomeBusca");
				List<ModelLogin> dadosJson = daoUsuario.consultarUsuarioList(idUser, super.getUserLogado(request));
				ObjectMapper mapper = new ObjectMapper();
				String json = mapper.writeValueAsString(dadosJson);
				response.getWriter().write(json);
			} else if (acao != null && !acao.isEmpty() && acao.equalsIgnoreCase("buscarEditar")) {
				String id = request.getParameter("id");
				ModelLogin login = daoUsuario.consultaUsuarioId(id, super.getUserLogado(request));
				List<ModelLogin> usuarios = daoUsuario.consultarTodosUsuario(super.getUserLogado(request));
				request.setAttribute("modelLogins", usuarios);
				request.setAttribute("msg", "Usuario em edição");
				request.setAttribute("modelLogin", login);
				request.getRequestDispatcher("principal/usuario.jsp").forward(request, response);
			} else if (acao != null && !acao.isEmpty() && acao.equalsIgnoreCase("listarUser")) {
				List<ModelLogin> usuarios = daoUsuario.consultarTodosUsuario(super.getUserLogado(request));
				request.setAttribute("msg", "Todos Usuários");
				request.setAttribute("modelLogins", usuarios);
				request.getRequestDispatcher("/principal/usuario.jsp").forward(request, response);
			} else {
				List<ModelLogin> usuarios = daoUsuario.consultarTodosUsuario(super.getUserLogado(request));
				request.setAttribute("modelLogins", usuarios);
				request.getRequestDispatcher("principal/usuario.jsp").forward(request, response);
			}

		} catch (SQLException e) {
			e.printStackTrace();
			RequestDispatcher redirecionar = request.getRequestDispatcher("erro.jsp");
			request.setAttribute("msg", e.getMessage());
			redirecionar.forward(request, response);
		}

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			String msg = "Operação realizada com sucesso";
			String id = request.getParameter("id");
			String nome = request.getParameter("nome");
			String email = request.getParameter("email");
			String login = request.getParameter("login");
			String senha = request.getParameter("senha");

			ModelLogin modelLogin = new ModelLogin();
			modelLogin.setId(id != null && !id.isEmpty() ? Long.parseLong(id) : null);
			modelLogin.setNome(nome);
			modelLogin.setEmail(email);
			modelLogin.setLogin(login);
			modelLogin.setSenha(senha);
			if (daoUsuario.validarLogin(modelLogin.getLogin()) && modelLogin.getId() == null) {
				msg = "ja existe um login cadastrado com esse nome!";
			} else {
				if (modelLogin.isNovo()) {
					msg = "Salvo com sucesso";
				} else {
					msg = "Atualizado com sucesso.";
				}
				modelLogin = daoUsuario.salvarUser(modelLogin, super.getUserLogado(request));
			}
			
			List<ModelLogin> usuarios = daoUsuario.consultarTodosUsuario(super.getUserLogado(request));
			request.setAttribute("modelLogins", usuarios);

			request.setAttribute("msg", msg);
			RequestDispatcher redirecionar = request.getRequestDispatcher("principal/usuario.jsp");
			request.setAttribute("modelLogin", modelLogin);
			redirecionar.forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
			RequestDispatcher redirecionar = request.getRequestDispatcher("erro.jsp");
			request.setAttribute("msg", e.getMessage());
			redirecionar.forward(request, response);
		}

	}

}


