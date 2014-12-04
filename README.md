<pre>
   ____   _       _                                          
  / __ \ (_) ___ | |__  _ __  _ __  _   ___      _____ _   _ 
 / / _` || |/ _ \| '_ \| '_ \| '_ \| | | \ \ /\ / / _ \ | | |
| | (_| || | (_) | | | | | | | | | | |_| |\ V  V /  __/ |_| |
 \ \__,_|/ |\___/|_| |_|_| |_|_| |_|\__, | \_/\_/ \___|\__, |
  \____/__/                         |___/              |___/ 

</pre>
# Building Awesome APIs Using Ratpack and Java 8
## Intro
---
Hi, I'm Johnny Wey.

* [Twitter](https://twitter.com/johnnywey)
* [Github](https://github.com/johnnywey)
* johnnywey@gmail.com

## Outline
---
* Project Organization
* Gradle
* Spring Loaded
* Switch to Java (remove ratpack.groovy)
* Testing
* Persistance
* JSON
* Authentication
* Code coverage


## Project Organization
---
So, let's start creating a Ratpack application.

```git checkout step-1```

```git clean -d -f```

[Explain gvm]

```gvm install lazybones```

```lazybones list```

[Notice the `ratpack` and `ratpack-lite` project templates]

```lazybones create ratpack api```

[Notice the new directory structure in /api]

## Built with Gradle
---
Ratpack is built on Gradle (makes a lot of sense since Luke Daley is the lead contributor). The lazybones template we installed includes an instance of the *Gradle wrapper* which allows us to use Gradle without installing locally on our machine.

```git checkout step-2```

```git clean -d -f```

```gradlew tasks```

[Observe Ratpack specific tasks]

```gradlew idea```

[Open project in IntelliJ]

Run project:

```gradlew run```

[Open http://localhost:5050 and observe skeleton app]

## Spring Loaded
---
If you're anything like me, you've gotten used to frameworks like Grails and Rails automatically reloading when changes are made to the application. This is a huge time-saver.

Ratpack utilizes [Spring Loaded](https://github.com/spring-projects/spring-loaded), which is the same system Grails uses, to watch and reload files when they change.

[Demo of things changing with Spring Loaded]

## Switch to Java
---
While I love Groovy, in this demo, we're going to re-tool to use only Java 8 (we're still using Groovy and Spock for unit tests, though).

```git checkout step-3```

```gradlew idea```

You'll notice that the `ratpack.groovy` file is now gone. This removes the Groovy syntactic sugar on top of Ratpack.

Ratpack uses a `HandlerFactory`. This defines the various `Handlers` and how they interact. Ratpack's `Handlers` are all optimized for Java 8 lambdas so you don't need to worry about create anonymous inner classes.

[Show update to ratpack.properties]

[Show RatpackApiHandler.java]

```gradlew dev```

[Gradle watch plugin]

[Show new Spring Loaded config via watch and the updates to `build.gradle`]

## Testing
---
Ratpack has both unit and functional testing. Since a `Handler` is the standard contract, we can execute unit tests against that interface.

```git checkout step-4```

To test the `Handler`, I've split it out of the `HandlerFactory`. This allows me to target testing to only the specific functions the `Handler` performs.

I've chosen to use [Spock](https://code.google.com/p/spock/) as the unit testing framework.

[Demo unit tests]

Note that the `UnitTest.handle()` method is lambda aware but we need to use a Groovy closure to pass it in (this is a Groovyism in the way Groovy supports Java 8 lambdas).

[Demo debugging in IntelliJ]

```gradlew test```

[Show test reports]

## Persistance
---
We're going to use [MongoDB](http://www.mongodb.org) as our persistance store. There are a bunch of ways we can get data in and out of MongoDB using Java. A couple ways I've done this in the past:

* [MongoDB Driver](https://github.com/mongodb/mongo-java-driver/releases)
* [Spring Data MongoDB](http://projects.spring.io/spring-data-mongodb/)
* [Gmongo](https://github.com/poiati/gmongo) if you can use Groovy (don't forget `@CompileStatic`)

For this demo, we're goint to use a project called [Morphium](http://sboesebeck.github.io/morphium/)

```git checkout step-5```

```gradlew idea```

[Show new services and tests]

[Show Bootstrap]

[Show new HandlerFactory]

## Adding JSON
---
Now that we have persistence wired through, we need to be able to render the result out through the API. For this, we're going to use the Ratpack Jackson module.

In order to take advantage of the JacksonModule, we need to introduce Google Guice. Guice is Ratpack's framework of choice for dependency injection.

Don't worry; you don't have to know Guice much at all to use Ratpack!

```git checkout step-6```

```gradlew idea```

```gradlew dev```

[Show new updates to use Jackson and Guice in the ApiHandler]

[Demonstrate finding all users]

This is all well and good but we're returning parts of the user object that don't work. For example, we have the password there. We also want the `String` version of an `ObjectId` and not the Jackson produced version.

How can we customize this?

```git checkout step-7```

```gradlew dev```

[Show `User.java` with updated attribution]

## Adding Auth
---
Ratpack has a session module that supports different types of back-end storage. The default is an in-memory map.

However, ratpack's session support is still pretty limited and I want a bit more flexibilty. As such, I added a handler that will support authentication via a cookie.

It's also interesting to note that Ratpack's auth support is relatively limited right now. However, as it's not difficult to create modules (as we've seen), I imagine this is a temporary situation.

Let's take a look at one approach that would work for this example.

```git checkout step-8```

```gradlew idea```

[Show gate to api]

[Show new handlers for login and logout]

[Show how handlers are wired]

[Show example of login, get users (/api/user), logout (/logout) and a 403 when getting users again]

## Code Coverage
---
We're going to switch gears for a bit and talk about test coverage. Our example is using Java 8. Unfortunately, Cobertura has issues with Java 8 and either fails to create coverage or, when failures are overriden, causes coverge gaps when it encounters lambdas.

Hopefully, this will be fixed soon as I really like Cobertura. Until then, [Jacoco](http://www.eclemma.org/jacoco/) works very well.

There is a [Gradle plugin](http://www.gradle.org/docs/current/userguide/jacoco_plugin.html) that also does the job.

```git checkout step-9```

[Demo `build.gradle` changes]

[gradlew cover -> show html test report]