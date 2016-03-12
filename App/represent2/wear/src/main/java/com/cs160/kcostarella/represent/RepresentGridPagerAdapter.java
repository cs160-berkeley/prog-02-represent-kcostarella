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
    protected final Context mContext;
    protected final Page[][] PAGES;

    public RepresentGridPagerAdapter(Context ctx, FragmentManager fm) {
        super(fm);
        mContext = ctx;
        PAGES = getPages();
    }


    protected static class Page {
        String titleRes;
        String bodyRes = "";
        int iconRes;
        int code;
    }


    public Page[][] getPages()
    {
        Page page1 = new Page();
        page1.titleRes = "blah";
        page1.iconRes = R.drawable.barbara_lee;
        page1.code = 0;

        Page page2 = new Page();
        page2.titleRes = "blah";
        page2.iconRes = R.drawable.barbara_boxer;
        page2.code = 1;

        Page page3 = new Page();
        page3.titleRes = "blah";
        page3.iconRes = R.drawable.dianne_feinstein;
        page3.code = 2;

        Page page4 = new Page();
        page4.titleRes = "SuperBlah";

        return new Page[][]{new Page[]{page1, page2, page3}, new Page[]{page4}};
    }

    @Override
    public Fragment getFragment(int row, int col) {

        final Page page = PAGES[row][col];
        String title = page.titleRes.equals("") ? page.titleRes : null;
        String body = page.bodyRes.equals("") ? page.bodyRes : null;
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
            CardFragment fragment = CardFragment.create(page.titleRes, page.bodyRes, page.iconRes);
            return fragment;
        }

        //Advanced settings
        //fragment.setCardGravity(page.cardGravity);
        //fragment.setExpansionEnabled(page.expansionEnabled);
        //fragment.setExpansionDirection(page.expansionDirection);
        //fragment.setExpansionFactor(page.expansionFactor);

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
            //return mContext.getResources().getDrawable(R.drawable.open_on_phone, null);
            return super.getBackgroundForPage(row,column);
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