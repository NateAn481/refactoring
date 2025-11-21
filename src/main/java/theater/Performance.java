package theater;

/**
 * Class representing a performance of a play.
 */
public class Performance {

    /** ID of the play being performed. */
    private String playID;

    /** Size of the audience for this performance. */
    private int audience;

    public Performance(String playID, int audience) {
        this.playID = playID;
        this.audience = audience;
    }

    public String getPlayID() {
        return playID;
    }

    public int getAudience() {
        return audience;
    }
}
