-- Create the donation_requests table
CREATE TABLE donation_requests (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description VARCHAR(1000) NOT NULL,
    organization VARCHAR(255) NOT NULL,
    donation_type_food BOOLEAN DEFAULT FALSE,
    donation_type_money BOOLEAN DEFAULT FALSE,
    period_start DATE,
    period_end DATE,
    user_id BIGINT NOT NULL,
    created_at DATE NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

-- Add index for faster lookups
CREATE INDEX idx_donation_requests_user_id ON donation_requests(user_id);
