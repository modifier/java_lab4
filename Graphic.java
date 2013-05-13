/**
 * Created with IntelliJ IDEA.
 * User: Модификатор
 * Date: 11.05.13
 * Time: 18:34
 * To change this template use File | Settings | File Templates.
 */
import javax.swing.*;
import java.awt.*;
import java.util.Observable;
import java.util.Observer;

public class Graphic extends JPanel implements Observer {
    private Graphics graphic;
    private Thread animator;

    private MarkCollection points;

    final boolean SCALE_POINTS_INSTEAD_OF_FIGURE = false;

    final int WIDTH = 300;
    final int HEIGHT = 300;

    final int MARGIN_X = 20;
    final int MARGIN_Y = 20;

    final int VIEWPORT_X = 30;
    final int VIEWPORT_Y = 30;

    int viewport_x = VIEWPORT_X;
    int viewport_y = VIEWPORT_Y;

    final int MARK_SIZE = 10;

    final int POINT_RADIUS = 5;

    final String BG_COLOR = "#F9FCBA"; // Light yellow
    final String FIGURE_COLOR = "#946B51"; // Brown
    final String AXIS_COLOR = "#000000";
    final String MARK_INSIDE_COLOR = "#00FF00";
    final String MARK_OUTSIDE_COLOR = "#FF0000";

    private double point_opacity = 1;
    private int point_radius = POINT_RADIUS;

    public Graphic(MarkCollection points) {
        super();

        this.points = points;
        points.addObserver(this);
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

        if(SCALE_POINTS_INSTEAD_OF_FIGURE) {
            viewport_x = (int)points.getRadius();
            viewport_y = (int)points.getRadius();
        }

        graphic.setColor(Color.decode(BG_COLOR));
        graphic.fillRect(0, 0, WIDTH + 2 * MARGIN_X, HEIGHT + 2 * MARGIN_Y);

        drawGraphic();
        drawAxis();
        drawMarks();
    }

    private void drawAxis() {
        final int CENTER_X = MARGIN_X + WIDTH / 2;
        final int CENTER_Y = MARGIN_Y + HEIGHT / 2;

        int RADIUS_X = WIDTH / 2 * (int)points.getRadius() / viewport_x;
        int RADIUS_Y = HEIGHT / 2 * (int)points.getRadius() / viewport_y;

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

        int RADIUS_X = WIDTH / 2 * (int)points.getRadius() / viewport_x;
        int RADIUS_Y = HEIGHT / 2 * (int)points.getRadius() / viewport_y;

        graphic.setColor(Color.decode(FIGURE_COLOR));

        graphic.fillRect(CENTER_X, CENTER_X, -RADIUS_X, -RADIUS_Y / 2);
        graphic.fillArc(CENTER_X - RADIUS_X / 2, CENTER_Y - RADIUS_Y / 2, RADIUS_X, RADIUS_Y, 0, 90);
        Polygon polygon = new Polygon();
        polygon.addPoint(CENTER_X, CENTER_Y);
        polygon.addPoint(CENTER_X + RADIUS_X / 2, CENTER_Y);
        polygon.addPoint(CENTER_X, CENTER_Y + RADIUS_Y);
        graphic.fillPolygon(polygon);
    }

    private void drawMarks() {
        points.forEach(new IMarkIterator() {
            @Override
            public boolean Iterate(Mark mark, boolean isInside) {
                drawMark(mark, isInside);
                return true;
            }
        });
    }

    private void drawMark(Mark m, boolean point_inside) {
        int X_POS = MARGIN_X + WIDTH / 2 + Math.round(WIDTH / viewport_x *  m.x / 2);
        int Y_POS = MARGIN_Y + HEIGHT / 2 - Math.round(HEIGHT / viewport_y * m.y / 2);

        Color innercolor = Color.decode(point_inside ? MARK_INSIDE_COLOR : MARK_OUTSIDE_COLOR);
        graphic.setColor(new Color(innercolor.getRed(), innercolor.getGreen(), innercolor.getBlue(), (int)(point_opacity * 255)));
        graphic.fillOval(X_POS - point_radius / 2, Y_POS - point_radius / 2, point_radius, point_radius);
    }

    public void setPointFromCoords(int x, int y) {
        float new_x = (float)(x - MARGIN_X - WIDTH / 2) * viewport_x / WIDTH * 2;
        float new_y = (float)(MARGIN_Y + HEIGHT / 2 - y) * viewport_y / HEIGHT * 2;
        points.add(new Mark(new_x, new_y));
        draw();
    }

    public void animate() {
        if(animator != null) {
            return;
        }

        animator = new Thread(new Runnable() {
            @Override
            public void run() {
                float start_size = points.getRadius() / 14 * WIDTH / viewport_x;
                float end_size = points.getRadius() / 24 * WIDTH / viewport_y;
                final int duration = 500;
                final int step = 5;
                boolean grow = false;
                int counter = 0;

                try {
                    while(true) {
                        if(counter > duration) {
                            counter = 0;
                            grow = !grow;
                        }
                        if(grow) {
                            point_radius = (int)(start_size + (end_size - start_size) * counter / duration);
                        } else {
                            point_radius = (int)(start_size + (end_size - start_size) * (duration - counter) / duration);
                        }

                        counter += step;
                        animator.sleep(step);
                    }
                }
                catch (Exception e) {
                    animator = null;
                    point_radius = POINT_RADIUS;
                }
            }
        });
        animator.start();
    }

    @Override
    public void update(Observable o, Object arg) {
        String action = (String)arg;
        if(action.equals("animate")) {
            animate();
        } else if(action.equals("change")) {
            repaint();
        }
    }
}
