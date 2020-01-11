package group18;

import java.util.ArrayList;
import java.util.Date;

public class RedditPost {

    // POST-FIELDS
    boolean isTopLevelPost;
    boolean isRepost;

    // POST- & COMMENT-FIELDS
    String title;
    String id;
    String text;
    String username;
    int date;
    int upvotes;
    String source = "reddit";
    ArrayList<RedditPost> comments;
    String parentId;

    // GETTERS | SETTERS

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public boolean isTopLevelPost() {
        return isTopLevelPost;
    }

    public void setTopLevelPost(boolean topLevelPost) {
        isTopLevelPost = topLevelPost;
    }

    public boolean isRepost() {
        return isRepost;
    }

    public void setRepost(boolean repost) {
        isRepost = repost;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
    }

    public int getUpvotes() {
        return upvotes;
    }

    public void setUpvotes(int upvotes) {
        this.upvotes = upvotes;
    }

    public ArrayList<RedditPost> getComments() {
        return comments;
    }

    public void setComments(ArrayList<RedditPost> comments) {
        this.comments = comments;
    }

    public void addComment(RedditPost comment) {
        this.comments.add(comment);
    }
}
