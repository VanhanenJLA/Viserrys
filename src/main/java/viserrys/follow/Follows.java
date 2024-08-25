package viserrys.follow;

import org.springframework.data.domain.Page;

public record Follows(Page<Follow> sent, Page<Follow> received) {
}
