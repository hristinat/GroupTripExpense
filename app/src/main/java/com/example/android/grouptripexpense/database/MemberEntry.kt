package com.example.android.grouptripexpense.database

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "member")
class MemberEntry(
        var name: String,
        @PrimaryKey(autoGenerate = true)
        var id: Int = 0
)