package surfaces;

import java.util.ArrayList;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

public class Clarkinator {
	
	private FVPolygonMesh pm;
	private ArrayList<ClarkCell> cells;

	public Clarkinator(FVPolygonMesh pm) {
		this.pm = pm;
	}
	
	public void setMesh(FVPolygonMesh pm) {
		this.pm = pm;
	}
	
	public FVPolygonMesh getMesh() {
		return pm;
	}
	
	//Returns the cells containing the vertex.
	public ArrayList<ClarkCell> cellsWithP(Point3D p) {
		ArrayList<ClarkCell> foundCells = new ArrayList<>();
		for(int i = 0; i<cells.size(); i++) {
			if(cells.get(i).getV1().equals(p) || cells.get(i).getV2().equals(p) || cells.get(i).getV3().equals(p)) {
				foundCells.add(cells.get(i));
			}
		}
//System.out.println(foundCells.size());
		return foundCells;
	}
	
	//Returns all edgePoints connected with a vertex
	public ArrayList<Point3D> findEdgePoints(Point3D p) {
		ArrayList<Point3D> edgePoints= new ArrayList<>();
		for(int i = 0; i<cells.size(); i++) {
			if(cells.get(i).getV1().equals(p)) {
				edgePoints.add(cells.get(i).getEdgePoint12());
				edgePoints.add(cells.get(i).getEdgePoint13());
			}
			else if(cells.get(i).getV2().equals(p)) {
				edgePoints.add(cells.get(i).getEdgePoint12());
				edgePoints.add(cells.get(i).getEdgePoint23());
			}
			else if(cells.get(i).getV3().equals(p)) {
				edgePoints.add(cells.get(i).getEdgePoint23());
				edgePoints.add(cells.get(i).getEdgePoint13());
			}	
		}
/*for(int j = 0; j<edgePoints.size(); j++)
	System.out.println(edgePoints.get(j));*/
		ArrayList<Point3D> realEdgePoints= new ArrayList<>();
		for(int i = 0; i<edgePoints.size(); i++) {
			realEdgePoints.add(edgePoints.get(i));
			for(int j = 0; j<realEdgePoints.size()-1; j++) {

//System.out.println("i:" + i + "  edgePoint i:" + edgePoints.get(i) + "  j:" + j + "  realEdgePoints j:" +realEdgePoints.get(j) );
				if(realEdgePoints.get(j).equals(edgePoints.get(i)))
					realEdgePoints.remove(realEdgePoints.size()-1);
			}
		}
		return realEdgePoints;
	}
	
	//Checks if the edge between the 2 points has 2 faces
	public boolean edgeHasTwoFaces(Point3D p1, Point3D p2) {
		int facesFound = 0;
		for(int i = 0; i<pm.triangulate().size(); i++) {
			Triangle3D face = pm.triangulate().get(i);
			boolean p1Found = false;
			boolean p2Found = false;
			if(face.vertices().get(0).equals(p1) || face.vertices().get(1).equals(p1) || face.vertices().get(2).equals(p1)) {
				p1Found = true;
				if(p2Found){
					facesFound++;
					break;
				}
			}
			if(face.vertices().get(0).equals(p2) || face.vertices().get(1).equals(p2) || face.vertices().get(2).equals(p2)) {
				p2Found = true;
				if(p1Found){
					facesFound++;
					break;
				}
			}
			if(facesFound == 2)
				break;
		}
		if(facesFound==2)
			return true;
		else
			return false;
	}
	
	//Returns the 2 matching points of the ClarkCells. Only use after the if-statement: if(isNeighbor)
	public int[] pointsNeighbor(ClarkCell c1, ClarkCell c2) {
		ArrayList<Integer> commenPoints = new ArrayList<>();
		boolean p1Found = false; //Vertices 1, 2 and 3 of c1
		boolean p2Found = false;
		boolean p3Found = false;
		if(c1.getV1().equals(c2.getV1()) || c1.getV1().equals(c2.getV2()) || c1.getV1().equals(c2.getV3())) {
			p1Found = true;
		}
		if(c1.getV2().equals(c2.getV1()) || c1.getV2().equals(c2.getV2()) || c1.getV2().equals(c2.getV3())) {
			p2Found = true;
		}
		if(c1.getV3().equals(c2.getV1()) || c1.getV3().equals(c2.getV2()) || c1.getV3().equals(c2.getV3())) {
			p3Found = true;
		}
		
		if(p1Found)
			commenPoints.add(new Integer(1));
		if(p2Found)
			commenPoints.add(new Integer(2));
		if(p3Found)
			commenPoints.add(new Integer(3));
		int[] comPoints = new int[2];
		comPoints[0] = commenPoints.get(0);
		comPoints[1] = commenPoints.get(1);
		return comPoints;
	}
	
	
	//Checks if the 2 inputed ClarkCells are neighbors
	public boolean isNeighbor(ClarkCell c1, ClarkCell c2) {
		boolean p1Found = false; //Vertices 1, 2 and 3 of c1
		boolean p2Found = false;
		boolean p3Found = false;
		if(c1.getV1().equals(c2.getV1()) || c1.getV1().equals(c2.getV2()) || c1.getV1().equals(c2.getV3())) {
			p1Found = true;
//System.out.println("1:"+p1Found);
		}
		if(c1.getV2().equals(c2.getV1()) || c1.getV2().equals(c2.getV2()) || c1.getV2().equals(c2.getV3())) {
			p2Found = true;
//System.out.println("2:"+p2Found);
		}
		if(c1.getV3().equals(c2.getV1()) || c1.getV3().equals(c2.getV2()) || c1.getV3().equals(c2.getV3())) {
			p3Found = true;
//System.out.println("3:"+p3Found);
		}
		
		if((p1Found && p2Found) || (p2Found && p3Found) || (p1Found && p3Found))
			return true;
		else
			return false;
	}
	
	//Will divide the complete FVMesh model.
	public void subdivide(int iterations) {
		for(int its = 0; its < iterations; its ++) {
			cells = new ArrayList<>();
			//Fill ClarkCells with the vertices and their values in the vertices array
//System.out.println(pm.triangulate().size());
			for(int i = 0; i<pm.triangulate().size(); i++) {
				int val1 = 0;
				int val2 = 0;
				int val3 = 0;
				for(int k = 0; k<pm.vertices().size(); k++) {
					if(pm.vertices().get(k).equals(pm.triangulate().get(i).vertices().get(0)))
						val1 = k;
					if(pm.vertices().get(k).equals(pm.triangulate().get(i).vertices().get(1)))
						val2 = k;
					if(pm.vertices().get(k).equals(pm.triangulate().get(i).vertices().get(2)))
						val3 = k;
				}
//System.out.println(val1 + " " + val2 + " " + val3);
				//Add new ClarkCells to the complete array which at this point contain:
				//the vertices 1, 2 and 3
				//The integers 1, 2 and 3 of the vertices
//System.out.println("TEST: "+ pm.triangulate().get(i).vertices().get(0));
				cells.add(new ClarkCell(pm.triangulate().get(i).vertices().get(0), pm.triangulate().get(i).vertices().get(1), pm.triangulate().get(i).vertices().get(2), val1, val2, val3));
			}
//System.out.println("Done with filling Cells with V's and indexes");
			
			//Add facePoints to the ClarkCells, which are the middle of the faces
			for(int i = 0; i<cells.size(); i++) {
				double faceX = (cells.get(i).getV1().getX() + cells.get(i).getV2().getX() + cells.get(i).getV3().getX())/3;
				double faceY = (cells.get(i).getV1().getY() + cells.get(i).getV2().getY() + cells.get(i).getV3().getY())/3;
				double faceZ = (cells.get(i).getV1().getZ() + cells.get(i).getV2().getZ() + cells.get(i).getV3().getZ())/3;
				cells.get(i).setFacePoint(new Point3D(faceX,faceY,faceZ));
			}
//System.out.println("Done with adding facePoints");
			
			//Add EdgePoints to the ClarkCells, which are the middle point between the middle of the two facePoints and the middle of the edge.
			for(int i = 0; i<cells.size(); i++) {
				for(int j = 0; j<cells.size(); j++) {
					if(i!=j && isNeighbor(cells.get(i), cells.get(j))) {
						int[] numbers = pointsNeighbor(cells.get(i),cells.get(j));
						Point3D middleEdge;
						//Middle of the edge
						if(numbers[0] == 1 && numbers[1] == 2)
							middleEdge = new Point3D((cells.get(i).getV1().getX()+cells.get(i).getV2().getX())/2,
									(cells.get(i).getV1().getY()+cells.get(i).getV2().getY())/2, (cells.get(i).getV1().getZ()+cells.get(i).getV2().getZ())/2);
						else if(numbers[0] == 2 && numbers[1] == 3)
							middleEdge = new Point3D((cells.get(i).getV2().getX()+cells.get(i).getV3().getX())/2,
									(cells.get(i).getV2().getY()+cells.get(i).getV3().getY())/2, (cells.get(i).getV2().getZ()+cells.get(i).getV3().getZ())/2);
						else
							middleEdge = new Point3D((cells.get(i).getV1().getX()+cells.get(i).getV3().getX())/2,
									(cells.get(i).getV1().getY()+cells.get(i).getV3().getY())/2, (cells.get(i).getV1().getZ()+cells.get(i).getV3().getZ())/2);
						//Middle of the two facePoints
						Point3D middleFaces = new Point3D((cells.get(i).getFacePoint().getX() + cells.get(j).getFacePoint().getX())/2, 
								(cells.get(i).getFacePoint().getY() + cells.get(j).getFacePoint().getY())/2, (cells.get(i).getFacePoint().getZ() + cells.get(j).getFacePoint().getZ())/2);
						Point3D edgePoint = new Point3D((middleEdge.getX() + middleFaces.getX())/2, (middleEdge.getY() + middleFaces.getY())/2, (middleEdge.getZ() + middleFaces.getZ())/2);
//System.out.println("edgePoint" + edgePoint);
						
						if(numbers[0] == 1 && numbers[1] == 2)
							cells.get(i).setEdgePoint12(edgePoint);
						else if(numbers[0] == 2 && numbers[1] == 3)
							cells.get(i).setEdgePoint23(edgePoint);
						else if(numbers[0] == 1 && numbers[1] == 3)
							cells.get(i).setEdgePoint13(edgePoint);
					}
				}
			}
//System.out.println("Done with adding edgePoints");
			
			//Change coordinates of FVPolygonMesh
			ArrayList<Point3D> verts = pm.vertices();
			ArrayList<Point3D> newVerts = new ArrayList<>();
			for(int i = 0; i<verts.size(); i++) {
				Vector3D oldCoords = new Vector3D(verts.get(i).getX(), verts.get(i).getY(), verts.get(i).getZ());

				ArrayList<ClarkCell> foundCells = cellsWithP(verts.get(i));
				double n = foundCells.size();
				double m1 = (n-3)/n;
				double m2 = 1.0/n;
				double m3 = 2.0/n;

//System.out.println("n:" + n + "  m1:" + m1 + "  m2:" + m2 + "  m3:" + m3);
				
				//Calculate the average face points for each vertex.
				double avgFaceX = 0;
				double avgFaceY = 0;
				double avgFaceZ = 0;
				for(int j = 0; j<n; j++) {
					avgFaceX += foundCells.get(j).getFacePoint().getX();
					avgFaceY += foundCells.get(j).getFacePoint().getY();
					avgFaceZ += foundCells.get(j).getFacePoint().getZ();
//System.out.println("Found avg face values");
				}
				Vector3D avgFacePoint = new Vector3D(avgFaceX/n, avgFaceY/n, avgFaceZ/n);
				
				//Calculate the average Edge points for each vertex
				ArrayList<Point3D> foundEdgePoints = findEdgePoints(verts.get(i));
				double avgEdgeX = 0;
				double avgEdgeY = 0;
				double avgEdgeZ = 0;
				for(int j = 0; j<n; j++) {
					avgEdgeX += foundEdgePoints.get(j).getX();
					avgEdgeY += foundEdgePoints.get(j).getY();
					avgEdgeZ += foundEdgePoints.get(j).getZ();
//System.out.println("Found avg edge values");
				}
				Vector3D avgEdgePoint = new Vector3D(avgEdgeX/n, avgEdgeY/n, avgEdgeZ/n);
				
				//Calculate new coords
				Vector3D newCoords = (oldCoords.scalarMultiply(m1).add(avgFacePoint.scalarMultiply(m2).add(avgEdgePoint.scalarMultiply(m3))));
				
				newVerts.add(new Point3D(newCoords.getX(), newCoords.getY(), newCoords.getZ()));
				
				ArrayList<ClarkCell> cellsToChange = cellsWithP(verts.get(i));
				for(int j = 0; j<cellsToChange.size(); j++) {
					if(cellsToChange.get(j).getV1().equals(verts.get(i)))
						cellsToChange.get(j).setV1(newVerts.get(i));
					else if(cellsToChange.get(j).getV2().equals(verts.get(i)))
						cellsToChange.get(j).setV2(newVerts.get(i));
					else if(cellsToChange.get(j).getV3().equals(verts.get(i)))
						cellsToChange.get(j).setV3(newVerts.get(i));
				}
			}
//System.out.println("Done with the new vertex coords");
			
			//Make new faces
			ArrayList<Triangle3D> newFaces = new ArrayList<>();
			for(int j = 0; j<cells.size(); j++) {
				//Quadrilateral 1 (devided in 2 triangles)
//System.out.println("CONSTRUCTION:  " + cells.get(j).getV3());
				newFaces.add(new Triangle3D(cells.get(j).getV1(), cells.get(j).getEdgePoint12(), cells.get(j).getFacePoint()));
				newFaces.add(new Triangle3D(cells.get(j).getV1(), cells.get(j).getFacePoint(), cells.get(j).getEdgePoint13()));
	
				//Quadrilateral 2 (devided in 2 triangles)
				newFaces.add(new Triangle3D(cells.get(j).getV2(), cells.get(j).getEdgePoint23(), cells.get(j).getFacePoint()));
				newFaces.add(new Triangle3D(cells.get(j).getV2(), cells.get(j).getFacePoint(), cells.get(j).getEdgePoint12()));
	
				//Quadrilateral 3 (devided in 2 triangles)
				newFaces.add(new Triangle3D(cells.get(j).getV3(), cells.get(j).getEdgePoint13(), cells.get(j).getFacePoint()));
				newFaces.add(new Triangle3D(cells.get(j).getV3(), cells.get(j).getFacePoint(), cells.get(j).getEdgePoint23()));
				
			}
//System.out.println("Done with the new faces");
			
			//Make the verticesToFaces for the FVPolygonMesh instantiator
			ArrayList<ArrayList<Triangle3D>> vertsToFaces = new ArrayList<>();
			for(int i = 0; i<newVerts.size(); i++) {
				ArrayList<Triangle3D> foundFaces = new ArrayList<>();
				for(int j = 0; j<newFaces.size(); j++) {
					if(newFaces.get(1).equals(newVerts.get(i)) || newFaces.get(2).equals(newVerts.get(i)) || newFaces.get(3).equals(newVerts.get(i))) {
						foundFaces.add(newFaces.get(j));
					}
				}
				vertsToFaces.add(foundFaces);
			}
//System.out.println("Done with the new verts to faces");
			
			//Add all made vertices to current vertices list
			for(int j = 0; j < cells.size(); j ++) {
				newVerts.add(cells.get(j).getEdgePoint12());
				newVerts.add(cells.get(j).getEdgePoint23());
				newVerts.add(cells.get(j).getEdgePoint13());
				newVerts.add(cells.get(j).getFacePoint());
			}
			
			ArrayList<Point3D> realNewVerts= new ArrayList<>();
			for(int i = 0; i<newVerts.size(); i++) {
				realNewVerts.add(newVerts.get(i));
				for(int j = 0; j<realNewVerts.size()-1; j++) {
					if(realNewVerts.get(j).equals(newVerts.get(i)))
						realNewVerts.remove(realNewVerts.size()-1);
				}
			}
//for(int j = 0; j<realNewVerts.size(); j++)
//System.out.println("vertex: " + j + "   newCoords x:" + realNewVerts.get(j).getX()+ "   newCoords y:" + realNewVerts.get(j).getY()+ "   newCoords z:" + realNewVerts.get(j).getZ());

//System.out.println("Done with adding all the vertices");
	
//System.out.println(newVerts.size() + "   " + newFaces.size() + "   " + vertsToFaces.size());
			//Construct new Polygon Mesh
			setMesh(new FVPolygonMesh(newVerts, newFaces, vertsToFaces));
		}
	}
}
