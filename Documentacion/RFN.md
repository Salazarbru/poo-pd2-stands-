# Requisitos No Funcionales (RFN) – Módulo de Notificaciones

RFN01- Extensibilidad de notificaciones
El sistema debe permitir agregar nuevos tipos de notificación (nuevos observadores) sin modificar la lógica existente de negocio, siguiendo el patrón Observer.

RFN02- Desacoplamiento del paquete de notificaciones
Las notificaciones deben generarse en un paquete independiente (notificaciones), desacoplado de las clases de datos y de negocio.

RFN03- Configurabilidad de los plazos
Las fechas que disparan las notificaciones (vencimiento de reserva, pronto pago y pago total) deben ser configurables, sin estar fijas ("hardcodeadas") en el código.

RFN04- Trazabilidad
El sistema debe permitir verificar (por ejemplo, mediante mensajes en consola o un registro) qué notificaciones fueron emitidas y a qué editorial, para facilitar pruebas y depuración.
