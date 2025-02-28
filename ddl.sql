USE testdb;


DROP TABLE IF EXISTS chatmessage;
DROP TABLE IF EXISTS member;
DROP TABLE IF EXISTS room;

CREATE TABLE member (
                        id BIGINT PRIMARY KEY AUTO_INCREMENT,
                        name VARCHAR(20),
                        password VARCHAR(20)
);

CREATE TABLE room
(
    id       BIGINT PRIMARY KEY AUTO_INCREMENT,
    name     VARCHAR(20)

);

CREATE TABLE chatmessage (
                             id BIGINT PRIMARY KEY AUTO_INCREMENT,
                             memberId BIGINT,
                             roomId BIGINT,
                             content VARCHAR(20),
                             created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                             FOREIGN KEY (memberId) REFERENCES member(id) ON DELETE CASCADE,
                             FOREIGN KEY (roomId) REFERENCES room(id) ON DELETE CASCADE
);

INSERT INTO member VALUES (null, "이소연", "1");
INSERT INTO member VALUES (null, "이원빈", "2");
INSERT INTO member VALUES (null, "유승한", "3");
INSERT INTO member VALUES (null, "김성준", "4");

INSERT INTO room VALUES (null, "classA");
INSERT INTO room VALUES (null, "classB");
