package kotlinCoroutinesMedium

import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking



    fun main() {
        println("Start")

        val planetsProducer = PlanetsProducer()
        val planetsConsumer = PlanetsConsumer()

        runBlocking {
            launch {
                planetsProducer.processPlanetsStream()
            }

            launch {
                planetsConsumer.processPlanetsStream(planetsProducer.getChannel())
                planetsConsumer.release()
            }
        }

        println("End")
    }
