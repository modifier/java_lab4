import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemListener;
import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: Модификатор
 * Date: 11.05.13
 * Time: 21:18
 * To change this template use File | Settings | File Templates.
 */
public class YPanel extends JPanel {
    private JLabel label;
    private ArrayList<JCheckBox> checkBoxes;
    private float[] values;

    public YPanel(float[] values, ItemListener changed) {
        super(new GridLayout(0, 1));
        this.values = values;

        label = new JLabel();
        this.add(label);

        checkBoxes = new ArrayList<JCheckBox>();

        for(int i = 0; i < values.length; i++) {
            JCheckBox btn = new JCheckBox(Float.toString(values[i]));
            btn.addItemListener(changed);
            checkBoxes.add(btn);

            this.add(btn);
        }
    }

    public void setLabel(String label) {
        this.label.setText(label);
    }

    public ArrayList<Float> getValues() {
        ArrayList<Float> result = new ArrayList<Float>();
        for(int i = 0; i < checkBoxes.size(); i++) {
            if(checkBoxes.get(i).isSelected()) {
                result.add(values[i]);
            }
        }
        return result;
    }
}
