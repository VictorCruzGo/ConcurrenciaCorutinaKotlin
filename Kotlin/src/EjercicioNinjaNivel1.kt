fun main(args: Array<String>){
    //Obtener la serie de fibonacii
    //0,1,1,2,3,5,8,13,21,34
    var a=-1
    var b=1
    var c=a+b

    for(i in 1..10){
        print("$c,")
        a=b
        b=c
        c=a+b
    }
}

