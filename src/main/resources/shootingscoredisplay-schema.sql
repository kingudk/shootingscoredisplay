
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
    seqNumber INT NOT NULL,
    xcoord FLOAT NOT NULL, 
    ycoord FLOAT NOT NULL,
    value INT NOT NULL,
    decimalvalue INT NOT NULL,
    type INT NOT NULL,
    competitionid INT,
    fireingpoint INT,
    caliber INT,
    
    --FOREIGN KEY (competitionid) REFERENCES competition(id),
    UNIQUE(competitionid, time)
    --UNIQUE(fireingpoint, seqNumber, competitionid)
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

-- Test data, yes I know this is cheating..
INSERT INTO clubs (club) VALUES ('DSB/ASF');
INSERT INTO shooters (name, club) VALUES ('Kim Christensen', 1);
INSERT INTO competitions (id, shooter) VALUES (1,1);
INSERT INTO shots (time, seqNumber, xcoord, ycoord, value, decimalvalue, type, competitionid, fireingpoint, caliber) VALUES (TIMESTAMP('1970-01-01', '23.59.39'), 1, 0.000, 0.000, 10, 109, 2, 1, 44, 450);


