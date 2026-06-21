CREATE TABLE dependentes (
    id              BIGSERIAL PRIMARY KEY,
    funcionario_id  BIGINT NOT NULL REFERENCES funcionarios(id),
    nome            VARCHAR(100) NOT NULL,
    parentesco      VARCHAR(30) NOT NULL,
    data_nascimento DATE,
    cpf             VARCHAR(14),
    ativo           BOOLEAN DEFAULT TRUE,
    criado_em       TIMESTAMP DEFAULT NOW()
);