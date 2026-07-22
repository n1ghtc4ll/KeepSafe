package com.example.cardioproject.workout.domain.usecase

import com.example.cardioproject.core.common.domain.model.TabataProfile
import com.example.cardioproject.workout.domain.model.TabataSetup
import com.example.cardioproject.workout.domain.model.WorkoutPhase
import com.example.cardioproject.workout.domain.model.WorkoutTimerState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.flow

class StartWorkoutTimerUseCase {
    operator fun invoke(setup: TabataSetup): Flow<WorkoutTimerState> = flow {
        var totalElapsedTime = 0
        val setsCount: Int
        val repsCount: Int
        val warmUpTimeSec: Int
        val workoutTimeSec: Int
        val relaxTimeSec: Int
        val breakTimeSec: Int
        val coolDownTimeSec: Int

        when (setup) {
            is TabataSetup.Preset -> {
                setsCount = setup.profile.setsCount
                repsCount = setup.profile.repsCount
                warmUpTimeSec = setup.profile.warmUpTimeSec
                workoutTimeSec = setup.profile.workoutTimeSec
                relaxTimeSec = setup.profile.relaxTimeSec
                breakTimeSec = setup.profile.breakTimeSec
                coolDownTimeSec = setup.profile.coolDownTimeSec
            }
            is TabataSetup.Custom -> {
                setsCount = setup.setsCount
                repsCount = setup.repsCount
                warmUpTimeSec = setup.warmUpTimeSec
                workoutTimeSec = setup.workoutTimeSec
                relaxTimeSec = setup.relaxTimeSec
                breakTimeSec = setup.breakTimeSec
                coolDownTimeSec = setup.coolDownTimeSec
            }
        }

        totalElapsedTime = runPhase(WorkoutPhase.WARMUP, warmUpTimeSec, 1, 1, totalElapsedTime)

        for (set in 1..setsCount) {
            for (rep in 1..repsCount) {
                totalElapsedTime = runPhase(WorkoutPhase.WORKOUT, workoutTimeSec, set, rep, totalElapsedTime)

                if (rep < repsCount)
                    totalElapsedTime = runPhase(WorkoutPhase.BREAK, breakTimeSec, set, rep, totalElapsedTime)
            }
            totalElapsedTime = runPhase(WorkoutPhase.RELAX, relaxTimeSec, set, repsCount, totalElapsedTime)
        }

        totalElapsedTime = runPhase(WorkoutPhase.COOLDOWN, coolDownTimeSec, setsCount, repsCount, totalElapsedTime)
        emit(WorkoutTimerState(WorkoutPhase.FINISHED, 0, setsCount, repsCount, totalElapsedTime))
    }

    private suspend fun FlowCollector<WorkoutTimerState>.runPhase(
        phase: WorkoutPhase,
        durationSec: Int,
        currentSet: Int,
        currentRep: Int,
        startElapsedTime: Int
    ): Int {
        var currentTotal = startElapsedTime
        var timeRemaining = durationSec

        while (timeRemaining > 0) {
            emit(
                WorkoutTimerState(phase,
                    timeRemaining,
                    currentSet,
                    currentRep,
                    currentTotal
                )
            )
            delay(1000L)
            timeRemaining--
            currentTotal++
        }

        return currentTotal
    }
}