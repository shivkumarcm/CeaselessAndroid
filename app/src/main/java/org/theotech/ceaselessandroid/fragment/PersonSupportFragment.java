package org.theotech.ceaselessandroid.fragment;


import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.joanzapata.iconify.widget.IconTextView;
import com.makeramen.roundedimageview.RoundedImageView;

import org.theotech.ceaselessandroid.R;
import org.theotech.ceaselessandroid.cache.CacheManager;
import org.theotech.ceaselessandroid.cache.LocalDailyCacheManagerImpl;
import org.theotech.ceaselessandroid.person.PersonManager;
import org.theotech.ceaselessandroid.person.PersonManagerImpl;
import org.theotech.ceaselessandroid.util.CommonUtils;
import org.theotech.ceaselessandroid.util.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PersonSupportFragment extends Fragment implements ICardPageFragment {
    private static final String TAG = PersonSupportFragment.class.getSimpleName();

    @BindView(R.id.person_name)
    TextView personName;
    @BindView(R.id.person_image)
    RoundedImageView personImage;
    @BindView(R.id.person_notes_list)
    ListView notes;
    @BindView(R.id.note_btn)
    IconTextView noteButton;

    private CacheManager cacheManager;
    private PersonManager personManager;

    public PersonSupportFragment() {
        // Required empty public constructor
    }

    public static PersonSupportFragment newInstance(String personId) {
        PersonSupportFragment fragment = new PersonSupportFragment();
        Bundle args = new Bundle();
        args.putString(Constants.PERSON_ID_BUNDLE_ARG, personId);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        cacheManager = LocalDailyCacheManagerImpl.getInstance(getActivity());
        personManager = PersonManagerImpl.getInstance(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // create view and bind
        View view = inflater.inflate(R.layout.fragment_person, container, false);
        ButterKnife.bind(this, view);

        // display person
        Bundle bundle = getArguments();
        if (bundle != null && bundle.containsKey(Constants.PERSON_ID_BUNDLE_ARG) && bundle.containsKey(Constants.HOME_SECTION_NUMBER_BUNDLE_ARG)) {
            final String personId = bundle.getString(Constants.PERSON_ID_BUNDLE_ARG);
            int homeIndex = bundle.getInt(Constants.HOME_SECTION_NUMBER_BUNDLE_ARG);
            final FragmentState backStackInfo = new FragmentState(getString(R.string.nav_home));
            Bundle currentState = new Bundle();
            currentState.putInt(Constants.HOME_SECTION_NUMBER_BUNDLE_ARG, homeIndex);
            backStackInfo.setState(currentState);

            CommonUtils.injectPersonIntoView(getActivity(), personManager, personName, personImage,
                    notes, view, personId, getString(R.string.empty_notes), getActivity().getSupportFragmentManager(),
                    backStackInfo);

            CommonUtils.wireAddNote(noteButton, personId, getActivity(), backStackInfo);
            CommonUtils.wireFavoriteShortcut(getActivity(), view, personId, personManager);
            CommonUtils.wireSendMessage(getActivity(), view, personId);
            CommonUtils.wireInvitePerson(getActivity(), view, personId);
            CommonUtils.wireDeletePerson(getActivity(), view, personId, personManager);
            CommonUtils.wireShowPersonMenu(view, personId, getActivity(), backStackInfo, personManager);
        }

        return view;
    }

    public String getCardName() {
        return "PersonSupportCard";
    }
}
