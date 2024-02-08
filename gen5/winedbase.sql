USE winedb;
CREATE TABLE users (
username VARCHAR(8) NOT NULL UNIQUE,
user_password VARCHAR(8) NOT NULL,
PRIMARY KEY (username)
);
CREATE TABLE QuizAnswers (
username VARCHAR(8) NOT NULL,
w_name1 VARCHAR(45) NOT NULL,
w_name2 VARCHAR(45) NOT NULL,
w_name3 VARCHAR(45) NOT NULL,
creation_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
FOREIGN KEY (username) REFERENCES users(username)
);
CREATE TABLE WhiteWines (
	w_name VARCHAR(45) NOT NULL UNIQUE,
	w_color VARCHAR(45) NOT NULL,
	w_flavor VARCHAR(45) NOT NULL,
    PRIMARY KEY (w_name)
    );

CREATE TABLE RedWines (
	w_name VARCHAR(45) NOT NULL UNIQUE,
	w_color VARCHAR(45) NOT NULL,
	w_flavor VARCHAR(45) NOT NULL,
    PRIMARY KEY (w_name)
);

CREATE TABLE RoseWines (
	w_name VARCHAR(45) NOT NULL UNIQUE,
	w_color VARCHAR(45) NOT NULL,
	w_flavor VARCHAR(45) NOT NULL,
    PRIMARY KEY (w_name)
);
INSERT INTO WhiteWines(w_name, w_color, w_flavor) VALUES ("winename", "white", "dry");
SELECT * FROM WhiteWines;
SELECT * FROM RedWines;
SELECT * FROM RoseWines;
SELECT * FROM quizanswers;
SELECT * FROM users;

DELETE FROM WhiteWines;
DELETE FROM RedWines;
DELETE FROM RoseWines;

DROP TABLE RedWines;
DROP TABLE WhiteWines;
DROP TABLE RoseWines;
DROP TABLE users;
DROP TABLE quizanswers;

ALTER TABLE redwines DROP COLUMN w_key;
ALTER TABLE whitewines DROP COLUMN w_key;
ALTER TABLE rosewines DROP COLUMN w_key;

SELECT username FROM users WHERE username = "gigi";

DELETE FROM WhiteWines WHERE w_name = Albariño;