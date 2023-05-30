-- Создание внешнего ключа для связи с таблицей Organization
ALTER TABLE usr
    ADD CONSTRAINT fk_user_organization
        FOREIGN KEY (organization_id)
            REFERENCES organization (id);
