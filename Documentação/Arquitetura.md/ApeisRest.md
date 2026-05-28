FUNCIONÁRIOS
  GET    /api/funcionarios              → lista apenas ativo = true
  GET    /api/funcionarios/todos        → lista incluindo inativos
  GET    /api/funcionarios/{id}
  POST   /api/funcionarios
  PUT    /api/funcionarios/{id}
  DELETE /api/funcionarios/{id}         → SET ativo = false (soft delete)

ÓRGÃOS
  GET    /api/orgaos
  GET    /api/orgaos/{id}
  POST   /api/orgaos
  PUT    /api/orgaos/{id}
  DELETE /api/orgaos/{id}              → SET ativo = false

CONTRATOS
  GET    /api/contratos
  GET    /api/contratos/{id}
  GET    /api/contratos/orgao/{id}     → contratos de um órgão
  POST   /api/contratos
  PUT    /api/contratos/{id}

ALOCAÇÕES
  GET    /api/alocacoes
  GET    /api/alocacoes/{id}
  GET    /api/alocacoes/funcionario/{id}
  GET    /api/alocacoes/contrato/{id}
  POST   /api/alocacoes
  PUT    /api/alocacoes/{id}

AUTH
  POST   /api/auth/login               → retorna token JWT
  POST   /api/auth/logout