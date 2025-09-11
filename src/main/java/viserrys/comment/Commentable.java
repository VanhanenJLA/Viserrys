package viserrys.comment;

import jakarta.validation.constraints.NotNull;

public interface Commentable {
    @NotNull Long getId();
    @NotNull String getType();
}
