/*
 * Copyright (C) 2024 The Android Open Source Project
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.android.systemui.volume.panel.component.captioning.ui.viewmodel

import android.content.Context
import com.android.settingslib.view.accessibility.domain.interactor.CaptioningInteractor
import com.android.systemui.common.shared.model.Icon
import com.android.systemui.res.R
import com.android.systemui.volume.panel.component.button.ui.viewmodel.ToggleButtonViewModel
import com.android.systemui.volume.panel.dagger.scope.VolumePanelScope
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/** Volume Panel captioning UI model. */
@VolumePanelScope
class CaptioningViewModel
@Inject
constructor(
    private val context: Context,
    private val captioningInteractor: CaptioningInteractor,
    @VolumePanelScope private val coroutineScope: CoroutineScope,
) {

    val buttonViewModel: StateFlow<ToggleButtonViewModel?> =
        captioningInteractor.isSystemAudioCaptioningEnabled
            .map { isEnabled ->
                ToggleButtonViewModel(
                    isChecked = isEnabled,
                    icon =
                        Icon.Resource(
                            if (isEnabled) R.drawable.ic_volume_odi_captions
                            else R.drawable.ic_volume_odi_captions_disabled,
                            null
                        ),
                    label = context.getString(R.string.volume_panel_captioning_title),
                )
            }
            .stateIn(coroutineScope, SharingStarted.Eagerly, null)

    fun setIsSystemAudioCaptioningEnabled(enabled: Boolean) {
        coroutineScope.launch { captioningInteractor.setIsSystemAudioCaptioningEnabled(enabled) }
    }
}
