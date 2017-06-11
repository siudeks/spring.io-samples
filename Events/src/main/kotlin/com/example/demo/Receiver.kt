package com.example.demo

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import reactor.bus.Event
import reactor.fn.Consumer

import java.util.concurrent.CountDownLatch

@Service
internal class Receiver : Consumer<Event<Int>> {

    @Autowired
    var latch: CountDownLatch? = null

    var restTemplate = RestTemplate()

    override fun accept(ev: Event<Int>) {
        val quoteResource = restTemplate.getForObject("http://gturnquist-quoters.cfapps.io/api/random", QuoteResource::class.java)
        println("Quote " + ev.data + ": " + quoteResource.value?.quote)
        latch!!.countDown()
    }

}