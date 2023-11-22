 -- Составить первый JOIN-запрос, чтобы получить информацию обо всех студентах (имя, возраст, факультет)
 -- Составить второй JOIN-запрос, чтобы получить только тех студентов, у которых есть аватарки.

SELECT s.name, s.age , f.name FROM students s
    LEFT JOIN faculties f ON s.faculty_id = f.id;

SELECT s.name, s.age, a.id FROM students s
    LEFT JOIN avatars a ON s.avatar_id = a.id;