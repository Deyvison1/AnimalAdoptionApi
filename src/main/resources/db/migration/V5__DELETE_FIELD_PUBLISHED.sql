-- Remove a coluna "published" da tabela animal (caso exista)
ALTER TABLE animal_adoption.animal
    DROP COLUMN IF EXISTS published;