package mdpa.lasalle.propertycross.ui.fragments.main;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import mdpa.lasalle.propertycross.ApplicationPropertyCross;
import mdpa.lasalle.propertycross.R;
import mdpa.lasalle.propertycross.base.fragment.FragmentBase;
import mdpa.lasalle.propertycross.http.Http;
import mdpa.lasalle.propertycross.http.project.Requests;
import mdpa.lasalle.propertycross.http.project.request.RequestAddComment;
import mdpa.lasalle.propertycross.http.project.response.Response;
import mdpa.lasalle.propertycross.http.project.response.ResponseError;

public class CommentFragment extends FragmentBase {

    private Button addCommentButton;
    private EditText commentEditText;

    private String idProperty;

    @NonNull
    @Override
    public ID getComponent() {
        return ID.CommentFragment;
    }

    public static CommentFragment newInstance(String idProperty) {
        CommentFragment fragment = new CommentFragment();
        Bundle args = new Bundle();
        args.putString("idProperty", idProperty);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getHttpManager().receiverRegister(getContext(), Requests.Values.POST_COMMENT);
        idProperty = getArguments().getString("idProperty");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getHttpManager().receiverUnregister(getContext());
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
                    getHttpManager().callStart(
                            Http.RequestType.POST,
                            Requests.Values.POST_COMMENT,
                            idProperty + "/comments",
                            new RequestAddComment(comment,
                                    ApplicationPropertyCross.getInstance().preferences().getUserId()),
                            ApplicationPropertyCross.getInstance().preferences().getLoginApiKey(),
                            ApplicationPropertyCross.getInstance().preferences().getUserId(),
                            null
                    );
                }else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle(R.string.error);
                    builder.setMessage(R.string.empty_fields);
                    builder.setPositiveButton(R.string.ok, null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }
        });
    }

    private boolean isEmpty(EditText editText) {
        return editText.getText().toString().trim().length() == 0;
    }

    @Override
    public void onHttpBroadcastError(String requestId, ResponseError response) {
        super.onHttpBroadcastError(requestId, response);
        if (requestId.equals(Requests.Values.POST_COMMENT.id)) {

        }
    }

    @Override
    public void onHttpBroadcastSuccess(String requestId, Response response) {
        super.onHttpBroadcastSuccess(requestId, response);
        if (requestId.equals(Requests.Values.POST_COMMENT.id)) {
            getActivity().onBackPressed();
        }
    }
}
