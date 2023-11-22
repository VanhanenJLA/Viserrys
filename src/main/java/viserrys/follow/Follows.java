package viserrys.follow;

import java.util.List;

public record Follows(List<Follow> sent, List<Follow> received) {
}
