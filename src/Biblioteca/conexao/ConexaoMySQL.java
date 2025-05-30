package src.Biblioteca.conexao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexaoMySQL {
    // Configurações do banco (substitua com seus dados)
    private static final String URL = "jdbc:mysql://localhost:3306/BDbiblioteca";
    private static final String USER = "root"; // ou seu usuário
    private static final String PASSWORD = "";

    public static Connection getConexao() throws SQLException {
        try {
            // Registra o driver JDBC
            Class.forName("com.mysql.cj.jdbc.Driver");
            // Retorna a conexão
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            throw new SQLException("Driver JDBC não encontrado!", e);
        }
    }

    // Teste de conexão
    public static void main(String[] args) {
        try (Connection conexao = ConexaoMySQL.getConexao()) {
            System.out.println("✅ Conexão com MySQL estabelecida!");
        } catch (SQLException e) {
            System.err.println("❌ Erro na conexão: " + e.getMessage());
        }
    }
}