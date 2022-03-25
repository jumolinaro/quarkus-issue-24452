package quarkus

import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class GreetingService {

    fun getGreetingMessage() = GreetingMessage("Hello", "Hello Avro")

}