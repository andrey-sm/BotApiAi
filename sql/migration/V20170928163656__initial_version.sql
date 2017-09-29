START TRANSACTION;

CREATE TYPE PUSH_DEVICE_TYPE AS ENUM ('ANDROID', 'IOS');


CREATE TABLE conversation
(
    id                          BIGSERIAL PRIMARY KEY,
    type                        VARCHAR(30) NOT NULL,
    sender_name                 VARCHAR(100) DEFAULT NULL,
    photo_url                   VARCHAR(500) DEFAULT NULL,
    "timestamp"                 TIMESTAMP,
    slack_user_id               VARCHAR(100) DEFAULT NULL,
    slack_channel_id            VARCHAR(100) DEFAULT NULL,
    fb_sender_id                VARCHAR(100) DEFAULT NULL,
    fb_recipient_id             VARCHAR(100) DEFAULT NULL,
    tg_chat_id                  VARCHAR(100) DEFAULT NULL,
    skype_sender_id             VARCHAR(100) DEFAULT NULL,
    skype_conversation_id       VARCHAR(100) DEFAULT NULL,
    skype_access_token          VARCHAR(500) DEFAULT NULL,
    skype_access_token_exp_date TIMESTAMP DEFAULT NULL

);

CREATE TABLE message
(
    id              BIGSERIAL PRIMARY KEY,
    text            VARCHAR(10000)  NOT NULL,
    "timestamp"     TIMESTAMP       NOT NULL,
    conversation_id BIGINT          REFERENCES conversation (id) ON UPDATE CASCADE ON DELETE CASCADE,
    botreply        BOOLEAN         DEFAULT false
);

CREATE TABLE push_device
(
    id              BIGSERIAL PRIMARY KEY,
    token           VARCHAR(1000)     NOT NULL,
    device_type     PUSH_DEVICE_TYPE  NOT NULL
);

COMMIT;
