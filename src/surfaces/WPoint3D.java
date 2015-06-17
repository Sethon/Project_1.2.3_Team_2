package surfaces;
public class WPoint3D extends Point3D {

     private double weight;

     public WPoint3D(double x, double y, double z, double weight) {
          super(x,y,z);
          this.weight = weight;
     }

     public WPoint3D(Point3D p, double weight) {
          super(p.getX(),p.getY(),p.getZ());
          this.weight = weight;
     }

     public double getWeight() {
          return weight;
     }

     public void setWeight(double weight) {
          this.weight = weight;
     }

     public WPoint3D multiply(double factor) {
          return new WPoint3D(getX() * factor, getY() * factor, getZ() * factor, getWeight() * factor);
     }

     public WPoint3D add(double x, double y, double z, double weight) {
          return new WPoint3D(
                                       getX() + x,
                                       getY() + y,
                                       getZ() + z,
                                       getWeight() + weight);
     }

     public WPoint3D add(WPoint3D point) {
          return add(point.getX(), point.getY(), point.getZ(), point.getWeight());
     }

     public WPoint3D subtract(double x, double y, double z, double weight) {
          return new WPoint3D(
                                       getX() - x,
                                       getY() - y,
                                       getZ() - z,
                                       getWeight() - weight);
     }

     public WPoint3D subtract(WPoint3D point) {
          return subtract(point.getX(), point.getY(), point.getZ(), point.getWeight());
     }

     public Point3D convert() {
          return new Point3D(getX()/weight, getY()/weight, getZ()/weight);
     }

     public WPoint3D transform() {
          return new WPoint3D(getX() / weight, getY() / weight, getZ() / weight, weight);
     }

     public String toString() {
          return super.toString() + "[" + weight + "]";
     }
}
