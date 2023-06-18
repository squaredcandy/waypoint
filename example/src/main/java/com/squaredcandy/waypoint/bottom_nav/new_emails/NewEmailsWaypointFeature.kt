package com.squaredcandy.waypoint.bottom_nav.new_emails

import com.squaredcandy.waypoint.core.content.WaypointContent
import com.squaredcandy.waypoint.core.feature.MainWaypointFeature

class NewEmailsWaypointFeature : MainWaypointFeature {
    override fun getContent(): WaypointContent = NewEmailsWaypointContent()
}
