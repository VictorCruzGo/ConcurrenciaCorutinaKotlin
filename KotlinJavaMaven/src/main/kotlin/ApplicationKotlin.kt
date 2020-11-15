import paqJava.JavaService
import paqKotlin.KotlinService


    fun main(args: Array<String>) { //String language = args[0];
        //comentario vic 1 k
        val JAVA = "java"
        val KOTLIN = "kotlin"

        val language = "java"
        when (language) {
            JAVA -> JavaService().sayHello()
            KOTLIN -> KotlinService().sayHello()
            else -> {
                println("Por no")
            }
        }
    }
