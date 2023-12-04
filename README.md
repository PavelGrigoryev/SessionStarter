# SessionStarter приложение с spring-boot стартером для отслеживания сессий (код стартера в [starter](starter/src/main/java/ru/clevertec/starter))

## Автор: [Grigoryev Pavel](https://pavelgrigoryev.github.io/GrigoryevPavel/)

***

### Технологии, которые я использовал в стартере:

* Java 17
* Gradle 8.4
* Lombok plugin 8.4
* Spring-boot 3.2
* Spring-boot-starter-data-jpa (тянет как транзитивную зависимость)
* Spring-boot-starter-web (тянет как транзитивную зависимость)
* Spring-boot-configuration-processor

***

### Инструкция запуска модуля для [session](session/src/main/java/ru/clevertec/session):

* В него ходит стартер за сессиями через RestClient, он должен быть запущен первым.
* У вас должна быть установлена [Postgresql](https://www.postgresql.org/download/) (P.S: Postgresql можно развернуть
  в докере).
* В Postgresql нужно создать базу данных. Как пример: "session". Sql: CREATE DATABASE session.
* В [application.yaml](session/src/main/resources/application.yaml) в строчке №4 введите ваш username для
  Postgresql, в строчке №5 введите ваш password для Postgresql.
* При запуске приложения Liquibase сам создаст таблицу sessions.
* Модуль готов к использованию,
  запускаем [SessionApplication](session/src/main/java/ru/clevertec/session/SessionApplication.java).
* HTTP запросы: [sessions.http](session/src/main/resources/http/sessions.http).

***

### Инструкция для использования стартера:

* Запустить задачу в корне проекта SessionStarter `./gradlew -p starter publishToMavenLocal`
* Добавить в build.gradle в dependency своего проекта:

````groovy
dependencies {
    implementation 'ru.clevertec:starter:1.0'
}
````

* Зависимости `'org.springframework.boot:spring-boot-starter-web'`
  и `'org.springframework.boot:spring-boot-starter-data-jpa'` входят в стартер и будут подтянуты транзитивно.
* Добавить в application.yaml своего проекта, где `session.aware.url: ?????` это url сервиса хранения сессий:

 ````yaml
session:
  aware:
    enabled: true
    url: http://localhost:8081/sessions
````

* Для работы стартера над методом нужно ставить
  аннотацию: [@SessionAware](starter/src/main/java/ru/clevertec/starter/annotation/SessionAware.java).
* Параметры метода должны содержать [Session](starter/src/main/java/ru/clevertec/starter/model/Session.java) для
  отслеживания сессий. Если не будет, то
  вылетит [SessionAwareException](starter/src/main/java/ru/clevertec/starter/exception/SessionAwareException.java) c
  сообщением `"Must be a Session object in the parameters"`.
* Так же параметры метода должны содержать объект, который содержит в себе поле String login. Если сделать свой, то
  объект должен имплементировать со
  стартера [Login](starter/src/main/java/ru/clevertec/starter/model/Login.java) и переопределять его метод.
* Можно не имплементировать Login. Объект тогда должен быть классом. И у него должно быть поле login и должен быть
  геттер на это поле.
* Если геттеров не будет, то
  вылетит [SessionAwareException](starter/src/main/java/ru/clevertec/starter/exception/SessionAwareException.java) c
  сообщением `"The object must be a class or a record and have a Login implementation"`.
* Если будет больше одного объекта с не пустым полем login(помимо сессии), то вылетит
  [SessionAwareException](starter/src/main/java/ru/clevertec/starter/exception/SessionAwareException.java) c
  сообщением `"More than one objects with non-blank field login detected"`.
* Если не будет ни одного или будут с пустым полем login, то вылетит
  [SessionAwareException](starter/src/main/java/ru/clevertec/starter/exception/SessionAwareException.java) c
  сообщением `"Must be an object with a login non-blank field in the parameters"`.
* Порядок в параметрах метода для сессий и объектов с полем login не важен.
* В аннотации [@SessionAware](starter/src/main/java/ru/clevertec/starter/annotation/SessionAware.java) можно задать
  blackList логинов, которые не будут допущены к сессии и будет
  выброшен [BlackListException](starter/src/main/java/ru/clevertec/starter/exception/BlackListException.java) c
  сообщением `"Login is in black list for Sessions"`. Пример аннотации с blackList:

````java
@SessionAware(blackList = {"Ann", "Sasha"})
````

* BlackList можно задать и через класс. По умолчанию, используется
  [PropertyBlackListHandler](starter/src/main/java/ru/clevertec/starter/sevice/handler/impl/PropertyBlackListHandler.java),
  который тянет значение из property/yaml.
* Так же есть
  бин [SessionServiceBlackListHandler](starter/src/main/java/ru/clevertec/starter/sevice/handler/impl/SessionServiceBlackListHandler.java).
  По умолчанию отключен. Он берёт значения blackList из базы данных сессий. Подключить можно через аннотацию или
  property/yaml файл.
* Можно сделать свой blackList. Класс должен
  имплементировать [BlackListHandler](starter/src/main/java/ru/clevertec/starter/sevice/handler/BlackListHandler.java)
  интерфейс и переопределить метод `Set<String> getBlackList();`. Пример аннотации с blackListHandlers:

````java
@SessionAware(blackListHandlers = PersonBlackListHandler.class)
````

* И наконец blackList можно задать через application.yaml. Пример:

````yaml
session:
  aware:
    enabled: true
    url: http://localhost:8081/sessions
    black-list:
      - Helmut35
      - Sasha
    black-list-handlers:
      - ru.clevertec.testdata.config.PersonBlackListHandler
````

* Все blackList-ы заданные разными путями объединяются в один HashSet и фильтрация идёт по нему.
* Если сервис с сессиями не будет запущен, или не доступен, или введён не верный url, то
  вылетит [SessionServiceException](starter/src/main/java/ru/clevertec/starter/exception/SessionServiceException.java) с
  сообщением `"Service with sessions is disabled or not available on this url"`.
* Если сервис с сессиями вернет 500 статус, то
  вылетит [SessionServiceException](starter/src/main/java/ru/clevertec/starter/exception/SessionServiceException.java) с
  сообщением об ошибке от сервиса сессий.
* Пример кода с использованием:

````java

@RestController
@RequiredArgsConstructor
@RequestMapping("/persons")
public class PersonController {

    private final PersonService personService;

    @SessionAware(blackList = {"Ann", "Sasha"}, blackListHandlers = SessionServiceBlackListHandler.class)
    @GetMapping("/{id}")
    public ResponseEntity<PersonResponse> findById(@RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false)
                                                   Authorization authorization,
                                                   @PathVariable
                                                   Long id,
                                                   Session session) {
        return ResponseEntity.ok(personService.findById(id));
    }

    @SessionAware
    @PostMapping
    public ResponseEntity<PersonResponse> save(@RequestBody PersonRequest request, Session session) {
        return ResponseEntity.status(HttpStatus.CREATED).body(personService.save(request));
    }

}
````

***

### Можно протестировать на модуле [testdata](testdata/src/main/java/ru/clevertec/testdata). Для этого нужна:

* Для работы должен сперва запущен модуль с
  сессиями [SessionApplication](session/src/main/java/ru/clevertec/session/SessionApplication.java).
* У вас должна быть установлена [Postgresql](https://www.postgresql.org/download/) (P.S: Postgresql можно развернуть
  в докере).
* В Postgresql нужно создать базу данных. Как пример: "persons". Sql: CREATE DATABASE persons.
* В [application.yaml](testdata/src/main/resources/application.yaml) в строчке №4 введите ваш username для
  Postgresql, в строчке №5 введите ваш password для Postgresql.
* При запуске приложения Liquibase сам создаст таблицу persons.
* Модуль готов к использованию,
  запускаем [TestDataApplication](testdata/src/main/java/ru/clevertec/testdata/TestDataApplication.java).
* HTTP запросы: [persons.http](testdata/src/main/resources/http/persons.http).

***
