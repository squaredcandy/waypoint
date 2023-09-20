package com.squaredcandy.waypoint.bottom_nav.email_details

import com.squaredcandy.waypoint.core.content.WaypointContent
import com.squaredcandy.waypoint.core.feature.WaypointFeature

class EmailDetailsWaypointFeature(private val emailId: String) : WaypointFeature {
    override fun getContent(): WaypointContent = EmailDetailsWaypointContent(emailId)
}
