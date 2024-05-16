# MovieGOAdmin
Głównym celem projektu jest stworzenie systemu informatycznego dla nowego kina. Administrator otrzyma dostęp do pewnych danych zawartych w bazie danych kina, między innymi będzie mógł wyświetlać/dodawać/edytować/usuwać filmy jak i seanse, usuwać użytkowników z bazy oraz wyświetlać historie zakupów dokonanych przez klientów kina.

Dzięki tym ułatwieniom dla pracowników kina, właściciel nowego kina będzie mógł lepiej i efektywniej nim zarządzać poprzez monitorowanie sprzedaży, zmianę ceny biletów czy ulepszanie infrastruktury.

>[!Important]
> Niezbędne jest jednoczesne uruchomienie serwera MovieGOApi, aby skutecznie zmienić dane w bazie.
> Dla wersji działającej TYLKO dla [MovieGONative](https://github.com/EagleBlood/MovieGONative) musisz przełączyć się na gałąź [```main-reactNative```](https://github.com/EagleBlood/MovieGOAdmin/tree/main-reactNative). Nie zapomnij również użyć poprawnej wersji API, ponieważ niezastosowanie się do tego może powodować nieoczekiwane błędy.

## Funkcjonalność

By połączyć się z bazą kina musisz zalogować się do bazy danych poprzez menu logowania.<br>
<p align="center">
  <img src="https://github.com/EagleBlood/MovieGOAdmin/blob/main/imgs/1.PNG" alt="Ekran logowania" />
</p>
<br>


Po udanym logowaniu możemy rozpocząć zarządzanie kinem.<br>
<p align="center">
  <img src="https://github.com/EagleBlood/MovieGOAdmin/blob/main/imgs/2.PNG" alt="Menu główne" />
</p>
<br>


Zakładka filmy:<br>
<table>
  <tr>
    <td align="center"><img src="https://github.com/EagleBlood/MovieGOAdmin/blob/main/imgs/3.PNG" alt="Zakładka do wyświetlania filmów"/></td>
    <td align="center"><img src="https://github.com/EagleBlood/MovieGOAdmin/blob/main/imgs/4.PNG" alt="Zakładka do dodawania nowego filmu"/></td>
  </tr>
  <tr>
    <td align="center"><img src="https://github.com/EagleBlood/MovieGOAdmin/blob/main/imgs/5.PNG" alt="Ekran edycji istniejącego filmu" /></td>
    <td align="center"><img src="https://github.com/EagleBlood/MovieGOAdmin/blob/main/imgs/6.PNG" alt="Zakładka do usuwania filmów" /></td>
  </tr>
</table>
<br>


Zakładka seanse:<br>
<table>
  <tr>
    <td align="center"><img src="https://github.com/EagleBlood/MovieGOAdmin/blob/main/imgs/7.PNG" alt="Zakładka do wyświetlania seansów" /></td>
    <td align="center"><img src="https://github.com/EagleBlood/MovieGOAdmin/blob/main/imgs/8.PNG" alt="Zakładka do dodawania seansów" /></td>
  </tr>
  <tr>
    <td align="center"><img src="https://github.com/EagleBlood/MovieGOAdmin/blob/main/imgs/9.PNG" alt="Ekran edycji seansów" /></td>
    <td align="center"><img src="https://github.com/EagleBlood/MovieGOAdmin/blob/main/imgs/10.PNG" alt="Zakładka do usuwania seansów" /></td>
  </tr>
</table>
<br>


Zakładka usuń użytkownika:<br>
<p align="center">
  <img src="https://github.com/EagleBlood/MovieGOAdmin/blob/main/imgs/11.PNG" alt="Ekran do usuwania użytkowników" />
</p>
<br>


Zakładka transakcje:<br>
<p align="center">
  <img src="https://github.com/EagleBlood/MovieGOAdmin/blob/main/imgs/12.PNG" alt="Ekran do wyświetlania transkacji użytkowników" />
</p>

## Instalacja
- Upewnij się, że masz zainstalowaną Javę na swoim komputerze.
- Sklonuj repozytorium projektu z GitHuba do lokalnego katalogu na swoim komputerze.
- Uruchom program w wybranym środowkisku JDK

## Narzędzia oraz biblioteki
* Maven
* Java (v. 19.0.1)
* mysql-connector-java (v. 8.0.32)
* org.openjfx.javafx - controls (v. 20.0.1)
* org.openjfx.javafx - fxml (v. 19.0.2.1)
