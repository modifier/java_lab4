import javax.swing.*;
import javax.swing.event.ChangeListener;
import java.awt.*;

/**
 * Created with IntelliJ IDEA.
 * User: Модификатор
 * Date: 11.05.13
 * Time: 22:57
 * To change this template use File | Settings | File Templates.
 */
public class RadiusList extends JPanel {
    private JSlider slider;

    public RadiusList(float min, float max) {
        super();
        slider = new JSlider();
        slider.setMinimum((int)min);
        slider.setMaximum((int)max);

        add(slider);
    }

    public float getValue() {
        return slider.getValue();
    }

    public void setValue(float value) {
        slider.setValue((int)value);
    }

    public synchronized void addChangeListener(ChangeListener changed) {
        slider.addChangeListener(changed);
    }
}
