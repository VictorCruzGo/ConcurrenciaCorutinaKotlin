package Interoperablidad

import Interoperabilidad.ValidacionFirmaEnJava
import kotlinx.coroutines.*
import org.example.launch
import kotlin.system.measureTimeMillis

fun main(){
    println("----------validacion en kotlin----------")
    println("tiempo Kotlin secuencial:"+measureTimeMillis { validarKotlinSecuencial()  })
    println("tiempo Kotlin secuencial Convertido:"+measureTimeMillis { validarKotlinSecuencialConv()  })
    println("tiempo Kotlin paralelo:"+measureTimeMillis { validarKotlin()  })
    println("----------validacion en Java----------")
    println("tiempo java secuencial:"+measureTimeMillis { validarJavaSecuencial()  })
    println("tiempo java secuencial Convertido:"+measureTimeMillis { validarJavainSecuencialConv()  })
    println("tiempo java paralelo:"+measureTimeMillis { validarJava()  })

}


fun validarKotlinSecuencial(){
    var kotlin:ValidarFirmaEnKotlin= ValidarFirmaEnKotlin()

    var res1= kotlin.validarAutenticidadSec()
    var res2= kotlin.validarAutenticidadSec()
    var resultado=res1+res2
    println("resultado validarKotlinSecuencial:"+resultado)
}

fun validarKotlinSecuencialConv(){
    var kotlin:ValidarFirmaEnKotlin= ValidarFirmaEnKotlin()

    runBlocking {
        var res1= async { kotlin.validarAutenticidadSec() }
        var res2= async { kotlin.validarAutenticidadSec() }
        var resultado=res1.await()+res2.await()
        println("resultado validarKotlinSecuencialConv:"+resultado)
    }
}

fun validarKotlin()= runBlocking{
    var kotlin:ValidarFirmaEnKotlin= ValidarFirmaEnKotlin()

        var res1= async { kotlin.validarAutenticidad() }
        var res2= async { kotlin.validarAutenticidad() }

        var resultado=res1.await()+res2.await();
        println("resultado validarKotlin"+resultado)

}

/*
fun validarJava()= runBlocking{
    var kotlin:ValidarFirmaEnKotlin= ValidarFirmaEnKotlin()
    var res1=launch { kotlin.validarAutenticidadJEncapsulado() }
    var res2=launch{ kotlin.validarRevocatorEncapsulado() }
    //var resultado=res1.await()+res2.await()
    //println("resultado validarJava enc:"+resultado)
    println("resultado validarJava enc:")
}*/
 fun validarJava()= runBlocking{
    var kotlin:ValidarFirmaEnKotlin= ValidarFirmaEnKotlin()

    var job=GlobalScope.async {
        //println("por aqui")
        var res1=async { kotlin.validarAutenticidadJEncapsulado() }
        var res2=async{ kotlin.validarRevocatorEncapsulado() }
        var resultado=res1.await()+res2.await()
        println("resultado validarJava enc:"+resultado)
    }
    var resupuesta=job.await()

}

fun validarJavainSecuencialConv()= runBlocking {
    var java: ValidacionFirmaEnJava = ValidacionFirmaEnJava()
    var job=GlobalScope.launch {
        var res1= async { java.validarAutenticidad() }
        var res2= async { java.validarRevocator() }
        var resultado=res1.await()+res2.await()
        println("resultado validarJavainSecuencialConv..:"+resultado)
    }
    job.join()

}

fun validarJavaSecuencial() {
    var java:ValidacionFirmaEnJava= ValidacionFirmaEnJava()
    var res1=java.validarAutenticidad()
    var res2=java.validarRevocator()
    var resultado=res1+res2
    println("resultado validarJavaSecuencial:"+resultado)
}

