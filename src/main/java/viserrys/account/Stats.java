package viserrys.account;

public record Stats(Follows follows, Tweets tweets, Photos photos) {

    public record Follows(long sentCount, long receivedCount) {}

    public record Tweets(long sentCount, long receivedCount) {}

    public record Photos(long uploadedCount) {}
}
