package posidon.uranium.client

import posidon.potassium.packets.PlayerJoinPacket
import posidon.uranium.main.Main
import java.io.EOFException
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.io.StreamCorruptedException
import java.net.Socket
import java.net.SocketException

class Client : Runnable {
    override fun run() {
        while (Main.running) {
            try { EventListener.onEvent(input!!.readObject()) }
            catch (e: EOFException) { kill() }
            catch (e: SocketException) { kill() }
            catch (e: StreamCorruptedException) { kill() }
            catch (e: Exception) { e.printStackTrace() }
        }
        kill()
    }

    companion object {
        private var output: ObjectOutputStream? = null
        private var input: ObjectInputStream? = null
        private var connection: Socket? = null
        fun start(ip: String?, port: Int): Boolean {
            return try {
                connection = Socket(ip, port)
                output = ObjectOutputStream(connection!!.getOutputStream())
                output!!.flush()
                input = ObjectInputStream(connection!!.getInputStream())
                send(PlayerJoinPacket(0, "leoxshn"))
                Thread(Client(), "unraniumClient").start()
                true
            } catch (e: Exception) {
                e.printStackTrace()
                Main.running = false
                System.err.println("Can't connect to potassium server")
                false
            }
        }

        fun send(o: Any?) {
            try {
                output!!.writeObject(o)
                output!!.flush()
            }
            catch (e: SocketException) { kill() }
            catch (e: Exception) { e.printStackTrace() }
        }

        fun kill() {
            try {
                output!!.close()
                input!!.close()
                connection!!.close()
            } catch (ignore: Exception) {}
        }
    }
}