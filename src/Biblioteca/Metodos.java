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

    // Cadastra um novo aluno.
    // Solicita os dados via terminal, valida e persiste no banco de dados.
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
                if (!validarEmail(email)) {
                    System.out.println("E-mail inválido!");
                }
            } while (!validarEmail(email)); // repete enquanto o email for inválido

            // Exibe os dados informados para o usuário conferir antes de salvar
            System.out.println("\nDados do aluno:");
            System.out.println("RGM: " + rgm);
            System.out.println("Nome: " + nome);
            System.out.println("Endereço: " + endereco);
            System.out.println("E-mail: " + email);

            // Pede confirmação para salvar o aluno no banco
            if (!confirmarOperacao("Confirmar cadastro")) {
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

    // Permite editar dados de um aluno.
    // O usuário escolhe qual campo deseja alterar.
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
                        if (!validarEmail(novoValor)) {
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
            if (!confirmarOperacao("Confirmar alteração")) {
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

    // Exclui um aluno e seus relacionamentos do banco de dados.
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
            if (!confirmarOperacao("Tem certeza que deseja excluir este aluno?")) {
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

    // Lista todos os alunos cadastrados.
    // Mostra RGM, nome e e-mail, ordenados por nome.
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

// ================= MÉTODOS PARA LIVROS ================= //

    // Cadastra um novo livro
    // Solicita os dados via terminal, valida e persiste no banco de dados.
    public static void cadastrarLivro() {
        try {
            System.out.println("\n=== CADASTRO DE LIVRO ===");

            // Solicitar dados do livro
            String titulo;
            do {
                System.out.print("Título: ");
                titulo = scanner.nextLine().trim();
                if (titulo.isEmpty()) {
                    System.out.println("Título não pode ser vazio!");
                }
            } while (titulo.isEmpty());

            String autor;
            do {
                System.out.print("Autor: ");
                autor = scanner.nextLine().trim();
                if (autor.isEmpty()) {
                    System.out.println("Autor não pode ser vazio!");
                }
            } while (autor.isEmpty());

            int ano;
            while (true) {
                System.out.print("Ano de publicação: ");
                try {
                    ano = Integer.parseInt(scanner.nextLine());
                    if (ano <= 0 || ano > Calendar.getInstance().get(Calendar.YEAR) + 1) {
                        System.out.println("Ano inválido!");
                    } else {
                        break;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Digite um ano válido!");
                }
            }

            String genero;
            do {
                System.out.print("Gênero: ");
                genero = scanner.nextLine().trim();
                if (genero.isEmpty()) {
                    System.out.println("Gênero não pode ser vazio!");
                }
            } while (genero.isEmpty());

            // Confirmar cadastro
            System.out.println("\nDados do livro:");
            System.out.println("Título: " + titulo);
            System.out.println("Autor: " + autor);
            System.out.println("Ano: " + ano);
            System.out.println("Gênero: " + genero);

            if (!confirmarOperacao("Confirmar cadastro")) {
                System.out.println("Cadastro cancelado.");
                return;
            }

            // Inserir no banco
            try (Connection conn = ConexaoMySQL.getConexao();
                 PreparedStatement stmt = conn.prepareStatement(
                         "INSERT INTO Livro (Titulo, Autor, Ano, Genero, Status) VALUES (?, ?, ?, ?, 'Disponível')")) {

                stmt.setString(1, titulo);
                stmt.setString(2, autor);
                stmt.setInt(3, ano);
                stmt.setString(4, genero);

                int linhasAfetadas = stmt.executeUpdate();

                if (linhasAfetadas > 0) {
                    System.out.println("Livro cadastrado com sucesso!");
                } else {
                    System.out.println("Falha ao cadastrar livro.");
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

    // Permite editar dados de um livro.
    // O usuário escolhe qual campo deseja alterar.
    public static void editarLivro() {
        try {
            System.out.println("\n=== EDITAR LIVRO ===");

            // Listar livros para seleção
            listarLivros();

            // Solicitar ID do livro
            System.out.print("\nDigite o ID do livro a editar: ");
            int idLivro = Integer.parseInt(scanner.nextLine());

            // Verificar se livro existe
            if (!livroExiste(idLivro)) {
                System.out.println("Livro não encontrado!");
                return;
            }

            // Mostrar dados atuais
            Livro livro = buscarLivro(idLivro);
            if (livro == null) {
                System.out.println("Erro ao buscar dados do livro.");
                return;
            }

            System.out.println("\nDados atuais:");
            System.out.println("1. Título: " + livro.getTitulo());
            System.out.println("2. Autor: " + livro.getAutor());
            System.out.println("3. Ano: " + livro.getAno());
            System.out.println("4. Gênero: " + livro.getGenero());
            System.out.println("5. Status: " + livro.getStatus());

            // Menu de edição
            System.out.println("\nO que deseja editar?");
            System.out.println("1. Título");
            System.out.println("2. Autor");
            System.out.println("3. Ano");
            System.out.println("4. Gênero");
            System.out.println("5. Status");
            System.out.println("0. Cancelar");
            System.out.print("Escolha: ");

            int opcao = Integer.parseInt(scanner.nextLine());

            String campo ;
            Object novoValor ;

            switch (opcao) {
                case 1:
                    System.out.print("Novo título: ");
                    novoValor = scanner.nextLine().trim();
                    campo = "Titulo";
                    break;
                case 2:
                    System.out.print("Novo autor: ");
                    novoValor = scanner.nextLine().trim();
                    campo = "Autor";
                    break;
                case 3:
                    int ano;
                    while (true) {
                        System.out.print("Novo ano: ");
                        try {
                            ano = Integer.parseInt(scanner.nextLine());
                            if (ano <= 0 || ano > Calendar.getInstance().get(Calendar.YEAR) + 1) {
                                System.out.println("Ano inválido!");
                            } else {
                                novoValor = ano;
                                break;
                            }
                        } catch (NumberFormatException e) {
                            System.out.println("Digite um ano válido!");
                        }
                    }
                    campo = "Ano";
                    break;
                case 4:
                    System.out.print("Novo gênero: ");
                    novoValor = scanner.nextLine().trim();
                    campo = "Genero";
                    break;
                case 5:
                    System.out.println("Status disponíveis:");
                    System.out.println("1. Disponível");
                    System.out.println("2. Emprestado");
                    System.out.println("3. Em manutenção");
                    System.out.print("Escolha: ");
                    int statusOpcao = Integer.parseInt(scanner.nextLine());

                    switch (statusOpcao) {
                        case 1: novoValor = "Disponível"; break;
                        case 2: novoValor = "Emprestado"; break;
                        case 3: novoValor = "Em manutenção"; break;
                        default:
                            System.out.println("Opção inválida.");
                            return;
                    }
                    campo = "Status";
                    break;
                case 0:
                    System.out.println("Edição cancelada.");
                    return;
                default:
                    System.out.println("Opção inválida.");
                    return;
            }

            if (!confirmarOperacao("Confirmar alteração")) {
                System.out.println("Edição cancelada.");
                return;
            }

            // Atualizar no banco
            try (Connection conn = ConexaoMySQL.getConexao();
                 PreparedStatement stmt = conn.prepareStatement(
                         "UPDATE Livro SET " + campo + " = ? WHERE ID = ?")) {

                if (novoValor instanceof Integer) {
                    stmt.setInt(1, (Integer) novoValor);
                } else {
                    stmt.setString(1, novoValor.toString());
                }
                stmt.setInt(2, idLivro);

                int linhasAfetadas = stmt.executeUpdate();

                if (linhasAfetadas > 0) {
                    System.out.println("Livro atualizado com sucesso!");
                } else {
                    System.out.println("Falha ao atualizar livro.");
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

    // Exclui um livro
    public static void excluirLivro() {
        try {
            System.out.println("\n=== EXCLUIR LIVRO ===");

            // Listar livros para seleção
            listarLivros();

            // Solicitar ID do livro
            System.out.print("\nDigite o ID do livro a excluir: ");
            int idLivro = Integer.parseInt(scanner.nextLine());

            // Verificar se livro existe
            if (!livroExiste(idLivro)) {
                System.out.println("Livro não encontrado!");
                return;
            }

            // Mostrar dados do livro
            Livro livro = buscarLivro(idLivro);
            if (livro != null) {
                System.out.println("\nDados do livro:");
                System.out.println("ID: " + livro.getId());
                System.out.println("Título: " + livro.getTitulo());
                System.out.println("Autor: " + livro.getAutor());
                System.out.println("Status: " + livro.getStatus());
            }

            if (!confirmarOperacao("Tem certeza que deseja excluir este livro?")) {
                System.out.println("Exclusão cancelada.");
                return;
            }

            // Verificar se o livro está emprestado
            assert livro != null;
            if (livro.getStatus().equals("Emprestado")) {
                System.out.println("Não é possível excluir um livro emprestado!");
                return;
            }

            // Excluir livro
            try (Connection conn = ConexaoMySQL.getConexao();
                 PreparedStatement stmt = conn.prepareStatement(
                         "DELETE FROM Livro WHERE ID = ?")) {

                stmt.setInt(1, idLivro);
                int linhas = stmt.executeUpdate();

                if (linhas > 0) {
                    System.out.println("Livro excluído com sucesso!");
                } else {
                    System.out.println("Falha ao excluir livro.");
                }
            }
        } catch (NumberFormatException e) {
            System.out.println("ID deve ser um número!");
        } catch (SQLException e) {
            System.err.println("Erro no banco de dados: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Erro inesperado: " + e.getMessage());
        } finally {
            pausar();
        }
    }

    // Lista todos os livros cadastrados.
    // Mostra ID, título, autor, ano, gênero e status, ordenados por ID.
    public static void listarLivros() {
        try {
            System.out.println("\n=== LISTA DE LIVROS ===");

            try (Connection conn = ConexaoMySQL.getConexao();
                 Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(
                         "SELECT ID, Titulo, Autor, Ano, Genero, Status FROM Livro ORDER BY ID")) {

                boolean encontrou = false;
                while (rs.next()) {
                    encontrou = true;
                    System.out.println("\nID: " + rs.getInt("ID"));
                    System.out.println("Título: " + rs.getString("Titulo"));
                    System.out.println("Autor: " + rs.getString("Autor"));
                    System.out.println("Ano: " + rs.getInt("Ano"));
                    System.out.println("Gênero: " + rs.getString("Genero"));
                    System.out.println("Status: " + rs.getString("Status"));
                    System.out.println("   ");
                    System.out.println("---");
                }

                if (!encontrou) {
                    System.out.println("Nenhum livro cadastrado.");
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar livros: " + e.getMessage());
        } finally {
            pausar();
        }
    }

    // Listo todos os livros que estão disponíveis
    // Mostra ID, título, autor, ano, gênero e status, ordenados por ID.
    public static void listarLivrosDisponiveis() {
        try {
            System.out.println("\n=== LIVROS DISPONÍVEIS ===");

            try (Connection conn = ConexaoMySQL.getConexao();
                 PreparedStatement stmt = conn.prepareStatement(
                         "SELECT ID, Titulo, Autor, Ano, Genero FROM Livro WHERE Status = 'Disponível' ORDER BY ID");
                 ResultSet rs = stmt.executeQuery()) {

                boolean encontrou = false;
                while (rs.next()) {
                    encontrou = true;
                    System.out.println("\nID: " + rs.getInt("ID"));
                    System.out.println("Título: " + rs.getString("Titulo"));
                    System.out.println("Autor: " + rs.getString("Autor"));
                    System.out.println("Ano: " + rs.getInt("Ano"));
                    System.out.println("Gênero: " + rs.getString("Genero"));
                    System.out.println("   ");
                    System.out.println("---");
                }

                if (!encontrou) {
                    System.out.println("Nenhum livro disponível no momento.");
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar livros: " + e.getMessage());
        } finally {
            pausar();
        }
    }

// ================= MÉTODOS PARA EMPRESTIMOS ================= //

    // Realiza o processo de empréstimo de um livro a um aluno.
    public static void fazerEmprestimo() {
        try {
            System.out.println("\n=== NOVO EMPRÉSTIMO ===");

            // Lista livros disponíveis
            listarLivrosDisponiveis();
            List<Integer> idsDisponiveis = obterIdsLivrosDisponiveis();
            if (idsDisponiveis.isEmpty()) {
                System.out.println("Não há livros disponíveis para empréstimo.");
                return;
            }

            // Solicita o RGM do aluno e valida sua existência
            System.out.print("\nDigite o RGM do aluno: ");
            int rgm = Integer.parseInt(scanner.nextLine());

            if (!alunoExiste(rgm)) {
                System.out.println("Aluno não encontrado!");
                return;
            }

            // Solicita o ID do livro e valida sua disponibilidade
            System.out.print("Digite o ID do livro que deseja emprestar: ");
            int idLivro = Integer.parseInt(scanner.nextLine());

            if (!idsDisponiveis.contains(idLivro)) {
                System.out.println("Livro não disponível ou ID inválido!");
                return;
            }

            Livro livro = buscarLivro(idLivro);
            if (livro == null) {
                System.out.println("Erro ao buscar informações do livro.");
                return;
            }

            // Confirmação do empréstimo
            System.out.println("\nConfirmar empréstimo:");
            System.out.println("Aluno RGM: " + rgm);
            System.out.println("Livro: " + livro.getTitulo());

            if (!confirmarOperacao("Confirmar empréstimo")) {
                System.out.println("Empréstimo cancelado.");
                return;
            }

            // Regristra o Empréstimo no sistema
            boolean sucesso = registrarEmprestimo(rgm, idLivro);

            if (sucesso) {
                System.out.println("\nEmpréstimo realizado com sucesso!");
                System.out.println("Livro: " + livro.getTitulo());
                System.out.println("Prazo de devolução: 7 dias");
            } else {
                System.out.println("Falha ao registrar empréstimo.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Por favor, digite um número válido.");
        } catch (SQLException e) {
            System.err.println("Erro no banco de dados: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Erro inesperado: " + e.getMessage());
        } finally {
            pausar();
        }
    }

    // Finaliza um empréstimo ativo e atualiza os dados no banco de dados.
    public static void finalizarEmprestimo() {
        try {
            System.out.println("\n=== FINALIZAR EMPRÉSTIMO ===");

            // Lista os empréstimos ativos
            listarEmprestimosAtivos();

            // Solicita o ID do empréstimo e valida sua existência e status
            System.out.print("\nDigite o ID do empréstimo para finalizar: ");
            int idEmprestimo = Integer.parseInt(scanner.nextLine());

            if (!emprestimoAtivoExiste(idEmprestimo)) {
                System.out.println("Empréstimo não encontrado ou já finalizado!");
                return;
            }

            Emprestimo emprestimo = buscarEmprestimo(idEmprestimo);
            if (emprestimo == null) {
                System.out.println("Erro ao buscar informações do empréstimo.");
                return;
            }

            Livro livro = buscarLivro(emprestimo.getIdLivro());
            Aluno aluno = buscarAluno(emprestimo.getRgmAluno());

            // Exibe detalhes do empréstimo, aluno e livro envolvidos
            System.out.println("\nDados do empréstimo:");
            System.out.println("ID: " + emprestimo.getId());
            assert aluno != null;
            System.out.println("Aluno: " + aluno.getNome() + " (RGM: " + aluno.getRgm() + ")");
            assert livro != null;
            System.out.println("Livro: " + livro.getTitulo());
            System.out.println("Data de retirada: " + emprestimo.getDataRetirada());
            System.out.println("Data prevista de devolução: " + emprestimo.getDataDevolucao());

            // Verifica se o empréstimo está atradaso em relação com a Data de devolução
            Date hoje = new Date();
            if (hoje.after(emprestimo.getDataDevolucao())) {
                System.out.println("ATENÇÃO: Empréstimo em atraso!");
            }

            // Confirma a finalização com o usuário
            if (!confirmarOperacao("Confirmar finalização do empréstimo")) {
                System.out.println("Operação cancelada.");
                return;
            }

            // Atualiza o status do empréstimo e do livro dentro do BD
            Connection conn = null;
            try {
                conn = ConexaoMySQL.getConexao();
                conn.setAutoCommit(false);

                try (PreparedStatement stmtEmprestimo = conn.prepareStatement(
                        "UPDATE Emprestimo SET Status = 'Concluído' WHERE ID = ?")) {
                    stmtEmprestimo.setInt(1, idEmprestimo);
                    stmtEmprestimo.executeUpdate();
                }

                try (PreparedStatement stmtLivro = conn.prepareStatement(
                        "UPDATE Livro SET Status = 'Disponível' WHERE ID = ?")) {
                    stmtLivro.setInt(1, emprestimo.getIdLivro());
                    stmtLivro.executeUpdate();
                }

                conn.commit();
                System.out.println("Empréstimo finalizado com sucesso!");

            } catch (SQLException e) {
                if (conn != null) {
                    try {
                        conn.rollback();
                    } catch (SQLException ex) {
                        System.err.println("Erro ao reverter transação: " + ex.getMessage());
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
            System.out.println("ID deve ser um número!");
        } catch (SQLException e) {
            System.err.println("Erro no banco de dados: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Erro inesperado: " + e.getMessage());
        } finally {
            pausar();
        }
    }

     // Lista todos os empréstimos ativos registrados no sistema.
    public static void listarEmprestimosAtivos() {
        try {
            System.out.println("\n=== EMPRÉSTIMOS ATIVOS ===");

            // Consulta empréstimos com status "Ativo"
            try (Connection conn = ConexaoMySQL.getConexao();
                PreparedStatement stmt = conn.prepareStatement(
                        "SELECT e.ID, c.RGM, c.Nome AS NomeAluno, l.ID AS IDLivro, l.Titulo, e.DataRetirada, e.DataDevolucao " +
                                "FROM Emprestimo e " +
                                "JOIN ClienteEmprestimo ce ON e.ID = ce.ID_Emprestimo " +
                                "JOIN Cliente c ON ce.RGM_Cliente = c.RGM " +
                                "JOIN EmprestimoLivro el ON e.ID = el.ID_Emprestimo " +
                                "JOIN Livro l ON el.ID_Livro = l.ID " +
                                "WHERE e.Status = 'Ativo' " +
                                "ORDER BY e.DataDevolucao");
                ResultSet rs = stmt.executeQuery()) {

                boolean encontrou = false;
                while (rs.next()) {
                    encontrou = true; // Exibe os dados de cada empréstimo: ID, aluno, livro, datas
                    System.out.println("\nID Empréstimo: " + rs.getInt("ID"));
                    System.out.println("Aluno: " + rs.getString("NomeAluno") + " (RGM: " + rs.getInt("RGM") + ")");
                    System.out.println("Livro: " + rs.getString("Titulo") + " (ID: " + rs.getInt("IDLivro") + ")");
                    System.out.println("Retirada: " + rs.getDate("DataRetirada"));
                    System.out.println("Devolução: " + rs.getDate("DataDevolucao"));
                    System.out.println("---");

                    // Indica se o empréstimo está atrasado
                    if (rs.getDate("DataDevolucao").before(new Date())) {
                        System.out.println("ATRASADO!");
                    }
                }

                if (!encontrou) {
                    System.out.println("Nenhum empréstimo ativo no momento.");
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar empréstimos: " + e.getMessage());
        } finally {
            pausar();
        }
    }

    // Lista todo o histórico de empréstimos registrados no sistema.
    public static void listarHistoricoCompleto() {
        try {
            System.out.println("\n=== HISTÓRICO DE EMPRÉSTIMOS ===");

            //Consulta todos os empréstimos, independentemente do status
            //Ordena a lista pela data de retirada, da mais recente para a mais antiga;
            try (Connection conn = ConexaoMySQL.getConexao();
                PreparedStatement stmt = conn.prepareStatement(
                        "SELECT e.ID, c.RGM, c.Nome AS NomeAluno, l.ID AS IDLivro, l.Titulo, e.DataRetirada, e.DataDevolucao, e.Status " +
                        "FROM Emprestimo e " +
                        "JOIN ClienteEmprestimo ce ON e.ID = ce.ID_Emprestimo " +
                        "JOIN Cliente c ON ce.RGM_Cliente = c.RGM " +
                        "JOIN EmprestimoLivro el ON e.ID = el.ID_Emprestimo " +
                        "JOIN Livro l ON el.ID_Livro = l.ID " +
                        "ORDER BY e.DataRetirada DESC");
                ResultSet rs = stmt.executeQuery()) {

                boolean encontrou = false;

                //Exibe os dados de cada empréstimo: ID, aluno, livro, datas e status
                while (rs.next()) {
                    encontrou = true;
                    System.out.println("\nID: " + rs.getInt("ID"));
                    System.out.println("Aluno: " + rs.getString("NomeAluno") + " (RGM: " + rs.getInt("RGM") + ")");
                    System.out.println("Livro: " + rs.getString("Titulo") + " (ID: " + rs.getInt("IDLivro") + ")");
                    System.out.println("Retirada: " + rs.getDate("DataRetirada"));
                    System.out.println("Devolução: " + rs.getDate("DataDevolucao"));
                    System.out.println("Status: " + rs.getString("Status"));
                    System.out.println("---");
                }

                if (!encontrou) {
                    System.out.println("Nenhum empréstimo registrado.");
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar histórico: " + e.getMessage());
        } finally {
            pausar();
        }
    }

    // Consulta e exibe o histórico de empréstimos de um aluno específico.
    public static void consultarHistoricoAluno() {
        try {
            System.out.println("\n=== HISTÓRICO DO ALUNO ===");

            // Solicitar RGM do aluno
            System.out.print("Digite seu RGM: ");
            int rgm = Integer.parseInt(scanner.nextLine());

            // Verificar se aluno existe
            if (!alunoExiste(rgm)) {
                System.out.println("Aluno não encontrado!");
                return;
            }

            // Buscar histórico
            try (Connection conn = ConexaoMySQL.getConexao();
                PreparedStatement stmt = conn.prepareStatement(
                        "SELECT e.ID, l.Titulo, e.DataRetirada, e.DataDevolucao, e.Status " +
                        "FROM Emprestimo e " +
                        "JOIN ClienteEmprestimo ce ON e.ID = ce.ID_Emprestimo " +
                        "JOIN EmprestimoLivro el ON e.ID = el.ID_Emprestimo " +
                        "JOIN Livro l ON el.ID_Livro = l.ID " +
                        "WHERE ce.RGM_Cliente = ? " +
                        "ORDER BY e.DataRetirada DESC")) {

                stmt.setInt(1, rgm);
                ResultSet rs = stmt.executeQuery();

                boolean encontrou = false;

                // Consulta todos os empréstimos do aluno, exibindo dados relevantes
                while (rs.next()) {
                    encontrou = true;
                    System.out.println("\nID Empréstimo: " + rs.getInt("ID"));
                    System.out.println("Livro: " + rs.getString("Titulo"));
                    System.out.println("Retirada: " + rs.getDate("DataRetirada"));
                    System.out.println("Devolução: " + rs.getDate("DataDevolucao"));
                    System.out.println("Status: " + rs.getString("Status"));
                    System.out.println("---");
                }

                if (!encontrou) {
                    System.out.println("Nenhum empréstimo registrado para este aluno.");
                }
            }
        } catch (NumberFormatException e) {
            System.out.println("RGM deve ser um número!");
        } catch (SQLException e) {
            System.err.println("Erro ao buscar histórico: " + e.getMessage());
        } finally {
            pausar();
        }
    }

    // ================= CONSULTAS AVANÇADAS ================= //

    // Consulta livros por Gênero
    public static void consultarLivrosPorGenero() {
        try {
            System.out.println("\n=== LIVROS POR GÊNERO ===");

            // Listar gêneros disponíveis
            System.out.println("Gêneros disponíveis:");
            try (Connection conn = ConexaoMySQL.getConexao();
                 Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery("SELECT DISTINCT Genero FROM Livro ORDER BY Genero")) {

                while (rs.next()) {
                    System.out.println("- " + rs.getString("Genero"));
                }
            }

            // Solicitar gênero
            System.out.print("\nDigite o gênero para filtrar: ");
            String genero = scanner.nextLine();

            // Buscar livros
            try (Connection conn = ConexaoMySQL.getConexao();
                 PreparedStatement stmt = conn.prepareStatement(
                         "SELECT ID, Titulo, Autor, Ano, Status FROM Livro WHERE Genero LIKE ? ORDER BY Titulo")) {

                stmt.setString(1, "%" + genero + "%");
                ResultSet rs = stmt.executeQuery();

                boolean encontrou = false;
                while (rs.next()) {
                    encontrou = true;
                    System.out.println("\nID: " + rs.getInt("ID"));
                    System.out.println("Título: " + rs.getString("Titulo"));
                    System.out.println("Autor: " + rs.getString("Autor"));
                    System.out.println("Ano: " + rs.getInt("Ano"));
                    System.out.println("Status: " + rs.getString("Status"));
                    System.out.println("---");
                }

                if (!encontrou) {
                    System.out.println("Nenhum livro encontrado para este gênero.");
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar livros: " + e.getMessage());
        } finally {
            pausar();
        }
    }

    // Consulta livros por Autor
    public static void consultarLivrosPorAutor() {
        try {
            System.out.println("\n=== LIVROS POR AUTOR ===");

            // Listar autores disponíveis
            System.out.println("Autores disponíveis:");
            try (Connection conn = ConexaoMySQL.getConexao();
                 Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery("SELECT DISTINCT Autor FROM Livro ORDER BY Autor")) {

                while (rs.next()) {
                    System.out.println("- " + rs.getString("Autor"));
                }
            }

            // Solicitar autor
            System.out.print("\nDigite o autor para filtrar: ");
            String autor = scanner.nextLine();

            // Buscar livros
            try (Connection conn = ConexaoMySQL.getConexao();
                 PreparedStatement stmt = conn.prepareStatement(
                         "SELECT ID, Titulo, Genero, Ano, Status FROM Livro WHERE Autor LIKE ? ORDER BY Titulo")) {

                stmt.setString(1, "%" + autor + "%");
                ResultSet rs = stmt.executeQuery();

                boolean encontrou = false;
                while (rs.next()) {
                    encontrou = true;
                    System.out.println("\nID: " + rs.getInt("ID"));
                    System.out.println("Título: " + rs.getString("Titulo"));
                    System.out.println("Gênero: " + rs.getString("Genero"));
                    System.out.println("Ano: " + rs.getInt("Ano"));
                    System.out.println("Status: " + rs.getString("Status"));
                    System.out.println("---");
                }

                if (!encontrou) {
                    System.out.println("Nenhum livro encontrado para este autor.");
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar livros: " + e.getMessage());
        } finally {
            pausar();
        }
    }

    //Consulta Livros por Status
    public static void consultarLivrosPorStatus() {
        try {
            System.out.println("\n=== LIVROS POR STATUS ===");

            //Seleciona o Status que quer consultar
            System.out.println("Status disponíveis:");
            System.out.println("1. Disponível");
            System.out.println("2. Emprestado");
            System.out.println("3. Em manutenção");
            System.out.print("Escolha: ");

            int opcao = Integer.parseInt(scanner.nextLine());
            String status;

            switch (opcao) {
                case 1: status = "Disponível"; break;
                case 2: status = "Emprestado"; break;
                case 3: status = "Em manutenção"; break;
                default:
                    System.out.println("Opção inválida.");
                    return;
            }

            // Buscar livros
            try (Connection conn = ConexaoMySQL.getConexao();
                 PreparedStatement stmt = conn.prepareStatement(
                         "SELECT ID, Titulo, Autor, Genero FROM Livro WHERE Status = ? ORDER BY Titulo")) {

                stmt.setString(1, status);
                ResultSet rs = stmt.executeQuery();

                boolean encontrou = false;
                while (rs.next()) {
                    encontrou = true;
                    System.out.println("\nID: " + rs.getInt("ID"));
                    System.out.println("Título: " + rs.getString("Titulo"));
                    System.out.println("Autor: " + rs.getString("Autor"));
                    System.out.println("Gênero: " + rs.getString("Genero"));
                    System.out.println("---");
                }

                if (!encontrou) {
                    System.out.println("Nenhum livro encontrado com este status.");
                }
            }
        } catch (NumberFormatException e) {
            System.out.println("Digite um número válido!");
        } catch (SQLException e) {
            System.err.println("Erro ao buscar livros: " + e.getMessage());
        } finally {
            pausar();
        }
    }

    // Consulta livros Atrasados
    public static void consultarAtrasados() {
        try {
            System.out.println("\n=== EMPRÉSTIMOS ATRASADOS ===");

            // Seleciona livros atrasados por ordem de Data de devolução
            try (Connection conn = ConexaoMySQL.getConexao();
                 PreparedStatement stmt = conn.prepareStatement(
                         "SELECT e.ID, c.RGM, c.Nome AS NomeAluno, l.Titulo, e.DataRetirada, e.DataDevolucao " +
                                 "FROM Emprestimo e " +
                                 "JOIN ClienteEmprestimo ce ON e.ID = ce.ID_Emprestimo " +
                                 "JOIN Cliente c ON ce.RGM_Cliente = c.RGM " +
                                 "JOIN EmprestimoLivro el ON e.ID = el.ID_Emprestimo " +
                                 "JOIN Livro l ON el.ID_Livro = l.ID " +
                                 "WHERE e.Status = 'Ativo' AND e.DataDevolucao < CURDATE() " +
                                 "ORDER BY e.DataDevolucao");
                 ResultSet rs = stmt.executeQuery()) {

                boolean encontrou = false;
                while (rs.next()) {
                    encontrou = true;
                    System.out.println("\nID Empréstimo: " + rs.getInt("ID"));
                    System.out.println("Aluno: " + rs.getString("NomeAluno") + " (RGM: " + rs.getInt("RGM") + ")");
                    System.out.println("Livro: " + rs.getString("Titulo"));
                    System.out.println("Retirada: " + rs.getDate("DataRetirada"));
                    System.out.println("Devolução prevista: " + rs.getDate("DataDevolucao"));

                    // Calcular dias de atraso
                    long diff = new Date().getTime() - rs.getDate("DataDevolucao").getTime();
                    long diasAtraso = diff / (1000 * 60 * 60 * 24);
                    System.out.println("Dias em atraso: " + diasAtraso);
                    System.out.println("---");
                }

                if (!encontrou) {
                    System.out.println("Nenhum empréstimo em atraso no momento.");
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar empréstimos: " + e.getMessage());
        } finally {
            pausar();
        }
    }

    // ================= METODOS AUXILIARES ================= //

    // Metodo para verificar se aluno existe pelo RGM
    public static boolean alunoExiste(int rgm) throws SQLException {
        try (Connection conn = ConexaoMySQL.getConexao();
             PreparedStatement stmt = conn.prepareStatement("SELECT 1 FROM Cliente WHERE RGM = ?")) {
            stmt.setInt(1, rgm);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        }
    }

    // Metodo para buscar aluno pelo RGM, retornando nome, endereço e e-mail
    public static Aluno buscarAluno(int rgm) throws SQLException {
        try (Connection conn = ConexaoMySQL.getConexao();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT Nome, Endereco, Email FROM Cliente WHERE RGM = ?")) {
            stmt.setInt(1, rgm);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Aluno(rgm, rs.getString("Nome"),
                            rs.getString("Endereco"),
                            rs.getString("Email"));
                }
                return null;
            }
        }
    }

    // Metodo para verificar se livro existe por ordem de ID
    public static boolean livroExiste(int idLivro) throws SQLException {
        try (Connection conn = ConexaoMySQL.getConexao();
             PreparedStatement stmt = conn.prepareStatement("SELECT 1 FROM Livro WHERE ID = ?")) {
            stmt.setInt(1, idLivro);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        }
    }

    // Metodo para buscar livro por ordem de ID, retornando Título, autor, ano, gênero e status
    public static Livro buscarLivro(int idLivro) throws SQLException {
        try (Connection conn = ConexaoMySQL.getConexao();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT Titulo, Autor, Ano, Genero, Status FROM Livro WHERE ID = ?")) {
            stmt.setInt(1, idLivro);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Livro(idLivro, rs.getString("Titulo"),
                            rs.getString("Autor"),
                            rs.getInt("Ano"),
                            rs.getString("Genero"),
                            rs.getString("Status"));
                }
                return null;
            }
        }
    }

    // Metodo para verificar se Empréstimo existe, retornando por Status 'Ativo'
    public static boolean emprestimoAtivoExiste(int idEmprestimo) throws SQLException {
        try (Connection conn = ConexaoMySQL.getConexao();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT 1 FROM Emprestimo WHERE ID = ? AND Status = 'Ativo'")) {
            stmt.setInt(1, idEmprestimo);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        }
    }

    // Metodo para buscar empréstimo
    public static Emprestimo buscarEmprestimo(int idEmprestimo) throws SQLException {
        try (Connection conn = ConexaoMySQL.getConexao();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT e.DataRetirada, e.DataDevolucao, e.Status, " +
                             "ce.RGM_Cliente, el.ID_Livro " +
                             "FROM Emprestimo e " +
                             "JOIN ClienteEmprestimo ce ON e.ID = ce.ID_Emprestimo " +
                             "JOIN EmprestimoLivro el ON e.ID = el.ID_Emprestimo " +
                             "WHERE e.ID = ?")) {
            stmt.setInt(1, idEmprestimo);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Emprestimo(idEmprestimo,
                            rs.getInt("RGM_Cliente"),
                            rs.getInt("ID_Livro"),
                            rs.getDate("DataRetirada"),
                            rs.getDate("DataDevolucao"),
                            rs.getString("Status"));
                }
                return null;
            }
        }
    }

    // Metodo para obter Id de livros disponíveis
    public static List<Integer> obterIdsLivrosDisponiveis() throws SQLException {
        List<Integer> ids = new ArrayList<>();
        try (Connection conn = ConexaoMySQL.getConexao();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT ID FROM Livro WHERE Status = 'Disponível'");
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                ids.add(rs.getInt("ID"));
            }
        }
        return ids;
    }

    // Metodo para registrar Empréstimo em Banco de dados
    public static boolean registrarEmprestimo(int rgm, int idLivro) throws SQLException {
        Connection conn = null;
        try {
            conn = ConexaoMySQL.getConexao();
            conn.setAutoCommit(false);

            // 1. Inserir o empréstimo
            try (PreparedStatement stmtEmprestimo = conn.prepareStatement(
                    "INSERT INTO Emprestimo (Status, DataRetirada, DataDevolucao) " +
                            "VALUES ('Ativo', CURDATE(), DATE_ADD(CURDATE(), INTERVAL 7 DAY))",
                    PreparedStatement.RETURN_GENERATED_KEYS)) {

                stmtEmprestimo.executeUpdate();

                // 2. Obter o ID do empréstimo criado
                int idEmprestimo;
                try (ResultSet rs = stmtEmprestimo.getGeneratedKeys()) {
                    if (rs.next()) {
                        idEmprestimo = rs.getInt(1);
                    } else {
                        throw new SQLException("Falha ao obter ID do empréstimo");
                    }
                }

                // 3. Registrar relação Cliente-Empréstimo
                try (PreparedStatement stmtClienteEmprestimo = conn.prepareStatement(
                        "INSERT INTO ClienteEmprestimo (RGM_Cliente, ID_Emprestimo) VALUES (?, ?)")) {
                    stmtClienteEmprestimo.setInt(1, rgm);
                    stmtClienteEmprestimo.setInt(2, idEmprestimo);
                    stmtClienteEmprestimo.executeUpdate();
                }

                // 4. Registrar relação Empréstimo-Livro
                try (PreparedStatement stmtEmprestimoLivro = conn.prepareStatement(
                        "INSERT INTO EmprestimoLivro (ID_Livro, ID_Emprestimo) VALUES (?, ?)")) {
                    stmtEmprestimoLivro.setInt(1, idLivro);
                    stmtEmprestimoLivro.setInt(2, idEmprestimo);
                    stmtEmprestimoLivro.executeUpdate();
                }

                // 5. Atualizar status do livro
                try (PreparedStatement stmtAtualizarLivro = conn.prepareStatement(
                        "UPDATE Livro SET Status = 'Emprestado' WHERE ID = ?")) {
                    stmtAtualizarLivro.setInt(1, idLivro);
                    stmtAtualizarLivro.executeUpdate();
                }
            }

            conn.commit();
            return true;

        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    System.err.println("Erro ao reverter transação: " + ex.getMessage());
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
    }

    // Excluir Relacionamentos de alunos no Banco de Dados
    public static void excluirRelacionamentosAluno(Connection conn, int rgm) throws SQLException {
        // Excluir de ClienteEmprestimo
        try (PreparedStatement stmt = conn.prepareStatement(
                "DELETE FROM ClienteEmprestimo WHERE RGM_Cliente = ?")) {
            stmt.setInt(1, rgm);
            stmt.executeUpdate();
        }

        // Excluir de SupervisaoCliente (se existir)
        try (PreparedStatement stmt = conn.prepareStatement(
                "DELETE FROM SupervisaoCliente WHERE RGM_Cliente = ?")) {
            stmt.setInt(1, rgm);
            stmt.executeUpdate();
        }
    }

    // Metodo para verificar se e-mail é valido
    public static boolean validarEmail(String email) {
        return emailPattern.matcher(email).matches();
    }

    // Metodo para confirmar operação
    public static boolean confirmarOperacao(String mensagem) {
        System.out.print("\n" + mensagem + " (S/N)? ");
        String confirmacao = scanner.nextLine().trim().toUpperCase();
        return confirmacao.equals("S");
    }

    // Metodo para pausar
    public static void pausar() {
        System.out.print("\nPressione Enter para continuar...");
        scanner.nextLine();
    }

}