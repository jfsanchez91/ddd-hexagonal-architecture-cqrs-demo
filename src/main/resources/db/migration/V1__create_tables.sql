CREATE TABLE users (
    id UUID PRIMARY KEY,
    name TEXT NOT NULL,
    email TEXT NOT NULL UNIQUE
);

CREATE TABLE teams (
    id UUID PRIMARY KEY,
    name TEXT NOT NULL
);

CREATE TABLE team_users (
    team_id UUID NOT NULL REFERENCES teams(id),
    user_id UUID NOT NULL REFERENCES users(id),
    alias TEXT NOT NULL,
    PRIMARY KEY (team_id, user_id)
);
