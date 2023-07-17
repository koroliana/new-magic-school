--liquibase formatted sql

--changeset koroleva:1
CREATE INDEX students_name_index ON students (name);

--changeset koroleva:2
CREATE INDEX faculties_name_color_index ON faculties (name, color);