package com.raywenderlich.android.spacingout.lookup

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.raywenderlich.android.spacingout.CoroutinesTestRule
import com.raywenderlich.android.spacingout.logEvent
import com.raywenderlich.android.spacingout.models.EarthImage
import com.raywenderlich.android.spacingout.network.SpacingOutApi
import io.mockk.*
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class LookupViewModelTest {

    @get:Rule
    var coroutinTesRule = CoroutinesTestRule()

    @get:Rule
    var instatTaskExecutorRule = InstantTaskExecutorRule()

    @Test
    fun `A Image is sent through to the view after coordinates are input`() {
        val mockApi = mockk<SpacingOutApi>()

        mockkObject(SpacingOutApi)

        every { SpacingOutApi.create() } returns mockApi

        coEvery { mockApi.getEarthImagery(any(), any()) } returns EarthImage("testUrl")

        val viewModel = LookupViewModel()
        viewModel.latLongInput(10f, 10f)

        assertEquals("testUrl", viewModel.imageLiveData.value)
    }

    @Test
    fun `An event is logged whenever coordinates are input`() {
        val mockApi = mockk<SpacingOutApi>()

        mockkObject(SpacingOutApi)
        mockkStatic("com.raywenderlich.android.spacingout.UtilsKt")

        every { SpacingOutApi.create() } returns mockApi

        coEvery { mockApi.getEarthImagery(any(), any()) } returns EarthImage("testUrl")

        val viewModel = LookupViewModel()
        viewModel.latLongInput(10f, 10f)
        verify(exactly = 1) {
            viewModel.logEvent("image retrieved", mapOf("latitude" to "10.0", "longitude" to "10.0"))
        }
    }

}