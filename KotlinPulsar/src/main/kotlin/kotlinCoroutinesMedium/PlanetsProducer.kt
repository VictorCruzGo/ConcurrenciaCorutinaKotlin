package kotlinCoroutinesMedium

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.withContext

class PlanetsProducer {

    private val channel = Channel<Planet>()

    private val planets = listOf(
            Planet("Mercury", 60_827_208_742, 2_439.7, 0, false),
            Planet("Venus", 928_415_345_893, 6_051.8, 0, false),
            Planet("Earth", 1_083_206_916_846, 6_371.00, 1, false),
            Planet("Mars", 163_115_609_799, 3_389.5, 2, false),
            Planet("Jupiter", 1_431_281_810_739_360, 69_911.0, 79, true),
            Planet("Saturn", 827_129_915_150_897, 58_232.0, 83, true),
            Planet("Uranus", 68_334_355_695_584, 25_362.0, 27, true),
            Planet("Neptune", 62_525_703_987_421, 24_622.0, 14, true)
    )

    //Se obtiene el acceso al canal
    fun getChannel(): ReceiveChannel<Planet> = this.channel

    suspend fun processPlanetsStream() = withContext(Dispatchers.Default) {
        planets.forEach { channel.send(it) }
        channel.close()
    }
}