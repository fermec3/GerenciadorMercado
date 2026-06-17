package mercadinho;

import java.util.Scanner;
import conectapostgres.ConectaPostgres;

public class Main {
    public static void main(String[] args) {

        ConectaPostgres banco = new ConectaPostgres();

        String url   = "jdbc:postgresql://localhost:5432/postgres";
        String user  = "postgres";
        String senha = "1234";

        banco.Conectar(url, user, senha);

        ClienteDAO clienteDAO = new ClienteDAO(banco);
        ProdutoDAO produtoDAO = new ProdutoDAO(banco);
        VendaDAO vendaDAO = new VendaDAO(banco);

        Scanner scanner = new Scanner(System.in);
        int opcaoPrincipal = -1;

        while (opcaoPrincipal != 0) {
            System.out.println("\n===== MENU MERCADINHO =====");
            System.out.println("1 - Clientes");
            System.out.println("2 - Produtos");
            System.out.println("3 - Vendas");
            System.out.println("0 - Sair");
            System.out.print("Escolha uma opção: ");

            opcaoPrincipal = scanner.nextInt();

            switch (opcaoPrincipal) {

                case 1:
                    int opcaoCliente = -1;
                    while (opcaoCliente != 0) {
                        System.out.println("\n-- CLIENTES --");
                        System.out.println("1 - Adicionar cliente");
                        System.out.println("2 - Listar clientes");
                        System.out.println("3 - Atualizar email do cliente");
                        System.out.println("4 - Deletar cliente");
                        System.out.println("0 - Voltar");
                        System.out.print("Escolha uma opção: ");

                        opcaoCliente = scanner.nextInt();

                        switch (opcaoCliente) {
                            case 1:
                                scanner.nextLine();
                                System.out.print("Nome: ");
                                String nomeCli = scanner.nextLine();
                                System.out.print("CPF: ");
                                String cpf = scanner.nextLine();
                                System.out.print("Email: ");
                                String emailCli = scanner.nextLine();
                                clienteDAO.inserirCliente(nomeCli, cpf, emailCli);
                                break;

                            case 2:
                                clienteDAO.listarClientes();
                                break;

                            case 3:
                                System.out.print("ID do cliente: ");
                                int idCliAtt = scanner.nextInt();
                                scanner.nextLine();
                                System.out.print("Novo email: ");
                                String novoEmail = scanner.nextLine();
                                clienteDAO.atualizarCliente(idCliAtt, novoEmail);
                                break;

                            case 4:
                                System.out.print("ID do cliente a deletar: ");
                                int idCliDel = scanner.nextInt();
                                clienteDAO.deletarCliente(idCliDel);
                                break;

                            case 0:
                                System.out.println("Voltando ao menu principal...");
                                break;

                            default:
                                System.out.println("Opção inválida!");
                        }
                    }
                    break;

                case 2:
                    int opcaoProduto = -1;
                    while (opcaoProduto != 0) {
                        System.out.println("\n-- PRODUTOS --");
                        System.out.println("1 - Adicionar produto");
                        System.out.println("2 - Listar produtos");
                        System.out.println("3 - Atualizar preço do produto");
                        System.out.println("4 - Deletar produto");
                        System.out.println("0 - Voltar");
                        System.out.print("Escolha uma opção: ");

                        opcaoProduto = scanner.nextInt();

                        switch (opcaoProduto) {
                            case 1:
                                System.out.print("ID da categoria: ");
                                int idCat = scanner.nextInt();
                                scanner.nextLine();
                                System.out.print("Nome do produto: ");
                                String nomeProd = scanner.nextLine();
                                System.out.print("Preço: ");
                                double preco = scanner.nextDouble();
                                produtoDAO.inserirProduto(idCat, nomeProd, preco);
                                break;

                            case 2:
                                produtoDAO.listarProdutos();
                                break;

                            case 3:
                                System.out.print("ID do produto: ");
                                int idProdAtt = scanner.nextInt();
                                System.out.print("Novo preço: ");
                                double novoPreco = scanner.nextDouble();
                                produtoDAO.atualizarProduto(idProdAtt, novoPreco);
                                break;

                            case 4:
                                System.out.print("ID do produto a deletar: ");
                                int idProdDel = scanner.nextInt();
                                produtoDAO.deletarProduto(idProdDel);
                                break;

                            case 0:
                                System.out.println("Voltando ao menu principal...");
                                break;

                            default:
                                System.out.println("Opção inválida!");
                        }
                    }
                    break;

                case 3:
                    int opcaoVenda = -1;
                    while (opcaoVenda != 0) {
                        System.out.println("\n-- VENDAS --");
                        System.out.println("1 - Registrar venda");
                        System.out.println("2 - Listar vendas (view)");
                        System.out.println("3 - Calcular total da venda (function)");
                        System.out.println("4 - Atualizar total da venda (function)");
                        System.out.println("5 - Deletar venda");
                        System.out.println("0 - Voltar");
                        System.out.print("Escolha uma opção: ");

                        opcaoVenda = scanner.nextInt();

                        switch (opcaoVenda) {
                            case 1:
                                System.out.print("ID do cliente: ");
                                int idClienteVenda = scanner.nextInt();
                                int idVenda = vendaDAO.inserirVenda(idClienteVenda);
                                System.out.println("Venda criada com ID: " + idVenda);

                                char continuar = 's';
                                while (continuar == 's') {
                                    System.out.print("ID do produto: ");
                                    int idProdItem = scanner.nextInt();
                                    System.out.print("Quantidade: ");
                                    int qtd = scanner.nextInt();
                                    System.out.print("Preço unitário: ");
                                    double precoUnit = scanner.nextDouble();

                                    vendaDAO.inserirItemVenda(idVenda, idProdItem, qtd, precoUnit);

                                    System.out.print("Adicionar outro produto? (s/n): ");
                                    continuar = scanner.next().charAt(0);
                                }

                                vendaDAO.registrarSaidaEstoque(idVenda);
                                vendaDAO.atualizarTotalVenda(idVenda);
                                System.out.println("Venda finalizada!");
                                break;

                            case 2:
                                vendaDAO.listarVendas();
                                break;

                            case 3:
                                System.out.print("ID da venda: ");
                                int idVendaCalc = scanner.nextInt();
                                vendaDAO.calcularTotalVenda(idVendaCalc);
                                break;

                            case 4:
                                System.out.print("ID da venda: ");
                                int idVendaAtt = scanner.nextInt();
                                vendaDAO.atualizarTotalVenda(idVendaAtt);
                                break;

                            case 5:
                                System.out.print("ID da venda a deletar: ");
                                int idVendaDel = scanner.nextInt();
                                vendaDAO.deletarVenda(idVendaDel);
                                break;

                            case 0:
                                System.out.println("Voltando ao menu principal...");
                                break;

                            default:
                                System.out.println("Opção inválida!");
                        }
                    }
                    break;

                case 0:
                    System.out.println("Saindo");
                    break;

                default:
                    System.out.println("Opção inválida!");
            }
        }

        scanner.close();
        banco.Desconectar();
    }
}