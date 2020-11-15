package Facturas

import kotlinx.coroutines.delay

class FacturaEstandar{
    //Atributos
    var Cabecera=Facturas.Cabecera()
    var Detalle=Facturas.Detalle()

    //Metodos
    suspend fun validacionNitEmisor():Int{
        println("1....validando validacionNitEmisor")
        delay(3000)
        return 1
    }
    suspend fun validacionNumeroFactura():Int{
        println("2....validando validacionNumeroFactura")
        delay(3000)
        return 1
    }
    suspend fun validacionCuf():Int{
        println("3....validando validacionCuf")
        delay(3000)
        return 1
    }
    suspend fun validacionCufd():Int{
        println("4....validando validacionCufd")
        delay(3000)
        return 1
    }
    suspend fun validacionCodigoSucursal():Int{
        println("5....validando validacionCodigoSucursal")
        delay(3000)
        return 1
    }
    suspend fun validacionDireccion():Int{
        println("6....validando validacionDireccion")
        delay(3000)
        return 1
    }
    suspend fun validacionCodigoPuntoDeVenta():Int{
        println("7....validando validacionCodigoPuntoDeVenta")
        delay(3000)
        return 1
    }
    suspend fun validacionFechaDeEmision():Int{
        println("8....validando validacionFechaDeEmision")
        delay(3000)
        return 1
    }
    suspend fun validacionNombreRazonSocial():Int{
        println("9....validando validacionNombreRazonSocial")
        delay(3000)
        return 1
    }
    suspend fun validacionNumeroDocumento():Int{
        println("10....validando validacionNumeroDocumento")
        delay(3000)
        return 1
    }
}

class Cabecera{
    var nitEmisor: String
    var numeroFactura: String
    var cuf: String
    var cufd: String
    var codigoSucursal: String
    var direccion: String
    var fechaEmision: String
    var nombreRazonSocial: String
    var codigoTipoDocumentoIdentidad: String
    var numeroDocumento: String
    var codigoCliente: String
    var codigoMetodoPago: String
    var montoTotal :Int
    var codigoMoneda: String
    var tipoCambio :Float
    var montoTotalMoneda :Float
    var leyenda: String
    var usuario: String
    var codigoDocumentoSector :Int

    init{
        this.nitEmisor=""
        this.numeroFactura=""
        this.cuf=""
        this.cufd=""
        this.codigoSucursal=""
        this.direccion=""
        this.fechaEmision=""
        this.nombreRazonSocial=""
        this.codigoTipoDocumentoIdentidad=""
        this.numeroDocumento=""
        this.codigoCliente=""
        this.codigoMetodoPago=""
        this.montoTotal = 0
        this.codigoMoneda=""
        this.tipoCambio = 0f
        this.montoTotalMoneda = 0f
        this.leyenda=""
        this.usuario=""
        this.codigoDocumentoSector = 0
    }
}

class Detalle{
    var actividadEconomica: String
    var codigoProductoSin: String
    var codigoProducto: String
    var descripcion: String
    var cantidad: String
    var unidadMedida: String
    var precioUnitario: Float
    var montoDescuento: Int
    var subTotal: Float
    var numeroSerie:Long
    var numeroImei:String


    init {
        this.actividadEconomica=""
        this.codigoProductoSin=""
        this.codigoProducto=""
        this.descripcion=""
        this.cantidad=""
        this.unidadMedida=""
        this.precioUnitario=0.0f
        this.montoDescuento=0
        this.subTotal=0.0f
        this.numeroSerie=0
        this.numeroImei=""
    }
}
