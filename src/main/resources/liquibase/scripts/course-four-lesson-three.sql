--liquibase formatted sql

--changeset koroleva:create_student_name_index
--precondition onFail:MARK_RAN
--precondition-sql-check expectedResult:0 select count(*) from pg_catalog.pg_tables t inner join pg_indexes i on i.tablename = t.tablename where t.tablename = 'students' and i.indexname = 'students_name_index';
--rollback DROP INDEX students_name_index
CREATE INDEX students_name_index ON students (name);

--changeset koroleva:create_faculties_name_color_index
--precondition onFail:MARK_RAN
--precondition-sql-check expectedResult:0 select count(*) from pg_catalog.pg_tables t inner join pg_indexes i on i.tablename = t.tablename where t.tablename = 'faculties' and i.indexname = 'faculties_name_color_index';
--rollback DROP INDEX faculties_name_color_index
CREATE INDEX faculties_name_color_index ON faculties (name, color);