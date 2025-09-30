/**
 * @license
 * Copyright (c) 2014, 2025, Oracle and/or its affiliates.
 * Licensed under The Universal Permissive License (UPL), Version 1.0
 * as shown at https://oss.oracle.com/licenses/upl/
 * @ignore
 */
/*
 * Your Accounts ViewModel code goes here
 */
define(['../accUtils'],
 function(accUtils) {
    function AccountsViewModel() {
      this.connected = () => {
        accUtils.announce('Accounts page loaded.', 'assertive');
        document.title = "Accounts";
        // Add any custom logic you want when this page loads
      };

      this.disconnected = () => {
        // Cleanup logic if needed
      };

      this.transitionCompleted = () => {
        // Code to run after the view transition completes
      };
    }

    return AccountsViewModel;
  }
);
