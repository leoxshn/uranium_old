package posidon.uranium.main;

import posidon.uranium.engine.maths.Vec3f;

public class Globals {

    public static Vec3f skyColor = new Vec3f(0, 0, 0);
    public static Vec3f ambientLight = skyColor;
    public static double time;
    public static int timeSpeed = 1;

    private static final Vec3f SKY_NORMAL = new Vec3f(0.403f, 0.639f, 0.956f);
    private static final Vec3f SKY_NIGHT = new Vec3f(0.007f, 0.047f, 0.113f);
    private static final Vec3f LIGHT_DAY = new Vec3f(1, 1, 1);
    private static final Vec3f LIGHT_NIGHT = new Vec3f(0.098f, 0.137f, 0.180f);
    private static final int MAX_TIME = 24000;

    public static void tick() {
        time = (time < MAX_TIME) ? time + timeSpeed : 0;
        skyColor = Vec3f.blend(SKY_NIGHT, SKY_NORMAL, (float)(Math.pow((Globals.time-MAX_TIME/2f)/MAX_TIME*2, 2)));
        ambientLight = Vec3f.blend(LIGHT_NIGHT, LIGHT_DAY, (float)(Math.pow((Globals.time-MAX_TIME/2f)/MAX_TIME*2, 2)));
    }
}
