package gui;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import surfaces.ParametricSurface3D;

/**
 * Class for encapsulating check box panel controlling the set of plotting modes
 * and numerical methods. One of the "Controller" family classes
 */
public class CheckBoxPanel extends JPanel implements ActionListener {
	
	private static final long serialVersionUID 	= 1L;
	private GLFrame frame;
	
	/**
	 * Constructs a new check box panel
	 * 
	 * @param frame Frame containing GLCanvas
	 */
	public CheckBoxPanel(GLFrame frame) {
		this.frame = frame;
		this.setLayout(new FlowLayout(FlowLayout.LEFT));
		JCheckBox chB1 = new JCheckBox(Renderer.RENDER_LINES);
		chB1.setSelected(true);
		chB1.addActionListener(this);
		chB1.setFocusable(false);
		JCheckBox chB2 = new JCheckBox(Renderer.RENDER_POINTS);
		chB2.addActionListener(this);
		chB2.setFocusable(false);
		JCheckBox chB3 = new JCheckBox(Renderer.RENDER_TRIANGLES);
		chB3.setSelected(true);
		chB3.addActionListener(this);
		chB3.setFocusable(false);
		JCheckBox chB4 = new JCheckBox(Renderer.RENDER_AXES);
		chB4.setSelected(true);
		chB4.addActionListener(this);
		chB4.setFocusable(false);
		JCheckBox chB5 = new JCheckBox(Renderer.RENDER_GRID);
		chB5.addActionListener(this);
		chB5.setFocusable(false);
		JCheckBox chB6 = new JCheckBox(Renderer.RENDER_NUMS);
		chB6.addActionListener(this);
		chB6.setFocusable(false);
		this.add(chB1);
		this.add(chB2);
		this.add(chB4);
		this.add(chB6);
		this.add(chB5);
		this.add(chB3);
		
		JRadioButton jRb1 = new JRadioButton(ParametricSurface3D.TRAPEZOID);
		jRb1.setFocusable(false);
		jRb1.addActionListener(this);
		jRb1.setSelected(true);
		JRadioButton jRb2 = new JRadioButton(ParametricSurface3D.SIMPSON);
		jRb2.setFocusable(false);
		jRb2.addActionListener(this);
		ButtonGroup bG = new ButtonGroup();
		bG.add(jRb1);
		bG.add(jRb2);
		this.add(jRb1);
		this.add(jRb2);
	}
	
	/**
	 * Method for handling button action events
	 * 
	 * @param e Event to be handled
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() instanceof JCheckBox) {
			JCheckBox chB = (JCheckBox) e.getSource();
			frame.switchRendering(chB.getText(), chB.isSelected());
		} else {
			JRadioButton jRb = (JRadioButton) e.getSource();
			ParametricSurface3D.switchNumericalMethod(jRb.getText());
		}
	}

}
