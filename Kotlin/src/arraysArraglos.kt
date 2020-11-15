import java.util.Arrays

fun main(args: Array<String>){
    var i=1
    //se puede escribir de manera explicita o implicita
    var arreglo:Array<Int> = arrayOf(1,2,3)
    println(arreglo[2])

    var arregloCaracteres:Array<String> = arrayOf("Victor","Sega","Rocky","Balboa")
    println(arregloCaracteres[1])

    //Se recupera los parametros de los argumentos
    println(args[2])

    //Arreglo de enteros
    var arregloEnteros= intArrayOf(10,20,30)
    println(arregloEnteros[1])
}