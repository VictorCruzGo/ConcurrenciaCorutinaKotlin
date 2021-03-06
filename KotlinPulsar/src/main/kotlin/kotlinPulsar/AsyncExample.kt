package kotlinPulsar

import java.util.*
import org.apache.pulsar.client.api.Producer
import org.apache.pulsar.client.api.PulsarClient
import org.apache.pulsar.client.api.Schema
import org.apache.pulsar.client.api.SubscriptionType

fun asyncExample(client: PulsarClient) {
    val producer = client.newProducer<String>(Schema.STRING)
        .topic("FMRecepcionVentas")
        .create()

    println("PRODUCING THREADS STARTING")
    for (i in 1..10) {
        Thread(ProduceMessages(producer)).start()
    }
    println("PRODUCING THREADS STARTED")

    println("CONSUMER THREADS STARTING")
    for (i in 1..10) {
        Thread(CreateConsumers(client, "Subscription-$i")).start()
    }
    println("CONSUMER THREADS STARTED")
}

class ProduceMessages(private val producer: Producer<String>) : Runnable {
    public override fun run() {
        while (true) {
            val sleepTime = (1..1000).shuffled().first().toLong()
            val msgId = UUID.randomUUID()
            Thread.sleep(sleepTime)
            println("Enqueuing to send $msgId")
            producer.sendAsync("MSG VIC Message sent from ${Thread.currentThread()} id $msgId").thenAccept { pulsarMsgId ->
                println("Sent $msgId | $pulsarMsgId")
            }
        }
    }
}

class CreateConsumers(private val client: PulsarClient, private val subscriptionName: String) : Runnable {
    public override fun run() {
        println("SETTING UP CONSUMER $subscriptionName")
        val consumer = client.newConsumer()
                .topic("FMRecepcionVentas")
                .subscriptionName(subscriptionName)
                .subscriptionType(SubscriptionType.Exclusive)
                .subscribe()

        println("STARTING CONSUME LOOP")
        while (true) {
            // Wait for a message
            val msg = consumer.receive()

            try {
                // Do something with the message
                println("CON VIC Consuming from ${Thread.currentThread()} Subscription $subscriptionName Received: ${String(msg.data)}")

                // Acknowledge the message so that it can be deleted by the message broker
                consumer.acknowledge(msg)
            } catch (e: Exception) {
                println(e)
                // Message failed to process, redeliver later
                consumer.negativeAcknowledge(msg)
            }
        }
    }
}
