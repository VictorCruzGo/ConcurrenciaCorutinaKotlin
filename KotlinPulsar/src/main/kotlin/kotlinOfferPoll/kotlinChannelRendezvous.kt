package kotlinOfferPoll

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main(){
    /*NOTA: Con Channerl Rendezvous, siempre se envia y recibe la misma cantidad, caso contrario el programa sigue funcionando*/
    val channel = Channel<Int>(Channel.RENDEZVOUS)

    runBlocking {
        //Coroutina 1

        launch {
            println("Enviar 0 y suspender")
            channel.send(0)
            println("Enviar 1 y complir con un recibido inmediatamente")
            channel.send(1)
            //channel.send(2)
        }

        //Coroutina 2
        launch{
            println("invocar a receive()")
            val recibir0=channel.receive()
            println("recibir 0 inmediatamente")
            println("invocar a receive()")
            val recibir1=channel.receive()
            println("recibir 1 inmediatamente")
            println("receptor finalizado")
            //val recibir2=channel.receive()
        }

    }
}