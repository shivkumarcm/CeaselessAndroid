package org.theotech.ceaselessandroid.fragment;


import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.theotech.ceaselessandroid.R;
import org.theotech.ceaselessandroid.cache.CacheManager;
import org.theotech.ceaselessandroid.cache.LocalCacheData;
import org.theotech.ceaselessandroid.cache.LocalDailyCacheManagerImpl;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class VerseFragment extends Fragment {
    private static final String TAG = VerseFragment.class.getSimpleName();

    @Bind(R.id.verse_view_title)
    TextView verseViewTitle;
    @Bind(R.id.verse_view_text)
    TextView verseViewText;
    @Bind(R.id.verse_share_button)
    Button verseShareButton;

    private CacheManager<LocalCacheData> cacheManager = null;

    public VerseFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cacheManager = LocalDailyCacheManagerImpl.getInstance(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setTitle(getString(R.string.nav_verse));
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_verse, container, false);
        ButterKnife.bind(this, view);

        final LocalCacheData cacheData = cacheManager.getCacheData();
        verseViewTitle.setText(cacheData.getScriptureCitation());
        verseViewText.setText(cacheData.getScriptureText());

        View.OnClickListener shareListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cacheData != null &&
                        cacheData.getScriptureText() != null &&
                        !cacheData.getScriptureText().isEmpty() &&
                        cacheData.getScriptureCitation() != null &&
                        !cacheData.getScriptureCitation().isEmpty()) {
                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT, cacheData.getScriptureCitation() + "\n" + cacheData.getScriptureText());
                    sendIntent.setType("text/plain");
                    startActivity(Intent.createChooser(sendIntent, getResources().getText(R.string.verse_share)));
                }
            }
        };
        verseShareButton.setOnClickListener(shareListener);

        return view;
    }


}
