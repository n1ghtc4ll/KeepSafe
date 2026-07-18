package com.example.cardioproject.workout.domain.usecase

import com.example.cardioproject.workout.domain.model.TabataSetup
import com.example.cardioproject.workout.domain.model.WorkoutPhase
import com.example.cardioproject.workout.domain.model.WorkoutTimerState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.flow

class StartWorkoutTimerUseCase {
    operator fun invoke(setup: TabataSetup): Flow<WorkoutTimerState> = flow {
        val setsCount = when (setup) {
            is TabataSetup.Preset -> setup.profile.setsCount
            is TabataSetup.Custom -> setup.setsCount
        }
        val repsCount = when (setup) {
            is TabataSetup.Preset -> setup.profile.repsCount
            is TabataSetup.Custom -> setup.repsCount
        }
        val warmUpTimeSec = when (setup) {
            is TabataSetup.Preset -> setup.profile.warmUpTimeSec
            is TabataSetup.Custom -> setup.warmUpTimeSec
        }
        val workoutTimeSec = when (setup) {
            is TabataSetup.Preset -> setup.profile.workoutTimeSec
            is TabataSetup.Custom -> setup.workoutTimeSec
        }
        val relaxTimeSec = when (setup) {
            is TabataSetup.Preset -> setup.profile.relaxTimeSec
            is TabataSetup.Custom -> setup.relaxTimeSec
        }
        val breakTimeSec = when (setup) {
            is TabataSetup.Preset -> setup.profile.breakTimeSec
            is TabataSetup.Custom -> setup.breakTimeSec
        }
        val coolDownTimeSec = when (setup) {
            is TabataSetup.Preset -> setup.profile.coolDownTimeSec
            is TabataSetup.Custom -> setup.coolDownTimeSec
        }

        runPhase(WorkoutPhase.WARMUP, warmUpTimeSec, 1, 1)

        for (set in 1..setsCount) {
            for (rep in 1..repsCount) {
                runPhase(WorkoutPhase.WORKOUT, workoutTimeSec, set, rep)

                if (rep < repsCount)
                    runPhase(WorkoutPhase.BREAK, breakTimeSec, set, rep)
            }

            runPhase(WorkoutPhase.RELAX, breakTimeSec, set, repsCount)
        }

        runPhase(WorkoutPhase.COOLDOWN, coolDownTimeSec, setsCount, repsCount)

        emit(WorkoutTimerState(WorkoutPhase.FINISHED, 0, setsCount, repsCount))
    }

    private suspend fun FlowCollector<WorkoutTimerState>.runPhase(
        phase: WorkoutPhase,
        durationSec: Int,
        currentSet: Int,
        currentRep: Int
    ) {
        var timeRemaining = durationSec
        while (timeRemaining > 0) {
            emit(
                WorkoutTimerState(
                    phase,
                    timeRemaining,
                    currentSet,
                    currentRep
                )
            )

            delay(1000L)
            timeRemaining--
        }

    }
}