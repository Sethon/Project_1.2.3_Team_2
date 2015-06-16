package plyer;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import surfaces.*;

public class NRBWriter {
     public void writeNURBS(String filename, NURBS nurbs) {
          try {
               int degreeU = nurbs.getDegreeU();
               int degreeV = nurbs.getDegreeV();
               ArrayList<Double> knotsU = nurbs.getKnotsU();
               ArrayList<Double> knotsV = nurbs.getKnotsV();
               ArrayList<ArrayList<WPoint3D>> controlNet = nurbs.getControlNet();
               BufferedWriter writer = new BufferedWriter(new FileWriter(filename + ".nrb"));
               writer.write("nurbs" + "\n"
                                      + "degreeU " + degreeU + "\n"
                                      + "degreeV " + degreeV + "\n"
                                      + "end_header");
               for (Double i : knotsU) {
                    writer.write(i + " ");
               }
               writer.newLine();
               for (Double i : knotsV) {
                    writer.write(i + " ");
               }
               writer.newLine();
               for (ArrayList<WPoint3D> i : controlNet) {
                    for (WPoint3D j : i) {
                         writer.write(j.getX() + " " + j.getY() + " " + j.getZ() + " " + j.getWeight() + "-");
                    }
                    writer.newLine();
               }
               writer.write("end_file");
               writer.close();
          } catch (IOException e) {
               e.printStackTrace();
          }
     }
}
