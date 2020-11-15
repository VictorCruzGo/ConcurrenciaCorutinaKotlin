package kotlinChannel

import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main(){
    runBlocking {
        val channel = Channel<String>()
        launch { sendString(channel, "foo", 200L) }
        launch { sendString(channel, "bar", 500L) }

        repeat(5){
            println(channel.receive())
        }
        coroutineContext.cancelChildren()
    }
}

suspend fun sendString(channel: SendChannel<String>, s:String, time:Long){
    while(true){
        delay(time)
        channel.send(s)
    }
}