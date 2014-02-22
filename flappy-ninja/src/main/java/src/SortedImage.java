package src;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.opengl.ImageData;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.renderer.SGL;

import java.io.InputStream;

/**
 * An {@link Image} which can be used for sprite-sorting without much effort, using the depth buffer. When drawing just
 * add a z value (keep in mind that the z-buffer Slick uses has a range of -1 to 1) and the {@link Image} will be drawn
 * to the depth. The most common way is to say z = y (this class uses z = y / 1000 to fit Slick), which will overlap
 * images along there y-axis.
 *
 * Also, keep mind that Slick starts the Display with no depth test! So use the startDepthUse() and endDepthUse()
 * methods to activate the depth test. This is also a good way to separate non-depth images and depth images.
 * <p></p>
 * =================================================================== <br>
 *
 * Version Status: <br>
 *    - Simple drawing is possible use drawS methods <br>
 *  - enable startDepthUse and endDepthUse working <br>
 *  - will add more methods on demand.
 *
 * @author Stefan Lange aka R.D.
 * @version 0.5.0
 * @since 18.07.2011
 */
public class SortedImage extends Image {

    // tells us if the Image is currently in  depth mode (might make a static value
    // since we draw more then one image inside the start/end depth methods)
    private boolean deptTestinUse;

    // CONSTRUCTORS
    // ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
    /**
     * Create an image from a image data source. Note that this method uses
     *
     * @param data
     *            The pixelData to use to create the image
     * @param f
     *            The filter to use when scaling this image
     */
    public SortedImage(ImageData data, int f) {
        super(data, f);
    }
    /**
     * Create an image from a image data source
     *
     * @param data
     *            The pixelData to use to create the image
     */
    public SortedImage(ImageData data) {
        super(data);
    }
    /**
     * Create an image based on a file at the specified location
     *
     * @param in
     *            The input stream to read the image from
     * @param ref
     *            The name that should be assigned to the image
     * @param flipped
     *            True if the image should be flipped on the y-axis on load
     * @param filter
     *            The filter to use when scaling this image
     * @throws SlickException
     *             Indicates a failure to load the image
     */
    public SortedImage(InputStream in, String ref, boolean flipped, int filter) throws SlickException {
        super(in, ref, flipped, filter);
    }
    /**
     * Create an image based on a file at the specified location
     *
     * @param in The input stream to read the image from
     * @param ref The name that should be assigned to the image
     * @param flipped True if the image should be flipped on the y-axis  on load
     * @throws SlickException Indicates a failure to load the image
     */
    public SortedImage(InputStream in, String ref, boolean flipped) throws SlickException {
        super(in, ref, flipped, FILTER_LINEAR);
    }
    /**
     * Create an empty image
     *
     * @param width The width of the image
     * @param height The height of the image
     * @param f The filter to apply to scaling the new image
     * @throws SlickException Indicates a failure to create the underlying resource
     */
    public SortedImage(int width, int height, int f) throws SlickException {
        super(width, height, f);
    }
    /**
     * Create an empty image
     *
     * @param width The width of the image
     * @param height The height of the image
     * @throws SlickException Indicates a failure to create the underlying resource
     */
    public SortedImage(int width, int height) throws SlickException {
        super(width, height, FILTER_NEAREST);
    }
    /**
     * Create an image based on a file at the specified location
     *
     * @param ref The location of the image file to load
     * @param flipped True if the image should be flipped on the y-axis on load
     * @param f The filtering method to use when scaling this image
     * @param transparent The color to treat as transparent
     * @throws SlickException Indicates a failure to load the image
     */
    public SortedImage(String ref, boolean flipped, int f, Color transparent) throws SlickException {
        super(ref, flipped, f, transparent);
    }
    /**
     * Create an image based on a file at the specified location
     *
     * @param ref The location of the image file to load
     * @param flipped True if the image should be flipped on the y-axis on load
     * @param filter The filtering method to use when scaling this image
     * @throws SlickException Indicates a failure to load the image
     */
    public SortedImage(String ref, boolean flipped, int filter) throws SlickException {
        super(ref, flipped, filter, null);
    }
    /**
     * Create an image based on a file at the specified location
     *
     * @param ref The location of the image file to load
     * @param flipped True if the image should be flipped on the y-axis on load
     * @throws SlickException Indicates a failure to load the image
     */
    public SortedImage(String ref, boolean flipped) throws SlickException {
        super(ref, flipped, FILTER_LINEAR, null);
    }
    /**
     * Create an image based on a file at the specified location
     *
     * @param ref The location of the image file to load
     * @param trans The color to be treated as transparent
     * @throws SlickException Indicates a failure to load the image
     */
    public SortedImage(String ref, Color trans) throws SlickException {
        super(ref, false, FILTER_LINEAR, trans);
    }
    /**
     * Create an image based on a file at the specified location
     *
     * @param ref
     *            The location of the image file to load
     * @throws SlickException
     *             Indicates a failure to load the image
     */
    public SortedImage(String ref) throws SlickException {
        super(ref, false, FILTER_LINEAR, null);
    }
    /**
     * Creates an image using the specified texture
     *
     * @param texture
     *            The texture to use
     */
    public SortedImage(Texture texture) {
        super(texture);
    }

    // METHODS
    // ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
    /**
     * Draws this Image to a specific depth at image, y.
     */
    public void drawS(float x, float y, float depth) {
        drawS(x, y, width, height, depth, Color.white);
    }
    /**
     * Draws this Image to a specific depth at image, y and with a specific size.
     */
    public void drawS(float x, float y, float width, float height, float depth) {
        drawS(x, y, width, height, depth, Color.white);
    }
    /**
     * Draws this Image to a specific depth at image, y and with a specific size and a filter to use.
     */
    public void drawS(float x, float y, float width, float height, float depth, Color filter) {
        if (alpha != 1) {
            if (filter == null) {
                filter = Color.white;
            }

            filter = new Color(filter);
            filter.a *= alpha;
        }
        if (filter != null) {
            filter.bind();
        }

        texture.bind();
        GL.glTranslatef(x, y, -depth / 1000);

        GL.glBegin(SGL.GL_QUADS);
        drawEmbedded(0, 0, width, height);
        GL.glEnd();
        GL.glTranslatef(-x, -y, depth / 1000);
    }

    // SETTER & GETTER
    // ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
    /**
     * Starts the depth buffer.
     */
    public void startDepthUse() {
        if (deptTestinUse)
            return;
        deptTestinUse = true;

        // SGL in SVN provides Depth test but no LEQUAL? why?
        GL.glEnable(GL11.GL_DEPTH_TEST);
        GL.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glAlphaFunc(GL11.GL_GEQUAL, 1f);
        GL.glDepthFunc(GL11.GL_LEQUAL);
    }
    /**
     * ends the depth buffer.
     */
    public void endDepthUse() {
        if(!deptTestinUse)
            return;
        deptTestinUse = false;
        GL.glDisable(GL11.GL_ALPHA_TEST);
        GL.glDisable(GL11.GL_DEPTH_TEST);
    }

}
