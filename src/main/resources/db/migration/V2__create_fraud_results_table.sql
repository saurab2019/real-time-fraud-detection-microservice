CREATE TABLE fraud_results (
                               transaction_id VARCHAR(50) PRIMARY KEY,
                               fraud_score INT NOT NULL,
                               decision VARCHAR(30) NOT NULL,
                               processed_at TIMESTAMP NOT NULL,

                               CONSTRAINT fk_fraud_results_transaction
                                   FOREIGN KEY (transaction_id)
                                       REFERENCES transactions(transaction_id)
);