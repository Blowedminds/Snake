package ui;

import javax.swing.*;


/**
 * The main application window.
 *
 * it will be reset when grading your project.
 */
public class ApplicationWindow {

    private JFrame frame;
    private GridPanel gridPanel;

    public ApplicationWindow(GridPanel gridPanel) {
        this.gridPanel = gridPanel;
        initialize();
    }

    /**
     * Initialize JFrame
     */
    private void initialize() {
        frame = new JFrame();
        frame.setResizable(false);
        frame.setBounds(50, 50, 500, 520);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.getContentPane().add(gridPanel);
        frame.pack();
    }

    /**
     * get Frame
     *
     * @return frame
     */
    public JFrame getFrame() {
        return frame;
    }

}
