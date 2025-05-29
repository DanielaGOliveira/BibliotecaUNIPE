package Biblioteca; // Define o pacote onde esta classe está localizada
import java.util.Scanner; // Importa a classe Scanner para entrada de dados do usuário
import static Biblioteca.Metodos.*; // Importa todos os métodos estáticos da classe Metodos do pacote Biblioteca

public class AppBiblioteca { // Declara a classe principal do sistema de biblioteca
    private static final Scanner scanner = new Scanner(System.in); // Cria um Scanner para ler a entrada do usuário

    public static void main(String[] args) { // Método principal que inicia a execução do programa

        int perfil;
        do { // Início de um loop que exibe o menu até o usuário escolher sair
            System.out.println("\n=== SISTEMA DE BIBLIOTECA ==="); // Exibe uma mensagem no console
            System.out.println("1. Bibliotecário"); // Exibe uma mensagem no console
            System.out.println("2. Aluno"); // Exibe uma mensagem no console
            System.out.println("0. Sair"); // Exibe uma mensagem no console
            System.out.print("Escolha: "); // Exibe a mensagem no console sem pular linha

            try {
                perfil = Integer.parseInt(scanner.nextLine()); // Converte a entrada do usuário para um número inteiro
            } catch (NumberFormatException e) { // Trata erro caso o usuário digite algo que não seja número
                perfil = -1;
            }

            switch (perfil) { // Seleciona uma ação com base na escolha do usuário
                case 1 -> menuBibliotecario(); // Caso o usuário escolha 1, chama o menu do bibliotecário
                case 2 -> menuAluno(); // Caso o usuário escolha 2, chama o menu do aluno
                case 0 -> System.out.println("Saindo do sistema..."); // Exibe uma mensagem no console
                default -> System.out.println("Opção inválida. Tente novamente."); // Exibe uma mensagem no console
            }
        } while (perfil != 0); // Continua exibindo o menu enquanto o usuário não escolher sair
    }

    // MENUS
    private static void menuBibliotecario() {
        int opcao;
        do { // Início de um loop que exibe o menu até o usuário escolher sair
            System.out.println("\n=== MENU BIBLIOTECÁRIO ==="); // Exibe uma mensagem no console
            System.out.println("1. Gerenciar Alunos"); // Exibe uma mensagem no console
            System.out.println("2. Gerenciar Livros"); // Exibe uma mensagem no console
            System.out.println("3. Gerenciar Empréstimos"); // Exibe uma mensagem no console
            System.out.println("4. Consultas Avançadas"); // Exibe uma mensagem no console
            System.out.println("0. Voltar"); // Exibe uma mensagem no console
            System.out.print("Escolha: "); // Exibe a mensagem no console sem pular linha

            opcao = Integer.parseInt(scanner.nextLine()); // Converte a entrada do usuário para um número inteiro

            switch (opcao) {
                case 1 -> gerenciarAlunos(); // Caso o usuário escolha 1, chama o menu do bibliotecário
                case 2 -> gerenciarLivros(); // Caso o usuário escolha 2, chama o menu do aluno
                case 3 -> gerenciarEmprestimos();
                case 4 -> menuConsultas();
                case 0 -> System.out.println("Voltando..."); // Exibe uma mensagem no console
                default -> System.out.println("Opção inválida."); // Exibe uma mensagem no console
            }
        } while (opcao != 0);
    }

    private static void menuAluno() {
        int opcao;
        do { // Início de um loop que exibe o menu até o usuário escolher sair
            System.out.println("\n=== MENU ALUNO ==="); // Exibe uma mensagem no console
            System.out.println("1. Fazer Empréstimo"); // Exibe uma mensagem no console
            System.out.println("2. Consultar Histórico"); // Exibe uma mensagem no console
            System.out.println("3. Livros Disponíveis"); // Exibe uma mensagem no console
            System.out.println("0. Voltar"); // Exibe uma mensagem no console
            System.out.print("Escolha: "); // Exibe a mensagem no console sem pular linha

            opcao = Integer.parseInt(scanner.nextLine()); // Converte a entrada do usuário para um número inteiro

            switch (opcao) {
                case 1 -> fazerEmprestimo(); // Caso o usuário escolha 1, chama o menu do bibliotecário
                case 2 -> consultarHistoricoAluno(); // Caso o usuário escolha 2, chama o menu do aluno
                case 3 -> listarLivrosDisponiveis();
                case 0 -> System.out.println("Voltando..."); // Exibe uma mensagem no console
                default -> System.out.println("Opção inválida."); // Exibe uma mensagem no console
            }
        } while (opcao != 0);
    }

    // SUBMENUS
    private static void gerenciarAlunos() {
        int opcao;
        do { // Início de um loop que exibe o menu até o usuário escolher sair
            System.out.println("\n=== GERENCIAR ALUNOS ==="); // Exibe uma mensagem no console
            System.out.println("1. Cadastrar Aluno"); // Exibe uma mensagem no console
            System.out.println("2. Editar Aluno"); // Exibe uma mensagem no console
            System.out.println("3. Excluir Aluno"); // Exibe uma mensagem no console
            System.out.println("4. Listar Alunos"); // Exibe uma mensagem no console
            System.out.println("0. Voltar"); // Exibe uma mensagem no console
            System.out.print("Escolha: "); // Exibe a mensagem no console sem pular linha

            opcao = Integer.parseInt(scanner.nextLine()); // Converte a entrada do usuário para um número inteiro

            switch (opcao) {
                case 1 -> cadastrarAluno(); // Caso o usuário escolha 1, chama o menu do bibliotecário
                case 2 -> editarAluno(); // Caso o usuário escolha 2, chama o menu do aluno
                case 3 -> excluirAluno();
                case 4 -> listarAlunos();
                case 0 -> System.out.println("Voltando..."); // Exibe uma mensagem no console
                default -> System.out.println("Opção inválida."); // Exibe uma mensagem no console
            }
        } while (opcao != 0);
    }

    private static void gerenciarLivros() {
        int opcao;
        do { // Início de um loop que exibe o menu até o usuário escolher sair
            System.out.println("\n=== GERENCIAR LIVROS ==="); // Exibe uma mensagem no console
            System.out.println("1. Cadastrar Livro"); // Exibe uma mensagem no console
            System.out.println("2. Editar Livro"); // Exibe uma mensagem no console
            System.out.println("3. Excluir Livro"); // Exibe uma mensagem no console
            System.out.println("4. Listar Livros"); // Exibe uma mensagem no console
            System.out.println("0. Voltar"); // Exibe uma mensagem no console
            System.out.print("Escolha: "); // Exibe a mensagem no console sem pular linha

            opcao = Integer.parseInt(scanner.nextLine()); // Converte a entrada do usuário para um número inteiro

            switch (opcao) {
                case 1 -> cadastrarLivro(); // Caso o usuário escolha 1, chama o menu do bibliotecário
                case 2 -> editarLivro(); // Caso o usuário escolha 2, chama o menu do aluno
                case 3 -> excluirLivro();
                case 4 -> listarLivros();
                case 0 -> System.out.println("Voltando..."); // Exibe uma mensagem no console
                default -> System.out.println("Opção inválida."); // Exibe uma mensagem no console
            }
        } while (opcao != 0);
    }

    private static void gerenciarEmprestimos() {
        int opcao;
        do { // Início de um loop que exibe o menu até o usuário escolher sair
            System.out.println("\n=== GERENCIAR EMPRÉSTIMOS ==="); // Exibe uma mensagem no console
            System.out.println("1. Novo Empréstimo"); // Exibe uma mensagem no console
            System.out.println("2. Finalizar Empréstimo"); // Exibe uma mensagem no console
            System.out.println("3. Listar Empréstimos Ativos"); // Exibe uma mensagem no console
            System.out.println("4. Histórico Completo"); // Exibe uma mensagem no console
            System.out.println("0. Voltar"); // Exibe uma mensagem no console
            System.out.print("Escolha: "); // Exibe a mensagem no console sem pular linha

            opcao = Integer.parseInt(scanner.nextLine()); // Converte a entrada do usuário para um número inteiro

            switch (opcao) {
                case 1 -> fazerEmprestimo(); // Caso o usuário escolha 1, chama o menu do bibliotecário
                case 2 -> finalizarEmprestimo(); // Caso o usuário escolha 2, chama o menu do aluno
                case 3 -> listarEmprestimosAtivos();
                case 4 -> listarHistoricoCompleto();
                case 0 -> System.out.println("Voltando..."); // Exibe uma mensagem no console
                default -> System.out.println("Opção inválida."); // Exibe uma mensagem no console
            }
        } while (opcao != 0);
    }

    private static void menuConsultas() {
        int opcao;
        do { // Início de um loop que exibe o menu até o usuário escolher sair
            System.out.println("\n=== CONSULTAS AVANÇADAS ==="); // Exibe uma mensagem no console
            System.out.println("1. Livros por Gênero"); // Exibe uma mensagem no console
            System.out.println("2. Livros por Autor"); // Exibe uma mensagem no console
            System.out.println("3. Livros por Status"); // Exibe uma mensagem no console
            System.out.println("4. Alunos com Empréstimos Atrasados"); // Exibe uma mensagem no console
            System.out.println("0. Voltar"); // Exibe uma mensagem no console
            System.out.print("Escolha: "); // Exibe a mensagem no console sem pular linha

            opcao = Integer.parseInt(scanner.nextLine()); // Converte a entrada do usuário para um número inteiro

            switch (opcao) {
                case 1 -> consultarLivrosPorGenero(); // Caso o usuário escolha 1, chama o menu do bibliotecário
                case 2 -> consultarLivrosPorAutor(); // Caso o usuário escolha 2, chama o menu do aluno
                case 3 -> consultarLivrosPorStatus();
                case 4 -> consultarAtrasados();
                case 0 -> System.out.println("Voltando..."); // Exibe uma mensagem no console
                default -> System.out.println("Opção inválida."); // Exibe uma mensagem no console
            }
        } while (opcao != 0);
    }
}
