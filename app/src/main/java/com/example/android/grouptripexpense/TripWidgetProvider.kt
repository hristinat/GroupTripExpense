package com.example.android.grouptripexpense

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews

class TripWidgetProvider : AppWidgetProvider() {
    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        TripWidgetService.startActionUpdateTripWidget(context)
    }

    companion object {
        private fun updateAppWidget(context: Context, appWidgetManager: AppWidgetManager,
                                    appWidgetId: Int, tripName: String?, totalExpenses: Double) {
            val views = RemoteViews(context.packageName, R.layout.trip_widget)
            val intent = Intent(context, MainActivity::class.java)
            val pendingIntent = PendingIntent.getActivity(context, 0, intent, 0)
            views.setOnClickPendingIntent(R.id.widget, pendingIntent)
            views.setTextViewText(R.id.trip_name, tripName)
            views.setTextViewText(R.id.total_expenses, totalExpenses.toString())
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }

        fun updateTripWidgets(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray, tripName: String, totalExpenses: Double) {
            for (appWidgetId in appWidgetIds) {
                updateAppWidget(context, appWidgetManager, appWidgetId, tripName, totalExpenses)
            }
        }
    }
}