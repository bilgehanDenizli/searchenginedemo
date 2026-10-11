-- Development sample data for PostgreSQL. Explicit IDs make the related rows easy to follow.
-- ON CONFLICT DO NOTHING makes reruns safe and does not overwrite existing records.
BEGIN;

INSERT INTO owners (id, first_name, last_name, email, phone, is_active) VALUES
    (1001, 'Aylin', 'Kaya', 'aylin.kaya@sample.test', '+90-555-0101', TRUE),
    (1002, 'Emre', 'Demir', 'emre.demir@sample.test', '+90-555-0102', TRUE),
    (1003, 'Deniz', 'Yilmaz', 'deniz.yilmaz@sample.test', '+90-555-0103', TRUE),
    (1004, 'Sofia', 'Alvarez', 'sofia.alvarez@sample.test', '+1-555-0104', TRUE),
    (1005, 'Omar', 'Farouk', 'omar.farouk@sample.test', '+1-555-0105', TRUE),
    (1006, 'Maya', 'Chen', 'maya.chen@sample.test', '+1-555-0106', TRUE),
    (1007, 'Priya', 'Nair', 'priya.nair@sample.test', '+1-555-0107', TRUE),
    (1008, 'Noah', 'Bennett', 'noah.bennett@sample.test', '+1-555-0108', TRUE),
    (1009, 'Elif', 'Arslan', 'elif.arslan@sample.test', '+90-555-0109', TRUE),
    (1010, 'James', 'Okafor', 'james.okafor@sample.test', '+1-555-0110', TRUE)
ON CONFLICT DO NOTHING;

-- Additional deterministic rows for exercising paging, filtering, and relevance.
-- These are generated (not random), so the same IDs and values are produced on every run.
INSERT INTO owners (id, first_name, last_name, email, phone, is_active)
SELECT 1100 + n,
       (ARRAY['Alex', 'Jamie', 'Morgan', 'Taylor', 'Casey', 'Robin', 'Avery', 'Jordan',
              'Sam', 'Riley', 'Cameron', 'Drew', 'Kendall', 'Lee', 'Parker', 'Quinn',
              'Reese', 'Skyler', 'Sage', 'Rowan'])[((n - 1) % 20) + 1]
           || CASE WHEN n > 20 THEN ' ' || (((n - 1) / 20) + 1)::text ELSE '' END,
       (ARRAY['Kaya', 'Demir', 'Yilmaz', 'Alvarez', 'Farouk', 'Chen', 'Nair', 'Bennett',
              'Arslan', 'Okafor', 'Patel', 'Garcia', 'Kim', 'Singh', 'Turner', 'Cohen',
              'Ivanov', 'Costa', 'Wilson', 'Haddad'])[((n - 1) % 20) + 1],
       'owner' || n || '@sample.test',
       '+90-555-' || lpad((1000 + n)::text, 4, '0'),
       TRUE
FROM generate_series(1, 100) AS n
ON CONFLICT DO NOTHING;

INSERT INTO pets (id, name, species, breed, birth_date, owner_id, is_active)
SELECT 2100 + n,
       (ARRAY['Buddy', 'Cleo', 'Daisy', 'Finn', 'Gizmo', 'Hazel', 'Indie', 'Jasper',
              'Koda', 'Maple', 'Nova', 'Olive', 'Pip', 'Remy', 'Sunny', 'Teddy',
              'Uma', 'Willow', 'Yoshi', 'Ziggy'])[((n - 1) % 20) + 1] || ' ' || n,
       (ARRAY['DOG', 'CAT', 'BIRD', 'RABBIT', 'REPTILE', 'OTHER'])[((n - 1) % 6) + 1],
       (ARRAY['Mixed Breed', 'Domestic Shorthair', 'Cockatiel', 'Holland Lop',
              'Leopard Gecko', 'Betta Fish', 'Beagle', 'Siamese', 'Border Collie',
              'Maine Coon'])[((n - 1) % 10) + 1],
       DATE '2015-01-01' + ((n * 13) % 3650),
       1101 + ((n - 1) % 100),
       TRUE
FROM generate_series(1, 200) AS n
ON CONFLICT DO NOTHING;

INSERT INTO visits (id, pet_id, scheduled_at, reason, notes, status, is_active)
SELECT 3100 + n,
       2101 + ((n - 1) % 200),
       TIMESTAMP '2025-01-01 08:00:00'
           + ((n * 17) % 730) * INTERVAL '1 day'
           + ((n * 37) % 600) * INTERVAL '1 minute',
       (ARRAY['Annual wellness exam', 'Vaccination', 'Dental examination',
              'Skin allergy follow-up', 'Digestive concerns', 'Limping after exercise',
              'Weight management', 'Ear infection follow-up', 'Routine blood work',
              'Nail and grooming check', 'Cough and respiratory check',
              'Nutrition consultation'])[((n - 1) % 12) + 1],
       (ARRAY['Routine checkup; appetite and energy are normal.',
              'Booster administered; no adverse reaction reported.',
              'Discussed home care and recommended a follow-up if symptoms return.',
              'Symptoms have improved; continue monitoring over the next week.',
              'Reviewed diet, hydration, and a gradual activity plan.',
              'Owner noticed the issue after outdoor exercise; examination completed.',
              'Screening results are within the expected range for this patient.',
              'Treatment plan reviewed with the owner; schedule a recheck as needed.'])
           [((n - 1) % 8) + 1],
       (ARRAY['SCHEDULED', 'COMPLETED', 'CANCELLED'])[((n - 1) % 3) + 1],
       n % 25 <> 0
FROM generate_series(1, 500) AS n
ON CONFLICT DO NOTHING;

INSERT INTO pets (id, name, species, breed, birth_date, owner_id, is_active) VALUES
    (2001, 'Milo', 'DOG', 'Golden Retriever', '2021-03-14', 1001, TRUE),
    (2002, 'Pepper', 'CAT', 'Domestic Shorthair', '2020-08-02', 1001, TRUE),
    (2003, 'Luna', 'DOG', 'Border Collie', '2022-01-19', 1002, TRUE),
    (2004, 'Mochi', 'CAT', 'Siamese', '2023-05-11', 1002, TRUE),
    (2005, 'Atlas', 'DOG', 'German Shepherd', '2019-11-23', 1003, TRUE),
    (2006, 'Nori', 'RABBIT', 'Holland Lop', '2023-02-05', 1003, TRUE),
    (2007, 'Coco', 'DOG', 'Cavalier King Charles Spaniel', '2020-06-17', 1004, TRUE),
    (2008, 'Frida', 'CAT', 'Maine Coon', '2021-12-08', 1004, TRUE),
    (2009, 'Pamuk', 'CAT', 'Turkish Angora', '2022-04-26', 1005, TRUE),
    (2010, 'Tarcin', 'DOG', 'Kangal', '2018-09-30', 1005, TRUE),
    (2011, 'Kiwi', 'BIRD', 'Cockatiel', '2022-07-12', 1006, TRUE),
    (2012, 'Basil', 'RABBIT', 'Mini Rex', '2024-01-03', 1006, TRUE),
    (2013, 'Scout', 'DOG', 'Beagle', '2021-10-21', 1007, TRUE),
    (2014, 'Tofu', 'CAT', 'Ragdoll', '2020-02-14', 1007, TRUE),
    (2015, 'Zeytin', 'CAT', 'Turkish Van', '2021-05-06', 1008, TRUE),
    (2016, 'Pasha', 'DOG', 'Anatolian Shepherd', '2019-03-29', 1008, TRUE),
    (2017, 'Bubbles', 'OTHER', 'Betta Fish', '2024-06-01', 1009, TRUE),
    (2018, 'Kiki', 'BIRD', 'African Grey Parrot', '2017-12-19', 1009, TRUE),
    (2019, 'Chai', 'CAT', 'Bengal', '2022-09-10', 1010, TRUE),
    (2020, 'Cedar', 'REPTILE', 'Leopard Gecko', '2023-07-22', 1010, TRUE)
ON CONFLICT DO NOTHING;

INSERT INTO visits (id, pet_id, scheduled_at, reason, notes, status, is_active) VALUES
    (3001, 2001, '2026-10-15 09:00:00', 'Annual wellness exam', 'Rabies booster, weight check, and routine examination.', 'SCHEDULED', TRUE),
    (3002, 2001, '2026-04-10 10:30:00', 'Seasonal itching', 'Mild skin irritation after walks; monitor for pollen allergies.', 'COMPLETED', TRUE),
    (3003, 2002, '2026-10-17 11:00:00', 'Dental check', 'Evaluate tartar and discuss a dental cleaning.', 'SCHEDULED', TRUE),
    (3004, 2002, '2026-07-01 14:00:00', 'Reduced appetite', 'Vomiting resolved; appetite returned after dietary change.', 'COMPLETED', TRUE),
    (3005, 2003, '2026-10-18 09:30:00', 'Limping after hiking', 'Check left hind leg; owner noticed limping after a trail walk.', 'SCHEDULED', TRUE),
    (3006, 2004, '2026-05-20 13:15:00', 'Vaccination', 'Routine booster administered; no adverse reaction reported.', 'COMPLETED', TRUE),
    (3007, 2005, '2026-10-21 08:45:00', 'Annual wellness exam', 'General checkup and joint mobility assessment.', 'SCHEDULED', TRUE),
    (3008, 2006, '2026-09-15 15:00:00', 'Rabbit dental and nail check', 'Molars normal; nails trimmed; continue hay-based diet.', 'COMPLETED', TRUE),
    (3009, 2007, '2026-10-23 10:00:00', 'Ear infection follow-up', 'Recheck right ear after completing ear drops.', 'SCHEDULED', TRUE),
    (3010, 2008, '2026-08-11 12:30:00', 'Weight management', 'Discuss portion sizes and gradual weight reduction.', 'COMPLETED', TRUE),
    (3011, 2009, '2026-10-25 09:15:00', 'Vaccination booster', 'Booster and general health check for indoor cat.', 'SCHEDULED', TRUE),
    (3012, 2010, '2026-06-03 16:00:00', 'Joint stiffness', 'Stiff after rest; assess hips and recommend low-impact exercise.', 'COMPLETED', TRUE),
    (3013, 2011, '2026-10-27 11:30:00', 'Beak and feather examination', 'Check beak condition and feather quality; review diet.', 'SCHEDULED', TRUE),
    (3014, 2012, '2026-07-29 10:15:00', 'Dental examination', 'Incisors aligned; continue monitoring chewing and appetite.', 'COMPLETED', TRUE),
    (3015, 2013, '2026-10-29 14:30:00', 'Wellness and flea prevention', 'Routine exam and renewal of flea prevention.', 'SCHEDULED', TRUE),
    (3016, 2014, '2026-09-08 09:45:00', 'Urinary discomfort', 'Urinalysis completed; increase water intake and monitor symptoms.', 'COMPLETED', TRUE),
    (3017, 2015, '2026-10-30 13:00:00', 'Annual examination', 'Routine checkup and vaccine review.', 'SCHEDULED', TRUE),
    (3018, 2016, '2026-04-19 08:30:00', 'Skin allergy follow-up', 'Redness improved; continue prescribed treatment for one week.', 'COMPLETED', TRUE),
    (3019, 2017, '2026-11-02 15:30:00', 'Exotic pet wellness check', 'Assess water temperature, tank conditions, and feeding routine.', 'SCHEDULED', TRUE),
    (3020, 2018, '2026-05-30 11:45:00', 'Respiratory checkup', 'Cancelled by owner; reschedule if wheezing or discharge returns.', 'CANCELLED', TRUE)
ON CONFLICT DO NOTHING;

SELECT setval(pg_get_serial_sequence('owners', 'id')::regclass,
              GREATEST((SELECT MAX(id) FROM owners), 1200), TRUE);
SELECT setval(pg_get_serial_sequence('pets', 'id')::regclass,
              GREATEST((SELECT MAX(id) FROM pets), 2300), TRUE);
SELECT setval(pg_get_serial_sequence('visits', 'id')::regclass,
              GREATEST((SELECT MAX(id) FROM visits), 3600), TRUE);

COMMIT;
