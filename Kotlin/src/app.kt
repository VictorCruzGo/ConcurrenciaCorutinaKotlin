import clases.Frutas
import clases.FrutasConGrasa

fun main(){
    var manzana=Frutas("rojo","amargo",10)
    var banana=Frutas("amarillo")
    println(manzana.sabor)
    println(banana.color)
    println(banana.sabor)
    banana.pudrirse()

    var aguacate=FrutasConGrasa(10)
    println(aguacate.cantidadGrasa)
    aguacate.color="verde"
    println(aguacate.color)
    aguacate.pudrirse()
}