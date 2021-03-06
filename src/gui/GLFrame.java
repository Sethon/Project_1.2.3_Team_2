package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;

import surfaces.Point3D;
import surfaces.Triangle3D;

import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.FPSAnimator;

/**
 * Class for frame containing GLCanvas
 */
public class GLFrame extends JFrame {
	private static final long serialVersionUID 			= 1L;
	private final static GLProfile PROFILE				= GLProfile.get(GLProfile.GL2);
	private final static GLCapabilities CAPABILITIES 	= new GLCapabilities(PROFILE);
	private final static int FPS_MAX					= 60;
	
	private GLCanvas 	canvas;
	private FPSAnimator animator;
	private Renderer	renderer;
	
	private ArrayList<ArrayList<Triangle3D>> drawables = new ArrayList<>();
	
	/**
	 * Constructs a new frame
	 * 
	 * @param canvasWidth Width for GLCanvas
	 * @param canvasHeight Height for GLCanvas
	 * @param title Title for the frame
	 */
	public GLFrame(int canvasWidth, int canvasHeight, String title) {
		super(title);
		
		setLayout(new BorderLayout());
		
		JPanel chP = new CheckBoxPanel(this);
		add(chP, BorderLayout.SOUTH);
		chP.setFocusable(false);
		
		CAPABILITIES.setBackgroundOpaque(false);
		
		canvas = new GLCanvas(CAPABILITIES);
		renderer = new Renderer();
		canvas.addGLEventListener(renderer);
		canvas.setSize(canvasWidth, canvasHeight);
		canvas.setFocusable(false);
		
		//very important
		animator = new FPSAnimator(canvas, FPS_MAX);
		
		this.setBackground(Color.WHITE);
		
		this.add(canvas, BorderLayout.CENTER);
		
		this.addMouseWheelListener(renderer);
		this.addKeyListener(renderer);
		this.addMouseMotionListener(renderer);
		
		//close operation
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				if (animator.isStarted()) {
					animator.stop();
				}
				System.exit(0);
			}
		});
		
		//setting the size to the content's
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		double frameWidth = screenSize.getWidth();
		double frameHeight = screenSize.getHeight();
		this.setSize((int) frameWidth, (int) frameHeight);
		
		animator.start();
	}
	
	/**
	 * Method for pushing triangulated structure to GLCanvas
	 * 
	 * @param struct List of triangles
	 */
	public void pushStructure(ArrayList<Triangle3D> struct) {
		drawables.add(struct);
		renderer.setTriangles(drawables);
	}
	
	/**
	 * Method for removing triangulated structure from GLCanvas
	 * 
	 * @param index Index within the list
	 */
	public void removeStructure(int index) {
		drawables.remove(index);
		renderer.setTriangles(drawables);
	}
	
	/**
	 * Method for replacing triangulated structure with the specified one
	 * 
	 * @param index Index of the replaceable structure
	 * @param struct New structure
	 */
	public void replaceStructure(int index, ArrayList<Triangle3D> struct) {
		drawables.remove(index);
		if (drawables.size() == 0) {
			drawables.add(struct);
		} else {
			drawables.add(index, struct);	
		}
		renderer.setTriangles(drawables);
	}
	
	/**
	 * Method for switching various view modes
	 * 
	 * @param element Identifier for the mode
	 * @param flag Boolean value for the mode 
	 */
	public void switchRendering(String element, boolean flag) {
		renderer.switchRendering(element, flag);
	}
	
	/**
	 * Method for highlighting the structure
	 * 
	 * @param struct Structure to be highlighted
	 */
	public void highlight(ArrayList<Point3D> struct) {
		renderer.highlight(struct);
	}
}
