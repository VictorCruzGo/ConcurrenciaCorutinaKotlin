package kotlinChannelBasico

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


fun main() {
    runBlocking {
        val channel = Channel<Int>()

        println("---Antes de enviar---")
        launch {
            (1..10).forEach {
                println("enviando..$it")
                channel.send(it)
            }
        }
        println("---Despues de enviar---")

        println("---Antes de recibir---")
        launch {
            /*NOTA: al enviar 10 se debe recibir 10, caso contrario el programa  sigue ejecutando*/
            repeat(10) {
                println("demo valor recibido: " + "${channel.receive()}")
            }
            println("demo finalizado la recepcion")
        }
        println("---Despues de recibir---")
    }
}