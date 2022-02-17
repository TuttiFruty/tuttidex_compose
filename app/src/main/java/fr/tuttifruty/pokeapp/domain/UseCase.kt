package fr.tuttifruty.pokeapp.domain

import arrow.core.Either

interface UseCase<I : UseCase.InputValues?, O : Either<UseCase.Errors?, UseCase.OutputValues?>> {
    suspend operator fun invoke(input: I? = null): O
    interface InputValues
    interface OutputValues
    interface Errors {
        val message: String
    }
}