package com.netcommlabs.greencontroller.activities;

import android.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.netcommlabs.greencontroller.R;

public class PlaceACTandMap extends AppCompatActivity {

    private PlaceAutocompleteFragment place_autocomplete_fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.place_ac_and_map);

        place_autocomplete_fragment = (PlaceAutocompleteFragment) getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
        place_autocomplete_fragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                Toast.makeText(PlaceACTandMap.this, "" + place, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(Status status) {
                Toast.makeText(PlaceACTandMap.this, "" + status.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
