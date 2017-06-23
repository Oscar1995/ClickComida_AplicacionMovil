package com.chile.oscar.clickcomida_aplicacionmovil;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Oscar on 23-06-2017.
 */

public class tab_map_work extends Fragment
{
    private static String TAG = "Mapa";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_map_work, container, false);
        return view;
    }
}
