package dev.davelpz.jray.light.operations.impl;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import dev.davelpz.jray.color.Color;
import dev.davelpz.jray.color.ColorFactory;
import dev.davelpz.jray.light.LightSource;
import dev.davelpz.jray.light.operations.LightOperations;
import dev.davelpz.jray.material.Material;
import dev.davelpz.jray.material.Pattern;
import dev.davelpz.jray.shape.Shape;
import dev.davelpz.jray.tuple.Tuple;

import java.util.Optional;

public class LightOperationsImpl implements LightOperations {
    public final ColorFactory colorFactory;
    private final Color black;

    @Inject
    public LightOperationsImpl(ColorFactory colorFactory, @Named("blackColor") Color black) {
        this.colorFactory = colorFactory;
        this.black = black;
    }

    @Override
    public Color lighting(Material material, Shape shape, LightSource light, Tuple position, Tuple eyev, Tuple normalv, boolean inShadow) {
        Color color;
        Optional<Pattern> patternOptional = material.pattern();
        if (patternOptional.isPresent()) {
            color = patternOptional.get().patternAtShape(shape, position);
        } else {
            color = material.color();
        }

        // Combine the surface color with the light's color/intensity
        Color effectiveColor = color.multiply(light.intensity());

        // Find the direction to the light source
        Tuple lightv = light.position().subtract(position).normalize();

        // Compute the ambient contribution
        Color ambient = effectiveColor.multiply(material.ambient());

        // lightDotNormal represents the cosine of the angle between the
        // light vector and the normal vector. A negative number means the
        // light is on the other side of the surface.
        double lightDotNormal = lightv.dot(normalv);
        Color diffuse;
        Color specular;
        if (lightDotNormal < 0) {
            diffuse = black;
            specular = black;
        } else {
            // Compute the diffuse contribution
            diffuse = effectiveColor.multiply(material.diffuse()).multiply(lightDotNormal);

            // reflectDotEye represents the cosine of the angle between the
            // reflection vector and the eye vector. A negative number means the
            // light reflects away from the eye.
            Tuple reflectv = lightv.negate().reflect(normalv);
            double reflectDotEye = reflectv.dot(eyev);
            if (reflectDotEye <= 0) {
                specular = black;
            } else {
                // Compute the specular contribution
                double factor = Math.pow(reflectDotEye, material.shininess());
                specular = light.intensity().multiply(material.specular()).multiply(factor);
            }
        }

        // if inShadow return ambient else return ambient + diffuse + specular
        if (inShadow) {
            return ambient;
        } else {
            return ambient.add(diffuse).add(specular);
        }
    }
}
