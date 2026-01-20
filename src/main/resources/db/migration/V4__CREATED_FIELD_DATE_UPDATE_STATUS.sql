-- Adiciona coluna "data_update_status" à tabela animal (caso ainda não exista)
ALTER TABLE animal_adoption.animal
    ADD COLUMN IF NOT EXISTS date_update_status TIMESTAMP;

-- Comentário explicando a finalidade da coluna
COMMENT ON COLUMN animal_adoption.animal.date_update_status
    IS 'Data e hora da última alteração do status do animal (LocalDateTime).';
