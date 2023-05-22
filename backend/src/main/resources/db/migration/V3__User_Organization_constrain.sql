ALTER TABLE usr
    ADD COLUMN organization_id int8;

-- Создание внешнего ключа для связи с таблицей Organization
ALTER TABLE usr
    ADD CONSTRAINT fk_user_organization
        FOREIGN KEY (organization_id)
            REFERENCES organization (id);
