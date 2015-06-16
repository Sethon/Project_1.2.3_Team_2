package surfaces;

import java.util.ArrayList;

public class NURBS extends EditableSurface {

     private ArrayList<Double> knotsU;
     private ArrayList<Double> knotsV;
     private ArrayList<ArrayList<Point3D>> controlNet;
     private ArrayList<ArrayList<Double>> weights;
     private int degreeU;
     private int degreeV;
     private int nU;
     private int nV;

     public NURBS(int degreeU, int degreeV) {
          this.degreeU = degreeU;
          this.degreeV = degreeV;
          nU = 0;
          nV = 0;
          knotsU = new ArrayList<>();
          knotsV = new ArrayList<>();
          controlNet = new ArrayList<>();
          weights = new ArrayList<>();
     }

     public NURBS(int degreeU, int degreeV, WPoint3D[][] cP, double[] knotsU, double[] knotsV) {

          controlNet = new ArrayList<>(cP.length);
          weights = new ArrayList<>(cP.length);
          this.knotsU = new ArrayList<>();
          this.knotsV = new ArrayList<>();
          this.degreeU = degreeU;
          this.degreeV = degreeV;
          for (int i = 0; i < cP.length; i++) {
               ArrayList<Point3D> lC = new ArrayList<>();
               ArrayList<Double> lW = new ArrayList<>();
               for (int j = 0; j < cP[0].length; j++) {
                    lC.add(cP[i][j].convert());
                    lW.add(cP[i][j].getWeight());
               }
               controlNet.add(lC);
               weights.add(lW);
          }
          nU = cP.length;
          nV = cP[0].length;
          for (double i : knotsU)
               this.knotsU.add(i);
          for (double i : knotsV)
               this.knotsV.add(i);
     }

     public ArrayList<ArrayList<Point3D>> get() {
          return controlNet;
     }

     public Point3D getClosest(Point3D p) {

          Point3D close = null;
          double maxDistance = Double.MAX_VALUE;
          double distance;

          for (ArrayList<Point3D> i : controlNet) {
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

          if (u == knotsU.get(nU + 1)) return nU;
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

          if (v == knotsV.get(nV + 1)) return nV;
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

     public void scaleKnotsU(double factor) {
          for (Double i : knotsU)
               i = i * factor;
     }

     public void scaleKnotsV(double factor) {
          for (Double i : knotsV)
               i = i * factor;
     }

     public Point3D surfacePoint(double u, double v) {
          if (u == knotsU.get(nU + degreeU) && v == knotsV.get(nU + degreeU)) return controlNet.get(nU - 1).get(nV - 1);

          int knotIndexU = findKnotSpanU(u);
          int knotIndexV = findKnotSpanV(v);
          double[] bFU = basisFunctionsU(u, knotIndexU);
          double[] bFV = basisFunctionsV(v, knotIndexV);

          WPoint3D[][] pW = wPoints(controlNet, weights, knotIndexU, knotIndexV);

          WPoint3D rP = new WPoint3D(0, 0, 0, 0);
          WPoint3D[] temp = new WPoint3D[degreeU + 1];
          for (int i = 0; i < pW.length; i++) {
               temp[i] = new WPoint3D(0, 0, 0, 0);
               for (int j = 0; j < pW[0].length; j++) {
                    temp[i] = temp[i].add(pW[i][j].multiply(bFV[j]));
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
          Point3D[][] vertices = new Point3D[steps][steps];

          for (int i = 0; i < steps; i++) {
               for (int j = 0; j < steps; j++) {
                    vertices[i][j] = (surfacePoint(u, v));
                    v += dV;
               }
               u += dU;
               v = 0.0;
          }
          return vertices;
     }

     @Override
     public ArrayList<Triangle3D> triangulate() {
          long starttime = System.currentTimeMillis();
          ArrayList<Triangle3D> triangles = new ArrayList<>();
          Point3D[][] v = getVertices(30);
          int size = v.length;

          for (int i = 0; i < size - 1; i++) {
               for (int j = 0; j < size - 1; j++) {
                    triangles.add(new Triangle3D(v[i][j], v[i + 1][j], v[i][j + 1]));
                    triangles.add(new Triangle3D(v[i + 1][j], v[i][j + 1], v[i + 1][j + 1]));
                    //System.out.println(triangles.get(i).toString());
               }
          }
          long endtime = System.currentTimeMillis();
          System.out.println(endtime - starttime);
          return triangles;
     }

     private WPoint3D[][] wPoints(ArrayList<ArrayList<Point3D>> controlNet, ArrayList<ArrayList<Double>> weights, int uSpan, int vSpan) {

          WPoint3D[][] pW = new WPoint3D[degreeU + 1][degreeV + 1];

          for (int i = 0; i <= degreeU; i++) {
               for (int j = 0; j <= degreeV; j++) {
                    Point3D p = controlNet.get(uSpan - degreeU + i).get(vSpan - degreeV + j);
                    double w = weights.get(uSpan - degreeU + i).get(vSpan - degreeV + j);
                    //pW[i][j] = p.convert(w);
               }
          }
          return pW;
     }

     @Override
     public ArrayList<Point3D> vertices() {
          ArrayList<Point3D> v = new ArrayList<>();
          Point3D[][] p = getVertices(30);
          for (Point3D[] i : p)
               for (Point3D j : i)
                    v.add(j);
          return v;
     }

     public ArrayList<ArrayList<Point3D>> getControlNet() {
          return controlNet;
     }

     @Override
     public double surfaceArea() {
          return 0;
     }

     @Override
     public void addVertex(Point3D p) {

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