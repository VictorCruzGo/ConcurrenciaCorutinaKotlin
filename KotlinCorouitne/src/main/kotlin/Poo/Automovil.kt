package Poo

class Automovil ( mar:String, col:String, pre:Int) {
    //---Propiedades o caracteristicas---
    var marca:String=mar
    var color:String=col
    var precio:Int=pre
        //Getter
        get() = field +3
        //Setter
        set(value){
            if (value<100) {
                field=0
            }
        }
    private var velocidadMaxima:Int=0

    //Constructor

    //---Funciones o metodos
    fun encender(){
        println("El auto "+marca+ " ha encendido")
    }

    private fun apagar(){
        println("El auto se ha apagado" )
    }

    fun aceledar(){
        println("El auto esta acelarando" )
    }

    fun detenerse(){
        println("El auto se ha detenido" )
    }

    //Getter y Setter
    fun getVel():Int{
        return velocidadMaxima
    }

    fun setVel(vel:Int){
        velocidadMaxima=vel
    }
}
