package Biblioteca;

import java.sql.*;
import java.util.*;
import Biblioteca.conexao.ConexaoMySQL;
import java.util.Date;
import java.util.regex.Pattern;

public class Metodos {
    
    private static final Scanner scanner = new Scanner(System.in);
    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@(.+)$";
    private static final Pattern emailPattern = Pattern.compile(EMAIL_REGEX);

    // MÉTODOS PARA ALUNOS
    public static void cadastrarAluno() {
        try {
            System.out.println("\n=== CADASTRO DE ALUNO ===");

            // Solicitar RGM
            int rgm;
            while (true) {
                System.out.print("Digite o RGM do aluno (8 dígitos): ");
                String rgmInput = scanner.nextLine();

                if (rgmInput.length() != 8) {
                    System.out.println("RGM deve ter exatamente 8 dígitos!");
                    continue;
                }

                try {
                    rgm = Integer.parseInt(rgmInput);
                    break;
                } catch (NumberFormatException e) {
                    System.out.println("RGM deve conter apenas números!");
                }
            }

            // Verificar se RGM já existe
            if (alunoExiste(rgm)) {
                System.out.println("Erro: Este RGM já está cadastrado!");
                return;
            }

            // Solicitar nome
            String nome;
            do {
                System.out.print("Nome completo: ");
                nome = scanner.nextLine().trim();
                if (nome.isEmpty()) {
                    System.out.println("Nome não pode ser vazio!");
                }
            } while (nome.isEmpty());

            // Solicitar endereço
            String endereco;
            do {
                System.out.print("Endereço: ");
                endereco = scanner.nextLine().trim();
                if (endereco.isEmpty()) {
                    System.out.println("Endereço não pode ser vazio!");
                }
            } while (endereco.isEmpty());

            // Solicitar e-mail
            String email;
            do {
                System.out.print("E-mail: ");
                email = scanner.nextLine().trim();
                if (validarEmail(email)) {
                    System.out.println("E-mail inválido!");
                }
            } while (validarEmail(email));

            // Confirmar cadastro
            System.out.println("\nDados do aluno:");
            System.out.println("RGM: " + rgm);
            System.out.println("Nome: " + nome);
            System.out.println("Endereço: " + endereco);
            System.out.println("E-mail: " + email);

            if (confirmarOperacao("Confirmar cadastro")) {
                System.out.println("Cadastro cancelado.");
                return;
            }

            // Inserir no banco de dados
            try (Connection conn = ConexaoMySQL.getConexao();
                 PreparedStatement stmt = conn.prepareStatement(
                         "INSERT INTO Cliente (RGM, Nome, Endereco, Email) VALUES (?, ?, ?, ?)")) {

                stmt.setInt(1, rgm);
                stmt.setString(2, nome);
                stmt.setString(3, endereco);
                stmt.setString(4, email);

                int linhasAfetadas = stmt.executeUpdate();

                if (linhasAfetadas > 0) {
                    System.out.println("\nAluno cadastrado com sucesso!");
                } else {
                    System.out.println("Falha ao cadastrar aluno.");
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro no banco de dados: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Erro inesperado: " + e.getMessage());
        } finally {
            pausar();
        }
    }

    public static void editarAluno() {
        try {
            System.out.println("\n=== EDITAR ALUNO ===");

            // Solicitar RGM do aluno
            System.out.print("Digite o RGM do aluno a editar: ");
            int rgm = Integer.parseInt(scanner.nextLine());

            // Verificar se aluno existe
            if (!alunoExiste(rgm)) {
                System.out.println("Aluno não encontrado!");
                return;
            }

            // Mostrar dados atuais
            Aluno aluno = buscarAluno(rgm);
            if (aluno == null) {
                System.out.println("Erro ao buscar dados do aluno.");
                return;
            }

            System.out.println("\nDados atuais:");
            System.out.println("1. Nome: " + aluno.getNome());
            System.out.println("2. Endereço: " + aluno.getEndereco());
            System.out.println("3. E-mail: " + aluno.getEmail());

            // Menu de edição
            System.out.println("\nO que deseja editar?");
            System.out.println("1. Nome");
            System.out.println("2. Endereço");
            System.out.println("3. E-mail");
            System.out.println("0. Cancelar");
            System.out.print("Escolha: ");

            int opcao = Integer.parseInt(scanner.nextLine());

            String novoValor ;
            String campo ;

            switch (opcao) {
                case 1:
                    System.out.print("Novo nome: ");
                    novoValor = scanner.nextLine().trim();
                    campo = "Nome";
                    break;
                case 2:
                    System.out.print("Novo endereço: ");
                    novoValor = scanner.nextLine().trim();
                    campo = "Endereco";
                    break;
                case 3:
                    do {
                        System.out.print("Novo e-mail: ");
                        novoValor = scanner.nextLine().trim();
                        if (validarEmail(novoValor)) {
                            System.out.println("E-mail inválido!");
                            novoValor = "";
                        }
                    } while (novoValor.isEmpty());
                    campo = "Email";
                    break;
                case 0:
                    System.out.println("Edição cancelada.");
                    return;
                default:
                    System.out.println("Opção inválida.");
                    return;
            }

            if (confirmarOperacao("Confirmar alteração")) {
                System.out.println("Edição cancelada.");
                return;
            }

            // Atualizar no banco
            try (Connection conn = ConexaoMySQL.getConexao();
                 PreparedStatement stmt = conn.prepareStatement(
                         "UPDATE Cliente SET " + campo + " = ? WHERE RGM = ?")) {

                stmt.setString(1, novoValor);
                stmt.setInt(2, rgm);

                int linhasAfetadas = stmt.executeUpdate();

                if (linhasAfetadas > 0) {
                    System.out.println("Aluno atualizado com sucesso!");
                } else {
                    System.out.println("Falha ao atualizar aluno.");
                }
            }
        } catch (NumberFormatException e) {
            System.out.println("Entrada inválida. Digite um número.");
        } catch (SQLException e) {
            System.err.println("Erro no banco de dados: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Erro inesperado: " + e.getMessage());
        } finally {
            pausar();
        }
    }

    public static void excluirAluno() {
        try {
            System.out.println("\n=== EXCLUIR ALUNO ===");

            // Solicitar RGM do aluno
            System.out.print("Digite o RGM do aluno a excluir: ");
            int rgm = Integer.parseInt(scanner.nextLine());

            // Verificar se aluno existe
            if (!alunoExiste(rgm)) {
                System.out.println("Aluno não encontrado!");
                return;
            }

            // Mostrar dados do aluno
            Aluno aluno = buscarAluno(rgm);
            if (aluno != null) {
                System.out.println("\nDados do aluno:");
                System.out.println("RGM: " + aluno.getRgm());
                System.out.println("Nome: " + aluno.getNome());
                System.out.println("E-mail: " + aluno.getEmail());
            }

            if (confirmarOperacao("Tem certeza que deseja excluir este aluno?")) {
                System.out.println("Exclusão cancelada.");
                return;
            }

            // Excluir aluno com tratamento de transação
            Connection conn = null;
            try {
                conn = ConexaoMySQL.getConexao();
                conn.setAutoCommit(false); // Iniciar transação

                // 1. Excluir relacionamentos
                excluirRelacionamentosAluno(conn, rgm);

                // 2. Excluir o aluno
                try (PreparedStatement stmt = conn.prepareStatement(
                        "DELETE FROM Cliente WHERE RGM = ?")) {

                    stmt.setInt(1, rgm);
                    int linhas = stmt.executeUpdate();

                    if (linhas == 0) {
                        conn.rollback();
                        System.out.println("Falha ao excluir aluno.");
                        return;
                    }
                }

                conn.commit(); // Confirmar transação
                System.out.println("Aluno excluído com sucesso!");

            } catch (SQLException e) {
                if (conn != null) {
                    try {
                        conn.rollback();
                    } catch (SQLException ex) {
                        System.err.println("Erro no rollback: " + ex.getMessage());
                    }
                }
                throw e;
            } finally {
                if (conn != null) {
                    try {
                        conn.setAutoCommit(true);
                        conn.close();
                    } catch (SQLException e) {
                        System.err.println("Erro ao fechar conexão: " + e.getMessage());
                    }
                }
            }
        } catch (NumberFormatException e) {
            System.out.println("RGM deve ser um número!");
        } catch (SQLException e) {
            System.err.println("Erro no banco de dados: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Erro inesperado: " + e.getMessage());
        } finally {
            pausar();
        }
    }

    public static void listarAlunos() {
        try {
            System.out.println("\n=== LISTA DE ALUNOS ===");

            try (Connection conn = ConexaoMySQL.getConexao();
                 Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery("SELECT RGM, Nome, Email FROM Cliente ORDER BY Nome")) {

                boolean encontrou = false;
                while (rs.next()) {
                    encontrou = true;
                    System.out.println("\nRGM: " + rs.getInt("RGM"));
                    System.out.println("Nome: " + rs.getString("Nome"));
                    System.out.println("E-mail: " + rs.getString("Email"));
                    System.out.println("   ");
                    System.out.println("---");
                }

                if (!encontrou) {
                    System.out.println("Nenhum aluno cadastrado.");
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar alunos: " + e.getMessage());
        } finally {
            pausar();
        }
    }

}