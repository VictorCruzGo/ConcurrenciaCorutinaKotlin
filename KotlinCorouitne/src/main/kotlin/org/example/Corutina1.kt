package org.example


import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/*
fun log(message: String){
    println("[${Thread.currentThread().name}]: $message")
}

fun log(character: Char){
    print("$character")
}
*/
/*
fun main() {
    log("Start")
    GlobalScope.launch {
        //Se crea por defecto en un hilo trabajador (worker thread o background thread (hilo de fondo))
        Thread.sleep(1000)
        log("After Thread.sleep") //(interno)
    }
    //Solucion para mostrar el mensaje (interno)
    //Thread.sleep(2000)

    log("End")
}
*/


/*
* Cambiar sleep por delay fuera del GlobalScope generaria un error puesto que delay es una suspend function que
* debe ser llamada dentro de una corutina.
* */
fun main() {
    log("Start")
    GlobalScope.launch {
        delay(1000)
        log("After Thread.sleep") //(interno)
    }
    Thread.sleep(2000)

    log("End")
}