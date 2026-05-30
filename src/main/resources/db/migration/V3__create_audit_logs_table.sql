CREATE TABLE audit_logs (
                            id BIGSERIAL PRIMARY KEY,
                            transaction_id VARCHAR(50) NOT NULL,
                            rule_name VARCHAR(100) NOT NULL,
                            rule_score INT NOT NULL,
                            reason VARCHAR(255) NOT NULL,
                            created_at TIMESTAMP NOT NULL,

                            CONSTRAINT fk_audit_transaction
                                FOREIGN KEY (transaction_id)
                                    REFERENCES transactions(transaction_id)
);