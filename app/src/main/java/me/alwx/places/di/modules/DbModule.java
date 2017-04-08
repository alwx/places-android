package me.alwx.places.di.modules;

import android.app.Application;
import android.database.sqlite.SQLiteOpenHelper;

import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import me.alwx.places.data.db.DbOpenHelper;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * @author alwx
 * @version 1.0
 */

@Module
public final class DbModule {
    private Application application;

    public DbModule(Application application) {
        this.application = application;
    }

    @Provides
    @Singleton
    SQLiteOpenHelper provideOpenHelper() {
        return new DbOpenHelper(application);
    }

    @Provides
    @Singleton
    SqlBrite provideSqlBrite() {
        return new SqlBrite.Builder()
                .logger(new SqlBrite.Logger() {
                    @Override
                    public void log(String message) {
                        Timber.tag("Database").v(message);
                    }
                })
                .build();
    }

    @Provides
    @Singleton
    BriteDatabase provideDatabase(SqlBrite sqlBrite, SQLiteOpenHelper helper) {
        BriteDatabase db = sqlBrite.wrapDatabaseHelper(helper, Schedulers.io());
        db.setLoggingEnabled(true);
        return db;
    }
}
