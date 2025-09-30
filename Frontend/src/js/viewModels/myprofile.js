/**
 * @license
 * Copyright (c) 2014, 2025, Oracle and/or its affiliates.
 * Licensed under The Universal Permissive License (UPL), Version 1.0
 * as shown at https://oss.oracle.com/licenses/upl/
 * @ignore
 */
/*
 * Your My Profile ViewModel code goes here
 */
define(['../accUtils', ], 
 function(accUtils, Router) {
    function MyProfileViewModel(params) {
      var self = this;
      
      const { router } = params;
      self.move=()=>{
        router.go({path:"editprofile"});
      }

      this.connected = () => {
        accUtils.announce('My Profile page loaded.', 'assertive');
        document.title = "My Profile";
      };

      this.disconnected = () => {
        // Cleanup logic if needed
      };

      this.transitionCompleted = () => {
        // Code to run after the view transition completes
      };

      // 🔹 Button handlers
      this.goBack = () => {
        window.history.back();
      };

      this.goToEditPage = () => {
        // If you want to use router navigation inside OJET
        Router.rootInstance.go('editprofile');  

        // OR, if editprofile is a separate standalone HTML file:
        // window.location.href = "editprofile.html";
      };
    }

    return MyProfileViewModel;
  }
);
