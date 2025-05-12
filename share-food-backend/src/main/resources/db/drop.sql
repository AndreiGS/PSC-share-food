-- Drop junction tables first to avoid foreign key constraint issues
DROP TABLE IF EXISTS users_authorities;
DROP TABLE IF EXISTS users_roles;
DROP TABLE IF EXISTS roles_authorities;

-- Drop main tables after their dependencies have been removed
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS roles;
DROP TABLE IF EXISTS authorities;