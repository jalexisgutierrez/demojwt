-- Datos de prueba
INSERT INTO parking_lot (id, name, capacity, hourly_rate, owner_user_id, owner_email)
VALUES
  ('00000000-0000-0000-0000-000000000001', 'Mi Parqueadero', 50, 3.50, '11111111-1111-1111-1111-111111111111', 'socio@mail.com')
ON CONFLICT (id) DO NOTHING;
