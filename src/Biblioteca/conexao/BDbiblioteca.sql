CREATE DATABASE IF NOT EXISTS BDbiblioteca;
USE BDbiblioteca;

CREATE TABLE Cliente (
    RGM INT PRIMARY KEY NOT NULL,
    Nome VARCHAR(100) NOT NULL,
    Endereco VARCHAR(200) NOT NULL,
    Email VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE Livro (
    ID INT PRIMARY KEY AUTO_INCREMENT,
    Status ENUM('Disponível', 'Emprestado', 'Manutenção') DEFAULT 'Disponível',
    Titulo VARCHAR(100) NOT NULL,
    Autor VARCHAR(100) NOT NULL,
    Ano INT NOT NULL,
    Genero VARCHAR(100)
);

CREATE TABLE Bibliotecario (
    ID INT PRIMARY KEY AUTO_INCREMENT,
    Nome VARCHAR(100) NOT NULL
);

CREATE TABLE Emprestimo (
    ID INT PRIMARY KEY AUTO_INCREMENT,
    Status ENUM('Ativo', 'Concluído', 'Atrasado') DEFAULT 'Ativo',
    DataRetirada DATE,
    DataDevolucao DATE
);

CREATE TABLE ClienteEmprestimo (
    RGM_Cliente INT,
    ID_Emprestimo INT,
    PRIMARY KEY (RGM_Cliente, ID_Emprestimo),
    FOREIGN KEY (RGM_Cliente) REFERENCES Cliente(RGM),
    FOREIGN KEY (ID_Emprestimo) REFERENCES Emprestimo(ID)
);

CREATE TABLE EmprestimoLivro (
    ID_Livro INT,
    ID_Emprestimo INT,
    PRIMARY KEY (ID_Livro, ID_Emprestimo),
    FOREIGN KEY (ID_Livro) REFERENCES Livro(ID),
    FOREIGN KEY (ID_Emprestimo) REFERENCES Emprestimo(ID)
);

CREATE TABLE SupervisaoCliente (
    RGM_Cliente INT,
    ID_Bibliotecario INT,
    PRIMARY KEY (RGM_Cliente, ID_Bibliotecario),
    FOREIGN KEY (RGM_Cliente) REFERENCES Cliente(RGM),
    FOREIGN KEY (ID_Bibliotecario) REFERENCES Bibliotecario(ID)
);

CREATE TABLE ControleLivro (
    ID_Livro INT,
    ID_Bibliotecario INT,
    PRIMARY KEY (ID_Livro, ID_Bibliotecario),
    FOREIGN KEY (ID_Livro) REFERENCES Livro(ID),
    FOREIGN KEY (ID_Bibliotecario) REFERENCES Bibliotecario(ID)
);



INSERT INTO Cliente (RGM, Nome, Endereco, Email) VALUES 
(39291057, 'Daniela Gomes', 'Rua Radialista, 61', 'danielagomes@gmail.com'),
(38606356, 'Maria Eduarda', 'Rua Itambé, 48', 'mariaeduarda17@gmail.com'),
(38322731, 'Maria Clara', 'Rua Manaíra, 14', 'mariaclara@gmail.com'),
(37604589, 'Leandra Lima', 'Rua Mangabeira, 72', 'leandralima@gmail.com'),
(38608588, 'Thais Rainara', 'Rua Tambaú, 39', 'thaisraynara@gmail.com');


INSERT INTO Livro (Titulo, Autor, Ano, Genero) VALUES
('A Culpa é das Estrelas', 'John Green', 2012, 'Romance'),
('Cidades de Papel', 'John Green', 2008,'Mistério'),
('Will & Will: Um nome, Um Destino', 'John Green', 2010, 'LGBT+ / Romance'),
('Harry Potter e a Pedra Filosofal', 'J.K. Rowling', 1997, 'Fantasia'),
('Harry Potter e a Câmara Secreta', 'J.K. Rowling', 1998, 'Fantasia'),
('Harry Potter e o Prisioneiro de Azkaban', 'J.K. Rowling', 1999, 'Fantasia'),
('Harry Potter e o Cálice de Fogo', 'J.K. Rowling', 2000, 'Fantasia'),
('Harry Potter e a Ordem da Fênix', 'J.K. Rowling', 2003, 'Fantasia'),
('Harry Potter e o Enigma do Príncipe', 'J.K. Rowling', 2005, 'Fantasia'),
('Harry Potter e as Relíquias da Morte', 'J.K. Rowling', 2007, 'Fantasia'),
('1984', 'George Orwell', 1949, 'Distopia / Ficção Científica'),
('O Código da Vinci', 'Dan Brown', 2003, 'Suspense'),
('A Menina que Roubava Livros', 'Markus Zusak', 2005, 'Drama'),
('O Pequeno Príncipe', 'Antoine de Saint-Exupéry', 1943, 'Fábula'),
('It: A Coisa', 'Stephen King', 1986, 'Terror'),
('Como Fazer Amigos e Influenciar Pessoas', 'Dale Carnegie', 1936, 'Desenvolvimento Pessoal'),
('Sapiens: Uma Breve História da Humanidade', 'Yuval Noah Harari', 2011, 'História');

INSERT INTO Bibliotecario (Nome) VALUES
('Professor Douglas'),
('Professor Jonatas'),
('Professor Herriot');