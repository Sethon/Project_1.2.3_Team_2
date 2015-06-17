package gui;




import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.ArrayList;

import surfaces.Point3D;
import surfaces.Triangle3D;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.glu.GLU;


public class Renderer implements GLEventListener, KeyListener, MouseMotionListener,
MouseWheelListener {
	public final static String RENDER_POINTS 			= "Show vertices";
	public final static String RENDER_TRIANGLES 		= "Coloured plot";
	public final static String RENDER_LINES 			= "Show edges";
	public final static String RENDER_AXES 				= "Show axes";
	public final static String RENDER_GRID 				= "Show XY plane";

	private final float MAX_Z 							= 100000.0f;	// maximal zoom in 
	private final float MIN_Z 							= -100000.0f;	

	private float translateZ 							= -10.0f; 	// z-location
	private float translateX 							= 0.0f; 	
	private float translateY 							= 0.0f; 	
	private float rotateSpeedXIncrement 				= 10;     // adjusting x rotational speed
	private float rotateSpeedYIncrement 				= 10;     // adjusting y rotational speed
	private ArrayList<ArrayList<Triangle3D>> triangles 	= new ArrayList<>();
	private ArrayList<Point3D> highlighted				= new ArrayList<>();
	private GLU glu 									= new GLU();

	private boolean faces 								= true;
	private boolean edges 								= true;
	private boolean vertices 							= false;
	private boolean axes								= true;
	private boolean grid								= false;
	private boolean highlight							= false;

	private float rotateX;    // rotation amounts about axes, controlled by keyboard
	private float rotateY;

	@Override
	public void display(GLAutoDrawable arg0) {
		GL2 gl = arg0.getGL().getGL2();  // get the OpenGL 2 graphics context
		gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT); // clear color and depth buffers

		gl.glLoadIdentity();                // reset the current model-view matrix
		gl.glTranslatef(translateX, translateY, translateZ); // translate 
		gl.glRotatef(rotateX, 1.0f, 0.0f, 0.0f); // rotate about the x-axis
		gl.glRotatef(rotateY, 0.0f, 1.0f, 0.0f); // rotate about the y-axis
		renderTriangles(gl);
	}

	private void renderTriangles(GL2 gl) {
		gl.glLoadIdentity();                // reset the current model-view matrix
		gl.glTranslatef(translateX, translateY, translateZ); // translate 
		gl.glRotatef(rotateX, 1.0f, 0.0f, 0.0f); // rotate about the x-axis
		gl.glRotatef(rotateY, 0.0f, 1.0f, 0.0f); // rotate about the y-axis

		//XY plane grid
		if (grid) {
			gl.glBegin(GL2.GL_LINES);
			gl.glColor3f(0.5f, 0.5f, 0.5f);
			for (float x = - 50; x <= 50; x += 1.0f) {
				gl.glVertex3f(x, -50.0f, 0.0f);
				gl.glVertex3f(x, 50.0f, 0.0f);
			}
			gl.glEnd();

			gl.glBegin(GL2.GL_LINES);
			gl.glColor3f(0.5f, 0.5f, 0.5f);
			for (float y = - 50; y <= 50; y += 1.0f) {
				gl.glVertex3f(-50.0f, y, 0.0f);
				gl.glVertex3f(50.0f, y, 0.0f);
			}
			gl.glEnd();
		}

		//X, Y, Z axes
		if (axes) {
			gl.glBegin(GL2.GL_LINES);
			gl.glColor3f(1.0f, 0.0f, 0.0f);
			gl.glVertex3f(100.0f, 0.0f, 0.0f);
			gl.glVertex3f(-100.0f, 0.0f, 0.0f);
			gl.glEnd();

			gl.glBegin(GL2.GL_LINES);
			gl.glColor3f(0.0f, 1.0f, 0.0f);
			gl.glVertex3f(0.0f, 100.0f, 0.0f);
			gl.glVertex3f(0.0f, -100.0f, 0.0f);
			gl.glEnd();

			gl.glBegin(GL2.GL_LINES);
			gl.glColor3f(0.0f, 0.0f, 1.0f);
			gl.glVertex3f(0.0f, 0.0f, 100.0f);
			gl.glVertex3f(0.0f, 0.0f, -100.0f);
			gl.glEnd();
		}

		for (int k = 0; k < triangles.size(); k++) {
			ArrayList<Triangle3D> trl = triangles.get(k); 
			double zMax = Integer.MIN_VALUE;
			double yMax = Integer.MIN_VALUE;
			double xMax = Integer.MIN_VALUE;
			double zMin = Integer.MAX_VALUE;
			double yMin = Integer.MAX_VALUE;
			double xMin = Integer.MAX_VALUE;


			for (int i = 0; i < trl.size(); i++) {
				ArrayList<Point3D> ps = trl.get(i).vertices();
				for (int j = 0; j < 3; j++) {
					if (ps.get(j).getX() > xMax) {
						xMax = ps.get(j).getX();
					}
					if (ps.get(j).getY() > yMax) {
						yMax = ps.get(j).getY();
					}
					if (ps.get(j).getZ() > zMax) {
						zMax = ps.get(j).getZ();
					}

					if (ps.get(j).getX() < xMin) {
						xMin = ps.get(j).getX();
					}
					if (ps.get(j).getY() < yMin) {
						yMin = ps.get(j).getY();
					}
					if (ps.get(j).getZ() < zMin) {
						zMin = ps.get(j).getZ();
					}
				}
			}

			for (int i = 0; i < trl.size(); i++) {
				ArrayList<Point3D> ps = trl.get(i).vertices();

				//triangles
				if (faces) {
					gl.glBegin(GL2.GL_TRIANGLES);
					gl.glColor3f((float) (Math.abs(ps.get(0).getX())/(xMax - xMin)), 
							(float) (Math.abs(ps.get(0).getY())/(yMax - yMin)), 
							(float) (Math.abs(ps.get(0).getZ())/(zMax - zMin)));
					gl.glVertex3f((float) ps.get(0).getX()/10.0f, 
							(float) ps.get(0).getY()/10.0f, (float) ps.get(0).getZ()/10.0f);
					gl.glColor3f((float) Math.abs((ps.get(1).getX())/(xMax - xMin)), 
							(float) (Math.abs(ps.get(1).getY())/(yMax - yMin)), 
							(float) (Math.abs(ps.get(1).getZ())/(zMax - zMin)));
					gl.glVertex3f((float) ps.get(1).getX()/10.0f, 
							(float) ps.get(1).getY()/10.0f, (float) ps.get(1).getZ()/10.0f);
					gl.glColor3f((float) (Math.abs(ps.get(2).getX())/(xMax - xMin)), 
							(float) (Math.abs(ps.get(2).getY())/(yMax - yMin)), 
							(float) (Math.abs(ps.get(2).getZ())/(zMax - zMin)));
					gl.glVertex3f((float) ps.get(2).getX()/10.0f, 
							(float) ps.get(2).getY()/10.0f, (float) ps.get(2).getZ()/10.0f);
					gl.glColor3f((float) (Math.abs(ps.get(0).getX())/(xMax - xMin)), 
							(float) (Math.abs(ps.get(0).getY())/(yMax - yMin)), 
							(float) (Math.abs(ps.get(0).getZ())/(zMax - zMin)));
					gl.glVertex3f((float) ps.get(0).getX()/10.0f, 
							(float) ps.get(0).getY()/10.0f, (float) ps.get(0).getZ()/10.0f);
					gl.glEnd();
				}

				//lines
				if (edges) {
					gl.glBegin(GL2.GL_LINES);
					gl.glColor3f(0.5f, 0.5f, 0.5f);
					gl.glVertex3f((float) ps.get(0).getX()/10.0f, 
							(float) ps.get(0).getY()/10.0f, (float) ps.get(0).getZ()/10.0f);
					gl.glVertex3f((float) ps.get(1).getX()/10.0f, 
							(float) ps.get(1).getY()/10.0f, (float) ps.get(1).getZ()/10.0f);
					gl.glVertex3f((float) ps.get(2).getX()/10.0f, 
							(float) ps.get(2).getY()/10.0f, (float) ps.get(2).getZ()/10.0f);
					gl.glVertex3f((float) ps.get(0).getX()/10.0f, 
							(float) ps.get(0).getY()/10.0f, (float) ps.get(0).getZ()/10.0f);
					gl.glEnd();
					
					/*//normals
					float x0 = (float) ((ps.get(0).getX() + ps.get(1).getX() + ps.get(2).getX())/3);
					float y0 = (float) ((ps.get(0).getY() + ps.get(1).getY() + ps.get(2).getY())/3);
					float z0 = (float) ((ps.get(0).getZ() + ps.get(1).getZ() + ps.get(2).getZ())/3);
					gl.glBegin(GL2.GL_LINES);
					gl.glVertex3f(x0/10.0f, y0/10.0f, z0/10.0f);
					Vector3D v1 = new Vector3D(ps.get(0).getX(), ps.get(0).getY(), ps.get(0).getZ());
					Vector3D v2 = new Vector3D(ps.get(1).getX(), ps.get(1).getY(), ps.get(1).getZ());
					Vector3D v3 = new Vector3D(ps.get(2).getX(), ps.get(2).getY(), ps.get(2).getZ());
					Vector3D normal = v2.subtract(v1).crossProduct(v3.subtract(v1));
					if (true) {
						System.out.println(v1.dotProduct(normal));
					}
					gl.glVertex3f((float) (normal.getX()/10.0f), (float) (normal.getY()/10.0f), (float) (normal.getZ()/10.0f));
					gl.glEnd();*/
				}

				//points
				if (vertices) {
					gl.glPointSize(10);
					gl.glBegin (GL.GL_POINTS);
					gl.glColor3f(0.5f, 0.5f, 0.5f);
					gl.glVertex3f((float) ps.get(0).getX()/10.0f, 
							(float) ps.get(0).getY()/10.0f, (float) ps.get(0).getZ()/10.0f);
					gl.glVertex3f((float) ps.get(1).getX()/10.0f, 
							(float) ps.get(1).getY()/10.0f, (float) ps.get(1).getZ()/10.0f);
					gl.glVertex3f((float) ps.get(2).getX()/10.0f, 
							(float) ps.get(2).getY()/10.0f, (float) ps.get(2).getZ()/10.0f);
					gl.glVertex3f((float) ps.get(0).getX()/10.0f, (float) ps.get(0).getY()/10.0f, (float) ps.get(0).getZ()/10.0f);
					gl.glEnd();	
				}
			}
		}

		//highlight
		if (highlight) {
			for (Point3D h : highlighted) {
				gl.glPointSize(10);
				gl.glBegin (GL.GL_POINTS);
				gl.glColor3f(0.0f, 1.0f, 1.0f);
				gl.glVertex3f((float) h.getX()/10.0f, 
						(float) h.getY()/10.0f, (float) h.getZ()/10.0f);
				gl.glEnd();	
			}
		}
	}

	public void setTriangles(ArrayList<ArrayList<Triangle3D>> triangles) {
		this.triangles = triangles;
	}

	@Override
	public void dispose(GLAutoDrawable arg0) {

	}

	@Override
	public void init(GLAutoDrawable arg0) {
		final GL2 gl = arg0.getGL().getGL2(); 
		gl.glShadeModel(GL2.GL_SMOOTH); 
		gl.glClearColor(0.9f, 0.95f, 1.0f, 1.0f); 
		gl.glClearDepth(1.0f); 
		gl.glEnable(GL2.GL_DEPTH_TEST);
		gl.glDepthFunc(GL2.GL_LEQUAL);
		gl.glHint(GL2.GL_PERSPECTIVE_CORRECTION_HINT, GL2.GL_NICEST);
	}

	@Override
	public void reshape(GLAutoDrawable arg0, int arg1, int arg2, int arg3,
			int arg4) {
		GL2 gl = arg0.getGL().getGL2();  // get the OpenGL 2 graphics context
		arg4 = (arg4 == 0) ? 1 : arg4;// prevent divide by zero
		float aspect = (float) arg3 / arg4;
		// Set the view port (display area) to cover the entire window
		gl.glViewport(0, 0, arg3, arg4);
		// Setup perspective projection, with aspect ratio matches viewport
		gl.glMatrixMode(GL2.GL_PROJECTION);  // choose projection matrix
		gl.glLoadIdentity();             // reset projection matrix
		glu.gluPerspective(45.0, aspect, 0.1, 100.0); // fovy, aspect, zNear, zFar
		// Enable the model-view transform
		gl.glMatrixMode(GL2.GL_MODELVIEW);
		gl.glLoadIdentity(); // reset

	}

	/*
	 * zoom using mouse wheel
	 */
	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		if (e.getWheelRotation() < 0) {
			translateZ += 0.1f;
		} else {
			translateZ -= 0.1f;
		}
		translateZ = (translateZ > MAX_Z) ? MAX_Z : translateZ;
		translateZ = (translateZ < MIN_Z) ? MIN_Z : translateZ;
	}

	@Override
	public void mouseDragged(MouseEvent e) {

	}

	@Override
	public void mouseMoved(MouseEvent e) {

	}

	@Override
	public void keyTyped(KeyEvent e) {

	}

	@Override
	public void keyPressed(KeyEvent e) {
		switch (e.getKeyCode()) {

		//Zoom
		case KeyEvent.VK_E:   // zoom-out
			translateZ -= 0.25;
			break;
		case KeyEvent.VK_Q: // zoom-in
			translateZ += 0.25;
			break;

			//Movements
		case KeyEvent.VK_W: // move up
			translateY -= 0.0625;
			break;
		case KeyEvent.VK_S: // move down
			translateY += 0.0625;
			break;
		case KeyEvent.VK_A: // move left
			translateX += 0.0625;
			break;
		case KeyEvent.VK_D: // move right
			translateX -= 0.0625;
			break;

			//Rotation
		case KeyEvent.VK_UP:   // rotate up
			rotateX -= rotateSpeedXIncrement;
			break;
		case KeyEvent.VK_DOWN: // rotate down
			rotateX += rotateSpeedXIncrement;
			break;
		case KeyEvent.VK_LEFT:  // rotate left
			rotateY -= rotateSpeedYIncrement;
			break;
		case KeyEvent.VK_RIGHT: // rotate right
			rotateY += rotateSpeedYIncrement;
			break;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {

	}

	public void switchRendering(String element, boolean flag) {
		switch (element) {
		case RENDER_POINTS:
			vertices = flag;
			break;
		case RENDER_TRIANGLES:
			faces = flag;
			break;
		case RENDER_LINES:
			edges = flag;
			break;
		case RENDER_AXES:
			axes = flag;
			break;
		case RENDER_GRID:
			grid = flag;
			break;
		}
	}

	public void highlight(ArrayList<Point3D> struct) {
		if (struct != null) {
			highlighted = struct;
		} else {
			highlighted = new ArrayList<>();
		}
	}
}
