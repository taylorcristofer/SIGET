-- ===================================
-- USUARIOS DO SISTEMA
-- ===================================
CREATE TABLE usuarios (
    id          BIGSERIAL PRIMARY KEY,
    nome        VARCHAR(100) NOT NULL,
    email       VARCHAR(100) UNIQUE NOT NULL,
    senha_hash  VARCHAR(255) NOT NULL,
    perfil      VARCHAR(20) NOT NULL,
    ativo       BOOLEAN DEFAULT TRUE,
    criado_em   TIMESTAMP DEFAULT NOW()
);

-- ===================================
-- FUNCIONARIOS
-- ===================================
CREATE TABLE funcionarios (
    id               BIGSERIAL PRIMARY KEY,
    nome             VARCHAR(100) NOT NULL,
    cpf              VARCHAR(14) UNIQUE NOT NULL,
    rg               VARCHAR(20),
    data_nascimento  DATE,
    telefone         VARCHAR(20),
    email            VARCHAR(100),
    funcao           VARCHAR(50) NOT NULL,
    status           VARCHAR(20) DEFAULT 'ATIVO',
    ativo            BOOLEAN DEFAULT TRUE,
    data_admissao    DATE,
    criado_em        TIMESTAMP DEFAULT NOW()
);

-- ===================================
-- ORGAOS PUBLICOS
-- ===================================
CREATE TABLE orgaos (
    id            BIGSERIAL PRIMARY KEY,
    nome          VARCHAR(150) NOT NULL,
    sigla         VARCHAR(20),
    cnpj          VARCHAR(18) UNIQUE,
    endereco      VARCHAR(200),
    cidade        VARCHAR(100),
    contato_nome  VARCHAR(100),
    contato_tel   VARCHAR(20),
    ativo         BOOLEAN DEFAULT TRUE
);

-- ===================================
-- CONTRATOS
-- ===================================
CREATE TABLE contratos (
    id            BIGSERIAL PRIMARY KEY,
    orgao_id      BIGINT NOT NULL REFERENCES orgaos(id),
    numero        VARCHAR(50) UNIQUE NOT NULL,
    data_inicio   DATE NOT NULL,
    data_fim      DATE,
    objeto        TEXT,
    valor_mensal  NUMERIC(12,2),
    ativo         BOOLEAN DEFAULT TRUE
);

-- ===================================
-- ALOCACOES
-- ===================================
CREATE TABLE alocacoes (
    id              BIGSERIAL PRIMARY KEY,
    funcionario_id  BIGINT NOT NULL REFERENCES funcionarios(id),
    contrato_id     BIGINT NOT NULL REFERENCES contratos(id),
    funcao_alocada  VARCHAR(50) NOT NULL,
    data_inicio     DATE NOT NULL,
    data_fim        DATE,
    turno           VARCHAR(20),
    status          VARCHAR(20) DEFAULT 'ATIVA',
    observacoes     TEXT,
    criado_em       TIMESTAMP DEFAULT NOW()
);

-- ===================================
-- ESCALAS
-- ===================================
CREATE TABLE escalas (
    id            BIGSERIAL PRIMARY KEY,
    alocacao_id   BIGINT NOT NULL REFERENCES alocacoes(id),
    dia_semana    VARCHAR(15) NOT NULL,
    hora_entrada  TIME NOT NULL,
    hora_saida    TIME NOT NULL,
    carga_horaria NUMERIC(4,2)
);

-- ===================================
-- DOCUMENTOS
-- ===================================
CREATE TABLE documentos (
    id               BIGSERIAL PRIMARY KEY,
    funcionario_id   BIGINT NOT NULL REFERENCES funcionarios(id),
    tipo             VARCHAR(50),
    descricao        VARCHAR(200),
    data_emissao     DATE,
    data_vencimento  DATE,
    arquivo_path     VARCHAR(300),
    alertar          BOOLEAN DEFAULT FALSE
);