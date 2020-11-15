package kotlinCoroutinesMedium

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.consumeEach
import kotlin.coroutines.CoroutineContext

class PlanetsConsumer: CoroutineScope {
    private val job = Job()
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Default

    suspend fun processPlanetsStream(planets: ReceiveChannel<Planet>) = withContext(Dispatchers.Default) {
        planets.filterByMoons(2)
                .mapToName()
                .consumeEach { planet ->
                    println("Planet consumed: $planet")
                }
    }

    private fun ReceiveChannel<Planet>.filterByMoons(moons: Int): ReceiveChannel<Planet> {
        val filteredElementsChannel = Channel<Planet>()
        launch {
            consumeEach { planet ->
                if (planet.moons >= moons)
                    filteredElementsChannel.send(planet)
            }
            filteredElementsChannel.close()
        }
        return filteredElementsChannel
    }

    private fun ReceiveChannel<Planet>.mapToName(): ReceiveChannel<String> {
        val mappedElementsChannel = Channel<String>()
        launch {
            consumeEach { planet -> mappedElementsChannel.send(planet.name) }
            mappedElementsChannel.close()
        }
        return mappedElementsChannel
    }

    fun release() {
        this.job.cancel()
    }
}
