import Service from '@ember/service';

export default Service.extend({
  evaluate(actualHomeGoals, actualAwayGoals, betHomeGoals, betAwayGoals) {
    if (betHomeGoals == null || betAwayGoals == null) {
      return null;
    }

    if (betHomeGoals === actualHomeGoals && betAwayGoals === actualAwayGoals) {
      return 5;
    } else if (
      betHomeGoals - betAwayGoals ===
      actualHomeGoals - actualAwayGoals
    ) {
      return 3;
    } else if (
      (actualHomeGoals > actualAwayGoals && betHomeGoals > betAwayGoals) ||
      (actualHomeGoals < actualAwayGoals && betHomeGoals < betAwayGoals)
    ) {
      return 1;
    } else {
      return 0;
    }
  },
});
