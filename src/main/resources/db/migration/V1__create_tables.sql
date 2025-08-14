-- V1__create_tables.sql

-- Tabela principal de solicitacao
CREATE TABLE solicitacao (
    id UUID PRIMARY KEY,
    cliente_id UUID NOT NULL,
    produto_id UUID NOT NULL,
    categoria VARCHAR(50) NOT NULL,
    canal_venda VARCHAR(50),
    metodo_pagamento VARCHAR(50),
    status VARCHAR(50) NOT NULL,
    premio_mensal_total NUMERIC(12,2) NOT NULL,
    valor_segurado NUMERIC(18,2) NOT NULL,
    criado_em TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now(),
    finalizado_em TIMESTAMP WITH TIME ZONE,
    chave_idempotencia VARCHAR(100) UNIQUE,
    versao BIGINT NOT NULL DEFAULT 0
);

-- Índices adicionais
CREATE INDEX idx_solicitacao_cliente_id ON solicitacao (cliente_id);
CREATE INDEX idx_solicitacao_chave_idempotencia ON solicitacao (chave_idempotencia);

-- Coberturas
CREATE TABLE cobertura (
    id BIGSERIAL PRIMARY KEY,
    solicitacao_id UUID REFERENCES solicitacao(id) ON DELETE CASCADE,
    nome TEXT NOT NULL,
    valor NUMERIC(18,2) NOT NULL
);

-- Assistências
CREATE TABLE assistencia (
    id BIGSERIAL PRIMARY KEY,
    solicitacao_id UUID REFERENCES solicitacao(id) ON DELETE CASCADE,
    descricao TEXT NOT NULL
);

-- Histórico de movimentações
CREATE TABLE historico_movimentacao (
    id BIGSERIAL PRIMARY KEY,
    solicitacao_id UUID NOT NULL REFERENCES solicitacao(id) ON DELETE CASCADE,
    status VARCHAR(50) NOT NULL,
    data_movimentacao TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now()
);