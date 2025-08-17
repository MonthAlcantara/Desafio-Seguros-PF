-- V2__insert_initial_data.sql

-- Inserir solicitacao inicial para testes de idempotência
INSERT INTO solicitacao (
    id,
    cliente_id,
    produto_id,
    categoria,
    canal_venda,
    metodo_pagamento,
    status,
    premio_mensal_total,
    valor_segurado,
    criado_em,
    finalizado_em,
    chave_idempotencia,
    versao
) VALUES (
    'e3a1f9d6-7b29-4b91-9d59-1f53d5a6c2e7',   -- UUID aleatório para solicitacao
    '6b11b3d4-0e0a-4f54-931d-8f4e2c4c5f33',   -- UUID aleatório para cliente
    'd59b6fca-4b28-4d3e-9cfa-3821ebdf9e67',   -- UUID aleatório para produto
    'AUTO',
    'MOBILE',
    'CARTAO_CREDITO',
    'RECEBIDO',
    75.25,
    275000.50,
    NOW(),
    NOW(),
    'abc123',
    0
);

-- Coberturas associadas
INSERT INTO cobertura (solicitacao_id, nome, valor) VALUES
('e3a1f9d6-7b29-4b91-9d59-1f53d5a6c2e7', 'Roubo', 100000.25),
('e3a1f9d6-7b29-4b91-9d59-1f53d5a6c2e7', 'Perda Total', 100000.25),
('e3a1f9d6-7b29-4b91-9d59-1f53d5a6c2e7', 'Colisão com Terceiros', 75000.00);

-- Assistências associadas
INSERT INTO assistencia (solicitacao_id, descricao) VALUES
('e3a1f9d6-7b29-4b91-9d59-1f53d5a6c2e7', 'Guincho até 250km'),
('e3a1f9d6-7b29-4b91-9d59-1f53d5a6c2e7', 'Troca de Óleo'),
('e3a1f9d6-7b29-4b91-9d59-1f53d5a6c2e7', 'Chaveiro 24h');

-- Histórico inicial com datas diferentes para movimentações
INSERT INTO historico_movimentacao (solicitacao_id, status, data_movimentacao) VALUES
    ('e3a1f9d6-7b29-4b91-9d59-1f53d5a6c2e7', 'RECEBIDO', NOW())

