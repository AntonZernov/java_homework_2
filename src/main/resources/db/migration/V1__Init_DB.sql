CREATE TABLE articles (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    text TEXT NOT NULL,
    author VARCHAR(150) NOT NULL,
    published_at TIMESTAMP WITH TIME ZONE NOT NULL
);

CREATE TABLE videos (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    url VARCHAR(1024) NOT NULL,
    duration TIME NOT NULL
);

CREATE TABLE podcasts (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL
);

CREATE TABLE podcast_episodes (
    id BIGSERIAL PRIMARY KEY,
    podcast_id BIGINT NOT NULL,
    episode_number INT NOT NULL,
    title VARCHAR(255) NOT NULL,
    audio_url VARCHAR(1024) NOT NULL,
    duration_seconds INT NOT NULL,

    CONSTRAINT fk_podcast FOREIGN KEY (podcast_id) REFERENCES podcasts (id) ON DELETE CASCADE,
    CONSTRAINT uk_podcast_episode UNIQUE (podcast_id, episode_number)
);
