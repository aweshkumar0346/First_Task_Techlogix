/**
 * @license
 * Copyright (c) 2014, 2025, Oracle and/or its affiliates.
 * Licensed under The Universal Permissive License (UPL), Version 1.0
 * as shown at https://oss.oracle.com/licenses/upl/
 * @ignore
 */
define(["knockout", "../accUtils"], function (ko, accUtils) {
  function EditPageViewModel(params) {
    var self = this;
    const { router } = params;

    // 🔙 Go Back
    self.goBack = () => {
      router.go({ path: "myprofile" });
    };

    // Observables (form fields)
    self.cnic = ko.observable();
    self.contactNumber = ko.observable("");
    self.email = ko.observable("");
    self.address = ko.observable("");
    self.selectedCountry = ko.observable("");
    self.selectedCity = ko.observable("");
    self.cnicExpirationDate = ko.observable();
    self.cnicImage = ko.observable("src/css/images/nic.svg"); // default

    // Country & city data
    self.countries = ["Pakistan", "USA", "UK", "Canada", "Australia"];
    self.citiesData = {
      Pakistan: ["Karachi", "Lahore", "Islamabad", "Multan", "Peshawar"],
      USA: ["New York", "Los Angeles", "Chicago", "Houston", "San Francisco"],
      UK: ["London", "Manchester", "Liverpool", "Birmingham", "Leeds"],
      Canada: ["Toronto", "Vancouver", "Montreal", "Calgary", "Ottawa"],
      Australia: ["Sydney", "Melbourne", "Brisbane", "Perth", "Adelaide"],
    };

    // Computed cities based on country
    self.availableCities = ko.computed(function () {
      return self.selectedCountry()
        ? self.citiesData[self.selectedCountry()]
        : [];
    });

    self.loadFromLocalStorage = function () {
  const pending = localStorage.getItem("pendingUpdate");
  const current = localStorage.getItem("currentUser");
  const source = localStorage.getItem("editSource"); // 👈 who sent us here

  // 🧠 Decision logic
  if (source === "otpscreen" && pending) {
    console.log("📦 Loading pending edit (back from OTP screen)");
    try {
      const data = JSON.parse(pending);
      self.cnic(data.cnic);
      self.contactNumber(data.registeredContactNumber || "");
      self.email(data.registeredEmailAddress || "");
      self.address(data.registeredHomeAddress || "");
      self.selectedCountry(data.country || "");
      self.selectedCity(data.city || "");
    } catch (e) {
      console.error("⚠️ Failed to parse pendingUpdate:", e);
    }
  } else if (current) {
    console.log("📦 Loading profile data (from MyProfile)");
    try {
      const user = JSON.parse(current);
      self.cnic(user.cnic);
      self.contactNumber(user.registeredContactNumber || "");
      self.email(user.registeredEmailAddress || "");
      self.address(user.registeredHomeAddress || "");
      self.selectedCountry(user.country || "");
      self.selectedCity(user.city || "");
      self.cnicExpirationDate(user.cnicExpirationDate || "");
    } catch (e) {
      console.error("⚠️ Failed to parse currentUser:", e);
    }
  } else {
    console.warn("⚠️ No local profile data found in storage.");
  }

  // Clear the flag after using it
  localStorage.removeItem("editSource");
};

    self.formattedId = ko.computed(function () {
      const raw = String(self.cnic() || "").trim();
      if (raw.length !== 13) return raw;
      return `${raw.slice(0, 5)}-${raw.slice(5, 12)}-${raw.slice(12)}`;
    });

    // Contact Number Setup
    self.contactNumber = ko.observable("");

    // Subscribe: auto-clean + format
    self.contactNumber.subscribe(function (newValue) {
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
    self.contactError = ko.computed(function () {
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
      if (raw.length < 11) {
        return "Contact number must be exactly 11 digits";
      }
      if (raw.length > 11) {
        return "Too many digits! Must be 11 digits";
      }
      return ""; // valid
    });

    // 📧 Email Validation
    self.emailError = ko.computed(function () {
      var value = self.email().trim();
      if (!value) return "";
      var emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
      return emailPattern.test(value) ? "" : "Enter valid email format";
    });

    // 🏠 Address Validation (25–30 chars)
    self.addressError = ko.computed(function () {
      var value = self.address().trim();
      if (!value) return "";
      if (value.length < 25 || value.length > 30)
        return "Address should have 25-30 characters ";
      return "";
    });

    // ✅ Form Validity (for enabling Next button)
    self.isFormValid = ko.computed(function () {
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
    self.nextButtonClass = ko.computed(function () {
      return self.isFormValid() ? "next-btn-dark" : "next-btn-light";
    });

    // 🚀 Update profile API call and move to OTP screen
    // 🚀 Generate OTP before update
    self.goNext = async function () {
      if (!self.isFormValid()) {
        alert("Please fill all fields before proceeding!");
        return;
      }

      const payload = {
        cnic: self.cnic(),
        registeredContactNumber: self.contactNumber(),
        registeredEmailAddress: self.email(),
        registeredHomeAddress: self.address(),
        country: self.selectedCountry(),
        city: self.selectedCity(),
      };

      try {
        const response = await fetch("http://localhost:8080/api/request-otp", {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify(payload),
        });

        if (!response.ok) throw new Error("Failed to generate OTP");

        const data = await response.json();
        console.log("OTP requested for:", data.email);

        // ✅ Save pending update and email for verification
        localStorage.setItem("pendingUpdate", JSON.stringify(payload));
        localStorage.setItem("otpEmail", self.email()); // ✅ fixed key

        alert("OTP sent! Please enter it on the next screen.");
        router.go({ path: "otpscreen" });
      } catch (err) {
        console.error(err);
        alert("Error generating OTP. Please try again.");
      }
    };

    // 🔄 On page load
    this.connected = () => {
      accUtils.announce("Edit Page loaded.", "assertive");
      document.title = "Edit Profile";
      self.loadFromLocalStorage();
    };

    this.disconnected = () => {};
    this.transitionCompleted = () => {};
  }

  return EditPageViewModel;
});
