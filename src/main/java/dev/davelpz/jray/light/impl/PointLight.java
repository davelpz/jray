package dev.davelpz.jray.light.impl;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import dev.davelpz.jray.color.Color;
import dev.davelpz.jray.light.LightSource;
import dev.davelpz.jray.tuple.Tuple;

public class PointLight implements LightSource {
    private final Tuple position;
    private final Color intensity;

    @Inject
    public PointLight(@Assisted Tuple position, @Assisted Color intensity) {
        this.position = position;
        this.intensity = intensity;
    }

    @Override
    public Color intensity() {
        return intensity;
    }

    @Override
    public Tuple position() {
        return position;
    }
}
