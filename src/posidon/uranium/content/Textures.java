package posidon.uranium.content;

import posidon.uranium.engine.graphics.Texture;

import java.util.HashMap;

public class Textures {
    public static HashMap<String, Texture> blocks = new HashMap<>();
    private static String[] blockNames = {
            "grass", "stone"
    };

    public static void set(String path) {
        if (path == null) {
            for (String name : blockNames)
                blocks.put(name, new Texture("res/textures/block/" + name + ".png"));
        }
    }

    public static void clear() {
        for (Texture texture : blocks.values()) texture.delete();
        blocks.clear();
    }
}