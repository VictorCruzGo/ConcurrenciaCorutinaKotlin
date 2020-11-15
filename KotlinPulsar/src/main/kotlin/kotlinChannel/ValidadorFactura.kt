package kotlinChannel

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class ValidadorFactura {
    //suspend fun validar(fac:Factura){
    suspend fun validar(fac: Factura) {
                //Thread.sleep(300)
                delay(300)
                println("...factura validado:" + fac.nroFactura)

    }
}