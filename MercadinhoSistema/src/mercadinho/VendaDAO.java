package mercadinho;

import java.sql.ResultSet;
import conectapostgres.ConectaPostgres;

public class VendaDAO {
    private ConectaPostgres banco;

    public VendaDAO(ConectaPostgres banco) {
        this.banco = banco;
    }
    
    public int inserirVenda(int idCliente) {
        try {
            String sql = "INSERT INTO venda (id_cliente, data_venda, total_venda) "
                       + "VALUES (" + idCliente + ", CURRENT_DATE, 0) RETURNING id_venda";

            ResultSet rs = banco.stmt.executeQuery(sql);

            if (rs != null && rs.next()) {
                return rs.getInt("id_venda");
            } else {
                System.out.println("Venda não retornou ID.");
            }

        } catch (Exception e) {
            System.out.println("Erro ao inserir venda: " + e.getMessage());
        }
        return -1;
    }
    
    public void inserirItemVenda(int idVenda, int idProduto, int quantidade, double precoUnitario) {
        try {
            String sql = "INSERT INTO produto_venda (id_venda, id_produto, quantidade, preco_unitario) "
                       + "VALUES (" + idVenda + ", " + idProduto + ", " + quantidade + ", " + precoUnitario + ")";

            banco.stmt.executeUpdate(sql);
            System.out.println("Item inserido com sucesso!");

        } catch (Exception e) {
            System.out.println("Erro ao inserir item: " + e.getMessage());
        }
    }
    
    public void registrarSaidaEstoque(int idVenda) {
        try {
            String sql = "CALL pr_registrar_saida_estoque(" + idVenda + ")";

            banco.stmt.executeUpdate(sql);
            System.out.println("Estoque atualizado com sucesso!");

        } catch (Exception e) {
            System.out.println("Erro ao atualizar estoque: " + e.getMessage());
        }
    }
    
    public double calcularTotalVenda(int idVenda) {
        try {
            String sql = "SELECT fn_calcular_total_venda(" + idVenda + ")";

            ResultSet rs = banco.stmt.executeQuery(sql);

            if (rs.next()) {
                double total = rs.getDouble(1);
                System.out.println("Total calculado da venda " + idVenda + ": R$ " + total);
                return total;
            }

        } catch (Exception e) {
            System.out.println("Erro ao calcular total da venda: " + e.getMessage());
        }
        return 0;
    }

    public void atualizarTotalVenda(int idVenda) {
        try {
            String sql = "UPDATE venda SET total_venda = fn_calcular_total_venda(" + idVenda + ") "
                       + "WHERE id_venda = " + idVenda;

            banco.stmt.executeUpdate(sql);
            System.out.println("Total da venda " + idVenda + " atualizado com sucesso!");

        } catch (Exception e) {
            System.out.println("Erro ao atualizar total da venda: " + e.getMessage());
        }
    }

    public void deletarVenda(int idVenda) {
        try {
            String sqlPV = "DELETE FROM produto_venda WHERE id_venda = " + idVenda;
            banco.stmt.executeUpdate(sqlPV);

            String sqlVenda = "DELETE FROM venda WHERE id_venda = " + idVenda;
            banco.stmt.executeUpdate(sqlVenda);

            System.out.println("Venda " + idVenda + " deletada com sucesso!");

        } catch (Exception e) {
            System.out.println("Erro ao deletar venda: " + e.getMessage());
        }
    }

    public void listarVendas() {
        try {
            String sql = "SELECT * FROM vw_vendas_detalhadas";

            ResultSet rs = banco.stmt.executeQuery(sql);

            while (rs.next()) {
                System.out.println("Cliente: " + rs.getString("nome_cliente"));	
                System.out.println("Venda ID: " + rs.getInt("id_venda"));
                System.out.println("Data: " + rs.getString("data_venda"));
                System.out.println("Produto: " + rs.getString("nome_produto"));
                System.out.println("Quantidade: " + rs.getInt("quantidade"));
                System.out.println("Subtotal: " + rs.getDouble("subtotal"));
                System.out.println("----------------------------");
            }

        } catch (Exception e) {
            System.out.println("Erro ao listar vendas: " + e.getMessage());
        }
    }
}