package viserrys.Reaction;

public enum ReactionType {

  LIKE("👍"), 
  HEART("💗"), 
  LAUGH("🤣"), 
  SAD("☹"), 
  CRY("😭"), 
  SHOCKED("😲"),
  ANGRY("😡");

  public final String emoji;

  private ReactionType(String emoji) {
    this.emoji = emoji;
  }
}
