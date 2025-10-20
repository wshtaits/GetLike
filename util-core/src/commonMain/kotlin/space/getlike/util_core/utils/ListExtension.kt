package space.getlike.util_core.utils

val <A, B> List<Pair<A, B>>.firstsAndSeconds: Pair<List<A>, List<B>>
    get() = map { pair -> pair.first } to map { pair -> pair.second }