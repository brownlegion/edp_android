package com.example.owner.edp;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by Krishna N. Deoram on 2016-03-27.
 * Gumi is love. Gumi is life.
 */
public class FragmentChangeMark extends Fragment {

    private Button button;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_change_mark, container, false);
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
