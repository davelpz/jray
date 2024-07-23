package dev.davelpz.jray.material;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.name.Named;
import dev.davelpz.jray.color.Color;
import dev.davelpz.jray.material.impl.CheckersPattern;
import dev.davelpz.jray.material.impl.GradientPattern;
import dev.davelpz.jray.material.impl.RingPattern;
import dev.davelpz.jray.material.impl.StripePattern;
import dev.davelpz.jray.material.impl.TestPattern;

public interface PatternFactory {
    @Named("stripePattern") StripePattern createStripePattern(@Assisted("a") Color a, @Assisted("b")  Color b);
    @Named("gradientPattern") GradientPattern createGradientPattern(@Assisted("a") Color a, @Assisted("b")  Color b);
    @Named("ringPattern") RingPattern createRingPattern(@Assisted("a") Color a, @Assisted("b")  Color b);
    @Named("checkersPattern") CheckersPattern createCheckersPattern(@Assisted("a") Color a, @Assisted("b")  Color b);
    @Named("testPattern") TestPattern createTestPattern();
}
