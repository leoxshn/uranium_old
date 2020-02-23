package posidon.potassium.packets;

import posidon.potassium.universe.block.Block;
import java.io.Serializable;

public class ChunkUpdate implements Serializable {
    private static final long serialVersionUID = 1;
    public int x, y, z;
    public Block[] blocks;
}
