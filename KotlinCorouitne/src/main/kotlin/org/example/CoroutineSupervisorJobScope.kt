package org.example

import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

/*
-Interfazz grafica de una aplicacion acerda del pronostico del tiempo.
-La pantalla se divide en dos paneles independientes.
-Un panel muestra la informacion actual del sitio elegiod. El otro panel muestra una lista de sitios a elegir
-Que pasaria si ambas comparten un contexto y una de ellas lanza una excepcion? La excepcion se propagaria cancelando todas la coroutinas
-Una solucion no optima seria romper la relacion padre-hijo. La solucion optima serai cambiar el tipo Job por un SupervisorJob
*/

//Supervisor Job
//Es un tipo de job en el que la cancelacion solo se propaga hacia abajo de la jerarquia.


//1
//Se tiene tres coroutinas A, B y C. A tiene dos subcoroutinas A1 y A2 (lanza excepcion). B tiene una coroutina B1. C tiene dos coroutina C1 y C2
//En el ejemplo la subcoroutina A2 lanza excepcion, por lo que se cancelar la coroutina A. Sin embargo B y C finalizan su ejecucion por tener
//en la coroutina context un supervisor Job
/*
fun main() {
    log("Start")

    val myObject = MyObjectN()

    runBlocking {
        log("RunBlocking start")

        val jobA = myObject.myFirstTask() //El job A2 lanza una excepcion
        val jobB = myObject.mySecondTaskAsync()
        val jobC = myObject.myThirdTask()

        jobA.invokeOnCompletion {exception -> if (exception != null) log("Job A On Completion - Exception: [ $exception ]") }
        jobB.invokeOnCompletion {exception -> if (exception != null) log("Job B On Completion - Exception: [ $exception ]") }
        jobC.invokeOnCompletion {exception -> if (exception != null) log("Job C On Completion - Exception: [ $exception ]") }

        try {
            log("RunBlocking: Job B deferred value contains: ${jobB.await()}")
        } catch (exception: Exception) {
            log("RunBlocking Caught: [ $exception ]")
        }

        joinAll(jobA, jobC)

        log("RunBlocking end")
    }

    myObject.release()

    log("End")
}

class MyObjectN: CoroutineScope { //COROUTINE SCOPE
    //Job del tipo SupervisorJob
    private val job = SupervisorJob()
    //Manejador de excepciones
    private val exceptionHandler = CoroutineExceptionHandler { _, exception ->
        log("Exception Handler Caught: [ $exception ] with suppressed ${exception.suppressed.contentToString()}")
    }
    //"Job J Start" COROUTINE CONTEXT
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Default + exceptionHandler

    fun myFirstTask() = launch {

        log("Job A start")

        val jobA1 = launch {
            log("Job A1 start")
            delay(5000)
            log("Job A1 end")
        }

        val jobA2 = async<Int> {
            log("Job A2 start")
            delay(3000)
            log("Job A2: before throwing exception")
            throw IndexOutOfBoundsException("Job A2: Working with coroutine exceptions.")
        }

        jobA1.invokeOnCompletion {exception -> if (exception != null) log("Job A1 On Completion - Exception: [ $exception ]") }
        jobA2.invokeOnCompletion {exception -> if (exception != null) log("Job A2 On Completion - Exception: [ $exception ]") }

        joinAll(jobA1, jobA2)
        log("Job A end")
    }

    fun mySecondTaskAsync() = async {
        log("Job B start")

        val jobB1 = launch {
            log("Job B1 start")
            delay(5000)
            log("Job B1 end")
        }

        jobB1.invokeOnCompletion {exception -> if (exception != null) log("Job B1 On Completion - Exception: [ $exception ]") }

        jobB1.join()

        log("Job B end")
        (1..100).random()
    }

    //Separando el Job padre y job hijo
    fun myThirdTask() = launch(Job()) {

        log("Job C start")

        val jobC1 = async {
            log("Job C1 start")
            delay(5000)
            log("Job C1 end")
            (1..100).random()
        }

        val jobC2 = launch  {
            log("Job C2 start")
            delay(5000)
            log("Job C2 end")
        }

        jobC1.invokeOnCompletion {exception -> if (exception != null) log("Job C1 On Completion - Exception: [ $exception ]") }
        jobC2.invokeOnCompletion {exception -> if (exception != null) log("Job C2 On Completion - Exception: [ $exception ]") }

        try {
            log("Job C: Job C1 deferred value contains: ${jobC1.await()}")
        } catch (exception: Exception) {
            log("Job C Caught: [ $exception ]")
        }

        jobC2.join()
        log("Job C end")
    }

    fun release() {
        this.job.cancel()
    }

}
*/


//Supervisor Scope
//Similar al Supervisor Scope pero mas sencillo. Funciona parecida a la funcion withContext.

//2
//Se tiene una coroutina Job A con dos subcoroutinas. La corutina A1 y A2. La coroutina A2 Lanza una excepcion y de detiene.
//Las coroutinas A y A1 terminan su ejecucion por la coroutina Supervisor Scope.
//Las coroutinas A1 y A2 estan envueltos dentro de la corutina A

/*
fun main() {
    log("Start")

    val myObject = MyObjectN()

    runBlocking {
        log("RunBlocking start")

        val jobA = myObject.myFirstTask()

        jobA.invokeOnCompletion {exception -> if (exception != null) log("Job A On Completion - Exception: [ $exception ]") }

        jobA.join()

        log("RunBlocking end")
    }

    myObject.release()

    log("End")
}

class MyObjectN: CoroutineScope {

    private val job = Job()
    private val exceptionHandler = CoroutineExceptionHandler { _, exception ->
        log("Exception Handler Caught: [ $exception ] with suppressed ${exception.suppressed.contentToString()}")
    }
    //Job J
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Default + exceptionHandler

    fun myFirstTask() = launch {
        log("Job A start")

        //SUPERVISOR SCOPE
        supervisorScope {
            val jobA1 = launch {
                repeat(5) {
                    delay(1000)
                    log("Job A1: $it")
                }
            }

            val jobA2 = launch {
                log("Job A2 start")
                delay(3000)
                log("Job A2: before throwing exception")
                throw IndexOutOfBoundsException("Job A2: Working with coroutine exceptions.")
            }

            jobA1.invokeOnCompletion {exception -> if (exception != null) log("Job A1 On Completion - Exception: [ $exception ]") }
            jobA2.invokeOnCompletion {exception -> if (exception != null) log("Job A2 On Completion - Exception: [ $exception ]") }

            repeat(6) {
                delay(700)
                log("Job A: $it")
            }
        }

        log("Job A end")
    }

    fun release() {
        this.job.cancel()
    }

}
*/



//3
//Uso de manejadores de excepcion dentro de cada coroutina que esta asociada al manejador de excepciones
//Con supervisor scope se recomienda usar manejadores de excepcion en cada subcoroutina, caso contrario
//es probable que no te des cuenta de que ocurrio una contingencia.
fun main() {
    log("Start")

    runBlocking {

        log("RunBlocking start")

        //Con SUPPRESSED se accede a las excepciones del JobA y JobB
        val handler = CoroutineExceptionHandler { _, exception ->
            log("Exception Handler Caught: [ $exception ] with suppressed ${exception.suppressed.contentToString()}")
        }

        supervisorScope {

            val jobA = launch(Dispatchers.Default + handler) {
                repeat(5) {
                    delay(1000)
                    log("Job A: $it")
                }
            }

            val jobB = launch(Dispatchers.Default + handler) {
                log("Job B start")
                delay(3000)
                log("Job B: before throwing exception")
                throw IndexOutOfBoundsException("Job B: Working with coroutine exceptions.")
            }

            jobA.invokeOnCompletion {exception -> if (exception != null) log("Job A On Completion - Exception: [ $exception ]") }
            jobB.invokeOnCompletion {exception -> if (exception != null) log("Job B On Completion - Exception: [ $exception ]") }

            repeat(6) {
                delay(700)
                log("RunBlocking: $it")
            }
        }

        log("RunBlocking end")
    }

    log("End")
}


