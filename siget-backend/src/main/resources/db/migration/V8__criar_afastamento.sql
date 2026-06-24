CREATE TABLE afastamentos (
    id              BIGSERIAL PRIMARY KEY,
    funcionario_id  BIGINT NOT NULL REFERENCES funcionarios(id),
    tipo            VARCHAR(30) NOT NULL, -- ATESTADO, SUSPENSAO, INSS, FALTA_JUSTIFICADA, LICENCA_MATERNIDADE
    data_inicio     DATE NOT NULL,
    data_fim        DATE NOT NULL,
    dias            INT NOT NULL,
    observacao      VARCHAR(300),
    ativo           BOOLEAN DEFAULT TRUE,
    criado_em       TIMESTAMP DEFAULT NOW()
);