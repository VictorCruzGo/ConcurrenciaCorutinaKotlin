package org.example

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import org.intellij.lang.annotations.Flow.*
import kotlin.system.measureTimeMillis


/*
Flow.
-Es un asincronos data stream que se ejecuta de manera secuencial en la misma coroutina.
-Son paracidos a los Stream de java. Con la diferencia que no hay que preocupar de los memories leak
-Flow es el stream data. Es revolucionario al escribir stream data.
-Se llamas flow cold porque sino se llama al collect no se ejecutan
-Las funciones suspendidad asincronas devuelven un solo valor.
-Que pasa si nos devuelven multiples valores, pasado varios segundos
*/


fun main(){
    //show()
    /*
    runBlocking {
        runAsynchronous().forEach { i -> println(i) }
    }*/
    /*
    runBlocking{
        GlobalScope.launch {
            for (j in 1..3) {
                println("No estoy bloqueado $j")
                delay(1000)
            }
        }
        firtflow().collect{value -> println(value)}
    }
    */


    //Flow Cold Stream
    //flow cold
    //Se llama porque se ejecuta al final de la instruccion al llamar a collect
    //Flow es el stream data. Evita los memorie leak

    /*
    runBlocking {
         println("Llamando flow...")
         var flow:Flow<Int> = firtflow()
         println("Collect...")
         flow.collect{value-> println(value)}
         println("Collect again...")
     }*/


    //Cancelar el Flow
    /*Cancela el Flow despues de 2.5 segundos*/
    /*
    runBlocking {
        withTimeoutOrNull(2500){
            firtflow().collect { value -> println(value) }
        }
        println("Finalizado")
    }
    */

    //Segundo Constructor Flow
    /*
    runBlocking {
        secondFlow().collect { value -> println(value) }
    }*/

    /*
    runBlocking {
        thirdFlow().collect { value -> println(value)}
    }
    */

    //Map
    /*
    runBlocking {
        (1..3).asFlow()
                .map { request-> perfomrRequest(request)}
                .collect { response-> println(response)}
    }*/

    //Filter
    //Permite filtrar de un flow los valores a coger
    //Los operadores a la hora de trabajar con flow nos dan total libertad
    /*
    runBlocking {
        (1..3).asFlow()
                .filter {request -> request>1}
                .map{ request-> perfomrRequest(request) }
                .collect { response -> println(response) }
    }*/

    //Transform
    //Es el operador mas general. Puede imitar a los operadores map y filter.
    //Ej. Recibimos un entero y luego se transforma
    /*runBlocking {
        (1..3).asFlow()
                .transform {
                    request->emit("Making request $request")
                    emit(perfomrRequest(request))
                }
                .collect {
                    response->println(response) //Aqui recien imprime las transformaciones
                }
    }*/

    //Take
    //Cancela el flow cuando se alcanza un limite
    //Con los operadores de flow podemos hacer todas las operaciones intermedias que queramos.
    /*
    runBlocking {
        (1..3).asFlow()
                .take(2)
                .collect { response->println(response)}
    }*/

    //Operadores Terminal
    //toList
    //Pasar un flow a una lista
    /*runBlocking {
        var lista: List<Int> = (1..3).asFlow().toList()
        println(lista)
    }*/

    //first
    //Devuelve el primer elemento
    /*
    runBlocking {
        var numero:Int=(6..90).asFlow()
                .first()
        println(numero)
    }*/

    //Reduce
    //Operador terminal.
    //Reduce los elementos del flow
    /*
    runBlocking {
        var resultado:Int=(1..3).asFlow()
                .reduce{ a,b ->a+b}
        println(resultado)
    }*/

    //Flow secuencial
    //El flow es secuencial, si la primera instruccion filtra elementos, los siguientes son de esos elementos.
    /*
    runBlocking {
        (1..5).asFlow()
                .filter {
                    i-> println("---Filtrado $i") //Por cada elemento del flow imprime Filtrado
                    i%2==0//Si se cumple la condicion se ejecutara la siguiente instruccion
                }
                .map{
                    i->println("---Map $i")
                    "String $i"//Se devuelve "String i"
                }
                .collect{ i-> println("--Collect $i")} //Imprimer Collect "String i"
    }
    */

    //Buffer
    /*
    runBlocking {
        val time:Long= measureTimeMillis {
            firtflow()
                    //.buffer() //Quiere decir que se van a emitir cada 1000 ms cada elemento del flow y se van almacenar en buffer.
                    .collect{
                value -> delay(300) //a cada elemento del flow retarda 300 ms
                println(value)
            }
        }
        println("$time ms")
        //Sin buffer()=3938
        //Con buffer()=3396
    }*/

    //Conflate (combinar)
    //Procesa solo el primero y ultimo debido al delay del collect (300ms) es mayor al delay del firstflow (100ms)
    //Permite fusionar.
    //Util para mostrar datos en tiempo real.
    //El valor 1 y 2 no se procesarian. Directamente se procesario el 3
    /*runBlocking {
        val time:Long= measureTimeMillis {
            firtflow()
                    .conflate()//Muestra el 1 y directamente pasa al 3 sin procesar el delay()
                    .collect{
                        value -> delay(300) //a cada elemento del flow retarda 300 ms
                        println(value)
                    }
        }
        println("$time ms")
        //Sin buffer()=3938
        //Con buffer()=3396
        //Con conflate()=3395
    }
    */

    //CollectLastet
    //Operador terminal
    //Al incluir un delay en el collectLatest permite imprimir solo el ultimo elemento.
    //En funcion del delay() se desechan los elementos menores al tiempo establecido
    /*
    runBlocking {
        val time:Long= measureTimeMillis {
            firtflow()
                    .collectLatest {
                        value->println("Collecting $value")
                        delay(300) //a cada elemento del flow retarda 300 ms
                        println("Finalizado $value")
                    }
        }
        println("$time ms")
    }
    */

    //Operador Zip
    //Combina los valores correspondiente de dos flujos
    /*
    val nums: Flow<Int> = (1..3).asFlow()
    val strs: Flow<String> = flowOf("Uno","Dos","Tres" )
    runBlocking {
        nums.zip(strs){
            a,b->"Zip: $a : $b"
        }.collect { print(it) }
    }
    */

    //Flattening flow
    //Secuencias de valores recibidas de forma asincrona

    /*
    runBlocking {
        //Para poder procesar deberia ser un flujo y no un flujo de flujo. Sera dificil de procesar.
        //Cada valor de flujo encadene otra secuencia de valores
        var ejemplo:Flow<Flow<String>> = (1..3).asFlow().map{requestFlow(it)}
    }*/

    //FlatMapContact
    //Procesa un flujo de flujos
    //Espera que se complete el flujo interno antes de comenzar a recopilar los datos del flujo siguiente.
    //Procesa los flows desde adentro hacia afuera.
    runBlocking {
        val starTime:Long=System.currentTimeMillis()
        (1..3).asFlow().onEach{ delay(100)}
                .flatMapConcat{requestFlow(it)}
                .collect { value-> println("$value at ${System.currentTimeMillis()-starTime} as for start") }

    }

}

fun requestFlow(i:Int): Flow<String> = flow {
    emit("--$i first--") //Emitir un primer valor
    delay(500) //Esperamos
    emit("--$i--") //Emitir un segundo valor
}

fun show(){
    //listar().forEach { i->println(i) }
    secuencia().forEach { i -> println(i) }
}
fun listar():List<Int> = listOf(3,78,90)

fun secuencia():Sequence<Int> = sequence{
    //Codigo secuencial
    //Bloquea el hilo principal
    for(i in 1..3){
        Thread.sleep(1000) //Operacion de larga duracion
        yield(i) //Enviar cada valor
    }
}

suspend fun runAsynchronous():List<Int>{
    //Devuelve los valores de la lista de forma asincrona sin bloquear el hilo principal
    return  runBlocking {
        delay(3000)
        return@runBlocking listOf(1,2,3)
    }
}

/*
-Al llamar al metodo collect se empieza a lanzar
-El bucle puede ser infinito. Cada segundo se puede estar haciendo cualquier accion en segundo plano.
-Flow es el stream data
*/

fun firtflow(): Flow<Int> = flow{
    for(i in 1..3){
        delay(100)
        emit(i)
    }
}

fun secondFlow(): Flow<Int>{
    return flowOf(1,2,3)
}

fun thirdFlow(): Flow<Int>{
    return (1..3).asFlow()
}

//Map
//Convierte un objeto flow de un objeto a otro tipo objeto flow
//Ej. Convierte un flow de tipo entero a un flow del tipo string
suspend fun perfomrRequest(request: Int):String{
    delay(1000)
    return "response $request"
}