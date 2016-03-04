package com.cs160.kcostarella.represent;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.wearable.view.CardFragment;
import android.support.wearable.view.FragmentGridPagerAdapter;

/**
 * Created by kcostarella on 3/1/16.
 */
public class RepresentGridPagerAdapter extends FragmentGridPagerAdapter {
    private final Context mContext;
    private final Page[][] PAGES;

    public RepresentGridPagerAdapter(Context ctx, FragmentManager fm) {
        super(fm);
        mContext = ctx;
        PAGES = getPages();
    }


    private static class Page {
        int titleRes;
        int textRes;
        int iconRes;
        int code;
    }


    private Page[][] getPages()
    {
        Page page1 = new Page();
        page1.titleRes = R.string.title_1;
        page1.textRes = R.string.body_1;
        page1.iconRes = R.drawable.barbara_lee;
        page1.code = 0;

        Page page2 = new Page();
        page2.titleRes = R.string.title_2;
        page2.textRes = R.string.body_2;
        page2.iconRes = R.drawable.barbara_boxer;
        page2.code = 1;

        Page page3 = new Page();
        page3.titleRes = R.string.title_3;
        page3.textRes = R.string.body_3;
        page3.iconRes = R.drawable.dianne_feinstein;
        page3.code = 2;

        Page page4 = new Page();
        page4.titleRes = R.string.title_4;
        page4.textRes = R.string.body_4;

        return new Page[][]{new Page[]{page1, page2, page3}, new Page[]{page4}};
    }

    @Override
    public Fragment getFragment(int row, int col) {

        final Page page = PAGES[row][col];
        String title = page.titleRes != 0 ? mContext.getString(page.titleRes) : null;
        String text = page.textRes != 0 ? mContext.getString(page.textRes) : null;
        //CardFragment fragment = CardFragment.create(title,text,page.iconRes);

        if (row == 0) {
            ActionFragment fragment = ActionFragment.create(page.iconRes, page.titleRes, new ActionFragment.Listener() {
                @Override
                public void onActionPerformed() {
                    Intent watchToPhone = new Intent(mContext, WatchToPhoneService.class);
                    watchToPhone.putExtra("ARG", "info");
                    watchToPhone.putExtra("OPT", page.code + "");
                    mContext.startService(watchToPhone);
                }
            });
            return fragment;
        }

        else {
            CardFragment fragment = CardFragment.create(title, text, page.iconRes);
            return fragment;
        }

        //Advanced settings
        //fragment.setCardGravity(page.cardGravity);
        //fragment.setExpansionEnabled(page.expansionEnabled);
        //fragment.setExpansionDirection(page.expansionDirection);
        //fragment.setExpansionFactor(page.expansionFactor);

    }

    private int getCode(int androidSucks) {
        switch (androidSucks) {
            case R.drawable.barbara_lee:
                return 0;
            case R.drawable.barbara_boxer:
                return 1;
            case R.drawable.dianne_feinstein:
                return 2;
        }
        return 3;
    }


    //obtain background color from the row
    @Override
    public Drawable getBackgroundForRow(int row) {
       return super.getBackgroundForRow(row);
    }

    //Obtain the background for a specific page
    @Override
    public Drawable getBackgroundForPage(int row, int column) {
        if (row == 0) {
            return mContext.getResources().getDrawable(PAGES[row][column].iconRes, null);
        }
        return super.getBackgroundForPage(row, column);
    }

    //Obtain the number of pages (vertical)
    @Override
    public int getRowCount() {
        return PAGES.length;
    }

    //Obtain the number of pages (horizontal)
    @Override
    public int getColumnCount(int rowNum) {
        return PAGES[rowNum].length;
    }



}