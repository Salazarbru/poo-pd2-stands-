# Justificación del uso del patrón Observer

## Problema

El sistema debe notificar a una editorial cuando se acerca el vencimiento de alguno de los
tres plazos asociados a una reserva (RF01, RF02, RF03):

- Vencimiento de la reserva (antes del pago de anticipo).
- Vencimiento del pronto pago (10% de descuento).
- Vencimiento del pago total.

Además, el estado de un `Stand` cambia como consecuencia de eventos de negocio (pago de
anticipo, pago total, vencimiento de plazos) y ese cambio también debe disparar una
notificación a la editorial correspondiente (RF05).

Si esta lógica se implementara llamando directamente a un método de notificación desde
`Reserva` o `Stand` (por ejemplo `enviarEmail()`, `logearEnConsola()`), la clase de negocio
quedaría acoplada a los mecanismos de notificación concretos. Cualquier notificación nueva
obligaría a modificar esa clase, violando el principio abierto/cerrado y mezclando lógica de
dominio con lógica de aviso.

## Por qué Observer resuelve esto

El patrón Observer separa el **sujeto que cambia de estado** (`Reserva` / `Stand`) de los
**interesados en reaccionar a ese cambio** (los distintos tipos de notificación), sin que el
primero conozca los detalles de los segundos:

- **Desacoplamiento (RFN02).** `Reserva` solo conoce la interfaz `Observer` (p. ej. un método
  `actualizar(evento)`), no las clases concretas de notificación. Toda la lógica de aviso vive
  en el paquete `notificaciones`, separado de `datos` y `negocio`, tal como exige la
  arquitectura propuesta.

- **Un observador por tipo de evento (RF04).** El enunciado pide explícitamente un observador
  distinto para cada vencimiento (reserva, pronto pago, pago total). Esto es exactamente el
  caso de uso canónico de Observer: múltiples observadores que reaccionan de forma
  independiente al mismo sujeto, cada uno con su propia condición y su propia acción.

- **Extensibilidad sin tocar el negocio (RFN01).** Si en el futuro se agrega una notificación
  por WhatsApp, un registro en base de datos, o una alerta al administrador, basta con crear
  una nueva clase que implemente `Observer` y suscribirla. No se modifica `Reserva`, `Stand`
  ni ninguna clase de negocio existente. Esto cumple directamente el principio abierto/cerrado
  (OCP) que pide RFN01.

- **Un mismo cambio, múltiples reacciones (RF05).** Cuando un `Stand` cambia de estado
  (RESERVADO → OCUPADO, por ejemplo), puede interesarle a más de un observador a la vez
  (notificación a la editorial, registro de trazabilidad, etc.). Observer permite que todos se
  enteren del mismo evento sin que el sujeto tenga que saber cuántos ni cuáles son.

- **Trazabilidad (RFN04).** Al tener observadores independientes, es trivial agregar uno
  dedicado solo a registrar en consola o en un log qué notificación se emitió y a qué
  editorial, sin ensuciar los observadores que sí envían el aviso real.

## Alternativas descartadas

- **Llamadas directas desde `Reserva`/`Stand`:** acopla negocio y notificación, y cada nuevo
  tipo de aviso requiere modificar la clase de dominio. Incumple RFN01 y RFN02.
- **Polling externo** (un proceso que recorra todas las reservas y revise fechas sin
  notificación basada en eventos): no hay una relación clara sujeto-observador, dificulta
  trazar qué disparó qué, y no responde naturalmente a RF05 (cambio de estado inmediato).

## Conclusión

Observer es la solución correcta porque el problema tiene exactamente la forma que el patrón
resuelve: un sujeto (`Reserva`/`Stand`) cuyo cambio de estado debe propagarse a varios
interesados (notificaciones por tipo de vencimiento), sin que el sujeto conozca su
implementación concreta ni su cantidad. Esto satisface RF04, RF05, RFN01 y RFN02 de forma
directa, y facilita RFN03 (los observadores reciben las fechas configurables como parámetro
del evento, no hardcodeadas) y RFN04 (trazabilidad vía un observador dedicado a logging).
