package Poo


fun main(){
    //Objetos
    //var nombreObjeto:Clase =Inicialicacion
    //Al igual que todas las variables los objetos se inicializan
    /*
    var ferrari:Automovil=Automovil("ferrari", col = "azul", pre = 10)
    ferrari.encender()
    ferrari.precio=10
    println(ferrari.precio)

    var toyota:Automovil= Automovil("toyota","blanco", pre = 100)
    toyota.encender()
    */

    //DataClass
    /*
    var articulo1=Articulo(1,"television",Precio.BARATO)
    println(articulo1)
    println(articulo1.codigo)
    articulo1.codigo=10
    articulo1.precio=Precio.CARO
    println(articulo1)
    println(articulo1.codigo)
     */

    //Objetos nombrados
    println(Auto.color)
    Auto.encender()


}

//Data class
//Permite almacenar datos sin implementar sus funcionalidades
data class Articulo(var codigo:Int, var descripcion:String, var precio:Precio)

//Enun class
//Permite declarar un conjunto de constantes
enum class Precio{
    BARATO,
    JUSTO,
    CARO
}

//Objetos Nombrados
//Es similar a una clase
//Definir un objeto sin la necesidad de utilizar una clase, aveces es necesario solo tener un objeto
object Auto{
    //Atributos
    val color="rojo"
    //Metodos
    fun encender(){
        println("El auto encendio")
    }
}

//Herencia


/*
class Automovil{
    //---Propiedades o caracteristicas---
    var marca:String=""
    var color:String=""
    var precio:Int=0
    var velocidadMaxima:Int=0

    //---Funciones o metodos
    fun encender(){
        println("El auto ha encendido")
    }

    fun apagar(){
        println("El auto se ha apagado" )
    }

    fun aceledar(){
        println("El auto esta acelarando" )
    }

    fun detenerse(){
        println("El auto se ha detenido" )
    }
}
*/
