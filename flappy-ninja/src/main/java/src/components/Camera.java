package src.components;

import com.artemis.Component;

/**
 * User: eptwalabha
 * Date: 24/02/14
 * Time: 21:19
 */
public class Camera extends Component {

    public float screenWidth;
    public float screenHeight;
    public Position cameraPosition;

    public Camera(Position cameraPosition, float screenWidth, float screenHeight) {
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.cameraPosition = cameraPosition;
    }

    public boolean containsThisPoint(Position position) {
        return  position.getX() >= cameraPosition.getX() &&
                position.getX() <= cameraPosition.getX() + screenWidth &&
                position.getY() >= cameraPosition.getY() &&
                position.getY() <= cameraPosition.getY() + screenHeight;
    }
}
