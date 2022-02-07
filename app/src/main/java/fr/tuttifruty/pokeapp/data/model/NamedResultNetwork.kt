package fr.tuttifruty.pokeapp.data.model

import se.ansman.kotshi.JsonSerializable

@JsonSerializable
data class NamedResultNetwork(
    val name: String?,
    val url: String?
) {
    fun getIdFromUrl(): Int? {
        // Hard to test need to use powermock, but from all the other solution, is the most understandable one
        // return url?.toUri()?.lastPathSegment?.toIntOrNull()

        // Easy to test, but it's not fool proof
        // val urlsContains = url?.split("/")
        // return urlsContains?.get(urlsContains.size-2)?.toIntOrNull()

        // East to test, works and is fool proof ! But the least understandable
        // .*/ -> look for anything up to the last backslash
        // ([^/?]+) -> find and save all following characters up to the next backslash
        //          -> the + guarantees that at least 1 char is matched to avoid empty string
        // .*       -> find all following characters
        // $1       -> retrieve the value saved previously
        //          -> we replace with regex the entire string with the saved value
        return Regex(".*/([^/?]+).*").replaceFirst(this.url!!, "$1").toIntOrNull()
    }
}
