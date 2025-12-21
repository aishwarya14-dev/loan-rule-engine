-- V3__fix_id_column.sql
-- Fix id column to be auto-increment identity

ALTER TABLE loan_application
    ALTER COLUMN id DROP DEFAULT,
    ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY;