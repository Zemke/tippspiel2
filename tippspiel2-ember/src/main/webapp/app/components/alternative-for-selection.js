import Component from '@ember/component';
import {computed} from "@ember/object";
import {Promise} from "rsvp";

export default Component.extend({
  classNames: ['select'],
  classNameBindings: ['isLoading'],
  isLoading: false,
  actions: {
    select(value) {
      const newSelected = this.get('options').find(o => o.get(this.get('value')) === value);
      const performChange = () => this.set('selected', newSelected);

      const onChange = this.get('onChange');

      if (onChange == null) return performChange();

      this.set('isLoading', true);
      this.set('disabled', true);

      Promise.resolve(onChange(newSelected))
        .then(() => {
          performChange();
        })
        .finally(() => {
          this.set('isLoading', false);
          this.set('disabled', false);
        });
    }
  },
  alternatives: computed('options', 'selected', function () {
    const selected = this.get('selected');
    if (selected == null) return this.get('options');
    return this.get('options')
      .filter(o => o.get(this.get('value')) !== selected.get(this.get('value')));
  })
});
