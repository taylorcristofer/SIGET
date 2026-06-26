ALTER TABLE funcionarios
    ADD COLUMN IF NOT EXISTS rg_orgao_emissor  VARCHAR(20),
    ADD COLUMN IF NOT EXISTS rg_data_emissao   DATE,
    ADD COLUMN IF NOT EXISTS rg_data_vencimento DATE,
    ADD COLUMN IF NOT EXISTS pis_numero        VARCHAR(20),
    ADD COLUMN IF NOT EXISTS ctps_numero       VARCHAR(20),
    ADD COLUMN IF NOT EXISTS ctps_serie        VARCHAR(10);