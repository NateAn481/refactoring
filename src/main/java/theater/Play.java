package theater;

/**
 * Class representing a play.
 */
public class Play {

    /** Name of the play. */
    private String name;

    /** Type of the play (e.g., tragedy, comedy). */
    private String type;

    public Play(String name, String type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }
}
