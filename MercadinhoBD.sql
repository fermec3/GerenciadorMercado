CREATE TABLE categoria (
   id_categoria SERIAL PRIMARY KEY,
   nome_categoria VARCHAR (50) NOT NULL UNIQUE
); 

CREATE TABLE produto (
   id_produto SERIAL PRIMARY KEY,
   id_categoria INTEGER NOT NULL,
   nome_produto VARCHAR(100) NOT NULL,
   preco_produto NUMERIC (10,2) NOT NULL,

   CONSTRAINT fk_protudo_categoria
   FOREIGN KEY (id_categoria)
   REFERENCES categoria(id_categoria),

    CONSTRAINT ck_preco_produto
    CHECK(preco_produto > 0)
   
   
);   

 CREATE TABLE cliente(
    id_cliente SERIAL PRIMARY KEY,
	nome_cliente VARCHAR(100) NOT NULL,
	cpf_cliente VARCHAR (11) UNIQUE NOT NULL,
	email_cliente VARCHAR(100) UNIQUE
	
 );

 CREATE TABLE venda(
   id_venda SERIAL PRIMARY KEY,
   id_cliente INTEGER NOT NULL,
   data_venda DATE NOT NULL,
   total_venda NUMERIC (10,2),

   CONSTRAINT fk_venda_cliente
   FOREIGN KEY (id_cliente)
   REFERENCES cliente(id_cliente)
);
 
CREATE TABLE produto_venda(
   id_produto INTEGER NOT NULL,
   id_venda INTEGER NOT NULL,
   quantidade INTEGER NOT NULL,
   preco_unitario NUMERIC( 10,2) NOT NULL,

   CONSTRAINT pk_produto_venda
   PRIMARY KEY (id_produto, id_venda),
   
   CONSTRAINT fk_pv_produto
   FOREIGN KEY (id_produto)
   REFERENCES produto (id_produto),

   CONSTRAINT fk_pv_venda
   FOREIGN KEY (id_venda)
   REFERENCES venda (id_venda)
);

CREATE TABLE estoque(
   id_estoque SERIAL PRIMARY KEY,
   id_produto INTEGER NOT NULL,
   quantidade_estoque INTEGER NOT NULL,
   tipo_movimentacao VARCHAR (20) NOT NULL,
   data_movimentacao DATE NOT NULL,

   CONSTRAINT fk_estoque_produto
   FOREIGN KEY (id_produto)
   REFERENCES produto(id_produto), 

   CONSTRAINT ck_quantidade_estoque
   CHECK (quantidade_estoque >=0)
);

INSERT INTO categoria(nome_categoria)
VALUES
('Alimentos'),
('Bebidas'),
('Carnes'), 
('Doces'),
('Limpeza');

  INSERT INTO produto
(id_categoria,nome_produto,preco_produto)
VALUES
(2,'Coca-Cola 2L',12.50),
(2,'Guarana 2L',10.00),
(3,'Carne de Hamburguer',6.00),
(4,'Chocolate ao Leite',9.50),
(5,'Alvejante Liquido',4.00);

INSERT INTO cliente 
(nome_cliente,cpf_cliente,email_cliente)
VALUES
('Alessandra Silva','11122233344','bahia@gmail.com'),
('Anderson Souza','22233344455','flamengo@gmail.com'),
('Adelson Santos','33344455566','botafogo@gmail.com'),
('Alexa Costa','44455566677','palmeiras@gmail.com'),
('Alvaro Lima','55566677788','vasco@gmail.com');

INSERT INTO venda
(id_cliente,data_venda,total_venda)
VALUES
(1,'2026-06-01',0),
(2,'2026-06-02',0),
(3,'2026-06-03',0),
(4,'2026-06-04',0),
(5,'2026-06-05',0);

INSERT INTO produto_venda
(id_produto,id_venda,quantidade,preco_unitario)
VALUES
(1,1,2,12.50),
(2,1,1,10.00),
(3,2,3,6.00),
(4,3,5,9.50),
(5,4,1,4.00);

INSERT INTO estoque
(id_produto,quantidade_estoque,tipo_movimentacao,data_movimentacao)
VALUES
(1,50,'ENTRADA','2026-06-01'),
(2,30,'ENTRADA','2026-06-01'),
(3,20,'ENTRADA','2026-06-01'),
(4,40,'ENTRADA','2026-06-01'),
(5,10,'ENTRADA','2026-06-01');

UPDATE produto
SET preco_produto = 13.00
WHERE id_produto = 1;

UPDATE cliente
SET email_cliente = 'fluminence@gmail.com'
WHERE id_cliente = 2;

UPDATE estoque
SET quantidade_estoque = 45
WHERE id_produto = 1;

DELETE FROM estoque
WHERE id_produto = 5; 

SELECT
    p.id_produto,
    p.nome_produto,
    p.preco_produto,
    c.nome_categoria
	
FROM
    produto p
INNER JOIN 
    categoria c ON p.id_categoria = c.id_categoria;

SELECT
    v.id_venda,
    cl.nome_cliente,
    v.data_venda,
    v.total_venda
FROM
    venda v
INNER JOIN
    cliente cl ON v.id_cliente = cl.id_cliente;

SELECT
    cl.nome_cliente,
    p.nome_produto,
    pv.quantidade,
    pv.preco_unitario,
    (pv.quantidade * pv.preco_unitario) AS subtotal
FROM
     cliente cl
INNER JOIN venda v ON cl.id_cliente = v.id_cliente
INNER JOIN produto_venda pv ON v.id_venda = pv.id_venda
INNER JOIN produto p ON pv.id_produto = p.id_produto;
	
CREATE VIEW vw_vendas_detalhadas AS
SELECT
    cl.nome_cliente,
    v.id_venda,
    v.data_venda,
    p.nome_produto,
    pv.quantidade,
    pv.preco_unitario,
    (pv.quantidade * pv.preco_unitario) AS subtotal
FROM cliente cl
INNER JOIN venda v ON cl.id_cliente = v.id_cliente
INNER JOIN produto_venda pv ON v.id_venda = pv.id_venda
INNER JOIN produto p ON pv.id_produto = p.id_produto;

-- FUNCTION
CREATE OR REPLACE FUNCTION fn_calcular_total_venda(p_id_venda INTEGER)
RETURNS NUMERIC AS $$
DECLARE
    v_total NUMERIC;
BEGIN
    SELECT SUM(quantidade * preco_unitario)
    INTO v_total
    FROM produto_venda
    WHERE id_venda = p_id_venda;

    RETURN v_total;
END;
$$ LANGUAGE plpgsql;

-- PROCEDURE
CREATE OR REPLACE PROCEDURE pr_registrar_saida_estoque(p_id_venda INTEGER)
LANGUAGE plpgsql AS $$
DECLARE
    r RECORD;
BEGIN
    FOR r IN
        SELECT id_produto, quantidade
        FROM produto_venda
        WHERE id_venda = p_id_venda
    LOOP
        INSERT INTO estoque
            (id_produto, quantidade_estoque, tipo_movimentacao, data_movimentacao)
        VALUES
            (r.id_produto, r.quantidade, 'SAIDA', CURRENT_DATE);
    END LOOP;
END;
$$;


INSERT INTO estoque
(id_produto, quantidade_estoque, tipo_movimentacao, data_movimentacao)
VALUES
(1, 50, 'ENTRADA', '2026-06-01'),
(2, 30, 'ENTRADA', '2026-06-01'),
(3, 20, 'ENTRADA', '2026-06-01'),
(4, 40, 'ENTRADA', '2026-06-01');
