package kotlinChannel

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class GuardarFactura {
    //suspend fun registrar(fac:Factura){
    suspend fun registrar(fac: Factura) {
                //Thread.sleep(10)
                delay(10)
                println("...Factura registrada:" + fac.nroFactura)
    }
}