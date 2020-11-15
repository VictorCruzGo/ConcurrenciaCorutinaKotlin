package org.example

import kotlinx.coroutines.*
import kotlin.concurrent.thread
import kotlin.system.measureTimeMillis

/*
Bloquear
-Bloque el hilo en ejecucion. Ej. bloquer el thread main. Ej. hacer una peticion a un servicio rest.

Suspender
-No bloque la ejecucion padre.
-Permite suspender la ejecucion de una coroutina.
-Se puede pausar y reaunadar mas adelante.

Constructores de coroutinas
1.RunBloquing
-Ejecuta una nueva coroutina y bloquea el hilo actual hasta su finalizacion.
2.Launch
-Lanza una nueva coroutina y devuelve un referencia de la clase job.
-La coroutina puede ser cancelada cuando el job es cancelado.
3.Async
-Permite ejecutar algo y esperar el resultado.
-Devuelve un deferred.
-Es como escribir codigo asincrono que realmente parece sincrono.
-Ejecutar codigo y esperar un resultado, utilizar async caso contrario lauch.
-Para operaciones en background.

Deferred
-Async devuelve un deferred que es un futuro cancelable sin bloqueos. Es un futuro porque vamos a obtener un valor en el futuro
-Es como un job que devuelve un valor.

Dispacher
-El despachador de la coroutina, viene a decir en que hilo se va a ejecutar la coroutina
-Se puede saber en que hilo ejecutar la coroutina

GlobalScope
-GlobalScope: ayuda a definir el ciclo de vida de la coroutina. Puede abarcar toda la aplicacion o solo un determinado componente (Activity, fragment)
-Una coroutina tiene que tener siempre asociado un scope.
-Operan durante toda la vida util de la aplicacion. Tanto dure la aplicacion sera la vida util de la corutina.No se recomienda utilizar GlobalScope

Job
-Es un elemento cancelable con un ciclo de vida que culmina a su finalizacion.
-A diferencia de Deferred, no produce ningun valor como resultado.
-Launch devuelve un job

*/

//https://www.youtube.com/watch?v=IPUE2cSi6P8

fun main(){
    //blockinExample()
    //suspendExample()
    //dispacher()
    //launch()
    //exampleJob()
    //asyncAwait()
    //asyncAwaitDeferrec()
    //Thread.sleep(5000)
    println(measureTimeMillis { asyncAwait() }.toString())
    println(measureTimeMillis { asyncAwaitDeferrec() }.toString())
}

fun longTaskWithMessage(message: String){
    Thread.sleep(4000)
    println(message + Thread.currentThread().name)
}

suspend fun blockinExample(){
    println("tarea 1:"+ Thread.currentThread().name)
    //longTaskWithMessage("tarea 2:")
    delayCoroutine("tarea 2")
    println("tarea 3:"+ Thread.currentThread().name)
}

suspend fun delayCoroutine(message:String){
    delay(4000)
    println(message+" "+ Thread.currentThread().name)
}

fun suspendExample(){
    println("tarea 1:"+ Thread.currentThread().name)
    runBlocking {
        delayCoroutine("tarea 2")
    }
    println("tarea 3:"+ Thread.currentThread().name)
}

//Otras forma de ejecutar runBlocking
fun suspendExample2()= runBlocking{
    println("tarea 1:"+ Thread.currentThread().name)
    delayCoroutine("tarea 2")
    println("tarea 3:"+ Thread.currentThread().name)
}

//Si estamos en el main se ejecuta en el main, en el hilo que este.
fun dispacher(){
    runBlocking {
        println("HIlo en que se ejecuta 1: ${Thread.currentThread().name}")
    }
    //Sino importa el hilo a ejecutar. No importa si el Hilo se ejecuta en un hilo de entrada o salida de dato
    runBlocking (Dispatchers.Unconfined) {
        println("HIlo en que se ejecuta 2: ${Thread.currentThread().name}")
    }
    //Se utiliza para la operaciones que hacen un uso intensivo del cpu
    runBlocking(Dispatchers.Default) {
        println("HIlo en que se ejecuta 3: ${Thread.currentThread().name}")
    }
    //Se utiliza para operaciones de entrada y salida de datos. Ej. base de datos, web service
    runBlocking(Dispatchers.IO) {
        println("HIlo en que se ejecuta 4: ${Thread.currentThread().name}")
    }
    //Se utilizar para ejecutar en un hilo propio
    runBlocking(newSingleThreadContext(name = "MiHilo")) {
        println("HIlo en que se ejecuta 5: ${Thread.currentThread().name}")
    }
    //En android se puede utilizar sin problemas. En Intellig comentar
    /*
    runBlocking(Dispatchers.Main) {
        println("HIlo en que se ejecuta 5: ${Thread.currentThread().name}")
    }
     */
}

fun launch(){
    println("tarea 1:"+ Thread.currentThread().name)
    //Siempre va asociado a un GlobalScope
    //GlobalScope: ayuda a definir el ciclo de vida de la coroutina. Puede abarcar toda la aplicacion o solo un determinado componente (Activity, fragment)
    //Una coroutina tiene que tener siempre asociado un scope.
    //Operan durante toda la vida util de la aplicacion. Tanto dure la aplicacion sera la vida util de la corutina.No se recomienda utilizar GlobalScope
    GlobalScope.launch(){
        delayCoroutine("tarea 2")
    }
    println("tarea 3:"+ Thread.currentThread().name)
}

fun exampleJob(){
    //Es un elemento cancelable con un ciclo de vida que culmina a su finalizacion.
    //A diferencia de Deferred, no produce ningun valor como resultado.
    //Launch devuelve un job
    println("tarea 1:"+ Thread.currentThread().name)
    val job= GlobalScope.launch {
        delayCoroutine("tarea 2")
    }
    println("tarea 3:"+ Thread.currentThread().name)
    job.cancel() //Cancela la coroutina.
}


/*
Async
//Permite ejecutar algo y esperar el resultado.
//Devuelve un deferred
*/

suspend fun calculteHard():Int{
    delay(2000)
    return 15
}

suspend fun calculteHard2():Int{
    delay(2000)
    return 15
}

fun asyncAwait()= runBlocking{
    //Se ejecuta secuencialmente pero asyncrono
    //En total toda la ejeuccion tarda 4000ms.
    //println(System.currentTimeMillis().toString())
    val numero1:Int=async {calculteHard2()}.await()
    //println(System.currentTimeMillis().toString())
    val numero2:Int=async {calculteHard()}.await()
    //println(System.currentTimeMillis().toString())
    val resultado=numero1+numero2
    println(resultado.toString())
}

fun asyncAwaitDeferrec()= runBlocking {
    //Se ejecuta en paralelo
    //println(System.currentTimeMillis().toString())
    val numero1:Deferred<Int> =async { calculteHard()}
    //println(System.currentTimeMillis().toString())
    val numero2:Deferred<Int> =async { calculteHard()}
    //println(System.currentTimeMillis().toString())
    val resultado:Int=numero1.await()+numero2.await()

    println(resultado.toString())
}
