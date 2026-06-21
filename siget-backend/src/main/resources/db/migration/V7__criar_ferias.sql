CREATE TABLE ferias (
    id                BIGSERIAL PRIMARY KEY,
    funcionario_id    BIGINT NOT NULL REFERENCES funcionarios(id),
    data_inicio       DATE NOT NULL,
    data_fim          DATE NOT NULL,
    dias_direito      INT NOT NULL DEFAULT 30,
    dias_gozados      INT NOT NULL,
    ativo             BOOLEAN DEFAULT TRUE,
    criado_em         TIMESTAMP DEFAULT NOW()
);