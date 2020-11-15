package kotlinChannel

import kotlinCoroutinesMedium.Planet
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.SendChannel
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
    Channel.RENDEZVOUS = 121
    Channel.BUFFERED = 86
    Channel.UNLIMITED = 83

    //100000 facturas
    Channel.RENDEZVOUS = 1129
    Channel.BUFFERED = 1051
    Channel.BUFFERED(100) = 1100
    Channel.UNLIMITED = 1094
    */
    val nroFacturas = 10

    var t = measureTimeMillis {
        runBlocking {
            println("//-------------------ENVIANDO A VALIDACION-------------------")
            launch {
                for (i in 0..15) {
                    //-----------Enviar a validacion-----------
                    val factura1: Factura = Factura()
                    factura1.nroFactura = i
                    println("Enviando la factura "+factura1.nroFactura)
                    channelParaValidar.send(factura1)
                    //-----------Enviar a validacion-----------
                }
                println("canal para validar cerrardo")
                channelParaValidar.close()
            }
            println("//-------------------VALIDANDO Y ENVIANDO A REGISTRO-------------------")
            println("inicio validacion")
            launch {
                while (!channelParaValidar.isEmpty) {
                    for (c in 0..10) {
                        println("for n="+c)
                        if(!channelParaValidar.isClosedForReceive){
                            println("canal para validar esta cerrado para enviar:"+ channelParaValidar.isClosedForSend)
                            println("canal para validar esta vacio:"+channelParaValidar.isEmpty)
                            println("canal para validar esta cerrado para recibir:"+channelParaValidar.isClosedForReceive)
                            var fac = channelParaValidar.receive()
                            println("valor recibido"+fac.nroFactura)
                            println(c)
                            launch {
                                val validador: ValidadorFactura = ValidadorFactura()
                                validador.validar(fac)
                                channelParaRegistrar.send(fac)
                            }
                        }

                        if(channelParaValidar.isClosedForReceive)
                        {
                            println("break")
                            println("--canal para validar esta cerrado para enviar:"+ channelParaValidar.isClosedForSend)
                            println("--canal para validar esta vacio:"+channelParaValidar.isEmpty)
                            println("--canal para validar esta cerrado para recibir:"+channelParaValidar.isClosedForReceive)
                            break
                        }
                    }
                    if(channelParaValidar.isClosedForReceive)
                    {
                        println("--while")
                        break
                    }

                }
                println("fin validacion")
                println("--canal para validar esta cerrado para enviar:"+ channelParaValidar.isClosedForSend)
                println("--canal para validar esta vacio:"+channelParaValidar.isEmpty)
                if(channelParaValidar.isClosedForReceive)
                {
                    println("cerrando el canal para registrar")
                    //Tengo que cerrar siempre y cuando las corutinas haya finalizado PENDIENTE
                    channelParaRegistrar.close()
                }
                //necesitor saber cuando todo la anterior finaliza para cerrar el canal
            }

            println("//-------------------REGISTRANDO FACTURAS-------------------")
            launch {
                //Registrar

                for (fac in channelParaRegistrar) {
                    val almacenar: GuardarFactura = GuardarFactura()
                    almacenar.registrar(fac)
                }

                //channelParaValidar.close()
                //channelParaRegistrar.close()

            }
        }
    }

    println("tiempo de ejecucion: " + t)
}

//suspend fun enviarCanal(fac:Factura, channel: SendChannel<Factura>){
//    channel.send(fac)
//}


