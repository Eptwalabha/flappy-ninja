package src.components;

import com.artemis.Component;

/**
 * User: eptwalabha
 * Date: 22/02/14
 * Time: 15:24
 */
public class Limit extends Component {

    public float x;
    public float y;

    public float width;
    public float height;

    public Position origin;

    public Limit(float positionX, float positionY, float width, float height) {
        x = positionX;
        y = positionY;
        this.width = width;
        this.height = height;
    }

    public float getX() {
        if (origin != null)
            return origin.getX() + x;
        return x;
    }

    public float getY() {
        if (origin != null)
            return origin.getY() + y;
        return y;
    }

    public boolean isPositionInsideLimit(Position position) {

        return (position.getX() >= getX() && position.getX() <= getX() + width &&
                position.getY() >= getY() && position.getY() <= getY() + height);
    }
}
