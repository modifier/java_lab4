import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Created with IntelliJ IDEA.
 * User: Модификатор
 * Date: 11.05.13
 * Time: 21:25
 * To change this template use File | Settings | File Templates.
 */
public class XList extends JPanel {
    private JLabel label;
    private JComboBox x_group;
    private float[] values;

    public XList(float[] values, ActionListener changed) {
        super(new GridLayout(0, 1));

        this.values = values;

        label = new JLabel();
        this.add(label);

        DefaultComboBoxModel model = new DefaultComboBoxModel();
        for(int i = 0; i < values.length; i++) {
            model.addElement(Float.toString(values[i]));
        }

        x_group = new JComboBox();
        x_group.setModel(model);

        x_group.addActionListener(changed);

        this.add(x_group);
    }

    public void setLabel(String label) {
        this.label.setText(label);
    }

    public float getValue() {
        return values[x_group.getSelectedIndex()];
    }
}
