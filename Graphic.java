/**
 * Created with IntelliJ IDEA.
 * User: Модификатор
 * Date: 11.05.13
 * Time: 18:34
 * To change this template use File | Settings | File Templates.
 */
import javax.swing.*;
import java.awt.*;
public class Graphic extends JPanel implements ICollectionListener {
    private Graphics graphic;
    private Thread animator;

    private MarkCollection points;

    final int WIDTH = 300;
    final int HEIGHT = 300;

    final int MARGIN_X = 20;
    final int MARGIN_Y = 20;

    final int VIEWPORT_X = 20;
    final int VIEWPORT_Y = 20;

    final int MARK_SIZE = 10;

    final int POINT_RADIUS = 5;

    final String BG_COLOR = "#f5f5dc"; // Bezheviy
    final String FIGURE_COLOR = "#0000ff"; // Blue
    final String AXIS_COLOR = "#000000";
    final String MARK_INSIDE_COLOR = "#00FF00";
    final String MARK_OUTSIDE_COLOR = "#FF0000";

    private double point_opacity = 1;

    public INotifyable observer;

    public Graphic(MarkCollection points) {
        super();

        this.points = points;
        points.observer = this;
    }

    @Override
    public void paintComponent(Graphics g) {
        this.setPreferredSize(new Dimension(WIDTH + 2 * MARGIN_X, HEIGHT + 2 * MARGIN_Y));
        graphic = g;
        super.paintComponent(g);

        draw();
    }

    public void draw() {
        if(graphic == null) {
            return;
        }

        graphic.setColor(Color.decode(BG_COLOR));
        graphic.fillRect(0, 0, WIDTH + 2 * MARGIN_X, HEIGHT + 2 * MARGIN_Y);

        drawGraphic();
        drawAxis();
        drawMarks();
        this.repaint();
    }

    private void drawAxis() {
        final int CENTER_X = MARGIN_X + WIDTH / 2;
        final int CENTER_Y = MARGIN_Y + HEIGHT / 2;

        int RADIUS_X = WIDTH / 2 * (int)points.getRadius() / VIEWPORT_X;
        int RADIUS_Y = HEIGHT / 2 * (int)points.getRadius() / VIEWPORT_Y;

        final int FULL_VIEWPORT_X = WIDTH + MARGIN_X * 2;
        final int FULL_VIEWPORT_Y = HEIGHT + MARGIN_Y * 2;

        graphic.setColor(Color.decode(AXIS_COLOR));

        // Horizontal and vertical axis
        graphic.drawLine(0, CENTER_Y, FULL_VIEWPORT_X, CENTER_Y);
        graphic.drawLine(CENTER_X, 0, CENTER_X, FULL_VIEWPORT_Y);

        // Arrows
        int sin_arrow_size = (int)(Math.sin(Math.PI / 6) * MARK_SIZE);
        int cos_arrow_size = (int)(Math.cos(Math.PI / 6) * MARK_SIZE);
        graphic.drawLine(CENTER_X, 0, CENTER_X + sin_arrow_size, cos_arrow_size);
        graphic.drawLine(CENTER_X, 0, CENTER_X - sin_arrow_size, cos_arrow_size);

        graphic.drawLine(FULL_VIEWPORT_X, CENTER_Y, FULL_VIEWPORT_X - cos_arrow_size, CENTER_Y + sin_arrow_size);
        graphic.drawLine(FULL_VIEWPORT_X, CENTER_Y, FULL_VIEWPORT_X - cos_arrow_size, CENTER_Y - sin_arrow_size);

        // Marks on the graphic
        drawHorizontalMark(CENTER_X - RADIUS_X);
        drawHorizontalMark(CENTER_X - RADIUS_X / 2);
        drawHorizontalMark(CENTER_X + RADIUS_X / 2);
        drawHorizontalMark(CENTER_X + RADIUS_X);

        drawVerticalMark(CENTER_Y - RADIUS_Y);
        drawVerticalMark(CENTER_Y - RADIUS_Y / 2);
        drawVerticalMark(CENTER_Y + RADIUS_Y / 2);
        drawVerticalMark(CENTER_Y + RADIUS_Y);

        // Labels
        Font font = new Font("Tahoma", Font.BOLD, 13);

        graphic.setFont(font);
        graphic.drawString("x", FULL_VIEWPORT_X - 15, CENTER_Y + 15);
        graphic.drawString("y", CENTER_X + 15, 15);
    }

    private void drawHorizontalMark(int x) {
        final int CENTER_Y = MARGIN_Y + HEIGHT / 2;

        graphic.drawLine(x, CENTER_Y - MARK_SIZE / 2, x, CENTER_Y + MARK_SIZE / 2);
    }

    private void drawVerticalMark(int y) {
        final int CENTER_X = MARGIN_X + WIDTH / 2;

        graphic.drawLine(CENTER_X - MARK_SIZE / 2, y, CENTER_X + MARK_SIZE / 2, y);
    }

    private void drawGraphic() {
        final int CENTER_X = MARGIN_X + WIDTH / 2;
        final int CENTER_Y = MARGIN_Y + HEIGHT / 2;

        int RADIUS_X = WIDTH / 2 * (int)points.getRadius() / VIEWPORT_X;
        int RADIUS_Y = HEIGHT / 2 * (int)points.getRadius() / VIEWPORT_Y;

        graphic.setColor(Color.decode(FIGURE_COLOR));

        Polygon polygon = new Polygon();
        polygon.addPoint(CENTER_X, CENTER_Y);
        polygon.addPoint(CENTER_X, CENTER_Y - RADIUS_Y / 2);
        polygon.addPoint(CENTER_X - RADIUS_X, CENTER_Y);
        graphic.fillPolygon(polygon);
        graphic.fillArc(CENTER_X - RADIUS_X, CENTER_Y - RADIUS_Y, RADIUS_X * 2, RADIUS_Y * 2, 180, 90);
        graphic.fillRect(CENTER_X, CENTER_Y, RADIUS_X, RADIUS_Y);
    }

    private void drawMarks() {
        points.forEach(new MarkIterator() {
            @Override
            public boolean Iterate(Mark mark, boolean isInside) {
                drawMark(mark, isInside);
                return true;
            }
        });
    }

    private void drawMark(Mark m, boolean point_inside) {
        int X_POS = MARGIN_X + WIDTH / 2 + Math.round(WIDTH / VIEWPORT_X *  m.x / 2);
        int Y_POS = MARGIN_Y + HEIGHT / 2 - Math.round(HEIGHT / VIEWPORT_Y * m.y / 2);

        Color innercolor = Color.decode(point_inside ? MARK_INSIDE_COLOR : MARK_OUTSIDE_COLOR);
        graphic.setColor(new Color(innercolor.getRed(), innercolor.getGreen(), innercolor.getBlue(), (int)(point_opacity * 255)));
        graphic.fillOval(X_POS - POINT_RADIUS / 2, Y_POS - POINT_RADIUS / 2, POINT_RADIUS, POINT_RADIUS);
    }

    public void setPointFromCoords(int x, int y) {
        float new_x = (float)(x - MARGIN_X - WIDTH / 2) * VIEWPORT_X / WIDTH * 2;
        float new_y = (float)(MARGIN_Y + HEIGHT / 2 - y) * VIEWPORT_Y / HEIGHT * 2;
        points.add(new Mark(new_x, new_y));
        draw();
    }

    public void Notify() {
        if(animator != null) {
            return;
        }

        animator = new Thread(new Runnable() {
            @Override
            public void run() {
                final int duration = 1000;
                final int delay = 8000;
                final int step = 5;
                final double finalOpacity = 0;
                double startOpacity = point_opacity;
                int counter = 0;

                try {
                    animator.sleep(delay);

                    while(counter < duration) {
                        point_opacity = finalOpacity + (startOpacity - finalOpacity) * (duration - counter) / duration;

                        counter += step;
                        animator.sleep(step);
                    }
                }
                catch (Exception e) {
                    animator = null;
                    point_opacity = 1;
                }
            }
        });
        animator.start();
    }
}
