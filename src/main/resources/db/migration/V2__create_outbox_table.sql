-- V2__create_outbox_table.sql

-- Tabela para implementar o padrão Outbox
CREATE TABLE outbox (
    id UUID PRIMARY KEY,
    id_agregado VARCHAR(255) NOT NULL,
    tipo_agregado VARCHAR(255) NOT NULL,
    tipo_evento VARCHAR(255) NOT NULL,
    conteudo TEXT NOT NULL,
    data_criacao TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now()
);

-- Índice para otimizar a busca por eventos a serem processados
CREATE INDEX idx_outbox_data_criacao ON outbox (data_criacao);