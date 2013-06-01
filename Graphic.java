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
    private Thread animator;

    private MarkCollection points;

    final boolean SCALE_POINTS_INSTEAD_OF_FIGURE = false;

    final int WIDTH = 250;
    final int HEIGHT = 250;

    final int MARGIN_X = 50;
    final int MARGIN_Y = 50;

    final int VIEWPORT_X = 20;
    final int VIEWPORT_Y = 20;

    int viewport_x = VIEWPORT_X;
    int viewport_y = VIEWPORT_Y;

    final int MARK_SIZE = 10;

    final int POINT_RADIUS = 5;

    final String BG_COLOR = "#66ff00"; // Light green
    final String FIGURE_COLOR = "#013220"; // Dark green
    final String AXIS_COLOR = "#000000";
    final String MARK_INSIDE_COLOR = "#00FF00";
    final String MARK_OUTSIDE_COLOR = "#FF0000";

    private double point_opacity = 1;
    private int point_radius = POINT_RADIUS;

    public Graphic(MarkCollection points) {
        super();

        setPreferredSize(new Dimension(WIDTH + 2 * MARGIN_X, HEIGHT + 2 * MARGIN_Y));

        this.points = points;
        points.addObserver(this);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        draw(g);
    }

    public void draw(Graphics g) {
        if(g == null) {
            return;
        }

        if(SCALE_POINTS_INSTEAD_OF_FIGURE) {
            viewport_x = (int)points.getRadius();
            viewport_y = (int)points.getRadius();
        }

        g.setColor(Color.decode(BG_COLOR));
        g.fillRect(0, 0, WIDTH + 2 * MARGIN_X, HEIGHT + 2 * MARGIN_Y);

        drawGraphic(g);
        drawAxis(g);
        drawMarks(g);
    }

    private void drawAxis(Graphics g) {
        final int CENTER_X = MARGIN_X + WIDTH / 2;
        final int CENTER_Y = MARGIN_Y + HEIGHT / 2;

        int RADIUS_X = WIDTH / 2 * (int)points.getRadius() / viewport_x;
        int RADIUS_Y = HEIGHT / 2 * (int)points.getRadius() / viewport_y;

        final int FULL_VIEWPORT_X = WIDTH + MARGIN_X * 2;
        final int FULL_VIEWPORT_Y = HEIGHT + MARGIN_Y * 2;

        g.setColor(Color.decode(AXIS_COLOR));

        // Horizontal and vertical axis
        g.drawLine(0, CENTER_Y, FULL_VIEWPORT_X, CENTER_Y);
        g.drawLine(CENTER_X, 0, CENTER_X, FULL_VIEWPORT_Y);

        // Arrows
        int sin_arrow_size = (int)(Math.sin(Math.PI / 6) * MARK_SIZE);
        int cos_arrow_size = (int)(Math.cos(Math.PI / 6) * MARK_SIZE);
        g.drawLine(CENTER_X, 0, CENTER_X + sin_arrow_size, cos_arrow_size);
        g.drawLine(CENTER_X, 0, CENTER_X - sin_arrow_size, cos_arrow_size);

        g.drawLine(FULL_VIEWPORT_X, CENTER_Y, FULL_VIEWPORT_X - cos_arrow_size, CENTER_Y + sin_arrow_size);
        g.drawLine(FULL_VIEWPORT_X, CENTER_Y, FULL_VIEWPORT_X - cos_arrow_size, CENTER_Y - sin_arrow_size);

        // Marks on the graphic
        drawHorizontalMark(g, CENTER_X - RADIUS_X);
        drawHorizontalMark(g, CENTER_X - RADIUS_X / 2);
        drawHorizontalMark(g, CENTER_X + RADIUS_X / 2);
        drawHorizontalMark(g, CENTER_X + RADIUS_X);

        drawVerticalMark(g, CENTER_Y - RADIUS_Y);
        drawVerticalMark(g, CENTER_Y - RADIUS_Y / 2);
        drawVerticalMark(g, CENTER_Y + RADIUS_Y / 2);
        drawVerticalMark(g, CENTER_Y + RADIUS_Y);

        // Labels
        Font font = new Font("Tahoma", Font.BOLD, 13);

        g.setFont(font);
        g.drawString("x", FULL_VIEWPORT_X - 15, CENTER_Y + 15);
        g.drawString("y", CENTER_X + 15, 15);
    }

    private void drawHorizontalMark(Graphics g, int x) {
        final int CENTER_Y = MARGIN_Y + HEIGHT / 2;

        g.drawLine(x, CENTER_Y - MARK_SIZE / 2, x, CENTER_Y + MARK_SIZE / 2);
    }

    private void drawVerticalMark(Graphics g, int y) {
        final int CENTER_X = MARGIN_X + WIDTH / 2;

        g.drawLine(CENTER_X - MARK_SIZE / 2, y, CENTER_X + MARK_SIZE / 2, y);
    }

    private void drawGraphic(Graphics g) {
        final int CENTER_X = MARGIN_X + WIDTH / 2;
        final int CENTER_Y = MARGIN_Y + HEIGHT / 2;

        int RADIUS_X = WIDTH / 2 * (int)points.getRadius() / viewport_x;
        int RADIUS_Y = HEIGHT / 2 * (int)points.getRadius() / viewport_y;

        g.setColor(Color.decode(FIGURE_COLOR));

        Polygon rect = new Polygon();
        rect.addPoint(CENTER_X, CENTER_Y);
        rect.addPoint(CENTER_X, CENTER_Y - RADIUS_Y);
        rect.addPoint(CENTER_X + RADIUS_X, CENTER_Y - RADIUS_Y);
        rect.addPoint(CENTER_X + RADIUS_X, CENTER_Y);
        g.fillPolygon(rect);

        g.fillArc(CENTER_X - RADIUS_X, CENTER_Y - RADIUS_Y, RADIUS_X * 2, RADIUS_Y * 2, 0, -90);

        Polygon polygon = new Polygon();
        polygon.addPoint(CENTER_X, CENTER_Y);
        polygon.addPoint(CENTER_X - RADIUS_X / 2, CENTER_Y);
        polygon.addPoint(CENTER_X, CENTER_Y + RADIUS_Y);
        g.fillPolygon(polygon);
    }

    private void drawMarks(final Graphics g) {
        points.forEach(new IMarkIterator() {
            @Override
            public boolean Iterate(Mark mark, boolean isInside) {
                drawMark(g, mark, isInside);
                return true;
            }
        });
    }

    private void drawMark(Graphics g, Mark m, boolean point_inside) {
        int X_POS = MARGIN_X + WIDTH / 2 + Math.round(WIDTH / viewport_x *  m.x / 2);
        int Y_POS = MARGIN_Y + HEIGHT / 2 - Math.round(HEIGHT / viewport_y * m.y / 2);

        Color innercolor = Color.decode(point_inside ? MARK_INSIDE_COLOR : MARK_OUTSIDE_COLOR);
        g.setColor(new Color(innercolor.getRed(), innercolor.getGreen(), innercolor.getBlue(), (int)(point_opacity * 255)));
        g.fillOval(X_POS - point_radius / 2, Y_POS - point_radius / 2, point_radius, point_radius);
    }

    public void setPointFromCoords(int x, int y) {
        float new_x = (float)(x - MARGIN_X - WIDTH / 2) * viewport_x / WIDTH * 2;
        float new_y = (float)(MARGIN_Y + HEIGHT / 2 - y) * viewport_y / HEIGHT * 2;
        points.add(new Mark(new_x, new_y));
        this.repaint();
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
                        repaint();
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
