/**
 * @license
 * Copyright (c) 2014, 2025, Oracle and/or its affiliates.
 * Licensed under The Universal Permissive License (UPL), Version 1.0
 * as shown at https://oss.oracle.com/licenses/upl/
 * @ignore
 */
define(['knockout', '../accUtils'], 
function(ko, accUtils) {
  function EditPageViewModel(params) {
    var self = this;
    const { router } = params;

    // 🔙 Go Back
    self.goBack = () => {
      router.go({ path: "myprofile" });
    };

    // Observables (form fields)
    self.id = ko.observable();
    self.contactNumber = ko.observable("");
    self.email = ko.observable("");
    self.address = ko.observable("");
    self.selectedCountry = ko.observable("");
    self.selectedCity = ko.observable("");
    self.cnicImage = ko.observable("src/css/images/nic.svg"); // default

    // Country & city data
    self.countries = ["Pakistan", "USA", "UK", "Canada", "Australia"];
    self.citiesData = {
      "Pakistan": ["Karachi", "Lahore", "Islamabad", "Multan", "Peshawar"],
      "USA": ["New York", "Los Angeles", "Chicago", "Houston", "San Francisco"],
      "UK": ["London", "Manchester", "Liverpool", "Birmingham", "Leeds"],
      "Canada": ["Toronto", "Vancouver", "Montreal", "Calgary", "Ottawa"],
      "Australia": ["Sydney", "Melbourne", "Brisbane", "Perth", "Adelaide"]
    };

    // Computed cities based on country
    self.availableCities = ko.computed(function() {
      return self.selectedCountry() ? self.citiesData[self.selectedCountry()] : [];
    });

    // 📥 Load data from localStorage (auto-fill)
    self.loadFromLocalStorage = function() {
      const storedUser = localStorage.getItem("currentUser");
      if (storedUser) {
        const user = JSON.parse(storedUser);
        self.id(user.id);
        self.contactNumber(user.registeredContactNumber || "");
        self.email(user.registeredEmailAddress || "");
        self.address(user.registeredHomeAddress || "");
        self.selectedCountry(user.country || "");
        self.selectedCity(user.city || "");
        
      }
    };

    // 📞 Contact Number Validation
    self.contactError = ko.computed(function() {
      var value = self.contactNumber().trim();
      if (/[a-zA-Z]/.test(value)) return "Invalid number: letters not allowed";
      var raw = value.replace(/\D/g, "");
      if (raw.length > 0 && raw.length !== 11)
        return "Number must be 11 digits";
      return "";
    });

    // 📧 Email Validation
    self.emailError = ko.computed(function() {
      var value = self.email().trim();
      if (!value) return "";
      var emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
      return emailPattern.test(value) ? "" : "Enter valid email format";
    });

    // 🏠 Address Validation (25–30 chars)
    self.addressError = ko.computed(function() {
      var value = self.address().trim();
      if (!value) return "";
      if (value.length < 25 || value.length > 30)
        return "Address must be 25–30 characters long";
      return "";
    });

    // ✅ Form Validity (for enabling Next button)
    self.isFormValid = ko.computed(function() {
      return (
        self.contactNumber().trim().length > 0 &&
        self.contactError() === "" &&
        self.email().trim().length > 0 &&
        self.emailError() === "" &&
        self.address().trim().length > 0 &&
        self.addressError() === "" &&
        self.selectedCountry() &&
        self.selectedCity()
      );
    });

    // 🎨 Computed for button CSS class (light/dark)
    self.nextButtonClass = ko.computed(function() {
      return self.isFormValid() ? "next-btn-dark" : "next-btn-light";
    });

    // 🚀 Update profile API call and move to OTP screen
    self.goNext = async function() {
      if (!self.isFormValid()) {
        alert("Please fill all fields correctly before proceeding!");
        return;
      }

      const payload = {
        id: self.id(),
        registeredContactNumber: self.contactNumber(),
        registeredEmailAddress: self.email(),
        registeredHomeAddress: self.address(),
        country: self.selectedCountry(),
        city: self.selectedCity()
      };

      try {
        const response = await fetch(`http://localhost:8080/update/${self.id()}`, {
          method: "PUT",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify(payload)
        });

        if (!response.ok) throw new Error("Failed to update profile");

        console.log("Profile updated successfully!");
        router.go({ path: "otpscreen" });
      } catch (err) {
        console.error("Update failed:", err);
        alert("Error updating profile. Please try again.");
      }
    };

    // 🔄 On page load
    this.connected = () => {
      accUtils.announce('Edit Page loaded.', 'assertive');
      document.title = "Edit Profile";
      self.loadFromLocalStorage();
    };

    this.disconnected = () => {};
    this.transitionCompleted = () => {};
  }

  return EditPageViewModel;
});
