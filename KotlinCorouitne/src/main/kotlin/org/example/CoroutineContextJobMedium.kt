package org.example

import kotlinx.coroutines.*
import kotlin.coroutines.ContinuationInterceptor

//CONSTRUCTOR LAUNCH

fun log(message: String) {
    println("[${Thread.currentThread().name}] : $message")
}

fun log(character: Char) {
    print("$character")
}
/*
fun main() {
    log("Start")

    runBlocking {
        val job = launch {
            repeat(3) {
                log("Launch rep #$it : I'm active")
                delay(1000)
            }
            log("Launch : I'm finishing.")
        }

        delay(100)
        while(job.isActive) {
            log("RunBlocking : Job is active")
            delay(1000)
        }

        log("RunBlocking : Job is not active")
    }

    log("End")
}
*/


//JOB CANCEL
/*
fun main() {
    log("Start")

    runBlocking {
        val job = launch {
            repeat(10) {
                log("Launch rep #$it : I'm active")
                delay(1000)
            }
            log("Launch : I'm finishing.")
        }

        delay(2500)
        while(job.isActive) {
            log("RunBlocking : Job is active")
            delay(1000)
            log("RunBlocking : Cancelling Job")
            job.cancel()
        }

        if(job.isCancelled) {
            log("RunBlocking : Job is cancelled")
        } else {
            log("RunBlocking : Job is not cancelled")
        }
    }

    log("End")
}
*/


/*
fun main() {
    log("Start")

    runBlocking {
        val job = launch {
            repeat(10) {
                log("Launch rep #$it : I'm active")
                delay(1000)
            }
            log("Launch : I'm finishing.")
        }

        delay(2500)
        while(job.isActive) {
            log("RunBlocking : Job is active")
            delay(1000)
            log("RunBlocking : Cancelling Job")
            job.cancel()
        }

        if(job.isCancelled) {
            log("RunBlocking : Job is cancelled")
        }

        //Si estuvise en estado Cancelled la condicion isCompleted se cumpliria
        if(job.isCompleted) {
            log("RunBlocking : Job is completed")
        }
    }

    log("End")
}
*/

/*
fun main() {
    log("Start")

    runBlocking {
        val job = launch {
            repeat(3) {
                log("---Launch rep #$it : I'm active")
                delay(1000)
            }
            log("Launch : I'm finishing.")
        }

        delay(100)
        while(job.isActive) {
            log("RunBlocking : Job is active")
            delay(1000)
        }

        if(job.isCancelled) {
            log("RunBlocking : Job is cancelled")
        }

        //Tener cuidado al acceder a la propiedad isCompleted dato que si cancelaste el job pero este no ha alcanzado el estaod Cancelado
        if(job.isCompleted) {
            log("RunBlocking : Job is completed")
        }
    }

    log("End")
}
*/

//JOB CHILD
/*
fun main() {
    log("Start")

    runBlocking {
        val job = launch {

            val childJob = launch {
                repeat(3) {
                    log("---Child rep #$it : I'm active")
                    delay(1000)
                }

                log("----Child : I'm finishing.")
            }

            delay(100)
            while(childJob.isActive) {
                log("Launch : Child I'm active")
                delay(1000)
            }

            log("Launch : Child I'm finishing.")
        }

        delay(200)
        while(job.isActive) {
            log("RunBlocking : Father Job is active")
            delay(1000)
        }

        if(job.isCancelled) {
            log("RunBlocking : Father Job is cancelled")
        }

        if(job.isCompleted) {
            log("RunBlocking : Father Job is completed")
        }
    }

    log("End")
}
 */

//El Job padre estara activo hasta que todos sus job hijos finalicen.
//El job padre estaria en completing hata que el job hijo acabe su ejecucion
/*
fun main() {
    log("Start")

    runBlocking {
        val job = launch {

            val childJob = launch {
                repeat(3) {
                    log("Child rep #$it : I'm active")
                    delay(1000)
                }

                log("Child : I'm finishing.")
            }

            log("Launch : Child I'm finishing.")
        }

        delay(200)
        while(job.isActive) {
            log("RunBlocking : Father Job is active")
            delay(1000)
        }

        if(job.isCancelled) {
            log("RunBlocking : Father Job is cancelled")
        }

        if(job.isCompleted) {
            log("RunBlocking : Father Job is completed")
        }
    }

    log("End")
}
 */


//Cancelar job hijo mientra job padre espera
//El job hijo termina de manera abrupta.
//El job padre termina con su estado completado
/*
fun main() {
    log("Start")

    runBlocking {
        val job = launch {

            val childJob = launch {
                repeat(10) {
                    log("Child rep #$it : I'm active")
                    delay(1000)
                }

                log("Child : I'm finishing.")
            }

            delay(2500)
            while(childJob.isActive) {
                log("Launch : Child Job is active")
                delay(1000)
                log("Launch : Child Cancelling Job")
                childJob.cancel()
            }

            if(childJob.isCancelled) {
                log("Launch : Child Job is cancelled")
            }

            if(childJob.isCompleted) {
                log("Launch : Child Job is completed")
            }

            log("Launch : Child I'm finishing.")
        }

        delay(200)
        while(job.isActive) {
            log("RunBlocking : Father Job is active")
            delay(1000)
        }

        if(job.isCancelled) {
            log("RunBlocking : Father Job is cancelled")
        }

        if(job.isCompleted) {
            log("RunBlocking : Father Job is completed")
        }
    }

    log("End")
}
 */


//Job padre cancelado sin que el job hijo haya finalizado
//Al cancelar el job padre, terminan de manera abrupta tanto el job padre como el hijo
/*
fun main() {
    log("Start")

    runBlocking {
        var childJob: Job? = null
        val job = launch {

            childJob = launch {
                repeat(10) {
                    log("Child rep #$it : I'm active")
                    delay(1000)
                }

                log("Child : I'm finishing.")
            }

            delay(100)
            while(childJob?.isActive == true) {
                log("Launch : child Job is active")
                delay(1000)
            }

            log("Launch : Father I'm finishing.")
        }

        delay(2500)
        while(job.isActive) {
            log("RunBlocking : Father Job is active")
            delay(1000)
            log("RunBlocking : Father Cancelling Job")
            job.cancel()
        }

        if(childJob?.isCancelled == true) {
            log("RunBlocking : Child Job is cancelled")
        }

        if(childJob?.isCompleted == true) {
            log("RunBlocking : Child Job is completed")
        }

        if(job.isCancelled) {
            log("RunBlocking : Father Job is cancelled")
        }

        if(job.isCompleted) {
            log("RunBlocking : Father Job is completed")
        }
    }

    log("End")
}

 */



//Job padre en estado COMPLETING esperando a que su jo hijo termine, pero cancelmos el job padre antes que su hijo finalice.
//El job hijo termina de forma abrupta. Terminan con estado cancelados

/*
fun main() {
    log("Start")

    runBlocking {
        var childJob: Job? = null
        val job = launch {

            childJob = launch {
                repeat(10) {
                    log("Child rep #$it : I'm active")
                    delay(1000)
                }

                log("Child : I'm finishing.")
            }

            log("Launch : I'm finishing.")
        }

        delay(2500)
        while(job.isActive) {
            log("RunBlocking : Job is active")
            delay(1000)
            log("RunBlocking : Cancelling Job")
            job.cancel()
        }

        if(childJob?.isCancelled == true) {
            log("RunBlocking : Child Job is cancelled")
        }

        if(childJob?.isCompleted == true) {
            log("RunBlocking : Child Job is completed")
        }

        if(job.isCancelled) {
            log("RunBlocking : Job is cancelled")
        }

        if(job.isCompleted) {
            log("RunBlocking : Job is completed")
        }
    }

    log("End")
}
*/

//DEFERRED
//Async se comporta exactamente igual que el contructor launch.
//Launch devuelve un Job. Async devuelve un deferred
/*
fun main() {
    log("Start")

    runBlocking {
        val result = async {
            log("Async : I'm starting.")
            val a = (1..50).random()
            log("Async : A = $a")
            delay(1000)
            val b = (1..50).random()
            log("Async : B = $b")
            delay(1000)
            log("Async : I'm finishing.")
            a + b
        }

        delay(100)
        while(result.isActive) {
            log("RunBlocking : Async is active")
            delay(1000)
        }

        log("RunBlocking : Result is ${result.await()}")
    }

    log("End")
}*/

/*
fun main() {
    log("Start")

    runBlocking {
        val result = async {
            log("Async : I'm starting.")
            val a = (1..50).random()
            log("Async : A = $a")
            delay(1000)
            val b = (1..50).random()
            log("Async : B = $b")
            delay(1000)
            log("Async : I'm finishing.")
            a + b
        }

        log("RunBlocking : Before calling .await()")
        log("RunBlocking : Result is ${result.await()}")
    }

    log("End")
}

 */

//JOB -  JOIN
/*
fun main() {
    log("Start")

    runBlocking {
        val job = launch {
            repeat(3) {
                log("Launch rep #$it : I'm active")
                delay(1000)
            }
            log("Launch : I'm finishing.")
        }

        log("RunBlocking : Before calling .join()")
        job.join()
        log("RunBlocking : Job is not active")
    }

    log("End")
}
*/

//Cancelar el Job mienstra se espera que finalice "join"
//Al cancelar un job, cualquier coroutina suspendido (join) continua con su ejecucion
/*
fun main() {
    log("Start")

    runBlocking {
        val job = launch {
            repeat(10) {
                log("Launch rep #$it : I'm active")
                delay(1000)
            }
            log("Launch : I'm finishing.")
        }

        launch {
            delay(3500)
            log("Before cancelling Job")
            job.cancel()
        }

        log("RunBlocking : Before calling .join()")
        job.join()

        if(job.isCancelled) {
            log("RunBlocking : Job is cancelled")
        }

        if(job.isCompleted) {
            log("RunBlocking : Job is completed")
        }
    }

    log("End")
}

 */


//Cancelar el job desde una corutina hermana (async o lauch) mientras espera la llamada a la funciona await
//Se obtiene una excepcion
//Es sumamente importante encerrae dentro de un bloque try-catch cualquier llamada a la funcion await
//Al usar join y cancelar no se cuelga el programa. Ocurre lo contrario con await.

/*
fun main() {
    log("Start")

    runBlocking {
        val result = async {
            log("Async : I'm starting.")
            val a = (1..50).random()
            log("Async : A = $a")
            delay(1000)
            val b = (1..50).random()
            log("Async : B = $b")
            delay(1000)
            log("Async : I'm finishing.")
            a + b
        }

        launch {
            delay(1500)
            log("Before cancelling Job")
            result.cancel()
        }

        log("RunBlocking : Before calling .await()")
        log("RunBlocking : Result is ${result.await()}")

        if(result.isCancelled) {
            log("RunBlocking : Job is cancelled")
        }

        if(result.isCompleted) {
            log("RunBlocking : Job is completed")
        }
    }

    log("End")
}
*/


//Solucion a la excepcion
/*
fun main() {
    log("Start")

    runBlocking {
        val result = async {
            log("Async : I'm starting.")
            val a = (1..50).random()
            log("Async : A = $a")
            delay(1000)
            val b = (1..50).random()
            log("Async : B = $b")
            delay(1000)
            log("Async : I'm finishing.")
            a + b
        }

        launch {
            delay(1500)
            log("Before cancelling Job")
            result.cancel()
        }

        try {
            log("RunBlocking : Before calling .await()")
            log("RunBlocking : Result is ${result.await()}")
        }catch(e: Exception) {
            log("RunBlocking : Oops! There was a problem: ${e.message}")
        }

        if(result.isCancelled) {
            log("RunBlocking : Job is cancelled")
        }

        if(result.isCompleted) {
            log("RunBlocking : Job is completed")
        }
    }

    log("End")
}
*/

//Cancelar job padre con dos hijos y un nieto
//La cancelacion del job padre se propaga a los job hijos recursivamente
/*
fun main() {
    log("Start")

    runBlocking {
        var childJob1: Job? = null
        var childJob2: Job? = null
        var childChildJob: Job? = null
        val job = launch {

            childJob1 = launch {

                childChildJob = launch {
                    repeat(5) {
                        log("Child1-Child rep #$it : I'm active")
                        delay(1000)
                    }
                    log("Child1-Child : I'm finishing.")
                }

                repeat(5) {
                    log("Child 1 rep #$it : I'm active")
                    delay(1000)
                }
                log("Child 1 : I'm finishing.")
            }

            childJob2 = launch {
                repeat(5) {
                    log("Child 2 rep #$it : I'm active")
                    delay(1000)
                }
                log("Child 2 : I'm finishing.")
            }

            log("Launch : I'm finishing.")
        }

        delay(1500)
        while(job.isActive) {
            log("RunBlocking : Job is active")
            delay(1000)
            log("RunBlocking : Cancelling Job")
            job.cancel()
        }

        if(childJob1?.isCancelled == true) {
            log("RunBlocking : Child Job 1 is cancelled")
        }

        if(childJob1?.isCompleted == true) {
            log("RunBlocking : Child Job 1 is completed")
        }

        if(childChildJob?.isCancelled == true) {
            log("RunBlocking : Child-Child Job is cancelled")
        }

        if(childChildJob?.isCompleted == true) {
            log("RunBlocking : Child-Child Job is completed")
        }

        if(childJob2?.isCancelled == true) {
            log("RunBlocking : Child Job 2 is cancelled")
        }

        if(childJob2?.isCompleted == true) {
            log("RunBlocking : Child Job 2 is completed")
        }

        if(job.isCancelled) {
            log("RunBlocking : Job is cancelled")
        }

        if(job.isCompleted) {
            log("RunBlocking : Job is completed")
        }
    }

    log("End")
}
*/

//Cancelar el job nieto
//Al cancelar el job nieto, los jobs padre, hijo1 y hijo 2 continuan con su ejecucion normalemente hasta completar
/*
fun main() {
    log("Start")

    runBlocking {
        var childJob1: Job? = null
        var childJob2: Job? = null
        var childChildJob: Job? = null
        val job = launch {

            childJob1 = launch {

                childChildJob = launch {
                    repeat(5) {
                        log("Child1-Child rep #$it : I'm active")
                        delay(1000)
                    }
                    log("Child1-Child : I'm finishing.")
                }

                repeat(5) {
                    log("Child 1 rep #$it : I'm active")
                    delay(1000)
                }
                log("Child 1 : I'm finishing.")
            }

            childJob2 = launch {
                repeat(5) {
                    log("Child 2 rep #$it : I'm active")
                    delay(1000)
                }
                log("Child 2 : I'm finishing.")
            }

            log("Launch : I'm finishing.")
        }

        delay(1500)
        while(job.isActive) {
            log("RunBlocking : Job is active")
            delay(1000)

            if(childChildJob?.isActive == true) {
                log("RunBlocking : Cancelling Child-Child Job")
                childChildJob?.cancel()
            }
        }

        if(childJob1?.isCancelled == true) {
            log("RunBlocking : Child Job 1 is cancelled")
        }

        if(childJob1?.isCompleted == true) {
            log("RunBlocking : Child Job 1 is completed")
        }

        if(childChildJob?.isCancelled == true) {
            log("RunBlocking : Child-Child Job is cancelled")
        }

        if(childChildJob?.isCompleted == true) {
            log("RunBlocking : Child-Child Job is completed")
        }

        if(childJob2?.isCancelled == true) {
            log("RunBlocking : Child Job 2 is cancelled")
        }

        if(childJob2?.isCompleted == true) {
            log("RunBlocking : Child Job 2 is completed")
        }

        if(job.isCancelled) {
            log("RunBlocking : Job is cancelled")
        }

        if(job.isCompleted) {
            log("RunBlocking : Job is completed")
        }
    }

    log("End")
}

 */


//Generar una excepcion en el job nieto.
//El programa se cuelga sin contemplaciones. Genera excepciones
/*
fun main() {
    log("Start")

    runBlocking {
        var childJob1: Job? = null
        var childJob2: Job? = null
        var childChildJob: Job? = null
        val job = launch {

            childJob1 = launch {

                childChildJob = launch {
                    repeat(5) {
                        log("Child1-Child rep #$it : I'm active")
                        delay(1000)

                        if(it == 2) {
                            log("Child1-Child : Before division by zero")
                            1 / 0
                        }
                    }
                    log("Child1-Child : I'm finishing.")
                }

                repeat(5) {
                    log("Child 1 rep #$it : I'm active")
                    delay(1000)
                }
                log("Child 1 : I'm finishing.")
            }

            childJob2 = launch {
                repeat(5) {
                    log("Child 2 rep #$it : I'm active")
                    delay(1000)
                }
                log("Child 2 : I'm finishing.")
            }

            log("Launch : I'm finishing.")
        }

        log("RunBlocking : Before calling .join()")
        job.join()

        if(childJob1?.isCancelled == true) {
            log("RunBlocking : Child Job 1 is cancelled")
        }

        if(childJob1?.isCompleted == true) {
            log("RunBlocking : Child Job 1 is completed")
        }

        if(childChildJob?.isCancelled == true) {
            log("RunBlocking : Child-Child Job is cancelled")
        }

        if(childChildJob?.isCompleted == true) {
            log("RunBlocking : Child-Child Job is completed")
        }

        if(childJob2?.isCancelled == true) {
            log("RunBlocking : Child Job 2 is cancelled")
        }

        if(childJob2?.isCompleted == true) {
            log("RunBlocking : Child Job 2 is completed")
        }

        if(job.isCancelled) {
            log("RunBlocking : Job is cancelled")
        }

        if(job.isCompleted) {
            log("RunBlocking : Job is completed")
        }
    }

    log("End")
}*/

//Construcciono del coroutine conexte con un job
//El job myJob sigue activo, aunque la coroutina a la que pasamos el job (myJob) termine su ejecucion.
//Esto permite asociar todos las coroutinas que querramos a nuestro job propio (myJob)
/*
fun main() {
    log("Start")
    val myJob = Job()
    log("MyJob: $myJob")

    runBlocking {
        log("RunBlocking ---| " +
                "Job: ${this.coroutineContext[Job]} , " +
                "ContinuationInterceptor: ${this.coroutineContext[ContinuationInterceptor]} , " +
                "CoroutineExceptionHandler: ${this.coroutineContext[CoroutineExceptionHandler]} , " +
                "CoroutineName: ${this.coroutineContext[CoroutineName]}")

        //hace que el job de la coroutina (launch(myJob)) creada sea un job hijo del job que pasamos como parametro
        launch(myJob) {
            log("Launch |||| " +
                    "Job: ${this.coroutineContext[Job]} , " +
                    "ContinuationInterceptor: ${this.coroutineContext[ContinuationInterceptor]} , " +
                    "CoroutineExceptionHandler: ${this.coroutineContext[CoroutineExceptionHandler]} , " +
                    "CoroutineName: ${this.coroutineContext[CoroutineName]}")

            delay(1000)
            log("Launch : I'm finishing.")
        }

        launch {
            delay(5500)
            log("Before cancelling MyJob.")
            myJob.cancel()
        }

        delay(100)
        while(myJob.isActive) {
            log("RunBlocking : MyJob is active.")
            delay(1000)
        }

        log("RunBlocking : MyJob is not active.")
    }
    log("End")
}*/

//Cancelando el job propio
//Antes se tienen dos coroutinas, a una de ellas se asocia el job propio
//Al cancelar el job propio, cancelara todas las coroutinas cuyos jobs sean hijos del myJob
/*
fun main() {
    log("Start")

    val myJob = Job()

    runBlocking {

        val job = launch(myJob) {
            repeat(10) {
                log("Launch rep #$it : I'm active.")
                delay(1000)
            }

            log("Launch : I'm finishing.")
        }

        launch {
            delay(3500)
            log("Before cancelling MyJob.")
            myJob.cancel()
        }

        delay(100)
        while(myJob.isActive) {
            log("RunBlocking : MyJob is active.")
            delay(1000)
        }

        if(job.isCancelled) {
            log("RunBlocking : Job is cancelled")
        }

        if(job.isCompleted) {
            log("RunBlocking : Job is completed")
        }

        if(myJob.isCancelled) {
            log("RunBlocking : MyJob is cancelled")
        }

        if(myJob.isCompleted) {
            log("RunBlocking : MyJob is completed")
        }
    }

    log("End")
}
*/

//Crear otra coroutina al mismo nivel de la coroutina "hija", pero sin asociar esta nueva coroutina al job propio.
//La coroutina nueva no asociado al job propio no tendra que cancelar.
/*
fun main() {
    log("Start")

    val myJob = Job()

    runBlocking {

        val job1 = launch(myJob) {
            repeat(5) {
                log("Launch 1 rep #$it : I'm active.")
                delay(1000)
            }

            log("Launch 1 : I'm finishing.")
        }

        val job2 = launch {
            repeat(5) {
                log("Launch 2 rep #$it : I'm active.")
                delay(1000)
            }

            log("Launch 2 : I'm finishing.")
        }

        launch {
            delay(1500)
            log("Before cancelling MyJob.")
            myJob.cancel()
        }

        delay(100)
        while(myJob.isActive) {
            log("RunBlocking : MyJob is active.")
            delay(1000)
        }

        if(job1.isActive) {
            log("RunBlocking : Job 1 is isActive")
        }

        if(job1.isCancelled) {
            log("RunBlocking : Job 1 is cancelled")
        }

        if(job1.isCompleted) {
            log("RunBlocking : Job 1 is completed")
        }

        if(job2.isActive) {
            log("RunBlocking : Job 2 is isActive")
        }

        if(job2.isCancelled) {
            log("RunBlocking : Job 2 is cancelled")
        }

        if(job2.isCompleted) {
            log("RunBlocking : Job 2 is completed")
        }

        if(myJob.isCancelled) {
            log("RunBlocking : MyJob is cancelled")
        }

        if(myJob.isCompleted) {
            log("RunBlocking : MyJob is completed")
        }
    }

    log("End")
}*/


//Asociar 2 job hijas al job propio y cancelar.
//Ambos jobs hijos fueron concelados
/*
fun main() {
    log("Start")

    val myJob = Job()

    runBlocking {

        val job1 = launch(myJob) {
            repeat(5) {
                log("Launch 1 rep #$it : I'm active.")
                delay(1000)
            }

            log("Launch 1 : I'm finishing.")
        }

        val job2 = launch(myJob) {
            repeat(5) {
                log("Launch 2 rep #$it : I'm active.")
                delay(1000)
            }

            log("Launch 2 : I'm finishing.")
        }

        launch {
            delay(1500)
            log("Before cancelling MyJob.")
            myJob.cancel()
        }

        delay(100)
        while(myJob.isActive) {
            log("RunBlocking : MyJob is active.")
            delay(1000)
        }

        if(job1.isActive) {
            log("RunBlocking : Job 1 is isActive")
        }

        if(job1.isCancelled) {
            log("RunBlocking : Job 1 is cancelled")
        }

        if(job1.isCompleted) {
            log("RunBlocking : Job 1 is completed")
        }

        if(job2.isActive) {
            log("RunBlocking : Job 2 is isActive")
        }

        if(job2.isCancelled) {
            log("RunBlocking : Job 2 is cancelled")
        }

        if(job2.isCompleted) {
            log("RunBlocking : Job 2 is completed")
        }

        if(myJob.isCancelled) {
            log("RunBlocking : MyJob is cancelled")
        }

        if(myJob.isCompleted) {
            log("RunBlocking : MyJob is completed")
        }
    }

    log("End")
}*/

//Cancelar la jeucion de una coroutine creada con async lanzara una excepcion en el momento de hacer llamada a la funcion await.
//NOTA: Al crear una coroutina dentro del scope de otra coroutina, no se deberia especificar un job propio porque eso romperia la relacion padre-hijo entre los job.
//Implicaria que algunas coroutinas no puedas ser canceladas, generando asi fugas de memoria.

/*
fun main() {
    log("Start")

    val myJob = Job()

    log("MyJob: $myJob")

    runBlocking {
        val result = async(myJob) {
            log("Async : I'm starting.")
            val a = (1..50).random()
            log("Async : A = $a")
            delay(1000)
            val b = (1..50).random()
            log("Async : B = $b")
            delay(1000)
            log("Async : I'm finishing.")
            a + b
        }

        launch {
            delay(1500)
            log("Before cancelling MyJob.")
            myJob.cancel()
        }

        try {
            log("RunBlocking : Before calling .await()")
            log("RunBlocking : Result is ${result.await()}")
        }catch(e: Exception) {
            log("RunBlocking : Oops! There was a problem: ${e.message}")
        }

        if(result.isCancelled) {
            log("RunBlocking : Job is cancelled")
        }

        if(result.isCompleted) {
            log("RunBlocking : Job is completed")
        }

        if(myJob.isCancelled) {
            log("RunBlocking : MyJob is cancelled")
        }

        if(myJob.isCompleted) {
            log("RunBlocking : MyJob is completed")
        }
    }

    log("End")
}
*/

//Job no cancelable
//NOTA: cuando cancelamos una coroutina padre, se cancelas recursivamente todas las coroutinas dentro del scope.
//Cuando estamos dentro del scope de una coroutina y creamos una coroutina usando los constructores lauch y async, y le pasamos un Job como parametros, rompemos la relacion padre-hijo

fun main() {
    log("Start")

    runBlocking {

        log("RunBlocking start")

        var parentScopeIsActive = false

        val parentJob = launch (Dispatchers.Default) {
            log("Parent Job start")
            parentScopeIsActive = true

            val childJob = launch (Job()) { //Comment this line
                //val childJob = launch { //Uncomment this line
                log("Child Job start")
                repeat(10) {
                    delay(500)
                    val suf = if(parentScopeIsActive) "It's OK, this should remain active." else "Uh-Oh!, this shouldn't be running."
                    log("Child Job: Imagine this is a long and expensive operation. $suf")
                }
                log("Child Job end")
            }

            log("Parent Job: waiting for child Job to complete.")
            childJob.join()
            log("Parent Job: stopped waiting for child Job.")

            log("Parent Job end")
        }

        launch {
            delay(1500)
            log("Launch: cancelling parent Job")
            parentJob.cancel()
            parentScopeIsActive = false
            log("Launch: Parent Job CANCELLED!!!")
        }

        log("RunBlocking: waiting for parent Job to complete.")
        parentJob.join()
        log("RunBlocking: stopped waiting for parent Job.")

        log("RunBlocking: before delay 5 seconds")
        delay(5000)

        log("RunBlocking end")
    }

    log("End")
}