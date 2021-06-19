package com.github.zemke.tippspiel2.persistence.model.enumeration

/**
 * [https://www.football-data.org/blog](https://www.football-data.org/blog)
 */
enum class FixtureStatus {
    SCHEDULED,
    IN_PLAY,
    FINISHED,
    POSTPONED,
    CANCELED,
    SUSPENDED,
    PAUSED,
    AWARDED;

    companion object {

        // TODO make function to distinguish from enums
        /** Betting is not permitted anymore. */
        val NO_BETTING = listOf(FixtureStatus.IN_PLAY, FixtureStatus.FINISHED, FixtureStatus.AWARDED, FixtureStatus.PAUSED)

        /** Result cannot change anymore. */
        fun done() = arrayOf(FixtureStatus.FINISHED, FixtureStatus.AWARDED)
    }
}
