package Interoperablidad

import Interoperabilidad.ValidacionFirmaEnJava
import kotlinx.coroutines.delay

class ValidarFirmaEnKotlin {
    suspend fun validarAutenticidad(): Int {
        //println("Inicio la validacion de la autenticidad")
        try {
            delay(1000)
        } catch (e: Exception) {
        }
        //println("Finalizo la validacion de la autenticidad")
        return 100
    }

    suspend fun validarRevocator(): Int {
        //println("Inicio la validacion de la revocacion")
        try {
            delay(1000)
        } catch (e: Exception) {
        }
        //println("Finalizo la validacion de la revocacion")
        return 200
    }

    fun validarAutenticidadSec(): Int {
        //println("Inicio la validacion de la autenticidad")
        try {
            Thread.sleep(1000)
        } catch (e: Exception) {
        }
        //println("Finalizo la validacion de la autenticidad")
        return 100
    }

    fun validarRevocatorSec(): Int {
        //println("Inicio la validacion de la revocacion")
        try {
            Thread.sleep(1000)
        } catch (e: Exception) {
        }
        //println("Finalizo la validacion de la revocacion")
        return 200
    }

    suspend fun validarAutenticidadJEncapsulado():Int{
        var java: ValidacionFirmaEnJava = ValidacionFirmaEnJava()
        return java.validarAutenticidad()
    }

    suspend fun validarRevocatorEncapsulado():Int{
        var java:ValidacionFirmaEnJava= ValidacionFirmaEnJava()
        return java.validarRevocator()
    }
}