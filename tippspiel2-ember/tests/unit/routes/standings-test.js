import { module, test } from 'qunit';
import { setupTest } from 'ember-qunit';

module('Unit | Route | standings', function(hooks) {
  setupTest(hooks);

  test('it exists', function(assert) {
    let route = this.owner.lookup('route:standings');
    assert.ok(route);
  });
});
