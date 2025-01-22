package dev.shtanko.androidlab.github.data.model

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class NetworkMapperTest {
    @Test
    fun `test NetworkUser to UserEntity mapping`() {
        val networkUser = NetworkUser(
            id = 1,
            login = "test_login",
            nodeId = "test_node_id",
            avatarUrl = "test_avatar_url",
            gravatarId = "test_gravatar_id",
            url = "test_url",
            htmlUrl = "test_html_url",
            followersUrl = "test_followers_url",
            followingUrl = "test_following_url",
            gistsUrl = "test_gists_url",
            starredUrl = "test_starred_url",
            subscriptionsUrl = "test_subscriptions_url",
            organizationsUrl = "test_organizations_url",
            reposUrl = "test_repos_url",
            eventsUrl = "test_events_url",
            receivedEventsUrl = "test_received_events_url",
            type = "test_type",
            userViewType = "test_user_view_type",
            siteAdmin = true,
        )

        val userEntity = networkUser.asEntity()

        assertEquals(networkUser.id, userEntity.id)
        assertEquals(networkUser.login, userEntity.login)
        assertEquals(networkUser.nodeId, userEntity.nodeId)
        assertEquals(networkUser.avatarUrl, userEntity.avatarUrl)
        assertEquals(networkUser.gravatarId, userEntity.gravatarId)
        assertEquals(networkUser.url, userEntity.url)
        assertEquals(networkUser.htmlUrl, userEntity.htmlUrl)
        assertEquals(networkUser.followersUrl, userEntity.followersUrl)
        assertEquals(networkUser.followingUrl, userEntity.followingUrl)
        assertEquals(networkUser.gistsUrl, userEntity.gistsUrl)
        assertEquals(networkUser.starredUrl, userEntity.starredUrl)
        assertEquals(networkUser.subscriptionsUrl, userEntity.subscriptionsUrl)
        assertEquals(networkUser.organizationsUrl, userEntity.organizationsUrl)
        assertEquals(networkUser.reposUrl, userEntity.reposUrl)
        assertEquals(networkUser.eventsUrl, userEntity.eventsUrl)
        assertEquals(networkUser.receivedEventsUrl, userEntity.receivedEventsUrl)
        assertEquals(networkUser.type, userEntity.type)
        assertEquals(networkUser.userViewType, userEntity.userViewType)
        assertEquals(networkUser.siteAdmin, userEntity.siteAdmin)
    }
}
