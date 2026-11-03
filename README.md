# poo-pd2-stands-
Repositorio para alojar la segunda actividad de prgramacion orientada a objetos

## Sistema de venta de stands - FILEY

Implementación en Java (sin dependencias externas) del dominio Stand/Mapa/Editorial/Reserva/Pago
con el patrón **Observer** para las notificaciones de vencimiento (ver `Documentacion/`).

Paquetes:
- `com.filey.datos` — Stand, Mapa, Editorial, Reserva, Pago, enums de estado/tipo de pago.
- `com.filey.negocio` — `GestorReservas` (reservar, bloquear, transferir, pagar anticipo/total,
  chequear vencimientos) y `ConfiguracionPlazos` (plazos configurables, RFN03).
- `com.filey.notificaciones` — Observer/Sujeto y un observador por tipo de evento (RF04), más
  un observador de trazabilidad (RFN04).
- `com.filey.cli` — `Main`, el menú interactivo por consola (RF06).

### Cómo correrlo desde la terminal

Solo requiere el JDK (no hace falta Maven instalado para compilar/ejecutar):

```powershell
# Compilar
$files = Get-ChildItem -Recurse -Filter *.java -Path src | ForEach-Object { $_.FullName }
javac -d out -encoding UTF-8 $files

# Ejecutar
java -cp out com.filey.cli.Main
```

En bash/Git Bash:

```bash
find src -name "*.java" > sources.txt
javac -d out -encoding UTF-8 @sources.txt
java -cp out com.filey.cli.Main
```

Si tienes Maven instalado, también puedes usar `mvn compile exec:java` (hay un `pom.xml` listo).

El menú permite listar el mapa, reservar, bloquear, transferir, pagar anticipo/total, avanzar
la fecha simulada del sistema para forzar vencimientos, y ver el historial de notificaciones
emitidas.
