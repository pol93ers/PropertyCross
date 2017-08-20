package mdpa.lasalle.propertycross.ui.fragments.main;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import mdpa.lasalle.propertycross.R;
import mdpa.lasalle.propertycross.base.fragment.FragmentBase;
import mdpa.lasalle.propertycross.util.Component;


public class CommentFragment extends FragmentBase {

    private Button addCommentButton;
    private EditText commentEditText;

    @NonNull
    @Override
    public ID getComponent() {
        return ID.CommentFragment;
    }

    public static CommentFragment newInstance() {
        return new CommentFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_comment, container, false);

        addCommentButton = (Button) root.findViewById(R.id.addCommentButton);
        commentEditText = (EditText) root.findViewById(R.id.commentEditText);

        setListeners();

        return root;
    }

    private void setListeners(){
        addCommentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isEmpty(commentEditText)){
                    String comment = commentEditText.getText().toString();
                }
            }
        });
    }

    private boolean isEmpty(EditText editText) {
        return editText.getText().toString().trim().length() == 0;
    }
}
