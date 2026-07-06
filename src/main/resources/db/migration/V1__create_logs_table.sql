
CREATE TABLE IF NOT EXISTS logs(
    log_id BIGSERIAL  PRIMARY KEY,
    raw_log TEXT NOT NULL ,
    log_type VARCHAR (200) NOT NULL,
    client_ip VARCHAR(100) NOT NULL,
    occurred_at TIMESTAMP NOT NULL,
    collected_at  TIMESTAMP DEFAULT NOW(),
    http_method VARCHAR(50) NOT NULL,
    request_path VARCHAR(2048) NOT NULL,
    status_code INT NOT NULL,
    response_size BIGINT,
    user_agent  VARCHAR(512)
);

CREATE INDEX IF NOT EXISTS idx_logs_client_ip ON logs(client_ip);
CREATE INDEX IF NOT EXISTS idx_logs_client_ip_N_occurred_at ON logs(client_ip,occurred_at);
CREATE INDEX IF NOT EXISTS idx_logs_status_code ON logs(status_code);

COMMENT ON TABLE  logs               IS '수집된 로그';
COMMENT ON COLUMN logs.log_type      IS 'NGINX | TOMCAT | SYSLOG';
COMMENT ON COLUMN logs.occurred_at   IS '로그 원본 발생 시각';
COMMENT ON COLUMN logs.raw_log       IS '파싱 전 원본 로그 라인';