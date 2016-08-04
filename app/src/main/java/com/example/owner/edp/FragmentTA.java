package com.example.owner.edp;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class FragmentTA extends Fragment {

    private Button button;

    public FragmentTA() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        
        View rootView = inflater.inflate(R.layout.fragment_ad_ta, container, false);
        button = (Button)rootView.findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //getFragmentManager().beginTransaction().replace(R.id.marks_container, new FragmentMarksList()).commit();
                ((ActivityAfterSignedIn)getActivity()).pressBackButton();
            }
        });

        return rootView;
    }

}
