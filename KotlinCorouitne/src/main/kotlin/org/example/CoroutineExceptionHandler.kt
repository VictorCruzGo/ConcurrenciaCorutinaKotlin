package org.example

import kotlinx.coroutines.*
import org.example.launch
import kotlin.coroutines.CoroutineContext


//1
/*
Manejo de excepcinoes
Launch
-Lanzan una excepcion y la propagaran en el momento que se de
Async, produce
-Propagaran la excepcion no la lanzaran hasta que se consuma el dato generado,
hasta que se haga la llamada a sus correspondientes funciones await y recibe

-Es una buena practica encerrar a las funciones await y recive dentro de un bloque try-catch
-CancellationException es el unico tipo de excepcion que no se propaga
*/


//CancellationException no se propaga por el arbol de coroutinas
/*
fun main() {
    log("Start")

    val MyObject1 = MyObject1()

    //runBlockin blquea el hilo principal hasta que termine la coroutina
    runBlocking {
        log("RunBlocking start")

        val job = MyObject1.myTask()
        job.join()

        log("RunBlocking end")
    }

    MyObject1.release()
    log("End")
}

class MyObject1: CoroutineScope {

    private val job = Job()
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Default //getter

    fun myTask() = launch {
        delay(1000)
        log("MyTask: before throwing exception..")
        throw CancellationException("MyTask: Working with coroutine exceptions.") //No se propaga
    }

    fun release() {
        log("Release: before to cancel ")
        this.job.cancel()
        log("Release: after to cancel ")
    }
}
*/


//2
//Propagacion por el arbol provocara que el Job padre se cancele recursivamente hacia arriba
/*
fun main() {
    log("Start")

    val MyObject1 = MyObject1()

    runBlocking {
        log("RunBlocking start")

        val job = MyObject1.myTask()
        job.join()

        log("RunBlocking end")
    }

    MyObject1.release()

    log("End")
}

class MyObject1: CoroutineScope {

    private val job = Job()
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Default //Getter

    fun myTask() = launch {
        delay(1000)
        log("MyTask: before throwing exception")
        throw IndexOutOfBoundsException("MyTask: Working with coroutine exceptions.") //La excepcion se propaga
    }

    fun release() {
        this.job.cancel()
    }
}
*/



//3. Al cancelar el job padre cancela hacia atras los job hijos (Primero en entrar ultimo en salir)
//Se puede observar que la coroutina se detuvo lanzando una excepcion de tipo JobCancellationException por causa del job padre.
//JoinAll, permite enviar como parametros varios jobs a la vez. El hilo que invoca a la funcion se suspendera hasta que todos
//los job enviados como parametros hayan finalizado
/*
fun main() {
    log("Start")

    val MyObject1 = MyObject1()

    runBlocking {
        log("RunBlocking start")

        val job1 = MyObject1.myFirstTask() //lanza excepcion despues de 3000 ms
        val job2 = MyObject1.mySecondTask() //No lanza excepcion

        job1.invokeOnCompletion {exception ->
            if (exception != null) log("Job 1 On Completion - Exception: [ $exception ]")
        }

        job2.invokeOnCompletion {exception ->
            if (exception != null) log("Job 2 On Completion - Exception: [ $exception ]")
        }

        joinAll(job1, job2) //Unir todos los trabajos dados uno por uno con foreach

        log("RunBlocking end")
    }

    MyObject1.release()

    log("End")
}


class MyObject1: CoroutineScope { //Job padre

    private val job = Job()
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Default

    fun myFirstTask() = launch {
        delay(3000)
        log("MyTask 1: before throwing exception")
        throw IndexOutOfBoundsException("MyTask: Working with coroutine exceptions.")
    }

    fun mySecondTask() = launch {
        (1..Int.MAX_VALUE).forEach {
            delay(1000)
            log("MyTask 2: repetition $it")
        }
    }

    fun release() {
        this.job.cancel()
    }

}
*/

//4
//Cambiar el contructor lauch por async
//Se observa que la coroutina creada con async no despliega el erro en pantall aunque podemos comprobar que la corutine de la
//funcion mySecondTask se cancelo por consecuencia de la excepcion lanzada.
/*
fun main() {
    log("Start")

    val MyObject1 = MyObject1()

    runBlocking {
        log("RunBlocking start")

        val def = MyObject1.myFirstTaskAsync()
        val job = MyObject1.mySecondTask()

        job.invokeOnCompletion {exception ->
            if (exception != null) log("Job On Completion - Exception: [ $exception ]")
        }

        joinAll(def, job)

        log("RunBlocking end")
    }

    MyObject1.release()

    log("End")
}

class MyObject1: CoroutineScope {

    private val job = Job()
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Default

    //Se cambio el constructor lauch por async
    fun myFirstTaskAsync() = async<Int> {
        delay(3000)
        log("MyTask 1: before throwing exception")
        throw IndexOutOfBoundsException("MyTask: Working with coroutine exceptions.")
    }

    fun mySecondTask() = launch {
        (1..Int.MAX_VALUE).forEach {
            delay(1000)
            log("MyTask 2: repetition $it")
        }
    }

    fun release() {
        this.job.cancel()
    }

}
*/


//5
//Consumir el datos el dato de los jobs async (await) y lauch(join)
//Al esperar datos de los job se atrapa la excepcion
/*
fun main() {
    log("Start")

    val MyObject1 = MyObject1()

    runBlocking {
        log("RunBlocking start")

        val def = MyObject1.myFirstTaskAsync()
        val job = MyObject1.mySecondTask()

        job.invokeOnCompletion {exception ->
            if (exception != null) log("Job On Completion - Exception: [ $exception ]")
        }

        try{
            log("RunBlocking: Def value = ${def.await()}") //Al mover esta linea fuera del try el programa terminara abruptamente
        }catch (e: Exception) {
            log("RunBlocking Catch: [ ${e.message} ]")
        }

        job.join()

        log("RunBlocking end")
    }

    MyObject1.release()

    log("End")
}

class MyObject1: CoroutineScope {

    private val job = Job()
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Default

    fun myFirstTaskAsync() = async<Int> {
        delay(3000)
        log("MyTask 1: before throwing exception")
        throw IndexOutOfBoundsException("MyTask: Working with coroutine exceptions.")
    }

    fun mySecondTask() = launch {
        (1..Int.MAX_VALUE).forEach {
            delay(1000)
            log("MyTask 2: repetition $it")
        }
    }

    fun release() {
        this.job.cancel()
    }

}
*/



//6
//Lanzamiento de excepciones dentro de coroutinas anidadas
//La excepcion se propaga a los jobs padres
/*
fun main() {
    log("Start")

    val MyObject1 = MyObject1()

    runBlocking {
        log("RunBlocking start")

        val jobA = MyObject1.myFirstTask()
        val jobB = MyObject1.mySecondTask()

        jobA.invokeOnCompletion {exception -> if (exception != null) log("Job A On Completion - Exception: [ $exception ]") }
        jobB.invokeOnCompletion {exception -> if (exception != null) log("Job B On Completion - Exception: [ $exception ]") }

        joinAll(jobA, jobB)
        log("RunBlocking end")
    }

    MyObject1.release()

    log("End")
}

class MyObject1: CoroutineScope {

    private val job = Job()
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Default

    fun myFirstTask() = launch {

        log("Job A start")

        val jobA1 = launch {
            (1..Int.MAX_VALUE).forEach {
                delay(1000)
                log("Job A1: repetition $it")
            }
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

    fun mySecondTask() = launch {
        log("Job B start")

        val jobB1 = launch {
            (1..Int.MAX_VALUE).forEach {
                delay(1000)
                log("Job B1: repetition $it")
            }
        }
        jobB1.invokeOnCompletion {exception ->
            if (exception != null) log("Job B1 On Completion - Exception: [ $exception ]")
        }

        jobB1.join()
        log("Job B end")
    }

    fun release() {
        this.job.cancel()
    }

}
*/


//7
//Romper la relacion padre-hijo y propagar la excepcion
//Al crear la coroutine usando job propio se rompe la relacion padre-hijo entre el job contexto global del objeto
//y el job de la coroutina.
//Al romper la relacino padre-hijo el job C continua con su ejecucion
//La cal esta entonces en saber si una tarea es dependiente de otra, de ser asi entonces si alguna fall
//tendrian que cancelarse en bloque, pero si son diferentes deberian vivir en socpes o contextos separados
/*
fun main() {
    log("Start")

    val MyObject1 = MyObject1()
    val maxRepetitions = 8

    runBlocking {
        log("RunBlocking start")

        val jobA = MyObject1.myFirstTask()
        val jobB = MyObject1.mySecondTask()
        val jobC = MyObject1.myThirdTask()

        jobA.invokeOnCompletion {exception -> if (exception != null) log("Job A On Completion - Exception: [ $exception ]") }
        jobB.invokeOnCompletion {exception -> if (exception != null) log("Job B On Completion - Exception: [ $exception ]") }
        jobC.invokeOnCompletion {exception -> if (exception != null) log("Job C On Completion - Exception: [ $exception ]") }

        var rep = 0
        while(rep < maxRepetitions && (jobA.isActive || jobB.isActive || jobC.isActive)) {
            delay(1000)
            log("RunBlocking: States: ${getJobsState(jobA, jobB, jobC)}")
            rep++
        }

        log("RunBlocking end")
    }

    MyObject1.release()

    log("End")
}

fun getJobsState(jobA: Job, jobB: Job, jobC: Job): String {
    val jobAText = if(jobA.isActive) "Job A is active" else if(jobA.isCancelled) "Job A is cancelled" else if(jobA.isCompleted) "Job A is completed" else "Job A Unknown state"
    val jobBText = if(jobB.isActive) "Job B is active" else if(jobB.isCancelled) "Job B is cancelled" else if(jobB.isCompleted) "Job B is completed" else "Job B Unknown state"
    val jobCText = if(jobC.isActive) "Job C is active" else if(jobC.isCancelled) "Job C is cancelled" else if(jobC.isCompleted) "Job C is completed" else "Job C Unknown state"

    return "$jobAText | $jobBText | $jobCText"
}

class MyObject1: CoroutineScope {

    private val job = Job()
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Default

    fun myFirstTask() = launch {

        log("Job A start")

        val jobA1 = launch {
            (1..Int.MAX_VALUE).forEach {
                delay(1000)
                log("Job A1: repetition $it")
            }
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

    fun mySecondTask() = launch {
        log("Job B start")

        val jobB1 = launch {
            (1..Int.MAX_VALUE).forEach {
                delay(1000)
                log("Job B1: repetition $it")
            }
        }
        jobB1.invokeOnCompletion {exception ->
            if (exception != null) log("Job B1 On Completion - Exception: [ $exception ]")
        }

        jobB1.join()
        log("Job B end")
    }

    //Al crear la coroutine usando job propio se rompe la relacion padre-hijo entre el job contexto global del objeto
    //y el job de la coroutina.
    //Al romper la relacino padre-hijo el job C continua con su ejecucion
    fun myThirdTask() = launch(Job()) {
        log("Job C start")

        val jobC1 = async {
            log("Job C1 start")

            val repetitions = (1..5).random()
            repeat(repetitions) {
                delay(1000)
                log("Job C1: repetition $it")
            }

            repetitions
        }

        val jobC2 = launch {
            (1..Int.MAX_VALUE).forEach {
                delay(1000)
                log("Job C2: repetition $it")
            }
        }

        jobC1.invokeOnCompletion {exception -> if (exception != null) log("Job C1 On Completion - Exception: [ $exception ]") }
        jobC2.invokeOnCompletion {exception -> if (exception != null) log("Job C2 On Completion - Exception: [ $exception ]") }

        try {
            log("Job C: Value = ${jobC1.await()}")
        } catch (e: Exception) {
            log("Job C Catch: [ ${e.message} ]")
        }

        jobC2.join()
        log("Job C end")
    }

    fun release() {
        this.job.cancel()
    }

}
*/


//8
//Atrapar excepcion generada por una coroutina hija (lauch)
//El uso de un manejardor de excepciones propio solo funciona con el tipo de coroutines (Launch) que propagan las excepciones.
//Con los constructores async y produce, un manejador propio no tiene ningun efecto

/*
fun main() {
    log("Start")

    val MyObject1 = MyObject1()

    runBlocking {
        log("RunBlocking start")

        val job = MyObject1.myFirstTask()
        job.invokeOnCompletion {exception -> if (exception != null) log("Job On Completion - Exception: [ $exception ]") } //Atrapar excepcion
        job.join()

        log("RunBlocking end")
    }

    MyObject1.release()

    log("End")
}

class MyObject1: CoroutineScope {

    private val job = Job()
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Default

    fun myFirstTask() = launch {
        log("Job start")
        delay(1000)
        log("Job: before throwing exception")
        throw IndexOutOfBoundsException("Job: Working with coroutine exceptions.")
    }

    fun release() {
        this.job.cancel()
    }
}
*/


//9
//Con manejador propio

/*
fun main() {
    log("Start")
    val MyObject1 = MyObject1()

    runBlocking {
        log("RunBlocking start")

        val job = MyObject1.myFirstTask()
        job.invokeOnCompletion {exception -> if (exception != null) log("Job On Completion - Exception: [ $exception ]") }
        job.join()

        log("RunBlocking end")
    }

    MyObject1.release()
    log("End")
}

class MyObject1: CoroutineScope {
    private val job = Job()
    //Manejador propio de excepciones
    private val exceptionHandler = CoroutineExceptionHandler { _, exception ->
        log("Exception Handler: [ ${exception.message} ]")
    }

    override val coroutineContext: CoroutineContext
        //adicionar el manejador de excepcion (+ exceptionHandler)
        get() = job + Dispatchers.Default + exceptionHandler

    fun myFirstTask() = launch {
        log("Job start")
        delay(1000)
        log("Job: before throwing exception")
        throw IndexOutOfBoundsException("Job: Working with coroutine exceptions.")
    }

    fun release() {
        this.job.cancel()
    }
}
*/



//10
//Sin manejador propio (try - catch)
/*
fun main() {
    log("Start")

    val MyObject1 = MyObject1()

    runBlocking {
        log("RunBlocking start")

        val def = MyObject1.myFirstTaskAsync()
        def.invokeOnCompletion {exception -> if (exception != null) log("Job On Completion - Exception: [ $exception ]") }

        try {
            log("RunBlocking: Deferred Value = ${def.await()}")
        } catch (e: Exception) {
            log("RunBlocking Catch: [ ${e.message} ]")
        }

        log("RunBlocking end")
    }

    MyObject1.release()

    log("End")
}

class MyObject1: CoroutineScope {

    private val job = Job()
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Default

    fun myFirstTaskAsync() = async<Int> {
        log("Job start")
        delay(1000)
        log("Job: before throwing exception")
        throw IndexOutOfBoundsException("Job: Working with coroutine exceptions.")
    }

    fun release() {
        this.job.cancel()
    }

}
*/



//12
//Coroutina que hara uso de un manejador propio y tres coroutinas "hijas" que lancen una excepcion diferente cada una.
//Al lanzar una excepcion desde mas de una coroutina hija, la que sea atrapada primero por el manejador de excepciones sera la que tenga prioirdad
//De todas la excepciones hijas, solo el primero tiene prioridad
/*
fun main() {
    log("Start")

    val MyObject1 = MyObject1()

    runBlocking {
        log("RunBlocking start")

        val jobA = MyObject1.myFirstTask()

        jobA.invokeOnCompletion {exception -> if (exception != null) log("Job A On Completion - Exception: [ $exception ]") }

        jobA.join()

        log("RunBlocking end")
    }

    MyObject1.release()

    log("End")
}

class MyObject1: CoroutineScope {

    private val job = Job()
    //Manejador propio de excepciones
    private val exceptionHandler = CoroutineExceptionHandler { _, exception ->
        log("Exception Handler Caught: [ $exception ] with suppressed ${exception.suppressed.contentToString()}")
    }
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Default + exceptionHandler

    fun myFirstTask() = launch {
        log("Job A start")

        val jobA1 = launch {
            delay(1000)
            log("Job A1: before throwing exception")
            throw IndexOutOfBoundsException("Job A1: Working with coroutine exceptions.")
        }

        val jobA2 = launch {
            try {
                log("Job A2: before delay")
                delay(2000)
                log("Job A2: after delay")
            } catch (e: Exception) {
                log("Job A2 Caught: [ $e ]")
            } finally {
                throw IllegalStateException("Job A2: Working with coroutine exceptions.")
            }
        }

        val jobA3 = launch {
            try {
                log("Job A3: before delay")
                delay(2500)
                log("Job A3: after delay")
            } catch (e: Exception) {
                log("Job A3 Caught: [ $e ]")
            } finally {
                throw ArithmeticException("Job A3: Working with coroutine exceptions.")
            }
        }

        joinAll(jobA1, jobA2, jobA3)

        log("Job A end")
    }

    fun release() {
        this.job.cancel()
    }

}
*/


//13
// Acceder a todas las excepciones hijas "suppresed"
//
fun main() {
    log("Start")

    val MyObject1 = MyObject1()

    runBlocking {
        log("RunBlocking start")

        val jobA = MyObject1.myFirstTask()
        val jobB = MyObject1.mySecondTask()

        jobA.invokeOnCompletion {exception -> if (exception != null) log("Job A On Completion - Exception: [ $exception ]") }
        jobB.invokeOnCompletion {exception -> if (exception != null) log("Job B On Completion - Exception: [ $exception ]") }

        joinAll(jobA, jobB)

        log("RunBlocking end")
    }

    MyObject1.release()

    log("End")
}

//Ambas coroutinas estan enlazadas al contexto global de la clase
//Manejar las excepciones de manera general es muy efectivo cuando todas la coroutinas estan correlacionadas,
//Pero no siempre es el caso, algunas veces podriamos requerir que ciertas coroutinas continues su ejecucion normalmente
//a pesar que de que se hay a lanzado alguna excepcion en otra coroutina con la que comprte el contexto. Esto
//se puede lograr haciendo uso de SupervisorJob y del supervisorScope.
class MyObject1: CoroutineScope {

    private val job = Job()
    //Manejador de excepciones
    private val exceptionHandler = CoroutineExceptionHandler { _, exception ->
        log("Exception Handler Caught: [ $exception ] with suppressed ${exception.suppressed.contentToString()}")
    }
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Default + exceptionHandler

    //Job A1 y A2 estan enlazadas a Job A.
    //La excepcion lanzada de primero tiene prioridad
    fun myFirstTask() = launch {
        log("Job A start")

        val jobA1 = launch {
            log("Job A1 start")
            delay(20000)
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

    fun mySecondTask() = launch {
        log("Job B start")

        val jobB1 = launch {
            try {
                log("Job B1 start")
                delay(20000)
                log("Job B1 end")
            } finally {
                throw IllegalStateException("Job B1: Working with coroutine exceptions.")
            }
        }
        jobB1.invokeOnCompletion {exception ->
            if (exception != null) log("Job B1 On Completion - Exception: [ $exception ]")
        }

        jobB1.join()
        log("Job B end")
    }

    fun release() {
        this.job.cancel()
    }

}


