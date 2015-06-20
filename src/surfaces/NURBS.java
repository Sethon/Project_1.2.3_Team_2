package surfaces;

import org.apache.commons.math3.linear.*;

import java.util.ArrayList;

public class NURBS extends EditableSurface {

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

     public NURBS(int degreeU, int degreeV, ArrayList<ArrayList<Point3D>> points) {
          instCnt++;
          assignLabel();
          this.degreeU = degreeU;
          this.degreeV = degreeV;
          controlNet = weightNet(surfaceInterpolation(points));
          nU = controlNet.size();
          nV = controlNet.get(0).size();
     }

     private ArrayList<ArrayList<WPoint3D>> weightNet(ArrayList<ArrayList<Point3D>> points) {
          ArrayList<ArrayList<WPoint3D>> controlNet = new ArrayList<>();
          for (ArrayList<Point3D> i : points) {
               ArrayList<WPoint3D> l = new ArrayList<>();
               for (Point3D j : i) {
                    l.add(new WPoint3D(j, 1));
               }
               controlNet.add(l);
          }
          return controlNet;
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
                    //System.out.print(v[i][j].toString() + " ");
                    triangles.add(new Triangle3D(v[i][j], v[i + 1][j], v[i][j + 1]));
                    triangles.add(new Triangle3D(v[i + 1][j], v[i][j + 1], v[i + 1][j + 1]));
               }
               //System.out.println();
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
          spacing = 1 / (double) nU;
          for (int i = 0; i < nV - 1; i++) {
               startSpacing = 0;
               for (int j = 0; j < nU; j++, startSpacing += spacing) {
                    controlNet.get(j).add(lerp(right[i], left[i], startSpacing));
               }
          }
     }

     private int findKnotSpan(double u, ArrayList<Double> knots, int degree) {

          int n = knots.size() - degree - 1;
          if (u == knots.get(n + 1)) return n - 1;
          int low = degree;
          int high = n + 1;
          int mid = (low + high) / 2;

          while (u < knots.get(mid) || u >= knots.get(mid + 1)) {
               if (u < knots.get(mid))
                    high = mid;
               else
                    low = mid;
               mid = (low + high) / 2;
          }
          return mid;
     }

     private double[] basisFunctions(ArrayList<Double> knots, double u, int index, int degree, int n) {
          double[] bF = new double[degree + 1];
          double[] left = new double[degree + 1];
          double[] right = new double[degree + 1];
          bF[0] = 1.0;

          for (int i = 1; i <= degree; i++) {
               left[i] = u - knots.get(index + 1 - i);
               right[i] = knots.get(index + i) - u;
               double saved = 0.0;

               for (int j = 0; j < i; j++) {
                    double temp = bF[j] / (right[j + 1] + left[i - j]);
                    bF[j] = saved + right[j + 1] * temp;
                    saved = left[i - j] * temp;
                    saved = Math.round(saved * 1000000)/1000000.0;
               }
               bF[i] = saved;
          }
          double[] N = new double[n];
          for (int i = index - degree; i < index + 1; i++) {
               N[i] = bF[i - index + degree];
          }
          return N;
     }

     private double[] chordLengthPara(ArrayList<Point3D> points) {
          double length = 0;
          int pSize = points.size();
          double[] paras = new double[pSize];
          for (int i = 0; i < pSize - 1; i++) {
               length += points.get(i).dist(points.get(i + 1));
          }
          if (length == 0.0) {
               double spacing = 1/(double) pSize;
               for (int i = 1; i < pSize-1; i++) {
                    paras[i] = spacing;
                    spacing += spacing;
               }
          }
          paras[0] = 0.0;
          for (int i = 1; i < pSize-1; i++) {
               double l = 0;
               for (int j = 1; j <= i; j++) {
                    l += points.get(j).dist(points.get(j - 1));
               }
               l = (1 / length) * l;
               paras[i] = Math.round(l * 1000000000)/1000000000.0;
          }
          paras[pSize-1] = 1.0;
          return paras;
     }

     private ArrayList<Double> genKnotV(double[] paras, int degree) {
          ArrayList<Double> knots = new ArrayList<>();
          int m = paras.length + degree + 1;
          for (int i = 0; i <= degree; i++) {
               knots.add(0.0);
          }
          for (int i = 1; i < paras.length - degree; i++) {
               double av = 0.0;
               for (int j = i; j <= i + degree - 1; j++) {
                    av += paras[j];
               }
               knots.add((1 / (double) degree) * av);
          }
          for (int i = 0; i <= degree; i++) {
               knots.add(1.0);
          }
          return knots;
     }

     private void params(double[] s, double[] t, ArrayList<ArrayList<Point3D>> points) {
          int m = points.size();
          int n = points.get(0).size();

          double[][] paras = new double[n][m];
          ArrayList<ArrayList<Point3D>> interPoints = new ArrayList<>();
          for (int j = 0; j < n; j++) {
               ArrayList<Point3D> inter = new ArrayList<>();
               for (int i = 0; i < m; i++) {
                    inter.add(points.get(i).get(j));
               }
               paras[j] = chordLengthPara(inter);
          }
          for (int j = 0; j < m; j++) {
               double av = 0.0;
               for (int i = 0; i < n; i++) {
                    av += paras[i][j];
               }
               s[j] = av/(double) n;
          }
          paras = new double[m][n];
          for (int i = 0; i < m; i++) {
               paras[i] = chordLengthPara(points.get(i));
          }
          for (int j = 0; j < n; j++) {
               double av = 0.0;
               for (int i = 0; i < m; i++) {
                    av += paras[i][j];
               }
               t[j] = av/(double) m;
          }
          knotsU = genKnotV(s,degreeU);
          knotsV = genKnotV(t,degreeV);
     }

     private ArrayList<ArrayList<Point3D>> surfaceInterpolation(ArrayList<ArrayList<Point3D>> points) {
          int m = points.size();
          int n = points.get(0).size();
          double[] s = new double[m];
          double[] t = new double[n];

          params(s, t, points);

          ArrayList<ArrayList<Point3D>> interPoints = new ArrayList<>();
          for (int j = 0; j < n; j++) {
               ArrayList<Point3D> inter = new ArrayList<>();
               for (int i = 0; i < m; i++) {
                    inter.add(points.get(i).get(j));
               }
               interPoints.add(curveInterpolation(inter, s, knotsU, degreeU));
          }

          points.clear();
          for (int j = 0; j < m; j++) {
               ArrayList<Point3D> inter = new ArrayList<>();
               for (int i = 0; i < n; i++) {
                    inter.add(interPoints.get(i).get(j));
               }
               points.add(curveInterpolation(inter, t, knotsV, degreeV));
          }
          return points;

     }

     public ArrayList<Point3D> curveInterpolation(ArrayList<Point3D> points, double[] params, ArrayList<Double> knots, int degree) {
          int n = params.length;

          double[][] N = new double[n][n];
          for (int i = 0; i < n; i++) {
               int kI = findKnotSpan(params[i], knots, degree);
               N[i] = basisFunctions(knots, params[i], kI, degree, n);
          }
          for (int i = 0; i < N.length; i++) {
               for (int j = 0; j < N[0].length; j++) {
                    System.out.print(N[i][j] + " ");
               }
               System.out.println();
          }

          RealMatrix NN = MatrixUtils.createRealMatrix(N);
          NN = MatrixUtils.inverse(NN);
          double[] x = new double[n];
          double[] y = new double[n];
          double[] z = new double[n];
          int count = 0;
          for (Point3D i : points) {
               x[count] = i.getX();
               y[count] = i.getY();
               z[count++] = i.getZ();
          }
          RealVector DX = MatrixUtils.createRealVector(x);
          RealVector DY = MatrixUtils.createRealVector(y);
          RealVector DZ = MatrixUtils.createRealVector(z);
          double[] px = NN.operate(DX).toArray();
          double[] py = NN.operate(DY).toArray();
          double[] pz = NN.operate(DZ).toArray();
          ArrayList<Point3D> newPoints = new ArrayList<>();
          for (int i = 0; i < n; i++) {
               newPoints.add(new Point3D(px[i], py[i], pz[i]));
          }
          return newPoints;
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