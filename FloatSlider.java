import javax.swing.*;
import java.awt.*;

/**
 * Created with IntelliJ IDEA.
 * User: Модификатор
 * Date: 11.05.13
 * Time: 22:57
 * To change this template use File | Settings | File Templates.
 */
public class FloatSlider extends JSlider {
    public FloatSlider(Radius_Changed changed) {
        super();
        setMinimum(1);
        setMaximum(20);

        changed.setSource(this);
        addChangeListener(changed);
    }
}
