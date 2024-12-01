DROP TABLE IF EXISTS Member;

create table Member
(
    id integer not null,
    name varchar(255) not null,
    primary key (id)
);

CREATE TABLE IF NOT EXISTS url_mapping (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    original_url VARCHAR(2048) NOT NULL,
    short_url VARCHAR(8) NOT NULL,
    request_count BIGINT NOT NULL DEFAULT 0,
    CONSTRAINT uk_original_url UNIQUE (original_url),
    CONSTRAINT uk_short_url UNIQUE (short_url)
);
