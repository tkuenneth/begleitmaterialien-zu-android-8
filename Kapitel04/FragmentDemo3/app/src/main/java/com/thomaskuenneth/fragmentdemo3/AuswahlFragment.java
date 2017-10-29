package com.thomaskuenneth.fragmentdemo3;

import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class AuswahlFragment extends ListFragment {

    private static final String STR_ZULETZT_SELEKTIERT =
            "zuletztSelektiert";

    boolean zweiSpaltenModus;
    int zuletztSelektiert = 0;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setListAdapter(new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_list_item_activated_1,
                new String[]{"eins", "zwei", "drei"}));
        View detailsFrame = getActivity().findViewById(R.id.details);
        zweiSpaltenModus = detailsFrame != null &&
                detailsFrame.getVisibility() == View.VISIBLE;
        if (savedInstanceState != null) {
            // ggf. zuletztSelektiert wiederherstellen
            zuletztSelektiert =
                    savedInstanceState.getInt(STR_ZULETZT_SELEKTIERT, 0);
        }
        if (zweiSpaltenModus) {
            // Im Zweispalten-Modus invertiert die View das
            // selektierte Element
            getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
            // Details anzeigen
            detailsAnzeigen(zuletztSelektiert);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // zuletzt selektierten Eintrag merken
        outState.putInt(STR_ZULETZT_SELEKTIERT, zuletztSelektiert);
    }

    @Override
    public void onListItemClick(ListView l, View v,
                                int position, long id) {
        detailsAnzeigen(position);
    }

    void detailsAnzeigen(int index) {
        zuletztSelektiert = index;
        if (zweiSpaltenModus) {
            // "in place"-Darstellung
            getListView().setItemChecked(index, true);
            DetailsFragment details = (DetailsFragment)
                    getFragmentManager()
                            .findFragmentById(R.id.details);
            if (details == null || details.getIndex() != index) {
                // neues Fragment passend zum selektierten
                // Eintrag erzeugen und anzeigen
                details = DetailsFragment.newInstance(index);
                FragmentTransaction ft =
                        getFragmentManager().beginTransaction();
                ft.replace(R.id.details, details);
                // einen Übergang darstellen
                ft.setTransition(
                        FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.commit();
            }
        } else {
            // neue Activity starten
            Intent intent = new Intent();
            intent.setClass(getActivity(), DetailsActivity.class);
            intent.putExtra(DetailsFragment.INDEX, index);
            startActivity(intent);
        }
    }
}