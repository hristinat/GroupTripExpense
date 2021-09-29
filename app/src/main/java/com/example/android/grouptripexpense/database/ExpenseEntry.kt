package com.example.android.grouptripexpense.database

import androidx.room.*

// TODO: check why we need indices
@Entity(tableName = "expense", foreignKeys = [ForeignKey(entity = MemberEntry::class, parentColumns = arrayOf("id"), childColumns = arrayOf("memberId"))],
        indices = [Index(value = arrayOf("memberId"))])
class ExpenseEntry(
        var type: String,
        var amount: Double,
        var memberId: Int,
        @PrimaryKey(autoGenerate = true)
        var id: Int = 0
)
