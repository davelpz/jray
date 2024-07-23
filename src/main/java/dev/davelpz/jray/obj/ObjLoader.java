package dev.davelpz.jray.obj;

import com.google.inject.Inject;
import de.javagl.obj.FloatTuple;
import de.javagl.obj.Obj;
import de.javagl.obj.ObjFace;
import de.javagl.obj.ObjGroup;
import de.javagl.obj.ObjReader;
import dev.davelpz.jray.shape.Group;
import dev.davelpz.jray.shape.GroupFactory;
import dev.davelpz.jray.shape.Shape;
import dev.davelpz.jray.shape.TriangleFactory;
import dev.davelpz.jray.tuple.impl.TupleFactory;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ObjLoader {
    private final TriangleFactory triangleFactory;
    private final TupleFactory tupleFactory;
    private final GroupFactory groupFactory;

    @Inject
    public ObjLoader(TriangleFactory triangleFactory, TupleFactory tupleFactory, GroupFactory groupFactory) {
        this.triangleFactory = triangleFactory;
        this.tupleFactory = tupleFactory;
        this.groupFactory = groupFactory;
    }

    public List<Group> parseObjFile(String fileName) {
        List<Group> groups = new ArrayList<>();
        try {
            Obj obj = ObjReader.read(new FileReader(fileName));
            for (int i = 0; i < obj.getNumGroups(); i++) {
                ObjGroup group = obj.getGroup(i);
                if (group.getNumFaces() > 0) {
                    Group newGroup = groupFactory.create();
                    List<Shape> triangles = new ArrayList<>();
                    for (int f = 0; f < group.getNumFaces(); f++) {
                        ObjFace face = group.getFace(f);
                        List<Shape> faceTriangles = getTriangles(obj, face);
                        triangles.addAll(faceTriangles);
                    }
                    newGroup.addChildren(triangles);
                    groups.add(newGroup);
                }
            }
        } catch (IOException ignored) {
        }

        return groups;
    }

    List<Shape> getTriangles(Obj obj, ObjFace face) {
        List<Shape> triangles = new ArrayList<>();
        int numVert = face.getNumVertices();
        if (numVert == 3) {
            FloatTuple point1 = obj.getVertex(face.getVertexIndex(0));
            FloatTuple point2 = obj.getVertex(face.getVertexIndex(1));
            FloatTuple point3 = obj.getVertex(face.getVertexIndex(2));
            triangles.add(triangleFactory.create(tupleFactory.point(point1.getX(), point1.getY(), point1.getZ()),
                    tupleFactory.point(point2.getX(), point2.getY(), point2.getZ()),
                    tupleFactory.point(point3.getX(), point3.getY(), point3.getZ())));
        } else {
            for (int i = 1; i < (numVert - 1); i++) {
                FloatTuple point1 = obj.getVertex(face.getVertexIndex(0));
                FloatTuple point2 = obj.getVertex(face.getVertexIndex(i));
                FloatTuple point3 = obj.getVertex(face.getVertexIndex(i + 1));
                triangles.add(triangleFactory.create(tupleFactory.point(point1.getX(), point1.getY(), point1.getZ()),
                        tupleFactory.point(point2.getX(), point2.getY(), point2.getZ()),
                        tupleFactory.point(point3.getX(), point3.getY(), point3.getZ())));
            }
        }

        return triangles;
    }

}
