package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import connection.SingleConnectionBanco;
import model.ModelLogin;

public class DAOLoginRepository {

	private Connection connection;

	public DAOLoginRepository() {

		connection = SingleConnectionBanco.getConnnection();
	}

	public boolean validarAutenticacao(ModelLogin modelLogin) throws SQLException {
		String sql = "select * from model_login ml where  ml.login = ? and ml.senha = ? ";
		PreparedStatement statement = connection.prepareStatement(sql);
		statement.setString(1, modelLogin.getLogin());
		statement.setString(2, modelLogin.getSenha());
		ResultSet reresultado = statement.executeQuery();
		if (reresultado.next()) {
			return true;
		}
		return false;
	}

}
