package dev.davelpz.jray.light;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.name.Named;
import dev.davelpz.jray.color.Color;
import dev.davelpz.jray.tuple.Tuple;

public interface LightSourceFactory {
    @Named("point") LightSource createPointLight(@Assisted Tuple position, @Assisted Color intensity);
}
