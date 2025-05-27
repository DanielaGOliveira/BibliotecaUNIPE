package Biblioteca;

public class Aluno {
    private int rgm;
    private String nome;
    private String endereco;
    private String email;

    public Aluno(int rgm, String nome, String endereco, String email) {
        this.rgm = rgm;
        this.nome = nome;
        this.endereco = endereco;
        this.email = email;
    }

    // Getters e Setters
    public int getRgm() { return rgm; }
    public String getNome() { return nome; }
    public String getEndereco() { return endereco; }
    public String getEmail() { return email; }

    public void setNome(String nome) { this.nome = nome; }
    public void setEndereco(String endereco) { this.endereco = endereco; }
    public void setEmail(String email) { this.email = email; }
}
