package viserrys.follow;

import org.springframework.data.domain.Page;

import java.util.List;

public record Follows(Page<Follow> sent, Page<Follow> received) {
}
