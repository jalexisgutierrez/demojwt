# Despliegue del reto técnico

- Primero como el proyecto está dockerizado solo tienes que tener docker instalado en tu maquina.
- Luego ejecutas el siguiente comando **sudo docker compose up -d --build**.
- ese comando levantará los contenedores para cada micro servicio y para cada base de datos.

## Curls de Login 
- curl -s -X POST http://localhost:8080/auth/login   -H "Content-Type: application/json"   -d '{"email":"admin@mail.com","password":"admin"}'
ese curl te devuelve un token que nos servirá para más adelante, ten en cuenta ese token, y este curl sirve tanto para ADMIN como para SOCIO.
- curl -X POST "http://localhost:8080/auth/register-socio" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{                                    
  "email": "nuevo.socio@example.com",
  "password": "ContraseñaSocio"
  }', Este curl solo lo pueden usar el ADMIN y Sirve para registrar socios.

## Curls de Parqueadero 
- curl -X POST "http://localhost:8081/lots"   -H "Content-Type: application/json"   -H "Authorization: Bearer $TOKEN_SOCIO"   -d '{
  "name": "Parqueadero Centro 2",
  "capacity": 50,
  "hourlyRate": 2500.50
  }'
Este curl nos permite crear un parkeadero con sus respectivos valores y en la parte del token puede ir el token de admin o el de socio, y cuando se crea queda asignado a el usuario que lo creó.
- curl -X GET "http://localhost:8081/lots/$LOT_ADMIN" \
  -H "Authorization: Bearer $TOKEN_ADMIN"
Este curl sirve para traer la info del LotParking se le debe enviar el UUID del LotParking que está en la tabla parking_lot y asegurarse que ese pertenezca a el admin o socio que se crea para poder enviar el token correspondiente.
- curl -X POST "http://localhost:8081/entries/df42ba33-f4ef-4c46-92f0-c600c9608035/enter" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $ADMIN_TOKEN" \
  -d '{
  "plate": "ABC123"
  }'
Este curl nos permite con el UUID del parqueadero registrar un vehiculo en su estacionamiento.
- curl -X POST "http://localhost:8081/entries/$LOT_ADMIN/exit/ABC123" \
  -H "Authorization: Bearer $TOKEN_ADMIN"
Este curl nos permite registrar la salida del vehiculo del parqueadero y saber cuanto se le debe cobrar.
- curl -X GET "http://localhost:8081/entries/$LOT_ADMIN/search?q=ABC"   -H "Authorization: Bearer $TOKEN_ADMIN"
Este curl nos ayuda a realizar una busqueda de vehiculo en el parqueadero por placa.
- curl -X GET "http://localhost:8081/indicators/top10/all" \
  -H "Authorization: Bearer $ADMIN_TOKEN" Este Curl nos ayuda a ver el top 10 de clientes.
- curl -X GET "http://localhost:8081/indicators/top10/lot/$LOT_ADMIN" \
  -H "Authorization: Bearer $TOKEN_ADMIN"
Este curl también nos trae un top 10 pero por parqueadero.
- curl -X GET "http://localhost:8081/indicators/first-timers/$LOT_ADMIN" \
  -H "Authorization: Bearer $TOKEN_ADMIN" Este curl nos ayuda a ver los primeros clientes o clientes nuevos por parqueadero.
- curl -X GET "http://localhost:8081/indicators/earnings/$LOT_ADMIN" \
  -H "Authorization: Bearer $TOKEN_ADMIN"
 Este curl sirve para ver las ganancia por parqueadero.
- curl -X GET "http://localhost:8081/indicators/earnings/$LOT_ADMIN?date=2025-08-01" \
  -H "Authorization: Bearer $TOKEN_ADMIN"
 Este curl también nos ayuda a ver las ganancia pero en un fecha en especifica.
- curl -X POST "http://localhost:8081/entries/$LOT_ADMIN/notify/$PLATE3"   -H "Content-Type: application/json"   -H "Authorization: Bearer $TOKEN_ADMIN"   -d '{
  "to": "cliente@example.com",
  "message": "Su vehículo está bloqueando la salida."
  }'
Este curl nos permite enviar un email a traves del consumo de un endpoint expuesto por el micro servicio mailer-service.


# Curl Mailer Service
- curl -X POST "http://localhost:8083/mail/send" \
  -H "Content-Type: application/json" \
  -d '{
  "email": "cliente@example.com",
  "placa": "ABC123",
  "mensaje": "Su vehículo está bloqueando la salida",
  "parqueaderoNombre": "Parqueadero Centro"
  }'
prueba el envio de correos con su respuesta {"mensaje":"Correo Enviado"}


