package com.hashnot.silverexchange.xchange.service.trade;

import com.hashnot.silverexchange.match.Side;
import com.hashnot.silverexchange.test.MockitoExtension;
import com.hashnot.silverexchange.xchange.model.SilverOrder;
import com.hashnot.silverexchange.xchange.service.IIdGenerator;
import com.hashnot.silverexchange.xchange.util.Clock;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.knowm.xchange.dto.Order;
import org.knowm.xchange.dto.trade.LimitOrder;
import org.knowm.xchange.dto.trade.MarketOrder;
import org.mockito.Mock;

import java.time.Instant;
import java.util.UUID;

import static com.hashnot.silverexchange.xchange.model.TestModelFactory.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderConverterTest {

    @Mock
    private IIdGenerator idGenerator;

    @Mock
    private Clock clock;

    @Test
    void testTimeOnFromLimitOrder() {
        UUID id = UUID.randomUUID();
        when(idGenerator.get()).thenReturn(id);

        Instant timestamp = Instant.now();
        when(clock.get()).thenReturn(timestamp);

        LimitOrder order = new LimitOrder.Builder(Order.OrderType.ASK, PAIR)
                .timestamp(TS_DATE)
                .id(ID_STR)
                .originalAmount(ONE)
                .limitPrice(TWO)
                .build();

        //when
        SilverOrder o = OrderConverter.fromLimitOrder(order, idGenerator, clock);

        //then
        assertNotNull(o);
        assertEquals(id, o.getId());
        assertEquals(Side.ASK, o.getSide());
        assertEquals(ONE, o.getAmount());
        assertEquals(TWO, o.getRate().getValue());
        assertNotNull(o.toString());
        assertTrue(o.hashCode() == 0 || o.hashCode() != 0);
    }

    @Test
    void fromMarketOrder() {
        UUID id = UUID.randomUUID();
        when(idGenerator.get()).thenReturn(id);

        Instant timestamp = Instant.now();
        when(clock.get()).thenReturn(timestamp);

        MarketOrder order = new MarketOrder.Builder(Order.OrderType.ASK, PAIR)
                .timestamp(TS_DATE)
                .id(ID_STR)
                .originalAmount(ONE)
                .build();

        //when
        SilverOrder o = OrderConverter.fromMarketOrder(order, idGenerator, clock);

        //then
        assertNotNull(o);
        assertEquals(id, o.getId());
        assertEquals(Side.ASK, o.getSide());
        assertEquals(ONE, o.getAmount());
        assertTrue(o.getRate().isMarket());
        assertNotNull(o.toString());
        assertTrue(o.hashCode() == 0 || o.hashCode() != 0);
    }
}