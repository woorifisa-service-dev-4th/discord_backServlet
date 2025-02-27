USE testdb;

DROP TABLE IF EXISTS member;
DROP TABLE IF EXISTS room;
DROP TABLE IF EXISTS chatmessage;

CREATE TABLE member (
    id bigint primary key ,
    name VARCHAR(20),
    password VARCHAR(20)
);

CREATE TABLE room (
    id bigint primary key ,
    name VARCHAR(20)
);
CREATE TABLE chatmessage (
    id bigint primary key ,
    memberId bigint,
    roomId bigint,
    content varchar(20),
    FOREIGN KEY (memberId) REFERENCES member(id) ON DELETE CASCADE,
    FOREIGN KEY (roomId) REFERENCES room(id) ON DELETE CASCADE

);

INSERT INTO member VALUES (1, "이소연", "1");
INSERT INTO member VALUES (2, "이원빈", "2");
INSERT INTO member VALUES (3, "유승한", "3");
INSERT INTO member VALUES (4, "김성준", "4");

INSERT INTO room VALUES (1, "classA");
INSERT INTO room VALUES (2, "classB");



