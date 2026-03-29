
CREATE TABLE IF NOT EXISTS users (
    email        VARCHAR(255) PRIMARY KEY,
    password     VARCHAR(255) NOT NULL,
    name         VARCHAR(255) NOT NULL,
    student_id   VARCHAR(50),
    phone_number VARCHAR(50),
    about_me     TEXT,
    program      VARCHAR(255),
    campus       VARCHAR(100)
);

-- -------------------------------------------------------
-- LISTINGS
-- id is auto-incremented (BIGSERIAL = auto sequence)
-- (consistent with stub behaviour).
-- -------------------------------------------------------
CREATE TABLE IF NOT EXISTS listings (
    id              BIGSERIAL PRIMARY KEY,
    seller_email    VARCHAR(255) NOT NULL,
    title           VARCHAR(500) NOT NULL,
    description     TEXT         NOT NULL,
    price           NUMERIC(10, 2) NOT NULL,
    course_code     VARCHAR(100),
    semester        VARCHAR(100),
    material_type   VARCHAR(100),
    condition       VARCHAR(50),
    exchange_type   VARCHAR(50),
    isbn            VARCHAR(50),
    bookstore_price NUMERIC(10, 2),
    date_posted     TIMESTAMP    NOT NULL DEFAULT NOW(),
    status          VARCHAR(50)  NOT NULL DEFAULT 'AVAILABLE',
    image_path      VARCHAR(500)
);

-- -------------------------------------------------------
-- MESSAGES (legacy stub — kept for reference)
-- sender_id / receiver_id are BIGINT user IDs.
-- Not used by the current contact flow; see contact_messages below.
-- -------------------------------------------------------
CREATE TABLE IF NOT EXISTS messages (
    id          BIGSERIAL PRIMARY KEY,
    listing_id  BIGINT    NOT NULL,
    sender_id   BIGINT,
    receiver_id BIGINT,
    content     TEXT      NOT NULL,
    timestamp   TIMESTAMP NOT NULL DEFAULT NOW()
);

-- -------------------------------------------------------
-- CONTACT MESSAGES  (KAN-93)
-- Stores messages sent from a buyer to a seller about
-- a specific listing.  Uses email addresses (consistent
-- with how users are identified elsewhere in the schema).
-- -------------------------------------------------------
CREATE TABLE IF NOT EXISTS contact_messages (
    id           BIGSERIAL    PRIMARY KEY,
    listing_id   BIGINT       NOT NULL,
    sender_email VARCHAR(255) NOT NULL,
    seller_email VARCHAR(255) NOT NULL,
    subject      VARCHAR(500) NOT NULL,
    message      TEXT         NOT NULL,
    created_at   TIMESTAMP    NOT NULL DEFAULT NOW()
);

-- -------------------------------------------------------
-- TRANSACTIONS
-- Tracks the exchange lifecycle between a buyer and seller
-- for a specific listing.
-- -------------------------------------------------------
CREATE TABLE IF NOT EXISTS transactions (
    id                BIGSERIAL    PRIMARY KEY,
    listing_id        BIGINT       NOT NULL,
    listing_title     VARCHAR(500),
    course_code       VARCHAR(100),
    source_message_id BIGINT       NOT NULL DEFAULT 0,
    buyer_email       VARCHAR(255) NOT NULL,
    seller_email      VARCHAR(255) NOT NULL,
    status            VARCHAR(50)  NOT NULL DEFAULT 'MEETUP_PENDING',
    buyer_confirmed   BOOLEAN      NOT NULL DEFAULT FALSE,
    seller_confirmed  BOOLEAN      NOT NULL DEFAULT FALSE,
    created_at        TIMESTAMP    NOT NULL DEFAULT NOW(),
    updated_at        TIMESTAMP    NOT NULL DEFAULT NOW()
);

-- -------------------------------------------------------
-- ISSUE REPORTS
-- Stores issue reports filed against a transaction.
-- -------------------------------------------------------
CREATE TABLE IF NOT EXISTS issue_reports (
    id             BIGSERIAL    PRIMARY KEY,
    transaction_id BIGINT       NOT NULL,
    reporter_email VARCHAR(255) NOT NULL,
    category       VARCHAR(50)  NOT NULL,
    severity       VARCHAR(50)  NOT NULL,
    description    TEXT         NOT NULL,
    created_at     TIMESTAMP    NOT NULL DEFAULT NOW()
);

-- -------------------------------------------------------
-- MATERIAL REQUESTS
-- Stores buyer requests for specific textbooks or lab kits.
-- -------------------------------------------------------
CREATE TABLE IF NOT EXISTS material_requests (
    id               BIGSERIAL    PRIMARY KEY,
    requester_email  VARCHAR(255) NOT NULL,
    course_code      VARCHAR(100),
    material_title   VARCHAR(500),
    material_type    VARCHAR(100),
    note             TEXT,
    status           VARCHAR(50)  NOT NULL DEFAULT 'OPEN',
    created_at       TIMESTAMP    NOT NULL DEFAULT NOW()
);

-- -------------------------------------------------------
-- DIRECT MESSAGES
-- Email-based direct chat messages between two users.
-- Replaces the legacy messages table (which used BIGINT IDs).
-- -------------------------------------------------------
CREATE TABLE IF NOT EXISTS direct_messages (
    id             BIGSERIAL    PRIMARY KEY,
    sender_email   VARCHAR(255) NOT NULL,
    receiver_email VARCHAR(255) NOT NULL,
    content        TEXT         NOT NULL,
    sent_at        TIMESTAMP    NOT NULL DEFAULT NOW(),
    is_read        BOOLEAN      NOT NULL DEFAULT FALSE
);
