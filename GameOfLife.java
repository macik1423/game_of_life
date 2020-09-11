package life;

import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class GameOfLife extends JFrame {

    private JPanel grid;
    private JPanel left;
    final private int width = 800;
    final private int size = 200;
    private Universe universe;
    private String[][] game;
    private Board board;
    private final AtomicBoolean paused;
    private final AtomicBoolean restart;
    private Thread thread;
    private final JLabel generationLabel;
    private final JLabel aliveLabel;
    private int sleepMs = 600;

    public GameOfLife() {
        this.generationLabel = new JLabel("Generation #1");
        this.generationLabel.setName("GenerationLabel");
        this.universe = new Universe(size);
        this.game = universe.create();
        this.paused = new AtomicBoolean(false);
        this.restart = new AtomicBoolean(false);
        this.aliveLabel = new JLabel("Alive: " + universe.getAlive());
        this.aliveLabel.setName("AliveLabel");
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                createGui();
            }
        });
    }

    public void createGui() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1300, 1300);
        setLocationRelativeTo(null);
        setLayout(new FlowLayout(FlowLayout.LEFT));
        grid = new JPanel();
        grid.setLayout(new BorderLayout());
        add(grid);

        left = createLeftPanel();
        board = new Board(width, game, size, universe);

        grid.add(left, BorderLayout.WEST);
        grid.add(board, BorderLayout.CENTER); //implicit paint board

        setVisible(true);
    }

    private void pause() {
        paused.set(true);
    }

    private void restart() {
        restart.set(true);
        universe = new Universe(size);
        game = universe.create();
        grid.remove(board);
        board = new Board(width, game, size, universe);
        grid.add(board);
        grid.revalidate();
        universe.setGenerationCounter(1);
        aliveLabel.setText("Alive: " + universe.getAlive());
        generationLabel.setText("Generation #" + universe.getGenerationCounter());
        System.out.println("restart");
    }

    private void play() {
        paused.set(false);
        restart.set(false);
        Runnable r = () -> {
            try {
                while (!Thread.interrupted()) {
                    if (paused.get()) {
                        synchronized (thread) {
                            try {
                                thread.wait();
                            } catch (InterruptedException e) {

                            }
                        }
                    } if (restart.get()) {
                        universe.setGenerationCounter(1);
                        generationLabel.setText("Generation #" + universe.getGenerationCounter());
                    } else {
                        synchronized (thread) {
                            thread.notify();
                        }
                        universe.increaseGenerationCounter();
                        generationLabel.setText("Generation #" + universe.getGenerationCounter());

                        board.repaint();
                        Thread.sleep(10);
                        aliveLabel.setText("Alive: " + universe.getAlive());

                        Thread.sleep(sleepMs);
                    }

                }
            } catch (InterruptedException e) {
            }
        };
        if (thread == null) {
            thread = new Thread(r);
            thread.start();
        } else {
            synchronized (thread) {
                thread.notify();
            }
        }
        System.out.println(thread.getId());
    }


    @NotNull
    public JPanel createLeftPanel() {
        JPanel left = new JPanel();
//        left.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLUE), "Left"));
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));

        JPanel buttons = createButtonsPanel(left);
        left.add(buttons);

        JPanel statistics = createStatisticsPanel();
        left.add(statistics);

        JPanel slider = createSlider();
        left.add(slider);

        return left;
    }

    public JPanel createSlider() {
        JSlider slider = new Potentiometer(sleepMs);
        slider.setAlignmentX(LEFT_ALIGNMENT);
        JPanel sliderPanel = new JPanel();

        sliderPanel.setLayout(new BoxLayout(sliderPanel, BoxLayout.Y_AXIS));
        sliderPanel.setAlignmentX(RIGHT_ALIGNMENT);
//        sliderPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLUE), "slider"));

        JLabel sliderLabel = new JLabel("Speed mode:");
        sliderLabel.setAlignmentX(LEFT_ALIGNMENT);
//        sliderLabel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLUE), "sliderLabel"));

        slider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent changeEvent) {
                sleepMs = ((JSlider) changeEvent.getSource()).getValue();
            }
        });

        sliderPanel.add(sliderLabel);
        sliderPanel.add(slider);
        return sliderPanel;
    }

    @NotNull
    public JPanel createStatisticsPanel() {
        JPanel statistics = new JPanel();
        statistics.setLayout(new GridLayout(2, 1, 1, 1));

//        statistics.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLUE), "statistics"));
        generationLabel.setBorder(new EmptyBorder(0,0,0,0));
        statistics.add(generationLabel);

        statistics.add(aliveLabel);
        statistics.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        return statistics;
    }

    public JPanel createButtonsPanel(JPanel left) {
        JPanel buttons = new JPanel();
//        buttons.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLUE), "buttons"));
        buttons.setLayout(new GridLayout(1, 3, 5, 1));
        buttons.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        left.add(buttons);
        setButtons(buttons);
        return buttons;
    }

    public void setButtons(JPanel buttons) {
        JButton play = new JButton("\u27a8");
        play.setName("PlayToggleButton");
        Font playFont = new Font(play.getFont().getName(), play.getFont().getStyle(), 30);
        play.setFont(playFont);
        buttons.add(play);

        JButton pause = new JButton("\u2758\u2758");
        Font pauseFont = new Font(pause.getFont().getName(), pause.getFont().getStyle(), 30);
        pause.setFont(pauseFont);
        pause.setEnabled(false);
        buttons.add(pause);

        JButton restart = new JButton("\u27f3");
        restart.setName("ResetButton");
        Font restartFont = new Font(restart.getFont().getName(), restart.getFont().getStyle(), 30);
        restart.setFont(restartFont);
        buttons.add(restart);

        play.addActionListener(event -> {
            play();
            play.setEnabled(false);
            pause.setEnabled(true);
        });

        pause.addActionListener(event -> {
            pause();
            pause.setEnabled(false);
            play.setEnabled(true);
        });

        restart.addActionListener(event -> {
            restart();
            play.setEnabled(true);
            pause.setEnabled(false);
        });
    }



}
