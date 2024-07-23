package dev.davelpz.jray.ray.operations.impl;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import dev.davelpz.jray.config.Constants;
import dev.davelpz.jray.ray.operations.Computation;
import dev.davelpz.jray.shape.Shape;
import dev.davelpz.jray.tuple.Tuple;

public class ComputationImpl implements Computation {
    private final double t;
    private final Shape object;
    private final Tuple point;
    private final Tuple eyeV;
    private final Tuple normalV;
    private final Tuple reflectV;
    private final boolean inside;
    private final Tuple overPoint;
    private final Tuple underPoint;
    private final double n1;
    private final double n2;

    @Inject
    public ComputationImpl(@Assisted("t") double t, @Assisted("object") Shape object,
                           @Assisted("point") Tuple point, @Assisted("eyeV") Tuple eyeV,
                           @Assisted("normalV") Tuple normalV, @Assisted("reflectV") Tuple reflectV, @Assisted("inside") boolean inside,
            @Assisted("n1") double n1, @Assisted("n2") double n2) {
        this.t = t;
        this.object = object;
        this.point = point;
        this.eyeV = eyeV;
        this.normalV = normalV;
        this.reflectV = reflectV;
        this.inside = inside;
        this.n1 = n1;
        this.n2 = n2;
        this.underPoint = point.subtract(normalV.multiply(Constants.EPSILON));
        this.overPoint = point.add(normalV.multiply(Constants.EPSILON));
    }

    @Override
    public double t() {
        return t;
    }

    @Override
    public Shape shape() {
        return object;
    }

    @Override
    public Tuple point() {
        return point;
    }

    @Override
    public Tuple eyeV() {
        return eyeV;
    }

    @Override
    public Tuple normalV() {
        return normalV;
    }

    @Override
    public Tuple reflectV() {
        return reflectV;
    }

    @Override
    public boolean inside() {
        return inside;
    }

    @Override
    public Tuple overPoint() {
        return overPoint;
    }

    @Override
    public Tuple underPoint() {
        return underPoint;
    }

    @Override
    public double n1() {
        return n1;
    }

    @Override
    public double n2() {
        return n2;
    }

    @Override
    public double schlick() {
        // find the cosine of the angle between the eye and normal vectors
        double cos = eyeV.dot(normalV);

        // total internal reflection can only occur if n1 > n2
        if (n1 > n2) {
            double n = n1 / n2;
            double sin2T = n * n * (1 - cos * cos);
            if (sin2T > 1) {
                return 1;
            }

            // compute cosine of theta_t using trig identity
            // when n1 > n2, use cos(theta_t) instead
            cos = Math.sqrt(1 - sin2T);
        }

        double temp = (n1 - n2) / (n1 + n2);
        double r0 = temp * temp;
        double temp2 = 1 - cos;
        return r0 + (1 - r0) * (temp2 * temp2 * temp2 * temp2 * temp2);
    }
}
