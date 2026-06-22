# ImageProcessor – Java + JavaFX

Projekt wykonany w języku Java w ramach laboratorium z przedmiotu Platformy Programistyczne .NET i Java.

## Opis projektu

Aplikacja desktopowa (JavaFX) umożliwiająca wczytywanie, przetwarzanie i zapisywanie obrazów w formacie JPG.

Program:

- wczytuje obraz JPG wybrany przez użytkownika,
- wyświetla obraz oryginalny obok obrazu po zmianach,
- wykonuje operacje przetwarzania obrazu w sposób wielowątkowy,
- umożliwia skalowanie i obracanie obrazu,
- pozwala na zapisanie wyniku do pliku JPG,
- prowadzi log działań aplikacji w pliku `app_log.txt`.

## Technologie

- Java 21
- JavaFX 21 (controls, swing)
- Maven
- ExecutorService (wielowątkowość)

## Struktura projektu

```text
src
└── main
    └── java
        ├── Launcher.java
        ├── ImageProcessorApp.java
        ├── controller
        │   └── MainController.java
        ├── service
        │   ├── ImageService.java
        │   └── LoggerService.java
        └── ui
            └── Toast.java
```

## Opis klas

### Launcher

Punkt wejścia uruchamiający aplikację JavaFX (workaround dla modułów JavaFX).

### ImageProcessorApp

Główna klasa aplikacji JavaFX odpowiedzialna za:
- budowę interfejsu użytkownika (nagłówek, panel sterowania, podgląd obrazów, stopka),
- konfigurację przycisków oraz listy operacji,
- powiązanie zdarzeń z metodami kontrolera.

### MainController

Kontroler obsługujący logikę interakcji użytkownika:
- wczytywanie pliku JPG (z walidacją rozszerzenia),
- uruchamianie wybranej operacji przetwarzania,
- obsługa modali (próg, skalowanie, zapis),
- obracanie obrazu w lewo / w prawo,
- zapis wyniku do katalogu `Pictures` / `Obrazy` użytkownika.

### ImageService

Klasa odpowiedzialna za operacje na obrazach:
- `rotate` – obrót obrazu o 90° w lewo lub w prawo,
- `scale` – zmiana rozmiaru obrazu metodą najbliższego sąsiada,
- `applyParallelOperation` – wielowątkowe przetwarzanie obrazu z podziałem na 4 fragmenty (`ExecutorService` z 4 wątkami).

Obsługiwane operacje:
- **Negatyw** – inwersja składowych RGB,
- **Progowanie** – binaryzacja na podstawie wartości progu (0–255),
- **Konturowanie** – detekcja krawędzi z wykorzystaniem operatora Sobela.

### LoggerService

Synchronizowany logger zapisujący komunikaty do pliku `app_log.txt` w formacie:

```text
[yyyy-MM-dd HH:mm:ss] [LEVEL] message
```

### Toast

Komponent UI wyświetlający krótkie, znikające powiadomienia w dolnej części okna aplikacji.

## Uruchomienie programu

### Kompilacja

```bash
mvn clean install
```

### Uruchomienie

```bash
mvn javafx:run
```

## Obsługa aplikacji

1. **Wczytaj plik** – wybór pliku JPG z dysku.
2. **Wybór operacji** – lista rozwijana: *Negatyw*, *Progowanie*, *Konturowanie*.
3. **Wykonaj** – uruchomienie wybranej operacji na obrazie.
4. **Skaluj** – zmiana wymiarów obrazu (z możliwością przywrócenia oryginalnych).
5. **Obróć 90° ← / →** – obrót obrazu w lewo lub w prawo.
6. **Zapisz** – zapisanie obrazu pod podaną nazwą w katalogu `Pictures` / `Obrazy`.

## Przykładowy wpis w logu

```text
[2026-06-17 15:45:12] [INFO] Uruchomienie aplikacji.
[2026-06-17 15:45:34] [INFO] Pomyślnie załadowano plik: C:\Users\Anton\Pictures\sample.jpg
[2026-06-17 15:45:51] [INFO] Wykonanie operacji: Negatyw
[2026-06-17 15:46:08] [INFO] Przeskalowano obraz do: 800x600
[2026-06-17 15:46:30] [INFO] Zapisano plik: C:\Users\Anton\Pictures\wynik.jpg
```

## Autor

Projekt wykonany w ramach laboratorium nr 6.
