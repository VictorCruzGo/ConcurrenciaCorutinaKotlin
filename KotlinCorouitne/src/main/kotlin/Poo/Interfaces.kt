package Poo

import org.omg.CORBA.NO_MEMORY

//Interfaces
//No implementan metodos ni constructores

fun main(){
    val prog=Programadorcito("Grace", 1000)
    prog.saludar()
    prog.imprimirNombre()
}

interface IPersona{
    fun saludar()
    fun imprimirNombre()
}

class Programadorcito (nombre:String, val sueldo:Int):IPersona{
    override fun saludar() {
        println("El programador esta saludando")
    }

    override fun imprimirNombre() {
        println("El programador se llama programador")
    }
}