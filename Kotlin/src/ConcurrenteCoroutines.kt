import javax.xml.bind.JAXBElement

fun main(){
    log("Start")

    //GlobalScope.launch{
        Thread.sleep(1000)
        log("After thread.sleep")
    //}

    log("End")
}

