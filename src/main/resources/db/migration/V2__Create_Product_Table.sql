CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE IF NOT EXISTS products (
    id UUID DEFAULT uuid_generate_v4() PRIMARY KEY,
    order_id UUID NOT NULL,
    name VARCHAR(50) NOT NULL,
    quantity NUMERIC(19, 2) NOT NULL,
    unit_price NUMERIC(19, 2) NOT NULL,
    CONSTRAINT fk_order
                FOREIGN KEY (order_id)
                REFERENCES orders(id)
);