package kotlinChannel

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.system.measureTimeMillis

fun main() {
    //Facturas
    //var lstFactura: List<Factura> = listOf()
    //lstFactura = llenarFacturas();

    //println("...Imprimiendo las facturas")
    //lstFactura.forEach { println("Nro factura: ${it.nroFactura}  - Razon social: ${it.razonSocial}") }

    //Recuperar factura de Pulsar
    val factura1: Factura = Factura()
    factura1.nroFactura = 10
    factura1.nit = 11111
    factura1.razonSocial = "razon social 10"
    factura1.montoTotal = 100.0f

    //Desempaquetear
    //val channelParaValidar = Channel<Factura>(Channel.RENDEZVOUS)
    //val channelParaRegistrar = Channel<Factura>(Channel.RENDEZVOUS)

    //val channelParaValidar = Channel<Factura>(Channel.BUFFERED)
    //val channelParaRegistrar = Channel<Factura>(Channel.BUFFERED)

    //val channelParaValidar = Channel<Factura>(100)
    //val channelParaRegistrar = Channel<Factura>(100)

    val channelParaValidar = Channel<Factura>(Channel.UNLIMITED)
    val channelParaRegistrar = Channel<Factura>(Channel.UNLIMITED)

    /*
    Tiempos con:
    //2000 facturas
    Channel.RENDEZVOUS = 121
    Channel.BUFFERED = 86
    Channel.UNLIMITED = 83

    //100000 facturas
    Channel.RENDEZVOUS = 1129
    Channel.BUFFERED = 1051
    Channel.BUFFERED(100) = 1100
    Channel.UNLIMITED = 1094
    */
    val nroFacturas=2000

    var t=measureTimeMillis {
        runBlocking {
            println("//-------------------ENVIANDO A VALIDACION-------------------")
            launch {
                //1.Obtener el ID recepcion de PULSAR
                //2.Buscar el paquete en bd.
                //3.Desempaqueter b64, tar
                //4.Recorrer el paquete
                for (i in 1..nroFacturas) {
                    //5. Obtener xml
                    //6. Convertir xml a dto
                    val factura1: Factura = Factura()
                    factura1.nroFactura = i
                    println("Enviando factura ${factura1.nroFactura}")
                    //7. Enviar al canal para validar
                    channelParaValidar.send(factura1)
                }
                println("Cerrando channelParaValidar")
                channelParaValidar.close()
            }
            println("//-------------------VALIDANDO Y ENVIANDO A REGISTRO-------------------")
            launch {
                //Validar
                for (fac in channelParaValidar) {
                    val validador: ValidadorFactura = ValidadorFactura()
                    println("Recibiendo factura ${fac.nroFactura}")
                    validador.validar(fac)
                    channelParaRegistrar.send(fac)
                }
                println("Cerrando canal para registrar")
                channelParaRegistrar.close()
            }

            /*
            println("//-------------------REGISTRANDO FACTURAS-------------------")
            launch {
                //Registrar
                for (fac in channelParaRegistrar) {
                    val almacenar: GuardarFactura = GuardarFactura()
                    println("Registrando factura ${fac.nroFactura}")
                    almacenar.registrar(fac)
                }
            }*/
        }
    }

    println("tiempo de ejecucion: "+t)
}


fun llenarFacturas(): List<Factura> {
    val factura1: Factura = Factura()
    factura1.nroFactura = 10
    factura1.nit = 11111
    factura1.razonSocial = "razon social 10"
    factura1.montoTotal = 100.0f

    val factura2: Factura = Factura()
    factura2.nroFactura = 20
    factura2.nit = 22222
    factura2.razonSocial = "razon social 10"
    factura2.montoTotal = 200.0f

    val factura3: Factura = Factura()
    factura3.nroFactura = 30
    factura3.nit = 33333
    factura3.razonSocial = "razon social 10"
    factura3.montoTotal = 300.0f

    var lstFactura = mutableListOf<Factura>()
    lstFactura.add(factura1)
    lstFactura.add(factura2)
    lstFactura.add(factura3)

    return lstFactura
}
