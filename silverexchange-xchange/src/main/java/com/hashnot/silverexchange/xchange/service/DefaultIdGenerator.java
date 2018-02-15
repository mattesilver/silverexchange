package com.hashnot.silverexchange.xchange.service;

import java.util.UUID;

public class DefaultIdGenerator implements IIdGenerator {
    @Override
    public UUID get() {
        return UUID.randomUUID();
    }
}
