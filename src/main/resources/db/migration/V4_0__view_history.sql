CREATE TABLE IF NOT EXISTS ContentViewHistory (
    id SERIAL PRIMARY KEY,
    user_id INT NOT NULL REFERENCES AppUser(id) ON DELETE CASCADE,
    content_id INT NOT NULL REFERENCES Content(id) ON DELETE CASCADE,
    viewed_at TIMESTAMP DEFAULT NOW()
);

-- (индекс для user_id в ContentViewHistory)
CREATE INDEX idx_content_view_history_user_id ON ContentViewHistory(user_id);
