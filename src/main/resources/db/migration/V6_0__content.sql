-- (тип контента - фильм, сериал)
CREATE TABLE IF NOT EXISTS public.ContentType (
    id SERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE,
    description TEXT,
    tags VARCHAR(255)
);

-- (ограничения)
CREATE TABLE IF NOT EXISTS public.Warning (
     id SERIAL PRIMARY KEY,
     name VARCHAR(100) NOT NULL UNIQUE
);

-- (сам контент - фильм, сериал)
CREATE TABLE IF NOT EXISTS public.Content (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    content_type_id INT NOT NULL REFERENCES ContentType(id) ON DELETE CASCADE,
    poster_url VARCHAR(500),
    trailer_url VARCHAR(500),
    video_url VARCHAR(500),
    description TEXT,
    duration_min INT,
    age_rating VARCHAR(10),
    release_date DATE,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT NOW(),
    updated_at TIMESTAMP DEFAULT NOW()
);

-- (серии сериалов)
CREATE TABLE IF NOT EXISTS public.Episode (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    content_id INT NOT NULL REFERENCES content(id) ON DELETE CASCADE,
    season_number INT NOT NULL,
    episode_number INT NOT NULL,
    duration_min INT,
    description TEXT,
    trailer_url VARCHAR(500),
    video_url VARCHAR(500),
    release_date DATE,
    created_at TIMESTAMP DEFAULT NOW()
);

-- (жанры)
CREATE TABLE IF NOT EXISTS public.Genre (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    description TEXT,
    tags VARCHAR(255)
);

-- (актеры)
CREATE TABLE IF NOT EXISTS public.Actor (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    surname VARCHAR(255) NOT NULL,
    biography TEXT
);

-- (отношения M:N, контент и ограничения)
CREATE TABLE IF NOT EXISTS public.ContentWarning (
    id SERIAL PRIMARY KEY,
    content_id INT NOT NULL REFERENCES Content(id) ON DELETE CASCADE,
    warning_id INT NOT NULL REFERENCES Warning(id) ON DELETE CASCADE,
    UNIQUE(content_id, warning_id)
);

-- (отношения М:N, контент и жанры)
CREATE TABLE IF NOT EXISTS public.ContentGenre (
    id SERIAL PRIMARY KEY,
    content_id INT NOT NULL REFERENCES Content(id) ON DELETE CASCADE,
    genre_id INT NOT NULL REFERENCES Genre(id) ON DELETE CASCADE,
    UNIQUE(content_id, genre_id)
);

-- (отношения М:N, контент и актеры)
CREATE TABLE IF NOT EXISTS public.ContentActor (
    id SERIAL PRIMARY KEY,
    content_id INT NOT NULL REFERENCES Content(id) ON DELETE CASCADE,
    actor_id INT NOT NULL REFERENCES Actor(id) ON DELETE CASCADE,
    UNIQUE(content_id, actor_id)
);

-- (список желаний, избранное)
CREATE TABLE IF NOT EXISTS public.WishList (
    id SERIAL PRIMARY KEY,
    user_id INT NOT NULL REFERENCES AppUser(id) ON DELETE CASCADE,
    content_id INT NOT NULL REFERENCES Content(id) ON DELETE CASCADE,
    created_at TIMESTAMP DEFAULT NOW(),
    UNIQUE (user_id, content_id)
);

-- (индекс для content_type_id в Content)
CREATE INDEX idx_content_content_type_id ON public.Content(content_type_id);

-- (индекс для content_id в Episode)
CREATE INDEX idx_episode_content_id ON public.Episode(content_id);
