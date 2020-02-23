package posidon.potassium.packets;

import java.io.Serializable;
import java.util.HashMap;

public class Packet extends HashMap<String, Object> implements Serializable {
    private static final long serialVersionUID = 1;
    public int getInt(Object key) { return (int) super.get(key); }
    public double getDouble(Object key) { return (double) super.get(key); }
    public float getFloat(Object key) { return (float) super.get(key); }
    public String getString(Object key) {
        try { return (String) super.get(key); }
        catch (Exception e) { return null; }
    }
}
