package com.squaredcandy.waypoint.bottom_nav.emails

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.time.ZonedDateTime
import java.util.UUID
import kotlin.random.Random

val LocalEmailRepository = compositionLocalOf<EmailRepository> { error("Missing EmailRepository") }

class EmailRepository(initialEmailList: ImmutableList<Email> = persistentListOf()) {

    private val mutableEmailListStateFlow: MutableStateFlow<ImmutableList<Email>> = MutableStateFlow(initialEmailList)
    val emailListStateFlow: StateFlow<ImmutableList<Email>> = mutableEmailListStateFlow.asStateFlow()

    fun updateEmailStarred(id: String) {
        mutableEmailListStateFlow.update { emailList ->
            emailList.map { if (it.id == id) it.copy(starred = !it.starred) else it }.toPersistentList()
        }
    }

    fun addNewEmail() {
        mutableEmailListStateFlow.update {
            val newEmail = Email(
                id = UUID.randomUUID().toString(),
                from = "example@email.com",
                to = "waypoint@email.com",
                title = LoremIpsum(Random.nextInt(5, 50)).values.first(),
                message = LoremIpsum(Random.nextInt(5, 50)).values.first(),
                starred = false,
                date = ZonedDateTime.now(),
            )
            persistentListOf(*it.toTypedArray(), newEmail)
        }
    }

    companion object {
        val saver = listSaver(
            save = { it.emailListStateFlow.value },
            restore = { EmailRepository(it.toPersistentList()) },
        )
    }
}
