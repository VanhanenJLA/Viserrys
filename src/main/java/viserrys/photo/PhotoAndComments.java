package viserrys.photo;

import viserrys.comment.Comment;

import java.util.List;

public record PhotoAndComments(Photo photo, List<Comment> comments) {
}
