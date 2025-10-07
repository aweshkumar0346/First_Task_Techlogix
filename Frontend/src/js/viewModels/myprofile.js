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
define(['knockout','../accUtils', ], 
 function(ko,accUtils, Router) {
    function MyProfileViewModel(params) {
      var self = this;
      

      self.name = ko.observable("MUHAMMAD ALI KHAN");
      self.accountTitle = ko.observable("Account Title");
      self.jobStatus = ko.observable("Active Employee");
      self.dateCreationStatus = ko.observable("Best Status");
      self.ercEmployeeStatus = ko.observable("16 Sep 2023");
      self.lastLoginDetails = ko.observable("Saturday, 17 April 2021 , 11:58 AM");
      self.dateOfBirth = ko.observable("15 Feb 1992");
      self.registeredHomeAddress = ko.observable("Flat No. 54, Namira Square Apartments, Block 17K, Gulshan-e-Shamim, Karachi.");
      self.registeredContactNumber = ko.observable("0322-3749010");
      self.registeredEmailAddress = ko.observable("alikhan@gmail.com");
      self.location = ko.observable("Karachi, Pakistan");




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
