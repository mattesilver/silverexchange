package com.hashnot.silverexchange.xchange.util;

import java.time.Instant;
import java.util.function.Supplier;

public interface Clock extends Supplier<Instant> {

    java.time.Clock SYSTEM_CLOCK = java.time.Clock.systemDefaultZone();

    static Clock systemDefaultZone() {
        return SYSTEM_CLOCK::instant;
    }
}
