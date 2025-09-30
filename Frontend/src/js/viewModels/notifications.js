/**
 * @license
 * Copyright (c) 2014, 2025, Oracle and/or its affiliates.
 * Licensed under The Universal Permissive License (UPL), Version 1.0
 * as shown at https://oss.oracle.com/licenses/upl/
 * @ignore
 */
/*
 * Your notifications ViewModel code goes here
 */
define(['../accUtils'],
 function(accUtils) {
    function NotificationsViewModel() {
      // Below are a set of the ViewModel methods invoked by the oj-module component.
      // Please reference the oj-module jsDoc for additional information.

      /**
       * Optional ViewModel method invoked after the View is inserted into the
       * document DOM. The application can put logic that requires the DOM being
       * attached here.
       */
      this.connected = () => {
        accUtils.announce('Notifications page loaded.', 'assertive');
        document.title = "Notifications";
        // Add any logic to fetch or display notifications here
      };

      /**
       * Optional ViewModel method invoked after the View is disconnected from the DOM.
       */
      this.disconnected = () => {
        // Implement if needed
      };

      /**
       * Optional ViewModel method invoked after transition to the new View is complete.
       */
      this.transitionCompleted = () => {
        // Implement if needed
      };
    }

    /*
     * Returns an instance of the ViewModel providing one instance of the ViewModel.
     * If needed, return a constructor for the ViewModel so that the ViewModel is
     * constructed each time the view is displayed.
     */
    return NotificationsViewModel;
  }
);
