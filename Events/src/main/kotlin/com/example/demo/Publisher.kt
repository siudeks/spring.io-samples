package com.example.demo

import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.CountDownLatch
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import reactor.bus.Event
import reactor.bus.EventBus

/**
 * The code uses a for loop to publish a fixed number of events. An AtomicInteger is used to fashion a unique number,
 * which gets turned into a Reactor event with Event.wrap(). The event is published to the quotes channel
 * using reactor.notify().
 */
@Service
class Publisher {

    @Autowired
    internal var eventBus: EventBus? = null

    @Autowired
    internal var latch: CountDownLatch? = null

    @Throws(InterruptedException::class)
    fun publishQuotes(numberOfQuotes: Int) {
        val start = System.currentTimeMillis()

        val counter = AtomicInteger(1)

        for (i in 0..numberOfQuotes - 1) {
            eventBus!!.notify("quotes", Event.wrap(counter.getAndIncrement()))
        }

        latch!!.await()

        val elapsed = System.currentTimeMillis() - start

        println("Elapsed time: " + elapsed + "ms")
        println("Average time per quote: " + elapsed / numberOfQuotes + "ms")
    }
}