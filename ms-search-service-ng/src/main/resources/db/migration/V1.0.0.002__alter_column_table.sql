ALTER TABLE index_column ADD COLUMN is_filter_column boolean not null default 1 AFTER active;
ALTER TABLE index_column ADD COLUMN is_display_column boolean not null default 1 AFTER active;