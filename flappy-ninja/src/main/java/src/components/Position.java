package src.components;

import com.artemis.Component;

/**
 * User: eptwalabha
 * Date: 20/02/14
 * Time: 14:39
 */
public class Position extends Component {

    public float x;
    public float y;
    public Position origin;

    public Position() {
        origin = null;
    }

    public Position(float x, float y) {
        this(x, y, null);
    }

    public Position(float x, float y, Position origin) {
        this.x = x;
        this.y = y;
        this.origin = origin;
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

    public void detachFromOrigin() {
        this.x = getX();
        this.y = getY();
        origin = null;
    }

    public void attachToOrigin(Position origin) {
        this.x -= origin.getX();
        this.y -= origin.getY();
        this.origin = origin;
    }

    public void setLocation(float x, float y) {
        this.x = x;
        this.y = y;
    }
}
