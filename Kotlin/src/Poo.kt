import clases.Frutas

fun main(){
    var manzana=Frutas("rojo","dulce",10)
    println("Mostrando datos del objeto:")
    println(manzana.color)
    println(manzana.sabor)
    println(manzana.precio)
    manzana.pudrirse()
}

