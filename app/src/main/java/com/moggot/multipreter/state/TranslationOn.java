package com.moggot.multipreter.state;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.moggot.multipreter.Consts;
import com.moggot.multipreter.DataBase;
import com.moggot.multipreter.translation.TranslateManager;
import com.moggot.multipreter.R;
import com.moggot.multipreter.fragments.TranslatorFragment;
import com.moggot.multipreter.observer.Display;
import com.moggot.multipreter.observer.TranslationDisplay;
import com.moggot.multipreter.observer.TranslatorData;
import com.moggot.multipreter.translation.Translation;
import com.moggot.multipreter.translator.Translator;

/**
 * Класс состояния On (включенного переводчика)
 */
public class TranslationOn extends State {

    private static final String LOG_TAG = Translation.class.getSimpleName();

    /**
     * Фрагмент с переводом слова
     */
    private Fragment fragment;

    /**
     * Конструктор
     *
     * @param parentFragment - родительский фрагмент
     */
    public TranslationOn(Fragment parentFragment) {
        super(parentFragment);

        fragment = parentFragment.getChildFragmentManager().findFragmentByTag(Consts.TAG_FRAGMENT_TRANSLATOR);
        if (fragment == null) {
            FragmentTransaction ft = parentFragment.getChildFragmentManager().beginTransaction();
            fragment = TranslatorFragment.newInstance();
            ft.replace(R.id.root_frame, fragment, Consts.TAG_FRAGMENT_TRANSLATOR);
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            ft.commit();

            parentFragment.getChildFragmentManager().executePendingTransactions();
        }
    }

    /**
     * Отображение данных при показе экрана перевода, когда транслятор включен
     *
     * @param translator - родительский фрагмент
     */
    public void show(Translator translator) {
        super.show(translator);

        DataBase db = new DataBase(parentFragment.getContext());
        Translator foundRecord = db.findRecord(translator);

        if (foundRecord == null) {
            TranslateManager translateManager = new TranslateManager(parentFragment);
            translateManager.translate(translator);
        } else {
            translator.setTranslator(foundRecord);
            TranslatorData translatorData = new TranslatorData();
            Display translationDisplay = new TranslationDisplay(fragment, translatorData);
            translatorData.setTranslator(translator);
            translationDisplay.display();
        }
    }
}