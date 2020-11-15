package clases

//class Frutas (color:String, sabor:String, precio:Int){
//La clase Frutas es final
//La clase open frutas es publica
open class Frutas {
    //Propiedades
    //var: lectura/escritura; val:lectura
    var color: String = ""
    var sabor: String = ""
    var precio: Int = 0


    //inicializacion
    //Se puede utilizar para inicializar valores
    /*
    init {
        this.color=color
        this.sabor=sabor
        this.precio=precio
    }
    */

    //Constructores

    constructor(color: String, sabor: String, precio: Int){
        this.color = color
        this.sabor = sabor
        this.precio = precio
    }

    constructor(color: String){
        this.color = color
    }

    constructor()

    //metodos
    fun pudrirse() {
        println("La fruta se esta pudriendo")
    }
}


class FrutasConGrasa: Frutas {
    var cantidadGrasa:Int=0

    constructor(grasa:Int){
        this.cantidadGrasa=grasa
    }
}
