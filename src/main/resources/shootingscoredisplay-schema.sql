
CREATE TABLE clubs (
    id INT NOT NULL GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    club VARCHAR(255) NOT NULL
);

CREATE TABLE shooters (
    id INT NOT NULL GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    club INT NOT NULL,
    
    FOREIGN KEY (club) REFERENCES clubs(id)
);

CREATE TABLE competitions (
    id INT NOT NULL PRIMARY KEY,
    shooter INT NOT NULL,
    
    FOREIGN KEY (shooter) REFERENCES shooters(id)
);


CREATE TABLE shots (
    id INT NOT NULL GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    time TIMESTAMP NOT NULL,
    xcoord FLOAT NOT NULL, 
    ycoord FLOAT NOT NULL,
    value INT NOT NULL,
    decimalvalue FLOAT NOT NULL,
    type INT NOT NULL,
    competitionid INT,
    fireingpoint INT,
    
    --FOREIGN KEY (competitionid) REFERENCES competition(id),
    UNIQUE(competitionid, time)
);


CREATE TABLE matches (
    id INT NOT NULL GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    state INT DEFAULT 0,
    competition1a INT NOT NULL,
    competition1b INT NOT NULL,
    competition2a INT NOT NULL,
    competition2b INT NOT NULL,
    competition3a INT NOT NULL,
    competition3b INT NOT NULL,
    
    FOREIGN KEY (competition1a) REFERENCES competitions(id),
    FOREIGN KEY (competition1b) REFERENCES competitions(id),
    FOREIGN KEY (competition2a) REFERENCES competitions(id),
    FOREIGN KEY (competition2b) REFERENCES competitions(id),
    FOREIGN KEY (competition3a) REFERENCES competitions(id),
    FOREIGN KEY (competition3b) REFERENCES competitions(id)
);

