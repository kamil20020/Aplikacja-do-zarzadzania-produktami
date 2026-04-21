# Aplikacja do zarządzania produktami

Prosta aplikacja webowa służąca do zarządzania różnymi produktami. Aplikacja składa się z
frontendu i backendu. Backend udostępnia kilka endpointów do zarządzania produktami a
frontent używa tych endpointów.

## Wymagania funkcjonalne
* Logowanie,
* Rejestracja,
* Wylogowanie
* Pobieranie listy produktów,
* Wyszukiwanie produktów po kategorii,
* Pobieranie szczegółów produktu,
* Dodawawanie nowego produktu,
* Aktualizacja produktu,
* Usuwanie produktu,
* Przygotowanie testowej aplikacji z przykładowymi danymi.

## Wymagania niefunkcjonalne
* Bezpieczeństwo
	- Wszystkie endpointy w backendzie powinny być zabezpieczone,
	- Istnienie roli użytkownika oraz admina,
	- Modifykowanie produktów jest możliwe tylko przez admina,
	- Wykorzystanie technologii JWT do zabezpieczenia aplikacji,
	- Podstawowa obługa błędów w backendzie np. sprawdzanie danych wejściowych.
* Testy:
	- Testy jednostkowe repozytoriów, kontrolerów oraz serwisów,
	- Testy integracyjne kontrolerów z użyciem wbudowanej bazy danych,
	- Testy bezpieczeństwa dla poszczególnych ról użytkowników.
* Technologie:
	a) Backend:
		- Java 17,
		- Spring Boot 3,
		- Spring Web,
		- Spring Data Jpa,
		- Spring Security,
		- Lombok,
		- Mapstruct,
		- Restassured,
		- Mockito,
		- Testcontainers
		- Maven,
		- Intellij.
	b) Frontend:
		- JavaScript / TypeScript,
		- Angular,
		- Visual Studio Code.
	c) Inne:
		- Baza danych PostgreSQL,
		- Zarządzanie bazą danych poprzez PgAdmin,
		- System kontroli wersji git i hosting GitHub,
		- Konteneryzacja całej aplikacji z Docker i Docker Compose,
		- Komunikacja między frontendem i backendem poprzez REST,
		- JSON jako format wiadomości przesyłanych między frontendem i backendem,
		- Modelowanie diagramu ERD bazy danych przy pomocy Visual Paradigm.
* Stworzenie prostej dokumentacji,
* Przygotowanie testowej aplikacji z przykładowymi danymi.

## Diagram ERD
<p align="center">
    <img src="./docs/erd/erd.png">
<p>
