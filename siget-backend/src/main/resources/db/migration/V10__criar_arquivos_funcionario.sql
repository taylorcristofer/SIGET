CREATE TABLE arquivos_funcionario (
    id              BIGSERIAL PRIMARY KEY,
    funcionario_id  BIGINT NOT NULL REFERENCES funcionarios(id),
    categoria       VARCHAR(30) NOT NULL,
    nome_arquivo    VARCHAR(255) NOT NULL,
    s3_key          VARCHAR(500),
    url             VARCHAR(1000),
    mes_referencia  VARCHAR(7),
    ativo           BOOLEAN DEFAULT TRUE,
    criado_em       TIMESTAMP DEFAULT NOW()
);