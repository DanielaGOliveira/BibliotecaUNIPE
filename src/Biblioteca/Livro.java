package Biblioteca;

public class Livro {
    private int id;
    private String titulo;
    private String autor;
    private int ano;
    private String genero;
    private String status;

    // Construtor, getters e setters
    public Livro(int id, String titulo, String autor, int ano, String genero, String status) {
        this.id = id;
        this.titulo = titulo;
        this.autor = autor;
        this.ano = ano;
        this.genero = genero;
        this.status = status;
    }

    // Getters e Setters
    public int getId() { return id; }
    public String getTitulo() { return titulo; }
    public String getAutor() { return autor; }
    public int getAno() { return ano; }
    public String getGenero() { return genero; }
    public String getStatus() { return status; }

    public void setStatus(String status) { this.status = status; }
}
