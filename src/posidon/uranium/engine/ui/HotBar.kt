package posidon.uranium.engine.ui;

import posidon.uranium.engine.graphics.Texture;
import posidon.uranium.engine.maths.Vec2f;

public class HotBar extends View {

    private static final float WIDTH_TO_HEIGHT_RATIO = 23 / 3f;

    public HotBar() {
        super(new Vec2f(0, -1 + 0.5f/WIDTH_TO_HEIGHT_RATIO), new Vec2f(1, 1/WIDTH_TO_HEIGHT_RATIO), new Texture("res/textures/ui/hotbar.png"));
    }
}
