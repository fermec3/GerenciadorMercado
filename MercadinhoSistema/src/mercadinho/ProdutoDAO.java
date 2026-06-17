package mercadinho;

import java.sql.ResultSet;
import conectapostgres.ConectaPostgres;

public class ProdutoDAO {
    private ConectaPostgres banco;

    public ProdutoDAO(ConectaPostgres banco) {
        this.banco = banco;
    }

    public void inserirProduto(int idCategoria, String nome, double preco) {
        try {
            String sql = "INSERT INTO produto (id_categoria, nome_produto, preco_produto) "
                       + "VALUES (" + idCategoria + ", '" + nome + "', " + preco + ")";

            banco.stmt.executeUpdate(sql);
            System.out.println("Produto inserido com sucesso!");

        } catch (Exception e) {
            System.out.println("Erro ao inserir produto: " + e.getMessage());
        }
    }

    public void listarProdutos() {
        try {
            String sql = "SELECT p.id_produto, p.nome_produto, p.preco_produto, c.nome_categoria "
                       + "FROM produto p "
                       + "INNER JOIN categoria c ON p.id_categoria = c.id_categoria";

            ResultSet rs = banco.stmt.executeQuery(sql);

            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("id_produto"));
                System.out.println("Nome: " + rs.getString("nome_produto"));
                System.out.println("Preço: " + rs.getDouble("preco_produto"));
                System.out.println("Categoria: " + rs.getString("nome_categoria"));
                System.out.println("----------------------------");
            }

        } catch (Exception e) {
            System.out.println("Erro ao listar produtos: " + e.getMessage());
        }
    }

    public void atualizarProduto(int id, double novoPreco) {
        try {
            String sql = "UPDATE produto SET preco_produto = " + novoPreco
                       + " WHERE id_produto = " + id;

            banco.stmt.executeUpdate(sql);
            System.out.println("Produto atualizado com sucesso!");

        } catch (Exception e) {
            System.out.println("Erro ao atualizar produto: " + e.getMessage());
        }
    }

    public void deletarProduto(int id) {
        try {
            String sql = "DELETE FROM produto WHERE id_produto = " + id;

            banco.stmt.executeUpdate(sql);
            System.out.println("Produto deletado com sucesso!");

        } catch (Exception e) {
            System.out.println("Erro ao deletar produto: " + e.getMessage());
        }
    }
}