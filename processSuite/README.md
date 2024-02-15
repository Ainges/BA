# processSuite

## Grundvoraussetzungen

### Docker

<https://www.docker.com/get-started>

### .NET 6.0

<https://dotnet.microsoft.com/en-us/download/dotnet/6.0>

### Node.js

Um das Projekt starten zu können ist die Installation von Node.js vorausgesetzt, welches von [der offiziellen Webseite](https://nodejs.org/en/download/)
heruntergeladen werden kann.

## Build und Start

Dieser Abschnitt zeigt die Grundlegenden Befehle, die zum Starten des Projekts notwendig sind.

### Basic Build

```zsh
dotnet build
# bzw:
dotnet build apps/processSuite
```

### Build

Folgender Befehl:

- Baut das apps/processSuite Projekt
- Installiert die NPM-Pakete
- Baut das Frontend
- Baut/pulled die benötigten Docker-Images

```zsh
dotnet msbuild -t:Setup
```

### Start

Zum Starten des Projekts kann `docker compose up` aufgerufen werden, **nachdem** das `Setup` erfolgreich ausgeführt wurde.
Zur Vereinheitlichung und Vereinfachung wird das Projekt gestartet mit:

```zsh
dotnet msbuild -t:Up
```

Wenn `Setup` und `Up` nacheinander ausgeführt werden sollen:

```zsh
dotnet msbuild -t:Start
```

Daraufhin wird der Docker Container zusammengebaut und gestartet.

### Aufräumen

Das Aufräumen der App wird automatisch nach einem Clean des Projektes durchgeführt, wenn das `Clear` Target entsprechend konfiguriert ist.

```xml
<Target Name="Clear" AfterTargets="Clean">
```

Folgender Befehl:

- Löscht die Ausgabe der vorigen Builds
- Räumt die Docker-Container ab
- Löscht das Docker-Image der App
- Löscht die `node_modules` und die `package-lock.json` im Frontend
- Löscht die Datenbank der 5Minds Engine

```zsh
dotnet clean
```

Bei einem folgenden `Setup` werden **alle** Dateien neu geladen und gebaut.

