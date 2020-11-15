fun main(args: Array<String>){
    //caracteres
    var caracter:Char ='a'
    //Los cambios en tipos de datos consume procesador.
    var caracterEntero:Int=caracter.toInt()-97 //convertir a su representacion de entero

    var suma:Int=caracterEntero+15
    println(caracter)
    println(caracterEntero)
    println(suma)

    //cadenas
    var cadena:String="Juan"
    println(cadena[0])

}