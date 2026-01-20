ALTER TABLE animal_adoption.animal
    ADD COLUMN IF NOT EXISTS motivo VARCHAR(255);

COMMENT ON COLUMN animal_adoption.animal.motivo
    IS 'Motivo da despublicacao do animal, cada vez que ele for despublicado esse valor sempre vai ter referencia a ultima despublicacao';
