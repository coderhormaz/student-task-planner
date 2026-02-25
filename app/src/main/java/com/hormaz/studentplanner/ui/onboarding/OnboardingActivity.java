package com.hormaz.studentplanner.ui.onboarding;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.hormaz.studentplanner.App;
import com.hormaz.studentplanner.R;
import com.hormaz.studentplanner.ui.auth.LoginActivity;
import com.hormaz.studentplanner.ui.onboarding.adapter.OnboardingPagerAdapter;

public class OnboardingActivity extends AppCompatActivity {

    private ViewPager2 viewPager;
    private Button btnNext;
    private TextView tvSkip;
    private TabLayout tabDots;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);

        viewPager = findViewById(R.id.viewPager);
        btnNext = findViewById(R.id.btnNext);
        tvSkip = findViewById(R.id.tvSkip);
        tabDots = findViewById(R.id.tabDots);

        OnboardingPagerAdapter adapter = new OnboardingPagerAdapter(this);
        viewPager.setAdapter(adapter);

        new TabLayoutMediator(tabDots, viewPager, (tab, position) -> {
            // Dots only, no text
        }).attach();

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                if (position == adapter.getItemCount() - 1) {
                    btnNext.setText(R.string.get_started);
                } else {
                    btnNext.setText(R.string.next);
                }
            }
        });

        btnNext.setOnClickListener(v -> {
            int current = viewPager.getCurrentItem();
            if (current < adapter.getItemCount() - 1) {
                viewPager.setCurrentItem(current + 1);
            } else {
                completeOnboarding();
            }
        });

        tvSkip.setOnClickListener(v -> completeOnboarding());
    }

    private void completeOnboarding() {
        App.getInstance().getPreferencesManager().setOnboardingComplete(true);
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }
}
