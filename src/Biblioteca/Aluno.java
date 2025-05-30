package Biblioteca;

/**
 * Representa um aluno cadastrado na biblioteca.
 * Contém informações básicas como RGM, nome, endereço e e-mail.
 */
public class Aluno {
    private int rgm;
    private String nome;
    private String endereco;
    private String email;

    /**
     * Construtor para criar um novo objeto Aluno.
     *
     * @param rgm      Número de registro do aluno (RGM).
     * @param nome     Nome completo do aluno.
     * @param endereco Endereço residencial do aluno.
     * @param email    Endereço de e-mail do aluno.
     */
    public Aluno(int rgm, String nome, String endereco, String email) {
        this.rgm = rgm;
        this.nome = nome;
        this.endereco = endereco;
        this.email = email;
    }

    /**
     * Retorna o RGM do aluno.
     *
     * @return RGM como inteiro.
     */
    public int getRgm() {
        return rgm;
    }

    /**
     * Retorna o nome do aluno.
     *
     * @return Nome do aluno como String.
     */
    public String getNome() {
        return nome;
    }

    /**
     * Retorna o endereço do aluno.
     *
     * @return Endereço do aluno como String.
     */
    public String getEndereco() {
        return endereco;
    }

    /**
     * Retorna o e-mail do aluno.
     *
     * @return E-mail do aluno como String.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Define um novo nome para o aluno.
     *
     * @param nome Novo nome a ser definido.
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * Define um novo endereço para o aluno.
     *
     * @param endereco Novo endereço a ser definido.
     */
    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    /**
     * Define um novo e-mail para o aluno.
     *
     * @param email Novo e-mail a ser definido.
     */
    public void setEmail(String email) {
        this.email = email;
    }
}
