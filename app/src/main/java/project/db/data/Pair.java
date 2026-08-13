package project.db.data;

public class Pair<Y,K> {

    private Y first;
    private K second;

    public Pair(Y first, K second) {
        this.first = first;
        this.second = second;
    }

    public Y getFirst() {
        return first;
    }

    public K getSecond() {
        return second;
    }


}
