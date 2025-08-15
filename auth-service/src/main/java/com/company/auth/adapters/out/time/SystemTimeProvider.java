package com.company.auth.adapters.out.time;

import com.company.auth.domain.port.TimeProviderPort;
import org.springframework.stereotype.Component;

@Component
public class SystemTimeProvider implements TimeProviderPort {
    @Override public long nowMillis(){ return System.currentTimeMillis(); }
}
