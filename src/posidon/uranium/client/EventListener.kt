package posidon.uranium.client;

import posidon.potassium.packets.ChatMessage;
import posidon.potassium.packets.ChunkUpdate;
import posidon.potassium.packets.InitInfoPacket;
import posidon.potassium.packets.Packet;
import posidon.potassium.universe.block.Block;
import posidon.uranium.engine.graphics.Renderer;
import posidon.uranium.engine.maths.Vec3f;
import posidon.uranium.engine.maths.Vec3i;
import posidon.uranium.engine.objects.Camera;
import posidon.uranium.main.Globals;

import static posidon.uranium.engine.objects.Chunk.CHUNK_SIZE;

public class EventListener {
    public static void onEvent(Object event) {
        if (event instanceof ChatMessage) {
            System.out.println(((ChatMessage)event).message);
        } else if (event instanceof Packet) {
            Packet packet = (Packet) event;
            if (packet.containsKey("time")) Globals.time = packet.getDouble("time");
            if (packet.containsKey("position")) {
                float[] pos = (float[]) packet.get("position");
                Camera.position = new Vec3f(pos[0], pos[1], pos[2]);
            }
        } else if (event instanceof ChunkUpdate) {
            ChunkUpdate data = (ChunkUpdate) event;
            Block[] blocks = data.blocks;
            for (int i = 0, blocksLength = blocks.length; i < blocksLength; i++) {
                Block block = blocks[i];
                Vec3i posInChunk = new Vec3i(
                        i / (CHUNK_SIZE * CHUNK_SIZE),
                        (i / CHUNK_SIZE) % CHUNK_SIZE,
                        i % CHUNK_SIZE
                );
                Vec3i chunkPos = new Vec3i(data.x, data.y, data.z);
                Renderer.setBlock(block, posInChunk, chunkPos);
            }
        } else if (event instanceof InitInfoPacket) {
            InitInfoPacket packet = (InitInfoPacket) event;
            Globals.time = packet.time;
            Camera.position.x = packet.x;
            Camera.position.y = packet.y;
            Camera.position.z = packet.z;
            Camera.moveSpeed = packet.moveSpeed;
            Camera.jumpHeight = packet.jumpHeight;
        }
    }
}
