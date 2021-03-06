/*
 * Copyright © 2017 Daniel Dias (daniel.dias.analistati@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package persistencia;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import modelos.Usuario;
import modelos.UsuarioNormal;
import modelos.UsuarioVip;

/**
 * @author daniel
 * github:Daniel-Dos
 * daniel.dias.analistati@gmail.com
 * twitter:@danieldiasjava
 */
public class DAOGenericoUsuario implements GenericDAO<Usuario> {

	private Connection conexao = null;
	private ResultSet resultado = null;
	private PreparedStatement stm = null;

	public DAOGenericoUsuario() throws ClassNotFoundException {
		conexao = DAOFactoryOracle.createConnection();
	}

	@Override
	public void incluir(Usuario entidade) throws SQLException {

		stm = conexao.prepareStatement("INSERT INTO USUARIO VALUES (?,?,?,?,?,?)");
		stm.setString(1, entidade.getLogin());
		stm.setString(2, entidade.getSenha());
		stm.setString(3, entidade.getNome());
		stm.setString(4, entidade.getTelefone());
		stm.setString(5, entidade.getEmail());
		stm.setString(6, entidade.getClass().getSimpleName());
		stm.executeUpdate();
		stm.close();
	}

	@Override
	public void excluir(Usuario entidade) throws SQLException {

		stm = conexao.prepareStatement("DELETE FROM RESERVA WHERE USUARIO = ?");
		stm.setString(1, entidade.getLogin());
		stm.executeUpdate();
		stm.close();

		stm = conexao.prepareStatement("DELETE FROM USUARIO WHERE LOGIN = ?");
		stm.setString(1, entidade.getLogin());
		stm.executeUpdate();
		stm.close();
	}

	@Override
	public void alterar(Usuario entidade) throws SQLException {

		stm = conexao.prepareStatement("UPDATE USUARIO SET NOME = ?, TELEFONE = ? , EMAIL = ? WHERE LOGIN = ?");
		stm.setString(4, entidade.getLogin());
		stm.setString(1, entidade.getNome());
		stm.setString(2, entidade.getTelefone());
		stm.setString(3, entidade.getEmail());
		stm.executeUpdate();
		stm.close();
	}

	@Override
	public Usuario consultar(Usuario entidade) throws SQLException {

		Usuario aux = null;
		String tipo = null;

		stm = conexao.prepareStatement("SELECT * FROM USUARIO WHERE LOGIN = ?");
		stm.setString(1, entidade.getLogin());

		resultado = stm.executeQuery();

		while (resultado.next()) {
			tipo = resultado.getString(6);

			if (tipo.equals("UsuarioVip")) {
				aux = new UsuarioVip();
			} else {
				aux = new UsuarioNormal();
			}

			aux.setLogin(resultado.getString(1));
			aux.setNome(resultado.getString(3));
			aux.setTelefone(resultado.getString(4));
			aux.setEmail(resultado.getString(5));
		}
		resultado.close();
		stm.close();
		return aux;
	}

	public Usuario lembrarSenha(Usuario entidade) throws SQLException {

		Usuario aux = null;
		String tipo = null;

		stm = conexao.prepareStatement("SELECT * FROM USUARIO WHERE EMAIL = ?");
		stm.setString(1, entidade.getEmail());

		resultado = stm.executeQuery();

		while (resultado.next()) {
			tipo = resultado.getString(6);

			if (tipo.equals("UsuarioVip")) {
				aux = new UsuarioVip();
			} else {
				aux = new UsuarioNormal();
			}

			aux.setSenha(resultado.getString(2));
			aux.setNome(resultado.getString(3));
		}
		resultado.close();
		stm.close();
		return aux;
	}

	public Usuario consultarLoginSenha(Usuario entidade) throws SQLException {

		Usuario aux = null;
		String tipo = null;

		stm = conexao.prepareStatement("SELECT * FROM USUARIO WHERE LOGIN = ? AND SENHA = ?");
		stm.setString(1, entidade.getLogin());
		stm.setString(2, entidade.getSenha());

		resultado = stm.executeQuery();

		while (resultado.next()) {
			tipo = resultado.getString(6);

			if (tipo.equals("UsuarioVip"))
				aux = new UsuarioVip();
			else
				aux = new UsuarioNormal();

			aux.setLogin(resultado.getString(1));
			aux.setSenha(resultado.getString(2));
			aux.setNome(resultado.getString(3));
			aux.setTelefone(resultado.getString(4));
			aux.setEmail(resultado.getString(5));
		}
		resultado.close();
		stm.close();
		return aux;
	}

	@Override
	public List<Usuario> getAllUsers() throws SQLException {
		return null;
	}

	@Override
	public List<Usuario> getUser(Usuario entidade) throws SQLException {
		throw new UnsupportedOperationException("Not supported yet."); 
	}
}