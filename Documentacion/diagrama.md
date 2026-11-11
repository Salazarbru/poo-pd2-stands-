Stand
atributos stand:
id
metros cuadrados (se establece un precio por metro cuadrado y se hace una multiplicacion de ese precio por la dimension del stand)





Ahora Stand pertenece a mapa ya que en el mapa tenemos diversos puestos de stand con su dimesion correspondiente, es decir que sin mapa de salon no existiría stand



Los Stand tienen 4 estados: DISPONIBLE, RESERVADO, OCUPADO, BLOQUEADO (ver diagrama de estados abajo).

---

## Diagrama de clases

```mermaid
classDiagram
    class Mapa {
        +listarDisponibles()
        +listarPorEstado(estado)
    }

    class Stand {
        -id
        -metrosCuadrados
        -precioM2
        +calcularCosto() float
    }

    class Editorial {
        -id
        -nombre
        -usuarioLibreria
    }

    class Reserva {
        -fechaInicio
        -fechaLimiteReserva
        -fechaLimiteProntoPago
        -fechaLimitePagoTotal
    }

    class Pago {
        -monto
        -fecha
        -tipo
        +aplicarDescuentoProntoPago()
    }

    Mapa "1" *-- "many" Stand : contiene
    Stand "1" -- "0..1" Reserva : tiene
    Editorial "1" -- "many" Reserva : realiza
    Reserva "1" *-- "many" Pago : registra
```

## Diagrama de estados (Stand)

```mermaid
stateDiagram-v2
    [*] --> DISPONIBLE

    DISPONIBLE --> RESERVADO : editorial reserva
    BLOQUEADO --> RESERVADO : transferencia a editorial

    RESERVADO --> OCUPADO : paga anticipo 15%
    RESERVADO --> DISPONIBLE : vence plazo de reserva sin pago

    OCUPADO --> [*] : pago total confirmado

    DISPONIBLE --> BLOQUEADO : admin bloquea (negociación interna)
```
