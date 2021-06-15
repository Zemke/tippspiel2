import Component from '@ember/component';
import { computed } from '@ember/object';

export default Component.extend({
  tagName: 'span',
  flag: computed('teamId', function () {
    const teamId = this.teamId;
    return teamId != null ? teamId : 'unknown';
  }),
});
