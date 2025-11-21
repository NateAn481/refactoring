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
        int totalAmount = 0;
        int volumeCredits = 0;
        StringBuilder result = new StringBuilder("Statement for "
                + invoice.getCustomer() + System.lineSeparator());

        NumberFormat format = NumberFormat.getCurrencyInstance(Locale.US);

        for (Performance p : invoice.getPerformances()) {

            int thisAmount = getThisAmount(p);

            // add volume credits
            volumeCredits = getVolumeCredits(p, volumeCredits);

            // print line for this order
            result.append(String.format("  %s: %s (%s seats)%n",
                    getPlay(p).name,
                    format.format(thisAmount / 100),
                    p.audience));

            totalAmount += thisAmount;
        }

        result.append(String.format("Amount owed is %s%n",
                format.format(totalAmount / 100)));
        result.append(String.format("You earned %s credits%n",
                volumeCredits));

        return result.toString();
    }

    @SuppressWarnings({"checkstyle:ParameterAssignment", "checkstyle:SuppressWarnings", "checkstyle:ParameterName"})
    private int getVolumeCredits(Performance p, int volumeCredits) {
        volumeCredits += Math.max(
                p.audience - Constants.BASE_VOLUME_CREDIT_THRESHOLD, 0);

        // add extra credit for every five comedy attendees
        if ("comedy".equals(getPlay(p).type)) {
            volumeCredits += p.audience / Constants.COMEDY_EXTRA_VOLUME_FACTOR;
        }
        return volumeCredits;
    }

    @SuppressWarnings({"checkstyle:ParameterName", "checkstyle:SuppressWarnings"})
    private Play getPlay(Performance p) {
        return plays.get(p.playID);
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
                            * (performance.audience - Constants.COMEDY_AUDIENCE_THRESHOLD));
                }
                result += Constants.COMEDY_AMOUNT_PER_AUDIENCE * performance.audience;
                break;

            default:
                throw new UnknownPlayTypeException(
                        String.format("unknown type: %s", getPlay(performance).type));
        }

        return result;
    }
}
