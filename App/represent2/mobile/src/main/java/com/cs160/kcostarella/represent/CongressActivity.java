package com.cs160.kcostarella.represent;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class CongressActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;
    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_congress);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), this);

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_congress, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class CongressFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static Boolean running = false;
        private static final String ARG_SECTION_NUMBER = "section_number";
        private static final String ARG_IMAGE_NUMBER = "image_number";
        private static final String ARG_DESCRIPTION_NUMBER = "description_number";
        public CongressFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static CongressFragment newInstance(int sectionNumber) {
            CongressFragment fragment = new CongressFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            args.putInt(ARG_IMAGE_NUMBER, ImageTable(sectionNumber));
            args.putInt(ARG_DESCRIPTION_NUMBER, NameTable(sectionNumber));
            fragment.setArguments(args);
            return fragment;
        }

        private static int ImageTable(int sectionNumber) {
            switch (sectionNumber) {
                case 1:
                    return R.drawable.barbara_lee;
                case 2:
                    return R.drawable.barbara_boxer;
                case 3:
                    return R.drawable.dianne_feinstein;
            }
            return 0;
        }

        private static int NameTable(int sectionNumber) {
            switch (sectionNumber) {
                case 1:
                    return R.string.barbara_lee;
                case 2:
                    return R.string.barbara_boxer;
                case 3:
                    return R.string.dianne_feinstein;

            }
            return 0;
        }

        static final String[] TWEET_STRINGS = new String[]{"Tweet 1", "Tweet 2"};

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_congress, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            ImageView imageView = (ImageView) rootView.findViewById(R.id.imageView);
            Button email = (Button) rootView.findViewById(R.id.email);
            email.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Snackbar.make(view, "Link to email shown here", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            });
            Button website = (Button) rootView.findViewById(R.id.website);
            website.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Snackbar.make(view, "Link to website shown here", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            });
            FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent activity = new Intent(getActivity().getBaseContext(), InfoActivity.class);
                    activity.putExtra("ARG",getArguments().getInt(ARG_IMAGE_NUMBER));
                    activity.putExtra("OPT","blue");
                    startActivity(activity);
                }
            });
            TextView tweet = (TextView) rootView.findViewById(R.id.tweet);
            textView.setText(getString(getArguments().getInt(ARG_DESCRIPTION_NUMBER)));
            imageView.setImageResource(getArguments().getInt(ARG_IMAGE_NUMBER));
            return rootView;
        }
    }



    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        Context context;
        public SectionsPagerAdapter(FragmentManager fm, Context ctx) {
            super(fm);
            context = ctx;
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return CongressFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "SECTION 1";
                case 1:
                    return "SECTION 2";
                case 2:
                    return "SECTION 3";
            }
            return null;
        }
    }
}
