package gui;


import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import application.SpaceModel;
import surfaces.NURBS;
import surfaces.Point3D;
import surfaces.Surface3D;

public class VertexTable extends JFrame implements Runnable {

	private static final long serialVersionUID = 1L;
	private Surface3D 			surface;
	private ArrayList<Point3D>	vertices;

	private int 			rows;
	private int 			cols;
	private JTable			table;
	private SpaceModel		model;	



	public VertexTable(Surface3D surface, SpaceModel model) {
		this.surface = surface;
		this.model = model;
		
		vertices = surface.vertices();
		rows = vertices.size();
		if (surface instanceof NURBS) {
			cols = 4;
		} else {
			cols = 3;
		}

		constructTable();
		table.getRowHeight();

		this.setSize(500, 500);

		JScrollPane pane = new JScrollPane(table);
		pane.setSize(500, (rows + 1) * table.getRowHeight());
		JPanel panel = new JPanel();
		panel.add(pane);
		this.add(panel);
	}

	private void constructTable() {
		String[] columnNames;

		if (cols == 4) {
			columnNames	= new String[] {"X", "Y", "Z", "W"};	
		} else {
			columnNames	= new String[] {"X", "Y", "Z"};
		}

		String[][] data = new String[rows][cols];

		for (int i = 0; i < rows; i++) {
			data[i][0] = "" + vertices.get(i).getX();
			data[i][1] = "" + vertices.get(i).getY(); 
			data[i][2] = "" + vertices.get(i).getZ(); 
		}

		table = new JTable(data, columnNames);
		table.setRowHeight(14);
		table.getModel().addTableModelListener(new TableModelListener() {

			public void tableChanged(TableModelEvent e) {
				if (e.getType() == TableModelEvent.DELETE) {
					if (e.getColumn() == 0) {
						vertices.get(e.getFirstRow()).setX(0);
					}
					else if (e.getColumn() == 1) {
						vertices.get(e.getFirstRow()).setY(0);
					} else {
						vertices.get(e.getFirstRow()).setZ(0);
					}
				} else {
					String regexDecimal = "^-?\\d*\\.\\d+$";
					String regexInteger = "^-?\\d+$";
					String regexDouble = regexDecimal + "|" + regexInteger;
					if (((String) (table.getValueAt(e.getFirstRow(), e.getColumn()))).matches(regexDouble)) {
						double coord = Double.parseDouble((String) (table.getValueAt(e.getFirstRow(), e.getColumn())));
						if (e.getColumn() == 0) {
							vertices.get(e.getFirstRow()).setX(coord);
						}
						else if (e.getColumn() == 1) {
							vertices.get(e.getFirstRow()).setY(coord);
						} else {
							vertices.get(e.getFirstRow()).setZ(coord);
						}
					}
				}
				model.updateStructure((surface).getLabel());
			}
		});
	}

	@Override
	public void run() {
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setResizable(false);
		this.setTitle("Vertices List");
		this.setVisible(true);
	}
}
