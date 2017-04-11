package com.moggot.mytranslator.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.TextView;

import com.moggot.mytranslator.BackAwareEditText;
import com.moggot.mytranslator.Consts;
import com.moggot.mytranslator.DataBase;
import com.moggot.mytranslator.LangSharedPreferences;
import com.moggot.mytranslator.LanguageActivity;
import com.moggot.mytranslator.MainActivity;
import com.moggot.mytranslator.R;
import com.moggot.mytranslator.State;
import com.moggot.mytranslator.TranslationOff;
import com.moggot.mytranslator.TranslationOn;
import com.moggot.mytranslator.TranslatorContext;
import com.moggot.mytranslator.animation.AnimationBounce;
import com.moggot.mytranslator.animation.ClearButtonAnimationBounce;
import com.moggot.mytranslator.observer.Display;
import com.moggot.mytranslator.observer.HistoryDisplay;
import com.moggot.mytranslator.observer.TranslationDisplay;
import com.moggot.mytranslator.observer.TranslatorData;
import com.moggot.mytranslator.translator.Translator;

/**
 * Created by toor on 10.04.17.
 */
public class RootFragment extends Fragment implements HistoryFragment.HistoryEventListener
        , TranslatorFragment.TranslatorEventListener
        , FavoritesFragment.FavoritesEventListener {

    private static final String LOG_TAG = "RootFragment";

    private Translator translator;
    private BackAwareEditText etText;
    private TranslatorContext translatorContext;
    private DataBase db;

    public RootFragment() {
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public static Fragment newInstance() {
        return new RootFragment();
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        db = new DataBase(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_root, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.v(LOG_TAG, "onViewCreated");

        createTranslator();

        State stateOff = new TranslationOff(this);
        translatorContext.setState(stateOff);
        translatorContext.show();

        etText = (BackAwareEditText) view.findViewById(R.id.etText);
        etText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int start,
                                      int lengthBefore,
                                      int lengthAfter) {
                if (etText.getText().toString().isEmpty()) {
                    State stateOff = new TranslationOff(RootFragment.this);
                    translatorContext.setState(stateOff);
                    translatorContext.show();
                    return;
                }

                if (translatorContext.getState() instanceof TranslationOff) {
                    State stateOn = new TranslationOn(RootFragment.this, translator.getIsFavorites());
                    translatorContext.setState(stateOn);
                }
                resetTranslator();
                translator.setText(cs.toString());
                translatorContext.show();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable str) {
            }
        });

        etText.setOnEditorActionListener(new TextView.OnEditorActionListener()

        {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    saveOrEditRecord();
                }
                return false;
            }
        });

        etText.setBackPressedListener(new BackAwareEditText.BackPressedListener()

        {
            @Override
            public void onImeBack(BackAwareEditText editText) {
                saveOrEditRecord();
            }
        });

        etText.setOnFocusChangeListener(new View.OnFocusChangeListener()

        {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    etText.setSelection(etText.getText().length());
                }
            }
        });

        Button btnChangeLang = (Button) view.findViewById(R.id.btnChangeLang);
        btnChangeLang.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Animation bounce = AnimationUtils.loadAnimation(getContext(), R.anim.change_lang);
                view.startAnimation(bounce);

                resetTranslator();
                String inputLang = LangSharedPreferences.loadInputLanguage(getContext());
                String outputLang = LangSharedPreferences.loadOutputLanguage(getContext());
                LangSharedPreferences.saveInputLanguage(getContext(), outputLang);
                LangSharedPreferences.saveOutputLanguage(getContext(), inputLang);

                inputLang = LangSharedPreferences.loadInputLanguage(getContext());
                outputLang = LangSharedPreferences.loadOutputLanguage(getContext());

                translator.setInputLanguage(inputLang);
                translator.setOutputLanguage(outputLang);
                translatorContext.show();
            }
        });

        Button btnClearText = (Button) view.findViewById(R.id.btnClearText);
        btnClearText.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                saveOrEditRecord();
                AnimationBounce animation = new ClearButtonAnimationBounce(getContext());
                animation.animate(view);
            }
        });

        TextView tvInputLang = (TextView) view.findViewById(R.id.tvInputLang);
        tvInputLang.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), LanguageActivity.class);
                intent.putExtra(Consts.EXTRA_LANG, Consts.LANG_TYPE.INPUT.getType());
                startActivityForResult(intent, Consts.REQUEST_CODE_ACTIVITY_LANGUAGE);
                saveOrEditRecord();
            }
        });

        TextView tvOutputLang = (TextView) view.findViewById(R.id.tvOutputLang);
        tvOutputLang.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), LanguageActivity.class);
                intent.putExtra(Consts.EXTRA_LANG, Consts.LANG_TYPE.OUTPUT.getType());
                startActivityForResult(intent, Consts.REQUEST_CODE_ACTIVITY_LANGUAGE);
                saveOrEditRecord();
            }
        });
    }

    private void createTranslator() {
        String inputLanguage = LangSharedPreferences.loadInputLanguage(getContext());
        String outputLanguage = LangSharedPreferences.loadOutputLanguage(getContext());
        translator = new Translator(null
                , ""
                , ""
                , inputLanguage
                , outputLanguage
                , false
                , "");

        translatorContext = new TranslatorContext(getActivity(), translator);
    }

    private void saveOrEditRecord() {
        if (translator.getText().isEmpty() || translator.getTranslation().isEmpty())
            return;
        if (db.findRecord(translator) != null)
            db.editRecord(translator);
        else
            db.addRecord(translator);
    }

    private void resetTranslator() {
        translator.setId(null);
        translator.setText(etText.getText().toString());
        translator.setTranslation("");
        translator.setInputLanguage(LangSharedPreferences.loadInputLanguage(getContext()));
        translator.setOutputLanguage(LangSharedPreferences.loadOutputLanguage(getContext()));
        translator.setIsFavorites(false);
        translator.setDetails("");
    }

    public Translator getTranslator() {
        return translator;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        try {
            if (isVisibleToUser) {
                FragmentManager fragmentManager = getChildFragmentManager();
                if (fragmentManager == null)
                    return;
                Fragment fragment = fragmentManager.findFragmentByTag(Consts.TAG_FRAGMENT_HISTORY);
                if (fragment == null)
                    return;
                TranslatorData translatorData = new TranslatorData();
                Display display = new HistoryDisplay(fragment, translatorData);
                translatorData.setTranslator(translator);
                display.display();
            }
        } catch (IllegalStateException e) {
        }
    }

    public void loadHistoryTranslator(Translator loadedTranslator) {
        translator.setTranslator(loadedTranslator);
        etText.setText(translator.getText());
        etText.setSelection(etText.getText().length());
    }

    public void loadFavoriteTranslator(Translator loadedTranslator) {
        translator.setTranslator(loadedTranslator);
        etText.setText(translator.getText());
        etText.setSelection(etText.getText().length());

        ((MainActivity) getActivity()).getViewPager().setCurrentItem(0);
    }

    public void setFavorites(boolean isFavorites) {
        translator.setIsFavorites(isFavorites);
        TranslatorData translatorData = new TranslatorData();
        Fragment fragment = getChildFragmentManager().findFragmentByTag(Consts.TAG_FRAGMENT_TRANSLATOR);
        Display display = new TranslationDisplay(fragment, translatorData);
        translatorData.setTranslator(translator);
        display.display();
        saveOrEditRecord();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case Consts.REQUEST_CODE_ACTIVITY_LANGUAGE:
                resetTranslator();
                String inputLang = LangSharedPreferences.loadInputLanguage(getContext());
                String outputLang = LangSharedPreferences.loadOutputLanguage(getContext());
                translator.setInputLanguage(inputLang);
                translator.setOutputLanguage(outputLang);

                translatorContext.show();
                break;
        }
    }
}
