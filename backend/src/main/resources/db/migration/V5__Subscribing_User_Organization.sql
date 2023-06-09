CREATE TABLE subscribing_user_organization (
                                   user_id BIGINT,
                                   organization_id BIGINT,
                                   FOREIGN KEY (user_id) REFERENCES usr (id),
                                   FOREIGN KEY (organization_id) REFERENCES organization (id),
                                   PRIMARY KEY (user_id, organization_id)
);