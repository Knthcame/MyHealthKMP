package com.knthcame.myhealthkmp.ui.common

import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.snapshots.SnapshotStateMap

/**
 * Custom [listSaver] that maps the key value pairs of a [SnapshotStateMap] to string
 * so that it can be stored.
 *
 * @param K the type of the pair's key.
 * @param V the type of the pair's value.
 */
fun <K, V> mutableStateMapSaver() = listSaver(save = { map ->
    map.map { entry -> entry.key to entry.value }
}, restore = { list ->
    mutableStateMapOf<K, V>().apply { putAll(list.associate { it }) }
})