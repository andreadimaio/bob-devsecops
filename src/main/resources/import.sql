INSERT INTO CUSTOMERS (ID, FULL_NAME, EMAIL, TAX_ID, IBAN, NOTES) VALUES
(1, 'Mario Rossi', 'm.rossi@example.com', 'RSSMRA80A01H501Z', 'IT60X0542811101000000123456', 'Cliente storico'),
(2, 'Giuseppe Verdi', 'g.verdi@example.com', 'VRDGPP75B02F205K', 'IT60X0542811101000000789012', 'Contratto premium'),
(3, 'Anna Bianchi', 'a.bianchi@example.com', 'BNCNNA85C03L219R', 'IT60X0542811101000000345678', 'In fase di rinnovo'),
(4, 'Luca Neri', 'l.neri@example.com', 'NRELCU90D04M089T', 'IT60X0542811101000000901234', 'Nuovo cliente'),
(5, 'Sofia Gialli', 's.gialli@example.com', 'GLLSFI95E05A662U', 'IT60X0542811101000000567890', 'Consulenza IT');

INSERT INTO PROJECTS (ID, CUSTOMER_ID, PROJECT_NAME, BUDGET, REVENUE, START_DATE, STATUS) VALUES
(1, 1, 'Implementazione ERP', 150000.00, 180000.00, '2025-01-15', 'IN_CORSO'),
(2, 1, 'Formazione personale', 25000.00, 30000.00, '2025-03-01', 'COMPLETATO'),
(3, 2, 'Sviluppo App Mobile', 85000.00, 95000.00, '2025-02-10', 'IN_CORSO'),
(4, 3, 'Migrazione Cloud', 220000.00, 240000.00, '2025-04-01', 'PIANIFICATO'),
(5, 4, 'Consulenza Cybersecurity', 45000.00, 50000.00, '2025-01-20', 'IN_CORSO'),
(6, 5, 'Analisi Dati BI', 75000.00, 0.00, '2025-05-15', 'PREVENTIVO');

INSERT INTO USER_ACCOUNTS (ID, CUSTOMER_ID, USERNAME, PASSWORD, ROLE) VALUES
(1, 1, 'mrossi', 'Password123!', 'USER'),
(2, 2, 'g.verdi', 'Opera2024$', 'USER'),
(3, 3, 'anna', 'SecretAdminAccess', 'ADMIN'),
(4, 4, 'lneri_90', 'LucaNeri!90', 'USER'),
(5, 5, 'sofia_it', 'ComplexPass#2025', 'USER');