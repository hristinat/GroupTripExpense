package com.example.android.grouptripexpense.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface MemberDao {
    @Query("SELECT * FROM member")
    fun loadAllMembersLiveData(): LiveData<List<MemberEntry>>

    @Query("SELECT * FROM member")
    fun loadAllMembers(): List<MemberEntry>

    @Insert
    fun insertAllMembers(members: List<MemberEntry>)

    @Query("Delete FROM member")
    fun deleteAllMembers()
}