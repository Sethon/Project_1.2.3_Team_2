package surfaces;

/**
 * Class representing a weighted point (4D space) used for efficiently describing NURBS surfaces.
 */
public class WPoint3D extends Point3D {

     private double weight;

     /**
      * Constructs a weighted Point3D using coordinates.
      * @param x X-coordinate of the point.
      * @param y Y-coordinate of the point.
      * @param z Z-coordinate of the point.
      * @param weight Weight of the point.
      */
     public WPoint3D(double x, double y, double z, double weight) {
          super(x,y,z);
          this.weight = weight;
     }

     /**
      * Constructs a weighted Point3D using an unweighted point.
      * @param p The point which will be weighted.
      * @param weight Weight of the new point.
      */
     public WPoint3D(Point3D p, double weight) {
          super(p.getX(),p.getY(),p.getZ());
          this.weight = weight;
     }

     /**
      * Returns the weight of this point.
      * @return Weight of this point.
      */
     public double getWeight() {
          return weight;
     }

     /**
      * Sets the weight of this point to the supplied value.
      * @param weight New weight for this point.
      */
     public void setWeight(double weight) {
          this.weight = weight;
     }

     /**
      * Multiplies this point by the supplied scalar and returns the new point.
      * @param factor Scalar which the points coordinates will be multiplied by.
      * @return New, scaled weighted point.
      */
     public WPoint3D multiply(double factor) {
          return new WPoint3D(getX() * factor, getY() * factor, getZ() * factor, getWeight() * factor);
     }

     /**
      * Adds the supplied coordinates to this point and returns the new point.
      * @param x X-coordinate to be added.
      * @param y Y-coordinate to be added.
      * @param z Z-coordinate to be added.
      * @param weight Weight to be added.
      * @return New point with added coordinates.
      */
     public WPoint3D add(double x, double y, double z, double weight) {
          return new WPoint3D(
                                       getX() + x,
                                       getY() + y,
                                       getZ() + z,
                                       getWeight() + weight);
     }

     /**
      * Adds the supplied point to this point and returns the result.
      * @param point Point to be added.
      * @return New point with added coordinates from supplied point.
      */
     public WPoint3D add(WPoint3D point) {
          return add(point.getX(), point.getY(), point.getZ(), point.getWeight());
     }

     /**
      * Subtracts the supplied coordinates from this point and returns the result.
      * @param x X-coordinate to be subtracted.
      * @param y Y-coordinate to be subtracted.
      * @param z Z-coordinate to be subtracted.
      * @param weight Weight to be subtracted.
      * @return New point with subtracted coordinates.
      */
     public WPoint3D subtract(double x, double y, double z, double weight) {
          return new WPoint3D(
                                       getX() - x,
                                       getY() - y,
                                       getZ() - z,
                                       getWeight() - weight);
     }

     /**
      * Subtracts the supplied point from this point and returns the result.
      * @param point Point to be subtracted.
      * @return New point with subtracted coordinates.
      */
     public WPoint3D subtract(WPoint3D point) {
          return subtract(point.getX(), point.getY(), point.getZ(), point.getWeight());
     }

     /**
      * Divides this point by its weight, mapping it into 3D space. Returns the transformed point.
      * @return New Point3D.
      */
     public Point3D convert() {
          return new Point3D(getX()/weight, getY()/weight, getZ()/weight);
     }

     /**
      * Divides this point by its weight, thus transforming it into 3D space while keeping the weight.
      * @return Point with X/Y/Z divided by the weight.
      */
     public WPoint3D transform() {
          return new WPoint3D(getX() / weight, getY() / weight, getZ() / weight, weight);
     }

     /**
      * Returns the string representation of this weighted point.
      * @return String representing this weighted point.
      */
     public String toString() {
          return super.toString() + "[" + weight + "]";
     }
}
