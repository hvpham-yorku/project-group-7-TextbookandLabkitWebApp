
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
