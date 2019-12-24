package posidon.potassium.packets;

import java.io.Serializable;

public class InitInfoPacket implements Serializable {
    private static final long serialVersionUID = 1;
    public double time;
    public float x, y, z, moveSpeed, jumpHeight;
}
