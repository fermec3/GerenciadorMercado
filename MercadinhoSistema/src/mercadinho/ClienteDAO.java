package mercadinho;

import java.sql.ResultSet;
import conectapostgres.ConectaPostgres;

public class ClienteDAO {
	private ConectaPostgres banco;
	
	public ClienteDAO(ConectaPostgres banco) {
		this.banco = banco;
	}
	
	public void inserirCliente(String nome, String cpf, String email) {
	    try {
	        String sql = "INSERT INTO cliente (nome_cliente, cpf_cliente, email_cliente) "
	                   + "VALUES ('" + nome + "', '" + cpf + "', '" + email + "')";

	        banco.stmt.executeUpdate(sql);
	        System.out.println("Cliente inserido com sucesso!");

	    } catch (Exception e) {
	        System.out.println("Erro ao inserir cliente: " + e.getMessage());
	    }
	}
	
	public void listarClientes() {
		try {
			String sql = "SELECT * FROM cliente";
	        ResultSet rs = banco.stmt.executeQuery(sql);
				
			while (rs.next()) {
	            System.out.println("ID: " + rs.getInt("id_cliente"));
	            System.out.println("Nome: " + rs.getString("nome_cliente"));
	            System.out.println("CPF: " + rs.getString("cpf_cliente"));
	            System.out.println("Email: " + rs.getString("email_cliente"));
	            System.out.println("----------------------------");
			}
	    } catch (Exception e) {
	        System.out.println("Erro ao listar clientes: " + e.getMessage());

		}
	}
	
	public void atualizarCliente(int id, String novoEmail) {
	    try {
	        String sql = "UPDATE cliente SET email_cliente = '" + novoEmail + "' WHERE id_cliente = " + id;

	        banco.stmt.executeUpdate(sql);
	        System.out.println("Cliente atualizado com sucesso.");

	    } catch (Exception e) {
	        System.out.println("Erro ao atualizar cliente: " + e.getMessage());
	    }
	}
	
	public void deletarCliente(int id) {
		try {
			String sql = "DELETE FROM cliente WHERE id_cliente = " + id;
			
			banco.stmt.executeUpdate(sql);
			System.out.println("Cliente deletado com sucesso!");
			
		 } catch (Exception e) {
		        System.out.println("Erro ao deletar cliente: " + e.getMessage());
		    }

		}
}
