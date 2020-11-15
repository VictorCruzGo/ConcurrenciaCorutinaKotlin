package kotlinCoroutinesMedium

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.withContext

class Receiver {
    var messagesAmount = 0
        private set

    suspend fun processAction(channel: ReceiveChannel<Int>) = withContext(Dispatchers.Default) {
        for(i in channel) {
            messagesAmount++
        }
    }
}
