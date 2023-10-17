package com.example.cursobasico.settings

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.cursobasico.databinding.ActivitySettingsBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding
    private var firstTimeFlow:Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initUI()
        initListeners()
    }

    private fun initListeners() {
        binding.rsVolume.addOnChangeListener { _, value, _ ->
            CoroutineScope(Dispatchers.IO).launch {
                saveVolumne(value.toInt())
            }
        }

        binding.switchDarkMode.setOnCheckedChangeListener { _, state ->
            enableDarkMode(state)
            CoroutineScope(Dispatchers.IO).launch {
                saveOption(DARKMODE, state)
            }
        }

        binding.switchBluetooth.setOnCheckedChangeListener { _, state ->
            CoroutineScope(Dispatchers.IO).launch {
                saveOption(BLUETOOTH, state)
            }
        }

        binding.switchVibration.setOnCheckedChangeListener { _, state ->
            CoroutineScope(Dispatchers.IO).launch {
                saveOption(VIBRATION, state)
            }
        }

        binding.btnTitle.setOnClickListener {
            supportActionBar?.title = "Settings App"
            supportActionBar?.subtitle = "Subtitulo"
        }
    }

    private fun enableDarkMode(state: Boolean) {
        AppCompatDelegate.setDefaultNightMode(if(state) MODE_NIGHT_YES else MODE_NIGHT_NO)
        delegate.applyDayNight()
    }

    private fun initUI() {
        CoroutineScope(Dispatchers.IO).launch {
            getSettings()
                .filter { firstTimeFlow }
                .collect{ model ->
                runOnUiThread {
                    binding.rsVolume.setValues(model.volume.toFloat())
                    binding.switchDarkMode.isChecked = model.darkMode
                    binding.switchBluetooth.isChecked = model.bluetooth
                    binding.switchVibration.isChecked = model.vibration
                    firstTimeFlow = false
                    enableDarkMode(model.darkMode)
                }
            }
        }
    }

    private suspend fun saveVolumne(value: Int) {
        dataStore.edit { preferences ->
            preferences[intPreferencesKey(VOLUME_LEVEL)] = value
        }
    }

    private suspend fun saveOption(key: String, value: Boolean) {
        dataStore.edit { preferences ->
            preferences[booleanPreferencesKey(key)] = value
        }
    }

    private fun getSettings(): Flow<SettingsModel> {
        return dataStore.data.map { preferences ->
            SettingsModel(
                volume = preferences[intPreferencesKey(VOLUME_LEVEL)] ?: 50,
                darkMode = preferences[booleanPreferencesKey(DARKMODE)] ?: false,
                bluetooth = preferences[booleanPreferencesKey(BLUETOOTH)] ?: false,
                vibration = preferences[booleanPreferencesKey(VIBRATION)] ?: true
            )
        }
    }

    companion object {
        const val VOLUME_LEVEL = "volume_level"
        const val DARKMODE = "darkmode"
        const val VIBRATION = "vibration"
        const val BLUETOOTH = "bluetooth"
    }
}