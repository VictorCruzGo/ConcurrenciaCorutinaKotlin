package org.example

import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

/*
-Estos constructores son: runBlocking, launch, async y produce
-Es posible crear coroutines dentro de otras sin limitaciones
*/

//RUNBLOCKING
/*
-Crea una corutine y suspende el hilo que lo ejecuta hasta que la corutina finalice.
-Se recomienda nunca utilizar, expectp para pruebas unitarias
*/
/*
fun main(){
    log("start")
    runBlocking {
        log("Before delay")
        delay(1000)
        log("After delay")
    }
    log("end")
}
*/

//LAUNCH
/*
-Se utilizan para tareas que no requieren devolucion de ningun valor
-Se puede llamar solamente dentro de una corutina o funcion suspend
*/
/*
fun main() {
    log("Start")
    runBlocking {
        log("start of runblocking")
        launch {
            log("before delay")
            delay(1000)
            log("after delay")
        }
        log("end of runblocking")
    }
    log("end")
}
*/

//ASYNC
/*
-Crea una corutina devolviendo un objeto de itpo Deferred<T> siendo T el tipo de dato esperado
-Se puede llamar dentro de una corutina o function suspend
-Deferred es un envoltorio que va a contener el dato requiedo al finalizar la ejecucion de la corutina.
-Para acceder al dato de deferred sera necesario hacer una llamada a la funcion await que provee el objeto deferred.
-Deferred se asemeja a un promise de javascript o a un future de java.
-Async no bloque ni suspende la ejecucion del hilo que crea la corutina
*/

/*
fun main(){
    log("start")
    runBlocking {
        log("start of runblocking")
        val number= async {
            log("----before delay")
            delay(1000)
            log("----after delay")
            (1..1000).random()
            //123
        }
        log("continue with runblocking...")
        log("--->>>end of runblocking with value ${number.await()}")
    }
    log("end")
}
*/


//PRODUCE
/*
-Crea un corutina que se utiliza para la comunicacion por medio de canales con otras corutinas.
*/

fun main(){
    println("hola victor gomez")
}
