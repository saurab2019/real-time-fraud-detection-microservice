CREATE TABLE transactions (
                              transaction_id VARCHAR(50) PRIMARY KEY,
                              user_id BIGINT NOT NULL,
                              amount DECIMAL(15,2) NOT NULL,
                              country VARCHAR(100) NOT NULL,
                              merchant_id VARCHAR(50) NOT NULL,
                              created_at TIMESTAMP NOT NULL
);