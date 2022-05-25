
package cc.highpurity.discord.music.command.data

import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

@OptIn(ExperimentalContracts::class)
inline fun command(
    name: String,
    description: String,
    build: ChatInputCommandDeclaration.Builder.() -> Unit = {},
): ChatInputCommandDeclaration {
    contract {
        callsInPlace(build, InvocationKind.EXACTLY_ONCE)
    }

    return ChatInputCommandDeclaration.Builder(name, description)
        .apply(build)
        .build()
}
