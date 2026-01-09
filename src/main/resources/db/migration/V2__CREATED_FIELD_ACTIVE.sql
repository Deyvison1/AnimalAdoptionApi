-- Adiciona coluna "active" à tabela animal (caso ainda não exista)
ALTER TABLE animal_adoption.animal 
    ADD COLUMN IF NOT EXISTS published  BOOLEAN NOT NULL DEFAULT FALSE;

-- Comentário explicando a finalidade da coluna
COMMENT ON COLUMN animal_adoption.animal.published  
    IS 'Indica se o animal pode ser publicado no sistema. Valor padrão: FALSE.';
