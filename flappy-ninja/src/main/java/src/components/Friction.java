package src.components;

import com.artemis.Component;

/**
 * User: eptwalabha
 * Date: 20/02/14
 * Time: 21:04
 */
public class Friction extends Component {

    public float restituctionRatio;

    public Friction(float restituctionRatio) {
        this.restituctionRatio = restituctionRatio;
    }
}
