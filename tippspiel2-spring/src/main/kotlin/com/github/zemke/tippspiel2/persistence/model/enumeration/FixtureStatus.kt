package com.github.zemke.tippspiel2.persistence.model.enumeration

/**
 * [https://www.football-data.org/blog](https://www.football-data.org/blog)
 */
enum class FixtureStatus {
    SCHEDULED,
    TIMED,
    IN_PLAY,
    FINISHED,
    POSTPONED,
    CANCELED;

    companion object {

        /** Betting is not permitted anymore. */
        val NO_BETTING = listOf(FixtureStatus.IN_PLAY, FixtureStatus.FINISHED)

        /** For persistence often times only a few statuses are of interest. */
        val OF_INTEREST = listOf(FixtureStatus.TIMED, FixtureStatus.IN_PLAY, FixtureStatus.FINISHED)
    }
}
