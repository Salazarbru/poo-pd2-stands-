# Requisitos Funcionales (RF) – Módulo de Notificaciones

RF01- Notificar vencimiento proximo de reserva
El sistema debe notificar a la editorial cuando la reserva de un stand esté a punto de vencer (Esto sin contar la fecha de primer pago), antes de la fecha limite.

RF02- Notificar vencimiento próximo de la fecha limite de pago total.
El sistema debe notificar a la editorial que la fecha máxima configurada para completar el pago total del stand está próxima a vencer.

RF03- Notificar vencimiento de fecha de pronto pago (descuento 10%)
El sistema debe notificar a la editorial que la fecha correspondiente al pago anticipado para obtener un 10% de descuento (Esto antes de la fecha de vencimiento de pago  )está a punto de vencer.

RF04- Suscribir observadores por tipo de evento
El sistema debe permitir registrar un observador distinto para cada tipo de evento de vencimiento (reserva, pronto pago, pago total), siguiendo el patrón Observer.

RF05- Emitir notificación al cambiar el estado de un stand
El sistema debe generar una notificación a la editorial correspondiente cuando el estado de un stand cambie (por ejemplo, de RESERVADO a OCUPADO tras confirmar el pago).

RF06- Ejecutar notificaciones por línea de comandos
El sistema debe permitir disparar o probar el envío de notificaciones desde la línea de comandos, de forma consistente con el resto de operaciones del sistema.
