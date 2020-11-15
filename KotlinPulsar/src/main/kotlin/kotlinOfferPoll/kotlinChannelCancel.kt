package kotlinOfferPoll

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main(){
    val channel = Channel<Int>(Channel.BUFFERED)

    runBlocking {
        //Coroutina 1

        launch {
            println("Cancelando")
            channel.cancel()
        }

        //Coroutina 2
        launch{
            try{
                println("Enviando")
                channel.send(0)
            }
            catch (e: CancellationException){
                println("Coroutina 2 - Se cancelo..."+throw CancellationException())
            }
        }

        //Coroutina 3
        launch{
            try{
                println("Recibiendo")
                channel.receive()
            }
            catch (e: CancellationException){
                println("Coroutina 3 - Se cancelo..."+throw CancellationException())
            }
        }

    }
}