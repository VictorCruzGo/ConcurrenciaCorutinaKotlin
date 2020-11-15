fun main(args: Array<String>){
    var mayor:Int
    mayor=10
    println(mayor)

    //if
    println("-------------if")
    var a=10
    var b=11
    var numeroMayor:Int

    if (a>b){
        println("el mayor es: $a")
    }
    else {
        println("el menor es: $b")
    }

    //
    if(a>b) println(a) else println(b)

    //
    numeroMayor= if(a>b) a else b
    println(numeroMayor)

    println("-------------when")
    var mes=10
    var mesListeral:String
    when (mes){
        1 ->{
            println("Mes 1")
            mesListeral="Enero"
        }
        2 -> mesListeral="Febrero"
        3 -> mesListeral="Marzo"
        4 -> mesListeral="Abril"
        5 -> mesListeral="Mayo"
        6 -> mesListeral="Junio"
        7 -> mesListeral="Julio"
        8 -> mesListeral="Agosto"
        9 -> mesListeral="Septiembre"
        10 -> mesListeral="Octubre"
        11 -> mesListeral="Noviembre"
        12 -> mesListeral="Diciembre"
        13,14,15-> mesListeral="varios meses"
        in 1..5-> mesListeral="meses entre 1 y 5"
        else -> mesListeral="No existe"
    }

    when{
        mes>1-> mesListeral="mayor a 1"
    }

    println(mesListeral)

    //for
    println("-------------for")
    for(i in 1..5){
        println("numero de interacion $i")
    }

    for((indice,valor) in (4..18).withIndex()){
        println("clave: $indice - valor: $valor")
    }

    //while
    println("-------------while")
    var x=0
    while(x<=3){
        println(x)
        x++
    }

    //funciones
    println("funciones")
    var resultado=suma(10,30)
    println("El resultado es $resultado")

    //Coseno
    val coseno=Math.cos(1.0)
    println("El coseno de 1 es: $coseno")

}

fun suma(a:Int, b:Int):Int{
    //print(a+b)
    return (a+b)
}


