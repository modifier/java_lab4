import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * Created with IntelliJ IDEA.
 * User: Модификатор
 * Date: 11.05.13
 * Time: 22:58
 * To change this template use File | Settings | File Templates.
 */
public class Radius_Changed implements ChangeListener {
    private MainController controller;
    private FloatSlider source;

    public Radius_Changed(MainController controller) {
        this.controller = controller;
    }

    public void setSource(FloatSlider source) {
        this.source = source;
    }

    public void stateChanged(ChangeEvent e) {
        controller.setRadius(source.getValue());
    }
}
