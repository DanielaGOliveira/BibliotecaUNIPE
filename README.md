# Sistema de Biblioteca UNIPÊ

## Descrição
Este projeto é um sistema de gerenciamento de biblioteca desenvolvido em Java, utilizando MySQL como banco de dados. O sistema permite o gerenciamento de alunos, livros e empréstimos, além de oferecer funcionalidades para bibliotecários e alunos.

## Funcionalidades
### Para Bibliotecários:
- *Gerenciar Alunos*: Cadastrar, editar, excluir e listar alunos.
- *Gerenciar Livros*: Cadastrar, editar, excluir e listar livros.
- *Gerenciar Empréstimos*: Registrar novos empréstimos, finalizar empréstimos ativos e listar empréstimos.
- *Consultas Avançadas*: Filtrar livros por gênero, autor ou status, e consultar empréstimos atrasados.

### Para Alunos:
- *Fazer Empréstimo*: Solicitar empréstimo de livros disponíveis.
- *Consultar Histórico*: Visualizar histórico de empréstimos.
- *Livros Disponíveis*: Listar livros disponíveis para empréstimo.

## Tecnologias Utilizadas
- *Java*: Linguagem de programação principal.
- *MySQL*: Banco de dados para armazenamento das informações.
- *MySQL Connector/J*: Driver JDBC para conexão com o MySQL.
- *MySQL Workbench*: Ferramenta gráfica para gerenciamento do banco de dados.

## Pré-requisitos
- Java JDK 8 ou superior.
- MySQL Community Server instalado e configurado.
- MySQL Connector/J adicionado ao classpath do projeto.
- MySQL Workbench (opcional, para gerenciamento visual do banco de dados).

## Configuração do Banco de Dados
1. Execute o script BDbiblioteca.sql no MySQL para criar o banco de dados e as tabelas necessárias.
2. Configure as credenciais de acesso no arquivo ConexaoMySQL.java:<br>
   java<br>
   private static final String URL = "jdbc:mysql://localhost:3306/BDbiblioteca";<br>
   private static final String USER = "root"; // substitua pelo seu usuário<br>
   private static final String PASSWORD = " "; // substitua pela sua senha
   

## Como Executar
1. Clone o repositório ou baixe os arquivos do projeto.
2. Certifique-se de que o MySQL está em execução.
3. Compile e execute a classe AppBiblioteca.java:<br>
   bash<br>
   javac -cp .;mysql-connector-j-9.3.0.jar Biblioteca/AppBiblioteca.java<br>
   java -cp .;mysql-connector-j-9.3.0.jar Biblioteca.AppBiblioteca
   

## Estrutura do Projeto
- *Biblioteca*: Pacote principal contendo as classes do sistema.
  - *Aluno.java*: Representa um aluno.
  - *Livro.java*: Representa um livro.
  - *Emprestimo.java*: Representa um empréstimo.
  - *AppBiblioteca.java*: Classe principal com a interface do usuário.
  - *Metodos.java*: Contém as funcionalidades do sistema.
  - *conexao*: Pacote com a classe de conexão ao banco de dados.
    - *ConexaoMySQL.java*: Gerencia a conexão com o MySQL.
- *BDbiblioteca.sql*: Script SQL para criação do banco de dados.

## Observações
- O sistema já vem com dados de exemplo pré-cadastrados (alunos, livros e bibliotecários).
- Para alterar as configurações de conexão, edite o arquivo ConexaoMySQL.java.

## Autor
Alunas de Ciência da Computação UNIPÊ.
