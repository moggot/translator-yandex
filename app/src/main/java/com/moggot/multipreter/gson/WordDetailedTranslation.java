package com.moggot.multipreter.gson;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Класс с данными тега "def" в json файле для детального перевода
 */
public class WordDetailedTranslation {

    /**
     * Список детальных данных перевода
     */
    @SerializedName("def")
    @Expose
    private List<Def> def = null;

    /**
     * Получение списка детальных данных перевода
     */
    public List<Def> getDef() {
        return def;
    }

    /**
     * Установка списка детальных данных перевода
     */
    public void setDef(List<Def> def) {
        this.def = def;
    }

}
