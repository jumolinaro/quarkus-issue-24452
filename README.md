# Quarkus issue 24452 Project

This project provides source code to reproduce Quarkus issue 24452 (https://github.com/quarkusio/quarkus/issues/24452)

## Overview 

We have a Quarkus application with Java and Kotlin sources.
This component contains an avro file used to produce messages for Kafka

Gradle build of this application works perfectly with Quarkus 2.6 (2.6.3.Final)
But after migration to Quarkus 2.7 (2.7.5.Final), gradle build fails with some dependencies injection issues.

It seems that some classes generated from Kotlin sources are not found by @Inject annotation used in classes generated from Java sources.

## Content

This project contains
- `src/main/avro/greeting_message.avsc` avro file to generate `GreetingMessage` POJO class
- `src/main/kotlin/quarkus/GreetingService.kt` kotlin file to implement an application scoped bean using `GreetingMessage` class
- `src/main/java/quarkus/GreetingResource.java` java file to implement a REST resource using `GreetingService` with `@Inject` CDI annotation

## Build the application with Quarkus 2.7.5.Final (issue 24452 is reproduced)

Set Quarkus version to 2.7.5.Final in `gradle.properties` file:
```shell script
quarkusPluginVersion=2.7.5.Final
quarkusPlatformVersion=2.7.5.Final
```

Build the application using:
```shell script
./gradlew build
```

Result:
```shell script
> Task :quarkusGenerateCode
preparing quarkus application

> Task :quarkusGenerateCodeTests
preparing quarkus application

> Task :quarkusBuild FAILED
building quarkus jar

FAILURE: Build failed with an exception.

* What went wrong:
Execution failed for task ':quarkusBuild'.
> io.quarkus.builder.BuildException: Build failure: Build failed due to errors
        [error]: Build step io.quarkus.arc.deployment.ArcProcessor#validate threw an exception: javax.enterprise.inject.spi.DeploymentException: javax.enterprise.inject.UnsatisfiedResolutionException: Unsatisfied dependency for type quarkus.GreetingService and qualifiers [@Default]
        - java member: quarkus.GreetingResource#greetingService
        - declared on CLASS bean [types=[quarkus.GreetingResource, java.lang.Object], qualifiers=[@Default, @Any], target=quarkus.GreetingResource]
        at io.quarkus.arc.processor.BeanDeployment.processErrors(BeanDeployment.java:1202)
        at io.quarkus.arc.processor.BeanDeployment.init(BeanDeployment.java:272)
        at io.quarkus.arc.processor.BeanProcessor.initialize(BeanProcessor.java:134)
        at io.quarkus.arc.deployment.ArcProcessor.validate(ArcProcessor.java:462)
        at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
        at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)
        at java.base/jdk.internal.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
        at java.base/java.lang.reflect.Method.invoke(Method.java:566)
        at io.quarkus.deployment.ExtensionLoader$2.execute(ExtensionLoader.java:882)
        at io.quarkus.builder.BuildContext.run(BuildContext.java:277)
        at org.jboss.threads.ContextHandler$1.runWith(ContextHandler.java:18)
        at org.jboss.threads.EnhancedQueueExecutor$Task.run(EnhancedQueueExecutor.java:2449)
        at org.jboss.threads.EnhancedQueueExecutor$ThreadBody.run(EnhancedQueueExecutor.java:1478)
        at java.base/java.lang.Thread.run(Thread.java:834)
        at org.jboss.threads.JBossThread.run(JBossThread.java:501)
  Caused by: javax.enterprise.inject.UnsatisfiedResolutionException: Unsatisfied dependency for type quarkus.GreetingService and qualifiers [@Default]
        - java member: quarkus.GreetingResource#greetingService
        - declared on CLASS bean [types=[quarkus.GreetingResource, java.lang.Object], qualifiers=[@Default, @Any], target=quarkus.GreetingResource]
        at io.quarkus.arc.processor.Beans.resolveInjectionPoint(Beans.java:428)
        at io.quarkus.arc.processor.BeanInfo.init(BeanInfo.java:524)
        at io.quarkus.arc.processor.BeanDeployment.init(BeanDeployment.java:260)
        ... 13 more


* Try:
> Run with --stacktrace option to get the stack trace.
> Run with --info or --debug option to get more log output.
> Run with --scan to get full insights.

* Get more help at https://help.gradle.org

BUILD FAILED in 6s
```

## Build the application with Quarkus 2.6.3.Final (no issue 24452)

Set Quarkus version to 2.6.3.Final in `gradle.properties` file:
```shell script
quarkusPluginVersion=2.6.3.Final
quarkusPlatformVersion=2.6.3.Final
```

Build the application using:
```shell script
./gradlew build
```

Result:
```shell script
> Task :quarkusGenerateCode
preparing quarkus application

> Task :quarkusGenerateCodeTests
preparing quarkus application

> Task :quarkusBuild
building quarkus jar

BUILD SUCCESSFUL in 6s
7 actionable tasks: 7 executed
```

Run the application in dev mode using:
```shell script
./gradlew quarkusDev
```

NOTE: the application is available at http://localhost:8080/hello.


