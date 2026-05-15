# Unbounded Knapsack Problem – Java

Projekt wykonany w języku Java w ramach laboratorium z przedmiotu Platformy Programistyczne .NET i Java.

## Opis projektu

Aplikacja realizuje rozwiązanie nieograniczonego problemu plecakowego (Unbounded Knapsack Problem) z wykorzystaniem algorytmu zachłannego.

Program:

- generuje losową instancję problemu,
- tworzy przedmioty o losowej wadze i wartości,
- sortuje przedmioty według stosunku wartości do wagi,
- wypełnia plecak najbardziej opłacalnymi przedmiotami,
- wyświetla wynik działania algorytmu,
- zawiera testy jednostkowe JUnit.

## Technologie

- Java 21
- Maven
- Lombok
- JUnit 5

## Struktura projektu

```text
src
├── main
│   └── java
│       └── org.example
│           ├── Main.java
│           ├── Problem.java
│           ├── Result.java
│           └── Item.java
│
└── test
    └── java
        └── org.example
            └── ProblemTest.java
```

## Opis klas

### Item

Klasa reprezentująca pojedynczy przedmiot.

Przechowuje:
- wartość,
- wagę,
- stosunek wartości do wagi.

### Problem

Klasa odpowiedzialna za:
- generowanie instancji problemu,
- przechowywanie listy przedmiotów,
- rozwiązanie problemu metodą zachłanną.

### Result

Klasa przechowująca wynik działania algorytmu:
- indeksy przedmiotów,
- liczby przedmiotów,
- całkowitą wartość,
- całkowitą wagę.

### ProblemTest

Klasa zawierająca testy jednostkowe JUnit.

## Algorytm

Program wykorzystuje algorytm zachłanny:

1. Sortowanie przedmiotów według współczynnika:
   value / weight
2. Dodawanie najbardziej opłacalnych przedmiotów do plecaka.
3. Zakończenie po zapełnieniu plecaka lub sprawdzeniu wszystkich przedmiotów.

## Uruchomienie programu

### Kompilacja

```bash
mvn clean install
```

### Uruchomienie

```bash
mvn exec:java
```

### Uruchomienie testów

```bash
mvn test
```

## Przykładowy wynik

```text
Problem items:
0: Item(value=3, weight=1)
1: Item(value=7, weight=10)
2: Item(value=6, weight=8)

Capacity: 20

Items in backpack:
Item 0 x 20

Total value: 60
Total weight: 20
```

## Autor

Projekt wykonany w ramach laboratorium nr 5.