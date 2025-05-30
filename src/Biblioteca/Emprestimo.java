package src.Biblioteca;

import java.util.Date;

/**
 * Representa um empréstimo de livro realizado por um aluno.
 * Contém informações sobre o aluno, o livro emprestado,
 * datas de retirada e devolução, além do status do empréstimo.
 */
public class Emprestimo {
    private int id;
    private int rgmAluno;
    private int idLivro;
    private Date dataRetirada;
    private Date dataDevolucao;
    private String status;

    /**
     * Construtor da classe Emprestimo.
     *
     * @param id             Identificador do empréstimo.
     * @param rgmAluno       RGM (registro) do aluno que realizou o empréstimo.
     * @param idLivro        Identificador do livro emprestado.
     * @param dataRetirada   Data em que o livro foi retirado.
     * @param dataDevolucao  Data prevista ou realizada da devolução do livro.
     * @param status         Status atual do empréstimo (ex: "Ativo", "Devolvido").
     */
    public Emprestimo(int id, int rgmAluno, int idLivro, Date dataRetirada, Date dataDevolucao, String status) {
        this.id = id;
        this.rgmAluno = rgmAluno;
        this.idLivro = idLivro;
        this.dataRetirada = dataRetirada;
        this.dataDevolucao = dataDevolucao;
        this.status = status;
    }

    /**
     * Retorna o identificador do empréstimo.
     *
     * @return id do empréstimo.
     */
    public int getId() {
        return id;
    }

    /**
     * Retorna o RGM do aluno que realizou o empréstimo.
     *
     * @return RGM do aluno.
     */
    public int getRgmAluno() {
        return rgmAluno;
    }

    /**
     * Retorna o identificador do livro emprestado.
     *
     * @return id do livro.
     */
    public int getIdLivro() {
        return idLivro;
    }

    /**
     * Retorna a data em que o livro foi retirado.
     *
     * @return data da retirada.
     */
    public Date getDataRetirada() {
        return dataRetirada;
    }

    /**
     * Retorna a data prevista ou realizada da devolução do livro.
     *
     * @return data da devolução.
     */
    public Date getDataDevolucao() {
        return dataDevolucao;
    }

    /**
     * Retorna o status atual do empréstimo.
     *
     * @return status do empréstimo.
     */
    public String getStatus() {
        return status;
    }

    /**
     * Define ou atualiza o status do empréstimo.
     *
     * @param status novo status (ex: "Devolvido", "Atrasado").
     */
    public void setStatus(String status) {
        this.status = status;
    }
}
