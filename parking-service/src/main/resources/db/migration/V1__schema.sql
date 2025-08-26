-- Extensiones útiles
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Tabla: parking_lot
CREATE TABLE IF NOT EXISTS parking_lot (
  id               UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
  name             VARCHAR(120) NOT NULL,
  capacity         INTEGER NOT NULL CHECK (capacity > 0),
  hourly_rate      NUMERIC(12,2) NOT NULL CHECK (hourly_rate > 0),
  owner_user_id    UUID NOT NULL,
  owner_email      VARCHAR(180) NOT NULL,
  created_at       TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW()
);

CREATE UNIQUE INDEX IF NOT EXISTS uq_parking_lot_owner_name
  ON parking_lot(owner_user_id, lower(name));

-- Tabla: vehicle_entry (vehículos actualmente dentro)
CREATE TABLE IF NOT EXISTS vehicle_entry (
  id               UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
  plate            VARCHAR(6) NOT NULL,
  parking_lot_id   UUID NOT NULL REFERENCES parking_lot(id) ON DELETE CASCADE,
  entered_at       TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
  CONSTRAINT chk_plate_format_entry CHECK (plate ~ '^[A-HJ-NP-Za-hj-np-z0-9]{6}$')
);

CREATE INDEX IF NOT EXISTS ix_entry_parking_lot ON vehicle_entry(parking_lot_id);
CREATE UNIQUE INDEX IF NOT EXISTS uq_entry_plate ON vehicle_entry(upper(plate));

-- Tabla: vehicle_history (vehículos que ya salieron)
CREATE TABLE IF NOT EXISTS vehicle_history (
  id               UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
  plate            VARCHAR(6) NOT NULL,
  parking_lot_id   UUID NOT NULL REFERENCES parking_lot(id) ON DELETE CASCADE,
  entered_at       TIMESTAMP WITH TIME ZONE NOT NULL,
  exited_at        TIMESTAMP WITH TIME ZONE NOT NULL,
  total_cost       NUMERIC(12,2) NOT NULL CHECK (total_cost >= 0),
  CONSTRAINT chk_plate_format_hist CHECK (plate ~ '^[A-HJ-NP-Za-hj-np-z0-9]{6}$'),
  CONSTRAINT chk_time_order CHECK (exited_at > entered_at)
);

CREATE INDEX IF NOT EXISTS ix_hist_parking_lot ON vehicle_history(parking_lot_id);
CREATE INDEX IF NOT EXISTS ix_hist_plate ON vehicle_history(upper(plate));
CREATE INDEX IF NOT EXISTS ix_hist_exited_at ON vehicle_history(exited_at);

-- Vistas/materialización ligera para indicadores (si quieres acelerar)
-- Aquí no creamos vistas; los repos usan queries nativas con UNION ALL (ver abajo).
