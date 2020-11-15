import Facturas.FacturaEstandar
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import kotlin.system.measureTimeMillis

fun main(){
    println("--------------Iniciando la validacion SECUENCIAL--------------")
    //println(measureTimeMillis { validarFacturaEstandarSecuencial() })
    println("--------------Iniciando la validacion PARALELA--------------")
    //println(measureTimeMillis { validarFacturaEstandarParalelo() })
    println("--------------Iniciando la validacion PARALELA BUCLE--------------")
    println(measureTimeMillis { validarFacturaEstandarParaleloBucle() })
}

fun validarFacturaEstandarSecuencial()= runBlocking{
    val factura= FacturaEstandar()
    println("Iniciando la validacion")
    var resultado1= factura.validacionNitEmisor()
    var resultado2= factura.validacionNumeroFactura()
    var resultado3= factura.validacionCuf()
    var resultado4= factura.validacionCufd()
    var resultado5= factura.validacionCodigoSucursal()
    var resultado6= factura.validacionDireccion()
    var resultado7= factura.validacionCodigoPuntoDeVenta()
    var resultado8= factura.validacionFechaDeEmision()
    var resultado9= factura.validacionNombreRazonSocial()
    var resultado10= factura.validacionNumeroDocumento()
    println("Finalizando la validacion")
    var total=resultado1+resultado2+resultado3+resultado4+resultado5+resultado6+resultado7+resultado8+resultado9+resultado10
    println(total)
}
fun validarFacturaEstandarParalelo()= runBlocking{
    val factura= FacturaEstandar()
    println("Iniciando la validacion")
    var resultado1:Deferred<Int> = async {factura.validacionNitEmisor()}
    var resultado2:Deferred<Int> = async {factura.validacionNumeroFactura()}
    var resultado3:Deferred<Int> = async {factura.validacionCuf()}
    var resultado4:Deferred<Int> = async {factura.validacionCufd()}
    var resultado5:Deferred<Int> = async {factura.validacionCodigoSucursal()}
    var resultado6:Deferred<Int> = async {factura.validacionDireccion()}
    var resultado7:Deferred<Int> = async {factura.validacionCodigoPuntoDeVenta()}
    var resultado8:Deferred<Int> = async {factura.validacionFechaDeEmision()}
    var resultado9:Deferred<Int> = async {factura.validacionNombreRazonSocial()}
    var resultado10:Deferred<Int> = async {factura.validacionNumeroDocumento()}
    println("Finalizando la validacion")

    var total=resultado1.await()+resultado2.await()+resultado3.await()+resultado4.await()+resultado5.await()+resultado6.await()+resultado7.await()+resultado8.await()+resultado9.await()+resultado10.await()

    println(total)
}


fun validarFacturaEstandarParaleloBucle()= runBlocking{
    val factura= FacturaEstandar()
    println("Iniciando la validacion")
    for (i in 1..2000){
        var resultado1:Deferred<Int> = async {factura.validacionNitEmisor()}
        var resultado2:Deferred<Int> = async {factura.validacionNumeroFactura()}
        var resultado3:Deferred<Int> = async {factura.validacionCuf()}
        var resultado4:Deferred<Int> = async {factura.validacionCufd()}
        var resultado5:Deferred<Int> = async {factura.validacionCodigoSucursal()}
        var resultado6:Deferred<Int> = async {factura.validacionDireccion()}
        var resultado7:Deferred<Int> = async {factura.validacionCodigoPuntoDeVenta()}
        var resultado8:Deferred<Int> = async {factura.validacionFechaDeEmision()}
        var resultado9:Deferred<Int> = async {factura.validacionNombreRazonSocial()}
        var resultado10:Deferred<Int> = async {factura.validacionNumeroDocumento()}
    }

    println("Finalizando la validacion")

    //var total=resultado1.await()+resultado2.await()+resultado3.await()+resultado4.await()+resultado5.await()+resultado6.await()+resultado7.await()+resultado8.await()+resultado9.await()+resultado10.await()
    var total=0

    println(total)
}