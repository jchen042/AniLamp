/* I declare that this code is my own work */
/* Author <Junxiang Chen> <jchen115@sheffield.ac.uk> */

/*
the main class that will be run
 */

import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.FPSAnimator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Anilamp extends JFrame implements ActionListener {
    private static final int WIDTH = 1280;
    private static final int HEIGHT = 720;
    private static final Dimension dimension = new Dimension(WIDTH, HEIGHT);
    private static GLCanvas glCanvas;
    private static Anilamp_GLEventListener glEventListener;
    private final FPSAnimator fpsAnimator;
    private Camera camera;
    public JButton light1Control, light2Control, lightBulbControl, randomPoseButton, resetButton, jumpButton;

    /**
     * constructor
     *
     * @param textForTitleBar the text displays in the title bar
     */
    public Anilamp(String textForTitleBar) {
        super(textForTitleBar);
        GLCapabilities glCapabilities = new GLCapabilities(GLProfile.get(GLProfile.GL3));
        glCanvas = new GLCanvas(glCapabilities);
        camera = new Camera(Camera.DEFAULT_POSITION, Camera.DEFAULT_TARGET, Camera.DEFAULT_UP);
        glEventListener = new Anilamp_GLEventListener(camera);
        glCanvas.addGLEventListener(glEventListener);
        glCanvas.addKeyListener(new MyKeyboardInput(camera));
        glCanvas.addMouseMotionListener(new MyMouseInput(camera));
        getContentPane().add(glCanvas, BorderLayout.CENTER);

        /*
        jMenuBar
         */
        JMenuBar jMenuBar = new JMenuBar();
        this.setJMenuBar(jMenuBar);
        JMenu fileMenu = new JMenu("File");
        JMenuItem quitItem = new JMenuItem("Quit");
        quitItem.addActionListener(this);
        fileMenu.add(quitItem);
        jMenuBar.add(fileMenu);

        /*
        jPanel and jButton
         */
        JPanel jPanel = new JPanel();
        light1Control = new JButton("Light 1 On/Off");
        light1Control.addActionListener(this);
        light2Control = new JButton("Light 2 On/Off");
        light2Control.addActionListener(this);
        lightBulbControl = new JButton("Lamp On/Off");
        lightBulbControl.addActionListener(this);
        randomPoseButton = new JButton("Random Pose");
        randomPoseButton.addActionListener(this);
        resetButton = new JButton("Reset");
        resetButton.addActionListener(this);
        jumpButton = new JButton("Jump");
        jumpButton.addActionListener(this);
        jPanel.add(light1Control);
        jPanel.add(light2Control);
        jPanel.add(lightBulbControl);
        jPanel.add(randomPoseButton);
        jPanel.add(resetButton);
        jPanel.add(jumpButton);
        this.add(jPanel, BorderLayout.SOUTH);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                fpsAnimator.stop();
                remove(glCanvas);
                dispose();
                System.exit(0);
            }
        });
        fpsAnimator = new FPSAnimator(glCanvas, 60);
        fpsAnimator.start();
    }

    /**
     * MAIN
     *
     * @param args default argument in the main function
     */
    public static void main(String[] args) {
        Anilamp aniLamp = new Anilamp("ANILAMP -- Final Scene");
        aniLamp.getContentPane().setPreferredSize(dimension);
        aniLamp.pack();
        aniLamp.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        /*
        buttons control lights
         */
        if (e.getActionCommand().equalsIgnoreCase("Lamp On/Off")) {
            glEventListener.LAMP_ON = !glEventListener.LAMP_ON;
        }
        if (e.getActionCommand().equalsIgnoreCase("Light 1 On/Off")) {
            glEventListener.LIGHT1_ON = !glEventListener.LIGHT1_ON;
        }
        if (e.getActionCommand().equalsIgnoreCase("Light 2 On/Off")) {
            glEventListener.LIGHT2_ON = !glEventListener.LIGHT2_ON;
        }

        /*
        handle the disable of "Random Pose" and "Jump" button for 2.5 seconds
         */
        int delay = 2500;
        Timer timer = new Timer(delay, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                randomPoseButton.setEnabled(true);
                jumpButton.setEnabled(true);
            }
        });
        timer.setRepeats(false);

        /*
        buttons control lamp poses and jump motion
         */
        if (e.getActionCommand().equalsIgnoreCase("Random Pose")) {
            glEventListener.setRandomPoseBegin();
            randomPoseButton.setEnabled(false);
            jumpButton.setEnabled(false);
            timer.start();
        }

        if (e.getActionCommand().equalsIgnoreCase("Reset")) {
            glEventListener.resetPose();
        }

        if (e.getActionCommand().equalsIgnoreCase("Jump")) {
            glEventListener.resetPose();
            glEventListener.setAnimationBegin();
            jumpButton.setEnabled(false);
            randomPoseButton.setEnabled(false);
            timer.start();
        }
    }
}
