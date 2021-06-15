import DS from 'ember-data';
import ApplicationJSONSerializer from './application';

export default ApplicationJSONSerializer.extend(DS.EmbeddedRecordsMixin, {
  attrs: {
    user: {
      deserialize: 'records',
    },
    bettingGame: {
      deserialize: 'records',
    },
  },
});
