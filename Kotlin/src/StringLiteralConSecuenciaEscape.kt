fun main(args: Array<String>){
    var cadena:String ="Juan\n\tVillalvazo"
    var cadena2:String ="""Juan
            Villalvazo""";

    var cadena3:String="Cadena 3"
    var dinero:Int=10
    println(cadena)
    println(cadena2)
    println(cadena2.length)
    println(cadena+ cadena3)
    println("Mi nombre es : $cadena2 y tengo $100 dolares")
    println("Mi nombre es : $cadena2 y tengo ${'$'}$dinero dolares")
}