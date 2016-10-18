package net.cloudapp.chooser.chooser.views.Statistics.StatisticsFragments;

import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import net.cloudapp.chooser.chooser.Common.PostRepository;
import net.cloudapp.chooser.chooser.R;


/**
 * Created by Ben on 08/07/2016.
 */
public class StatisticsFragmentManager extends Fragment {
    public static final String ARG_PAGE = "ARG_PAGE";
    private int postIndex;
    private MainFragment mainFramgent;
    private GenderFragment genderFragment;
    private AgeFragment ageFragment;


    public static StatisticsFragmentManager newInstance(int page, int postIndex) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        args.putSerializable("PostIndex", postIndex);
        StatisticsFragmentManager fragment = new StatisticsFragmentManager();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        postIndex = getArguments().getInt("PostIndex");
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState) {
        View view = null;
        switch (getArguments().getInt(ARG_PAGE)) {
            case 1:
                view = inflater.inflate(R.layout.frag_stat_general, container, false);
                mainFramgent = new MainFragment(view, postIndex);
                break;
            case 2:
                view = inflater.inflate(R.layout.frag_stat_gender, container, false);
                genderFragment = new GenderFragment(view);
                break;
            case 3:
                view = inflater.inflate(R.layout.frag_stat_age, container, false);
                ageFragment = new AgeFragment(view);
                break;
        }
        return view;
    }
}