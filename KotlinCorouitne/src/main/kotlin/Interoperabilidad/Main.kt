package Interoperabilidad

fun main(){
    //objeto en kotlin
    val moto=Moto()
    println(moto.nombre)
    println(moto.llantas)
    moto.saludar()

    //objeto en java
    val carro=Carro()
    println(carro.nombre)
    println(carro.llantas)
    carro.saludar()
}