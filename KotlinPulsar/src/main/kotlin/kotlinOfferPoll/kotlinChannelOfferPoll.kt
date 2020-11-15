package kotlinOfferPoll

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main() {
    /*NOTA: Offer y Poll no es necesario que este dentro de un Runblocking*/
    runBlocking {
        val channel = Channel<Int>(Channel.UNLIMITED)

        println("---Antes de enviar---")
        launch {
            (1..10000).forEach {
                println("enviando..$it")
                channel.offer(it)
            }
        }

        println("---Despues de enviar---")

        println("---Antes de recibir---")
        /*NOTA: al enviar 10 se debe recibir 10, caso contrario el programa  sigue ejecutando*/
        launch {
            repeat(10000) {
                println("demo valor recibido: " + "${channel.poll()}")
            }
            println("demo finalizado la recepcion")
        }

        println("---Despues de recibir---")
    }
}