-- Insert authorities if they don't exist
INSERT INTO authorities (name) 
SELECT 'read' FROM dual 
WHERE NOT EXISTS (SELECT 1 FROM authorities WHERE name = 'read');

INSERT INTO authorities (name) 
SELECT 'write' FROM dual 
WHERE NOT EXISTS (SELECT 1 FROM authorities WHERE name = 'write');

-- Insert roles if they don't exist
INSERT INTO roles (name) 
SELECT 'USER' FROM dual
WHERE NOT EXISTS (SELECT 1 FROM roles WHERE name = 'USER');

INSERT INTO roles (name) 
SELECT 'ADMIN' FROM dual
WHERE NOT EXISTS (SELECT 1 FROM roles WHERE name = 'ADMIN');

-- Insert role-authority mappings if they don't exist
-- ROLE_USER -> read
INSERT INTO roles_authorities (role_id, authority_id)
SELECT r.id, a.id FROM roles r JOIN authorities a 
WHERE r.name = 'USER' AND a.name = 'read'
AND NOT EXISTS (
    SELECT 1 FROM roles_authorities ra 
    WHERE ra.role_id = r.id AND ra.authority_id = a.id
);

-- ROLE_ADMIN -> read
INSERT INTO roles_authorities (role_id, authority_id)
SELECT r.id, a.id FROM roles r JOIN authorities a 
WHERE r.name = 'ADMIN' AND a.name = 'read'
AND NOT EXISTS (
    SELECT 1 FROM roles_authorities ra 
    WHERE ra.role_id = r.id AND ra.authority_id = a.id
);

-- ROLE_ADMIN -> write
INSERT INTO roles_authorities (role_id, authority_id)
SELECT r.id, a.id FROM roles r JOIN authorities a 
WHERE r.name = 'ADMIN' AND a.name = 'write'
AND NOT EXISTS (
    SELECT 1 FROM roles_authorities ra 
    WHERE ra.role_id = r.id AND ra.authority_id = a.id
);
