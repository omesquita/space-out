package com.raywenderlich.android.spacingout.images

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.raywenderlich.android.spacingout.CoroutinesTestRule
import com.raywenderlich.android.spacingout.SpacingAnalytics
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import org.junit.Rule
import org.junit.Test

class ImagesViewModelTest {

    @get:Rule
    val coroutinesTestRule = CoroutinesTestRule()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Test
    fun `An analytics call is made when  images are loaded`() {
        val provider = mockk<ImageListProvider>()
        val analytics = spyk<SpacingAnalytics>()

        every { analytics.logEvent(any(), any()) } answers {
            println("You've changed the underlying log event method!")
        }

        val viewModel = ImagesViewModel(provider, analytics)

        verify(exactly = 1) { analytics.logEvent("Fetching images") }
    }
}