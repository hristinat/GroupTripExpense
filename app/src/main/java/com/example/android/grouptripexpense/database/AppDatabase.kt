package com.example.android.grouptripexpense.database

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.android.grouptripexpense.database.ExpenseEntry
import com.example.android.grouptripexpense.database.TripEntry

@Database(entities = [TripEntry::class, MemberEntry::class, ExpenseEntry::class], version = 3, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    /**
     * Connects the database to the DAO.
     */
    abstract val tripDao: TripDao
    abstract val memberDao: MemberDao
    abstract val expenseDao: ExpenseDao

    /**
     * Define a companion object, this allows us to add functions on the AppDatabase class.
     *
     * For example, clients can call `AppDatabase.getInstance(context)` to instantiate
     * a new AppDatabase.
     */
    companion object {
        /**
         * INSTANCE will keep a reference to any database returned via getInstance.
         *
         * This will help us avoid repeatedly initializing the database, which is expensive.
         *
         *  The value of a volatile variable will never be cached, and all writes and
         *  reads will be done to and from the main memory. It means that changes made by one
         *  thread to shared data are visible to other threads.
         */
        @Volatile
        private var INSTANCE: AppDatabase? = null

        /**
         * Helper function to get the database.
         *
         * If a database has already been retrieved, the previous database will be returned.
         * Otherwise, create a new database.
         *
         * This function is threadsafe, and callers should cache the result for multiple database
         * calls to avoid overhead.
         *
         * This is an example of a simple Singleton pattern that takes another Singleton as an
         * argument in Kotlin.
         *
         * To learn more about Singleton read the wikipedia article:
         * https://en.wikipedia.org/wiki/Singleton_pattern
         *
         * @param context The application context Singleton, used to get access to the filesystem.
         */
        fun getInstance(context: Context): AppDatabase {
            // Multiple threads can ask for the database at the same time, ensure we only initialize
            // it once by using synchronized. Only one thread may enter a synchronized block at a
            // time.
            synchronized(this) {
                // Copy the current value of INSTANCE to a local variable so Kotlin can smart cast.
                // Smart cast is only available to local variables.
                var instance = INSTANCE
                // If instance is `null` make a new database instance.
                if (instance == null) {
                    instance = Room.databaseBuilder(
                            context.applicationContext,
                            AppDatabase::class.java,
                            "groupexpense"
                    )
                            // Wipes and rebuilds instead of migrating if no Migration object.
                            // Migration is not part of this lesson. You can learn more about
                            // migration with Room in this blog post:
                            // https://medium.com/androiddevelopers/understanding-migrations-with-room-f01e04b07929
                            .fallbackToDestructiveMigration()
                            .build()
                    // Assign INSTANCE to the newly created database.
                    INSTANCE = instance
                }
                // Return instance; smart cast to be non-null.
                return instance
            }
        }
    }
}

//@Database(entities = [TripEntry::class, MemberEntry::class, ExpenseEntry::class], version = 1, exportSchema = false)
//abstract class AppDatabase : RoomDatabase() {
//    abstract fun tripDao(): TripDao
//    abstract fun memberDao(): MemberDao
//    abstract fun expenseDao(): ExpenseDao
//
//    companion object {
//        private val LOG_TAG = AppDatabase::class.java.simpleName
//        private val LOCK = Any()
//        private const val DATABASE_NAME = "groupexpense"
//        private lateinit var sInstance: AppDatabase
//        fun getInstance(context: Context): AppDatabase {
//            if (!::sInstance.isInitialized) {
//                synchronized(LOCK) {
//                    Log.d(LOG_TAG, "Creating new database instance")
//                    sInstance = Room.databaseBuilder(context.applicationContext,
//                            AppDatabase::class.java, DATABASE_NAME)
//                            .fallbackToDestructiveMigration()
//                            .build()
//                }
//            }
//            Log.d(LOG_TAG, "Getting the database instance")
//            return sInstance
//        }
//    }
//}