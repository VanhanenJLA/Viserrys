package viserrys.account;

public class Stats {
    public Follows follows;
    public Tweets tweets;
    public Photos photos;

    public static Stats create() {
        return new Stats();
    }

    public Stats follows(long sentCount, long receivedCount) {
        this.follows = new Follows(sentCount, receivedCount);
        return this;
    }

    public Stats tweets(long sentCount, long receivedCount) {
        this.tweets = new Tweets(sentCount, receivedCount);
        return this;
    }

    public Stats photos(long uploadedCount) {
        this.photos = new Photos(uploadedCount);
        return this;
    }

    public static class Follows {
        public long sentCount;
        public long receivedCount;

        public Follows(long sentCount, long receivedCount) {
            this.sentCount = sentCount;
            this.receivedCount = receivedCount;
        }
    }

    public static class Tweets {
        public long sentCount;
        public long receivedCount;

        public Tweets(long sentCount, long receivedCount) {
            this.sentCount = sentCount;
            this.receivedCount = receivedCount;
        }
    }

    public static class Photos {
        public long uploadedCount;

        public Photos(long uploadedCount) {
            this.uploadedCount = uploadedCount;
        }
    }
}