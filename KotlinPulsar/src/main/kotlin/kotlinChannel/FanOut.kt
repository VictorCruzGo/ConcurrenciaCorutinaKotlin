package kotlinChannel

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


fun main(){
    runBlocking {
        val productor=producirNumeros()
        repeat(5){
            println("numero it:"+it)
            lauchProcesar(it, productor)
        }
        delay(950)
        productor.cancel()

    }
}

fun CoroutineScope.producirNumeros()=produce <Int>{
    var x=1
    while (true)
    {
        send(x++)
        delay(100)
    }
}

fun CoroutineScope.lauchProcesar(id:Int, channel:ReceiveChannel<Int>) = launch {
    for(msg in channel){
        println("Proceso $id msg $msg ")
    }
}


