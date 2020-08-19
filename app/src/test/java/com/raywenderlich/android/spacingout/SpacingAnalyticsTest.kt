package com.raywenderlich.android.spacingout

import io.mockk.*
import org.junit.Assert.assertEquals
import org.junit.Test

class SpacingAnalyticsTest {
    @Test
    fun `New analytic arguments are added before events are sent along`() {
        mockkObject(ThirdPartyAnalyticsProvider)

        val slot = slot<Map<String, String>>()

        every { ThirdPartyAnalyticsProvider.logEvent(any(), capture(slot)) } just runs

        SpacingAnalytics().logEvent("Test", mapOf("attribute" to "value"))

        val expected = mapOf(
            "attribute" to "value",
            "client_type" to "Android",
            "version" to "1"
        )

        assertEquals(expected, slot.captured)
    }
}