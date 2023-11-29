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
* HTTP запросы: [persons.http](session/src/main/resources/http/sessions.http).

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
* Добавить в application.yaml своего проекта:

 ````yaml
session:
  aware:
    enabled: true
````

* Если хотим очищать хранилище сессий, то в application.yaml нужно добавить `clean.enabled:true` и задать хронометраж
  очищения сессий `clean.cron: "00 00 00 * * *"`, означает что в 0 секунд 0 минут 0 часов каждого дня хранилище сессий
  будет очищено. Пример:

````yaml
session:
  aware:
    enabled: true
    clean:
      enabled: true
      cron: "00 00 00 * * *"
````

* Для работы стартера над методом нужно ставить
  аннотацию: [@SessionAware](starter/src/main/java/ru/clevertec/starter/annotation/SessionAware.java).
* Параметры метода должны содержать [Session](starter/src/main/java/ru/clevertec/starter/model/Session.java) для
  отслеживания сессий. Если не будет, то
  вылетит [SessionAwareException](starter/src/main/java/ru/clevertec/starter/exception/SessionAwareException.java) c
  сообщением `"Must be a Session object in the parameters"`.
* Так же параметры метода должны содержать объект, который содержит в себе поле String login. Можно использовать со
  стартера [Authorization](starter/src/main/java/ru/clevertec/starter/model/Authorization.java).
* Либо сделать свой. Объект должен быть либо рекордом, либо классом. И у него должны быть геттеры. Если геттеров не
  будет, то
  вылетит [SessionAwareException](starter/src/main/java/ru/clevertec/starter/exception/SessionAwareException.java) c
  сообщением `"The object must be class or record and have getters"`.
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

* BlackList можно задать и через класс. Можно использовать
  дефолтный [DefaultBlackListHandler](starter/src/main/java/ru/clevertec/starter/sevice/DefaultBlackListHandler.java).
* Либо сделать свой. Класс должен
  имплементировать [BlackListHandler](starter/src/main/java/ru/clevertec/starter/sevice/BlackListHandler.java) интерфейс
  и переопределить метод `Set<String> getBlackList();`. Пример аннотации с blackListHandlers:

````java
@SessionAware(blackListHandlers = DefaultBlackListHandler.class)
````

* И наконец blackList можно задать через application.yaml. Пример:

````yaml
session:
  aware:
    enabled: true
    black-list:
      - Helmut35
      - Sasha
    black-list-handlers:
      - ru.clevertec.starter.sevice.DefaultBlackListHandler
      - ru.clevertec.testdata.config.PersonBlackListHandler
````

* Все blackList-ы заданные разными путями объединяются в один HashSet и фильтрация идёт по нему.
* Пример кода с использованием:

````java

@RestController
@RequiredArgsConstructor
@RequestMapping("/persons")
public class PersonController {

    private final PersonService personService;

    @SessionAware(blackList = {"Ann", "Sasha"}, blackListHandlers = DefaultBlackListHandler.class)
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

* P.S. Если хотим использовать свой модуль для хранения сессий, то нужно создать свой бин SessionAwareService и передать
  в его конструктор свой RestClient. Пример:

````java

@Configuration
public class ExampleConfig {

    @Bean
    public SessionAwareService sessionAwareService() {
        RestClient restClient = RestClient.builder()
                .baseUrl("your session service url")
                .defaultHeaders(headers -> {
                    headers.setAccept(List.of(MediaType.APPLICATION_XML));
                    headers.setContentType(MediaType.APPLICATION_XML);
                })
                .build();
        return new SessionAwareService(restClient);
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
