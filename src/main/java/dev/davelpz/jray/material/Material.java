package dev.davelpz.jray.material;

import dev.davelpz.jray.color.Color;

import java.util.Optional;

public interface Material {
    Color color();
    double ambient();
    double diffuse();
    double specular();
    double shininess();
    double reflectivity();
    double transparency();
    double refractiveIndex();
    Optional<Pattern> pattern();
    void setColor(Color color);
    void setAmbient(double ambient);
    void setDiffuse(double diffuse);
    void setSpecular(double specular);
    void setShininess(double shininess);
    void setReflectivity(double reflectivity);
    void setTransparency(double transparency);
    void setRefractiveIndex(double refractiveIndex);

    void setPattern(Pattern pattern);

    default boolean equals(Material material) {
        return color().equals(material.color()) && ambient() == material.ambient() && diffuse() == material.diffuse()
                && specular() == material.specular() && shininess() == material.shininess();
    }
}
