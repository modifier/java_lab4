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
public class RadiusPanel extends JPanel {
    private JSlider slider;

    public RadiusPanel(int min, int max) {
        super(new GridLayout(2, 1));

        JLabel label = new JLabel("Radius:");
        add(label);

        slider = new JSlider(min, max);
        add(slider);
    }

    public float getValue() {
        return slider.getValue();
    }

    public void setValue(float value) {
        slider.setValue((int) value);
    }

    public synchronized void addChangeListener(ChangeListener changed) {
        slider.addChangeListener(changed);
    }
}
