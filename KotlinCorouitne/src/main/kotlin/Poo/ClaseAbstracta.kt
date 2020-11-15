package Poo

fun main(){
    val prog=Programadorsito("victorcito", 5000)
    prog.saludar()
    prog.imprimirNombre()
    //val pers=Personita(); //Las clases abstractas no pueden instanciarse.
}

//Clases abstractas
//Ayudan a definir metodos para utilizarlo en otras clases
//Es una clase que permite definir atributos y funciones. No van a tener implementacion
abstract class Personita(val nombre:String){
    abstract fun saludar()
    fun imprimirNombre(){
        println("El nombre es ${nombre}")
    }
}

class Programadorsito(nombre:String, val sueldo:Int):Personita(nombre){
    override fun saludar() {
        println("El programador esta saludando")
    }
}