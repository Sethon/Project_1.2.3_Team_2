package surfaces;

/**
 *Cell containing various information needed for Catmull-Clark subdivision
 */
public class ClarkCell {

	public Point3D facePoint;
	public Point3D v1;
	public Point3D v2;
	public Point3D v3;
	
	public int v1Val;
	public int v2Val;
	public int v3Val;
	
	public Point3D edgePoint12;
	public Point3D edgePoint23;
	public Point3D edgePoint13;
	
	public boolean outEdge12 = false;
	public boolean outEdge23 = false;
	public boolean outEdge13 = false;
	
	/**
	 * Instantiator which creates a new ClarkCell with the input information
	 * 
	 * @param p1 First vertex of the ClarkCell
	 * @param p2 Second vertex of the ClarkCell
	 * @param p3 Third vertex of the ClarkCell
	 * @param i1 First value of the vertex in the vertex array
	 * @param i2 Second value of the vertex in the vertex array
	 * @param i3 Third value of the vertex in the vertex array
	 */
	public ClarkCell(Point3D p1, Point3D p2, Point3D p3, int i1, int i2, int i3) {
		v1 = p1;
		v2 = p2;
		v3 = p3;
		v1Val = i1;
		v2Val = i2;
		v3Val = i3;
	}
	
	/**
	 * Sets the current face point of the ClarkCell
	 * 
	 * @param p The given face point
	 */
	public void setFacePoint(Point3D p) {
		facePoint = p;
	}

	/**
	 * Sets the current first vertex of the ClarkCell
	 * 
	 * @param p The given vertex
	 */
	public void setV1(Point3D p) {
		v1 = p;
	}

	/**
	 * Sets the current second vertex of the ClarkCell
	 * 
	 * @param p The given vertex
	 */
	public void setV2(Point3D p) {
		v2 = p;
	}

	/**
	 * Sets the current third vertex of the ClarkCell
	 * 
	 * @param p The given vertex
	 */
	public void setV3(Point3D p) {
		v3 = p;
	}

	/**
	 * Sets the current first vertex index of the ClarkCell
	 * 
	 * @param p The given index
	 */
	public void setv1Val(int i) {
		v1Val = i;
	}

	/**
	 * Sets the current second vertex index of the ClarkCell
	 * 
	 * @param p The given index
	 */
	public void setv2Val(int i) {
		v2Val = i;	
	}

	/**
	 * Sets the current third vertex index of the ClarkCell
	 * 
	 * @param p The given index
	 */
	public void setv3Val(int i) {
		v3Val = i;
	}

	/**
	 * Sets the current edge point between vertex 1 and 2 of the ClarkCell
	 * 
	 * @param p The given edge point
	 */
	public void setEdgePoint12(Point3D p) {
		edgePoint12 = p;
	}

	/**
	 * Sets the current edge point between vertex 2 and 3 of the ClarkCell
	 * 
	 * @param p The given edge point
	 */
	public void setEdgePoint23(Point3D p) {
		edgePoint23 = p;
	}

	/**
	 * Sets the current edge point between vertex 1 and 3 of the ClarkCell
	 * 
	 * @param p The given edge point
	 */
	public void setEdgePoint13(Point3D p) {
		edgePoint13 = p;
	}

	/**
	 * Sets the current state of being on the outside of the model of edge point 1-2
	 * 
	 * @param p The given truth state
	 */
	public void setOutEdge12(boolean b) {
		outEdge12 = b;
	}

	/**
	 * Sets the current state of being on the outside of the model of edge point 2-3
	 * 
	 * @param p The given truth state
	 */
	public void setOutEdge23(boolean b) {
		outEdge23 = b;
	}

	/**
	 * Sets the current state of being on the outside of the model of edge point 1-3
	 * 
	 * @param p The given truth state
	 */
	public void setOutEdge13(boolean b) {
		outEdge13 = b;
	}
	
	/**
	 * Retrieves the current face point
	 * 
	 * @return the current face point
	 */
	public Point3D getFacePoint() {
		return facePoint;
	}

	
	/**
	 * Retrieves the current first vertex
	 * 
	 * @return the current first vertex
	 */
	public Point3D getV1() {
		return v1;
	}

	
	/**
	 * Retrieves the current second vertex
	 * 
	 * @return the current second vertex
	 */
	public Point3D getV2() {
		return v2;
	}

	
	/**
	 * Retrieves the current third vertex
	 * 
	 * @return the current third vertex
	 */
	public Point3D getV3() {
		return v3;
	}
	
	/**
	 * Retrieves the current first vertex index
	 * 
	 * @return the current first vertex index
	 */
	public int getv1Val() {
		return v1Val;
	}

	/**
	 * Retrieves the current second vertex index
	 * 
	 * @return the current second vertex index
	 */
	public int getv2Val() {
		return v2Val;	
	}

	/**
	 * Retrieves the current third vertex index
	 * 
	 * @return the current third vertex index
	 */
	public int getv3Val() {
		return v3Val;
	}

	/**
	 * Retrieves the current edge point between vertices 1 and 2
	 * 
	 * @return the current edge point 1-2
	 */
	public Point3D getEdgePoint12() {
		return edgePoint12;
	}

	/**
	 * Retrieves the current edge point between vertices 2 and 3
	 * 
	 * @return the current edge point 2-3
	 */
	public Point3D getEdgePoint23() {
		return edgePoint23;
	}

	/**
	 * Retrieves the current edge point between vertices 1 and 3
	 * 
	 * @return the current edge point 1-3
	 */
	public Point3D getEdgePoint13() {
		return edgePoint13;
	}

	/**
	 * Retrieves the current truth state of the edge point between vertices 1 and 2
	 * 
	 * @return the current truth state of edge point 1-2
	 */
	public boolean getOutEdge12() {
		return outEdge12;
	}

	/**
	 * Retrieves the current truth state of the edge point between vertices 2 and 3
	 * 
	 * @return the current truth state of edge point 2-3
	 */
	public boolean getOutEdge23() {
		return outEdge23;
	}
	
	/**
	 * Retrieves the current truth state of the edge point between vertices 1 and 3
	 * 
	 * @return the current truth state of edge point 1-3
	 */
	public boolean getOutEdge13() {
		return outEdge13;
	}
}
