package theater;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.Map;

/**
 * This class generates a statement for a given invoice of performances.
 */
public class StatementPrinter {
    @SuppressWarnings({"checkstyle:VisibilityModifier", "checkstyle:SuppressWarnings"})
    public Invoice invoice;
    @SuppressWarnings({"checkstyle:VisibilityModifier", "checkstyle:SuppressWarnings"})
    public Map<String, Play> plays;

    public StatementPrinter(Invoice invoice, Map<String, Play> plays) {
        this.invoice = invoice;
        this.plays = plays;
    }

    /**
     * Returns a formatted statement of the invoice associated with this printer.
     *
     * @return the formatted statement
     * @throws UnknownPlayTypeException if one of the play types is not known
     */
    @SuppressWarnings(
            {"checkstyle:FinalLocalVariable", "checkstyle:SuppressWarnings",
                    "checkstyle:MagicNumber", "checkstyle:NeedBraces", "checkstyle:Indentation"})
    public String statement() {
        StringBuilder result = new StringBuilder("Statement for "
                + invoice.getCustomer() + System.lineSeparator());

        // Loop 3: build lines for each performance
        for (Performance performance : invoice.getPerformances()) {
            result.append(String.format("  %s: %s (%s seats)%n",
                    getPlay(performance).name,
                    usd(getThisAmount(performance)),
                    performance.audience));
        }

        // Use helper methods to get totals
        result.append(String.format("Amount owed is %s%n",
                usd(getTotalAmount())));
        result.append(String.format("You earned %s credits%n",
                getTotalVolumeCredits()));

        return result.toString();
    }

    @SuppressWarnings({"checkstyle:MagicNumber", "checkstyle:SuppressWarnings"})
    private static String usd(int amount) {
        return NumberFormat.getCurrencyInstance(Locale.US).format(amount / 100);
    }

    @SuppressWarnings({"checkstyle:SuppressWarnings"})
    private int getTotalAmount() {
        int totalAmount = 0;
        for (Performance performance : invoice.getPerformances()) {
            totalAmount += getThisAmount(performance);
        }
        return totalAmount;
    }

    @SuppressWarnings({"checkstyle:SuppressWarnings"})
    private int getTotalVolumeCredits() {
        int volumeCredits = 0;
        for (Performance performance : invoice.getPerformances()) {
            volumeCredits += getVolumeCredits(performance);
        }
        return volumeCredits;
    }

    @SuppressWarnings({"checkstyle:MagicNumber", "checkstyle:SuppressWarnings"})
    private int getVolumeCredits(Performance performance) {
        int result = 0;

        result += Math.max(
                performance.audience - Constants.BASE_VOLUME_CREDIT_THRESHOLD, 0);

        // add extra credit for every five comedy attendees
        if ("comedy".equals(getPlay(performance).type)) {
            result += performance.audience / Constants.COMEDY_EXTRA_VOLUME_FACTOR;
        }
        return result;
    }

    @SuppressWarnings({"checkstyle:SuppressWarnings"})
    private Play getPlay(Performance performance) {
        return plays.get(performance.playID);
    }

    @SuppressWarnings({"checkstyle:MagicNumber", "checkstyle:SuppressWarnings"})
    private int getThisAmount(Performance performance) {
        int result;

        switch (getPlay(performance).type) {
            case "tragedy":
                result = 40000;
                if (performance.audience > Constants.TRAGEDY_AUDIENCE_THRESHOLD) {
                    result += 1000 * (performance.audience - 30);
                }
                break;

            case "comedy":
                result = Constants.COMEDY_BASE_AMOUNT;
                if (performance.audience > Constants.COMEDY_AUDIENCE_THRESHOLD) {
                    result += Constants.COMEDY_OVER_BASE_CAPACITY_AMOUNT
                            + (Constants.COMEDY_OVER_BASE_CAPACITY_PER_PERSON
                            * (performance.audience
                            - Constants.COMEDY_AUDIENCE_THRESHOLD));
                }
                result += Constants.COMEDY_AMOUNT_PER_AUDIENCE * performance.audience;
                break;

            default:
                throw new UnknownPlayTypeException(
                        String.format("unknown type: %s",
                                getPlay(performance).type));
        }

        return result;
    }
}
