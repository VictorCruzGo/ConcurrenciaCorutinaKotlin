package kotlinOfferPoll

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main(){
    /*NOTA: En el canal ilimitado considerar la memoria. OutOfMemoryError*/
    val channel = Channel<Int>(Channel.UNLIMITED)

    runBlocking {
        //Coroutina 1
        launch {
            repeat(100){
                println("Enviar $it")
                channel.send(it)
            }
            println("Se enviaron todos los elementos")
        }

        //Coroutina 2
        launch{
            repeat(100){
                println("Se ha recibido  ${channel.receive()}")
            }
            println("finalizo la recepcion")
        }

    }
}