package com.moggot.mytranslator.translator;

import org.greenrobot.greendao.annotation.*;

// THIS CODE IS GENERATED BY greenDAO, EDIT ONLY INSIDE THE "KEEP"-SECTIONS

// KEEP INCLUDES - put your custom includes here
// KEEP INCLUDES END

/**
 * Entity mapped to table "TRANSLATOR".
 */
@Entity
public class Translator {

    @Id(autoincrement = true)
    private Long id;

    @NotNull
    private String text;

    @NotNull
    private String translation;

    @NotNull
    private String inputLanguage;

    @NotNull
    private String outputLanguage;
    private boolean isFavorites;
    private String details;

    // KEEP FIELDS - put your custom fields here
    // KEEP FIELDS END

    private static class TranslatorHolder {
        private final static Translator instance = new Translator();
    }

    public static Translator getInstance() {
        return TranslatorHolder.instance;
    }

    @Generated
    private Translator() {
    }

    private Translator(Long id) {
        this.id = id;
    }

    @Generated
    public Translator(Long id, String text, String translation, String inputLanguage, String outputLanguage, boolean isFavorites, String details) {
        this.id = id;
        this.text = text;
        this.translation = translation;
        this.inputLanguage = inputLanguage;
        this.outputLanguage = outputLanguage;
        this.isFavorites = isFavorites;
        this.details = details;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @NotNull
    public String getText() {
        return text;
    }

    /**
     * Not-null value; ensure this value is available before it is saved to the database.
     */
    public void setText(@NotNull String text) {
        this.text = text;
    }

    @NotNull
    public String getTranslation() {
        return translation;
    }

    /**
     * Not-null value; ensure this value is available before it is saved to the database.
     */
    public void setTranslation(@NotNull String translation) {
        this.translation = translation;
    }

    @NotNull
    public String getInputLanguage() {
        return inputLanguage;
    }

    /**
     * Not-null value; ensure this value is available before it is saved to the database.
     */
    public void setInputLanguage(@NotNull String inputLanguage) {
        this.inputLanguage = inputLanguage;
    }

    @NotNull
    public String getOutputLanguage() {
        return outputLanguage;
    }

    /**
     * Not-null value; ensure this value is available before it is saved to the database.
     */
    public void setOutputLanguage(@NotNull String outputLanguage) {
        this.outputLanguage = outputLanguage;
    }

    public boolean getIsFavorites() {
        return isFavorites;
    }

    public void setIsFavorites(boolean isFavorites) {
        this.isFavorites = isFavorites;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    // KEEP METHODS - put your custom methods here
    // KEEP METHODS END

}
