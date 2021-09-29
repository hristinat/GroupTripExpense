package com.example.android.grouptripexpense

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import androidx.core.app.JobIntentService
import com.example.android.grouptripexpense.TripWidgetProvider.Companion.updateTripWidgets
import com.example.android.grouptripexpense.database.AppDatabase
import com.example.android.grouptripexpense.database.ExpenseEntry
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class TripWidgetService : JobIntentService() {
    private val serviceJob = Job()
    private val serviceScope = CoroutineScope(Dispatchers.IO + serviceJob)

//    override fun onHandleIntent(intent: Intent?) {
//        if (intent != null) {
//            val action = intent.action
//            if (ACTION_TRIP_INFO == action) {
//                handleActionTripInfo(this)
//            }
//        }
//    }

    private fun handleActionTripInfo(context: Context) {
        val mDb = AppDatabase.getInstance(context)

        serviceScope.launch {
            val tripEntries = mDb.tripDao.loadAllTrips()
            val expenseEntries = mDb.expenseDao.loadAllExpenses()
            val tripName = if (tripEntries.isEmpty()) "" else tripEntries[0].name
            var expensesTotal = 0.0
            if (expenseEntries.isNotEmpty()) {
                expensesTotal = expenseEntries.stream().map { expense: ExpenseEntry -> expense.amount }.reduce { a: Double, b: Double -> a + b }.get()
            }
            val appWidgetManager = AppWidgetManager.getInstance(context)
            val appWidgetIds = appWidgetManager.getAppWidgetIds(ComponentName(context, TripWidgetProvider::class.java))
            updateTripWidgets(context, appWidgetManager, appWidgetIds, tripName, expensesTotal)
        }
    }

    companion object {
        const val ACTION_TRIP_INFO = "get_trip_info"
        fun startActionUpdateTripWidget(context: Context) {
            val intent = Intent(context, TripWidgetService::class.java)
            intent.action = ACTION_TRIP_INFO
            val FIXED_JOB_ID = 1
            enqueueWork(context, TripWidgetService::class.java, FIXED_JOB_ID, intent)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceJob.cancel()
    }

    override fun onHandleWork(intent: Intent) {
        val action = intent.action
           if (ACTION_TRIP_INFO == action) {
            handleActionTripInfo(this)
        }
    }
}