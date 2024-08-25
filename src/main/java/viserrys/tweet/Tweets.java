package viserrys.tweet;

import java.util.List;

public record Tweets(List<Tweet> sent, List<Tweet> received) {
}
