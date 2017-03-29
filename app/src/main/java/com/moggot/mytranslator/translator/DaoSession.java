package com.moggot.mytranslator.translator;

import java.util.Map;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import com.moggot.mytranslator.translator.Translator;

import com.moggot.mytranslator.translator.TranslatorDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see org.greenrobot.greendao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig translatorDaoConfig;

    private final TranslatorDao translatorDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        translatorDaoConfig = daoConfigMap.get(TranslatorDao.class).clone();
        translatorDaoConfig.initIdentityScope(type);

        translatorDao = new TranslatorDao(translatorDaoConfig, this);

        registerDao(Translator.class, translatorDao);
    }
    
    public void clear() {
        translatorDaoConfig.clearIdentityScope();
    }

    public TranslatorDao getTranslatorDao() {
        return translatorDao;
    }

}
