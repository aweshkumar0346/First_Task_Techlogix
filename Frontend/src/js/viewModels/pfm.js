/**
 * @license
 * Copyright (c) 2014, 2025, Oracle and/or its affiliates.
 * Licensed under The Universal Permissive License (UPL), Version 1.0
 * as shown at https://oss.oracle.com/licenses/upl/
 * @ignore
 */
define(['../accUtils'],
 function(accUtils) {
    function PfmViewModel() {
      this.connected = () => {
        accUtils.announce('PFM page loaded.', 'assertive');
        document.title = "PFM";
        // Add any additional logic here
      };

      this.disconnected = () => {
        // Cleanup logic if needed
      };

      this.transitionCompleted = () => {
        // Transition / animation logic if needed
      };
    }

    return PfmViewModel;
  }
);
