package org.example

import kotlinx.coroutines.*
import kotlin.coroutines.ContinuationInterceptor
import kotlin.coroutines.CoroutineContext

//ContinuationIntercepto
/*
-Aprender a mover de un lado a otro dentro del conjunto de hilos
-El poder de la coroutina esta en suspender el hilo envez de bloquearlo.
-
*/
/*
fun log(message: String) {
    println("[${Thread.currentThread().name}] : $message")
}

fun log(character: Char) {
    print("$character")
}
*/

//Concurrencia con delay
//Con el delay de genera concurrencia
/*
fun main() {
    log("Start")

    val repetitions = 5

    runBlocking {
        launch {
            repeat(repetitions) {
                log("Launch 1---: $it")
                delay(50)
            }
        }

        launch {
            repeat(repetitions) {
                log("Launch 2>>>: ${repetitions - it}")
                delay(50)
            }
        }
    }

    log("End")
}
 */

//Concurrencia sin delay
//Sin el delay se pierde el efecto de la concurrencia. Esto se debe a que en ningun momento el hilo principal fue suspendido, por lo tanto tuvo que ejecutar secuencialmente.
/*
fun main() {
    log("Start")

    val repetitions = 5

    runBlocking {
        launch {
            repeat(repetitions) {
                log("Launch 1---: $it")
            }
        }

        launch {
            repeat(repetitions) {
                log("Launch 2>>>: ${repetitions - it}")
            }
        }
    }

    log("End")
}
 */

//DISPACHERS
/*
Los dispatchers son un grupo de implementaciones de la clase abstracta CoroutineDispatcher que a su vez
implementa la interface ContinuationInterceptor.

Los dispacher incluyen los siguietne cuatro tipos:
1.Default
-Usado por defecto por lauch y async.
-Los hilos pertenecen a un pool de hilos.

2.IO
-usado para realizar tareas de larga duracion o un poco pesada de naturaleza "bloqueable". Ej. acceder a bd o archivo.
-Los hilos pertenecen a un pool de hilos.

3.Main
-Para los desarrollo de android. Para invocar al hilo principal.
-Por lo general usa un hilo unico.
-No es parte del pool de hilos.

4.Unconfined (No confinado = aislado, recluido, preso)
-Tiene un comportamiento particular. Comienza ejecutandose en el hilo en el que fue creada y continua asi hasta encontrar el primer punto de suspension.
Luego de la suspencion se reanuda en un hilo que es completamente determinado por la funcion suspend qeu fue invocada.
-Es util cuando se hace necesario ejecutar una coroutine inmediatamente despues de haber sido creada para evitar efectos no deseados.
-Tambien es apropiado usarlo en tareas que no consumen recursos computacionales y no acceden a datos compartidos con otros hilos.
-No es recomendable usar de manera generalizada.

Nota
-No existe una regla que nos obligue a usar un hilo especifico o oun pool de hilos.
Por lo que su uso se deja completamente a criterio del desarrollador. Sin envargo se puede establecer ciertas concenciones.

Convensiones:
1.Dispatchers.Main
-Actualizacion en la interfaz de usuario.
-Este hilo simpre debe estar disponible para capturar eventos del usuario como desplazamientos de pantalla, clicc, escritura de text, et.

2.Dispatchers.IO
-Tareas que tengan que ver:
    -consulta o actualizacion a base de datos
    -lectura o escritura de archivo
    -Consumo de servicio externos
-Todas las operaciones que tienen en comun que son de larga duracion y alto consumo de recursos.

3.Disptchers.Unconfined
-El uso de este dispatcher se debe evitar.
-Por lo general lo usara com oexcepcion a la regla.
-Seran muy pocas las veces que lo uses y cuando lo uses tendra absoluta justificacion
-Apropiado usuarl en tarea que no consumen muchos recursos  computacionales y cuando no se acceden a datos compartidos con otros hilos.

4.Dispatchers.Default
-Utiliza cuando no tengas muy claro lo que debes hacer, es decir cuando la tarea no encaja bien en ninguno de los otros tipos de coroutineDispacher
-Es el coroutine mas utilizado, al no lograr encajar el perfil de tarea en ninguno de los otros tipos.
-Por lo general hacemos muchos calculos, ejecutamos muchos algoritmos y realizamos otras tareas que no tienen que ver con el almacenamiento d datos, consumo de servicios.
*/


//Dispatcher default
//Cada coroutine se ejecuta en un hilo diferente correspondiente al pool de hilos que utiliza el Dispatcher.Default
//Al reanudar la ejecucion, no necesariamente sera el mismohilo usado antes de haber sido suspendida.
// Es posible que la reanudacion sea ejecutada en otro hilo. Siempre y cuando pertenesca al mismo pool de hilos.
/*
fun main() {
    log("Start")

    val repetitions = 5

    runBlocking {
        launch (Dispatchers.Default) {
            repeat(repetitions) {
                log("Launch 1---: $it")
                delay(50)
            }
        }

        launch (Dispatchers.Default) {
            repeat(repetitions) {
                log("Launch 2>>>: ${repetitions - it}")
                delay(50)
            }
        }
    }

    log("End")
}
*/


//Eliminar los delay
//Se conserva la concurrencia
//Ademas no lo hace en el mismo orden *(El orden de ejecucion es administrada por la maquina virtual)
/*
fun main() {
    log("Start")

    runBlocking {
        val repetitions = 10

        launch (Dispatchers.Default) {
            repeat(repetitions) {
                log("Launch 1---: $it")
            }
        }

        launch (Dispatchers.Default) {
            repeat(repetitions) {
                log("Launch 2>>>: ${repetitions - it}")
            }
        }
    }

    log("End")
}*/


//Dispacher IO
//El resultado de la ejecucion es igual al ejemplo anterior.
//al ser la maquina virutal la que administra el pool de hilos, usa el mismo pool de hilos para las dos coroutines
/*
fun main() {
    log("Start")

    runBlocking {
        val repetitions = 5

        launch (Dispatchers.Default) {
            repeat(repetitions) {
                log("Launch 1---: $it")
            }
        }

        launch (Dispatchers.IO) {
            repeat(repetitions) {
                log("Launch 2>>>: ${repetitions - it}")
            }
        }
    }

    log("End")
}
*/

//Unconfined
//No se recomienda usar este CoroutineDispacher de manera generalizada.
//Se ejecuta en el hilo que fue creado hasta encontrar el primer punto de suspension.
//Como no hay punto de suspension, todod se ejecuta en el hilo principal hasta finalizar.
/*
fun main() {
    log("Start")

    runBlocking {
        val repetitions = 5

        launch (Dispatchers.Unconfined) {
            repeat(repetitions) {
                log("Launch 1: $it")
            }
        }
    }

    log("End")
}
 */

//Unconfined con delay
//Continua se ejecucion en otro hilo despues de haber reanudado la tarea luego de haber sido suspendida.
/*
fun main() {
    log("Start")

    runBlocking {
        val repetitions = 5

        launch (Dispatchers.Unconfined) {
            repeat(repetitions) {
                log("Launch 1: $it")

                if(it == 2) {
                    log("Launch 1: Before suspending coroutine...")
                    delay(50)
                    log("Launch 1: After suspending coroutine...")
                }
            }
        }
    }

    log("End")
}
*/

//Main
//Lanza excepcion. Utilizado solo para androind
/*
fun main() {
    log("Start")

    runBlocking {
        val repetitions = 5

        launch (Dispatchers.Main) {
            repeat(repetitions) {
                log("Launch 1: $it")
            }
        }
    }

    log("End")
}*/

//Coroutine dentro de otra coroutine, con contexto vacio
//La coroutina padre que contiene a la hija se mantiene en en el estado completing en espera de que la coroutina hija termine su ejecucion.
//La corutina hija usa la misma coroutinadispacher que el padre que la contiene.
//La coroutina hija usara cualquier hilo perteneciente al pool de hilos del Dispatcher.Default

/*
fun main() {
    log("Start")

    runBlocking {

        launch (Dispatchers.Default) {

            log("Parent-Before | ContinuationInterceptor: ${this.coroutineContext[ContinuationInterceptor]}")

            launch {
                log("Child | ContinuationInterceptor: ${this.coroutineContext[ContinuationInterceptor]}")
            }

            log("Parent-After | ContinuationInterceptor: ${this.coroutineContext[ContinuationInterceptor]}")

        }

    }

    log("End")
}
*/


//Coroutine madre espera a que finalice la ejecucion de la coroutine hija haciendo uso de la funcion joing
//La coroutine hija reaudara usando cualquier hilo del pool de hilos de la madre
/*
fun main() {
    log("Start")

    runBlocking {

        launch (Dispatchers.Default) {

            log("Parent-Before | ContinuationInterceptor: ${this.coroutineContext[ContinuationInterceptor]}")

            val job = launch {
                log("Child | ContinuationInterceptor: ${this.coroutineContext[ContinuationInterceptor]}")
            }

            job.join() //Suspend de la funcion

            log("Parent-After | ContinuationInterceptor: ${this.coroutineContext[ContinuationInterceptor]}")

        }

    }

    log("End")
}
*/

//Coroutine hija con un dispacher diferente a la coroutina madre
//Al ver el dispatcher IO la maquina virutla usa un pool de hilos. usa el mismo pool de hilos del dispatcher default
/*
fun main() {
    log("Start")

    runBlocking {

        launch (Dispatchers.Default) {

            log("Parent-Before | ContinuationInterceptor: ${this.coroutineContext[ContinuationInterceptor]}")

            val job = launch (Dispatchers.IO) {
                log("Child | ContinuationInterceptor: ${this.coroutineContext[ContinuationInterceptor]}")
            }

            job.join()

            log("Parent-After | ContinuationInterceptor: ${this.coroutineContext[ContinuationInterceptor]}")

        }

    }

    log("End")
}
*/

//SINGLETHREAD Y FIXEDTHREADPOOL
//Las coroutinas nos dan la posibilidad de que les asignemos un hilo creado por nosostros, o bien un pool de hilos creados que disponemos por defecto.
//NOTA: Bastara con lo hilos que disponemos por defecto y lo que los diferentes dispacher proveen.


//newSingleThreadContext
//Se crea un hilo al que se le asigna como nombre el String enviado como parametro.
//NOTA: La creacion de un hilo es muy cara a nivel de recursos computacionales por lo que debes ser responsable en su uso y su liberacion.
//Con esto quiero decir que deber mantene una referencia al hilo para tenere la posisbilidad de reutilizarlo, o bien , para liberarlo cuano ya no lo necesites.

/*
fun main() {
    log("Start")

    runBlocking {

        launch (newSingleThreadContext("MyThread")) { //Se crea un hilo

            repeat(5) {
                log("Launch: $it")
                delay(50)
            }

        }

    }

    log("End")
}
*/

//newFixedThreadPoolContext
//Creacion de un Pool de hilos
//Se crearon 10 hilos con el nombre MyThreadPool
//Observar despues de cada reanudacion (delay) cambia de hilo
//NOTA: Recuerda mantener una referencia al hilo o pool de hilos para liberar recursos. En los ejemplos no lo estamos haciendo.

/*
fun main() {
    log("Start")
    runBlocking {
        launch (newFixedThreadPoolContext(10, "MyThreadPool")) {
            repeat(5) {
                log("Launch: $it")
                delay(500)
            }
        }
    }
    log("End")
}
*/

//Single Thread
//Single thread con suspencion (join) y librecion de recursos (close)
/*
fun main() {
    log("Start")
    runBlocking {
        val myThreadContext = newSingleThreadContext("MyThread")
        val job = launch (myThreadContext) {
            repeat(5) {
                log("Launch: $it")
                delay(50)
            }
        }
        job.join()
        myThreadContext.close()
    }
    log("End")
}
*/

//Thread pool
//Thread pool con suspencion (join) y liberacion de recursos (close)
/*
fun main() {
    log("Start")
    runBlocking {
        val myThreadPoolContext = newFixedThreadPoolContext(10, "MyThreadPool")
        val job = launch (myThreadPoolContext) {
            repeat(5) {
                log("Launch: $it")
                delay(50)
            }
        }
        job.join()
        myThreadPoolContext.close()
    }
    log("End")
}
*/

//Como cambiar de hilo sin tener que crear una nueva coroutina
//WhitContext cambia de hilo
//Para cambiar de una coroutina a otra tenemos una suspend function withContext, funciona igual a Runblocking pero sin crear una nueva coroutina
//En el codigo usamos un contexto con un hilo propio llamada "MyOuterThread", se inicia la ejecucion de la coroutina y saltamos
//a otro hilo llamada MyInnerThread usando la funcion withcContext. Al finalizar vuelve al hilo MyOuterThread
//Cabe mencionar que en caso de haber usado un pool de hilos en RunBlocking, al hacer uso de WithContext se suspenderia
//el hilo reanundando su ejecucion en uno de los hilos pertenecientes a ese pool.

/*
fun main() {
    log("Start")
    runBlocking(newSingleThreadContext("MyOuterThread")) {

        log("RunBlocking: Step 1")

        //Cambia de hilo a MyInnerThread
        withContext(newSingleThreadContext("MyInnerThread")) {
            log("WithContext singleThread 1: Starting...")
            delay(1000)
            log("WithContext singleThread 1: Finishing...")
        }
        log("RunBlocking: Step 2")
        //Cambia de hilo a Default
        withContext(Dispatchers.Default) {
            log("WithContext 2 dispatchers : Starting...")
            delay(1000)
            log("WithContext 2 dispatchers: Finishing...")
        }
        log("RunBlocking: Step 3")
    }
    log("End")
}
*/


//Construccion del coroutineContext con Job + ContinuationInterceptor (Dispatcher)
/*
fun main() {
    log("Start")
    val myCoroutineContext = Job() + Dispatchers.Default
    log("MyCoroutineContext: $myCoroutineContext")

    runBlocking {
        log("RunBlocking ||||| " +
                "Job: ${this.coroutineContext[Job]} , " +
                "ContinuationInterceptor: ${this.coroutineContext[ContinuationInterceptor]} , " +
                "CoroutineExceptionHandler: ${this.coroutineContext[CoroutineExceptionHandler]} , " +
                "CoroutineName: ${this.coroutineContext[CoroutineName]}")

        launch(myCoroutineContext) {
            log("Launch |>>> " +
                    "Job: ${this.coroutineContext[Job]} , " +
                    "ContinuationInterceptor: ${this.coroutineContext[ContinuationInterceptor]} , " +
                    "CoroutineExceptionHandler: ${this.coroutineContext[CoroutineExceptionHandler]} , " +
                    "CoroutineName: ${this.coroutineContext[CoroutineName]}")

            delay(1000)
            log("Launch : I'm finishing.")
        }

        launch {
            delay(5500)
            log("Before cancelling MyCoroutineContext.")
            myCoroutineContext.cancel()
        }

        delay(100)
        while(myCoroutineContext.isActive) {
            log("RunBlocking : MyCoroutineContext is active.")
            delay(1000)
        }
        log("RunBlocking : MyCoroutineContext is not active.")
    }
    log("End")
}
*/

//Uso correcto de un contexto propio construido con Job + Continuation Interceptor

//Creacion de un contexto para ser usado de manera global dentro del objeto. Cuando desechamos el objeto simplement cancelamos el job
//asociado a ese contexto global del objeto, lo que provocaria que todas la coroutinas en ejecucion se detengan automaticamente

//ANALIZAR: https://medium.com/kotlin-en-android/coroutines-con-kotlin-continuationinterceptor-8bfb72e43180

//Al implementar la interface CoroutineScope englobamos la clase dentro del Scope de una coroutine y nos obliga a construir su correspondiente contexto.
class MyObject: CoroutineScope {

    //Engloaba todos los metodos y propiedades estaticas
    companion object {
        const val WAITING_TIME_MILLIS: Long = 5000
        //const val WAITING_TIME_MILLIS: Long = 2500
    }

    //  Construir su conrrespondiente contexto
    //Da la libertad de crear coroutinas como querramos ya que cada coroutina esta asociada por defecto a este contexto
    private val job = Job()
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Default

    private fun notifyUI(number: Int) {
        log("MyObject: UI has been notified with number $number")
    }

    fun simulateLongTask() {
        val myMainThread = newSingleThreadContext("MyMainThread")
        //La coroutina estara asociada al contexto global de la clase
        val updaterJob = launch {
            (1..Int.MAX_VALUE).forEach {
                delay(1000)
                log("MyObject: Task 1 longtaks | Before notifying UI with number $it")
                //Para cambiar a un contexto que usara un hilo diferente sin romper la relacion padre e hijo
                withContext(myMainThread) {
                    //withContext(Dispatchers.Main) { //Use this CoroutineDispatcher on Android
                    notifyUI(it)
                }
                log("MyObject: Task 1 longtask | After UI notification with number $it")
            }
        }

        updaterJob.invokeOnCompletion {_ ->
            log("MyObject: Task 1 longtask | Closing dedicated thread")
            //myMainThread.close() //Line commented because of lack of permissions
            log("MyObject: Task 1 longtask | Dedicated thread closed!")
        }
    }

    fun simulateDataRetrievalAsync() = async<Int> {
        val repetitions = 10
        repeat(repetitions) {
            delay(300)
            log("MyObject: Task 2 dataretrival | Simulating data retrieval... ${(it+1) * 100 / repetitions}%")
        }

        (1..Int.MAX_VALUE).random()
    }

    //Cancela el Job y debe ser invocada al desechar el objeto para detener todas las coroutinas creadas dentro de la clase.
    fun release() {
        log("MyObject: Releasing resources...")
        this.job.cancel()
        log("MyObject: Resources released!!!")
    }

}



fun main() {
    log("Start")
    val myObject = MyObject()
    runBlocking {
        log("RunBlocking start")
        myObject.simulateLongTask()
        val data = myObject.simulateDataRetrievalAsync()

        launch {
            try {
                log("Launch 1 ---: Data retrieved [ ${data.await()} ]") //Esperando obtener un valor.
            } catch (e: Exception) {
                log("Launch 1 --- Catch [ ${e.message} ]")
            }
        }

        val job = launch {
            log("Launch 2 >>>: waiting for ${MyObject.WAITING_TIME_MILLIS / 1000} seconds before releasing resources")
            delay(MyObject.WAITING_TIME_MILLIS) //----------Antes de cancelar el job espera 5 segundos
            log("Launch 2 >>>: Releasing 'MyObject'")
            myObject.release()
        }

        job.join() //suspende el job hasta que finalice
        log("RunBlocking: waiting for 3 seconds to make sure 'MyObject' has been released. All coroutines should have stopped by now.")
        delay(3000) //Espera 3 segundos para asegurar que haya liberado MyObjecto. Todas las coroutinas ya deberian haberse detenido
        log("RunBlocking end")
    }
    log("End")
}

