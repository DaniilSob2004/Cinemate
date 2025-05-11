CREATE TABLE IF NOT EXISTS public.AppUser (
    id SERIAL PRIMARY KEY,
    username VARCHAR(255) NOT NULL,
    firstname VARCHAR(255),
    surname VARCHAR(255),
    email VARCHAR(255) NOT NULL UNIQUE,
    phone_num VARCHAR(50),
    enc_password VARCHAR(255),
    avatar TEXT,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS public.Role (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS public.UserRole (
    id SERIAL PRIMARY KEY,
    user_id INT NOT NULL REFERENCES AppUser(id) ON DELETE CASCADE,
    role_id INT NOT NULL REFERENCES Role(id) ON DELETE CASCADE,
    UNIQUE(user_id, role_id)
);

CREATE TABLE IF NOT EXISTS public.AuthProvider (
    id SERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS public.ExternalAuth (
    id SERIAL PRIMARY KEY,
    user_id INT NOT NULL REFERENCES AppUser(id) ON DELETE CASCADE,
    provider_id INT NOT NULL REFERENCES AuthProvider(id) ON DELETE CASCADE,
    access_token VARCHAR(255),
    external_id VARCHAR(255) NOT NULL,  -- уникальный ID пользователя во внешнем сервисе
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(provider_id, external_id)
);

-- (индекс для provider_id и external_id в ExternalAuth)
CREATE INDEX idx_external_auth_provider_external ON public.ExternalAuth (provider_id, external_id);
