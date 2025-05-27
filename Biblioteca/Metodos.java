package Biblioteca;

import java.sql.*;
import java.util.*;
import Biblioteca.conexao.ConexaoMySQL;
import java.util.Date;
import java.util.regex.Pattern;

public class Metodos {

    // Scanner para entrada de dados via terminal
    private static final Scanner scanner = new Scanner(System.in);

    // Expressão regular usada para validar endereços de e-mail
    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@(.+)$";
    // Compila a expressão regular para facilitar a validação de e-mails
    private static final Pattern emailPattern = Pattern.compile(EMAIL_REGEX);

    // ================= MÉTODOS PARA ALUNOS ================= //

    /**
     * Cadastra um novo aluno.
     * Solicita os dados via terminal, valida e persiste no banco de dados.
     */
    public static void cadastrarAluno() {
        try {
            System.out.println("\n=== CADASTRO DE ALUNO ===");

            // Leitura e validação do RGM (deve ter 8 dígitos numéricos)
            int rgm;
            while (true) {
                System.out.print("Digite o RGM do aluno (8 dígitos): ");
                String rgmInput = scanner.nextLine();

                // Verifica se o RGM possui exatamente 8 caracteres
                if (rgmInput.length() != 8) {
                    System.out.println("RGM deve ter exatamente 8 dígitos!");
                    continue; // volta para pedir novamente o RGM
                }

                try {
                    // Tenta converter o RGM para inteiro, para garantir que é numérico
                    rgm = Integer.parseInt(rgmInput);
                    break; // sai do loop se for válido
                } catch (NumberFormatException e) {
                    System.out.println("RGM deve conter apenas números!");
                }
            }

            // Verifica se já existe um aluno cadastrado com o mesmo RGM
            if (alunoExiste(rgm)) {
                System.out.println("Erro: Este RGM já está cadastrado!");
                return; // encerra o cadastro para evitar duplicidade
            }

            // Solicita e valida o nome completo do aluno
            String nome;
            do {
                System.out.print("Nome completo: ");
                nome = scanner.nextLine().trim();
                if (nome.isEmpty()) {
                    System.out.println("Nome não pode ser vazio!");
                }
            } while (nome.isEmpty());

            // Solicita e valida o endereço do aluno
            String endereco;
            do {
                System.out.print("Endereço: ");
                endereco = scanner.nextLine().trim();
                if (endereco.isEmpty()) {
                    System.out.println("Endereço não pode ser vazio!");
                }
            } while (endereco.isEmpty());

            // Solicita e valida o e-mail do aluno
            String email;
            do {
                System.out.print("E-mail: ");
                email = scanner.nextLine().trim();

                // Chama o método validarEmail que usa regex para checar o formato
                if (validarEmail(email)) {
                    System.out.println("E-mail inválido!");
                }
            } while (validarEmail(email)); // repete enquanto o email for inválido

            // Exibe os dados informados para o usuário conferir antes de salvar
            System.out.println("\nDados do aluno:");
            System.out.println("RGM: " + rgm);
            System.out.println("Nome: " + nome);
            System.out.println("Endereço: " + endereco);
            System.out.println("E-mail: " + email);

            // Pede confirmação para salvar o aluno no banco
            if (confirmarOperacao("Confirmar cadastro")) {
                System.out.println("Cadastro cancelado.");
                return; // cancela se usuário não confirmar
            }

            // Insere os dados no banco usando PreparedStatement para evitar SQL injection
            try (Connection conn = ConexaoMySQL.getConexao();
                 PreparedStatement stmt = conn.prepareStatement(
                         "INSERT INTO Cliente (RGM, Nome, Endereco, Email) VALUES (?, ?, ?, ?)")) {

                stmt.setInt(1, rgm);
                stmt.setString(2, nome);
                stmt.setString(3, endereco);
                stmt.setString(4, email);

                // Executa o comando SQL e verifica se alguma linha foi afetada
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
            pausar(); // Aguarda o usuário pressionar algo para continuar
        }
    }

    /**
     * Permite editar dados de um aluno.
     * O usuário escolhe qual campo deseja alterar.
     */
    public static void editarAluno() {
        try {
            System.out.println("\n=== EDITAR ALUNO ===");

            // Solicita o RGM do aluno que será editado
            System.out.print("Digite o RGM do aluno a editar: ");
            int rgm = Integer.parseInt(scanner.nextLine());

            // Verifica se o aluno existe no banco
            if (!alunoExiste(rgm)) {
                System.out.println("Aluno não encontrado!");
                return;
            }

            // Busca os dados atuais do aluno para mostrar na tela
            Aluno aluno = buscarAluno(rgm);
            if (aluno == null) {
                System.out.println("Erro ao buscar dados do aluno.");
                return;
            }

            // Mostra os dados atuais para o usuário escolher o que editar
            System.out.println("\nDados atuais:");
            System.out.println("1. Nome: " + aluno.getNome());
            System.out.println("2. Endereço: " + aluno.getEndereco());
            System.out.println("3. E-mail: " + aluno.getEmail());

            // Menu com opções para editar
            System.out.println("\nO que deseja editar?");
            System.out.println("1. Nome");
            System.out.println("2. Endereço");
            System.out.println("3. E-mail");
            System.out.println("0. Cancelar");
            System.out.print("Escolha: ");

            int opcao = Integer.parseInt(scanner.nextLine());

            String novoValor;
            String campo;

            // De acordo com a escolha, pede o novo valor e define o campo a alterar no banco
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
                    // Valida o novo e-mail com regex, repete até ser válido
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
                    return; // cancela edição se opção 0
                default:
                    System.out.println("Opção inválida.");
                    return; // sai se opção inválida
            }

            // Confirma alteração com o usuário antes de atualizar no banco
            if (confirmarOperacao("Confirmar alteração")) {
                System.out.println("Edição cancelada.");
                return;
            }

            // Atualiza o campo escolhido no banco
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
            pausar(); // Espera ação do usuário
        }
    }

    /**
     * Exclui um aluno e seus relacionamentos do banco de dados.
     */
    public static void excluirAluno() {
        try {
            System.out.println("\n=== EXCLUIR ALUNO ===");

            // Solicita o RGM do aluno a ser excluído
            System.out.print("Digite o RGM do aluno a excluir: ");
            int rgm = Integer.parseInt(scanner.nextLine());

            // Verifica se o aluno realmente existe
            if (!alunoExiste(rgm)) {
                System.out.println("Aluno não encontrado!");
                return;
            }

            // Busca os dados do aluno para mostrar antes da exclusão
            Aluno aluno = buscarAluno(rgm);
            if (aluno != null) {
                System.out.println("\nDados do aluno:");
                System.out.println("RGM: " + aluno.getRgm());
                System.out.println("Nome: " + aluno.getNome());
                System.out.println("E-mail: " + aluno.getEmail());
            }

            // Solicita confirmação do usuário para excluir
            if (confirmarOperacao("Tem certeza que deseja excluir este aluno?")) {
                System.out.println("Exclusão cancelada.");
                return;
            }

            Connection conn = null;
            try {
                // Abre conexão e desabilita commit automático para controlar transação
                conn = ConexaoMySQL.getConexao();
                conn.setAutoCommit(false);

                // Exclui registros relacionados ao aluno (ex: empréstimos)
                excluirRelacionamentosAluno(conn, rgm);

                // Exclui o aluno da tabela Cliente
                try (PreparedStatement stmt = conn.prepareStatement(
                        "DELETE FROM Cliente WHERE RGM = ?")) {

                    stmt.setInt(1, rgm);
                    int linhas = stmt.executeUpdate();

                    if (linhas == 0) {
                        // Se falhar, faz rollback para desfazer exclusão parcial
                        conn.rollback();
                        System.out.println("Falha ao excluir aluno.");
                        return;
                    }
                }

                // Confirma exclusão no banco
                conn.commit();
                System.out.println("Aluno excluído com sucesso!");

            } catch (SQLException e) {
                // Se erro, faz rollback para manter banco consistente
                if (conn != null) {
                    try {
                        conn.rollback();
                    } catch (SQLException ex) {
                        System.err.println("Erro no rollback: " + ex.getMessage());
                    }
                }
                throw e;
            } finally {
                // Restaura o auto commit e fecha conexão
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
            pausar(); // Espera o usuário pressionar algo antes de continuar
        }
    }

    /**
     * Lista todos os alunos cadastrados.
     * Mostra RGM, nome e e-mail, ordenados por nome.
     */
    public static void listarAlunos() {
        try {
            System.out.println("\n=== LISTA DE ALUNOS ===");

            // Abre conexão, cria Statement e executa consulta SQL para buscar alunos
            try (Connection conn = ConexaoMySQL.getConexao();
                 Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery("SELECT RGM, Nome, Email FROM Cliente ORDER BY Nome")) {

                boolean encontrou = false;

                // Percorre resultados, exibindo os dados de cada aluno
                while (rs.next()) {
                    encontrou = true;
                    System.out.println("\nRGM: " + rs.getInt("RGM"));
                    System.out.println("Nome: " + rs.getString("Nome"));
                    System.out.println("E-mail: " + rs.getString("Email"));
                    System.out.println("   ");
                    System.out.println("---");
                }

                // Se não encontrou nenhum aluno, avisa o usuário
                if (!encontrou) {
                    System.out.println("Nenhum aluno cadastrado.");
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar alunos: " + e.getMessage());
        } finally {
            pausar(); // Pausa para o usuário ver a lista antes de continuar
        }
    }

}
