# e-waha
Paliwo tańsze niż myślisz!

## Kolaboracja: 
- [Bartłomiej Zadrożny](https://github.com/bzadrozny) - lider
- [Damian Charkiewicz](https://github.com/charkied)
- [Jakub Glinka](https://github.com/GlinkaJakub)
- [Bartosz Michałowski](https://github.com/jaskola8)
- [Jan Starczewski](https://github.com/jstarczewski)
- [Karol Wójciński](https://github.com/kwojcinski)
- [Szymon Welter](https://github.com/SzymonWelter)

## Krótki opis projektu:
Projekt e-Waha opiera się na zaprojektowaniu, realizacji oraz wdrożeniu rozwiąznaia pozawalającego na znalezienie najlepszej ceny paliwa w okolicy.

## Harmonogram:
| Data | Oczekiwany stan prac |
| ------ | ------ |
| 2020-11-26 | Przygotwanie środowiska oraz konspektu projektu |
| 2020-12-03 | Przygotowanie dokumentacji projektowej |
| 2020-12-05 | Akceptacja dokumentacji | 
| 2020-12-10 | Zainicjalizowana implementacja poszczególnych **Produktów** |
| 2021-01-07 | Zintegrowane **Produkty** bez **ML** | 
| 2021-01-14 | Zintegrowane **Produkty** z **ML** | 
| 2021-01-21 | **Produkt finalny** | 
| 2021-01-28 | Prezentacja **Produktu finalnego** | 

## Opis ogólny:
"Buffalo Project" jest to start-up budujący w portfolio wiele różnorodnych aplikacji przydatnych użytkownikom w codziennym życiu.
Projekt e-Waha jest to jeden z pierwszych projektów, reprezentujące portfolio Buffalo Project, a jego głównym założeniem jest w dużym skrócie wejście w potrzebę rynku znajdowania najlepszej ceny paliwa w okolicy.

## Definicje:
- **Projekt** - pełen proces projektowo-wytwórczy rozwiązania zdefiniowanego w dokumencie wymagań oraz powiązanych artefaktach.
- **Produkt** - niezależny wynik realizacji **Projektu** pozwalający na instalację oraz uruchomienie na określonym urządezniu oraz spełniający przypisane do niego wymagania.
- **Produkt finalny** - zbiór niezależnych **Produktów** projektu zintegrowanych ze spobą oraz w pełni pokrywająceych wszystkie zdefiniowane wymagania.
---
- **DB** - jeden z **Produktów** składających się na **Produkt finalny** będący odseparowaną bazą danych wykorzystywanych w ramach realizacji wymagań przez inne **Produkty** 
- **ML** - jeden z **Produktów** składających się na **Produkt finalny** instalowany na bycie serwerowym, odpowiedzialny za realizację funkcjonalności dotyczących _Machine Learning_.
- **Aplikacja mobilna** - jeden z **Produktów** składających się na **Produkt finalny** instalowany na urządzeniu mobilnym, umożliwiająca korzystanie z funkcjonalności **Produktu finalnego**.
- **Mobile service** - jeden z **Produktów** składających się na **Produkt finalny** instalowany na bycie serwerowym, pozwalający na integrację funkcjonalności z **Aplikacji mobilnej** oraz **ML** jak i **DB**.
---
- **Użytkownik** - persona korzystająca z **Produktu finalnego** za pośrednictwem aplikacji mobilnej
- **Konto użytkownik** - zbiór danych przypisanych do **Użytkownika** definiujący jego dane indentyfikacyjne oraz dane dodatkowe jak np. **Statystyki użytkownika**
- **Statystyki użytkownika** - zbiór danych przypisanych do **Użytkownika** reprezentujące jego aktywności w systemie.
---
- **Stacja** - niezależna stacja paliw, posiadająca zdefiniowaną nazwę, adres oraz markę
- **Zdjęcie cen** - plik w rastrowym formacie graficznym, wykorzystywany przez **ML** do rozpoznania na podstawie obrazu umieszczonych na nim cen przypisanych do rodzaju paliwa.
- **Ceny paliwa stacji** - zbiór danych pozwalający na jednoznaczne określenie cen przypisanych do rodzaju paliwa **Stacji** w określonym formacie.  
- **Mapa cen** - wizualna reprezentacja mapy regionu, zawierająca przypięte do niej dostępne **Ceny paliwa stacji** mieszczące się w danym zakresie oraz ostatnią datę aktualizacji.

## Produkt finalny
**Produkt finalny** składa się z 5 głównych warstw:
- **Dokumentacja** 
  * Dokument wymagań
  * Diagram przypadków użycia
  * Scenariusze użycia
- **Aplikacja mobilna**:
  * Udostępnianie **Cen paliw stacji**
  * Wyświetlanie **Mapy cen**
- **Mobile service**:
  * Obsługa komunikacji z **Aplikacji mobilnej** po stronie Azure
- **ML** 
  * przetwarzanie **Zdjęć cen** na **Ceny pailwa stacji** 
- **DB** 
  * przechowywanie i udostępniania danych z **ML**
  * przechowywanie danych **użytkowników**

![architektura](https://github.com/bzadrozny/e-waha/blob/main/Architektura%20fizyczna.png)

## Podział głównych odpowiedzialności: 
| Produkt | Zespół |
| ------ | ------ |
| Dokumentacja | [Bartłomiej Zadrożny](https://github.com/bzadrozny) |
| Aplikacja mobilna | [Jan Starczewski](https://github.com/jstarczewski), [Bartłomiej Zadrożny](https://github.com/bzadrozny) |
| Mobile service | [Jakub Glinka](https://github.com/GlinkaJakub), [Bartosz Michałowski](https://github.com/jaskola8) |
| DB | [Jakub Glinka](https://github.com/GlinkaJakub), [Bartosz Michałowski](https://github.com/jaskola8) |
| ML | [Damian Charkiewicz](https://github.com/charkied), [Karol Wójciński](https://github.com/kwojcinski) |
| DevOps | [Szymon Welter](https://github.com/SzymonWelter) |

## Powiązane artefakty: 
| Data dodania | Nazwa |
| ------ | ------ |
| 2020-11-26 | Diagram przypadków użycia |
| 2020-12-28 | Aplikacja mobilna |
| 2020-01-04 | Aplikacja serverowa |
| 2020-01-04 | Baza danych |

## Założenia
**Aplikacja mobilna**:
- język: Kotlin   
- środowisko uruchomieniowe: Android
- [dokumentacja](https://developer.android.com/kotlin)

**Mobile service**:
- język: Kotlin   
- środowisko uruchomieniowe: Kontener aplikacyjny
- [dokumentacja](https://azure.microsoft.com/en-us/services/app-service/mobile/)

**DB**:
- baza danych: PostgreSQL
- środowisko uruchomieniowe: AzureDB
- [dokumentacja](https://azure.microsoft.com/en-us/services/postgresql/)

**ML**:
- środowisko uruchomieniowe: Azure Cognitive Services
- [dokumentacja](https://azure.microsoft.com/pl-pl/services/cognitive-services/#api)

## Wymagania systemowe
### Wymagania funkcjonalne 
- rejestracja użytkownika na urządzeniu mobilnym
- usunięcie danych użytkownika
- rozpoznawanie cen paliwa stacji na podstawie zdjęcia cen 
- korekcja cen paliwa otrzymanych na podstawie zdjęcia cen
- udostępnienie cen paliwa stacji
- podgląd cen paliwa różnych stacji na mapie 
- wyświetlanie statystyk użytkownika

### Wymagania niefunkcjonalne
- rozpoznawanie cen paliwa stacji na podstawie zdjęcia cen w czasie nie dłuższym niż **XX** sek.
- alikacja mobilna kompatybilna ze środowiskiem Android nie starszym niż z wersją **X.X.X**
- wykorzystane biblioteki są 'opensource'
