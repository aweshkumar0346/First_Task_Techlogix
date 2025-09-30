/**
 * @license
 * Copyright (c) 2014, 2025, Oracle and/or its affiliates.
 * Licensed under The Universal Permissive License (UPL), Version 1.0
 * as shown at https://oss.oracle.com/licenses/upl/
 * @ignore
 */
/*
 * Your Settings ViewModel code goes here
 */
define(['../accUtils'],
 function(accUtils) {
    function SettingsViewModel() {
      this.connected = () => {
        accUtils.announce('Settings page loaded.', 'assertive');
        document.title = "Settings";
        // Add any setup logic for the Settings page here
      };

      this.disconnected = () => {
        // Cleanup logic if needed
      };

      this.transitionCompleted = () => {
        // Code to run after the view transition completes
      };
    }

    return SettingsViewModel;
  }
);
