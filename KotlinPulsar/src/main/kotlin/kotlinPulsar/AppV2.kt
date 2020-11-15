package kotlinPulsar

import org.apache.pulsar.client.api.Producer
import org.apache.pulsar.client.api.PulsarClient

fun main(args: Array<String>) {
    val client = PulsarClient.builder()
            .serviceUrl("pulsar://10.1.17.52:6650")
            .build()

    val producer: Producer<ByteArray> = client.newProducer()
            //.topic("pruebas_facturas")
            .topic("FMRecepcionVentasPersistencia")
            .create()

// You can then send messages to the broker and topic you specified:
    // You can then send messages to the broker and topic you specified:
    for (i in 1 .. 100000){
        println("Enviando mensajes")
        producer.send("Hola victor cruz".toByteArray())

        println("Mensaje enviado")
    }

    producer.close()
}
