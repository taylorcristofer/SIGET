sgt-backend/
├── src/main/java/com/sgt/
│   │
│   ├── SgtApplication.java
│   │
│   ├── config/                        ← configurações gerais
│   │   ├── CorsConfig.java
│   │   └── OpenApiConfig.java         (Swagger, futuro)
│   │
│   ├── security/                      ← tudo de segurança aqui
│   │   ├── SecurityConfig.java        (regras de rotas, filtros)
│   │   ├── JwtUtil.java               (gera e valida tokens)
│   │   ├── JwtFilter.java             (intercepta requisições)
│   │   ├── UserDetailsServiceImpl.java
│   │   └── AuthController.java        (login endpoint)
│   │
│   ├── controller/
│   │   ├── FuncionarioController.java
│   │   ├── OrgaoController.java
│   │   ├── ContratoController.java
│   │   └── AlocacaoController.java
│   │
│   ├── service/
│   │   ├── FuncionarioService.java
│   │   ├── OrgaoService.java
│   │   ├── ContratoService.java
│   │   └── AlocacaoService.java
│   │
│   ├── repository/
│   │   ├── FuncionarioRepository.java
│   │   ├── OrgaoRepository.java
│   │   ├── ContratoRepository.java
│   │   └── AlocacaoRepository.java
│   │
│   ├── entity/
│   │   ├── Funcionario.java
│   │   ├── Orgao.java
│   │   ├── Contrato.java
│   │   └── Alocacao.java
│   │
│   ├── dto/
│   │   ├── request/                   ← o que entra na API
│   │   │   ├── FuncionarioRequest.java
│   │   │   └── AlocacaoRequest.java
│   │   └── response/                  ← o que sai da API
│   │       ├── FuncionarioResponse.java
│   │       └── AlocacaoResponse.java
│   │
│   └── exception/
│       ├── GlobalExceptionHandler.java
│       └── RecursoNaoEncontradoException.java
│
├── src/main/resources/
│   ├── application.properties
│   └── db/migration/
│       ├── V1__criar_tabelas.sql
│       └── V2__inserir_dados_iniciais.sql
│
sgt-frontend/
├── index.html                         (dashboard)
├── css/
│   └── style.css
├── js/
│   ├── api.js                         (chamadas fetch centralizadas)
│   ├── auth.js
│   └── funcionarios.js
└── pages/
    ├── login.html
    ├── funcionarios.html
    ├── orgaos.html
    └── alocacoes.html