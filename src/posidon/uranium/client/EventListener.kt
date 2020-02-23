package posidon.uranium.client

import posidon.potassium.packets.ChatMessage
import posidon.potassium.packets.ChunkUpdate
import posidon.potassium.packets.InitInfoPacket
import posidon.potassium.packets.Packet
import posidon.uranium.engine.graphics.Renderer
import posidon.uranium.engine.maths.Vec3f
import posidon.uranium.engine.maths.Vec3i
import posidon.uranium.engine.objects.Camera
import posidon.uranium.engine.objects.Chunk
import posidon.uranium.main.Globals

object EventListener {
    fun onEvent(event: Any?) { when (event) {
        is ChatMessage -> println(event.message)
        is Packet -> {
            if (event.containsKey("time")) Globals.time = event.getDouble("time")
            if (event.containsKey("position")) {
                val pos = event["position"] as FloatArray?
                Camera.position = Vec3f(pos!![0], pos[1], pos[2])
            }
        }
        is ChunkUpdate -> {
            val blocks = event.blocks
            var i = 0
            val blocksLength = blocks.size
            while (i < blocksLength) {
                val block = blocks[i]
                val posInChunk = Vec3i(
                        i / (Chunk.CHUNK_SIZE * Chunk.CHUNK_SIZE),
                        i / Chunk.CHUNK_SIZE % Chunk.CHUNK_SIZE,
                        i % Chunk.CHUNK_SIZE
                )
                val chunkPos = Vec3i(event.x, event.y, event.z)
                Renderer.setBlock(block, posInChunk, chunkPos)
                i++
            }
        }
        is InitInfoPacket -> {
            Globals.time = event.time
            Camera.position!!.x = event.x
            Camera.position!!.y = event.y
            Camera.position!!.z = event.z
            Camera.moveSpeed = event.moveSpeed
            Camera.jumpHeight = event.jumpHeight
        }
    }}
}