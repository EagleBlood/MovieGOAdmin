# MovieGOAdmin

[![pl](https://img.shields.io/badge/lang-pl-red.svg)](README-PL.md)

The main goal of the project is to create an IT system for a new cinema. The administrator will receive access to certain data stored in the cinema's database, including the ability to view/add/edit/delete movies and screenings, remove users from the database, and view the purchase history made by cinema customers.

With these improvements for cinema staff, the owner of the new cinema will be able to manage it better and more efficiently by monitoring sales, changing ticket prices, and improving infrastructure.

> [!IMPORTANT]
> Its crusial to simultaneously run [MovieGOApi](https://github.com/EagleBlood/MovieGOApi) server to sucessfully change data in DB.
> For version that works **ONLY** for [MovieGONative](https://github.com/EagleBlood/MovieGONative) you need to switch to [```main-reactNative```](https://github.com/EagleBlood/MovieGOAdmin/tree/main-reactNative) branch. Don't forget to also use correct version of API as failing to do so may cause unexpected errors.

## Functionality

To connect to the cinema database, you need to log in to the database through the login menu.<br>
<p align="center">
  <img src="https://github.com/EagleBlood/MovieGOAdmin/blob/main-android/imgs/1.PNG" alt="Ekran logowania" />
</p>
<br>


After successful login, we can begin managing the cinema.<br>
<p align="center">
  <img src="https://github.com/EagleBlood/MovieGOAdmin/blob/main-android/imgs/2.PNG" alt="Menu główne" />
</p>
<br>


Movie section:<br>
<table>
  <tr>
    <td align="center"><img src="https://github.com/EagleBlood/MovieGOAdmin/blob/main-android/imgs/3.PNG" alt="Zakładka do wyświetlania filmów"/></td>
    <td align="center"><img src="https://github.com/EagleBlood/MovieGOAdmin/blob/main-android/imgs/4.PNG" alt="Zakładka do dodawania nowego filmu"/></td>
  </tr>
  <tr>
    <td align="center"><img src="https://github.com/EagleBlood/MovieGOAdmin/blob/main-android/imgs/5.PNG" alt="Ekran edycji istniejącego filmu" /></td>
    <td align="center"><img src="https://github.com/EagleBlood/MovieGOAdmin/blob/main-android/imgs/6.PNG" alt="Zakładka do usuwania filmów" /></td>
  </tr>
</table>
<br>


Screenings section:<br>
<table>
  <tr>
    <td align="center"><img src="https://github.com/EagleBlood/MovieGOAdmin/blob/main-android/imgs/7.PNG" alt="Zakładka do wyświetlania seansów" /></td>
    <td align="center"><img src="https://github.com/EagleBlood/MovieGOAdmin/blob/main-android/imgs/8.PNG" alt="Zakładka do dodawania seansów" /></td>
  </tr>
  <tr>
    <td align="center"><img src="https://github.com/EagleBlood/MovieGOAdmin/blob/main-android/imgs/9.PNG" alt="Ekran edycji seansów" /></td>
    <td align="center"><img src="https://github.com/EagleBlood/MovieGOAdmin/blob/main-android/imgs/10.PNG" alt="Zakładka do usuwania seansów" /></td>
  </tr>
</table>
<br>

Remove user section:<br>
<p align="center">
  <img src="https://github.com/EagleBlood/MovieGOAdmin/blob/main-android/imgs/11.PNG" alt="Ekran do usuwania użytkowników" />
</p>
<br>


Transactions tab:<br>
<p align="center">
  <img src="https://github.com/EagleBlood/MovieGOAdmin/blob/main-android/imgs/12.PNG" alt="Ekran do wyświetlania transkacji użytkowników" />
</p>

## Installation
- Make sure you have Java installed on your computer.
- Clone the project repository from GitHub to a local directory on your computer.
- Run the program in your chosen JDK environment.

## Tools and libraries
* Maven
* Java (v. 19.0.1)
* mysql-connector-java (v. 8.0.32)
* org.openjfx.javafx - controls (v. 20.0.1)
* org.openjfx.javafx - fxml (v. 19.0.2.1)
