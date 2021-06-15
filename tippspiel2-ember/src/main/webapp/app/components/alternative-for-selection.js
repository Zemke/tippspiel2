import Component from '@ember/component';
import { computed } from '@ember/object';
import { Promise } from 'rsvp';

export default Component.extend({
  classNames: ['select'],
  classNameBindings: ['isLoading'],
  isLoading: false,
  actions: {
    select(value) {
      const newSelected = this.options.find((o) => o.get(this.value) === value);
      const performChange = () => this.set('selected', newSelected);

      const onChange = this.onChange;

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
    },
  },
  alternatives: computed('options', 'selected', 'value', function () {
    const selected = this.selected;
    if (selected == null) return this.options;
    return this.options.filter(
      (o) => o.get(this.value) !== selected.get(this.value)
    );
  }),
});
