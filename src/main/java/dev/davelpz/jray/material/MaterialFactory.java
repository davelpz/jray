package dev.davelpz.jray.material;

import com.google.inject.assistedinject.Assisted;
import dev.davelpz.jray.color.Color;

public interface MaterialFactory {
    Material create(@Assisted Color color, @Assisted("ambient") double ambient, @Assisted("diffuse") double diffuse,
            @Assisted("specular") double specular, @Assisted("shininess") double shininess,
            @Assisted("reflectivity") double reflectivity, @Assisted("transparency") double transparency,
            @Assisted("refractiveIndex") double refractiveIndex);
}
