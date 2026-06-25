INSERT INTO companies (name) VALUES ('myCompany');

INSERT INTO users (id, hash, salt, company_id) VALUES 
    ('test@test.com', '$2a$10$YDTqD/nSmbjWXJevLdX14.wUYLGVGo0nkdDkz3onHB.rfSQh1/gPe', 'hahahaha',
    (SELECT id FROM companies WHERE name = 'myCompany'));

INSERT INTO products(name, company_id) VALUES 
    ('vinegar',
    (SELECT id FROM companies WHERE name = 'myCompany'));