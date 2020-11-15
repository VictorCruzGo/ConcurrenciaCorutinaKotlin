package org.example

import kotlinx.coroutines.*
import org.example.launch
import kotlin.coroutines.CoroutineContext

//Un programador es un orquestador de orquesta y cada hilo es un instrumento musical.
//Cuando se trabaja con concurrencia generalmente muestras texto en pantalla ya sea para verificar los valores de las
//variables o simplementa para tener una idea del flujo de ejecucion que se da dentro de un conjunto de hilos.
//Se hace practico identificar cada hilo o bloque de ejecucion.
//El coroutineName a nivel de funcionamiento no hace ninguna diferencia pero si nos ayuda a nivel de programacion.

//https://medium.com/kotlin-en-android/coroutines-con-kotlin-coroutinename-a8f9315b7f20


//Coroutine name

//1
//Asigna nombre a las coroutinas
/*
fun main() {
    log("Start")

    runBlocking (CoroutineName("--My Parent Coroutine--")) {

        log("RunBlocking start [ ${this.coroutineContext[CoroutineName]} ]")

        launch (CoroutineName("--My Coroutine A--")) {
            repeat(5) {
                delay(1000)
                log("Hello from [ ${this.coroutineContext[CoroutineName]} ] : $it")
            }
        }

        launch (Dispatchers.Default + CoroutineName("--My Coroutine B--")) {
            repeat(10) {
                delay(700)
                log("Hello from [ ${this.coroutineContext[CoroutineName]} ] : $it")
            }
        }

        log("RunBlocking end [ ${this.coroutineContext[CoroutineName]} ]")
    }

    log("End")
}
*/


//2
//Se observan que las coroutinas hijas heredan el nombre de la coroutina padre
//Otra forma de nombrar es una coroutina es al construir un contexto, por herencia se les asignacion el mismo nombre del contexto
/*
fun main() {
    log("Start")

    val myObject = MyObjectN()

    runBlocking {
        log("RunBlocking start")

        val jobA = myObject.myFirstTask()
        val jobB = myObject.mySecondTask()

        joinAll(jobA, jobB)

        log("RunBlocking end")
    }

    myObject.release()

    log("End")
}

class MyObjectN: CoroutineScope { //Coroutine padre

    private val job = Job()
    private val exceptionHandler = CoroutineExceptionHandler { _, exception ->
        log("Exception Handler Caught: [ $exception ] with suppressed ${exception.suppressed.contentToString()}")
    }
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Default + exceptionHandler + CoroutineName("My Coroutine Name") //Construccion del contexto de la coroutina. Adicionando el nombre de la coroutina

    fun myFirstTask() = launch () {

        log("Job A start : Name: [ ${this.coroutineContext[CoroutineName]} ]")

        val jobA1 = launch {
            repeat(3) {
                delay(1000)
                log("Job A1: Name: [ ${this.coroutineContext[CoroutineName]} ] : $it")
            }
        }

        val jobA2 = async {
            repeat(3) {
                delay(700)
                log("Job A2: Name: [ ${this.coroutineContext[CoroutineName]} ] : $it")
            }
            (1..100).random()
        }

        joinAll(jobA1, jobA2) //Esperamos que finalicen las coroutinas JobA1 y JobA2
        log("Job A end : Name: [ ${this.coroutineContext[CoroutineName]} ]")
    }

    fun mySecondTask() = launch {
        log("Job B start : Name: [ ${this.coroutineContext[CoroutineName]} ]")

        val jobB1 = launch {
            repeat(7) {
                delay(300)
                log("Job B1: Name: [ ${this.coroutineContext[CoroutineName]} ] : $it")
            }
        }

        jobB1.join()
        log("Job B end : Name: [ ${this.coroutineContext[CoroutineName]} ]")
    }

    fun release() {
        this.job.cancel()
    }

}
*/


//3
//Asignacion de nombre de coroutinas a padres e hijos
/*
fun main() {
    log("Start")

    val myObject = MyObjectN()

    runBlocking {
        log("RunBlocking start")

        val jobA = myObject.myFirstTask()
        val jobB = myObject.mySecondTask()

        joinAll(jobA, jobB)

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
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Default + exceptionHandler + CoroutineName("My coroutine father")

    fun myFirstTask() = launch (CoroutineName("My Coroutine A")) {

        log("Job A start : Name: [ ${this.coroutineContext[CoroutineName]} ]")

        val jobA1 = launch (CoroutineName("My Coroutine A1")){
            repeat(3) {
                delay(1000)
                log("Job A1: Name: [ ${this.coroutineContext[CoroutineName]} ] : $it")
            }
        }

        val jobA2 = async (CoroutineName("My Coroutine A2")){
            repeat(3) {
                delay(700)
                log("Job A2: Name: [ ${this.coroutineContext[CoroutineName]} ] : $it")
            }
            (1..100).random()
        }

        joinAll(jobA1, jobA2)
        log("Job A end : Name: [ ${this.coroutineContext[CoroutineName]} ]")
    }

    fun mySecondTask() = launch (CoroutineName("My Coroutine B")){
        log("Job B start : Name: [ ${this.coroutineContext[CoroutineName]} ]")

        val jobB1 = launch (CoroutineName("My Coroutine B1")){
            repeat(7) {
                delay(300)
                log("Job B1: Name: [ ${this.coroutineContext[CoroutineName]} ] : $it")
            }
        }

        jobB1.join()
        log("Job B end : Name: [ ${this.coroutineContext[CoroutineName]} ]")
    }

    fun release() {
        this.job.cancel()
    }
}
*/



