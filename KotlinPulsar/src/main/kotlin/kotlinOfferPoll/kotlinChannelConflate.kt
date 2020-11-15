package kotlinOfferPoll

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main(){
    /*NOTA: Siempre recibe el ultimo valor*/
    val channel = Channel<Int>(Channel.CONFLATED)

    runBlocking {
        //Coroutina 1
        launch {
            repeat(3){
                println("Enviar $it")
                channel.send(it)
            }
            println("Se enviaron todos los elementos")
        }

        //Coroutina 2
        launch{
            println("recibir ${channel.receive()}")
            println("finalizo la recepcion")
        }

    }
}