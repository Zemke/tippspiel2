import { module, test } from 'qunit';
import { setupTest } from 'ember-qunit';

module('Unit | Route | competitions/new', function (hooks) {
  setupTest(hooks);

  test('it exists', function (assert) {
    let route = this.owner.lookup('route:competitions/new');
    assert.ok(route);
  });
});
