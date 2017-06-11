package com.example.demo

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import reactor.Environment
import reactor.bus.Event
import reactor.bus.EventBus

import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

import reactor.bus.selector.Selectors.`$`
import reactor.fn.Consumer

/**
 * README https://spring.io/guides/gs/messaging-reactor/
 */
@Configuration
@EnableAutoConfiguration
@ComponentScan
class Application : CommandLineRunner {

    @Bean
    internal fun env(): Environment {
        return Environment.initializeIfEmpty()
                .assignErrorJournal()
    }

    @Bean
    internal fun createEventBus(env: Environment): EventBus {
        return EventBus.create(env, Environment.THREAD_POOL)
    }

    @Autowired
    private val eventBus: EventBus? = null

    @Autowired
    private val receiver: Receiver? = null

    @Autowired
    private val publisher: Publisher? = null

    @Bean
    fun latch(): CountDownLatch {
        return CountDownLatch(NUMBER_OF_QUOTES)
    }

    @Throws(Exception::class)
    override fun run(vararg args: String) {
        eventBus!!.on<Event<*>>(`$`<String>("quotes"), receiver!! as Consumer<Event<*>>)
        publisher!!.publishQuotes(NUMBER_OF_QUOTES)
    }

    companion object {

        private val NUMBER_OF_QUOTES = 10

        @Throws(InterruptedException::class)
        @JvmStatic fun main(args: Array<String>) {
            val app = SpringApplication.run(Application::class.java, *args)

            app.getBean(CountDownLatch::class.java).await(1, TimeUnit.SECONDS)

            app.getBean(Environment::class.java).shutdown()
        }
    }

}
