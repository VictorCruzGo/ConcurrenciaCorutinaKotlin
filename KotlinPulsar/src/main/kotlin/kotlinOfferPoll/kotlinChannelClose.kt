package kotlinOfferPoll

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main(){
    val channel = Channel<Int>(Channel.BUFFERED)

    runBlocking {
        launch {
            // isClosedForSend: false, isClosedForReceive: false.
            channel.send(0)
            channel.send(1)
            channel.send(2)
            channel.close()
            //channel.send(3) //Excepcion, el channel fue cerrado
            // isClosedForSend: true, isClosedForReceive: false.
        }

        launch{
            // Because the `channel.close()` is called in Coroutine#1.
            // isClosedForSend: true, isClosedForReceive: false.
            println("demo ${channel.receive()}")
            println("demo ${channel.receive()}")
            /*NOTA: Genera una excepcion si el canal fue cerrado*/
            //println("demo ${channel.receive()}")
            // Because all the elements are received.
            // isClosedForSend: true, isClosedForReceive: true.
        }
    }
}