package com.song.sakura.dao

import androidx.annotation.NonNull
import androidx.room.*
import com.song.sakura.entity.Word
import kotlinx.coroutines.flow.Flow

/**
 * https://developer.android.com/codelabs/android-room-with-a-view-kotlin?hl=zh_cn#5
 */
@Dao
interface WordDao {

    @Query("SELECT * FROM word_table ORDER BY word ASC")
    fun getAlphabetizedWords(): Flow<List<Word>>

    //如果所选的onConflict策略与列表中已有的单词完全相同，则会忽略该单词。要了解有关可用冲突策略的更多信息，请查阅文档。
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(word: Word)

    @Query("DELETE FROM word_table")
    suspend fun deleteAll()

    //条件删除
    @Query("DELETE FROM word_table WHERE word = :words")
    suspend fun deleteWord(words: String)

    //删某一项
    @Delete
    suspend fun deleteOne(word: Word)
}