package net.cloudapp.chooser.chooser;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;

/**
 * Created by Ben on 17/07/2016.
 */
public class PostStatistics implements Serializable {
    public int maleVotes1, maleVotes2, femaleVotes1, femaleVotes2;
    public ArrayList<Integer> ageVotes1, ageVotes2;
    public HashMap<Locale,Integer> countryVotes1, countryVotes2;
    public int ageIntervals, fromAgeGroup, toAgeGroup;

    public PostStatistics () {
        femaleVotes1 = 0;
        femaleVotes2 = 0;
        maleVotes1 = 0;
        maleVotes2 = 0;
        ageIntervals = 5;
        fromAgeGroup = 0;
        toAgeGroup = 21;
    }

    public int getTotalFemaleVotes() {
        return femaleVotes1+femaleVotes2;
    }

    public int getTotalMaleVotes() {
        return maleVotes1+maleVotes2;
    }

    public ArrayList<Integer> getTotalAgeRangeVotes() {
        int ageCap = Math.max(ageVotes1.size(),ageVotes2.size());

        ArrayList<Integer> voteArray = new ArrayList<>(Collections.nCopies(1 + ageCap/ageIntervals, 0));

        for (int i = 0; i < ageCap; i++) {
            int currInterval = i/ageIntervals;
            if (i < ageVotes1.size())
                voteArray.set(currInterval, voteArray.get(currInterval) + ageVotes1.get(i));
            if (i < ageVotes2.size())
                voteArray.set(currInterval, voteArray.get(currInterval) + ageVotes2.get(i));
        }
    return voteArray;
    }

    public int getAgeRangeVotes(int voteNum) {
        //start inclusive, end inclusive
        int count = 0;
        ArrayList<Integer> arrayCount;
        if (voteNum == 1)
            arrayCount = ageVotes1;
        else if (voteNum == 2)
            arrayCount = ageVotes2;
        else return 0;

        int arraySize = arrayCount.size();
        if (fromAgeGroup > arraySize)
            return 0;

        for (int i = fromAgeGroup; i < Math.min(toAgeGroup+1,arraySize); i++)
            count += arrayCount.get(i);


        return count;
    }

}
