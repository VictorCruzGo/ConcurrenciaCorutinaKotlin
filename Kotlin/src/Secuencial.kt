fun log(message: String){
    println("[${Thread.currentThread().name}]: $message")
}

fun log(character: Char){
    print("$character")
}

fun main(){
    log("Start")
    Thread.sleep(1000)
    log("After thread.sleep")
    log("End")
}