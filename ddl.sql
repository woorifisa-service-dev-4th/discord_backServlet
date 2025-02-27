USE testdb;

DROP TABLE IF EXISTS chatmessage;
DROP TABLE IF EXISTS member;
DROP TABLE IF EXISTS room;

CREATE TABLE member (
                        id BIGINT PRIMARY KEY AUTO_INCREMENT,
                        name VARCHAR(20),
                        password VARCHAR(20)
);

CREATE TABLE room (

CREATE TABLE chatmessage (
                             id BIGINT PRIMARY KEY AUTO_INCREMENT,
                             memberId BIGINT,
                             roomId BIGINT,
                             content VARCHAR(20),
                             FOREIGN KEY (memberId) REFERENCES member(id) ON DELETE CASCADE,
                             FOREIGN KEY (roomId) REFERENCES room(id) ON DELETE CASCADE
);

INSERT INTO member VALUES (1, "이소연", "1");
INSERT INTO member VALUES (2, "이원빈", "2");
INSERT INTO member VALUES (3, "유승한", "3");
INSERT INTO member VALUES (4, "김성준", "4");

INSERT INTO room VALUES (1, "classA");
INSERT INTO room VALUES (2, "classB");
