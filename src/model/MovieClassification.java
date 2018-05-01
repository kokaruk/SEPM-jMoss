package model;

/**
 * enumerator for movie classifications
 */
public enum MovieClassification {

    G("G"),
    PG("PG"),
    M("M"),
    MA15("MA15+");

    private final String classification;

    MovieClassification(String classification) {
        this.classification = classification;
    }

    public String getClassification() {
        return classification;
    }
}
