package kotlinOfferPoll

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main(){
    val channel = Channel<Int>(Channel.BUFFERED)

    runBlocking {
        launch {
            repeat(1000){
                println("Valor enviado: $it")
                channel.send(it)
            }
            channel.close()
        }

        /*NOTA: for loop o Channel.consumeEach para asegurarnos de que todos los elementos se bloquen sin bloquear la aplicacion*/
        launch{
            for(it in channel){
                println("Valor recibido: $it")
            }
        }
    }
}