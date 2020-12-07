/*
    Darragh O'Halloran

    Custom circle that enables the ability to write a name on top of it, which will stay affixed to the center of the circle.
 */


import com.sun.javafx.tk.FontMetrics;
import com.sun.javafx.tk.Toolkit;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * Creates a circle object that allows point entry for center, as well as formatting for a text-box within.
 */
public class CircleWithText {

    /**
     * The font for the numbers inside the circle.
     */
    private final Font font =  Font.font("Times New Roman", FontWeight.BOLD, 16);
    private final FontMetrics fm = Toolkit.getToolkit().getFontLoader().getFontMetrics(font);

    /**
     * The radius of the circle.
     */
    public static final int RADIUS = 40;

    /**
     * The name for each circle to display
     */
    private String name;

    // The circle attributes
    private Point2D point;
    private Color backgroundColor;
    private Color borderColor;
    private Color fontColor;
    private int radius;

    /**
     * Creates a circle object
     * @param name name of the person represented by the circle
     */
    public CircleWithText(String name) {
        this.name = name;
        this.backgroundColor = Color.web("#0B18EC");
    }

    /**
     *
     * @param name name of the descendant
     * @param point an x/y point
     */
    public CircleWithText(String name, Point2D point) {
        this.name = name;
        this.point = point;
        this.backgroundColor = Color.web("#3174DE");
        this.setBorderColor(Color.rgb(99,4,16));
        this.fontColor = Color.web("#e2e2e2");

    }
    public CircleWithText(String name, Point2D point, int radius) {
        this.name = name;
        this.point = point;
        this.backgroundColor = Color.web("#3174DE");
        this.setBorderColor(Color.rgb(99,4,16));
        this.fontColor = Color.web("#e2e2e2");
        this.radius = radius;

    }


    /**
     * Draws the circle at a new position
     * @param gc The graphics object to use for drawing to a component
     */
    public void draw(GraphicsContext gc) {
        gc.setLineWidth(3);

        //The actual circle corresponding to each node
        gc.setFill(backgroundColor);
        gc.fillOval(point.getX() - RADIUS, point.getY() - RADIUS, 2 * RADIUS, 2 * RADIUS);

        //Circle border
        gc.setStroke(borderColor);
        gc.strokeOval(point.getX() - RADIUS, point.getY() - RADIUS, 2 * RADIUS, 2 * RADIUS);

        // Draw the id centered in the circle
        gc.setFont(font);
        gc.setFill(getFontColor());
        gc.fillText(name,
                point.getX() - (fm.computeStringWidth(name) / 2),
                point.getY() + (fm.getAscent() / 4));
    }
    public void drawCusRadius(GraphicsContext gc) {
        gc.setLineWidth(3);

        //The actual circle corresponding to each node
        gc.setFill(backgroundColor);
        gc.fillOval(point.getX() - radius, point.getY() - radius, 2 * radius, 2 * radius);

        //Circle border
        gc.setStroke(borderColor);
        gc.strokeOval(point.getX() - radius, point.getY() - radius, 2 * radius, 2 * radius);

        // Draw the id centered in the circle
        gc.setFont(font);
        gc.setFill(getFontColor());
        gc.fillText(name,
                point.getX() - (fm.computeStringWidth(name) / 2),
                point.getY() + (fm.getAscent() / 4));
    }

    /**
     * Gets the border color.
     * @return A color for the circle border
     */
    public Color getBorderColor() {
        return borderColor;
    }

    /**
     * Sets the border color.
     * @param borderColor - the circle's border color
     */
    private void setBorderColor(Color borderColor) {
        this.borderColor = borderColor;
    }

    /**
     * Gets the point coordinates.
     * @return point - the circle's point
     */
    public Point2D getPoint() {
        return point;
    }

    /**
     * Sets the point coordinates.
     * @param point - the circle's point
     */
    public void setPoint(Point2D point) {
        this.point = point;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    /**
     * Gets the background color.
     * @return backgroundColor the circle's background
     */
    public Color getBackgroundColor() {
        return backgroundColor;
    }

    /**
     * Sets the background color.
     * @param color - the circle's background color
     */
    private void setBackgroundColor(Color color) {
        this.backgroundColor = color;
    }

    /**
     * Gets the circle radius.
     * @return the radius
     */
    public int getRadius() {
        return radius;
    }

    public int getDefRadius() {
        return RADIUS;
    }

    /**
     * Gets the font color.
     * @return the circle's font color
     */
    public Color getFontColor() {
        return this.fontColor;
    }

    /**
     * Sets the font color.
     * @param fontColor the chosen font color
     */
    private void setFontColor(Color fontColor) {
        this.fontColor = fontColor;
    }
}

