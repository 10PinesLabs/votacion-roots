ALTER TABLE TEMADEMINUTA
ADD COLUMN IF NOT EXISTS FUENOTIFICADO BOOLEAN NOT NULL
DEFAULT TRUE;