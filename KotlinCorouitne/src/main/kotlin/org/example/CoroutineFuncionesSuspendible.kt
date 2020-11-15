package org.example

import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext
import kotlin.system.measureTimeMillis

/*
Suspend functions
-Delay, join, await, withContext, supervisorScope, etc son suspend functions
-Una suspend funtion puede ser llamado solo dentro de una coroutina o dentro de otra suspend function
-La diferencia con respecto a una funcion comun esta en el modificador suspend.
-Cuando usamos suspend estamos manifestando que la funcion tiene la posibilidad de suspender el hilo depende
enteramente de la implementacion de la funcion.
-Perfectamente podemos declarar un suspend function que no suspende el hilo, aunque oviamente no es lo ideal
*/

//1
//Declaracion de una suspend function
/*
fun main() {
    log("Start")

    runBlocking {
        log("RunBlocking start")
        mySuspendFunction()
        log("RunBlocking end")
    }

    log("End")
}

suspend fun mySuspendFunction() {
    log("MyFunction start")
    repeat(5){
        delay(300)
        log("MyFunction: repetition $it")
    }
    log("MyFunction end")
}
*/

//2
//myBlockingSuspendFunction, la funcion bloquea el hilo que si quitas el modificador suspend funcionara exactamente igual

/*
fun main() {
    log("Start")

    var launchOneTimeMillis: Long = 0
    var launchTwoTimeMillis: Long = 0

    val totalTime = measureTimeMillis {
        runBlocking {
            log("RunBlocking start")

            launch {
                launchOneTimeMillis = measureTimeMillis {
                    log("Launch 1 start")
                    //Bloque el hilo principal 3 segundos
                    myBlockingSuspendFunction()
                    log("Launch 1 end")
                }
            }

            launch {
                launchTwoTimeMillis = measureTimeMillis {
                    //No se ejecutar hata que termine lauch 1
                    log("Launch 2 start")
                    repeat(5) {
                        delay(600)
                        log("Launch 2: repetition $it")
                    }
                    log("Launch 2 end")
                }
            }

            log("RunBlocking end")
        }
    }

    log("Times: Total = $totalTime | Launch 1 = $launchOneTimeMillis | Launch 2 = $launchTwoTimeMillis")
    log("End")

}

suspend fun myBlockingSuspendFunction() {
    log("MyFunction start")
    //Simular una tarea pesada que bloquea el hilo que la invoca (consumo de un servico externo, base de datos, archivo, etc)
    //Al cambiar Thread por delay no es una solucion valida
    Thread.sleep(3000) //Simulating a heavy blocking task.
    log("MyFunction end")
}
*/

//3
//Sin bloquear el hilo principal
//La solucion para no bloquear el hilo principal seria cambiar de contexto

/*
fun main() {
    log("Start")

    var launchOneTimeMillis: Long = 0
    var launchTwoTimeMillis: Long = 0

    val totalTime = measureTimeMillis {
        runBlocking {
            log("RunBlocking start")

            launch {
                launchOneTimeMillis = measureTimeMillis {
                    log("Launch 1 start")
                    //Se suspende el hilo para darle paso perteneciente al contexto
                    myBlockingSuspendFunction()
                    log("Launch 1 end")
                }
            }

            launch {
                launchTwoTimeMillis = measureTimeMillis {
                    log("Launch 2 start")
                    repeat(5) {
                        delay(600)
                        log("Launch 2: repetition $it")
                    }
                    log("Launch 2 end")
                }
            }

            log("RunBlocking end")
        }
    }

    log("Times: Total = $totalTime | Launch 1 = $launchOneTimeMillis | Launch 2 = $launchTwoTimeMillis")
    log("End")

}

suspend fun myBlockingSuspendFunction() {
    log("MyFunction start")
    withContext(Dispatchers.Default) {
        Thread.sleep(3000) //Simulating a heavy blocking task.
    }
    log("MyFunction end")
}
 */

//3
//Segunda forma para no bloquear el hilo pesado
//Para hacer uso de correcto de la suspend function, solo debes asegurarte que cada una de las tareas dentro de la
//funcion se ejecuta en el hilo correcto
/*
fun main() {
    log("Start")

    var launchOneTimeMillis: Long = 0
    var launchTwoTimeMillis: Long = 0

    val totalTime = measureTimeMillis {
        runBlocking {
            log("RunBlocking start")

            launch {
                launchOneTimeMillis = measureTimeMillis {
                    log("Launch 1 start")
                    myNotBlockingSuspendFunction()
                    log("Launch 1 end")
                }
            }

            launch {
                launchTwoTimeMillis = measureTimeMillis {
                    log("Launch 2 start")
                    repeat(5) {
                        delay(600)
                        log("Launch 2: repetition $it")
                    }
                    log("Launch 2 end")
                }
            }

            log("RunBlocking end")
        }
    }

    log("Times: Total = $totalTime | Launch 1 = $launchOneTimeMillis | Launch 2 = $launchTwoTimeMillis")
    log("End")

}


//Es preferente englobal toda la suspend function porque asi permite reconocer el contexto  (main o DefaultDispatcher)
suspend fun myNotBlockingSuspendFunction() = withContext(Dispatchers.Default) {
    log("MyFunction start")
    Thread.sleep(3000) //Simulating a heavy blocking task.
    log("MyFunction end")
}
*/

//4
/*
//Version 1
fun loadItems() {
    showLoadingView() // mostrar vista de carga
    val data = requestData() //solicitar datos
    hideLoadingView() // ocultar vista de carga
    appendItemsToListView(data) //agregar elementos a la vista de lista
}

//Version 2
//Al utilizar diferentes contexto es necesario convertir la funcion a suspend
suspend fun loadItems() {
    withContext(Dispatchers.Main) {
        showLoadingView()
    }
    withContext(Dispatchers.IO) {
        val data = requestData()
    }
    withContext(Dispatchers.Main) {
        hideLoadingView()
        appendItemsToListView(data)
    }
}

//Version 3
//Para mayor legilibilidad englobar la suspend function. Englobando con Dispatcher.IO
suspend fun loadItems() = withContext(Dispatchers.IO) {
    withContext(Dispatchers.Main) {
        showLoadingView()
    }
    val data = requestData()
    withContext(Dispatchers.Main) {
        hideLoadingView()
        appendItemsToListView(data)
    }
}

//Version 4
//Englobando con Dispacher.Main
suspend fun loadItems() = withContext(Dispatchers.Main) {
    showLoadingView()
    val data = withContext(Dispatchers.IO) {
        requestData()
    }
    hideLoadingView()
    appendItemsToListView(data)
}

//Version 5
//Es preferible establer explicitamente el CONTEXTO porque si lo dejamos implicito, se podrian generar errores dado
//que deberas confiar en la funcion sera invocada por el hilo correcto
suspend fun loadItems() {
    showLoadingView() //Confiar que sera invocada por el hilo correcto
    val data = withContext(Dispatchers.IO) {
        requestData()
    }
    hideLoadingView() //Confiar que sera invocada por el hilo correcto
    appendItemsToListView(data)
}
 */

//5
//Las suspend function se ejecutan de manera secuencial por defecto
/*
fun main() {
    log("Start")

    runBlocking {
        log("RunBlocking start")

        launch(Dispatchers.Default) {
            log("Launch start")
            mySuspendFunction()
            log("Launch middle")
            mySuspendFunction()
            log("Launch end")
        }

        log("RunBlocking end")
    }

    log("End")
}

suspend fun mySuspendFunction() {
    log("MyFunction start")
    delay(1000)
    log("MyFunction end")
}
 */

//6
//Las suspend function pueden devolver valores
/*
fun main() {
    log("Start")

    val time = measureTimeMillis {
        runBlocking {
            log("Launch start 1")
            launch(Dispatchers.Default) {
                log("Launch start")
                val r1 = myRandomNumber()
                val r2 = myRandomNumber()
                val r3 = myRandomNumber()
                log("Launch Result = ${r1 + r2 + r3} | R1 = $r1 | R2 = $r2 | R3 = $r3")
                log("Launch end")
            }
            log("Launch end 1")
        }
    }

    log("Total Time = $time milliseconds.")
    log("End")
}

//Suspend function retorna un valor
suspend fun myRandomNumber(): Int = withContext(Dispatchers.Default) {
    log("Retrieving random number...")
    delay(2000) //Simulating a heavy computation
    val random = (1..100).random()
    log("Random Number = $random")
    random
}
 */

//7
//Llamada CONCURRENTE de la suspend function para obtener el numero aleatorio
/*
fun main() {
    log("Start")

    val time = measureTimeMillis {
        runBlocking {
            launch(Dispatchers.Default) {
                log("Launch start")
                val def1 = async { myRandomNumber() }
                val def2 = async { myRandomNumber() }
                val def3 = async { myRandomNumber() }
                val r1 = def1.await()
                val r2 = def2.await()
                val r3 = def3.await()
                log("Launch Result = ${r1 + r2 + r3} | R1 = $r1 | R2 = $r2 | R3 = $r3")
                log("Launch end")
            }
        }
    }

    log("Total Time = $time milliseconds.")
    log("End")
}

suspend fun myRandomNumber(): Int = withContext(Dispatchers.Default) {
    log("Retrieving random number...")
    delay(2000) //Simulating a heavy computation
    val random = (1..100).random()
    log("Random Number = $random")
    random
}
 */

//8
//Emplo practico
/*
Las tres operaciones independientes son las siguientes:
Sumar 3 números aleatorios y verificar que el resultado es un número par.
Sumar 3 números aleatorios y calcular el valor promedio del resultado.
Sumar 3 números aleatorios y calcular el valor elevado al cuadrado del resultado.
*/

//V1
/*
class Operator: CoroutineScope {
    companion object {
        const val OPERATION_IS_EVEN = "Operation [Is Even]"
        const val OPERATION_AVERAGE = "Operation [Average]"
        const val OPERATION_SQUARE = "Operation [Square]"
    }

    private val job = SupervisorJob()
    private val exceptionHandler = CoroutineExceptionHandler { _, exception ->
        log("Exception Handler Caught: [ $exception ] with suppressed ${exception.suppressed.contentToString()}")
    }
    //Contexto Global construido con SupervisorJob().
    //Para que las tres operaciones independientes entre si no se interrumpan
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Default + exceptionHandler

    private suspend fun myRandomNumberSuccess(operation: String): Int = withContext(Dispatchers.Default) {
        log("$operation Retrieving random number...")
        delay(2000) //Simulating a heavy computation
        val random = (1..100).random()
        log("$operation Random Number = $random")
        random
    }

    private suspend fun myRandomNumberFail(operation: String): Int = withContext(Dispatchers.Default) {
        log("$operation Retrieving random number...")
        delay(1000) //Simulating a heavy computation
        throw IllegalStateException("$operation Retrieving random number failed.")
    }

    fun release() {
        this.job.cancel()
    }

}

fun main() {
    log("Start")

    val operator = Operator()

    runBlocking {
        //To be implemented...
    }

    operator.release()

    log("End")
}
 */

//v2
//Obteniendo numero aleatorios correctos
/*
class Operator: CoroutineScope {

    companion object {
        const val OPERATION_IS_EVEN = "Operation [Is Even]"
        const val OPERATION_AVERAGE = "Operation [Average]"
        const val OPERATION_SQUARE = "Operation [Square]"
    }

    private val job = SupervisorJob()
    private val exceptionHandler = CoroutineExceptionHandler { _, exception ->
        log("Exception Handler Caught: [ $exception ] with suppressed ${exception.suppressed.contentToString()}")
    }
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Default + exceptionHandler

    fun sumIsEvenAsync() = async {
        val r1 = async { myRandomNumberSuccess(OPERATION_IS_EVEN) }
        val r2 = async { myRandomNumberSuccess(OPERATION_IS_EVEN) }
        val r3 = async { myRandomNumberSuccess(OPERATION_IS_EVEN) }

        val sum = r1.await() + r2.await() + r3.await()
        log("$OPERATION_IS_EVEN: Sum = $sum")
        sum % 2 == 0
    }

    private suspend fun myRandomNumberSuccess(operation: String): Int = withContext(Dispatchers.Default) {
        log("$operation Retrieving random number...")
        delay(2000) //Simulating a heavy computation
        val random = (1..100).random()
        log("$operation Random Number = $random")
        random
    }

    private suspend fun myRandomNumberFail(operation: String): Int = withContext(Dispatchers.Default) {
        log("$operation Retrieving random number...")
        delay(1000) //Simulating a heavy computation
        throw IllegalStateException("$operation Retrieving random number failed.")
    }

    fun release() {
        this.job.cancel()
    }

}

fun main() {
    log("Start")

    val operator = Operator()

    runBlocking {
        val defIsEven = operator.sumIsEvenAsync()

        launch {
            try {
                log("${Operator.OPERATION_IS_EVEN} Result = ${defIsEven.await()}")
            } catch (e: Exception) {
                log("${Operator.OPERATION_IS_EVEN} Caught: [ $e ]")
            } finally {
                log("--- NOTHING CONTAINING \"${Operator.OPERATION_IS_EVEN}\" SHOULD APPEAR AFTER THIS LINE ---")
                delay(3000)
            }
        }
    }

    operator.release()

    log("End")
}
*/

//v3
//Obteniendo un numero aleatorios incorrecto. La operacion se cancela
/*
class Operator: CoroutineScope {

    companion object {
        const val OPERATION_IS_EVEN = "Operation [Is Even]"
        const val OPERATION_AVERAGE = "Operation [Average]"
        const val OPERATION_SQUARE = "Operation [Square]"
    }

    private val job = SupervisorJob()
    private val exceptionHandler = CoroutineExceptionHandler { _, exception ->
        log("Exception Handler Caught: [ $exception ] with suppressed ${exception.suppressed.contentToString()}")
    }
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Default + exceptionHandler

    fun sumIsEvenAsync() = async {
        val r1 = async { myRandomNumberSuccess(OPERATION_IS_EVEN) }
        val r2 = async { myRandomNumberFail(OPERATION_IS_EVEN) }
        val r3 = async { myRandomNumberSuccess(OPERATION_IS_EVEN) }

        val sum = r1.await() + r2.await() + r3.await()
        log("$OPERATION_IS_EVEN: Sum = $sum")
        sum % 2 == 0
    }

    private suspend fun myRandomNumberSuccess(operation: String): Int = withContext(Dispatchers.Default) {
        log("$operation Retrieving random number...")
        delay(2000) //Simulating a heavy computation
        val random = (1..100).random()
        log("$operation Random Number = $random")
        random
    }

    private suspend fun myRandomNumberFail(operation: String): Int = withContext(Dispatchers.Default) {
        log("$operation Retrieving random number...")
        delay(1000) //Simulating a heavy computation
        throw IllegalStateException("$operation Retrieving random number failed.")
    }

    fun release() {
        this.job.cancel()
    }

}

fun main() {
    log("Start")

    val operator = Operator()

    runBlocking {
        val defIsEven = operator.sumIsEvenAsync()

        launch {
            try {
                log("${Operator.OPERATION_IS_EVEN} Result = ${defIsEven.await()}")
            } catch (e: Exception) {
                log("${Operator.OPERATION_IS_EVEN} Caught: [ $e ]")
            } finally {
                log("--- NOTHING CONTAINING \"${Operator.OPERATION_IS_EVEN}\" SHOULD APPEAR AFTER THIS LINE ---")
                delay(3000)
            }
        }
    }

    operator.release()

    log("End")
}
*/

//9
//Unas de las operaciones Falla, pero la ejecucion continua
/*
class Operator: CoroutineScope {

    companion object {
        const val OPERATION_IS_EVEN = "Operation [Is Even]"
        const val OPERATION_AVERAGE = "Operation [Average]"
        const val OPERATION_SQUARE = "Operation [Square]"
    }

    private val job = SupervisorJob()
    private val exceptionHandler = CoroutineExceptionHandler { _, exception ->
        log("Exception Handler Caught: [ $exception ] with suppressed ${exception.suppressed.contentToString()}")
    }
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Default + exceptionHandler

    fun sumIsEvenAsync() = async {
        val r1 = async { myRandomNumberSuccess(OPERATION_IS_EVEN) }
        val r2 = async { myRandomNumberSuccess(OPERATION_IS_EVEN) }
        val r3 = async { myRandomNumberSuccess(OPERATION_IS_EVEN) }

        val sum = r1.await() + r2.await() + r3.await()
        log("$OPERATION_IS_EVEN: Sum = $sum")
        sum % 2 == 0
    }

    fun sumAverageAsync() = async {
        val r1 = async { myRandomNumberSuccess(OPERATION_AVERAGE) }
        val r2 = async { myRandomNumberFail(OPERATION_AVERAGE) }
        val r3 = async { myRandomNumberSuccess(OPERATION_AVERAGE) }

        val sum = r1.await() + r2.await() + r3.await()
        log("$OPERATION_AVERAGE: Sum = $sum")
        sum.toFloat() / 3
    }

    private suspend fun myRandomNumberSuccess(operation: String): Int = withContext(Dispatchers.Default) {
        log("$operation Retrieving random number...")
        delay(2000) //Simulating a heavy computation
        val random = (1..100).random()
        log("$operation Random Number = $random")
        random
    }

    private suspend fun myRandomNumberFail(operation: String): Int = withContext(Dispatchers.Default) {
        log("$operation Retrieving random number...")
        delay(1000) //Simulating a heavy computation
        throw IllegalStateException("$operation Retrieving random number failed.")
    }

    fun release() {
        this.job.cancel()
    }

}

fun main() {
    log("Start")

    val operator = Operator()

    runBlocking {
        val defIsEven = operator.sumIsEvenAsync()
        val defAverage = operator.sumAverageAsync()

        launch {
            try {
                log("${Operator.OPERATION_IS_EVEN} Result = ${defIsEven.await()}")
            } catch (e: Exception) {
                log("${Operator.OPERATION_IS_EVEN} Caught: [ $e ]")
            } finally {
                log("--- NOTHING CONTAINING \"${Operator.OPERATION_IS_EVEN}\" SHOULD APPEAR AFTER THIS LINE ---")
                delay(3000)
            }
        }

        launch {
            try {
                log("${Operator.OPERATION_AVERAGE} Result = ${defAverage.await()}")
            } catch (e: Exception) {
                log("${Operator.OPERATION_AVERAGE} Caught: [ $e ]")
            } finally {
                log("--- NOTHING CONTAINING \"${Operator.OPERATION_AVERAGE}\" SHOULD APPEAR AFTER THIS LINE ---")
                delay(3000)
            }
        }
    }

    operator.release()

    log("End")
}
 */

//10
//Progrma completo
/*
class Operator: CoroutineScope {

    companion object {
        const val OPERATION_IS_EVEN = "Operation [Is Even]"
        const val OPERATION_AVERAGE = "Operation [Average]"
        const val OPERATION_SQUARE = "Operation [Square]"
    }

    private val job = SupervisorJob()
    private val exceptionHandler = CoroutineExceptionHandler { _, exception ->
        log("Exception Handler Caught: [ $exception ] with suppressed ${exception.suppressed.contentToString()}")
    }
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Default + exceptionHandler

    fun sumIsEvenAsync() = async {
        val r1 = async { myRandomNumberSuccess(OPERATION_IS_EVEN) }
        val r2 = async { myRandomNumberSuccess(OPERATION_IS_EVEN) }
        val r3 = async { myRandomNumberSuccess(OPERATION_IS_EVEN) }

        val sum = r1.await() + r2.await() + r3.await()
        log("$OPERATION_IS_EVEN: Sum = $sum")
        sum % 2 == 0
    }

    fun sumAverageAsync() = async {
        val r1 = async { myRandomNumberSuccess(OPERATION_AVERAGE) }
        val r2 = async { myRandomNumberSuccess(OPERATION_AVERAGE) }
        val r3 = async { myRandomNumberSuccess(OPERATION_AVERAGE) }

        val sum = r1.await() + r2.await() + r3.await()
        log("$OPERATION_AVERAGE: Sum = $sum")
        sum.toFloat() / 3
    }

    fun sumSquareAsync() = async {
        val r1 = async { myRandomNumberSuccess(OPERATION_SQUARE) }
        val r2 = async { myRandomNumberSuccess(OPERATION_SQUARE) }
        val r3 = async { myRandomNumberSuccess(OPERATION_SQUARE) }

        val sum = r1.await() + r2.await() + r3.await()
        log("$OPERATION_SQUARE: Sum = $sum")
        sum * sum
    }

    private suspend fun myRandomNumberSuccess(operation: String): Int = withContext(Dispatchers.Default) {
        log("$operation Retrieving random number...")
        delay(2000) //Simulating a heavy computation
        val random = (1..100).random()
        log("$operation Random Number = $random")
        random
    }

    private suspend fun myRandomNumberFail(operation: String): Int = withContext(Dispatchers.Default) {
        log("$operation Retrieving random number...")
        delay(1000) //Simulating a heavy computation
        throw IllegalStateException("$operation Retrieving random number failed.")
    }

    fun release() {
        this.job.cancel()
    }

}

fun main() {
    log("Start")

    val operator = Operator()

    runBlocking {
        val defIsEven = operator.sumIsEvenAsync()
        val defAverage = operator.sumAverageAsync()
        val defSquare = operator.sumSquareAsync()

        launch {
            try {
                log("${Operator.OPERATION_IS_EVEN} Result = ${defIsEven.await()}")
            } catch (e: Exception) {
                log("${Operator.OPERATION_IS_EVEN} Caught: [ $e ]")
            } finally {
                log("--- NOTHING CONTAINING \"${Operator.OPERATION_IS_EVEN}\" SHOULD APPEAR AFTER THIS LINE ---")
                delay(3000)
            }
        }

        launch {
            try {
                log("${Operator.OPERATION_AVERAGE} Result = ${defAverage.await()}")
            } catch (e: Exception) {
                log("${Operator.OPERATION_AVERAGE} Caught: [ $e ]")
            } finally {
                log("--- NOTHING CONTAINING \"${Operator.OPERATION_AVERAGE}\" SHOULD APPEAR AFTER THIS LINE ---")
                delay(3000)
            }
        }

        launch {
            try {
                log("${Operator.OPERATION_SQUARE} Result = ${defSquare.await()}")
            } catch (e: Exception) {
                log("${Operator.OPERATION_SQUARE} Caught: [ $e ]")
            } finally {
                log("--- NOTHING CONTAINING \"${Operator.OPERATION_SQUARE}\" SHOULD APPEAR AFTER THIS LINE ---")
                delay(3000)
            }
        }
    }

    operator.release()

    log("End")
}
*/


//11
//El programa completo funcional quedaría de la siguiente manera: (me quede en esta linea)
/*
class Operator: CoroutineScope {

    companion object {
        const val OPERATION_IS_EVEN = "Operation [Is Even]"
        const val OPERATION_AVERAGE = "Operation [Average]"
        const val OPERATION_SQUARE = "Operation [Square]"
    }

    //SupervisorJob evitara que la excepcion se propague
    private val job = SupervisorJob()
    private val exceptionHandler = CoroutineExceptionHandler { _, exception ->
        log("Exception Handler Caught: [ $exception ] with suppressed ${exception.suppressed.contentToString()}")
    }
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Default + exceptionHandler

    fun sumIsEvenAsync() = async {
        val r1 = async { myRandomNumberSuccess(OPERATION_IS_EVEN) }
        val r2 = async { myRandomNumberSuccess(OPERATION_IS_EVEN) }
        val r3 = async { myRandomNumberSuccess(OPERATION_IS_EVEN) }

        val sum = r1.await() + r2.await() + r3.await()
        log("$OPERATION_IS_EVEN: Sum = $sum")
        sum % 2 == 0
    }

    fun sumAverageAsync() = async {
        val r1 = async { myRandomNumberSuccess(OPERATION_AVERAGE) }
        val r2 = async { myRandomNumberSuccess(OPERATION_AVERAGE) }
        val r3 = async { myRandomNumberSuccess(OPERATION_AVERAGE) }

        val sum = r1.await() + r2.await() + r3.await()
        log("$OPERATION_AVERAGE: Sum = $sum")
        sum.toFloat() / 3
    }

    fun sumSquareAsync() = async {
        val r1 = async { myRandomNumberSuccess(OPERATION_SQUARE) }
        val r2 = async { myRandomNumberSuccess(OPERATION_SQUARE) }
        val r3 = async { myRandomNumberSuccess(OPERATION_SQUARE) }

        val sum = r1.await() + r2.await() + r3.await()
        log("$OPERATION_SQUARE: Sum = $sum")
        sum * sum
    }

    private suspend fun myRandomNumberSuccess(operation: String): Int = withContext(Dispatchers.Default) {
        log("$operation Retrieving random number...")
        delay(2000) //Simulating a heavy computation
        val random = (1..100).random()
        log("$operation Random Number = $random")
        random
    }

    private suspend fun myRandomNumberFail(operation: String): Int = withContext(Dispatchers.Default) {
        log("$operation Retrieving random number...")
        delay(1000) //Simulating a heavy computation
        throw IllegalStateException("$operation Retrieving random number failed.")
    }

    fun release() {
        this.job.cancel()
    }

}

fun main() {
    log("Start")

    val operator = Operator()

    runBlocking {
        val defIsEven = operator.sumIsEvenAsync()
        val defAverage = operator.sumAverageAsync()
        val defSquare = operator.sumSquareAsync()

        launch {
            try {
                log("${Operator.OPERATION_IS_EVEN} Result = ${defIsEven.await()}")
            } catch (e: Exception) {
                log("${Operator.OPERATION_IS_EVEN} Caught: [ $e ]")
            } finally {
                log("--- NOTHING CONTAINING \"${Operator.OPERATION_IS_EVEN}\" SHOULD APPEAR AFTER THIS LINE ---")
                delay(3000)
            }
        }

        launch {
            try {
                log("${Operator.OPERATION_AVERAGE} Result = ${defAverage.await()}")
            } catch (e: Exception) {
                log("${Operator.OPERATION_AVERAGE} Caught: [ $e ]")
            } finally {
                log("--- NOTHING CONTAINING \"${Operator.OPERATION_AVERAGE}\" SHOULD APPEAR AFTER THIS LINE ---")
                delay(3000)
            }
        }

        launch {
            try {
                log("${Operator.OPERATION_SQUARE} Result = ${defSquare.await()}")
            } catch (e: Exception) {
                log("${Operator.OPERATION_SQUARE} Caught: [ $e ]")
            } finally {
                log("--- NOTHING CONTAINING \"${Operator.OPERATION_SQUARE}\" SHOULD APPEAR AFTER THIS LINE ---")
                delay(3000)
            }
        }
    }

    operator.release()

    log("End")
}
*/


//12
//Eliminando codigo repetido. Se sumaran los resultado de los numeros aleatorios
/*
class Operator: CoroutineScope {

    companion object {
        const val OPERATION_IS_EVEN = "Operation [Is Even]"
        const val OPERATION_AVERAGE = "Operation [Average]"
        const val OPERATION_SQUARE = "Operation [Square]"
    }

    private val job = SupervisorJob()
    private val exceptionHandler = CoroutineExceptionHandler { _, exception ->
        log("Exception Handler Caught: [ $exception ] with suppressed ${exception.suppressed.contentToString()}")
    }
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Default + exceptionHandler

    fun sumIsEvenAsync() = async {
        val sum = sumThreeRandomNumbers(OPERATION_IS_EVEN)
        log("$OPERATION_IS_EVEN: Sum sumIsEvenAsync()= $sum")
        sum % 2 == 0
    }

    fun sumAverageAsync() = async {
        val sum = sumThreeRandomNumbers(OPERATION_AVERAGE)
        log("$OPERATION_AVERAGE: Sum sumAverageAsync()= $sum")
        sum.toFloat() / 3
    }

    fun sumSquareAsync() = async {
        val sum = sumThreeRandomNumbers(OPERATION_SQUARE)
        log("$OPERATION_SQUARE: Sum sumSquareAsync()= $sum")
        sum * sum
    }

    //Funcion que elimina codigo repetido
    private suspend fun sumThreeRandomNumbers(operation: String): Int {
        val r1 = async { myRandomNumberSuccess(operation) }
        val r2 = async { myRandomNumberSuccess(operation) }
        val r3 = async { myRandomNumberSuccess(operation) }

        return r1.await() + r2.await() + r3.await()
    }

    private suspend fun myRandomNumberSuccess(operation: String): Int = withContext(Dispatchers.Default) {
        log("$operation Retrieving random number...")
        delay(2000) //Simulating a heavy computation
        val random = (1..100).random()
        log("$operation Random Number = $random")
        random
    }

    private suspend fun myRandomNumberFail(operation: String): Int = withContext(Dispatchers.Default) {
        log("$operation Retrieving random number...")
        delay(1000) //Simulating a heavy computation
        throw IllegalStateException("$operation Retrieving random number failed.")
    }

    fun release() {
        this.job.cancel()
    }

}

fun main() {
    log("Start")

    val operator = Operator()

    runBlocking {
        val defIsEven = operator.sumIsEvenAsync()
        val defAverage = operator.sumAverageAsync()
        val defSquare = operator.sumSquareAsync()

        launch {
            try {
                log("${Operator.OPERATION_IS_EVEN} Result = ${defIsEven.await()}")
            } catch (e: Exception) {
                log("${Operator.OPERATION_IS_EVEN} Caught: [ $e ]")
            } finally {
                log("--- NOTHING CONTAINING \"${Operator.OPERATION_IS_EVEN}\" SHOULD APPEAR AFTER THIS LINE ---")
                delay(3000)
            }
        }

        launch {
            try {
                log("${Operator.OPERATION_AVERAGE} Result = ${defAverage.await()}")
            } catch (e: Exception) {
                log("${Operator.OPERATION_AVERAGE} Caught: [ $e ]")
            } finally {
                log("--- NOTHING CONTAINING \"${Operator.OPERATION_AVERAGE}\" SHOULD APPEAR AFTER THIS LINE ---")
                delay(3000)
            }
        }

        launch {
            try {
                log("${Operator.OPERATION_SQUARE} Result = ${defSquare.await()}")
            } catch (e: Exception) {
                log("${Operator.OPERATION_SQUARE} Caught: [ $e ]")
            } finally {
                log("--- NOTHING CONTAINING \"${Operator.OPERATION_SQUARE}\" SHOULD APPEAR AFTER THIS LINE ---")
                delay(3000)
            }
        }
    }

    operator.release()

    log("End")
}
 */

//13
//De las tres operaciones falla la funcion sumIsEvenAsync()
//Las demas operaciones continuan funcionando
//Al fallar la generacion de los numeros randomicos, las tres operaciones fallan puesto que las tres llaman a la misma
//funciona para obtener el valor aleatorio.
//Las 3 funciones que llaman a la funcion que obtiene el numero aleatorio se estan creando en un scope ambiguo.
//Dentro de la funcion sumThreeRandomNumber existen dos posibles scopes, el scope global de la clase y e scope de la coroutine que invoca la funcion

/*
class Operator: CoroutineScope {

    companion object {
        const val OPERATION_IS_EVEN = "Operation [Is Even]"
        const val OPERATION_AVERAGE = "Operation [Average]"
        const val OPERATION_SQUARE = "Operation [Square]"
    }

    private val job = SupervisorJob()
    private val exceptionHandler = CoroutineExceptionHandler { _, exception ->
        log("Exception Handler Caught: [ $exception ] with suppressed ${exception.suppressed.contentToString()}")
    }
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Default + exceptionHandler

    //FALLA LANZANDO UNA EXCEPCION. LAS DEMAS TAREAS CONTINUAN CON SU EJECUCION
    fun sumIsEvenAsync() = async {
        val sum = sumThreeRandomNumbers(OPERATION_IS_EVEN)
        delay(1000)
        throw ArithmeticException("$OPERATION_IS_EVEN throwing exception sumIsEvenAsync()!!!")
    }

    fun sumAverageAsync() = async {
        val sum = sumThreeRandomNumbers(OPERATION_AVERAGE)
        log("$OPERATION_AVERAGE: Sum sumAverageAsync() = $sum")
        sum.toFloat() / 3
    }

    fun sumSquareAsync() = async {
        val sum = sumThreeRandomNumbers(OPERATION_SQUARE)
        log("$OPERATION_SQUARE: Sum sumSquareAsync() = $sum")
        sum * sum
    }

    //Existen dos posibles scopes, el scope global de la clase (coroutine scope) y el scope de la coroutina (async)
    //Para solucionar el problema debemos especificar el socope en el que se crean las funciones
    private suspend fun sumThreeRandomNumbers(operation: String): Int = coroutineScope {
        val r1 = async { myRandomNumberSuccess(operation) }
        val r2 = async { myRandomNumberSuccess(operation) }
        val r3 = async { myRandomNumberSuccess(operation) }

        //return r1.await() + r2.await() + r3.await()
        r1.await() + r2.await() + r3.await()
    }

    private suspend fun myRandomNumberSuccess(operation: String): Int = withContext(Dispatchers.Default) {
        log("$operation Retrieving random number...")
        delay(2000) //Simulating a heavy computation
        val random = (1..100).random()
        log("$operation Random Number myRandomNumberSuccess()= $random")
        random
    }

    private suspend fun myRandomNumberFail(operation: String): Int = withContext(Dispatchers.Default) {
        log("$operation Retrieving random number...")
        delay(1000) //Simulating a heavy computation
        throw IllegalStateException("$operation Retrieving random number failed. myRandomNumberFail()")
    }

    fun release() {
        this.job.cancel()
    }

}

fun main() {
    log("Start")

    val operator = Operator()

    runBlocking {
        val defIsEven = operator.sumIsEvenAsync()
        val defAverage = operator.sumAverageAsync()
        val defSquare = operator.sumSquareAsync()

        launch {
            try {
                log("${Operator.OPERATION_IS_EVEN} Result = ${defIsEven.await()}")
            } catch (e: Exception) {
                log("${Operator.OPERATION_IS_EVEN} Caught: [ $e ]")
            } finally {
                log("--- NOTHING CONTAINING \"${Operator.OPERATION_IS_EVEN}\" SHOULD APPEAR AFTER THIS LINE ---")
                delay(3000)
            }
        }

        launch {
            try {
                log("${Operator.OPERATION_AVERAGE} Result = ${defAverage.await()}")
            } catch (e: Exception) {
                log("${Operator.OPERATION_AVERAGE} Caught: [ $e ]")
            } finally {
                log("--- NOTHING CONTAINING \"${Operator.OPERATION_AVERAGE}\" SHOULD APPEAR AFTER THIS LINE ---")
                delay(3000)
            }
        }

        launch {
            try {
                log("${Operator.OPERATION_SQUARE} Result = ${defSquare.await()}")
            } catch (e: Exception) {
                log("${Operator.OPERATION_SQUARE} Caught: [ $e ]")
            } finally {
                log("--- NOTHING CONTAINING \"${Operator.OPERATION_SQUARE}\" SHOULD APPEAR AFTER THIS LINE ---")
                delay(3000)
            }
        }
    }

    operator.release()

    log("End")
}
*/

//14
//Al asignarle COROUTINE SCOPE a la funcion sumThreeRandomNumbers() (el ambito de la clase) fallan las tres operacion
//myRandomNumberSuccess, existen dos posibles scopes, el scope global de la clase (coroutine scope) y el scope de la coroutina (async)
//Para solucionar el problema debemos especificar el scope en el que se crean las funciones
//Cuando falla una de las coroutinas creadas dentro de la funcion sumThreeRandomNumbers, las otras dos se detienen (COROUTINE SCOPE)

class Operator: CoroutineScope {
    companion object {
        const val OPERATION_IS_EVEN = "Operation [Is Even]"
        const val OPERATION_AVERAGE = "Operation [Average]"
        const val OPERATION_SQUARE = "Operation [Square]"
    }

    private val job = SupervisorJob()
    private val exceptionHandler = CoroutineExceptionHandler { _, exception ->
        log("Exception Handler Caught: [ $exception ] with suppressed ${exception.suppressed.contentToString()}")
    }
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Default + exceptionHandler

    fun sumIsEvenAsync() = async {
        val sum = sumThreeRandomNumbers(OPERATION_IS_EVEN)
        log("$OPERATION_IS_EVEN: Sum = $sum")
        sum % 2 == 0
    }

    fun sumAverageAsync() = async {
        val sum = sumThreeRandomNumbers(OPERATION_AVERAGE)
        log("$OPERATION_AVERAGE: Sum = $sum")
        sum.toFloat() / 3
    }

    fun sumSquareAsync() = async {
        val sum = sumThreeRandomNumbers(OPERATION_SQUARE)
        log("$OPERATION_SQUARE: Sum = $sum")
        sum * sum
    }

    //Al asignarle COROUTINE SCOPE a la funcion sumThreeRandomNumbers() (el ambito de la clase) fallan las tres operacion.
    //Sino se coloca el scope, sera ambiguo y continuara con la generacion de los numeros aleatorios.
    private suspend fun sumThreeRandomNumbers(operation: String): Int = coroutineScope {
    //private suspend fun sumThreeRandomNumbers(operation: String): Int {
        val r1 = async { myRandomNumberFail(operation) }
        val r2 = async { myRandomNumberSuccess(operation) }
        val r3 = async { myRandomNumberSuccess(operation) }

        //return r1.await() + r2.await() + r3.await()
        r1.await() + r2.await() + r3.await()
    }

    //CONTEXT
    private suspend fun myRandomNumberSuccess(operation: String): Int = withContext(Dispatchers.Default) {
        log("$operation Retrieving random number...")
        delay(2000) //Simulating a heavy computation
        val random = (1..100).random()
        log("$operation Random Number myRandomNumberSuccess()= $random")
        random
    }

    private suspend fun myRandomNumberFail(operation: String): Int = withContext(Dispatchers.Default) {
        log("$operation Retrieving random number...")
        delay(1000) //Simulating a heavy computation
        throw IllegalStateException("$operation Retrieving random number failed.")
    }

    fun release() {
        this.job.cancel()
    }

}

fun main() {
    log("Start")

    val operator = Operator()

    runBlocking {
        val defIsEven = operator.sumIsEvenAsync()
        val defAverage = operator.sumAverageAsync()
        val defSquare = operator.sumSquareAsync()

        launch {
            try {
                log("${Operator.OPERATION_IS_EVEN} Result = ${defIsEven.await()}")
            } catch (e: Exception) {
                log("${Operator.OPERATION_IS_EVEN} Caught: [ $e ]")
            } finally {
                log("--- NOTHING CONTAINING \"${Operator.OPERATION_IS_EVEN}\" SHOULD APPEAR AFTER THIS LINE ---")
                delay(3000)
            }
        }

        launch {
            try {
                log("${Operator.OPERATION_AVERAGE} Result = ${defAverage.await()}")
            } catch (e: Exception) {
                log("${Operator.OPERATION_AVERAGE} Caught: [ $e ]")
            } finally {
                log("--- NOTHING CONTAINING \"${Operator.OPERATION_AVERAGE}\" SHOULD APPEAR AFTER THIS LINE ---")
                delay(3000)
            }
        }

        launch {
            try {
                log("${Operator.OPERATION_SQUARE} Result = ${defSquare.await()}")
            } catch (e: Exception) {
                log("${Operator.OPERATION_SQUARE} Caught: [ $e ]")
            } finally {
                log("--- NOTHING CONTAINING \"${Operator.OPERATION_SQUARE}\" SHOULD APPEAR AFTER THIS LINE ---")
                delay(3000)
            }
        }
    }

    operator.release()

    log("End")
}
