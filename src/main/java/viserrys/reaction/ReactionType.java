package viserrys.reaction;

public enum ReactionType {

  LIKE("👍"), 
  HEART("💗"), 
  LAUGH("🤣"), 
  SAD("☹"), 
  CRY("😭"), 
  SHOCKED("😲"),
  ANGRY("😡");

  public final String emoji;

  ReactionType(String emoji) {
    this.emoji = emoji;
  }
}
