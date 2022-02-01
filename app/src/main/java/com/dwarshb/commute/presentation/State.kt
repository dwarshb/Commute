package com.dwarshb.commute.presentation

/**
 *
 * Created at December 24, 2021
 *
 * @author Darshan Bhanushali
 */
sealed class State<out T> {
    object LoadingState : State<Nothing>()
    data class ErrorState(var exception: Throwable) : State<Nothing>()
    data class DataState<T>(var data: T) : State<T>()
}