package com.squaredcandy.waypoint.bottom_nav.starred_emails

import com.squaredcandy.waypoint.core.content.WaypointContent
import com.squaredcandy.waypoint.core.feature.MainWaypointFeature

class StarredEmailsWaypointFeature : MainWaypointFeature {
    override fun getContent(): WaypointContent = StarredEmailsWaypointContent()
}
