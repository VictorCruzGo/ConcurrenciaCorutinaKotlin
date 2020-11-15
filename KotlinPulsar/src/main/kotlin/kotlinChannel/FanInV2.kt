package kotlinChannel

import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.channels.consume
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main() {
    runBlocking {
        val channel = Channel<String>(Channel.UNLIMITED)
        launch {
        for (c in 1..10) {
            //launch {
                println("enviando $c")
                //enviarString(channel, "victor $c")
            channel.send("victor $c")
            //}
            delay(1000)
        }
            channel.close()
        }

        println("------antes del repeat------")
        launch {
            //Continua la ejecucion porque el canal esta abierto
            for(msg in channel){
                println(msg)
            }

            //Para la ejecucion por que el canal esta vacio
            //while(!channel.isEmpty) {
              //  println(channel.receive())
            //}
            println("Cancelando Childrens")
            //coroutineContext.cancelChildren()
            println("Cancelando Cerrando")
            //channel.close()
        }
    }
}

suspend fun enviarString(channel: SendChannel<String>, s: String) {
    channel.send(s)
}