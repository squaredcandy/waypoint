package com.squaredcandy.waypoint.bottom_nav.email_details

import com.squaredcandy.waypoint.core.content.WaypointContent
import com.squaredcandy.waypoint.core.feature.MainWaypointFeature

class EmailDetailsWaypointFeature(private val emailId: String) : MainWaypointFeature {
    override fun getContent(): WaypointContent = EmailDetailsWaypointContent(emailId)
}
