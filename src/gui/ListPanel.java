package gui;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import application.SpaceModel;
import surfaces.Surface3D;


public class ListPanel extends JPanel {

	private static final String INFO 				= "Info";
	private static final String REMOVAL 			= "Remove";
	private static final String ADDITION 			= "Add point";
	private static final String ROTATE 				= "Rotate";
	private static final String TRANSLATE 			= "Translate";
	
	private static final double ONE_RAD				= 57.2957795;

	private static final long 	serialVersionUID 	= 1L;
	private static final int 	ROW_HEIGHT 			= 12;

	private JList<String> 		surfaceList;
	private JScrollPane			scrollPane;
	private JPopupMenu			popUp;
	private double 				screenWidth;
	private double 				screenHeight;
	private SpaceModel			model;
	private String 				currentChoice;


	public ListPanel() {
		super();

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		screenWidth = screenSize.getWidth();
		screenHeight = screenSize.getHeight();

		surfaceList = new JList<>(new String[] {});
		surfaceList.setFixedCellHeight(ROW_HEIGHT);
		surfaceList.setPreferredSize(new Dimension((int) (screenWidth/8 - 20), 
				(int) (screenHeight - 210)));

		MouseListener mouseListener = new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				JList<String> theList = (JList<String>) e.getSource();
				if (SwingUtilities.isRightMouseButton(e)) {
					int index = theList.locationToIndex(e.getPoint());
					if (index >= 0) {
						String o = theList.getModel().getElementAt(index);
						popUp.show(surfaceList, e.getX(), e.getY());
						currentChoice = o;
					}
				} else {
					int index = theList.locationToIndex(e.getPoint());
					if (index >= 0) {
						model.higlightStructure(theList.getSelectedValue());
					}
				}
			}
		};
		surfaceList.addMouseListener(mouseListener);

		surfaceList.setFocusable(false);

		scrollPane = new JScrollPane(surfaceList);
		scrollPane.setPreferredSize(new Dimension((int) (screenWidth/8), 
				(int) (screenHeight - 190)));
		
		this.add(scrollPane);
		this.setBorder(BorderFactory.createTitledBorder("Surfaces"));
		this.setFocusable(false);

		ActionListener itemListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JMenuItem item = (JMenuItem) e.getSource();
				String name = item.getText();

				switch (name) {
				case INFO:
					String info = model.getSurfaceInfo(currentChoice);
					JOptionPane.showMessageDialog(null, info, "Surface Info",
							JOptionPane.PLAIN_MESSAGE);
					break;
				case REMOVAL:
					model.removeSurface(currentChoice);
					String[] tmp = new String[surfaceList.getModel().getSize() - 1];
					boolean found = false;
					for (int i = 0; i < surfaceList.getModel().getSize(); i++) {
						if (!surfaceList.getModel().getElementAt(i).equals(currentChoice)) {
							if (!found) {
								tmp[i] = surfaceList.getModel().getElementAt(i);
							} else {
								tmp[i - 1] = surfaceList.getModel().getElementAt(i);
							}
						} else {
							found = true;
						}
					}
					surfaceList.setListData(tmp);

					if (tmp.length < screenHeight/ROW_HEIGHT) {
						surfaceList.setPreferredSize(new Dimension(surfaceList.getWidth(), 
								(int) (surfaceList.getHeight() - ROW_HEIGHT)));
					}
					break;
				case ADDITION:
					if (model.isEditable(currentChoice)) {
						JTextField xField = new JTextField(5);
						JTextField yField = new JTextField(5);
						JTextField zField = new JTextField(5);

						JPanel myPanel = new JPanel();
						myPanel.setLayout(new GridLayout(6, 1));
						myPanel.add(new JLabel("x = "));
						myPanel.add(xField);
						myPanel.add(new JLabel("y = "));
						myPanel.add(yField);
						myPanel.add(new JLabel("z = "));
						myPanel.add(zField);

						int result = JOptionPane.showConfirmDialog(null, myPanel, 
								"Enter coordinates", JOptionPane.OK_CANCEL_OPTION);
						if (result == JOptionPane.OK_OPTION) {
							String regexDecimal = "^-?\\d*\\.\\d+$";
							String regexInteger = "^-?\\d+$";
							String regexDouble = regexDecimal + "|" + regexInteger;
							String x = xField.getText();
							String y = yField.getText();
							String z = zField.getText();
							if (x.matches(regexDouble) && y.matches(regexDouble) &&
									z.matches(regexDouble)) {
								double xCoord = Double.parseDouble(x);
								double yCoord = Double.parseDouble(y);
								double zCoord = Double.parseDouble(z);
								model.addVertex(xCoord, yCoord, zCoord, currentChoice);
							}
						}
					} else {
						throwWrongSurfDialog();
					}
					break;
				case ROTATE:
					if (model.isEditable(currentChoice)) {
						JTextField phiField = new JTextField(4);

						JPanel myPanel = new JPanel();
						myPanel.setLayout(new GridLayout(5, 1));
						JRadioButton b1 = new JRadioButton("X");
						b1.setSelected(true);
						myPanel.add(b1);
						JRadioButton b2 = new JRadioButton("Y");
						myPanel.add(b2);
						JRadioButton b3 = new JRadioButton("Z");
						myPanel.add(b3);
						
						ButtonGroup bG = new ButtonGroup();
						bG.add(b1);
						bG.add(b2);
						bG.add(b3);
					
						myPanel.add(new JLabel("φ = "));
						myPanel.add(phiField);
						
						int result = JOptionPane.showConfirmDialog(null, myPanel, 
								"Enter rotation angle in °", JOptionPane.OK_CANCEL_OPTION);
						if (result  == JOptionPane.OK_OPTION) {
							String angle = phiField.getText();
							String regexDecimal = "^-?\\d*\\.\\d+$";
							String regexInteger = "^-?\\d+$";
							String regexDouble = regexDecimal + "|" + regexInteger;
							if (angle.matches(regexDouble)) {
								String axis = "";
								if (b1.isSelected()) {
									axis = "X";
								} 
								else if (b2.isSelected()) {
									axis = "Y";
								}
								else if (b3.isSelected()) {
									axis = "Z";
								}
								double phi = Double.parseDouble(angle) / ONE_RAD;
								model.rotate(currentChoice, axis, phi);
							}
						}
					} else {
						throwWrongSurfDialog();	
					}
					
					break;
					
				case TRANSLATE:
					if (model.isEditable(currentChoice)) {
						JTextField deltaField = new JTextField(4);

						JPanel myPanel = new JPanel();
						myPanel.setLayout(new GridLayout(5, 1));
						JRadioButton b1 = new JRadioButton("X");
						b1.setSelected(true);
						myPanel.add(b1);
						JRadioButton b2 = new JRadioButton("Y");
						myPanel.add(b2);
						JRadioButton b3 = new JRadioButton("Z");
						myPanel.add(b3);
						
						ButtonGroup bG = new ButtonGroup();
						bG.add(b1);
						bG.add(b2);
						bG.add(b3);
					
						myPanel.add(new JLabel("Δ = "));
						myPanel.add(deltaField);
						
						int result = JOptionPane.showConfirmDialog(null, myPanel, 
								"Translation", JOptionPane.OK_CANCEL_OPTION);
						if (result  == JOptionPane.OK_OPTION) {
							String angle = deltaField.getText();
							String regexDecimal = "^-?\\d*\\.\\d+$";
							String regexInteger = "^-?\\d+$";
							String regexDouble = regexDecimal + "|" + regexInteger;
							if (angle.matches(regexDouble)) {
								String axis = "";
								if (b1.isSelected()) {
									axis = "X";
								} 
								else if (b2.isSelected()) {
									axis = "Y";
								}
								else if (b3.isSelected()) {
									axis = "Z";
								}
								double delta = Double.parseDouble(angle);
								model.translate(currentChoice, axis, delta);
							}
						}
					} else {
						throwWrongSurfDialog();	
					}
					
					break;
				}
			}
			
			private void throwWrongSurfDialog() {
				JOptionPane.showMessageDialog(null, 
						"Surface is not dynamically editable!",
						"Wrong Surface", JOptionPane.ERROR_MESSAGE);
			}
		};

		popUp = new JPopupMenu();
		JMenuItem item1 = new JMenuItem(INFO);
		item1.addActionListener(itemListener);
		popUp.add(item1);
		JMenuItem item2 = new JMenuItem(REMOVAL);
		item2.addActionListener(itemListener);
		popUp.add(item2);
		JMenuItem item3 = new JMenuItem(ADDITION);
		item3.addActionListener(itemListener);
		popUp.add(item3);
		JMenuItem item4 = new JMenuItem(ROTATE);
		item4.addActionListener(itemListener);
		popUp.add(item4);
		JMenuItem item5 = new JMenuItem(TRANSLATE);
		item5.addActionListener(itemListener);
		popUp.add(item5);
	}

	public void addSurface(Surface3D s) {
		String[] tmp = new String[surfaceList.getModel().getSize() + 1];
		for (int i = 0; i < surfaceList.getModel().getSize(); i++) {
			tmp[i] = surfaceList.getModel().getElementAt(i);
		}
		tmp[tmp.length - 1] = s.getLabel();
		surfaceList.setListData(tmp);

		if (tmp.length > screenHeight/ROW_HEIGHT) {
			surfaceList.setPreferredSize(new Dimension(surfaceList.getWidth(), 
					(int) (surfaceList.getHeight() + ROW_HEIGHT)));
		}
	}

	public void attachModel(SpaceModel model) {
		this.model = model;
	}
}
