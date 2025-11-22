-- ============================================================
--  script-bd.sql  -  Script de criação do banco SkillUp
--  Banco: PostgreSQL (Azure Database for PostgreSQL Flexible Server)
--  Objetivo: criar as tabelas principais usadas na aplicação e alguns dados iniciais para demonstração de CRUD.
-- ============================================================

-- ===========================
-- Tabela: usuarios
-- ===========================
CREATE TABLE IF NOT EXISTS usuarios (
    id               BIGSERIAL PRIMARY KEY,
    nome             VARCHAR(255) NOT NULL,
    email            VARCHAR(255) NOT NULL UNIQUE,
    senha            VARCHAR(255) NOT NULL,
    profissao_atual  VARCHAR(255),
    meta_profissional VARCHAR(255) NOT NULL,
    perfil           VARCHAR(20) NOT NULL DEFAULT 'USER'
);

-- ===========================
-- Tabela: cursos
-- ===========================
CREATE TABLE IF NOT EXISTS cursos (
    id            BIGSERIAL PRIMARY KEY,
    nome          VARCHAR(255) NOT NULL,
    area          VARCHAR(255) NOT NULL,
    nivel         VARCHAR(50)  NOT NULL,
    carga_horaria INTEGER      NOT NULL
);

-- ===========================
-- Tabela: habilidades
-- ===========================
CREATE TABLE IF NOT EXISTS habilidades (
    id         BIGSERIAL PRIMARY KEY,
    nome       VARCHAR(255) NOT NULL,
    descricao  VARCHAR(500)
);

-- ===========================
-- Tabela: recomendacoes
-- (ligando usuário e curso com um score)
-- ===========================
CREATE TABLE IF NOT EXISTS recomendacoes (
    id                BIGSERIAL PRIMARY KEY,
    usuario_id        BIGINT      NOT NULL REFERENCES usuarios(id),
    curso_id          BIGINT      NOT NULL REFERENCES cursos(id),
    score             NUMERIC(4,2) NOT NULL,
    data_recomendacao TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- ===========================
-- Tabela de associação: usuario_habilidade
-- (nível de domínio de cada habilidade por usuário)
-- ===========================
CREATE TABLE IF NOT EXISTS usuario_habilidade (
    usuario_id    BIGINT NOT NULL REFERENCES usuarios(id),
    habilidade_id BIGINT NOT NULL REFERENCES habilidades(id),
    nivel_dominio INTEGER,
    PRIMARY KEY (usuario_id, habilidade_id)
);

-- ===========================
-- Tabelas para controle de acesso (Spring Security)
-- ===========================
CREATE TABLE IF NOT EXISTS roles (
    id   BIGSERIAL PRIMARY KEY,
    nome VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS usuarios_roles (
    usuario_id BIGINT NOT NULL REFERENCES usuarios(id),
    role_id    BIGINT NOT NULL REFERENCES roles(id),
    PRIMARY KEY (usuario_id, role_id)
);

-- ===========================
-- Dados iniciais (seed)
-- ===========================

-- Roles padrão
INSERT INTO roles (nome) VALUES ('ROLE_USER')
    ON CONFLICT (nome) DO NOTHING;

INSERT INTO roles (nome) VALUES ('ROLE_ADMIN')
    ON CONFLICT (nome) DO NOTHING;

-- Usuário administrador (senha deve ser substituída por hash BCrypt
-- quando for usada em produção real)
INSERT INTO usuarios (nome, email, senha, profissao_atual, meta_profissional, perfil)
VALUES ('Admin SkillUp', 'admin@skillup.com', 'admin123',
        'Admin', 'Administrar plataforma SkillUp', 'ADMIN')
ON CONFLICT (email) DO NOTHING;

-- Ligando o admin à role ADMIN
INSERT INTO usuarios_roles (usuario_id, role_id)
SELECT u.id, r.id
FROM usuarios u, roles r
WHERE u.email = 'admin@skillup.com'
  AND r.nome = 'ROLE_ADMIN'
ON CONFLICT (usuario_id, role_id) DO NOTHING;

-- Alguns cursos de exemplo
INSERT INTO cursos (nome, area, nivel, carga_horaria) VALUES
  ('Introdução ao Front-end', 'Front-end', 'Básico', 20),
  ('APIs REST com Spring Boot', 'Back-end', 'Intermediário', 30),
  ('Fundamentos de Cloud & DevOps', 'Cloud', 'Básico', 15)
ON CONFLICT DO NOTHING;

-- Algumas habilidades de exemplo
INSERT INTO habilidades (nome, descricao) VALUES
  ('HTML/CSS', 'Marcação e estilos para web'),
  ('Java', 'Desenvolvimento back-end com Java'),
  ('DevOps', 'Integração contínua e entrega contínua')
ON CONFLICT DO NOTHING;

