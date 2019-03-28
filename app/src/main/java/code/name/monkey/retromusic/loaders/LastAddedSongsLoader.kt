/*
 * Copyright (c) 2019 Hemanth Savarala.
 *
 * Licensed under the GNU General Public License v3
 *
 * This is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by
 *  the Free Software Foundation either version 3 of the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 */

package code.name.monkey.retromusic.loaders

import android.content.Context
import android.database.Cursor
import android.provider.MediaStore
import code.name.monkey.retromusic.model.Album
import code.name.monkey.retromusic.model.Artist
import code.name.monkey.retromusic.model.Song
import code.name.monkey.retromusic.util.PreferenceUtil
import io.reactivex.Observable
import io.reactivex.annotations.NonNull
import java.util.*

/**
 * Created by hemanths on 16/08/17.
 */

object LastAddedSongsLoader {

    @NonNull
    fun getLastAddedSongs(@NonNull context: Context): Observable<ArrayList<Song>> {
        return SongLoader.getSongs(makeLastAddedCursor(context))
    }

    private fun makeLastAddedCursor(@NonNull context: Context): Cursor? {
        val cutoff = PreferenceUtil.getInstance().lastAddedCutoff

        return SongLoader.makeSongCursor(
                context,
                MediaStore.Audio.Media.DATE_ADDED + ">?",
                arrayOf(cutoff.toString()),
                MediaStore.Audio.Media.DATE_ADDED + " DESC")
    }

    @NonNull
    fun getLastAddedAlbums(@NonNull context: Context): Observable<ArrayList<Album>> {
        return AlbumLoader.splitIntoAlbums(getLastAddedSongs(context))
    }

    @NonNull
    fun getLastAddedArtists(@NonNull context: Context): Observable<ArrayList<Artist>> {
        return ArtistLoader.splitIntoArtists(getLastAddedAlbums(context))
    }
}