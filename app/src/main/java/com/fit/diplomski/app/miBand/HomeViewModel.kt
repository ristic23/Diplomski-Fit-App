package com.fit.diplomski.app.miBand

import androidx.lifecycle.*
import com.fit.diplomski.app.database.EntryMiBand
import com.fit.diplomski.app.repository.MiBandRepository
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: MiBandRepository) : ViewModel() {

    var isScanRunning = false

    val allMiBandEntries: LiveData<List<EntryMiBand>> = repository.allMiBandEntries.asLiveData()

    /**
     * Launching a new coroutine to insert the data in a non-blocking way
     */
    fun insert(entryMiBand: EntryMiBand) = viewModelScope.launch {
        repository.insert(entryMiBand)
    }

}

class HomeViewModelFactory(private val repository: MiBandRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HomeViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}