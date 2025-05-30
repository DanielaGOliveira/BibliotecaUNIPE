package Biblioteca; // Define o pacote onde esta classe está localizada
import java.util.Scanner; // Importa a classe Scanner para entrada de dados do usuário
import static src.Biblioteca.Metodos.*; // Importa todos os métodos estáticos da classe Metodos do pacote Biblioteca

// Declara a classe principal do sistema de biblioteca
public class AppBiblioteca {
    private static final Scanner scanner = new Scanner(System.in); // Cria um Scanner para ler a entrada do usuário

    // Metodo principal que inicia a execução do programa
    public static void main(String[] args) {

        // Início de um loop que exibe o menu até o usuário escolher sair
        int perfil;
        do {
            System.out.println("\n=== SISTEMA DE BIBLIOTECA ===");
            System.out.println("1. Bibliotecário");
            System.out.println("2. Aluno");
            System.out.println("0. Sair");
            System.out.print("Escolha: ");

            // Converte a entrada do usuário para um número inteiro
            try {
                perfil = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) { // Trata erro caso o usuário digite algo que não seja número
                perfil = -1;
            }

            // Seleciona uma ação com base na escolha do usuário
            switch (perfil) {
                case 1 -> menuBibliotecario();
                case 2 -> menuAluno();
                case 0 -> System.out.println("Saindo do sistema...");
                default -> System.out.println("Opção inválida. Tente novamente.");
            }
        } while (perfil != 0); // Continua exibindo o menu enquanto o usuário não escolher sair
    }

    // MENUS
    private static void menuBibliotecario() {

        int opcao;

        // Início de um loop que exibe o menu até o usuário escolher sair
        do {
            System.out.println("\n=== MENU BIBLIOTECÁRIO ===");
            System.out.println("1. Gerenciar Alunos");
            System.out.println("2. Gerenciar Livros");
            System.out.println("3. Gerenciar Empréstimos");
            System.out.println("4. Consultas Avançadas");
            System.out.println("0. Voltar");
            System.out.print("Escolha: ");

            // Converte a entrada do usuário para um número inteiro
            opcao = Integer.parseInt(scanner.nextLine());

            // Seleciona uma ação com base na escolha do usuário
            switch (opcao) {
                case 1 -> gerenciarAlunos();
                case 2 -> gerenciarLivros();
                case 3 -> gerenciarEmprestimos();
                case 4 -> menuConsultas();
                case 0 -> System.out.println("Voltando...");
                default -> System.out.println("Opção inválida.");
            }
        } while (opcao != 0);
    }

    private static void menuAluno() {

        int opcao;

        // Início de um loop que exibe o menu até o usuário escolher sair
        do {
            System.out.println("\n=== MENU ALUNO ===");
            System.out.println("1. Fazer Empréstimo");
            System.out.println("2. Consultar Histórico");
            System.out.println("3. Livros Disponíveis");
            System.out.println("0. Voltar");
            System.out.print("Escolha: ");

            // Converte a entrada do usuário para um número inteiro
            opcao = Integer.parseInt(scanner.nextLine());

            // Seleciona uma ação com base na escolha do usuário
            switch (opcao) {
                case 1 -> fazerEmprestimo(); // Chama o metodo para fazer emprestimo
                case 2 -> consultarHistoricoAluno(); // Chama o metodo para consultar o histórico do aluno
                case 3 -> listarLivrosDisponiveis(); // Chama o metodo para ver livros Disponiveis
                case 0 -> System.out.println("Voltando..."); // Exibe uma mensagem no console
                default -> System.out.println("Opção inválida."); // Exibe uma mensagem no console
            }
        } while (opcao != 0);
    }

    // SUBMENUS
    private static void gerenciarAlunos() {

        int opcao;

        // Início de um loop que exibe o menu até o usuário escolher sair
        do {
            System.out.println("\n=== GERENCIAR ALUNOS ===");
            System.out.println("1. Cadastrar Aluno");
            System.out.println("2. Editar Aluno");
            System.out.println("3. Excluir Aluno");
            System.out.println("4. Listar Alunos");
            System.out.println("0. Voltar");
            System.out.print("Escolha: ");

            // Converte a entrada do usuário para um número inteiro
            opcao = Integer.parseInt(scanner.nextLine());

            // Seleciona uma ação com base na escolha do usuário
            switch (opcao) {
                case 1 -> cadastrarAluno();
                case 2 -> editarAluno();
                case 3 -> excluirAluno();
                case 4 -> listarAlunos();
                case 0 -> System.out.println("Voltando...");
                default -> System.out.println("Opção inválida.");
            }
        } while (opcao != 0);
    }

    private static void gerenciarLivros() {

        int opcao;

        // Início de um loop que exibe o menu até o usuário escolher sair
        do {
            System.out.println("\n=== GERENCIAR LIVROS ===");
            System.out.println("1. Cadastrar Livro");
            System.out.println("2. Editar Livro");
            System.out.println("3. Excluir Livro");
            System.out.println("4. Listar Livros");
            System.out.println("0. Voltar");
            System.out.print("Escolha: ");

            // Converte a entrada do usuário para um número inteiro
            opcao = Integer.parseInt(scanner.nextLine());

            // Seleciona uma ação com base na escolha do usuário
            switch (opcao) {
                case 1 -> cadastrarLivro();
                case 2 -> editarLivro();
                case 3 -> excluirLivro();
                case 4 -> listarLivros();
                case 0 -> System.out.println("Voltando...");
                default -> System.out.println("Opção inválida.");
            }
        } while (opcao != 0);
    }

    private static void gerenciarEmprestimos() {

        int opcao;

        // Início de um loop que exibe o menu até o usuário escolher sair
        do {
            System.out.println("\n=== GERENCIAR EMPRÉSTIMOS ===");
            System.out.println("1. Novo Empréstimo");
            System.out.println("2. Finalizar Empréstimo");
            System.out.println("3. Listar Empréstimos Ativos");
            System.out.println("4. Histórico Completo");
            System.out.println("0. Voltar");
            System.out.print("Escolha: ");

            // Converte a entrada do usuário para um número inteiro
            opcao = Integer.parseInt(scanner.nextLine());

            // Seleciona uma ação com base na escolha do usuário
            switch (opcao) {
                case 1 -> fazerEmprestimo();
                case 2 -> finalizarEmprestimo();
                case 3 -> listarEmprestimosAtivos();
                case 4 -> listarHistoricoCompleto();
                case 0 -> System.out.println("Voltando...");
                default -> System.out.println("Opção inválida.");
            }
        } while (opcao != 0);
    }

    private static void menuConsultas() {

        int opcao;

        // Início de um loop que exibe o menu até o usuário escolher sair
        do {
            System.out.println("\n=== CONSULTAS AVANÇADAS ===");
            System.out.println("1. Livros por Gênero");
            System.out.println("2. Livros por Autor");
            System.out.println("3. Livros por Status");
            System.out.println("4. Alunos com Empréstimos Atrasados");
            System.out.println("0. Voltar");
            System.out.print("Escolha: ");

            // Converte a entrada do usuário para um número inteiro
            opcao = Integer.parseInt(scanner.nextLine());

            // Seleciona uma ação com base na escolha do usuário
            switch (opcao) {
                case 1 -> consultarLivrosPorGenero();
                case 2 -> consultarLivrosPorAutor();
                case 3 -> consultarLivrosPorStatus();
                case 4 -> consultarAtrasados();
                case 0 -> System.out.println("Voltando...");
                default -> System.out.println("Opção inválida.");
            }
        } while (opcao != 0);
    }
}
