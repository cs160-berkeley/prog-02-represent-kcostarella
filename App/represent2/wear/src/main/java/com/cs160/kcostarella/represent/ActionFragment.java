package com.cs160.kcostarella.represent;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.wearable.view.CircledImageView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.app.Fragment;
/**
 * Created by kcostarella on 3/2/16.
 */
public class ActionFragment extends Fragment implements View.OnClickListener {

    public Listener mListener;
    private CircledImageView vIcon;
    private TextView vLabel;

    public static ActionFragment create(int iconResId, String labelResId, Listener listener) {
        //mListener = listener;
        ActionFragment fragment = new ActionFragment();
        fragment.mListener = listener;
        Bundle args = new Bundle();
        args.putInt("ICON", iconResId);
        args.putString("LABEL", labelResId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_action, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        vIcon = (CircledImageView) view.findViewById(R.id.icon);
        vLabel = (TextView) view.findViewById(R.id.label);
        vIcon.setImageResource(getArguments().getInt("ICON"));
        vLabel.setText(getArguments().getString("LABEL"));
        view.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        mListener.onActionPerformed();
    }

    public interface Listener {
        public void onActionPerformed();
    }
}
