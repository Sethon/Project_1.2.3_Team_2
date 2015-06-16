package plyer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

import surfaces.*;

public class NRBReader {

     private double magnFactor;

     public NRBReader(double magnFactor) {
          this.magnFactor = magnFactor;
     }

     public NURBS getNURBS(String filename) {
          int degreeU = 0;
          int degreeV = 0;
          ArrayList<Double> knotsU = new ArrayList<>();
          ArrayList<Double> knotsV = new ArrayList<>();
          ArrayList<ArrayList<WPoint3D>> controlNet = new ArrayList<>();

          try {
               BufferedReader br = new BufferedReader(new FileReader(filename));
               String line;
               while (!(line = br.readLine()).equals("end_header")) {
                    //System.out.println(line);
                    if (line.contains("degreeU")) {
                         String[] split = line.split(" ");
                         degreeU = Integer.parseInt(split[1]);
                    } else if (line.contains("degreeV")) {
                         String[] split = line.split(" ");
                         degreeV = Integer.parseInt(split[1]);
                    }
               }
               line = br.readLine();
               String[] split = line.split(" ");
               for (String i : split)
                    knotsU.add(Double.parseDouble(i));
               line = br.readLine();
               split = line.split(" ");
               for (String i : split)
                    knotsV.add(Double.parseDouble(i));

               while (!(line = br.readLine()).equals("end_file")) {
                    ArrayList<WPoint3D> pList = new ArrayList<>();
                    split = line.split("-");
                    for (String i : split) {
                         String[] j = i.split(" ");
                         pList.add(new WPoint3D(         Double.parseDouble(j[0]),
                                                         Double.parseDouble(j[1]),
                                                         Double.parseDouble(j[2]),
                                                         Double.parseDouble(j[3])));
                    }
                    controlNet.add(pList);
               }
               br.close();
          } catch (Exception e) {
          }
          return new NURBS(degreeU,degreeV,controlNet,knotsU,knotsV);
     }
}
