-- V4__Update_bets_table.sql
-- Atualizar tabela de apostas com novos campos

-- Alterar coluna amount para DECIMAL com precisão correta
ALTER TABLE bets
MODIFY COLUMN amount DECIMAL(10,2) NOT NULL;

-- Adicionar colunas se não existirem
ALTER TABLE bets
ADD COLUMN IF NOT EXISTS description VARCHAR(500),
ADD COLUMN IF NOT EXISTS status VARCHAR(20) NOT NULL DEFAULT 'PENDING';

-- Atualizar registros existentes se necessário
UPDATE bets SET status = 'PENDING' WHERE status IS NULL OR status = '';

-- Adicionar índices para melhor performance
CREATE INDEX idx_bets_user_id ON bets(user_id);
CREATE INDEX idx_bets_status ON bets(status);
CREATE INDEX idx_bets_type ON bets(type);
CREATE INDEX idx_bets_timestamp ON bets(timestamp);