CREATE TABLE orders (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL,
    product_id VARCHAR(255) NOT NULL,
    status VARCHAR(50) NOT NULL
);
