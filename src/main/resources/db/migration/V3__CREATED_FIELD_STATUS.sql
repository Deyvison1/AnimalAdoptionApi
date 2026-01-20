-- Adiciona coluna "status" à tabela animal (caso ainda não exista)
ALTER TABLE animal_adoption.animal
    ADD COLUMN IF NOT EXISTS status BIGINT NOT NULL DEFAULT 2;

-- Comentário explicando a finalidade da coluna
COMMENT ON COLUMN animal_adoption.animal.status
    IS 'Status do animal baseado no enum StatusAnimal (1=Publicado, 2=Não publicado, 3=Republicado, 4=Despublicado, por algum motivo). Valor padrão: Não publicado.';
