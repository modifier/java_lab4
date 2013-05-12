/**
 * Created with IntelliJ IDEA.
 * User: Модификатор
 * Date: 11.05.13
 * Time: 15:38
 * To change this template use File | Settings | File Templates.
 */
import java.awt.EventQueue;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class MainController implements INotifyable {
    public float[] y_values = new float[]{-4, 2, 4};
    public float[] x_values = new float[]{-3, -2, 1, 2, 5};

    private MarkCollection points;

    private Graphic graphic;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    MainController controller = new MainController();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public MainController() {
        points = new MarkCollection();
        points.setRadius(15);

        initializeWindow();
    }

    private void initializeWindow() {
        MainView window = new MainView();
        window.setVisible();

        window.setPoints(points);
        window.setRadiusListener(new Radius_Changed(this));

        window.setXValues(x_values);
        window.setYValues(y_values);
        window.setRadius(points.getRadius());

        graphic = new Graphic(points);
        graphic.observer = this;
        graphic.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                graphic.setPointFromCoords(e.getX(), e.getY());
            }

            @Override
            public void mousePressed(MouseEvent e) {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public void mouseExited(MouseEvent e) {
                //To change body of implemented methods use File | Settings | File Templates.
            }
        });
        window.setGraphic(graphic);
    }

    public void refresh() {
        if(graphic == null) {
            return;
        }
        graphic.draw();
    }

    public void setRadius(float radius) {
        this.points.setRadius(radius);
        refresh();
    }

    public void Notify(Mark mark) {
        points.add(mark);
        refresh();
    }
}

