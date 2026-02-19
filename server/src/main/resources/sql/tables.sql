-- Справочные таблицы --
CREATE TABLE IF NOT EXISTS races (
     race_id SERIAL PRIMARY KEY,
     race_name VARCHAR(50) UNIQUE NOT NULL,
     race_bonuses JSONB DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS classes (
     class_id SERIAL PRIMARY KEY,
     class_name VARCHAR(50) UNIQUE NOT NULL,
     class_bonuses JSONB DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS skills(
        skill_id SERIAL PRIMARY KEY,
        skill_name VARCHAR(50)NOT NULL,
        class_id INT NOT NULL,
        required_level INT NOT NULL,
        FOREIGN KEY(class_id) REFERENCES classes(class_id)
);


-- Основные таблицы --

CREATE TABLE IF NOT EXISTS players (
    player_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    username VARCHAR(32) UNIQUE NOT NULL,
    race_id INT NOT NULL,
    class_id INT NOT NULL,
    level INT DEFAULT 1,
    guild_id UUID REFERENCES guilds(guild_id) ON DELETE SET NULL DEFAULT NULL,
    experience BIGINT DEFAULT 0,
    gold BIGINT DEFAULT 0,
    vouchers BIGINT DEFAULT 0,
    crystals INT DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    last_login TIMESTAMP,
    FOREIGN KEY (race_id) REFERENCES races(race_id),
    FOREIGN KEY (class_id) REFERENCES classes(class_id)
);

CREATE TABLE IF NOT EXISTS player_stats (
    player_id UUID PRIMARY KEY,
    strength INT DEFAULT 5,
    dexterity INT DEFAULT 5,
    intelligence INT DEFAULT 5,
    recovery INT DEFAULT 5,
    luck INT DEFAULT 5,
    wisdom INT DEFAULT 5,
    available_points INT DEFAULT 15,
    FOREIGN KEY (player_id) REFERENCES players(player_id) ON DELETE CASCADE
);

-- Статы для ЛК --

CREATE TABLE IF NOT EXISTS player_secondary_stats (
    player_id UUID PRIMARY KEY,
    health DOUBLE PRECISION DEFAULT 100,
    max_health DOUBLE PRECISION DEFAULT 100,
    mana DOUBLE PRECISION DEFAULT 100,
    max_mana DOUBLE PRECISION DEFAULT 100,
    physical_attack DOUBLE PRECISION DEFAULT 10,
    magic_attack DOUBLE PRECISION DEFAULT 10,
    defense DOUBLE PRECISION DEFAULT 0,
    resistance DOUBLE PRECISION DEFAULT 0,
    critical_chance DOUBLE PRECISION DEFAULT 0,
    critical_damage DOUBLE PRECISION DEFAULT 1.5,
    critical_resistance DOUBLE PRECISION DEFAULT 0,
    evasion DOUBLE PRECISION DEFAULT 0,
    accuracy DOUBLE PRECISION DEFAULT 0,
    last_updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (player_id) REFERENCES players(player_id) ON DELETE CASCADE
);

-- Система предметов --

CREATE TABLE IF NOT EXISTS items (
    item_id SERIAL PRIMARY KEY,
    item_name VARCHAR(100) NOT NULL,
    item_type VARCHAR(20) NOT NULL,
    rarity VARCHAR(15) DEFAULT 'COMMON',
    equipment_slot VARCHAR(20),
    base_price BIGINT NOT NULL,
    required_level INT DEFAULT 1,
    bonuses JSONB DEFAULT NULL,
    max_enhancement_level INT DEFAULT 0,
    enhancement_chance JSONB,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Инвентарь --

CREATE TABLE IF NOT EXISTS player_inventory (
    inventory_id BIGSERIAL PRIMARY KEY,
    player_id UUID NOT NULL,
    slot INT NOT NULL,
    item_id INT NOT NULL,
    quantity INT DEFAULT 1,
    durability INT DEFAULT 100,
    enhancement_level INT DEFAULT 0,
    is_bound BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(player_id, slot),
    FOREIGN KEY (player_id) REFERENCES players(player_id) ON DELETE CASCADE,
    FOREIGN KEY (item_id) REFERENCES items(item_id)
);

CREATE TABLE IF NOT EXISTS player_equipment (
     equipment_id BIGSERIAL PRIMARY KEY, -- Добавить PK
     player_id UUID NOT NULL,
     slot_type VARCHAR(20) NOT NULL, -- 'HELMET', 'CHESTPLATE' etc
     inventory_id BIGINT NOT NULL,
     equipped_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
     UNIQUE(player_id, slot_type),
     FOREIGN KEY (player_id) REFERENCES players(player_id) ON DELETE CASCADE,
     FOREIGN KEY (inventory_id) REFERENCES player_inventory(inventory_id)
);

-- Аукционы --

CREATE TABLE IF NOT EXISTS auction_listings (
    listing_id BIGSERIAL PRIMARY KEY,
    seller_id UUID NOT NULL,
    item_inventory_id BIGINT NOT NULL,
    start_price BIGINT NOT NULL,
    buyout_price BIGINT,
    duration_hours INT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    expires_at TIMESTAMP NOT NULL,
    status VARCHAR(20) DEFAULT 'ACTIVE',
    deposit_paid BIGINT NOT NULL,
    commission_paid BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (seller_id) REFERENCES players(player_id) ON DELETE CASCADE,
    FOREIGN KEY (item_inventory_id) REFERENCES player_inventory(inventory_id) ON DELETE CASCADE
);


CREATE TABLE IF NOT EXISTS auction_bids (
    bid_id BIGSERIAL PRIMARY KEY,
    listing_id BIGINT NOT NULL,
    bidder_id UUID NOT NULL,
    bid_amount BIGINT NOT NULL,
    bid_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    is_winning BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (listing_id) REFERENCES auction_listings(listing_id) ON DELETE CASCADE,
    FOREIGN KEY (bidder_id) REFERENCES players(player_id),
    UNIQUE(listing_id, bidder_id)
);

-- Экономика --

CREATE TABLE IF NOT EXISTS currency_transactions (
        transaction_id BIGSERIAL PRIMARY KEY,
        player_id UUID NOT NULL,
        transaction_type VARCHAR(20) NOT NULL, -- 'AUCTION_BUY', 'LOAN_PAYMENT', 'SHOP_PURCHASE'
        currency_type VARCHAR(10) NOT NULL, -- 'GOLD', 'VOUCHERS', 'CRYSTALS'
        amount BIGINT NOT NULL,
        balance_after BIGINT NOT NULL,
        description TEXT,
        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
        FOREIGN KEY (player_id) REFERENCES players(player_id)
);

CREATE TABLE IF NOT EXISTS player_loans (
    loan_id BIGSERIAL PRIMARY KEY,
    player_id UUID NOT NULL,
    lender_type VARCHAR(10) NOT NULL,
    lender_id UUID,
    amount BIGINT NOT NULL,
    interest_rate DOUBLE PRECISION NOT NULL,
    borrowed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    due_date TIMESTAMP NOT NULL,
    amount_paid BIGINT DEFAULT 0,
    status VARCHAR(15) DEFAULT 'ACTIVE',
    FOREIGN KEY (player_id) REFERENCES players(player_id)
);

-- Гильдии --

CREATE TABLE IF NOT EXISTS guilds (
    guild_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    guild_name VARCHAR(50) UNIQUE NOT NULL,
    tag VARCHAR(4) UNIQUE NOT NULL,
    level INT DEFAULT 1,
    experience BIGINT DEFAULT 0,
    gold_balance BIGINT DEFAULT 0,
    guild_pesos BIGINT DEFAULT 0,
    wood_resources INT DEFAULT 0,
    stone_resources INT DEFAULT 0,
    food_resources INT DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    leader_id UUID NOT NULL,
    tax_rate DOUBLE PRECISION DEFAULT 0.0,
    FOREIGN KEY (leader_id) REFERENCES players(player_id)
);

CREATE TABLE IF NOT EXISTS guild_members (
    guild_id UUID NOT NULL,
    player_id UUID NOT NULL,
    rank VARCHAR(20) DEFAULT 'MEMBER',
    personal_rating INT DEFAULT 0,
    join_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    last_contribution TIMESTAMP,
    weekly_contribution INT DEFAULT 0,
    PRIMARY KEY (guild_id, player_id),
    FOREIGN KEY (guild_id) REFERENCES guilds(guild_id) ON DELETE CASCADE,
    FOREIGN KEY (player_id) REFERENCES players(player_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS guild_buildings (
    building_id BIGSERIAL PRIMARY KEY,
    guild_id UUID NOT NULL,
    building_type VARCHAR(30) NOT NULL,
    level INT DEFAULT 1,
    upgrade_progress INT DEFAULT 0,
    is_active BOOLEAN DEFAULT TRUE,
    last_maintenance TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (guild_id) REFERENCES guilds(guild_id) ON DELETE CASCADE
);

-- Скиллы --

CREATE TABLE IF NOT EXISTS player_skills (
    player_id UUID NOT NULL,
    skill_id INT NOT NULL,
    skill_level INT DEFAULT 1,
    experience_points INT DEFAULT 0,
    is_unlocked BOOLEAN DEFAULT FALSE,
    last_used TIMESTAMP,
    PRIMARY KEY (player_id, skill_id),
    FOREIGN KEY (player_id) REFERENCES players(player_id) ON DELETE CASCADE,
    FOREIGN KEY (skill_id) REFERENCES skills(skill_id)
);

-- Эффекты --

CREATE TABLE IF NOT EXISTS active_effects (
    effect_id BIGSERIAL PRIMARY KEY,
    player_id UUID NOT NULL,
    effect_type VARCHAR(50) NOT NULL,
    source_type VARCHAR(20) NOT NULL,
    source_id INT,
    duration_ms BIGINT NOT NULL,
    start_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    end_time TIMESTAMP NOT NULL,
    stacks INT DEFAULT 1,
    effect_data JSONB,
    FOREIGN KEY (player_id) REFERENCES players(player_id) ON DELETE CASCADE
);

-- Индексы для быстрого доступа --

CREATE INDEX IF NOT EXISTS idx_players_username ON players(username);
CREATE INDEX IF NOT EXISTS idx_players_last_login ON players(last_login);
CREATE INDEX IF NOT EXISTS idx_inventory_player ON player_inventory(player_id);
CREATE INDEX IF NOT EXISTS idx_inventory_slot ON player_inventory(player_id, slot);
CREATE INDEX IF NOT EXISTS idx_auction_expires ON auction_listings(expires_at) WHERE status = 'ACTIVE';
CREATE INDEX IF NOT EXISTS idx_transactions_player ON currency_transactions(player_id, created_at);
CREATE INDEX IF NOT EXISTS idx_auction_seller ON auction_listings(seller_id);
CREATE INDEX IF NOT EXISTS idx_auction_status ON auction_listings(status);
CREATE INDEX IF NOT EXISTS idx_bids_listing ON auction_bids(listing_id);
CREATE INDEX IF NOT EXISTS idx_bids_bidder ON auction_bids(bidder_id);
CREATE INDEX IF NOT EXISTS idx_guild_members_player ON guild_members(player_id);
CREATE INDEX IF NOT EXISTS idx_guild_members_guild ON guild_members(guild_id);
CREATE INDEX IF NOT EXISTS idx_active_effects_player ON active_effects(player_id);
CREATE INDEX IF NOT EXISTS idx_effects_expire ON active_effects(end_time) WHERE end_time > CURRENT_TIMESTAMP;
CREATE INDEX IF NOT EXISTS idx_skills_player ON player_skills(player_id);
CREATE INDEX IF NOT EXISTS idx_loans_player ON player_loans(player_id);
CREATE INDEX IF NOT EXISTS idx_loans_due_date ON player_loans(due_date) WHERE status = 'ACTIVE';
CREATE INDEX IF NOT EXISTS idx_players_class_race ON players(class_id, race_id);
CREATE INDEX IF NOT EXISTS idx_inventory_item ON player_inventory(item_id);
CREATE INDEX IF NOT EXISTS idx_skills_class ON skills(class_id);
CREATE INDEX IF NOT EXISTS idx_effects_type ON active_effects(effect_type);