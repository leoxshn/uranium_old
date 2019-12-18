package posidon.uranium.engine.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class HashMapUtils {
    public static int newID(Map map) {
        int id;
        do id = new Random().nextInt();
        while (map.containsKey(id));
        return id;
    }
}
