package mdpa.lasalle.propertycross.data.adapter;

import android.support.annotation.NonNull;

import mdpa.lasalle.propertycross.ui.adapters.AdapterRecyclerComments;

public class CommentItem implements AdapterRecyclerComments.CommentsItem<CommentItem.Comment> {

    public static class Comment implements AdapterRecyclerComments.CommentsItem.Comment {
        private String username, text, time;

        public Comment(String username, String text, String time) {
            this.username = username;
            this.text = text;
            this.time = time;
        }

        @NonNull
        @Override
        public String getUsername() {
            return username;
        }

        @NonNull
        @Override
        public String getTextComment() {
            return text;
        }

        @NonNull
        @Override
        public String getTimeComment() {
            return time;
        }
    }

    private Object item;

    public CommentItem(@NonNull Comment comment) {
        this.item = comment;
    }

    @NonNull @Override
    public Comment getComment() {
        return (Comment)item;
    }
}
