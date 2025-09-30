/**
 * @license
 * Copyright (c) 2014, 2025, Oracle and/or its affiliates.
 * Licensed under The Universal Permissive License (UPL), Version 1.0
 * as shown at https://oss.oracle.com/licenses/upl/
 * @ignore
 */
/*
 * Your customer ViewModel code goes here
 */
define(['knockout', '../accUtils'],
 function(ko,accUtils, Router) {
    function EditPageViewModel(params) {
      
      // Below are a set of the ViewModel methods invoked by the oj-module component.
      // Please reference the oj-module jsDoc for additional information.
      
      var self = this;
      const { router } = params;

      self.goBack = () => {
        router.go({path:"myprofile"});
      }

      self.goNext = () => {
        router.go({path:"otpscreen"});
      }

    


    // Contact Number Setup
    self.contactNumber = ko.observable("");

// Subscribe: auto-clean + format
self.contactNumber.subscribe(function(newValue) {
  // If contains any letters, keep raw input (don’t clean yet)
  if (/[a-zA-Z]/.test(newValue)) {
    // Don’t overwrite here, let computed handle error message
    return;
  }

  var raw = newValue.replace(/\D/g, ""); // keep only digits
  if (raw.length > 0) {
    if (raw.length <= 4) {
      self.contactNumber(raw);
    } else {
      self.contactNumber(raw.replace(/(\d{4})(\d{0,7})/, "$1-$2"));
    }
  }
});

// Validation: digits only + exactly 11 digits
self.contactError = ko.computed(function() {
  var value = self.contactNumber();
  
  // 1. Check for letters
  if (/[a-zA-Z]/.test(value)) {
    return "Invalid number: letters not allowed";
  }

  // 2. Only digits check
  var raw = value.replace(/\D/g, "");
  if (raw.length === 0) {
    return ""; // no error yet
  }
  if (raw.length > 11) {
    return "Too many digits! Must be 11 digits";
  }
  return ""; // valid
});


  // Email Setup
    self.email = ko.observable("");

// Error validation
self.emailError = ko.computed(function() {
    var value = self.email().trim();
    
    if (value.length === 0) {
        return ""; // empty input = no error
    }

    // Basic email regex
    var emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!emailPattern.test(value)) {
        return "Invalid email format";
    }

    return ""; // valid email
});

    // Address Setup
    self.address = ko.observable("");

// Validation: must be between 25 and 30 characters
self.addressError = ko.computed(function() {
    var value = self.address().trim();

    if (value.length === 0) {
        return ""; // empty input → no error yet
    }
    if (value.length < 25 || value.length > 30) {
        return "Address should have 25-30 characters maximum";
    }
    return ""; // valid
});

      // Country and City Setup

      self.selectedCountry = ko.observable();
self.selectedCity = ko.observable();

// JSON data
self.countries = ["Pakistan", "USA", "UK", "Canada", "Australia"];
self.citiesData = {
  "Pakistan": ["Karachi", "Lahore", "Islamabad", "Multan", "Peshawar"],
  "USA": ["New York", "Los Angeles", "Chicago", "Houston", "San Francisco"],
  "UK": ["London", "Manchester", "Liverpool", "Birmingham", "Leeds"],
  "Canada": ["Toronto", "Vancouver", "Montreal", "Calgary", "Ottawa"],
  "Australia": ["Sydney", "Melbourne", "Brisbane", "Perth", "Adelaide"]
};

// Computed: available cities depend on selected country
self.availableCities = ko.computed(function() {
  return self.selectedCountry() ? self.citiesData[self.selectedCountry()] : [];
});

  // image Upload Setup
  self.cnicImage = ko.observable("src/css/images/nic.svg");  

    // Example: Change dynamically (for testing)
    
      /**
       * Optional ViewModel method invoked after the View is inserted into the
       * document DOM.  The application can put logic that requires the DOM being
       * attached here.
       * This method might be called multiple times - after the View is created
       * and inserted into the DOM and after the View is reconnected
       * after being disconnected.
       */
      this.connected = () => {
        accUtils.announce('EditPage page loaded.', 'assertive');
        document.title = "EditPage";
        // Implement further logic if needed
      };

      /**
       * Optional ViewModel method invoked after the View is disconnected from the DOM.
       */
      this.disconnected = () => {
        // Implement if needed
      };

      /**
       * Optional ViewModel method invoked after transition to the new View is complete.
       * That includes any possible animation between the old and the new View.
       */
      this.transitionCompleted = () => {
        // Implement if needed
      };
    }

    /*
     * Returns an instance of the ViewModel providing one instance of the ViewModel. If needed,
     * return a constructor for the ViewModel so that the ViewModel is constructed
     * each time the view is displayed.
     */
    return EditPageViewModel;
  }
);
