package Poo

fun main(){
    val per1=Persona("victor", 26)
    per1.imprimirEdad()
    per1.imprimirNombre()

    val pro1=Programador("ruben", 34, 5000)
    pro1.imprimirNombre()
    pro1.imprimirEdad()
}

//Herencia
//Open, da el permiso de heredar
open class Persona(val nombre:String, val edad:Int){
    open fun imprimirNombre(){
        println("El nombre de la persona es:"+nombre)
    }

    open fun imprimirEdad(){
        println("La edad de la persona es:"+edad)
    }
}

class Programador(nombre:String, edad:Int, val sueldo:Int):Persona(nombre, edad){

    //override funcion con open (abierto)
    override fun imprimirNombre(){
        println("El nombre del programador es:"+nombre)
    }
}

