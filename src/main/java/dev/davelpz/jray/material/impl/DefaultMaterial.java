package dev.davelpz.jray.material.impl;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import dev.davelpz.jray.color.Color;
import dev.davelpz.jray.material.Material;
import dev.davelpz.jray.material.Pattern;

import java.util.Optional;

public class DefaultMaterial implements Material {
    private Color color;
    private double ambient;
    private double diffuse;
    private double specular;
    private double shininess;
    private double reflectivity;
    private double transparency;
    private double refractiveIndex;
    private Pattern pattern;

    @Inject
    public DefaultMaterial(@Assisted Color color, @Assisted("ambient") double ambient, @Assisted("diffuse") double diffuse,
            @Assisted("specular") double specular, @Assisted("shininess") double shininess, @Assisted("reflectivity") double reflectivity,
            @Assisted("transparency") double transparency, @Assisted("refractiveIndex") double refractiveIndex) {
        this.color = color;
        this.ambient = ambient;
        this.diffuse = diffuse;
        this.specular = specular;
        this.shininess = shininess;
        this.reflectivity = reflectivity;
        this.transparency = transparency;
        this.refractiveIndex = refractiveIndex;
    }

    @Override
    public Color color() {
        return color;
    }

    @Override
    public double ambient() {
        return ambient;
    }

    @Override
    public double diffuse() {
        return diffuse;
    }

    @Override
    public double specular() {
        return specular;
    }

    @Override
    public double shininess() {
        return shininess;
    }

    @Override
    public double reflectivity() {
        return reflectivity;
    }

    @Override
    public double transparency() {
        return transparency;
    }

    @Override
    public double refractiveIndex() {
        return refractiveIndex;
    }

    @Override
    public Optional<Pattern> pattern() {
        return Optional.ofNullable(pattern);
    }

    @Override
    public void setColor(Color color) {
        this.color = color;
    }

    @Override
    public void setAmbient(double ambient) {
        this.ambient = ambient;
    }

    @Override
    public void setDiffuse(double diffuse) {
        this.diffuse = diffuse;
    }

    @Override
    public void setSpecular(double specular) {
        this.specular = specular;
    }

    @Override
    public void setShininess(double shininess) {
        this.shininess = shininess;
    }

    @Override
    public void setReflectivity(double reflectivity) {
        this.reflectivity = reflectivity;
    }

    @Override
    public void setTransparency(double transparency) {
        this.transparency = transparency;
    }

    @Override
    public void setRefractiveIndex(double refractiveIndex) {
        this.refractiveIndex = refractiveIndex;
    }

    @Override
    public void setPattern(Pattern pattern) {
        this.pattern = pattern;
    }
}
