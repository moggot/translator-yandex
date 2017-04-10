package com.moggot.mytranslator;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;

import com.moggot.mytranslator.adapter.AdapterLanguage;
import com.moggot.mytranslator.animation.AnimationBounce;
import com.moggot.mytranslator.animation.BackButtonAnimationBounce;

public class LanguageActivity extends AppCompatActivity {

    private static final String LOG_TAG = "LanguageActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language);

        Consts.LANG_TYPE type = Consts.LANG_TYPE.fromInteger(getIntent().getIntExtra(Consts.EXTRA_LANG, 0));

        ListView listView = (ListView) findViewById(R.id.lvLanguages);
        AdapterLanguage adapter = new AdapterLanguage(this, type);
        listView.setAdapter(adapter);
    }

    public void onClickBack(View view) {
        AnimationBounce animationBounce = new BackButtonAnimationBounce(this);
        animationBounce.animate(view);
    }
}
