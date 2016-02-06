'use strict';

angular.module('simpleFormApp.version', [
  'simpleFormApp.version.interpolate-filter',
  'simpleFormApp.version.version-directive'
])

.value('version', '0.1');
