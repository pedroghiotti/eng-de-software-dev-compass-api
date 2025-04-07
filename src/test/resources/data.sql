-- Insert Brazilian States as Regions
INSERT INTO REGION (ID, NAME) VALUES
(UUID(), 'Acre'), (UUID(), 'Alagoas'), (UUID(), 'Amapá'), (UUID(), 'Amazonas'), (UUID(), 'Bahia'),
(UUID(), 'Ceará'), (UUID(), 'Distrito Federal'), (UUID(), 'Espírito Santo'), (UUID(), 'Goiás'),
(UUID(), 'Maranhão'), (UUID(), 'Mato Grosso'), (UUID(), 'Mato Grosso do Sul'), (UUID(), 'Minas Gerais'),
(UUID(), 'Pará'), (UUID(), 'Paraíba'), (UUID(), 'Paraná'), (UUID(), 'Pernambuco'), (UUID(), 'Piauí'),
(UUID(), 'Rio de Janeiro'), (UUID(), 'Rio Grande do Norte'), (UUID(), 'Rio Grande do Sul'),
(UUID(), 'Rondônia'), (UUID(), 'Roraima'), (UUID(), 'Santa Catarina'), (UUID(), 'São Paulo'),
(UUID(), 'Sergipe'), (UUID(), 'Tocantins');

-- Insert Technologies
INSERT INTO TECHNOLOGY (ID, NAME) VALUES
(UUID(), 'Python'), (UUID(), 'JavaScript'), (UUID(), 'Java'), (UUID(), 'C#'), (UUID(), 'Ruby'),
(UUID(), 'PHP'), (UUID(), 'Go'), (UUID(), 'Swift'), (UUID(), 'Kotlin'), (UUID(), 'Rust');

-- Insert 10 Job Listings per Region
INSERT INTO JOB_LISTING (ID, REGION_ID)
SELECT UUID(), r.ID FROM REGION r, (SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL
SELECT 4 UNION ALL SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8 UNION ALL
SELECT 9 UNION ALL SELECT 10) t;

-- Assign 2 to 5 Random Technologies to Each Job Listing
INSERT INTO JOB_LISTING_TECHNOLOGY (JOB_LISTING_ID, TECHNOLOGY_ID)
SELECT jl.ID, t.ID FROM JOB_LISTING jl
JOIN TECHNOLOGY t ON RAND() < 0.5
WHERE (SELECT COUNT(*) FROM JOB_LISTING_TECHNOLOGY WHERE JOB_LISTING_ID = jl.ID) BETWEEN 2 AND 5;