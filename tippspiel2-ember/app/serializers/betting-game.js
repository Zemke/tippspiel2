import DS from 'ember-data';
import ApplicationJSONSerializer from './application';

export default ApplicationJSONSerializer.extend(DS.EmbeddedRecordsMixin, {
  attrs: {
    community: {
      deserialize: 'records'
    },
    competition: {
      deserialize: 'records'
    }
  }
});
