package kotlinChannel

import kotlinCoroutinesMedium.Planet
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.system.measureTimeMillis

fun main() {
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

    //val channelParaValidar = Channel<Factura>(10)
    //val channelParaRegistrar = Channel<Factura>(10)

    val channelParaValidar = Channel<Factura>(Channel.UNLIMITED)
    val channelParaRegistrar = Channel<Factura>(Channel.UNLIMITED)

    /*
    Tiempos con:
    //2000 facturas
    Channel.UNLIMITED = 305

    //100000 facturas
    Channel.UNLIMITED = 3319
    */
    val nroFacturas = 10

    var t = measureTimeMillis {
        runBlocking {
            println("//-------------------ENVIANDO A VALIDACION-------------------")
            launch {
                for (i in 1..nroFacturas) {
                   // println("enviando a validacion")
                    //-----------Enviar a validacion-----------
                    val factura1: Factura = Factura()
                    factura1.nroFactura = i
                    println("Enviando la factura " + factura1.nroFactura)
                    channelParaValidar.send(factura1)
                    //-----------Enviar a validacion-----------
                }
                println("canal para validar cerrardo")
                channelParaValidar.close()
            }
            println("//-------------------VALIDANDO Y ENVIANDO A REGISTRO-------------------")
            launch {
                println("inicio validacion")
                val job=launch {
                    for (f in channelParaValidar) {
                        //println("facturas para validar :" + f)
                        launch {
                            val validador: ValidadorFactura = ValidadorFactura()
                            validador.validar(f)
                            channelParaRegistrar.send(f)
                        }
                    }
                }
                println("Antes del join")
                job.join()
                println("Despues del join")
                channelParaRegistrar.close()
            }

            println("//-------------------REGISTRANDO FACTURAS-------------------")

            launch {
                //Registrar
                println("iniciando el registro")
                for (fac in channelParaRegistrar) {
                    //println("facturas para registrar :" + fac)
                    val almacenar: GuardarFactura = GuardarFactura()
                    almacenar.registrar(fac)
                }
                println("finalizando el registro")
            }
        }
    }

    println("tiempo de ejecucion: " + t)
}

//suspend fun enviarCanal(fac:Factura, channel: SendChannel<Factura>){
//    channel.send(fac)
//}


