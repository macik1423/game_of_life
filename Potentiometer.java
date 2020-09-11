package life;

import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.util.Hashtable;

public class Potentiometer extends JSlider {
    static final private int MIN = 10;
    static final private int MAX = 1000;
    private int sleepMs;
    public Potentiometer(int sleepMs) {
        this.sleepMs = sleepMs;
        create();
    }

    private void create() {
        this.setMinimum(MIN);
        this.setMaximum(MAX);
        this.setValue(sleepMs);
        this.setMinorTickSpacing(50);
        this.setMajorTickSpacing(250);
        this.setPaintTicks(true);
        this.setLabelTable(getPositions());
        this.setPaintLabels(true);
    }

    @NotNull
    private Hashtable<Integer, JLabel> getPositions() {
        Hashtable<Integer, JLabel> positions = new Hashtable<>();
        positions.put(MIN, new JLabel("" + MIN));
        positions.put(500, new JLabel("500"));
        positions.put(MAX, new JLabel("" + MAX));
        return positions;
    }

}
