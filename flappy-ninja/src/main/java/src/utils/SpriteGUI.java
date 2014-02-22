package src.utils;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

/**
 * User: Eptwalabha
 * Date: 07/02/14
 * Time: 08:47
 */
public class SpriteGUI {

    public static final int LEFT = 0;
    public static final int TOP = 0;
    public static final int CENTER = 1;
    public static final int RIGHT = 2;
    public static final int BOTTOM = 2;

    private float pivotX, pivotY;
    private SpriteSheet spriteSheet;
    private int tileWidth;
    private float tileRatio = 1.0f;

    public SpriteGUI(String url, int nbrColumn, int nbrLine) throws SlickException{
        this(url, nbrColumn, nbrLine, SpriteGUI.LEFT, SpriteGUI.TOP);
    }

    public SpriteGUI(String url, int nbrColumn, int nbrLine, int pivotX, int pivotY) throws SlickException{
        Image image = new Image(url);
        image.setFilter(Image.FILTER_NEAREST);

        this.tileWidth = image.getWidth() / nbrColumn;
        this.tileRatio = (image.getHeight() / (nbrLine * 1.0f)) / (image.getWidth() / (nbrColumn* 1.0f));
//		System.out.println("ratio = " + this.tileRatio);
        this.spriteSheet = new SpriteSheet(image, image.getWidth() / nbrColumn, image.getHeight() / nbrLine);

        this.setCenterPivotPosition(pivotX, pivotY);

    }

    public void setCenterPivotPosition(int pivotX, int pivotY){

        this.pivotX = this.calculCenterOfRotation((float) this.tileWidth, pivotX);
        this.pivotY = this.calculCenterOfRotation((float) (this.tileWidth * this.tileRatio), pivotY);

    }

    private float calculCenterOfRotation(float mesure, int mode){

        if (mode == CENTER)
            return mesure / 2.0f;

        return mesure;
    }

    public Image getSpriteAt(int x, int y, int width){
        return this.getSpriteAt(x, y, width, (int) (width * this.tileRatio));
    }

    public Image getSpriteAt(int x, int y, int width, float angle){

        float ratio = this.tileWidth / (width * 1f);
//		System.out.println("sans =" + this.pivotX + "; ratio =" + (this.pivotX / ratio));
        Image image =this.spriteSheet.getSprite(x, y).getScaledCopy(width, (int) (width * this.tileRatio));
        image.setCenterOfRotation(this.pivotX / ratio, this.pivotY / ratio);
        image.setRotation(angle);
        return image;
    }

    public Image getSpriteAt(int positionXOnSheet, int positionYOnSheet, int desireWidth, int desireHeight){
        return this.spriteSheet.getSprite(positionXOnSheet, positionYOnSheet).getScaledCopy(desireWidth, desireHeight);
    }

    public int getTileWidth(){
        return this.tileWidth;
    }

    public int getTileHeight(){
        return (int) (this.tileWidth * this.tileRatio);
    }

    public float getTileRatio(){
        return this.tileRatio;
    }
}
