package net.cloudapp.chooser.chooser.views.Statistics.StatisticsFragments;

import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import net.cloudapp.chooser.chooser.Common.PostRepository;
import net.cloudapp.chooser.chooser.Controller.PostStatisticsFetchController;
import net.cloudapp.chooser.chooser.R;
import net.cloudapp.chooser.chooser.model.PostStatistics;


/**
 * Created by Ben on 08/07/2016.
 */
public class StatisticsFragmentManager extends Fragment {
    public static final String ARG_PAGE = "ARG_PAGE";
    private static String post_id;
    private int postIndex;
    private PostStatistics postStatistics;
    private MainFragment mainFragment;
    private GenderFragment genderFragment;
    private AgeFragment ageFragment;


    public static StatisticsFragmentManager newInstance(int page, int postIndex) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        args.putSerializable("PostIndex", postIndex);
        post_id = PostRepository.myPosts.get(postIndex)._id;
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
                mainFragment = new MainFragment(view, postIndex);
                break;
            case 2:
                view = inflater.inflate(R.layout.frag_stat_gender, container, false);
                genderFragment = new GenderFragment(view);
                break;
            case 3:
                view = inflater.inflate(R.layout.frag_stat_age, container, false);
                ageFragment = new AgeFragment(view, this);
                break;
        }
        updateFragments();
        return view;
    }

    public void updateFragments() {
        PostStatisticsFetchController postStatisticsFetchController = new PostStatisticsFetchController(this, post_id);
        postStatisticsFetchController.getTempPostStatistics();
    }

    public void setPostStatistics (PostStatistics postStatistics) {
        this.postStatistics = postStatistics;
    }

    public void refreshFragments () {
        switch (getArguments().getInt(ARG_PAGE)) {
            case 1:
                mainFragment.refreshGeneralBarData();
                break;
            case 2:
                //updateGenderBarData(genderBarChart, ChartColors.GENDER_DIST1.val, ChartColors.GENDER_DIST2.val, postStatistics.getTotalFemaleVotes(),postStatistics.getTotalMaleVotes(),genDist1,genDist2);
                //updateGenderBarData(femaleBarChart, ChartColors.GENDER_FVOTES1.val,ChartColors.GENDER_FVOTES2.val, postStatistics.femaleVotes1,postStatistics.femaleVotes2,genF1,genF2);
                //updateGenderBarData(maleBarChart, ChartColors.GENDER_MVOTES1.val,ChartColors.GENDER_MVOTES2.val, postStatistics.maleVotes1,postStatistics.maleVotes2,genM1,genM2);
                break;
            case 3:
                //updateAgePieData();
                //updateAgeBarData();
                break;
        }
    }
}