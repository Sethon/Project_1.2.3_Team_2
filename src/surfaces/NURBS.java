package surfaces;

import java.util.ArrayList;

public class NURBS extends Surface3D implements EditableSurface {

     private ArrayList<Double> knotsU;
     private ArrayList<Double> knotsV;
     private ArrayList<ArrayList<WPoint3D>> controlNet;
     private int degreeU;
     private int degreeV;
     private int nU;
     private int nV;
     public static final String U = "U";
     public static final String V = "V";

     public NURBS(int degreeU, int degreeV) {
          instCnt++;
          assignLabel();
          this.degreeU = degreeU;
          this.degreeV = degreeV;
          nU = 0;
          nV = 0;
          knotsU = new ArrayList<>();
          knotsV = new ArrayList<>();
          controlNet = new ArrayList<>();
     }

     public NURBS(int degreeU, int degreeV, WPoint3D[][] cP, double[] knotsU, double[] knotsV) {
          instCnt++;
          assignLabel();
          controlNet = new ArrayList<>(cP.length);
          this.knotsU = new ArrayList<>();
          this.knotsV = new ArrayList<>();
          this.degreeU = degreeU;
          this.degreeV = degreeV;
          for (int i = 0; i < cP.length; i++) {
               ArrayList<WPoint3D> lC = new ArrayList<>();
               for (int j = 0; j < cP[0].length; j++) {
                    lC.add(cP[i][j]);
               }
               controlNet.add(lC);
          }
          nU = cP.length;
          nV = cP[0].length;
          for (double i : knotsU)
               this.knotsU.add(i);
          for (double i : knotsV)
               this.knotsV.add(i);
     }

     public NURBS(int degreeU, int degreeV, ArrayList<ArrayList<WPoint3D>> cP, ArrayList<Double> knotsU, ArrayList<Double> knotsV) {
          instCnt++;
          assignLabel();
          this.degreeU = degreeU;
          this.degreeV = degreeV;
          controlNet = cP;
          this.knotsU = knotsU;
          this.knotsV = knotsV;
          nU = cP.size();
          nV = cP.get(0).size();
     }

     public ArrayList<ArrayList<Point3D>> get() {
          ArrayList<ArrayList<Point3D>> cN = new ArrayList<>();
          for (ArrayList<WPoint3D> i : controlNet) {
               ArrayList<Point3D> j = new ArrayList<>();
               j.addAll(i);
               cN.add(j);
          }
          return cN;
     }

     public Point3D getClosest(Point3D p) {

          Point3D close = null;
          double maxDistance = Double.MAX_VALUE;
          double distance;

          for (ArrayList<WPoint3D> i : controlNet) {
               for (Point3D j : i) {
                    if ((distance = j.dist(p)) < maxDistance) {
                         close = j;
                         maxDistance = distance;
                    }
               }
          }
          return close;
     }

     private int findKnotSpanU(double u) {

          if (u == knotsU.get(nU + 1)) return nU - 1;
          int low = degreeU;
          int high = nU + 1;
          int mid = (low + high) / 2;

          while (u < knotsU.get(mid) || u >= knotsU.get(mid + 1)) {
               if (u < knotsU.get(mid))
                    high = mid;
               else
                    low = mid;
               mid = (low + high) / 2;
          }
          return mid;
     }

     private int findKnotSpanV(double v) {

          if (v == knotsV.get(nV + 1)) return nV - 1;
          int low = degreeV;
          int high = nV + 1;
          int mid = (low + high) / 2;

          while (v < knotsV.get(mid) || v >= knotsV.get(mid + 1)) {
               if (v < knotsV.get(mid))
                    high = mid;
               else
                    low = mid;
               mid = (low + high) / 2;
          }
          return mid;
     }

     private double[] basisFunctionsU(double u, int index) {

          double[] bF = new double[degreeU + 1];
          double[] left = new double[degreeU + 1];
          double[] right = new double[degreeU + 1];
          bF[0] = 1;

          for (int i = 1; i <= degreeU; i++) {
               left[i] = u - knotsU.get(index + 1 - i);
               right[i] = knotsU.get(index + i) - u;
               double saved = 0.0;

               for (int j = 0; j < i; j++) {
                    double temp = bF[j] / (right[j + 1] + left[i - j]);
                    bF[j] = saved + right[j + 1] * temp;
                    saved = left[i - j] * temp;
               }
               bF[i] = saved;
          }
          return bF;
     }

     private double[] basisFunctionsV(double v, int index) {

          double[] bF = new double[degreeV + 1];
          double[] left = new double[degreeV + 1];
          double[] right = new double[degreeV + 1];
          bF[0] = 1;

          for (int i = 1; i <= degreeV; i++) {
               left[i] = v - knotsV.get(index + 1 - i);
               right[i] = knotsV.get(index + i) - v;
               double saved = 0.0;

               for (int j = 0; j < i; j++) {
                    double temp = bF[j] / (right[j + 1] + left[i - j]);
                    bF[j] = saved + right[j + 1] * temp;
                    saved = left[i - j] * temp;
               }
               bF[i] = saved;
          }
          return bF;
     }
     
     public Point3D surfacePoint(double u, double v) {
          
          int uSpan = findKnotSpanU(u);
          int vSpan = findKnotSpanV(v);
          double[] bFU = basisFunctionsU(u, uSpan);
          double[] bFV = basisFunctionsV(v, vSpan);

          WPoint3D rP = new WPoint3D(0, 0, 0, 0);
          WPoint3D[] temp = new WPoint3D[degreeU + 1];

          for (int i = 0; i < degreeU + 1; i++) {
               temp[i] = new WPoint3D(0, 0, 0, 0);
               for (int j = 0; j < degreeV + 1; j++) {
                    WPoint3D p = controlNet.get(uSpan - degreeU + i).get(vSpan - degreeV + j);
                    double w = p.getWeight();
                    p = p.multiply(w);
                    p.setWeight(w);
                    temp[i] = temp[i].add(p.multiply(bFV[j]));
               }
          }
          for (int i = 0; i < temp.length; i++) {
               rP = rP.add(temp[i].multiply(bFU[i]));
          }
          return rP.convert();
     }

     public Point3D[][] getVertices(int steps) {
          double u = 0;
          double v = 0;
          double dU = knotsU.get(nU + 1) / steps;
          double dV = knotsV.get(nV + 1) / steps;
          steps++;
          Point3D[][] vertices = new Point3D[steps][steps];

          for (int i = 0; i < steps; i++) {
               for (int j = 0; j < steps; j++) {
                    vertices[i][j] = (surfacePoint(u, v));
                    v += dV;
                    v = Math.round(v * 1000000.0) / 1000000.0;
               }
               u += dU;
               u = Math.round(u * 1000000.0) / 1000000.0;
               v = 0.0;
          }
          return vertices;
     }

     @Override
     public ArrayList<Triangle3D> triangulate() {

          if (controlNet.size() == 0) return new ArrayList<>();
          ArrayList<Triangle3D> triangles = new ArrayList<>();
          Point3D[][] v = getVertices(50);
          int size = v.length;

          for (int i = 0; i < size - 1; i++) {
               for (int j = 0; j < size - 1; j++) {
                    triangles.add(new Triangle3D(v[i][j], v[i + 1][j], v[i][j + 1]));
                    triangles.add(new Triangle3D(v[i + 1][j], v[i][j + 1], v[i + 1][j + 1]));
               }
          }
          return triangles;
     }

     private WPoint3D[][] wPoints(ArrayList<ArrayList<WPoint3D>> controlNet, int uSpan, int vSpan) {

          WPoint3D[][] pW = new WPoint3D[degreeU + 1][degreeV + 1];

          for (int i = 0; i <= degreeU; i++) {
               for (int j = 0; j <= degreeV; j++) {
                    WPoint3D p = controlNet.get(uSpan - degreeU + i).get(vSpan - degreeV + j);
                    double w = p.getWeight();
                    p = p.multiply(w);
                    p.setWeight(w);
                    pW[i][j] = p;
               }
          }
          return pW;
     }

     @Override
     public ArrayList<Point3D> vertices() {
          ArrayList<Point3D> v = new ArrayList<>();
          for (ArrayList<WPoint3D> i : controlNet)
               for (Point3D j : i)
                    v.add(j);
          return v;
     }

     public ArrayList<ArrayList<WPoint3D>> getControlNet() {
          return controlNet;
     }

     @Override
     public double surfaceArea() {
          return 0;
     }

     public void scaleKnotsU(double factor) {
          for (int i = degreeU + 1; i < nU; i++) {
               knotsU.set(i, knotsU.get(i) * factor);
          }
     }

     public void scaleKnotsV(double factor) {
          for (int i = degreeV + 1; i < nV; i++) {
               knotsV.set(i, knotsV.get(i) * factor);
          }
     }

     public void makeUniform() {
          double startU = 1 / (double) (nU - (degreeU + 1));
          for (int i = degreeU + 1; i < nU; i++) {
               knotsU.set(i, startU);
               startU += startU;
          }
          double startV = 1 / (double) (nV - (degreeV + 1));
          for (int i = degreeV + 1; i < nV; i++) {
               knotsV.set(i, startV);
               startV += startV;
          }
     }

     private void addKnot(int direction, double spacing) {
          //spacing should be a
          int d;
          if (direction == 1) {
               d = knotsU.size() - 2 * degreeU;
               double factor = (d - 1) / (double) d;
               scaleKnotsU(factor);
               knotsU.add(nU, 1 * factor);
          } else {
               d = knotsV.size() - 2 * degreeV;
               double factor = (d - 1) / (double) d;
               scaleKnotsV(factor);
               knotsV.add(nV, 1 * factor);
          }
     }

     @Override
     public void addVertex(Point3D p, String instruct) {
          if (controlNet.size() == 0) {
               ArrayList<WPoint3D> curve = new ArrayList<>();
               curve.add(new WPoint3D(p, 1.0));
               controlNet.add(curve);
          } else if (instruct.equals("U")) {
               ArrayList<WPoint3D> newU = new ArrayList<>();
               for (int i = 0; i < nV; i++) {
                    newU.add(new WPoint3D(p, 1.0));
               }
               controlNet.add(newU);
               addKnot(1, 0.5);
               nU++;
          } else {
               for (ArrayList<WPoint3D> i : controlNet) {
                    i.add(new WPoint3D(p, 1.0));
               }
               addKnot(2, 0.5);
               nV++;
          }
     }

     @Override
     public void add2Vertices(Point3D p1, Point3D p2, String instruct, double w) {

          if (controlNet.size() == 0) {
               initiateNurbs(new WPoint3D(p1, w), new WPoint3D(p2, w));
          } else if (instruct.equals("U")) {
               ArrayList<WPoint3D> newU = new ArrayList<>();
               newU.add(new WPoint3D(p1, w));
               double spacing = 1 / (double) nU;
               for (int i = 0; i < nU - 1; i++, spacing += spacing) {
                    newU.add(lerp(new WPoint3D(p1, w), new WPoint3D(p2, w), spacing));
               }
               newU.add(new WPoint3D(p2, w));
               controlNet.add(newU);
               addKnot(1, 0.5);
               nU++;
          } else {
               double spacing = 1 / (double) nV;
               double startSpacing = 0;
               for (ArrayList<WPoint3D> i : controlNet) {
                    i.add(lerp(new WPoint3D(p1, w), new WPoint3D(p2, w), startSpacing));
                    startSpacing += spacing;
               }
               addKnot(2, 0.5);
               nV++;
          }
     }

     private WPoint3D lerp(WPoint3D p1, WPoint3D p2, double spacing) {
          return p1.add(p2.subtract(p1).multiply(spacing));
     }

     private void initiateNurbs(WPoint3D p1, WPoint3D p3) {
          nU = degreeU + 1;
          nV = degreeV + 1;
          for (int j = 0; j < nU; j++) {
               knotsU.add(0.0);
          }
          for (int j = 0; j < nU; j++) {
               knotsU.add(1.0);
          }
          for (int j = 0; j < nV; j++) {
               knotsV.add(0.0);
          }
          for (int j = 0; j < nV; j++) {
               knotsV.add(1.0);
          }
          for (int i = 0; i < nU; i++)
               controlNet.add(new ArrayList<>());

          WPoint3D p2 = new WPoint3D(p1.getX(), p1.getY(), p3.getZ(), p1.getWeight());
          WPoint3D p4 = new WPoint3D(p3.getX(), p3.getY(), p1.getZ(), p2.getWeight());
          double spacing = 1 / (double) nU;
          double startSpacing = 0;
          for (ArrayList<WPoint3D> i : controlNet) {
               i.add(lerp(p1, p2, startSpacing));
               startSpacing += spacing;
          }
          WPoint3D[] left = new WPoint3D[nV];
          WPoint3D[] right = new WPoint3D[nV];
          spacing = 1 / (double) nV;
          startSpacing = 0;
          for (int i = 0; i < nV; i++, startSpacing += spacing) {
               left[i] = lerp(p2, p3, startSpacing);
          }
          startSpacing = 0;
          for (int i = 0; i < nV; i++, startSpacing += spacing) {
               right[i] = lerp(p1, p4, startSpacing);
          }
          spacing = 1/ (double) nU;
          for (int i = 0; i < nV - 1; i++) {
               startSpacing = 0;
               for (int j = 0; j < nU; j++, startSpacing += spacing) {
                    controlNet.get(j).add(lerp(right[i], left[i], startSpacing));
               }
          }
     }

     @Override
     public void add3Vertices(Point3D p1, Point3D p2, Point3D p3, String instruct, double w) {

     }

     public int getDegreeU() {
          return degreeU;
     }

     public int getDegreeV() {
          return degreeV;
     }

     public int getnU() {
          return nU;
     }

     public int getnV() {
          return nV;
     }

     public ArrayList<Double> getKnotsU() {
          return knotsU;
     }

     public ArrayList<Double> getKnotsV() {
          return knotsV;
     }

     public void printStuff() {
          System.out.println("DegreeU: " + degreeU + " DegreeV: " + degreeV);
          System.out.println("nU: " + nU + " nV: " + nV);
          System.out.println("KnotsU:");
          for (Double i : knotsU)
               System.out.println(i);
          System.out.println("KnotsV:");
          for (Double i : knotsV)
               System.out.println(i);
     }

     public String toString() {
          StringBuilder sb = new StringBuilder();
          sb.append("NURBS Surface" + System.lineSeparator());
          sb.append("Degree U: " + degreeU + " Degree V: " + degreeV + System.lineSeparator());
          sb.append("Controlpoints along U: " + nU + " Controlpoints along V: " + nV + System.lineSeparator());
          sb.append("Knots U: ");
          for (Double i : knotsU)
               sb.append(i + " ");
          sb.append(System.lineSeparator() + "Knots V: ");
          for (Double i : knotsV)
               sb.append(i + " ");
          return sb.toString();
     }
}

//     for (double i : bFU)
//                                  System.out.println(i);
//     System.out.println();
//     for (double i : bFV)
//                                  System.out.println(i);
//     System.out.println();
//     //Helper.print2D(pW);
//     System.out.println();
//     System.out.println(temp[i].toString());
//     System.out.println();
//     System.out.println(rP.toString());


//     public void insertKnot(double u, double v) {
//
//          int knotIndex = findKnotSpan(t);
//          if (knotIndex >= n) return;
//
//          ArrayList<Point3D> newPoints = new ArrayList<>();
//          ArrayList<Double> newWeights = new ArrayList<>();
//          WPoint3D[] pW;
//          pW = wPoints(controlNet, weights, knotIndex - degree, knotIndex);
//
//          for (int i = 1; i <= degree; i++) {
//               double ui = knots.get(knotIndex - degree + i);
//               double a = (t - ui) / (knots.get(knotIndex + i) - ui);
//               WPoint3D p = pW[i - 1].multiply(1 - a).add(pW[i].multiply(a));
//               newWeights.add(p.getWeight());
//               newPoints.add(p.convert());
//          }
//          int index = 0;
//          for (int i = knotIndex - degree + 1; i < knotIndex; i++) {
//               controlNet.set(i, newPoints.get(index));
//               weights.set(i, newWeights.get(index++));
//          }
//          controlNet.add(knotIndex, newPoints.get(degree - 1));
//          weights.add(knotIndex, newWeights.get(degree - 1));
//          knots.add(knotIndex + 1, t);
//          n++;
//     }
//
//     public void refineKnots() {
//
//          ArrayList<Point3D> newPoints = new ArrayList<>();
//          ArrayList<Double> newWeights = new ArrayList<>();
//          int s = knots.size() - 2 * degree - 1;
//          int a = degree;
//          for (int j = 0; j < s; j++) {
//               double t = (knots.get(a) + knots.get(a + 1)) / 2.0;
//               WPoint3D[] pW = wPoints(controlNet, weights, a - degree, a);
//
//               for (int i = 1; i <= degree; i++) {
//                    double ui = knots.get(a - degree + i);
//                    double factor = (t - ui) / (knots.get(a + i) - ui);
//                    WPoint3D p = pW[i - 1].multiply(1 - factor).add(pW[i].multiply(factor));
//                    newWeights.add(p.getWeight());
//                    newPoints.add(p.convert());
//               }
//               int index = 0;
//               for (int i = a - degree + 1; i < a; i++) {
//                    controlNet.set(i, newPoints.get(index));
//                    weights.set(i, newWeights.get(index++));
//               }
//               controlNet.add(a, newPoints.get(degree - 1));
//               weights.add(a, newWeights.get(degree - 1));
//               knots.add(a + 1, t);
//               n++;
//               a += 2;
//               newPoints.clear();
//               newWeights.clear();
//          }
//     }
//
//     public ArrayList<Point3D> getRefined(int precision) {
//
//          ArrayList<Double> knots = new ArrayList<>();
//          knots.addAll(this.knots);
//          ArrayList<Double> weights = new ArrayList<>();
//          weights.addAll(this.weights);
//          ArrayList<Point3D> controlNet = new ArrayList<>();
//          controlNet.addAll(this.controlNet);
//
//          for (int x = 0; x < precision; x++) {
//
//               ArrayList<Point3D> newPoints = new ArrayList<>();
//               ArrayList<Double> newWeights = new ArrayList<>();
//               int s = knots.size() - 2 * degree - 1;
//               int a = degree;
//               for (int j = 0; j < s; j++) {
//                    double t = (knots.get(a) + knots.get(a + 1)) / 2.0;
//                    WPoint3D[] pW = wPoints(controlNet, weights, a - degree, a);
//
//                    for (int i = 1; i <= degree; i++) {
//                         double ui = knots.get(a - degree + i);
//                         double factor = (t - ui) / (knots.get(a + i) - ui);
//                         WPoint3D p = pW[i - 1].multiply(1 - factor).add(pW[i].multiply(factor));
//                         newWeights.add(p.getWeight());
//                         newPoints.add(p.convert());
//                    }
//                    int index = 0;
//                    for (int i = a - degree + 1; i < a; i++) {
//                         controlNet.set(i, newPoints.get(index));
//                         weights.set(i, newWeights.get(index++));
//                    }
//                    controlNet.add(a, newPoints.get(degree - 1));
//                    weights.add(a, newWeights.get(degree - 1));
//                    knots.add(a + 1, t);
//                    a += 2;
//                    newPoints.clear();
//                    newWeights.clear();
//               }
//          }
//          return controlNet;
//     }


//     public void drawControl(Graphics2D g2, double nodeRadius, float lineWidth) {
//
//          if (n > 1) {
//               for (int i = 0; i < n - 1; i++)
//                    Point3D.drawLine(g2, controlNet.get(i), controlNet.get(i + 1), lineWidth);
//          }
//          g2.setColor(new Color(76, 76, 226));
//          for (int i = 0; i < n; i++) {
//               controlNet.get(i).draw(g2, nodeRadius);
//          }
//     }
//
//     public void drawCurve(Graphics2D g2, int steps, float lineWidth) {
//
//          double d = knots.get(n + 1) / steps;
//          double val = 0.0;
//          if (n > 1) {
//               for (double t = knots.get(degree); t < steps - 1; t++) {
//                    Point3D.drawLine(g2, curvePoint(val), curvePoint(val + d), lineWidth);
//                    val += d;
//               }
//               Point3D.drawLine(g2, curvePoint(val), curvePoint(knots.get(n + degree)), lineWidth);
//          }
//          length();
//     }

//     private double length(ArrayList<Point3D> points) {
//
//          double len = 0.0;
//
//          for (int i = 0; i < points.size() - 1; i++)
//               len += points.get(i).dist(points.get(i + 1));
//          return len;
//     }

//     public void set(int i, int j, Point3D point) {
//
//          controlNet.get(i).get(j).setLocation(point.getX(), point.getY());
//     }

//     private double clength(ArrayList<Point3D> points) {
//          double starttime = System.currentTimeMillis();
//          double length = length(points);
//          double endtime = System.currentTimeMillis();
//          System.out.println(endtime - starttime);
//          return length;
//     }
//
//     public void length() {
//          System.out.println("straight: " + controlNet.get(0).distance(controlNet.get(n - 1)));
//          System.out.println(clength(getRefined(3)));
//     }

//     public Point2D closestIndex(Point3D cP, Point3D newCP) {
//          int cUSize = controlNet.size();
//          int cVSize = controlNet.get(0).size();
//          if (cUSize == 1 && cVSize == 1)
//               return null;
//          int countU = 0;
//          int countV = 0;
//          for (ArrayList<Point3D> i : controlNet) {
//               for (Point3D j : i) {
//                    if (j.equals(cP)) {
//                         double d1; double d2; double d3; double d4;
//                         if (countU == 0) {
//                              if (countV == 0) {
//                                   return new Point2D.Double(0, 0);
//                              } else if (countV == cVSize - 1) {
//                                   return new Point2D.Double(countU, cVSize - 1);
//                              } else {
//                                   d1 = newCP.distance(controlNet.get(countU).get(countV+1));
//                                   d2 = newCP.distance(controlNet.get(countU).get(countV));
//                                   if (d1 < d2)
//                                        return new Point2D.Double(countU, countV+1);
//                                   else
//                                        return new Point2D.Double(countU, countV);
//                              }
//                         } else if (countV == 0) {
//                              if (countU == cUSize - 1) return new Point2D.Double()
//                              }
//                         }
//                         if (countU == cUSize - 1) return cUSize - 1;
//                         double d2 = newCP.distance(controlNet.get(countU+1).get(countV));
//                         double d3 = newCP.distance(controlNet.get(countU).get(countV));
//                         if (d1 < d2)
//                              return count;
//                         else
//                              return count + 1;
//                    }
//                    countV++;
//               }
//               countU++;
//          }
//          return null;
//     }


//     public Point3D surfacePoint(double u, double v) {
//
//          int knotIndexU = findKnotSpanU(u);
//          int knotIndexV = findKnotSpanV(v);
//          double[] bFU = basisFunctionsU(u, knotIndexU);
//          double[] bFV = basisFunctionsV(v, knotIndexV);
//          WPoint3D[][] pW;
//          if (u == knotsU.get(nU + degreeU)) {
//               if (v == knotsV.get(nU + degreeU))
//                    return controlNet.get(nU - 1).get(nV - 1);
//               else
//                    pW = wPoints(controlNet, knotIndexU - 1, knotIndexV);
//          } else if (v == knotsV.get(nU + degreeU))
//               pW = wPoints(controlNet, knotIndexU, knotIndexV - 1);
//          else
//               pW = wPoints(controlNet, knotIndexU, knotIndexV);
//
//          WPoint3D rP = new WPoint3D(0, 0, 0, 0);
//          WPoint3D[] temp = new WPoint3D[degreeU + 1];
//          for (int i = 0; i < pW.length; i++) {
//               temp[i] = new WPoint3D(0, 0, 0, 0);
//               for (int j = 0; j < pW[0].length; j++) {
//                    temp[i] = temp[i].add(pW[i][j].multiply(bFV[j]));
//               }
//          }
//          for (int i = 0; i < temp.length; i++) {
//               rP = rP.add(temp[i].multiply(bFU[i]));
//          }
//          return rP.convert();
//     }