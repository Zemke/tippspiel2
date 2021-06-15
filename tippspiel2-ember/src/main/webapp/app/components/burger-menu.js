import Component from '@ember/component';

export default Component.extend({
  dataTarget: 'navMenu',
  attributeBindings: ['data-target'],
  didRender() {
    this._super(...arguments);
    const $navbarBurgers = Array.prototype.slice.call(
      document.querySelectorAll('.navbar-burger'),
      0
    );

    if ($navbarBurgers.length < 1) {
      return;
    }

    function toggle($el, $target) {
      $el.classList.toggle('is-active');
      $target.classList.toggle('is-active');
    }

    $navbarBurgers.forEach(function ($el) {
      const $target = document.getElementById($el.dataset.target);

      $el.addEventListener('click', function () {
        toggle($el, $target);
      });

      $target.addEventListener('click', function () {
        toggle($el, $target);
      });
    });
  },
});
