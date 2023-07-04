package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import connection.SingleConnectionBanco;
import model.ModelLogin;

public class DaoUsuariorepository {
	private Connection connection;

	public DaoUsuariorepository() {
		connection = SingleConnectionBanco.getConnnection();
	}

	public ModelLogin salvarUser(ModelLogin modelLogin, Long userLogado) throws SQLException {

		if (modelLogin.isNovo()) {

			String Sql = "insert into model_login (login, senha, nome, email, usuario_id) values (?, ?,?,?,?); ";

			PreparedStatement prepareSQL = connection.prepareStatement(Sql);

			prepareSQL.setString(1, modelLogin.getLogin());
			prepareSQL.setString(2, modelLogin.getSenha());
			prepareSQL.setString(3, modelLogin.getNome());
			prepareSQL.setString(4, modelLogin.getEmail());
			prepareSQL.setLong(5, userLogado);
			prepareSQL.execute();
			connection.commit();
		} else {
			String sql = " update model_login set login =?, senha=?, nome=?, email=? where id = '" + modelLogin.getId()
					+ "'; ";
			PreparedStatement prepareSQL = connection.prepareStatement(sql);
			prepareSQL.setString(1, modelLogin.getLogin());
			prepareSQL.setString(2, modelLogin.getSenha());
			prepareSQL.setString(3, modelLogin.getNome());
			prepareSQL.setString(4, modelLogin.getEmail());
			prepareSQL.executeUpdate();
			connection.commit();

		}

		return consultaUsuario(modelLogin.getLogin(), userLogado);

	}
	
	
	
	public List<ModelLogin>consultarTodosUsuario(Long userLogado) throws SQLException{
		List<ModelLogin> lista = new ArrayList<>();
		String sql = "select * from model_login where user_admin is false and usuario_id =  " +userLogado;
		PreparedStatement statement = connection.prepareStatement(sql);
		ResultSet resultSet = statement.executeQuery();
		while(resultSet.next()) {
			ModelLogin usuario = new ModelLogin();
			usuario.setEmail(resultSet.getString("email"));
			usuario.setLogin(resultSet.getString("login"));
			usuario.setId(resultSet.getLong("id"));
			usuario.setNome(resultSet.getString("nome"));
			//usuario.setSenha(resultSet.getString("senha"));			
			lista.add(usuario);			
		}
		
		
		return lista;
	}
	
	public List<ModelLogin>consultarUsuarioList(String nome, Long userLogado) throws SQLException{
		List<ModelLogin> lista = new ArrayList<>();
		String sql = "select * from model_login where upper(nome) like upper(?) and  user_admin is false and usuario_id = ?";
		PreparedStatement statement = connection.prepareStatement(sql);
		statement.setString(1, "%"+nome+"%");
		statement.setLong(2, userLogado);
		ResultSet resultSet = statement.executeQuery();
		while(resultSet.next()) {
			ModelLogin usuario = new ModelLogin();
			usuario.setEmail(resultSet.getString("email"));
			usuario.setLogin(resultSet.getString("login"));
			usuario.setId(resultSet.getLong("id"));
			usuario.setNome(resultSet.getString("nome"));
			//usuario.setSenha(resultSet.getString("senha"));			
			lista.add(usuario);			
		}
		
		return lista;
	}
	 

	public ModelLogin consultaUsuario(String login) throws SQLException {
		ModelLogin modelLogin = new ModelLogin();
		String sql = "select * from model_login where upper(login) =  upper ('" + login + "') and  user_admin is false";
		PreparedStatement statement = connection.prepareStatement(sql);
		
		ResultSet resultado = statement.executeQuery();
		while (resultado.next()) {
			modelLogin.setId(resultado.getLong("id"));
			modelLogin.setEmail(resultado.getString("email"));
			modelLogin.setLogin(resultado.getString("login"));
			modelLogin.setSenha(resultado.getString("senha"));
			modelLogin.setNome(resultado.getString("nome"));
			modelLogin.setUserAdmin(resultado.getBoolean("userAdmin"));
		}
		return modelLogin;

	}
	

	public ModelLogin consultaUsuarioLogado(String login) throws SQLException {
		ModelLogin modelLogin = new ModelLogin();
		String sql = "select * from model_login where upper(login) =  upper ('" + login + "') ";
		PreparedStatement statement = connection.prepareStatement(sql);
		
		ResultSet resultado = statement.executeQuery();
		while (resultado.next()) {
			modelLogin.setId(resultado.getLong("id"));
			modelLogin.setEmail(resultado.getString("email"));
			modelLogin.setLogin(resultado.getString("login"));
			modelLogin.setSenha(resultado.getString("senha"));
			modelLogin.setNome(resultado.getString("nome"));
			modelLogin.setUserAdmin(resultado.getBoolean("usrAdmin"));
		}
		return modelLogin;

	}
	public ModelLogin consultaUsuario(String login, Long userLogado) throws SQLException {
		ModelLogin modelLogin = new ModelLogin();
		String sql = "select * from model_login where upper(login) =  upper ('" + login + "') and  user_admin is false and usuario_id  = " + userLogado;
		PreparedStatement statement = connection.prepareStatement(sql);
//		statement.setLong(1, Long.parseLong(login));
//		statement.setLong(2, userLogado);
		ResultSet resultado = statement.executeQuery();
		while (resultado.next()) {
			modelLogin.setId(resultado.getLong("id"));
			modelLogin.setEmail(resultado.getString("email"));
			modelLogin.setLogin(resultado.getString("login"));
			modelLogin.setSenha(resultado.getString("senha"));
			modelLogin.setNome(resultado.getString("nome"));
		}
		return modelLogin;

	}
	
	public ModelLogin consultaUsuarioId(String id, Long userLogado) throws SQLException {
		ModelLogin modelLogin = new ModelLogin();
		String sql = "select * from model_login where id = ? and  user_admin is false and usuario_id = ?";
		PreparedStatement statement = connection.prepareStatement(sql);
		statement.setLong(1, Long.parseLong(id));
		statement.setLong(2, userLogado);

		ResultSet resultado = statement.executeQuery();
		while (resultado.next()) {
			modelLogin.setId(resultado.getLong("id"));
			modelLogin.setEmail(resultado.getString("email"));
			modelLogin.setLogin(resultado.getString("login"));
			modelLogin.setSenha(resultado.getString("senha"));
			modelLogin.setNome(resultado.getString("nome"));
		}
		return modelLogin;

	}

	public boolean validarLogin(String login) throws SQLException {
		String sql = "select count(1) >0 as existe from model_login where upper(login) = upper('" + login + "')";
		PreparedStatement statement = connection.prepareStatement(sql);
		ResultSet resultSet = statement.executeQuery();
		if (resultSet.next()) {
			return resultSet.getBoolean("existe");
		}
		return false;
	}

	public void deletarUser(String idUser) throws SQLException {
		String sql = "delete from model_login where id = ? and  user_admin is false";

		PreparedStatement prepareSQL = connection.prepareStatement(sql);

		prepareSQL.setLong(1, Long.parseLong(idUser));

		prepareSQL.executeUpdate();

		connection.commit();

	}

}
