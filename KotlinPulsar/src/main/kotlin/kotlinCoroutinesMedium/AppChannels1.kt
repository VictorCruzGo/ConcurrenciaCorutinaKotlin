package kotlinCoroutinesMedium

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.util.*
import java.util.concurrent.ConcurrentLinkedQueue

//1
/*
-Este ejempo se bloquea
-Se uso ListkedList que no es thread-safe. No es una estructura de datos para ser accedida de manera concurrente.
*/
/*
fun main() {
    println("Start")

    val queue = LinkedList<Int>()
    var closed = false

    var produced = 0
    var consumed = 0

    val amounts = IntArray(5)

    runBlocking {
        launch(Dispatchers.Default) {
            val producers = List(100_000) {
                launch {
                    val number = (1..100).random()
                    if(queue.offer(number)) {
                        produced++
                    }
                }
            }

            producers.forEach { it.join() }
            closed = true
            println("Producers finished!")
        }

        launch(Dispatchers.Default) {
            val consumers = List(amounts.size) {
                launch {
                    while(!closed || !queue.isEmpty()) {
                        if (!queue.isEmpty()) {
                            val number = queue.poll()
                            if(number != null) {
                                consumed++
                                amounts[it]++
                            }
                        }
                    }
                }
            }

            consumers.forEach { it.join() }
            println("Consumers finished!")
        }
    }

    println("Queue size: ${queue.size}")
    println("Produced: $produced")
    println("Consumed: $consumed")

    var total = 0
    println("----- AMOUNTS ------")
    amounts.forEachIndexed { index, amount ->
        total += amount
        println("Index #$index: $amount")
    }
    println("--------------------")
    println("TOTAL = $total")
    println("--------------------")

    println("End")
}*/


//2
/*
-Se utilizara ConcurrenteLinkedQueque envez de LinkedList
*/
/*
fun main() {
    println("Start")

    val queue = ConcurrentLinkedQueue<Int>()
    var closed = false

    var produced = 0
    var consumed = 0

    val amounts = IntArray(5)

    runBlocking {
        launch(Dispatchers.Default) {
            val producers = List(100_000) {
                launch {
                    val number = (1..100).random()
                    if(queue.offer(number)) {
                        produced++
                    }
                }
            }

            producers.forEach { it.join() }
            closed = true
            println("Producers finished!")
        }

        launch(Dispatchers.Default) {
            val consumers = List(amounts.size) {
                launch {
                    while(!closed || !queue.isEmpty()) {
                        if (!queue.isEmpty()) {
                            val number = queue.poll()
                            if(number != null) {
                                consumed++
                                amounts[it]++
                            }
                        }
                    }
                }
            }

            consumers.forEach { it.join() }
            println("Consumers finished!")
        }
    }

    println("Queue size: ${queue.size}")
    println("Produced: $produced")
    println("Consumed: $consumed")

    var total = 0
    println("----- AMOUNTS ------")
    amounts.forEachIndexed { index, amount ->
        total += amount
        println("Index #$index: $amount")
    }
    println("--------------------")
    println("TOTAL = $total")
    println("--------------------")

    println("End")
}
*/

//3
/*
-Utilizando mutex para la sincronizacion de variables
-Al usar mutex se crean cuellos de botella.
-Ej. una autopista con 6 carriles. en el peaje solo hay un carril.
*/
/*
fun main() {
    println("Start")

    val mutex = Mutex()

    val queue = ConcurrentLinkedQueue<Int>()
    var closed = false

    var produced = 0
    var consumed = 0

    val amounts = IntArray(5)

    runBlocking {
        launch(Dispatchers.Default) {
            val producers = List(100_000) {
                launch {
                    val number = (1..100).random()
                    if(queue.offer(number)) {
                        //Se crean cuellos de botell
                        mutex.withLock {
                            produced++
                        }
                    }
                }
            }

            producers.forEach { it.join() }
            closed = true
            println("Producers finished!")
        }

        launch(Dispatchers.Default) {
            val consumers = List(amounts.size) {
                launch {
                    while(!closed || !queue.isEmpty()) {
                        if (!queue.isEmpty()) {
                            val number = queue.poll()
                            if(number != null) {
                                //Se crean cuellos de botell
                                mutex.withLock {
                                    consumed++
                                    amounts[it]++
                                }
                            }
                        }
                    }
                }
            }

            consumers.forEach { it.join() }
            println("Consumers finished!")
        }
    }

    println("Queue size: ${queue.size}")
    println("Produced: $produced")
    println("Consumed: $consumed")

    var total = 0
    println("----- AMOUNTS ------")
    amounts.forEachIndexed { index, amount ->
        total += amount
        println("Index #$index: $amount")
    }
    println("--------------------")
    println("TOTAL = $total")
    println("--------------------")

    println("End")
}
*/


//4
/*
Channels  y Muttex
-La ventaja de usar Channel es que el control de la lectura y escritura ya esta controlado y soportado dentro de su estructura.
-Un channel es un thread-safe
*/
/*
fun main() {
    println("Start")

    val mutex = Mutex()

    val channel = Channel<Int>()

    var produced = 0
    var consumed = 0

    val amounts = IntArray(5)

    runBlocking {
        launch(Dispatchers.Default) {
            val producers = List(100_000) {
                launch {
                    val number = (1..100).random()
                    channel.send(number)
                    mutex.withLock {
                        produced++
                    }
                }
            }

            producers.forEach { it.join() }
            channel.close()
            println("Producers finished!")
        }

        launch(Dispatchers.Default) {
            val consumers = List(amounts.size) {
                launch {
                    for(i in channel) {
                        mutex.withLock {
                            consumed++
                            amounts[it]++
                        }
                    }
                }
            }

            consumers.forEach { it.join() }
            println("Consumers finished!")
        }
    }

    println("Produced: $produced")
    println("Consumed: $consumed")

    var total = 0
    println("----- AMOUNTS ------")
    amounts.forEachIndexed { index, amount ->
        total += amount
        println("Index #$index: $amount")
    }
    println("--------------------")
    println("TOTAL = $total")
    println("--------------------")

    println("End")
}
*/

//5
/*
Uso de clase Sender y Recibe
*/
fun main() {
    println("Start")

    val channel = Channel<Int>()
    val sendersAmount = 100
    val receiversAmount = 5

    val amounts = IntArray(receiversAmount)

    runBlocking {
        launch {
            val senders = List(sendersAmount) {
                val sender = Sender()
                launch {
                    println("Enviando datos..")
                    sender.processAction(channel)
                }
            }

            senders.forEach { it.join() }
            println("hilos en la lista senders: "+senders.size)
            channel.close()
            println("Senders finished!")
        }

        launch {
            val receivers = List(receiversAmount) {
                val receiver = Receiver()
                launch {
                    println("Recibiendo datos..")
                    receiver.processAction(channel)
                    amounts[it] = receiver.messagesAmount
                }
            }

            receivers.forEach { it.join() }
            println("hilos en la lista receivers: "+receivers.size)
            println("Receivers finished!")
        }

    }

    var total = 0
    println("----- AMOUNTS ------")
    amounts.forEachIndexed { index, amount ->
        total += amount
        println("Index #$index: $amount")
    }
    println("--------------------")
    println("TOTAL = $total")
    println("--------------------")

    if(total == sendersAmount * Sender.MESSAGES_AMOUNT) {
        println("Final State: SUCCESS")
    } else {
        println("Final State: FAIL")
    }

    println("--------------------")
    println("End")
}

