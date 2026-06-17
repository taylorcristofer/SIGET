INSERT INTO usuarios (nome, email, senha_hash, perfil, ativo)
VALUES (
     'Administrador',
    'admin@siget.com',
    '$2a$10$7QxYv.PBgz1kLmNJHF5xOeKJr3e5sQwZ8dBnFpR4mLvC2uXaY6Ksy',
    'ADMIN',
    true
)
ON CONFLICT (email) DO NOTHING;