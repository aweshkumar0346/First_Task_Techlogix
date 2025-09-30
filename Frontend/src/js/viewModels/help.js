/**
 * @license
 * Copyright (c) 2014, 2025, Oracle and/or its affiliates.
 * Licensed under The Universal Permissive License (UPL), Version 1.0
 * as shown at https://oss.oracle.com/licenses/upl/
 * @ignore
 */
/*
 * Your Help ViewModel code goes here
 */
define(['../accUtils'],
 function(accUtils) {
    function HelpViewModel() {
      // Below are a set of the ViewModel methods invoked by the oj-module component.
      // Please reference the oj-module jsDoc for additional information.

      /**
       * Invoked after the View is inserted into the DOM.
       */
      this.connected = () => {
        accUtils.announce('Help page loaded.', 'assertive');
        document.title = "Help";
        // Add any page-specific logic for Help here (e.g., fetch FAQs)
      };

      /**
       * Invoked after the View is disconnected from the DOM.
       */
      this.disconnected = () => {
        // Implement if needed
      };

      /**
       * Invoked after transition to the new View is complete.
       */
      this.transitionCompleted = () => {
        // Implement if needed
      };
    }

    /*
     * Return an instance of the ViewModel
     */
    return HelpViewModel;
  }
);
