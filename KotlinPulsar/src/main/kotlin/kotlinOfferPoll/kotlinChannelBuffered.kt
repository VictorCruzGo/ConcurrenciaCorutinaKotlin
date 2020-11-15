package kotlinOfferPoll

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main(){
    /*NOTA: Con Channerl Rendezvous, siempre se envia y recibe la misma cantidad, caso contrario el programa sigue funcionando*/
    val channel = Channel<Int>(2)

    runBlocking {
        //Coroutina 1
        launch {
            repeat(5){
                println("Enviar $it")
                channel.send(it)
            }
            println("Se enviaron todos los elementos")
        }

        //Coroutina 2
        launch{
            repeat(5){
                println("Se ha recibido  ${channel.receive()}")
            }
            println("finalizo la recepcion")
        }

    }
}