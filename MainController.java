/**
 * Created with IntelliJ IDEA.
 * User: Модификатор
 * Date: 11.05.13
 * Time: 15:38
 * To change this template use File | Settings | File Templates.
 */
import java.awt.EventQueue;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Observable;
import java.util.Observer;

public class MainController implements Observer {
    public float[] y_values = new float[]{-4, 2, 4};
    public float[] x_values = new float[]{-3, -2, 1, 2, 5};

    private MarkCollection points;

    private Graphic graphic;

    private MainView window;

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
        points.setRadius(20);

        initializeWindow();
    }

    private void initializeWindow() {
        window = new MainView();
        window.setVisible();

        window.setPoints(points);

        window.setXValues(x_values);
        window.setYValues(y_values);
        window.setRadius(points.getRadius());

        graphic = new Graphic(points);
        graphic.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                graphic.setPointFromCoords(e.getX(), e.getY());
            }
        });
        window.setGraphic(graphic);

        points.addObserver(this);
    }

    public void update(Observable o, Object arg) {
        String action = (String)arg;
        if(action.equals("add")) {
            Mark last = points.last();
            window.setLabel("Recent added point has coordinates: (" + last.x + ", " + last.y + ")");
        }
    }
}

