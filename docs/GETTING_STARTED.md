# Getting Started

Prequisites:
* Maven
* Git 
* Java 8

## Import into your project

Please check the [example pom](../trampoline-example-app/pom.xml).

As you can see you require the trampoline starter dependency. This dependency adds all of the trampoline starters.

Trampoline is built upon `Spring Boot 2.1.3-RELEASE`

```xml
<dependency>
       <groupId>com.lucadev.trampoline</groupId>
       <artifactId>trampoline-starter</artifactId>
       <version>20190308</version>
</dependency>
```

### trampoline-starter

The `trampoline-starter` dependency wraps all the starter modules into a single dependency.

You may also add the separately or could exclude unwanted dependencies from the starter.

## Enable Trampoline

To enable trampoline services and components in your applications please add the `@EnableTrampoline` annotation. This will scan and load all trampoline components.
This annotation is inside the `trampoline-common` dependency.

Example:

```java
@EnableTrampoline
@SpringBootApplication
public class TrampolineApplication {

    public static void main(String[] args) {
        SpringApplication.run(TrampolineApplication.class, args);
    }

}
```

## Configuration requirements

As of spring boot 2.1.0-RELEASE bean overriding is disabled by default. Trampoline depends on this feature so please configure the following in your application properties:
```
spring.main.allow-bean-definition-overriding=true
```

## Write your app with the help of Trampoline

For an example on how to use Trampoline in your application please check the [trampoline-example-app](../trampoline-example-app) and read the other docs.