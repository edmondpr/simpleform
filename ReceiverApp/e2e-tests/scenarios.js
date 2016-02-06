'use strict';

/* https://github.com/angular/protractor/blob/master/docs/toc.md */

describe('my app', function() {


  it('should automatically redirect to /queueTable when location hash/fragment is empty', function() {
    browser.get('index.html');
    expect(browser.getLocationAbsUrl()).toMatch("/queueTable");
  });


  describe('queueTable', function() {

    beforeEach(function() {
      browser.get('index.html#/queueTable');
    });


    it('should render queueTable when user navigates to /queueTable', function() {
      expect(element.all(by.css('[ng-view] p')).first().getText()).
        toMatch(/partial forqueue Tabl1/);
    });

  });

});
