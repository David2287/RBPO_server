# Серверная часть проекта по предмету
## "Разработка безопасного программного обеспечения"

### Выполнил: Султанов Д.А.
### Группа: БКС2202

---

## Пример работы программы и способы запуска проекта

### Запуск проекта

Для запуска проекта необходимо настроить файл конфигурации `application.properties` следующим образом:


```bash 

spring.application.name= <nameofproject>

spring.datasource.url=jdbc:postgresql://localhost:<port>/<database name> 
spring.datasource.username= <name of user in database>
spring.datasource.password = <password in database>

```

---

Для корректной работы проекта требуется установить **_PostgreSQL_** через консоль **_Linux_** или использовать программное обеспечение **_Docker_** для более удобной работы с базой данных сервера.

## Установка базы данных с помощью Docker
Пример команды для установки базы данных в консоли _**Linux**_:

```bash

docker run --name <NameOfContainer> -p openport:portofcontainer -e POSTGRES_USER=<name> -e POSTGRES_PASSWORD=<password> -e POSTGRES_DB=mynewdb-d postgres

```

---

После успешной установки контейнера с базой данных необходимо проверить его работоспособность и возможность получения данных. Для этого можно воспользоваться следующей командой:
```bash 

ping <container_ip>

```

---

## Запуск проекта
После проверки работоспособности контейнера, необходимо запустить проект. Убедитесь, что он работает корректно и не содержит ошибок. Запуск проекта осуществляется с помощью файла **_application.properties_**.

При запуске программы в консоли компилятора будет выведено следующее описание:

```bash

  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/

 :: Spring Boot ::                (v3.3.5)
 
 2024-12-26T16:09:22.127+03:00  INFO 17800 --- [servak] [           main] r.mtuci.antivirus.AntivirusApplication   : Starting AntivirusApplication using Java 21.0.5 with PID 17800 (C:\Users\Admin\Documents\java_project\RBPO_Servak\servak\target\classes started by Admin in C:\Users\Admin\Documents\java_project\RBPO_Servak)
2024-12-26T16:09:22.129+03:00  INFO 17800 --- [servak] [           main] r.mtuci.antivirus.AntivirusApplication   : No active profile set, falling back to 1 default profile: "default"
2024-12-26T16:09:22.627+03:00  INFO 17800 --- [servak] [           main] .s.d.r.c.RepositoryConfigurationDelegate : Bootstrapping Spring Data JPA repositories in DEFAULT mode.
2024-12-26T16:09:22.677+03:00  INFO 17800 --- [servak] [           main] .s.d.r.c.RepositoryConfigurationDelegate : Finished Spring Data repository scanning in 46 ms. Found 7 JPA repository interfaces.
2024-12-26T16:09:23.083+03:00  INFO 17800 --- [servak] [           main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat initialized with port 8080 (http)
2024-12-26T16:09:23.095+03:00  INFO 17800 --- [servak] [           main] o.apache.catalina.core.StandardService   : Starting service [Tomcat]
2024-12-26T16:09:23.095+03:00  INFO 17800 --- [servak] [           main] o.apache.catalina.core.StandardEngine    : Starting Servlet engine: [Apache Tomcat/10.1.31]
2024-12-26T16:09:23.132+03:00  INFO 17800 --- [servak] [           main] o.a.c.c.C.[Tomcat].[localhost].[/]       : Initializing Spring embedded WebApplicationContext
2024-12-26T16:09:23.132+03:00  INFO 17800 --- [servak] [           main] w.s.c.ServletWebServerApplicationContext : Root WebApplicationContext: initialization completed in 972 ms
2024-12-26T16:09:23.238+03:00  INFO 17800 --- [servak] [           main] o.hibernate.jpa.internal.util.LogHelper  : HHH000204: Processing PersistenceUnitInfo [name: default]
2024-12-26T16:09:23.273+03:00  INFO 17800 --- [servak] [           main] org.hibernate.Version                    : HHH000412: Hibernate ORM core version 6.5.3.Final
2024-12-26T16:09:23.296+03:00  INFO 17800 --- [servak] [           main] o.h.c.internal.RegionFactoryInitiator    : HHH000026: Second-level cache disabled
2024-12-26T16:09:23.484+03:00  INFO 17800 --- [servak] [           main] o.s.o.j.p.SpringPersistenceUnitInfo      : No LoadTimeWeaver setup: ignoring JPA class transformer
2024-12-26T16:09:23.506+03:00  INFO 17800 --- [servak] [           main] com.zaxxer.hikari.HikariDataSource       : HikariPool-1 - Starting...
2024-12-26T16:09:23.621+03:00  INFO 17800 --- [servak] [           main] com.zaxxer.hikari.pool.HikariPool        : HikariPool-1 - Added connection org.postgresql.jdbc.PgConnection@72324965
2024-12-26T16:09:23.634+03:00  INFO 17800 --- [servak] [           main] com.zaxxer.hikari.HikariDataSource       : HikariPool-1 - Start completed.
2024-12-26T16:09:24.434+03:00  INFO 17800 --- [servak] [           main] o.h.e.t.j.p.i.JtaPlatformInitiator       : HHH000489: No JTA platform available (set 'hibernate.transaction.jta.platform' to enable JTA platform integration)
2024-12-26T16:09:24.565+03:00  INFO 17800 --- [servak] [           main] j.LocalContainerEntityManagerFactoryBean : Initialized JPA EntityManagerFactory for persistence unit 'default'
2024-12-26T16:09:25.046+03:00  INFO 17800 --- [servak] [           main] r$InitializeUserDetailsManagerConfigurer : Global AuthenticationManager configured with UserDetailsService bean with name userService
2024-12-26T16:09:25.451+03:00  INFO 17800 --- [servak] [           main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat started on port 8080 (http) with context path '/'
2024-12-26T16:09:25.457+03:00  INFO 17800 --- [servak] [           main] r.mtuci.antivirus.AntivirusApplication   : Started AntivirusApplication in 3.638 seconds (process running for 4.057)

```

Если база данных не будет запущена, в консоли появится следующее сообщение об ошибке:

```bash

2024-12-26T16:08:28.513+03:00 ERROR 7688 --- [servak] [           main] j.LocalContainerEntityManagerFactoryBean : Failed to initialize JPA EntityManagerFactory: Unable to create requested service [org.hibernate.engine.jdbc.env.spi.JdbcEnvironment] due to: Unable to determine Dialect without JDBC metadata (please set 'jakarta.persistence.jdbc.url' for common cases or 'hibernate.dialect' when a custom Dialect implementation must be provided)
2024-12-26T16:08:28.513+03:00 ERROR 7688 --- [servak] [           main] o.s.b.web.embedded.tomcat.TomcatStarter  : Error starting Tomcat context. Exception: org.springframework.beans.factory.UnsatisfiedDependencyException. Message: Error creating bean with name 'jwtRequestFilter' defined in file [C:\Users\Admin\Documents\java_project\RBPO_Servak\servak\target\classes\ru\mtuci\antivirus\utils\JwtRequestFilter.class]: Unsatisfied dependency expressed through constructor parameter 1: Error creating bean with name 'userService' defined in file [C:\Users\Admin\Documents\java_project\RBPO_Servak\servak\target\classes\ru\mtuci\antivirus\services\UserService.class]: Unsatisfied dependency expressed through constructor parameter 0: Error creating bean with name 'userRepository' defined in ru.mtuci.antivirus.repositories.UserRepository defined in @EnableJpaRepositories declared on JpaRepositoriesRegistrar.EnableJpaRepositoriesConfiguration: Cannot resolve reference to bean 'jpaSharedEM_entityManagerFactory' while setting bean property 'entityManager'
2024-12-26T16:08:28.529+03:00  INFO 7688 --- [servak] [           main] o.apache.catalina.core.StandardService   : Stopping service [Tomcat]
2024-12-26T16:08:28.531+03:00  WARN 7688 --- [servak] [           main] ConfigServletWebServerApplicationContext : Exception encountered during context initialization - cancelling refresh attempt: org.springframework.context.ApplicationContextException: Unable to start web server
2024-12-26T16:08:28.537+03:00  INFO 7688 --- [servak] [           main] .s.b.a.l.ConditionEvaluationReportLogger : 

Error starting ApplicationContext. To display the condition evaluation report re-run your application with 'debug' enabled.
2024-12-26T16:08:28.548+03:00 ERROR 7688 --- [servak] [           main] o.s.boot.SpringApplication               : Application run failed

```

---

## Управление сервером
После успешного запуска программы и её соединения с базой данных, для управления сервером потребуется программное обеспечение Postman.

1. Создайте новый запрос (New Request), где будет происходить управление сервером.

2. Проверьте подключение, используя порт, указанный при запуске программы. В данном случае сервер доступен по следующему адресу:
```bash

localhost:8080/

```

На основе данного подключения можно выполнять различные операции с сервером, такие как: регистрация, авторизация и тестирование.