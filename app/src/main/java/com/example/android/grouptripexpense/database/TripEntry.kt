package com.example.android.grouptripexpense.database

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "trip")
class TripEntry (
    var name: String,
    var membersCount: Int,
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
)