package com.hashnot.silverexchange.match;

import java.util.Objects;

/**
 * A result of a single atomic match {@link Offer#match(Offer, ITransactionListener)} of one {@link Offer} against another.
 * <p>
 * All fields are nullable
 */
public class OfferMatchResult<OfferT extends Offer> {

    /**
     * A remainder of the currently executed {@link Offer}. If the offer was fully matched it's null. If the offer wasn't matched at all, it's the same object as the original offer
     */
    public final OfferT activeRemainder;

    /**
     * A remainder of the {@link Offer} from order book. if the offer was full matched, it's null and the offer should be removed from the order book. Otherwise it should replace the first offer in the order book.
     */
    public final OfferT passiveRemainder;

    OfferMatchResult(OfferT activeRemainder, OfferT passiveRemainder) {
        this.activeRemainder = activeRemainder;
        this.passiveRemainder = passiveRemainder;
    }

    @Override
    public String toString() {
        return
                "activeRemainder=" + activeRemainder
                        + ", passiveRemainder=" + passiveRemainder
                ;
    }

    @Override
    public boolean equals(Object o) {
        return this == o || o instanceof OfferMatchResult && equals((OfferMatchResult) o);
    }

    private boolean equals(OfferMatchResult r) {
        return
                Objects.equals(activeRemainder, r.activeRemainder)
                        && Objects.equals(passiveRemainder, r.passiveRemainder)
                ;
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                activeRemainder,
                passiveRemainder
        );
    }
}
