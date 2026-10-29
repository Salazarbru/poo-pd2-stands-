# PROYECTO: Sistema de venta de stands - FILEY (feria del libro)

## 1. Contexto general

- Sistema con una parte administrativa (backend); el login es opcional.
- Se vende el espacio físico de los stands de la feria.
- Existe un mapa del salón que representa los espacios disponibles.
- Cada stand tiene:
  - id
  - metros cuadrados
  - precio por metro cuadrado (costo = m2 * precio_m2)
- Las editoriales son los clientes: se registran/usan una "librería" (biblioteca de acceso) para poder ver el mapa y reservar.
- Debe poder verse en todo momento qué stands están disponibles, reservados u ocupados (confirmados vía transferencia).

## 2. Estados de un stand

```
DISPONIBLE -> RESERVADO -> OCUPADO
     ^
BLOQUEADO (puede pasar a RESERVADO, esto debe ser un nuevo feature)
```

### BLOQUEADO
- Lo bloquea el administrador (negociación interna, fuera del sistema).
- El admin avisa al cliente que el stand ya está disponible para que lo compre.
- El cliente (editorial) debe registrarse y avisarle al admin.
- Se le entrega un QR de la reserva.
- Debe existir una función de **TRANSFERENCIA** para enviar un stand bloqueado directamente a una editorial (pasa de bloqueado a reservado).

### DISPONIBLE
- La editorial puede seleccionarlo y reservarlo.

### RESERVADO
- La editorial mostró interés y se le da la oportunidad de pagar.
- Tiene un plazo (ej. 1 mes) para pagar, definido por el admin (debe ser configurable).

### OCUPADO
- Ya se hizo un pago de anticipo (15% del total).
- Pronto pago: si paga antes de la fecha de vencimiento, obtiene 10% de descuento.
- Existe una fecha máxima de pago (configurable).
- La editorial debe quedar confirmada.

## 3. Notificaciones

- El sistema debe notificar a las editoriales cuando:
  1. La reserva está a punto de vencer.
  2. El plazo de pronto pago está a punto de vencer.
  3. La fecha límite de pago total está a punto de vencer.
- Patrón sugerido: **Observer** (un observador por tipo de evento/vencimiento).
- Debe existir un paquete dedicado a notificaciones de reservas.

## 4. Pagos

- **Pago por transferencia**: permite que un stand bloqueado se envíe directamente a una editorial.
- **Pago electrónico**: otro método de pago soportado.
- Condiciones de la editorial según su estado de pago:
  - No ha cubierto el 15% de anticipo de la reserva.
  - Cubrió el anticipo pero no el pago total.
  - Cubrió el pago total (confirmado).

## 5. Arquitectura propuesta

Separar en paquetes:

| Paquete | Contenido |
|---|---|
| `datos` | Clases de datos (Stand, Editorial, Reserva, Pago...) |
| `negocio` | Lógica de negocio (gestión de reservas, transferencias, cálculo de precios/descuentos) |
| `notificaciones` | Observers para vencimientos |

- Toda la operación (reservar, pagar, transferir, etc.) debe poder ejecutarse por línea de comandos.
- Cada stand debe poder asociarse a una editorial.
- Las fechas (plazo de reserva, pronto pago, pago máximo) deben ser configurables, no estar "hardcodeadas".

## 6. Pendiente de decidir

- Primer draft de requisitos y diagrama UML.
- Cómo definir/organizar exactamente las clases de cada paquete.
- Cómo administrar y disparar las notificaciones.
- Persistencia de datos: ¿objetos en memoria, o base de datos (H2 / Supabase)?
