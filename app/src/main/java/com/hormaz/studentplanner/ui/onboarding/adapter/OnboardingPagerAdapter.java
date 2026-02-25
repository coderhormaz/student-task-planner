package com.hormaz.studentplanner.ui.onboarding.adapter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.hormaz.studentplanner.R;

public class OnboardingPagerAdapter extends FragmentStateAdapter {

    private static final int PAGE_COUNT = 4;

    private static final int[] TITLES = {
            R.string.onboarding_1_title,
            R.string.onboarding_2_title,
            R.string.onboarding_3_title,
            R.string.onboarding_4_title
    };

    private static final int[] DESCRIPTIONS = {
            R.string.onboarding_1_desc,
            R.string.onboarding_2_desc,
            R.string.onboarding_3_desc,
            R.string.onboarding_4_desc
    };

    private static final int[] ICONS = {
            R.drawable.ic_onboarding_tasks,
            R.drawable.ic_onboarding_reminders,
            R.drawable.ic_onboarding_progress,
            R.drawable.ic_onboarding_offline
    };

    public OnboardingPagerAdapter(@NonNull FragmentActivity activity) {
        super(activity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return OnboardingPageFragment.newInstance(
                TITLES[position], DESCRIPTIONS[position], ICONS[position]);
    }

    @Override
    public int getItemCount() {
        return PAGE_COUNT;
    }

    public static class OnboardingPageFragment extends Fragment {

        private static final String ARG_TITLE = "title";
        private static final String ARG_DESC = "desc";
        private static final String ARG_ICON = "icon";

        public static OnboardingPageFragment newInstance(int titleRes, int descRes, int iconRes) {
            OnboardingPageFragment fragment = new OnboardingPageFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_TITLE, titleRes);
            args.putInt(ARG_DESC, descRes);
            args.putInt(ARG_ICON, iconRes);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.item_onboarding_page, container, false);

            if (getArguments() != null) {
                ((TextView) view.findViewById(R.id.tvTitle))
                        .setText(getArguments().getInt(ARG_TITLE));
                ((TextView) view.findViewById(R.id.tvDescription))
                        .setText(getArguments().getInt(ARG_DESC));
                ((ImageView) view.findViewById(R.id.ivIcon))
                        .setImageResource(getArguments().getInt(ARG_ICON));
            }

            return view;
        }
    }
}
