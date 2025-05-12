-- Create the user_sessions table
CREATE TABLE user_sessions (
    session_id VARCHAR(64) PRIMARY KEY,
    user_id BIGINT NOT NULL,
    expiration_time TIMESTAMP NOT NULL,
    provider INT NOT NULL,
    created_at TIMESTAMP NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

-- Add index for faster lookups
CREATE INDEX idx_user_sessions_user_id ON user_sessions(user_id);