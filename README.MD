# Test task: Implementation of a REST application "Gas & Water Usage Monitoring Application"

Create an application to monitor gas, cold and hot water usage. No UI needed, only REST API. Two REST API methods should be implemented: one for submitting the current measurements for a given user, other for getting the history of previously submitted measurements for a given user. User inputs should be validated to reject incomplete or invalid data.

Technical Requirements
1.	Use Java 1.8, Spring Framework and Maven.
2.	Use other Java libraries as needed.
3.	Use HSQLDB for storing data. It is ok NOT to persist data across application launches.
4.	Try to follow all the good principles of writing high-quality and testable code. Use Swagger to describe REST. When creating a database, do not forget about indexes, etc., etc.
5.	Fill in missing requirements as you feel suitable.
6.	Include a short README file describing how the application works and how to build and run the project.
      The approximate time to complete this sample task is 6 hours. Focus on the most essential features first. It is fine to leave TODOs in places across the code, where a certain feature is missing, but you would have implemented it if you had more time.

---


  <strong>
     🎮 <a href="https://meshtest-knastnt.herokuapp.com/" target="_blank">Показать Демо (login:user password:1)</a>
      <br> 🎁 <a href="https://github.com/knastnt/meshtest/archive/v1.0.zip" target="_blank">Скачать проект ZIP</a>
      <br> ☘ <a href="https://github.com/knastnt/meshtest/releases/download/v1.0/meshtest-0.0.1-SNAPSHOT.jar" target="_blank">Скачать JAR</a>
  </strong>


---

**Задание:** <a href="https://docs.google.com/document/d/1FKiNwuKPcfVhAa0p6ipLIlyT8uTrIIo8Ii_UfgmW5GQ/edit" target="_blank">https://docs.google.com/document/d/1FKiNwuKPcfVhAa0p6ipLIlyT8uTrIIo8Ii_UfgmW5GQ/edit</a>

---

##  📣 Application description

Used stack: Java 8, Maven, Spring Boot, Spring Web, Spring Data JPA (Hibernate), HSQL, Liquibase, Swagger.

Application starts at ```8080``` port.

---

Приложение позволяет вести базу данных клиентов (Фамилия, Имя, Отчество).
У каждого клиента может быть несколько счетов (например, по счёту на каждую квартиру клиента).
Счёт имеет уникальный идентификатор (например, 1801345/22), а также список привязанных к нему счётчиков.
В системе реализованы 2 типа счётчиков: счетчик газа и счётчик воды. Последний может быть либо для горячей, либо для холодной воды.
Каждый счётчик имеет параметры: идентификационный номер, дата ввода в эксплуатацию и даты вывода из неё.


---


## REST эндпоинты

> **Note:** Для документирования REST к приложению подключен <a href="https://swagger.io/">Swagger</a>:<br>
> <a href="http://localhost:8080/v2/api-docs">localhost:8080/v2/api-docs</a><br>
> <a href="http://localhost:8080/documentation/swagger-resources">localhost:8080/documentation/swagger-resources</a><br>
> <a href="http://localhost:8080/documentation/swagger-ui/">localhost:8080/documentation/swagger-ui/</a>
>
####В приложении реализованы 3 эндпоинта:


###```GET /account/{accountNumber}```
Отображает информацию о счёте. Включает в себя информацию о клиенте, информацию о прикрепленных к счёту счётчиках и характеристики счётчиков.
Пример ответа:
```
{
  "userName": "Иван Иванович И.",   //ФИО клиента. Отображается только первая буква фамилии
  "accountNumber": "1801345/22",    //Счёт клиента
  "meters": [                       //Список счётчиков, прикреплённых к счёту
    {
      "id": 24,                     //Уникальный идентификатор счётчика. Используется при передаче показаний и при отображении истории показаний
      "identity": "X000123",        //Серийный номер счётчика. Передаётся на фронт и используется для идентификации счётчика клиентом
      "type": "WATER_HOT",          //На данный момент доступно 3 типа счетчиков: WATER_HOT, WATER_COLD, GAS
      "currentValue": 9.93,         //Отображается последнее переданное показание по этому счётчику
      "startWorking": "2022-10-05", //Дата ввода в эксплуатацию
      "endWorking": null            //Дата вывода из эксплуатации
    },
    {
      "id": 25,
      "identity": "X000100",
      "type": "WATER_HOT",
      "currentValue": null,
      "startWorking": "2021-01-01",
      "endWorking": "2022-10-04"
    },
    {
      "id": 26,
      "identity": "G000200",
      "type": "GAS",
      "currentValue": null,
      "startWorking": "2022-09-01",
      "endWorking": null
    }
  ]
}
```

```GET /meter/{meterId}/history``` 

```POST http://localhost:8080/meter/{meterId}/history``` 



## Структура проекта


```
├── model/                          # каталог с Entity
|   └── Profile                     # Entity объект профиля
├── repository/                     # интерфейсы репозиториев
|   └── CrudProfileRepository       # наследник JpaRepository с парой дополнительных методов
├── util/                           # утильные классы
│   ├── exception/                  # объекты связанные с исключениями
│   |   ├── ErrorInfo               # объект возвращаемого JSON исключения
│   |   └── NotFoundException       # кастомный эксэпшн
|   └── ExceptionUtil               # утильные методы связанные с обработкой исключений
├── web/                            # классы непосредственно относящиеся к web взаимодействию
│   ├── error/                      # контроллеры исключений
│   |   └── ErrorRestController     # контроллер обрабатывающий GET /error/last
│   ├── profile/                    # контроллеры профиля
│   |   └── ProfileRestController   # контроллер обрабатывающий /profiles
│   ├── ExceptionInfoHandler        # обработчик исключений
│   └── RootController              # контроллер обрабатывающий GET /exit-success
├── MeshtestApplication             # главный класс приложения
└── WebSecurityConfig               # настройки Spring Security
...
── application.properties           # настройки приложения (порт, соединение с БД, генерация DDL, логирование...)

```





* Konstantin Zubrilin | [HH.ru](https://komsomolsk-na-amure.hh.ru/resume/2797db4fff056344b60039ed1f486a50525650) |  456kot@mail.ru


todo:
 1.сделать логику работы счётчика по циклу, в соответствии с максимальным значением на циферблате
 2.сделать защиту от сабмита показаний на чужой счётчик
 3.добавить контроллеры для управления пользователями/счетами/счётчиками/показаниями