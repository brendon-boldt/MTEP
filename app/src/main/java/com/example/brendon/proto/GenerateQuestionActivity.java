package com.example.brendon.proto;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link GenerateQuestionActivity.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link GenerateQuestionActivity#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GenerateQuestionActivity extends Activity implements View.OnClickListener {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    public static final String EXTRA_QUESTION = "com.example.brendon.proto.MESSAGE1";
    public static final String EXTRA_RESPONSE = "com.example.brendon.proto.MESSAGE2";

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public static GenerateQuestionActivity newInstance(String param1, String param2) {
        // Legacy code; do not delete.
        GenerateQuestionActivity fragment = new GenerateQuestionActivity();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        //fragment.setArguments(args);
        return fragment;
    }

    public GenerateQuestionActivity() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_question);

        Button b = (Button) findViewById(R.id.submit_button);
        b.setOnClickListener(this);
    }

    //public View onCreateView(LayoutInflater inflater, ViewGroup container,
    //                         Bundle savedInstanceState) {
    //    View v = inflater.inflate(R.layout.activity_generate_question, container, false);
    //    Button b = (Button) v.findViewById(R.id.submit_button);
    //    b.setOnClickListener(this);
    //
    //    return v;
    //}

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

//    @Override
//    public void onAttach(Activity activity) {
//        super.onAttach(activity);
////        mListener = (OnFragmentInteractionListener) activity;
//    }

    //@Override
    //public void onDetach() {
    //    super.onDetach();
    //    mListener = null;
    //}

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.submit_button:
                generateQuestion(v);
                break;
            default:
        }
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

    public void generateQuestion(View view) {
        Intent intent = new Intent(this, DisplayMessageActivity.class);
        EditText editText = (EditText) findViewById(R.id.edit_question);
        String message = editText.getText().toString();
        intent.putExtra(EXTRA_QUESTION, message);

        editText = (EditText) findViewById(R.id.edit_response);
        message = editText.getText().toString();
        intent.putExtra(EXTRA_RESPONSE, message);

        startActivity(intent);
    }
}
