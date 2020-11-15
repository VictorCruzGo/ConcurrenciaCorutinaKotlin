package kotlinCoroutinesMedium

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.withContext

class Sender {
    companion object {
        const val MESSAGES_AMOUNT = 1000
    }

    suspend fun processAction(channel: SendChannel<Int>) = withContext(Dispatchers.Default) {
        repeat(MESSAGES_AMOUNT) {
            channel.send((1..100).random())
        }
    }
}
