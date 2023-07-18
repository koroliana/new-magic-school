--liquibase formatted sql

--changeset koroleva:create_student_name_index
CREATE INDEX students_name_index ON students (name);

--changeset koroleva:create_faculties_name_color_index
CREATE INDEX faculties_name_color_index ON faculties (name, color);