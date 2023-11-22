-- Возраст студента не может быть меньше 16 лет.
-- Имена студентов должны быть уникальными и не равны нулю.
-- Пара “значение названия” - “цвет факультета” должна быть уникальной.
-- При создании студента без возраста ему автоматически должно присваиваться 20 лет.


ALTER TABLE students 
  ADD CONSTRAINT age_constraint CHECK (age >= 16);
  
ALTER TABLE students   
  ADD CONSTRAINT unique_name UNIQUE (name);
 
ALTER TABLE students  
  ALTER COLUMN name SET NOT NULL;
  
ALTER TABLE students    
  ALTER COLUMN age SET DEFAULT 20;
  
ALTER TABLE faculties 
  ADD CONSTRAINT name_color_unique UNIQUE (name, color);