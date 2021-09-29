package com.example.android.grouptripexpense.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface TripDao {
    @Query("SELECT * FROM trip")
    fun loadAllTripsLiveData(): LiveData<List<TripEntry>>

    @Query("SELECT * FROM trip")
    fun loadAllTrips(): List<TripEntry>

    @Insert
    fun insertTrip(tripEntry: TripEntry)

    @Query("Delete FROM trip")
    fun deleteAllTrips()
}